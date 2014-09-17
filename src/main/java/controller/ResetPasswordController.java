package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import utils.validation;

import DAO.Login;

@Controller
@RequestMapping(value = "/resetpassword")
public class ResetPasswordController {

	@Autowired
	private Login login;

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView forgotpassword(HttpSession session, @RequestParam("id") String id,
			@RequestParam("inputNewPassword") String newPassword) {
		ModelAndView view = new ModelAndView("login");
		
		if(!validation.validatePassword(newPassword)){
			
			ModelAndView view1 = new ModelAndView("resetpassword");
			
			session.setAttribute("message", "Password should be Alphanumeric,8-16 characters,"
					+ " no whitespaces, atleast - 1 lowercase & 1upper case & 1 non alphanumeric character");

			session.setAttribute("id", id);

			return view1;
 
		}

		if (login.resetPassword(id,newPassword)) {
			view.addObject("message", "Password reset successfully");
		} else {
			view.addObject("message", "Invalid opertaions");
		}
		
		return view;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getForgotPasswordPage(HttpSession session, @RequestParam("id") String id) {
		ModelAndView view = new ModelAndView("resetpassword");

		session.setAttribute("id", id);

		return new ModelAndView("resetpassword");
	}

}
