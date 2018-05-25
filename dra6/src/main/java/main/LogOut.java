package main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogOut {
	@RequestMapping("/logout")
	private String logOut(HttpServletRequest request, HttpServletResponse response, Model model) {
		HttpSession session = request.getSession();
		if(session!=null) {
			session.invalidate();
		}
		return "reg";
	}

}
