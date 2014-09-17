package controller;

import java.sql.SQLException;
import utils.validation;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import DAO.Document;
import DAO.Login;
import DAO.User;
import DAO.UserDAO;
import DAO.Authorization;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/revisions")
public class RevisionController {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired 
	Document doc;
	
	@Autowired
	Authorization authorization;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView revisionhistory(HttpSession session, HttpServletRequest httpServletRequest) throws SQLException {
		// TODO decide what to do with exceptions
		ModelAndView view = new ModelAndView("revisionhistory");
		
		User currentUser = (User)httpServletRequest.getSession(true).getAttribute("user");
		
		String groupid = httpServletRequest.getParameter("groupid");
		Integer groupidInt = Integer.parseInt(groupid);
		
		Document[] d = Document.getDocumentRevisionList(groupidInt);
		
		if ((d.length > 0) && (authorization.canRead(d[0].getDocguid(), currentUser.getUserId(), groupidInt)) )
		{
			view.addObject("revisions", d);
		}
		else
		{
			view.addObject("errormessage", "Not authorized to view these specific revisions");
			Document[] empty = null;
			view.addObject("revisions", empty);
		}
		
		return view;
	}
	
	

}
