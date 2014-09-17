package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import DAO.DBWrapper;
import DAO.Login;
import DAO.Role;
import DAO.User;
import DAO.UserDAO;
import DAO.SystemAdmin;


@Controller
@RequestMapping(value = "/login")
public class LoginController {
	
	@Autowired
	private Login login;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private DBWrapper sysadminDbWrapper;
	
	@RequestMapping(method = RequestMethod.POST)
	public String login(HttpSession session, HttpServletRequest httpServletRequest, @RequestParam("username") String email,
			@RequestParam("password") String password) throws Exception {
		
		ReCaptchaImpl reCaptcha = new ReCaptchaImpl();  
        reCaptcha.setPrivateKey("6Lc449gSAAAAAEgu0y1IlycSb4i4s2VVnrhpdXsy"); 
        String remoteAddr = httpServletRequest.getRemoteAddr();  
        String challengeField = httpServletRequest.getParameter("recaptcha_challenge_field");  
        String responseField = httpServletRequest.getParameter("recaptcha_response_field");  
        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challengeField, responseField);  
        ModelAndView retval;
        if(!reCaptchaResponse.isValid())  
        {  
			return "redirect:/";
        }  
		
		if (login.validLoginCredential(email, password)) {
			httpServletRequest.getSession(true).setAttribute("count",0);
			// Create a new user object
			User u=userDAO.getUser(email);
			session.setAttribute("user", u);
			session.setMaxInactiveInterval(5*60);
			if(u.getRole()==Role.SysAdmin){
				
				return "redirect:/sysadmin";
			}else{
				return "redirect:/";
			}
		} else {
				Integer count = (Integer)httpServletRequest.getSession(true).getAttribute("count");
			if(count==null){
				count=0;
			}
			count++;
			httpServletRequest.getSession(true).setAttribute("count", count);
			if(count<5)
			{
				return "redirect:/login/error";
			}else{
				session.setAttribute("count", 0);
				if(userDAO.isActive(email))
				{
					SystemAdmin systemAdmin = new SystemAdmin();
					systemAdmin.setUserId(-1);
					systemAdmin.setEmailId("fake@email.com");
					systemAdmin.setActive(true);
					systemAdmin.setRole(Role.SysAdmin);
					systemAdmin.setDbWrapper(sysadminDbWrapper);
					systemAdmin.deactivateUser(email);
					//TODO: Somehow call the systemAdmin function to disable the account.
				}
				String msg="You have input wrong password for 5 times, this account hass been locked";
				session.setAttribute("message", msg);
				return "redirect:/unauthorized";
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.POST, value="error")
	public ModelAndView loginError(HttpSession session){
		ModelAndView view = new ModelAndView("login");
		view.addObject("error", true);
		return view;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getLoginPage() {

		return new ModelAndView("login");
	}
	
	@RequestMapping(method = RequestMethod.GET,value="signout")
	public String signout(HttpSession session){
		
		session.invalidate();
		
		return "redirect:/login";
	}
	
	

}
