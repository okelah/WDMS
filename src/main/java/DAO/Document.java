package DAO;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;

import com.mysql.jdbc.StringUtils;

public class Document 
{
	private static final long MAX_DATA_PER_USER = 1073741824;
	private static final long MAX_DATA_PER_FILE = 1048576;
	
	
	private static DataSource dataSource;
	
	private String docguid = null;
	private String docname;
	private int ownerid;
	private int version;
	private String comment;
	private int directoryid;
	private java.util.Date creationTime;
	private java.util.Date modifiedTime;
	private int modifiedUser;
	private java.util.Date lastAccessTime;
	private int lastAccessUser;
	private int checkedOutUser;
	private long fileSize;
	private int groupid;
	private User owner;
	private User mUser;
	private User aUser;
	private User cUser;

	private static final String DESTINATION_DIR_PATH ="c:\\tmp\\files\\";
	
	/***********************************
	 ****** 
	 ****** PROPERTIES
	 ******
	 **********************************/
	public String getDocguid() 
	{
		return this.docguid;
	}
	
	public String getDocname() 
	{
		return this.docname;
	}

	public void setDocname(String docname) 
	{
		this.docname = docname;
	}

	public int getOwnerid() 
	{
		return ownerid;
	}
	
	public User getMUser()
	{
		return mUser;
	}
	
	public User getAUser()
	{
		return aUser;
	}
	
	public User getCUser()
	{
		return cUser;
	}
	
	public User getOwner()
	{
		return owner;
	}
	public int getGroupid() 
	{
		return groupid;
	}

	public int getVersion() 
	{
		return version;
	}

	public String getComment() 
	{
		return comment;
	}

	public int getDirectoryid() 
	{
		return directoryid;
	}

	public void setDirectoryid(int directoryid) 
	{
		this.directoryid = directoryid;
	}

	public java.util.Date getCreationTime() 
	{
		return creationTime;
	}

	public java.util.Date getModifiedTime() 
	{
		return modifiedTime;
	}

	public int getModifiedUser() 
	{
		return modifiedUser;
	}

	public java.util.Date getLastAccessTime()
	{
		return lastAccessTime;
	}

	public void setLastAccessTime(java.util.Date lastAccessTime) throws SQLException 
	{
		this.lastAccessTime = lastAccessTime;
		
		Connection connection = null;
		try 
		{
			connection = dataSource.getConnection();
			String sql = "update documents set lastaccesstime=? where docguid=?";
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.setString(1, sdf.format(this.lastAccessTime.getTime()));
			prest.setString(2, this.docguid);
			
			prest.executeUpdate(); 
			//lastaccessuser, creationtime, modificationtime, lastaccesstim
		}
		catch(SQLException e) 
		{
			throw e;
		} 
		finally
		{
			connection.close();
		}
	}

	public int getLastAccessUser() 
	{
		return lastAccessUser;
	}

	public void setLastAccessUser(int lastAccessUser) throws SQLException 
	{
		this.lastAccessUser = lastAccessUser;
		
		Connection connection = null;
		try 
		{
			connection = dataSource.getConnection();
			String sql = "update documents set lastaccessuser=? where docguid=?";
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.setInt(1, this.lastAccessUser);
			prest.setString(2, this.docguid);
			
			prest.executeUpdate(); 
			//lastaccessuser, creationtime, modificationtime, lastaccesstim
		}
		catch(SQLException e) 
		{
			throw e;
		} 
		finally
		{
			connection.close();
		}
	}
	
	public int getCheckedOutUser()
	{
		return checkedOutUser;
	}

	public long getFileSize()
	{
		return fileSize;
	}
	
	/***********************************
	 ****** 
	 ****** Constructors
	 ******
	 **********************************/
	@SuppressWarnings("unused")
	private Document()
	{

	}
	
	private Document(DataSource data)
	{
		dataSource = data;
	}
	
	public Document(FileItem file, int currentUser, String comment, int directoryid, java.util.Date currentTime,String symmetricKey) throws Exception
	{
		Connection connection = null;
		
		this.docguid = java.util.UUID.randomUUID().toString();
		this.docname = file.getName();
		this.comment = XSSEncode(comment);
		this.directoryid = directoryid;
		this.modifiedTime = currentTime;
		this.modifiedUser = currentUser;
		this.lastAccessTime = currentTime;
		this.lastAccessUser = currentUser;
		this.checkedOutUser = 0;
		this.fileSize = 0;
		this.groupid = -1;
		
		Document d = getDocument(this.docname, this.directoryid);
		if(d == null)
		{
			//new document
			this.creationTime = currentTime;
			this.ownerid = currentUser;
			this.version = 1;
		}
		else
		{
			Authorization auth = new Authorization(dataSource);
			
			if(auth.canUpload(d.docguid, currentUser, d.groupid) && 
					(d.getCheckedOutUser() == 0 ||
					(d.getCheckedOutUser() != 0 && d.getCheckedOutUser() == currentUser)))
			{
				//update to old document
				this.creationTime = d.getCreationTime();
				this.ownerid = d.getOwnerid();
				this.version = d.getVersion();
				this.checkedOutUser = d.getCheckedOutUser();
				this.groupid = d.getGroupid();
				this.version++;
			}
			else
			{
				throw new Exception("Not permitted to update document");
			}
		}
		
		boolean exit = false;
		while(!exit)
		{
			try
			{
				fileSize = file.getSize();
				
				if(fileSize < MAX_DATA_PER_FILE)
				{
					connection = dataSource.getConnection();
					String sql_checkDataUsed = "select filesize from documents where ownerid = ?";
					
					PreparedStatement prest_checkDataUsed = connection.prepareStatement(sql_checkDataUsed);
					prest_checkDataUsed.setInt(1, this.ownerid); 
					ResultSet rs = prest_checkDataUsed.executeQuery();	
					
					long totalSizeUsed = 0;
					while (rs.next()) 
					{	
						totalSizeUsed = totalSizeUsed + rs.getInt("filesize");
					}
					
					if(totalSizeUsed < MAX_DATA_PER_USER)
					{
						Tika tika = new Tika();
						Metadata metadata = new Metadata();
						String mime = tika.detect(file.getInputStream(), metadata);
						
						if(isSafeMimeType(mime))
						{
							String sql = "";
							
							String filePath=DESTINATION_DIR_PATH + this.docguid;
							String encryptPath=DESTINATION_DIR_PATH + this.docguid+".e";
							String decryptPath=DESTINATION_DIR_PATH + this.docguid+".dec";
							
							//File saveFile = new File(DESTINATION_DIR_PATH + this.docguid);
						
							System.out.println("Encrypting check symmetrickey"+symmetricKey);
							int flag=0;
							if(!StringUtils.isNullOrEmpty(symmetricKey)||!StringUtils.isEmptyOrWhitespaceOnly(symmetricKey)){
							flag=1;
							}
							
							if(this.version ==1)
							{
								sql = "Insert into documents (docguid, docname, ownerid, directoryid, comment, version, lastaccessuser, creationtime, modificationtime, lastaccesstime, modificationuser, filesize, groupid) " +
										"values (?,?,?,?,?,?,?,?,?,?,?,?," +
										"(select coalesce(max(groupid)+1,0) from documents d1)" +
										")";
								java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								
								PreparedStatement prest = connection.prepareStatement(sql);
								if(flag==1)
								{
									prest.setString(1, this.docguid+".e");
								}
								else
								{
									prest.setString(1, this.docguid);
								}
								prest.setString(2, this.docname);
								prest.setInt(3, this.ownerid); 
								prest.setInt(4, this.directoryid); 
								prest.setString(5, this.comment);
								prest.setInt(6, this.version); 
								prest.setInt(7, this.lastAccessUser);
								prest.setString(8, sdf.format(this.creationTime.getTime()));
								prest.setString(9, sdf.format(this.modifiedTime.getTime())); 
								prest.setString(10, sdf.format(this.lastAccessTime.getTime()));
								prest.setInt(11, this.modifiedUser);
								prest.setInt(12, (int)this.fileSize);
								
								prest.executeUpdate();
							}
							else
							{
								sql = "Insert into documents (docguid, docname, ownerid, directoryid, comment, version, lastaccessuser, creationtime, modificationtime, lastaccesstime, modificationuser, filesize, groupid) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
								java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								
								PreparedStatement prest = connection.prepareStatement(sql);
								if(flag==1)
								{
									prest.setString(1, this.docguid+".e");
								}
								else
								{
									prest.setString(1, this.docguid);
								}
								prest.setString(2, this.docname);
								prest.setInt(3, this.ownerid); 
								prest.setInt(4, this.directoryid); 
								prest.setString(5, this.comment);
								prest.setInt(6, this.version); 
								prest.setInt(7, this.lastAccessUser);
								prest.setString(8, sdf.format(this.creationTime.getTime()));
								prest.setString(9, sdf.format(this.modifiedTime.getTime())); 
								prest.setString(10, sdf.format(this.lastAccessTime.getTime()));
								prest.setInt(11, this.modifiedUser);
								prest.setInt(12, (int)this.fileSize);
								prest.setInt(13, this.groupid);
								
								prest.executeUpdate();
							}
							
							File saveFile = new File(DESTINATION_DIR_PATH + this.docguid);
							file.write(saveFile);
							saveFile.setExecutable(false);
							saveFile.setWritable(false);
							
							if(flag==1){
							
							Encryption.copy(Cipher.ENCRYPT_MODE, filePath,encryptPath, symmetricKey);
							
							saveFile.delete();
							}
							
							exit = true; //this means we have a valid docguid that was inserted into the db fine
						}
						else
						{
							throw new Exception("Invalid MIME type");
						}
					}
					else
					{
						throw new Exception("No more allocated space for user id " + this.ownerid);
					}
				}
				else
				{
					throw new Exception("File uploaded exceeded the maximum limit of " +
							MAX_DATA_PER_FILE/1024/1024 + "MB");
				}
			}
			catch(SQLException e) 
			{
				if(e.getMessage().contains("Duplicate entry"))
				{
					//the docguid was already taken, try again with a new docguid
					this.docguid = java.util.UUID.randomUUID().toString();
					exit = false; 
				}
				else
				{
					//some other error occured
					throw e;
				}
			}	
			catch(FileUploadException e) 
			{
				throw e;
			} 
			catch(Exception e)
			{
				throw e;
			}
			finally
			{
				connection.close();
			}
		}
	}
	
	//Very simple implementation, but should catch most attempts at XSS
	private String XSSEncode(String s) 
	{
		StringBuffer out = new StringBuffer();
	    for(int i=0; i<s.length(); i++)
	    {
	        char c = s.charAt(i);
	        if(c > 127 || c=='"' || c=='<' || c=='>')
	        {
	           out.append("&#"+(int)c+";");
	        }
	        else
	        {
	            out.append(c);
	        }
	    }
	    return out.toString();
	}

	//TODO: add mime types to database?
	private boolean isSafeMimeType(String mime) 
	{
		DBWrapper dbwrap=new DBWrapper();
	  ArrayList<String> queryParams=new ArrayList<String>();
	  queryParams.add(mime);
		try {
			HashMap<String,ArrayList<String>> resultset=dbwrap.executeQuery("select * from whitelist where filetype=?", queryParams);
			ArrayList<String> columnList=resultset.get("filetype");
			if(columnList.contains(mime))
			{
				return true;
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
		
//		if(mime.equals("image/gif") || mime.equals("image/jpeg") || mime.equals("image/pjpeg") ||
//				mime.equals("image/png") || mime.equals("image/tiff")  ||mime.equals("application/zip")
//				||mime.equals("application/pdf") || mime.equals("text/plain") ||
//				mime.equals("application/x-tika-msoffice") || mime.equals("application/msword") ||
//				mime.equals("application/octet-stream") || mime.equals("application/x-tika-ooxml"))
//		{
//			return true;
//		}
//		else
//		{
//			return false;
//		}
	}

	/***********************************
	 ****** 
	 ****** Methods
	 ******
	 **********************************/
	public void Delete() throws SQLException
	{
		Connection connection = null;
		
		try
		{
			connection = dataSource.getConnection();
			String sql = "delete from documents where docguid = ?";
			
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.setString(1, this.docguid);
			
			prest.executeUpdate(); 
			
			File deleteFile = new File(DESTINATION_DIR_PATH + this.docguid);
			deleteFile.delete();
		}
		catch(SQLException e) 
		{
			throw e;
		} 
		finally
		{
			connection.close();
		}
	}
	
	
	/***********************************
	 ****** 
	 ****** Static Methods
	 ******
	 **********************************/
	public static Document getDocument(String guid) throws SQLException
	{
		Document d = new Document(dataSource);
		Connection connection = null;
		try
		{
			connection = dataSource.getConnection();
			
			String sql = "select * from documents where docguid = ?";
			
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.setString(1, guid);
			
			ResultSet rs = prest.executeQuery();
			while (rs.next()) 
			{	
				d.docguid  = rs.getString("docguid");
				d.docname = rs.getString("docname");
				d.ownerid = rs.getInt("ownerid");
				d.version = rs.getInt("version");
				d.comment = rs.getString("comment");
				d.directoryid = rs.getInt("directoryid");
				d.modifiedUser = rs.getInt("modificationuser");
				d.lastAccessUser = rs.getInt("lastaccessuser");
				d.checkedOutUser = rs.getInt("checkedoutuser");
				d.groupid = rs.getInt("groupid");
				
				if(rs.getTimestamp("creationtime") != null)
					d.creationTime = new Date(rs.getTimestamp("creationtime").getTime());
				else
					d.creationTime = null;
				
				if(rs.getTimestamp("modificationtime") != null)
					d.modifiedTime = new Date(rs.getTimestamp("modificationtime").getTime());
				else
					d.modifiedTime = null;
				
				if(rs.getTimestamp("lastaccesstime") != null)
					d.lastAccessTime = new Date(rs.getTimestamp("lastaccesstime").getTime());
				else
					d.lastAccessTime = null;
				UserDAO userdao = new UserDAO();
				DBWrapper wrap = new DBWrapper();
				wrap.setDataSource(dataSource);
				userdao.setDbWrapper(wrap);
				d.mUser = userdao.getUser(d.modifiedUser);
				d.aUser = userdao.getUser(d.lastAccessUser);
				d.owner = userdao.getUser(d.ownerid);
				if(d.checkedOutUser != 0)
					d.cUser = userdao.getUser(d.checkedOutUser);
				else
					d.cUser = null;
			}
		}
		catch(SQLException e) 
		{
			throw e;
		} 
		finally
		{
			connection.close();
		}
		
		if(d.docguid == null)
			return null;
		else
			return d;
	}
	
	public static Document getDocument(String docname, int directory) throws SQLException
	{
		Document d = new Document(dataSource);
		Connection connection = null;
		try
		{
			connection = dataSource.getConnection();
			
			String sql = "select max(version), docname, docguid, ownerid, comment, " +
					"directoryid, modificationuser, lastaccessuser, creationtime, modificationtime, " +
					"lastaccesstime, checkedoutuser, groupid from documents where docname = ? " +
					"and directoryid = ? group by docname";
			
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.setString(1, docname);
			prest.setInt(2, directory);
			
			ResultSet rs = prest.executeQuery();
			while (rs.next()) 
			{	
				d.docguid  = rs.getString("docguid");
				d.docname = rs.getString("docname");
				d.ownerid = rs.getInt("ownerid");
				d.version = rs.getInt("max(version)");
				d.comment = rs.getString("comment");
				d.directoryid = rs.getInt("directoryid");
				d.modifiedUser = rs.getInt("modificationuser");
				d.lastAccessUser = rs.getInt("lastaccessuser");
				d.checkedOutUser = rs.getInt("checkedoutuser");
				d.groupid = rs.getInt("groupid");

				if(rs.getTimestamp("creationtime") != null)
					d.creationTime = new Date(rs.getTimestamp("creationtime").getTime());
				else
					d.creationTime = null;
				
				if(rs.getTimestamp("modificationtime") != null)
					d.modifiedTime = new Date(rs.getTimestamp("modificationtime").getTime());
				else
					d.modifiedTime = null;
				
				if(rs.getTimestamp("lastaccesstime") != null)
					d.lastAccessTime = new Date(rs.getTimestamp("lastaccesstime").getTime());
				else
					d.lastAccessTime = null;
				UserDAO userdao = new UserDAO();
				DBWrapper wrap = new DBWrapper();
				wrap.setDataSource(dataSource);
				userdao.setDbWrapper(wrap);
				d.mUser = userdao.getUser(d.modifiedUser);
				d.aUser = userdao.getUser(d.lastAccessUser);
				d.owner = userdao.getUser(d.ownerid);
				if(d.checkedOutUser != 0)
					d.cUser = userdao.getUser(d.checkedOutUser);
				else
					d.cUser = null;
			}
		}
		catch(SQLException e) 
		{
			throw e;
		} 
		finally
		{
			connection.close();
		}

		if(d.docguid == null)
			return null;
		else
			return d;
	}
	
	public static Document[] getDocumentListForDirectory(int directoryid) throws SQLException
	{
		Connection connection = null;
		Document[] results = null;
		
		try
		{
			connection = dataSource.getConnection();
			
			String sql = "select max(version), docname, docguid from documents where directoryid = ? group by docname";
			
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.setInt(1, directoryid);
			
			ResultSet rs = prest.executeQuery();
			int rowcount = 0;
			
			if (rs.last()) 
			{
				rowcount = rs.getRow();
				rs.beforeFirst();
			}
			
			results = new Document[rowcount];
			
			while (rs.next()) 
			{	
				results[--rowcount] = getDocument(rs.getString("docguid"));
			}
		}
		catch(SQLException e) 
		{
			throw e;
		} 
		finally
		{
			connection.close();
		}
		
		return results;
	}
	
	public void CheckIn(int currentUser) throws SQLException
	{
		if(this.checkedOutUser == currentUser) //TODO: detirmine if owner can check in for someone else
		{
			Connection connection = null;
			try 
			{
				connection = dataSource.getConnection();
				String sql = "update documents set checkedoutuser=NULL where docguid=?";
				PreparedStatement prest = connection.prepareStatement(sql);
				prest.setString(1, this.docguid);
	
				prest.executeUpdate(); 
			}
			catch(SQLException e) 
			{
				throw e;
			} 	
			finally
			{
				connection.close();
			}	
		}
	}
	
	public static Document[] getDocumentRevisionList(int groupid) throws SQLException
	{
		Connection connection = null;
		Document[] results = null;
		try
		{
			connection = dataSource.getConnection();
			
			String sql = "SELECT * FROM documents WHERE groupid=? ORDER BY version";
			
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.setInt(1, groupid);
			
			ResultSet rs = prest.executeQuery();
			int rowcount = 0;
			
			if (rs.last()) 
			{
				rowcount = rs.getRow();
				rs.beforeFirst();
			}
			
			results = new Document[rowcount];
			
			while (rs.next()) 
			{	
				results[--rowcount] = getDocument(rs.getString("docguid"));
			}
		}
		catch(SQLException e) 
		{
			throw e;
		} 
		finally
		{
			connection.close();
		}
		
		return results;
	}
	
	
	public void CheckOut(int currentUser) throws SQLException
	{
		Authorization auth = new Authorization(dataSource);
		if(auth.canLock(this.docguid, currentUser, this.groupid) && this.checkedOutUser == 0)
		{
			Connection connection = null;
			try 
			{
				connection = dataSource.getConnection();
				String sql = "update documents set checkedoutuser=? where docguid=?";
				PreparedStatement prest = connection.prepareStatement(sql);
				prest.setInt(1, currentUser);
				prest.setString(2, this.docguid);
	
				prest.executeUpdate(); 
			}
			catch(SQLException e) 
			{
				throw e;
			} 	
			finally
			{
				connection.close();
			}
		}
	}
	
	
	public void Share(int currentUser, int shareUser, boolean read, boolean update, boolean delete, boolean lock) throws SQLException
	{
		Authorization auth = new Authorization(dataSource);
		if(auth.canShare(this.docguid, currentUser))
		{
			Connection connection = null;
			try 
			{
				connection = dataSource.getConnection();
				// Check if this document already has a shared entry for this user.
				String sqlSel = "SELECT sharedread, sharedupdate, shareddelete, sharedlock FROM acl WHERE userid=? AND groupid=?";
				PreparedStatement prestSel = connection.prepareStatement(sqlSel);
				prestSel.setInt(1, shareUser);
				prestSel.setInt(2, this.groupid);  
				
				ResultSet rs = prestSel.executeQuery();	
				
				boolean doUpdate = false;

				while (rs.next()) 
				{	
					doUpdate = true;
				}
				
				String sql = "";
				if (doUpdate)
				{
					sql = "UPDATE acl SET sharedread=?, sharedupdate=?, shareddelete=?, sharedlock=? WHERE userid=? and groupid=?";
				}
				else
				{
					sql = "INSERT INTO acl (sharedread, sharedupdate, shareddelete, sharedlock, userid, groupid) VALUES (?, ?, ?, ?, ?, ?)";
				}
				PreparedStatement prest = connection.prepareStatement(sql);
			
				prest.setBoolean(1, read);
				prest.setBoolean(2, update);
				prest.setBoolean(3, delete);
				prest.setBoolean(4, lock);
				prest.setInt(5, shareUser);
				prest.setInt(6, this.groupid);
	
				prest.executeUpdate(); 
			}
			catch(SQLException e) 
			{
				throw e;
			} 	
			finally
			{
				connection.close();
			}
		}
	}
	
	public void setDataSource(DataSource data) 
	{
		dataSource = data;
	}

	
}