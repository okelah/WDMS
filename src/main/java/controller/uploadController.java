package controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import DAO.*;

@Controller
@RequestMapping(value="/upload")
public class uploadController 
{	
	@Autowired 
	Document doc;
	
	@Autowired
	Directory dir;
	
	@Autowired
	Authorization authorization;
	
	@Autowired
	Logger logger;
	
	User currentUser = null;
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView viewDocuments(HttpServletRequest httpServletRequest, HttpServletResponse response) throws ServletException, IOException, SQLException, ClassNotFoundException 
	{
		User currentUser = (User)httpServletRequest.getSession(true).getAttribute("user");
		ModelAndView view = new ModelAndView("upload");
		String location = httpServletRequest.getParameter("dir");
		int dirid = -1;
		view.addObject("currentUser", currentUser.getUserId());
		view.addObject("uploaddir", location);
		
		//Logger.storeLog("DocumentController.viewDocuments - User " + currentUser.getUserId() + " accessed directory " + location, 
		//		1, currentUser.getUserId());
		
		try
		{
			dirid = Integer.parseInt(location);
		}
		catch(Exception ex)
		{
			dirid = -1;
		}
		
		if(location == null || location == "" || dirid == -1 || dirid == 1)
	    {
			//Get own root directory
	    	//Directory dir = Directory.getRootDirectory(currentUser);
	    	if(Directory.getRootDirectory(currentUser.getUserId()).getDirectoryID() == -1)
	    	{
	    		//Create Root Directory
	    		new Directory(currentUser.getUserId(), "root");
	    	//	Logger.storeLog("Created root directory for user id" + currentUser.getUserId(), 
	    	//			1, currentUser.getUserId());
	    	}
	    	//Get other root directories have access to
	    	Directory[] roots = null;
			try {
				roots = Directory.getRootDirectories(currentUser.getUserId());
			} catch (Exception e) {
	
			}
	    	view.addObject("directories", roots);
	    	view.addObject("parentDirectory", -1);
	    }
		else 
	    {
			try
			{
				Directory currentDirectory = Directory.getDirectory(dirid);
				
				view.addObject("parentDirectory", currentDirectory.getParentDirectoryID());
			}
			catch (Exception E)
			{
				response.sendRedirect("unauthorized");
			}
	    }
		view.addObject("auth", authorization);
		return view;
	}
	
}