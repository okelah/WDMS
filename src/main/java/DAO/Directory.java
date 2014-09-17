package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

//import javax.sql.DataSource;

public class Directory 
{
	private static DataSource dataSource;
	
	private int dirid = -1;
	private String dirname = null;
	private int ownerid = -1;
	private int parent_dirid = -1;
	private User owner = null;	
	
	@SuppressWarnings("unused")
	private Directory()
	{

	}
	public User getOwner()
	{
		return this.owner;
	}
	
	private Directory(DataSource data)
	{
		dataSource = data;
	}
	
	public Directory(int ownerid, String dirname, int parent_id) throws SQLException
	{
		this.ownerid = ownerid;
		this.dirname = dirname;
		this.parent_dirid = parent_id;
		
		Connection connection = null;
		try 
		{
			connection = dataSource.getConnection();
			
			String sql2 = "select dirid from directories where dirname=? and parentfolder=? and owner=?";
			PreparedStatement prest2 = connection.prepareStatement(sql2);
			prest2.setString(1, this.dirname);
			prest2.setInt(2, this.parent_dirid);
			prest2.setInt(3, this.ownerid);
			
			ResultSet rs = prest2.executeQuery();
			while (rs.next()) 
			{	
				throw new Exception();
			}
			
			
			String sql = "insert into directories (dirname, parentfolder, owner) values(?,?,?)";
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.setString(1, this.dirname);
			prest.setInt(2, this.parent_dirid);
			prest.setInt(3, this.ownerid);
			
			prest.executeUpdate(); 
			
			rs = prest2.executeQuery();
			while (rs.next()) 
			{	
				this.dirid = rs.getInt("dirid");
			}
		}
		catch(SQLException e) 
		{
			throw e;
		} 
		catch (ClassNotFoundException e) 
		{
		}	
		catch(Exception e)
		{
			
		}
		finally
		{
			connection.close();
		}	
	}
	
	public Directory(int ownerid, String dirname) throws SQLException
	{
		this.ownerid = ownerid;
		this.dirname = dirname;
		
		Connection connection = null;
		try 
		{
			connection = dataSource.getConnection();
			String sql = "insert into directories (dirname, parentfolder, owner) values(?,1,?)";
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.setString(1, dirname);
			prest.setInt(2, ownerid);
			
			prest.executeUpdate(); 
			
			String sql2 = "select dirid from directories where dirname=? and owner=?";
			PreparedStatement prest2 = connection.prepareStatement(sql2);
			prest2.setString(1, this.dirname);
			prest2.setInt(2, this.ownerid);
			
			ResultSet rs = prest2.executeQuery();
			while (rs.next()) 
			{	
				this.dirid = rs.getInt("dirid");
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
	}
	
	public int getDirectoryID()
	{
		return this.dirid;
	}
	
	public String getDirectoryName()
	{
		return this.dirname;
	}
	
	public void setDirectoryName(String new_name) throws SQLException
	{
		Connection connection = null;
		try 
		{
			connection = dataSource.getConnection();
			String sql = "update directories set dirname=? where dirid=?";
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.setString(1, new_name);
			prest.setInt(2, this.dirid);

			prest.executeUpdate(); 
			
			this.dirname = new_name;
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
	
	public int getOwnerID()
	{
		return this.ownerid;
	}
	
	public int getParentDirectoryID()
	{
		return this.parent_dirid;
	}
	
	public void setParentDirectoryID(int new_id) throws SQLException
	{
		Connection connection = null;
		try 
		{
			connection = dataSource.getConnection();
			String sql = "update directories set parent=? where dirid=?";
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.setInt(1, new_id);
			prest.setInt(2, this.dirid);

			prest.executeUpdate(); 
			
			this.parent_dirid = new_id;
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
	
	public static Directory getDirectory(int directoryId) throws Exception
	{
		Directory d = new Directory(dataSource);
		Connection connection = null;
		try
		{
			connection = dataSource.getConnection();
			
			String sql = "select * from directories where dirid = ?";
			
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.setInt(1, directoryId);
			
			ResultSet rs = prest.executeQuery();
			if (rs.first())
			{
				rs.beforeFirst();
				while (rs.next()) 
				{	
					d.dirid = rs.getInt("dirid");
					d.dirname = rs.getString("dirname");
					d.ownerid = rs.getInt("owner");
					d.parent_dirid = rs.getInt("parentfolder");
				}
			}
			else
			{
				throw new Exception("Invalid directory id");
			}
			UserDAO userdao = new UserDAO();
			DBWrapper wrap = new DBWrapper();
			wrap.setDataSource(dataSource);
			userdao.setDbWrapper(wrap);
			d.owner = userdao.getUser(d.ownerid);
		}
		catch(SQLException e) 
		{
			throw e;
		}
		finally
		{
			connection.close();
		}
		return d;
	}
	
	public static Directory getRootDirectory(int ownerid) throws SQLException
	{
		Directory d = new Directory(dataSource);
		Connection connection = null;
		try
		{
			connection = dataSource.getConnection();
			
			String sql = "select * from directories where owner = ? and parentfolder = 1";
			
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.setInt(1, ownerid);
			
			ResultSet rs = prest.executeQuery();
			while (rs.next()) 
			{	
				d.dirid = rs.getInt("dirid");
				d.dirname = rs.getString("dirname");
				d.ownerid = rs.getInt("owner");
				d.parent_dirid = rs.getInt("parentfolder");
				UserDAO userdao = new UserDAO();
				DBWrapper wrap = new DBWrapper();
				wrap.setDataSource(dataSource);
				userdao.setDbWrapper(wrap);
				d.owner = userdao.getUser(d.ownerid);
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
		return d;
	}
	
	public static Directory[] getRootDirectories(int userid) throws Exception
	{
		Directory[] roots = null;
		Connection connection = null;
		try
		{
			connection = dataSource.getConnection();
			
			String sql = "select dirid from directories where parentfolder = 1";
			
			PreparedStatement prest = connection.prepareStatement(sql);
			
			ResultSet rs = prest.executeQuery();
			
			int rowcount = 0;
			
			if (rs.last()) 
			{
				rowcount = rs.getRow();
				rs.beforeFirst();
			}
			
			Directory[] temp = new Directory[rowcount];
			int accessCount = 0;
			
			while (rs.next()) 
			{	
				Directory d = getDirectory(rs.getInt("dirid"));
				
				if(d.hasReadAccess(userid))
				{
					temp[--rowcount] = d;
				}
			}
			rs.beforeFirst();
			roots = new Directory[temp.length];
			
			while (rs.next()) 
			{	
				Directory d = getDirectory(rs.getInt("dirid"));
				
				if(d.hasReadAccess(userid))
				{
					roots[accessCount] = d;
					accessCount++;
				}
			}
			
		}
		catch(SQLException e) 
		{
			throw e;
		} 
		catch (ClassNotFoundException e) 
		{

		}
		finally
		{
			connection.close();
		}
		return roots;
	}
	
	public Directory getParentDirectory() throws Exception
	{
		return getDirectory(this.parent_dirid);
	}
	
	public Directory[] getChildDirectories() throws SQLException
	{
		Directory[] children = null;
		Connection connection = null;
		
		try
		{
			connection = dataSource.getConnection();
			
			String sql = "select dirid, dirname from directories where parentfolder = ? order by dirname desc";
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.setInt(1, this.dirid);
			
			ResultSet rs = prest.executeQuery();
			
			int rowcount = 0;
			
			if (rs.last()) 
			{
				rowcount = rs.getRow();
				rs.beforeFirst();
			}
			
			children = new Directory[rowcount];	
			
			while (rs.next()) 
			{	
				children[--rowcount] = getDirectory(rs.getInt("dirid"));
			}
		}
		catch(SQLException e) 
		{
			throw e;
		} 
		catch (Exception e)
		{
			children = null;
		}
		finally
		{
			connection.close();
		}
		
		return children;
	}
	
	public String getCurrentPath() throws SQLException
	{
		String path = this.dirname;
		
		//Loop through parents adding to path until parent is null/-1/0
		Directory d;
		try {
			d = getDirectory(this.dirid);
		
			
			while(d.getParentDirectoryID() > 0)
			{
				d = getDirectory(d.getParentDirectoryID());
				path = d.getDirectoryName() + "\\" + path;
			}
			
			path = "\\\\" + path;
			
			return path;
		
		} catch (Exception e) {
			return null;
		}
	}
	
	/*public void setDataSource(DataSource dataSource) 
	{
		this.dataSource = dataSource;
	}*/
	
	public void Delete() throws SQLException
	{
		Connection connection = null;
		Directory[] children = this.getChildDirectories();
		
		for(int i = 0; i < children.length; i++)
		{
			children[i].Delete();
		}
		
		Document[] documents = Document.getDocumentListForDirectory(this.dirid);
		for(int i = 0; i < children.length; i++)
		{
			documents[i].Delete();
		}
		
		try
		{
			connection = dataSource.getConnection();
			String sql = "delete from directories where dirid = ?";
			
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.setInt(1, this.dirid);
			
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
	
	public boolean hasReadAccess(int userId) throws SQLException, ClassNotFoundException
	{
		if(this.ownerid == userId)
			return true;
		else
		{
			Connection connection = null;
			
			try
			{
				Directory[] children = getChildDirectories();
				
				connection = dataSource.getConnection();

				String sql = "select doc.docguid " +  
						"from documents doc, directories dir " +
						"where doc.directoryid = dir.dirid " +
						"and dir.dirid = ?";
				
				PreparedStatement prest = connection.prepareStatement(sql);
				prest.setInt(1, this.dirid);
				
				ResultSet rs = prest.executeQuery();			
				
				while (rs.next()) 
				{	
					String docguid  = rs.getString("docguid");
					Authorization auth = new Authorization(dataSource);
					if(auth.canRead(docguid, userId, Document.getDocument(docguid).getGroupid()))
					{
						return true;
					}
				}
				
				for(int i = 0; i < children.length; i++)
				{
					if(children[i].hasReadAccess(userId))	
					{
						return true;
					}
				}
			}
			catch(SQLException e) 
			{
				throw e;
			} 
			catch (ClassNotFoundException e) 
			{
				throw e;
			}
			finally
			{
				connection.close();
			}
		}
		return false;
	}
	
	public boolean isOwner(int userId) throws SQLException
	{
		if(this.ownerid == userId)
			return true;
		else
			return false;
	}
	
	public void setDataSource(DataSource data) 
	{
		dataSource = data;
	}
}