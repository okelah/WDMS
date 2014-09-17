package controller;
 
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import utils.validation;

import DAO.Directory;
import DAO.Logger;
import DAO.User;

@Controller
@RequestMapping(value="/directory")
public class DirectoryController
{
	@Autowired
	Logger logger;
	
	User currentUser = null;

	@RequestMapping(value="create",method=RequestMethod.POST)
	protected void createDirectory(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("name") String directoryName,
			@RequestParam("location") String location) throws Exception
	{
		User currentUser = (User)request.getSession(true).getAttribute("user");
		String dirname = directoryName;
		int parent_id;
		String currentDir = "";
		
		try
		{
			if(!validation.validateAlphanumeric(dirname, 255))
			{
				throw new Exception("Invalid directory name");
			}
			
			if(!validation.validateNumber(location, 11))
			{
				throw new Exception("Invalid location");
			}
			
			try
			{
				parent_id = Integer.parseInt(location); 
			}
			catch(Exception e)
			{
				parent_id = Directory.getRootDirectory(1).getDirectoryID();
			}
			
			if(Directory.getDirectory(parent_id).isOwner(currentUser.getUserId()))
			{
				Directory d = new Directory(currentUser.getUserId(), dirname, parent_id);
				response.sendRedirect("/cse545_WBDS/document?dir=" + parent_id);
				Logger.storeLog("DirectoryController.createDirectory - Directory created sucessfully "
						+ d.getDirectoryID(), 1, currentUser.getUserId());
			}
			else
			{
				response.sendRedirect("/cse545_WBDS/unauthorized");
				Logger.storeLog("DirectoryController.createDirectory - Unauthorized access to directory "
						+ parent_id, 3, currentUser.getUserId());
			}
		}
		catch(Exception e)
		{
			response.sendRedirect("/cse545_WBDS/error");
			Logger.storeLog("DirectoryController.createDirectory - Exception occured: "
					+ e.getMessage(), 2, currentUser.getUserId());
		}
	}
	
	@RequestMapping(value="delete", method=RequestMethod.GET)
	protected void deleteDirectory(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException
	{
		User currentUser = (User)request.getSession(true).getAttribute("user");
		String currentDir = "";
		try
		{
			if(!validation.validateNumber(request.getParameter("dir"), 11))
			{
				throw new Exception("Invalid current directory");
			}
			
			if(!validation.validateNumber(request.getParameter("current"), 11))
			{
				throw new Exception("Invalid current directory");
			}
			
			currentDir = request.getParameter("current");

			int dirid = Integer.parseInt(request.getParameter("dir"));
			
			Directory d = Directory.getDirectory(dirid);
			if(d.isOwner(currentUser.getUserId()))
			{
				d.Delete();
				response.sendRedirect("/cse545_WBDS/document?dir=" + currentDir);
				Logger.storeLog("DirectoryController.deleteDirectory - Directory deleted sucessfully"
						+ currentDir, 1, currentUser.getUserId());
			}
			else
			{
				response.sendRedirect("/cse545_WBDS/unauthorized");
				Logger.storeLog("DirectoryController.deleteDirectory - Unauthorized access to directory "
						+ currentDir, 3, currentUser.getUserId());
			}
		}
		catch(Exception e)
		{
			response.sendRedirect("/cse545_WBDS/error");
			Logger.storeLog("DirectoryController.deleteDirectory - Exception occured: "
					+ e.getMessage(), 2, currentUser.getUserId());
		}
	}
}