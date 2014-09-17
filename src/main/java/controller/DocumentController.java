package controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;

import utils.validation;

import DAO.*;

@Controller
@RequestMapping(value="/document")
public class DocumentController 
{	
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired 
	Document doc;
	
	@Autowired
	Directory dir;
	
	@Autowired
	Authorization authorization;
	
	@Autowired
	Logger logger;
	
	User currentUser = null;
	private static final int BUFSIZE = 4096;
	private static final String DESTINATION_DIR_PATH ="c:\\tmp\\files\\";
	
	private static final String TMP_DIR_PATH = "c:\\tmp";
	private File tmpDir;
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView viewDocuments(HttpServletRequest httpServletRequest, HttpServletResponse response) throws ServletException, IOException, SQLException, ClassNotFoundException 
	{
		ModelAndView view = new ModelAndView("listdocuments");
		view.addObject("message", httpServletRequest.getAttribute("message"));

		try
		{
			User currentUser = (User)httpServletRequest.getSession(true).getAttribute("user");
			
			String location = httpServletRequest.getParameter("dir");
			String dir = httpServletRequest.getParameter("dir");
			if( (dir != null && !validation.validateNumber(dir, 11) ) && !location.equals("null"))
			{
				throw new Exception("Invalid current directory");
			}
			
			int dirid = -1;
			view.addObject("currentUser", currentUser.getUserId());
			view.addObject("currentLocation", location);
			
			Logger.storeLog("DocumentController.viewDocuments - User " + currentUser.getUserId() + " accessed directory " + location, 
					3, currentUser.getUserId());
			
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
		    		Logger.storeLog("Created root directory for user id" + currentUser.getUserId(), 
		    				1, currentUser.getUserId());
		    	}
		    	//Get other root directories have access to
		    	Directory[] roots = Directory.getRootDirectories(currentUser.getUserId());
		    	view.addObject("directories", roots);
		    	view.addObject("parentDirectory", -1);
		    }
			else 
		    {
				try
				{
					// getDirectory can throw an exception if invalid dirid.
					Directory currentDirectory = Directory.getDirectory(dirid);
				
					view.addObject("parentDirectory", currentDirectory.getParentDirectoryID());
					
			    	if(currentDirectory.hasReadAccess(currentUser.getUserId()))
			    	{
			    		//Get list of directories user the currentUser's current location
			    		Directory[] dirs = currentDirectory.getChildDirectories();
			    		view.addObject("directories", dirs);
			    		
					    //Get list of documents currentUser owns
			    		Document[] docs = Document.getDocumentListForDirectory(currentDirectory.getDirectoryID());
			    		view.addObject("documents", docs);
			    	}
			    	else
			    	{
				    	throw new Exception("not authorized");
			    	}
				}
				catch (Exception E)
				{
					response.sendRedirect("/cse545_WBDS/unauthorized");
			    	Logger.storeLog("DocumentController.viewDocuments - Unauthorized access to directory " + dirid, 
		    				3, currentUser.getUserId());
			    	//return null; //TODO: should do or no?
				}
		    }
			view.addObject("auth", authorization);
			return view;
		}
		catch(Exception ex)
		{
			httpServletRequest.setAttribute("message", "Invalid Key Length");
			response.sendRedirect("/cse545_WBDS/document");
			
			Logger.storeLog("DocumentController.viewDocuments - Exception occured: "
					+ ex.getMessage(),2 , currentUser.getUserId());
		}
		
		return view;
	}

	@RequestMapping(value="get", method=RequestMethod.POST)
	protected void getDocument(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException 
	{
		currentUser = (User)request.getSession(true).getAttribute("user");
		String filename = ""; 
		int flag=0;
		String docguid = request.getParameter("doc");
		String extension=null;
		String docId=null;
		String symmetricKey=request.getParameter("key");
		
		try
		{
			if(!validation.validateFilenameGUID(docguid))
			{
				throw new Exception("Invalid document guid");
			}
			
			if(!StringUtils.isNullOrEmpty(symmetricKey))     //symmetric key is not null
			{
				
				if(symmetricKey.length()!=16)
				{	
					response.sendRedirect("/cse545_WBDS/document");
					Logger.storeLog("DocumentController.getDocument - Symmetric Key Length is not 16 digits "
							+ docguid, 2, currentUser.getUserId());
				}
				else
				{
					flag=1;
					
				}
				
			}
			
			Document d = Document.getDocument(docguid);
			//Access Check

			if(d.getOwnerid() == currentUser.getUserId() || authorization.canRead(d.getDocguid(), currentUser.getUserId(), d.getGroupid()))
			{
				filename = d.getDocname();
				int length = 0;

				String path=DESTINATION_DIR_PATH + docguid;
				if(flag==1)
				{
					String decryptPath=DESTINATION_DIR_PATH + docId;
					//decryptPath=decryptPath.substring(0, decryptPath.indexOf(".")-1);
					System.out.println("DecryptPath:"+decryptPath);
					Encryption.copy(Cipher.DECRYPT_MODE, DESTINATION_DIR_PATH + docguid,decryptPath, symmetricKey);
					path=decryptPath;
				}
				File f = new File(path);
				ServletOutputStream outStream = response.getOutputStream();
				String mimetype  = "application/octet-stream";
				
				response.setContentType(mimetype);
				response.setContentLength((int)f.length());
				response.setHeader("Content-Disposition", "attachment;filename=\"" + filename +"\"");
				
				byte[] byteBuffer = new byte[BUFSIZE];
				DataInputStream in = new DataInputStream(new FileInputStream(f));
				while((in != null) && ((length = in.read(byteBuffer))!= -1))
				{
					outStream.write(byteBuffer, 0, length);
				}
				in.close();

				d.setLastAccessTime(new java.util.Date());
				d.setLastAccessUser(currentUser.getUserId());
				
				outStream.close();
				Logger.storeLog("DocumentController.getDocument - User " + currentUser.getUserId() + " downloaded document "
						+ docguid, 1, currentUser.getUserId());
				if(flag==1)
					f.delete();
			
			}
			else
			{
				response.sendRedirect("/cse545_WBDS/unauthorized");
				Logger.storeLog("DocumentController.getDocument - Unauthorized access to document "
						+ docguid, 3, currentUser.getUserId());
			}
		}
		catch(Exception ex)
		{
			response.sendRedirect("/cse545_WBDS/error");
			Logger.storeLog("DocumentController.getDocument - Exception occured: "
					+ ex.getMessage(), 2, currentUser.getUserId());
		}
	}
	
	@RequestMapping(value="upload", method=RequestMethod.POST)
	protected void uploadDocument(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		currentUser = (User)request.getSession(true).getAttribute("user");
		String symmetricKey=null;
		
		tmpDir = new File(TMP_DIR_PATH);
		if(!tmpDir.isDirectory()) 
		{
			throw new ServletException(TMP_DIR_PATH + " is not a directory");
		}
		
		int directoryId = -1; //get from session?
		java.util.Date createdDate = new java.util.Date();
		
		DiskFileItemFactory  fileItemFactory = new DiskFileItemFactory();
		fileItemFactory.setSizeThreshold(1*1024*1024); //1 MB
		fileItemFactory.setRepository(tmpDir);

		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
		
		try 
		{
			@SuppressWarnings("unchecked")
			List<FileItem> items = uploadHandler.parseRequest(request);
			Iterator<FileItem> itr = items.iterator();
			FileItem untrustedFile = null;
			String untrustedComment = "";
			while(itr.hasNext()) 
			{
				FileItem item = itr.next();
				if(!item.isFormField())  
				{
					untrustedFile = item;
				}
				else if(item.getFieldName().equals("comment"))
				{
					untrustedComment = item.getString();
				}
				else if(item.getFieldName().equals("key")){
					symmetricKey=item.getString();
				}
				else if(item.getFieldName().equals("location"))
				{
					try
					{
						directoryId = Integer.parseInt(item.getString()); 
					}
					catch(Exception e)
					{
						directoryId = Directory.getRootDirectory(currentUser.getUserId()).getDirectoryID();
					}
				} 
			}
			
			Document currentDoc = Document.getDocument(untrustedFile.getName(), directoryId);
			
			if((currentDoc == null && Directory.getDirectory(directoryId).isOwner(currentUser.getUserId())) ||
					(currentDoc != null && authorization.canUpload(currentDoc.getDocguid(), currentUser.getUserId(), currentDoc.getGroupid())))
			{
				new Document(untrustedFile, currentUser.getUserId(), untrustedComment, directoryId, createdDate,symmetricKey);
				String dirid = request.getParameter("dir");
				try
				{
					Integer.parseInt(dirid);
					response.sendRedirect("/cse545_WBDS/document?dir=" + dirid);
				}
				catch(Exception e)
				{
					response.sendRedirect("/cse545_WBDS/document");
				}
				
				Logger.storeLog("DocumentController.uploadDocument - Document upload sucessful "
						+ untrustedFile.getName(), 1, currentUser.getUserId());
			}
			else
			{
				//This user is not the owner of the directory, so cant create new docs
				response.sendRedirect("/cse545_WBDS/unauthorized");
				Logger.storeLog("DocumentController.uploadDocument - Unauthorized access to document "
						+ untrustedFile.getName(), 3, currentUser.getUserId());
			}
		} 
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
			Logger.storeLog("DocumentController.uploadDocument - SQLException occured: "
					+ e.getMessage(), 2, currentUser.getUserId());
			response.sendRedirect("/cse545_WBDS/error");
		}
		catch (Exception e)
		{
			if(e.getMessage() == "Not permitted to update document")
			{
				//This user is not authorized to update that document
				response.sendRedirect("/cse545_WBDS/unauthorized");
				Logger.storeLog("DocumentController.uploadDocument - Unauthorized access to document "
						, 2, currentUser.getUserId());
			}
			else
			{
				response.sendRedirect("/cse545_WBDS/error");
				Logger.storeLog("DocumentController.uploadDocument - Exception occured: "
						+ e.getMessage(), 2, currentUser.getUserId());
			}
		}
	}

	@RequestMapping(value="share", method=RequestMethod.POST) // 
	protected void shareDocument(HttpServletRequest request, HttpServletResponse response,  @RequestParam("shareemail") String email, @RequestParam("filenames") String docguid) throws Exception
	{
		currentUser = (User)request.getSession(true).getAttribute("user");
		try
		{
			User shareUser = userDAO.getUser(email);
			if ( validation.validateFilenameGUID(docguid) && authorization.canShare(docguid, currentUser.getUserId()) )
			{
				Document d = Document.getDocument(docguid);
				boolean readState = request.getParameter("read") != null;
				boolean updateState = request.getParameter("update") != null;
				boolean deleteState = request.getParameter("delete") != null;
				boolean lockState = request.getParameter("lock") != null;
				d.Share(currentUser.getUserId(), shareUser.getUserId(), readState, updateState, deleteState, lockState);
				response.sendRedirect("/cse545_WBDS/document");
				//TODO: Make this redirect to the same directory as the one they shared from!!
			}
			else
			{
				throw new Exception ("Invalid GUID or not authorized to share.");
			}
		}
		catch (Exception e)
		{
			response.sendRedirect("/cse545_WBDS/unauthorized");
			Logger.storeLog("DocumentController.shareDocument - Unauthorized to share document "
					+ docguid, 2, currentUser.getUserId());
		}
	}

	
	@RequestMapping(value="delete", method=RequestMethod.GET)
	protected void deleteDocument(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		currentUser = (User)request.getSession(true).getAttribute("user");
		try
		{
			if(!validation.validateFilenameGUID(request.getParameter("doc")))
			{
				throw new Exception("Invalid document guid");
			}
			
			if(!validation.validateAlphanumeric(request.getParameter("dir"), 11))
			{
				throw new Exception("Invalid directory");
			}
			
			String docguid = request.getParameter("doc");
			String dirid = request.getParameter("dir");
			
			Document d = Document.getDocument(docguid);
			if(authorization.canDelete(d.getDocguid(), currentUser.getUserId(), d.getGroupid()))
			{
				if((d.getCheckedOutUser() != 0 && d.getCheckedOutUser() == currentUser.getUserId()) || d.getCheckedOutUser() == 0)
				{
					String docname_delete = d.getDocname();
					int dirid_delete = d.getDirectoryid();
					
					d.Delete();
					
					while((d = Document.getDocument(docname_delete, dirid_delete)) != null)
					{
						d.Delete();
					}
					
					if(Directory.getDirectory(Integer.parseInt(dirid)).hasReadAccess(currentUser.getUserId()))
						response.sendRedirect("/cse545_WBDS/document?dir=" + dirid);
					else
						response.sendRedirect("/cse545_WBDS/document");
					Logger.storeLog("DocumentController.deleteDocument - Document deleted sucessfully "
							+ docguid, 1, currentUser.getUserId());
				}
				else
				{
					//TODO: perhaps send error message back to user saying document is not checked out to them
					response.sendRedirect("/cse545_WBDS/unauthorized");
					Logger.storeLog("DocumentController.deleteDocument - Unauthorized access to document "
							+ docguid, 3, currentUser.getUserId());
				}
			}
			else
			{
				response.sendRedirect("/cse545_WBDS/unauthorized");
				Logger.storeLog("DocumentController.deleteDocument - Unauthorized access to document "
						+ docguid, 3, currentUser.getUserId());
			}
		}
		catch(Exception e)
		{
			response.sendRedirect("/cse545_WBDS/error");
			Logger.storeLog("DocumentController.deleteDocument - Exception occured: "
					+ e.getMessage(), 2, currentUser.getUserId());
		}
	}
	
	@RequestMapping(value="checkout", method=RequestMethod.GET)
	protected void checkOutDocument(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		currentUser = (User)request.getSession(true).getAttribute("user");
		try
		{
			if(!validation.validateFilenameGUID(request.getParameter("doc")))
			{
				throw new Exception("Invalid document guid");
			}
			
			if(!validation.validateAlphanumeric(request.getParameter("dir"), 11))
			{
				throw new Exception("Invalid directory");
			}
			
			String docguid = request.getParameter("doc");
			String dirid = request.getParameter("dir");
			
			Document d = Document.getDocument(docguid);
			if(authorization.canLock(d.getDocguid(), currentUser.getUserId(), d.getGroupid()))
			{
				d.CheckOut(currentUser.getUserId());
				response.sendRedirect("/cse545_WBDS/document?dir=" + dirid);
				Logger.storeLog("DocumentController.checkOutDocument - Document Checked Out sucessfully "
						+ docguid, 1, currentUser.getUserId());
			}
			else
			{
				response.sendRedirect("/cse545_WBDS/unauthorized");
				Logger.storeLog("DocumentController.checkOutDocument - Unauthorized access to document "
						+ docguid, 3, currentUser.getUserId());
			}
		}
		catch(Exception e)
		{
			response.sendRedirect("/cse545_WBDS/error");
			Logger.storeLog("DocumentController.checkOutDocument - Exception occured: "
					+ e.getMessage(), 2, currentUser.getUserId());
		}
	}
	
	@RequestMapping(value="checkin", method=RequestMethod.GET)
	protected void checkInDocument(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		currentUser = (User)request.getSession(true).getAttribute("user");
		try
		{
			if(!validation.validateFilenameGUID(request.getParameter("doc")))
			{
				throw new Exception("Invalid document guid");
			}
			
			if(!validation.validateAlphanumeric(request.getParameter("dir"), 11))
			{
				throw new Exception("Invalid directory");
			}
			
			String docguid = request.getParameter("doc");
			String dirid = request.getParameter("dir");
			
			Document d = Document.getDocument(docguid);
			if(authorization.canLock(d.getDocguid(), currentUser.getUserId(), d.getGroupid()))
			{
				d.CheckIn(currentUser.getUserId());
				response.sendRedirect("/cse545_WBDS/document?dir=" + dirid);
				Logger.storeLog("DocumentController.checkInDocument - Document Checked Out sucessfully "
						+ docguid, 1, currentUser.getUserId());
			}
			else
			{
				response.sendRedirect("/cse545_WBDS/unauthorized");
				Logger.storeLog("DocumentController.checkInDocument - Unauthorized access to document "
						+ docguid, 3, currentUser.getUserId());
			}
		}
		catch(Exception e)
		{
			response.sendRedirect("/cse545_WBDS/error");
			Logger.storeLog("DocumentController.checkInDocument - Exception occured: "
					+ e.getMessage(), 2, currentUser.getUserId());
		}
	}
}