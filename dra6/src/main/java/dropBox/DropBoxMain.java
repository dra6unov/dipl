package dropBox;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.dropbox.core.*;
import com.dropbox.core.v2.files.UploadErrorException;

@Controller
@RequestMapping("/dropbox/*")
public class DropBoxMain {
	private static final Logger LOG = LoggerFactory.getLogger(DropBoxMain.class);
	
	@Autowired
	DropBoxService dropBoxService;
	
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
	
	@RequestMapping("/test")
	public String checkboxValue(
			@RequestParam(value="GD", required=false) boolean GDValue,
			@RequestParam(value="DP", required=false)boolean DPValue,
			@RequestParam("file") MultipartFile file, 
			HttpSession session,
			Model model) throws UploadErrorException, IOException, DbxException{
		if(DPValue==true) {
		dropBoxService.uploadDropbox(file, session);
		System.out.println("GD: " + GDValue);
		System.out.println("DP: " + DPValue);
		return "redirect:/";
		}
		else {
			model.addAttribute("text", "Не выбран дропбокс АУЕ фарту масти");
			System.out.println("GD: " + GDValue);
			System.out.println("DP: " + DPValue);
			return "home";
		}
		
	}
}
