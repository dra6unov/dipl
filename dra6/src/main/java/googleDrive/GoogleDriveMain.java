package googleDrive;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import entity.Google_file;
import main.CustomProgressListener;
import repos.AuthRepo;
import repos.Google_fileRepo;

@Controller
@RequestMapping("/google/*")
public class GoogleDriveMain {
	
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	
	@Autowired
	GoogleDriveService driveService;
	
	@Autowired
	AuthRepo authRepo;
	
	@Autowired
	Google_fileRepo googleRepo;
	
	
	@RequestMapping(value="/allow", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.TEMPORARY_REDIRECT)
	public void allowGoogle(NetHttpTransport httpTransport, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		//driveService.generateAuthUrl(httpTransport);
		String url = driveService.googleAuthUrl(httpTransport);
		response.setHeader("Location", url);
		driveService.getRefreshToken(httpTransport, session);
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public String googleUpload(@RequestParam("file") MultipartFile file, NetHttpTransport httpTransport, HttpSession session) throws IOException {
		driveService.googleUpload(httpTransport, file, session);
		return "redirect:/";
	}
	
	@RequestMapping(value="/download", method = RequestMethod.POST)
	@ResponseBody
	public void googleDownload(@RequestParam(value="id") Long id, NetHttpTransport httpTransport, HttpSession session, HttpServletResponse response) throws IOException {
		System.out.println("ajax send id: " + id);
		/*Long fileId = id;
		Optional<Google_file> google = googleRepo.findById(fileId);
		System.out.println("google: " + google.get().getUser_file().getFile_name());*/
		//driveService.googleDownload(httpTransport, session, id, response);
		//return "redirect:/";
		Long fileId = id;
		Optional<Google_file> google = googleRepo.findById(fileId);
		if(!google.get().getFile_id().isEmpty()) {
		Credential cred = driveService.refreshAccessToken(httpTransport, session);
		Drive drive = new Drive.Builder(httpTransport, JSON_FACTORY, cred).setApplicationName("dra6").build();
		String home = System.getProperty("user.home");

		
		
		System.out.println("google: " + google);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		java.io.File file = new java.io.File(home +java.io.File.separator + "Downloads"+java.io.File.separator+google.get().getUser_file().getFile_name());
		drive.files().get(google.get().getFile_id()).executeMediaAndDownloadTo(baos);
		FileOutputStream fop;
		fop = new FileOutputStream(file);
		fop.write(baos.toByteArray());
		fop.flush();
		fop.close();
		file.deleteOnExit();
		
		System.out.println("google.get().getUser_file().getFile_name()): " + google.get().getUser_file().getFile_name());
		
		response.setHeader("Location", response+"?id="+google.get().getFile_id());
		response.setContentType("application/octet-stream");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", google.get().getUser_file().getFile_name()));
		OutputStream out = response.getOutputStream();
		FileInputStream inputStream = new FileInputStream(file);
		byte[] buffer = new byte[4096];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}
		out.flush();
		inputStream.close();
		out.close();
		}
		else {
			response.sendRedirect("/");
		}
	   /* OutputStream out = new FileOutputStream(google.get().getUser_file().getFile_name());

	    Drive.Files.Get request = drive.files().get(google.get().getFile_id());
	    request.getMediaHttpDownloader().setProgressListener(new CustomProgressListener());
	    request.executeMediaAndDownloadTo(out);
	    out.close();*/
		//response.setHeader("Location", response+"?id="+google.get().getFile_id());
	}

}
