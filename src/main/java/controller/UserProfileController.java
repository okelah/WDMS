package controller;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import DAO.DBQueries;
import DAO.Logger;
import DAO.Login;
import DAO.Role;
import DAO.Sha;
import DAO.User;
import DAO.UserDAO;

import utils.validation;

@Controller
@RequestMapping(value = "/profile")
public class UserProfileController{
	
	@Autowired
	Logger logger;
	
	@Autowired
	private Login login;
	
	@Autowired
	private UserDAO userdao;
	
	private PrintWriter out;
	
	User currentUser = null;
	
//	@RequestMapping(method = RequestMethod.GET)
//	public ModelAndView userprofile(HttpServletRequest httpServletRequest)	throws Exception {
//				return new ModelAndView ("userprofile");
//			}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getUserprofilePage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User currentUser = (User)request.getSession(true).getAttribute("user");
		Role currentRole=userdao.getRole(currentUser.getUserId());
		
		ModelAndView view = new ModelAndView("userprofile");
		return view;
	}
	
	@RequestMapping(value="modifypswd",method=RequestMethod.POST)
	protected void modifypassword(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("inputOldPassword") String OldPassword,
			@RequestParam("inputNewPassword") String NewPassword) throws Exception{
		User currentUser = (User)request.getSession(true).getAttribute("user");
		

		if (!login.validLoginCredential(currentUser.getEmailId(), OldPassword)){
			Integer count = (Integer)request.getSession(true).getAttribute("mdfcount");
			if(count==null){
				count=0;
			}
			
			count++;
			request.getSession(true).setAttribute("mdfcount", count);
			if(count<3)
			{
				request.getSession(true).setAttribute("message","Your password is wrong");
//				view.addObject("message", "Your password is wrong");
				response.sendRedirect("/cse545_WBDS/profile");
			}else{
			
				//TODO: needs to deactivate account
				request.getSession(true).invalidate();
				response.sendRedirect("/cse545_WBDS/login");
			}
			
		} else{
			
			if (!validation.validatePassword(NewPassword)) {

				request.getSession(true).setAttribute("message","Password should be Aplhanumeric.  It must contain 8-16 characters," +
						" with no whitespaces, and at least - 1 lowercase & 1 upper case & 1 non-alphanumeric character");
				response.sendRedirect("/cse545_WBDS/profile");
				
			}else{
				//TODO: Should we force a logout?
				request.getSession(true).invalidate();
				String hash = Sha.hashCreator(NewPassword);
				userdao.modifyUserPswd(currentUser.getUserId(), hash);
				response.sendRedirect("/cse545_WBDS/login");
			}
			
		}
		
	}
		
}