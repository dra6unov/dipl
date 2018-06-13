package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import entity.Auth;
import entity.User_inf;
import repos.AuthRepo;
import repos.User_groupRepo;
import repos.User_infRepo;

@Controller
public class RegistrationController {
	@Autowired
	private User_infRepo infRepo;

	@Autowired
	private User_groupRepo groupRepo;

	@Autowired
	private AuthRepo authRepo;

	@RequestMapping(value = "/reg", method = RequestMethod.GET)
	public String showRegPage(Model model) {
		return "reg";
	}

	@RequestMapping(value = "/reg", method = RequestMethod.POST)
	public String newUser(
			@RequestParam String login, 
			@RequestParam String pass, 
			@RequestParam String first_name,
			@RequestParam String last_name, 
			@RequestParam String email,
			Model model) {
		Long group = (long) 1;
		Auth auth = new Auth(login, pass, null, null);
		authRepo.save(auth);
		User_inf inf = new User_inf(first_name, last_name, auth.getId(), group, email);
		infRepo.save(inf);
		// System.out.println("");
		model.addAttribute("grats", "Поздравляем с успешной регистрацией");
		return "reg";
	}

	@RequestMapping("/au")
	public String authorize(@RequestParam String login, @RequestParam String pass, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Auth> log = authRepo.findByLogin(login);
		List<Auth> passlog = authRepo.findByLoginAndPass(login, pass);
		if (passlog.isEmpty()) {
			if (log.isEmpty()) {
				model.addAttribute("error", "Пользователь не зарегистрирован");
				return "reg";
				//response.sendRedirect("/reg");
			} else {
				model.addAttribute("error", "Неверный пароль");
				return "reg";
				//response.sendRedirect("/reg");
			}
		} else {
			List<Auth> id = authRepo.findByLogin(login);
			Long userId = id.get(0).getId();
			System.out.println("test: " + userId);
			
			HttpSession session = request.getSession();
			session.setAttribute("user", login);
			session.setAttribute("user_id", userId);
			
			System.out.println("Проверка id сессии: " + session.getAttribute("user_id").toString());
			
			model.addAttribute("aut", login);
			model.addAttribute("username", session.getAttribute("user").toString());
			//response.sendRedirect("/");
			return "redirect:/";
		}
	}

}
