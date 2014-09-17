package controller;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import utils.validation;
import DAO.Register;

@Controller
@RequestMapping(value = "/register")
public class UserAccountCreateController {

	@Autowired
	private Register register;

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView register(HttpServletRequest httpServletRequest,
			@RequestParam(value = "selectionField") String accountType,
			@RequestParam(value = "departmentField") String deptId,
			@RequestParam(value = "inputPassword") String pass,
			@RequestParam(value = "inputEmail") String username)
			throws Exception {
		String password = pass;
		ModelAndView retval;
		if (!validation.validateEmail(username)) {
			retval = new ModelAndView("createaccount");
			retval.addObject("disablecap", true);
			retval.addObject("message", "Invalid Email");
			
			return retval;
		}

		if (!validation.validatePassword(pass)) {
			retval = new ModelAndView("createaccount");
			retval.addObject("disablecap", true);
			retval.addObject("message", "Password did NOT meet complexity requirements!");
			
			return retval;

		}
		
		ReCaptchaImpl reCaptcha = new ReCaptchaImpl();  
        reCaptcha.setPrivateKey("6Lc449gSAAAAAEgu0y1IlycSb4i4s2VVnrhpdXsy"); 
        String remoteAddr = httpServletRequest.getRemoteAddr();  
        String challengeField = httpServletRequest.getParameter("recaptcha_challenge_field");  
        String responseField = httpServletRequest.getParameter("recaptcha_response_field");  
        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challengeField, responseField);  
  
        if(!reCaptchaResponse.isValid())  
        {  
        	retval = new ModelAndView("createaccount");
        	retval.addObject("disablecap", true);
			retval.addObject("message", "Captcha Invalid");
			return retval;
        }  

		if (register.createAccount(username, password, accountType, deptId)) {
			return new ModelAndView("login");
		} else {
			retval = new ModelAndView("createaccount");
			retval.addObject("disablecap", true);
			return retval;
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getRegisterPage() {
		ModelAndView a = new ModelAndView("createaccount");
		a.addObject("disablecap", true);
		return a;
	}

}
