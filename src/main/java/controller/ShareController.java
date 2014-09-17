package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import utils.validation;

import DAO.*;

@Controller
@RequestMapping(value="/shareasdf")
public class ShareController 
{	
	User currentUser = null;
	
	@Autowired
	Authorization authorization;
	
	@Autowired 
	Document doc;
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView shareDocuments(HttpSession session, HttpServletRequest httpServletRequest, HttpServletResponse response, @RequestParam("shareemail") String email, @RequestParam("filenames") String docguid) throws ServletException, IOException, SQLException, ClassNotFoundException 
	{
		boolean fileGUIDsValid = false; 
		List<Document> sharingFiles = new ArrayList<Document>();
		
		currentUser = (User)httpServletRequest.getSession(true).getAttribute("user");
		ModelAndView view = null;	
		// Check if we can share document
		if ( validation.validateFilenameGUID(docguid) && authorization.canShare(docguid, currentUser.getUserId()) )
		{
			view = new ModelAndView("share");
			view.addObject("email", email);
			
			
			
		}
		else
		{
			view = new ModelAndView("unauthorized");
		}
		
		
		//view.addObject("user", user);
		
		/*String fileList = httpServletRequest.getParameter("files");
		
		if(fileList != null && fileList.length() > 0)
	    {
			// Split file names by semicolon
			String[] GUIDList = fileList.split(";");
			
			// GUID length=36 chars type=hex
			// validate GUID
			for(int i=0; i < GUIDList.length; i++)
			{	
				if (validation.validateFilenameGUID(GUIDList[i]) && authorization.canShare(GUIDList[i], currentUser))
				{	
					sharingFiles.add(Document.getDocument(GUIDList[i]));
				}
			}
			view.addObject("sharingdocuments", sharingFiles);
	    	
	    }
		else
		{
			//TODO: Not sure if this will work right.  TEST THIS
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must specify some file(s) to share");
			
		}*/
		
		return view;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView shareDocuments(HttpSession session, HttpServletRequest httpServletRequest, HttpServletResponse response) throws ServletException, IOException, SQLException, ClassNotFoundException 
	{
		/*boolean fileGUIDsValid = false; 
		List<Document> sharingFiles = new ArrayList<Document>();
		
		
		ModelAndView view = new ModelAndView("share");
		
		String fileList = httpServletRequest.getParameter("files");
		
		if(fileList != null && fileList.length() > 0)
	    {
			// Split file names by semicolon
			String[] GUIDList = fileList.split(";");
			
			// GUID length=36 chars type=hex
			// validate GUID
			for(int i=0; i < GUIDList.length; i++)
			{	
				if (validation.validateFilenameGUID(GUIDList[i]) && authorization.canShare(GUIDList[i], currentUser))
				{	
					sharingFiles.add(Document.getDocument(GUIDList[i]));
				}
			}
			view.addObject("sharingdocuments", sharingFiles);
	    	
	    }
		else
		{
			//TODO: Not sure if this will work right.  TEST THIS
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must specify some file(s) to share");
			
		}
		
		return view;*/
		return new ModelAndView("share");
	}
}