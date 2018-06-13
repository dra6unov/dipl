package main;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.UploadErrorException;
import com.google.api.client.http.javanet.NetHttpTransport;

import dropBox.DropBoxService;
import entity.Dropbox_file;
import entity.Google_file;
import entity.User_file;
import googleDrive.GoogleDriveService;
import repos.AuthRepo;
import repos.Dropbox_fileRepo;
import repos.Google_fileRepo;
import repos.User_fileRepo;

@Controller
public class UploadController {
	
	@Autowired
	AuthRepo authRepo;
	
	@Autowired
	GoogleDriveService googleService;
	
	@Autowired
	DropBoxService dropboxService;
	
	@Autowired
	User_fileRepo fileRepo;
	
	@Autowired
	Google_fileRepo googleRepo;
	
	@Autowired
	Dropbox_fileRepo dropboxRepo;
	
	@RequestMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "GD", required = false) boolean GDValue,
			@RequestParam(value = "DP", required = false) boolean DPValue, 
			NetHttpTransport httpTransport,
			HttpSession session, Model model) throws IOException, UploadErrorException, DbxException {
		Long user_id = (Long) session.getAttribute("user_id");
		//List<User_file> data = fileRepo.find
		String name = file.getOriginalFilename();
		if(GDValue==true && DPValue==true) 
		{
			User_file user_file = new User_file(name, user_id);
			fileRepo.save(user_file);
			session.setAttribute("file_id", user_file.getId());
			googleService.googleUpload(httpTransport, file, session);
			dropboxService.uploadDropbox(file, session);
			//model.addAttribute("upload_res","Данные успешно загружены на облачные хранилища");
			return "redirect:/";
		}
		else if(GDValue==true)
		{
			User_file user_file = new User_file(name, user_id);
			fileRepo.save(user_file);
			session.setAttribute("file_id", user_file.getId());
			googleService.googleUpload(httpTransport, file, session);

			//dropboxService.uploadDropbox(file, session);
			Dropbox_file dropbox_file = new Dropbox_file((Long) session.getAttribute("file_id"), null);
			dropboxRepo.save(dropbox_file);
			//model.addAttribute("upload_res","Данные успешно загружены на Google Drive");
			//return "home";
			return "redirect:/";
		}
		else if(DPValue==true)
		{
			User_file user_file = new User_file(name, user_id);
			fileRepo.save(user_file);
			session.setAttribute("file_id", user_file.getId());
			dropboxService.uploadDropbox(file, session);
			
			//googleService.googleUpload(httpTransport, file, session);
			Google_file google_file = new Google_file((Long) session.getAttribute("file_id"), null);
			googleRepo.save(google_file);
			//model.addAttribute("upload_res","Данные успешно загружены на Dropbox");
			//return "home";
			return "redirect:/";
		}
		else
		{
			model.addAttribute("upload_res","Выберите хотя бы одно облачное хранилище");
			return "home";
		}
	}

}
