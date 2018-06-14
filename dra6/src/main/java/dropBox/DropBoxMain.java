package dropBox;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;

import entity.Auth;
import entity.Dropbox_file;
import repos.AuthRepo;
import repos.Dropbox_fileRepo;

@Controller
@RequestMapping("/dropbox/*")
public class DropBoxMain {
	private static final Logger LOG = LoggerFactory.getLogger(DropBoxMain.class);
	
	@Autowired
	DropBoxService dropBoxService;
	
	@Autowired
	AuthRepo authRepo;
	
	@Autowired
	Dropbox_fileRepo dropboxRepo;
	
	@Value("${dropbox.app.sessionstore.key}")
    private String sessionStoreKey;
	
	@RequestMapping(value="/allow", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.TEMPORARY_REDIRECT)
	public void allowDropox(HttpServletRequest requset, HttpServletResponse response, HttpSession session) throws Exception {
		String redirectUrl = dropBoxService.generateAuthURL(requset.getSession());
		
		System.out.println("state: "+session.getValue("state"));
		
		System.out.println("url: " +redirectUrl);
		response.setHeader("Location", redirectUrl);
		System.out.println("" + session.getAttribute(sessionStoreKey));
		
		//dropBoxService.saveAccessDetails(parameterMap, session);
	}
	
	@RequestMapping(value="/check", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.TEMPORARY_REDIRECT)
	public void checkDropboxAllowAccessURL(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String redirectUrl = dropBoxService.saveAccessDetails(request.getParameterMap(), request.getSession());
		LOG.info("redirect to: " + redirectUrl);
		response.setHeader("Location", redirectUrl);
	}
	
	@RequestMapping("/1")
	public void checkAccount(HttpSession session) throws IllegalAccessException, DbxApiException, DbxException {
		dropBoxService.getDropboxFile(session);
	}
	
	@RequestMapping("/upload")
	public void uploadFileToDropbox(@RequestParam("file") MultipartFile file, HttpSession session) throws UploadErrorException, IOException, DbxException {
		dropBoxService.uploadDropbox(file, session);
	}
	
	@RequestMapping(value="/download")
	@ResponseBody
	public void downloadDropbox(@RequestParam(value="id") Long id, HttpSession session) throws DbxException, IOException {
		DbxSessionStore dbxSessionStore = new DbxStandardSessionStore(session, sessionStoreKey);
		DbxRequestConfig config = new DbxRequestConfig("dra6", Locale.getDefault().toString());
		
		Optional<Auth> data = authRepo.findById((Long) session.getAttribute("user_id"));
		String home = System.getProperty("user.home");
		String accessToken = data.get().getDropbox_token();
		
		LOG.info("accessToken from uploadDropbox: " + accessToken);
		
		Long fileId = id;
		DbxClientV2 client = new DbxClientV2(config, accessToken);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Optional<Dropbox_file> dropbox = dropboxRepo.findById(fileId);
		
		java.io.File file = new java.io.File(home +java.io.File.separator + "Downloads"+java.io.File.separator+dropbox.get().getUser_file().getFile_name());
		OutputStream outputStream = new FileOutputStream(home +java.io.File.separator + "Downloads"+java.io.File.separator+dropbox.get().getUser_file().getFile_name());
		FileMetadata metadata = client.files().downloadBuilder(dropbox.get().getFile_path()).download(outputStream);
		
		outputStream.close();
		
		
	}

}
