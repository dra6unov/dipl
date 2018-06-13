package googleDrive;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.http.javanet.NetHttpTransport;

import repos.AuthRepo;

@Controller
@RequestMapping("/google/*")
public class GoogleDriveMain {
	
	@Autowired
	GoogleDriveService driveService;
	
	@Autowired
	AuthRepo authRepo;
	
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

}
