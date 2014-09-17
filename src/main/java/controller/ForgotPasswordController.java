package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import DAO.Login;

@Controller
@RequestMapping(value = "/forgotpassword")
public class ForgotPasswordController {
	
	@Autowired
	private Login login;

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView forgotpassword(HttpSession session, HttpServletRequest httpServletRequest, @RequestParam("email") String email){
		ModelAndView view = new ModelAndView("forgotpassword");

		if (login.sendPasswordResetLink(email)) {
			view.addObject("emailsent", true);
		} else {
			view.addObject("invalidemail", true);
		}
		return view;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getForgotPasswordPage() {

		return new ModelAndView("forgotpassword");
	}
	
	

}
