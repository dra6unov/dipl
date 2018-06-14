package main;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import entity.User_file;
import entity.User_inf;
import repos.User_fileRepo;
import repos.User_infRepo;

@Controller
public class MainController {
	
	@Autowired
	User_fileRepo fileRepo;
	
	@Autowired
	User_infRepo userRepo;
	
	@RequestMapping("/")
	public String main(HttpServletRequest request, HttpServletResponse response, Model model) {
		HttpSession session = request.getSession();
		//String name = session.getAttribute("user").toString();
		if (session.getAttribute("user")==null) {
			model.addAttribute("grats", "Вы должны авторизироваться");
			return "reg";
		} else {
			String name = session.getAttribute("user").toString();
			Long userId = (Long) session.getAttribute("user_id");
			model.addAttribute("username", name);
			System.out.println("name: " + session.getAttribute("user"));
			System.out.println("userId: " + userId);
			
			List<User_file> user_files = fileRepo.findByFile(userId);
			System.out.println("files: " + user_files.get(1).getFile_name());
			
			//for()
			
			//Optional<User_inf> user_files = userRepo.findById(userId);
			//System.out.println("file list: " + user_files.get().getFiles());
			//Optional<User_file> fileList = fileRepo.findByFile(userId);
			//Optional<User_file> fileList = fileRepo.findByUser(userId);
			//System.out.println("file list: " + fileList.get().getUser_inf());
			//System.out.println("file list: " + fileList.get().getId());
			//System.out.println("file list: " + fileList);
			
			return "home";
		}
	}
	
}
