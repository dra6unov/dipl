package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import entity.Dropbox_file;
import entity.Google_file;
import entity.User_file;
import entity.User_inf;
import repos.Dropbox_fileRepo;
import repos.Google_fileRepo;
import repos.User_fileRepo;
import repos.User_infRepo;

@Controller
public class MainController {
	
	@Autowired
	User_fileRepo fileRepo;
	
	@Autowired
	User_infRepo userRepo;
	
	@Autowired
	Dropbox_fileRepo dropboxRepo;
	
	@Autowired
	Google_fileRepo googleRepo;
	
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
			
			List<String> filesName = new ArrayList<String>();
			
			List<User_file> user_files = fileRepo.findByFile(userId);
			System.out.println("size: " + user_files.size());
			
			
			
			Map<Long, String> map = new HashMap<>();
			//Map<Long, List<String>> map2 = new HashMap<Long, List<String>>();
			//MultiValueMap<Long, String> multiValueMap = (MultiValueMap<Long, String>) new HashMap();
			Map<Long, List<String>> map2 = new HashMap<Long, List<String>>();
					
			List<Long> filesId = new ArrayList<Long>();
			
			//List<String> list = new ArrayList<String>();
		
			for(int i=0;i<user_files.size();i++) {
				List<String> list = new ArrayList<String>();
				Optional<Dropbox_file> dropbox = dropboxRepo.findById(user_files.get(i).getId());
				Optional<Google_file> google = googleRepo.findById(user_files.get(i).getId());
				//list.clear();
				System.out.println("file: " + user_files.get(i).getFile_name());
				model.addAttribute("fileName", user_files.get(i).getFile_name());
				filesName.add(user_files.get(i).getFile_name());
				filesId.add(user_files.get(i).getId());
				map.put(user_files.get(i).getId(), user_files.get(i).getFile_name());
				
				list.add(user_files.get(i).getFile_name());
				list.add(google.get().getFile_id());
				System.out.println("google.get().getFile_id()" + google.get().getFile_id());
				list.add(dropbox.get().getFile_path());
				//list.add("google");
				//list.add("dropbox");
				map2.put(user_files.get(i).getId(), list);
				//map2.put(user_files.get(i).getId(), user_files.get(i).getFile_name());
				//map2.put(user_files.get(i).getId(), google.get().getFile_id());
				//map2.put(user_files.get(i).getId(), dropbox.get().getFile_path());
				//list.clear();
			}
			
			
			
			System.out.println("filesName: " + filesName);
			//model.addAttribute("filesName", filesName);
			//model.addAttribute("filesId", filesId);
			//model.addAttribute("fileValues", user_files);
			model.addAttribute("fileValues", map);
			model.addAttribute("map", map2);
			model.addAttribute("google","google");
			model.addAttribute("dropbox","dropbox");
			System.out.println("map: " + map);
			System.out.println("map2: " + map2);
			//System.out.println("list: " + list);
			//model.addAllAttributes(fileValue);
			//model.addAttribute("filesName", user_files.);
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
