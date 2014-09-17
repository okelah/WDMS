package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.DataSource;

public class Authorization {

	private DataSource dataSource;

	public Authorization()
	{
		
	}
	
	public Authorization(DataSource _dataSource)
	{
		this.dataSource = _dataSource;
	}
	
		
	public ModelAndView ModelAndViewAuth(String ModelAndViewName) throws SQLException {

		// By default we are unauthorized to view any page.
		ModelAndView retModelAndView = new ModelAndView("unauthorized");
		
		Connection connection = null;
		connection = dataSource.getConnection();
		
		// Check if the user is of a type which is authorized to view this page.
		PreparedStatement statement = connection.prepareStatement("SELECT pg.modelandviewname FROM pagepermissions pgp, pages pg, users u, userrole ur WHERE u.userid=ur.userid AND ur.usertype=pgp.userrole AND pgp.pageid=pg.pageid AND pg.modelandviewname=\"?\" AND u.userid=?;");

		statement.setString(1, ModelAndViewName);
		
		// !!!! NEED TO FIGURE OUT HOW TO ACCESS USERID OF LOGGED IN USER !!!!
		String userid = "1";
		
		statement.setString(2, userid);
		ResultSet rs = statement.executeQuery();
		if (rs.next()) {
			if (rs.getString("modelandviewname").equals(ModelAndViewName)) {
				retModelAndView = new ModelAndView(ModelAndViewName);
			} 
		}
		rs.close();
		statement.close();

		return retModelAndView;
	}
	
	public boolean canShare(String docguid, int userid)
	{
		return ownsOrManages(docguid, userid);
	}
	
	public boolean canRead(String docguid, int userid, int groupid) throws SQLException
	{
		boolean isAuthorized = false;
		if (ownsOrManages(docguid, userid))
		{
			isAuthorized = true;
		}
		else
		{
			// Check if they have been shared read access
			Connection connection = null;
			connection = dataSource.getConnection();

			String sql = "select * from acl a " +
					"where a.groupid = ? and a.userid = ? " +
					"and a.sharedread = 1";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setInt(1, groupid);
			statement.setInt(2, userid);
			
			ResultSet rs = statement.executeQuery();
			if (rs.next()) 
			{
				isAuthorized = true;
			}
		}
	
		return isAuthorized;
	}
	
	public boolean canUpload(String docguid, int userid, int groupid) throws SQLException
	{
		boolean isAuthorized = false;
		if (ownsOrManages(docguid, userid))
		{
			isAuthorized = true;
		}
		else
		{
			// Check if they have been shared upload access
			Connection connection = null;
			connection = dataSource.getConnection();

			String sql = "select * from acl a " +
					"where a.groupid = ? and a.userid = ? " +
					"and a.sharedupdate = 1";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setInt(1, groupid);
			statement.setInt(2, userid);
			
			ResultSet rs = statement.executeQuery();
			if (rs.next()) 
			{
					isAuthorized = true;
			}
		}
	
		return isAuthorized;
	}
	
	public boolean canDelete(String docguid, int userid, int groupid) throws SQLException
	{
		boolean isAuthorized = false;
		if (ownsOrManages(docguid, userid))
		{
			isAuthorized = true;
		}
		else
		{
			// Check if they have been shared delete access
			Connection connection = null;
			connection = dataSource.getConnection();

			String sql = "select * from acl a " +
					"where a.groupid = ? and a.userid = ? " +
					"and a.shareddelete = 1";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setInt(1, groupid);
			statement.setInt(2, userid);
			
			ResultSet rs = statement.executeQuery();
			if (rs.next()) 
			{
					isAuthorized = true;
			}
			
		}
	
		return isAuthorized;
	}
	
	public boolean canLock(String docguid, int userid, int groupid) throws SQLException
	{
		boolean isAuthorized = false;
		if (ownsOrManages(docguid, userid))
		{
			isAuthorized = true;
		}
		else
		{
			// Check if they have been shared lock access
			Connection connection = null;
			connection = dataSource.getConnection();

			String sql = "select * from acl a " +
					"where a.groupid = ? and a.userid = ? " +
					"and a.sharedlock = 1";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setInt(1, groupid);
			statement.setInt(2, userid);
			
			ResultSet rs = statement.executeQuery();
			if (rs.next()) 
			{
					isAuthorized = true;
			}
		}
	
		return isAuthorized;
	}
	
	public boolean ownsOrManages(String docguid, int userid) 
	{
		boolean isAuthorized = false;
		try
		{
		Connection connection = null;
		connection = dataSource.getConnection();
		
		// Check if the user is authorized to share the document
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT d.* FROM documents d, userrole urM, userrole urO, deptresponsibilities dr WHERE d.docguid=? AND ");
		query.append("(");
		query.append("	d.ownerid=? "); //-- Owner
		query.append("	OR");
		query.append("	(  "); //-- Check if it's a manager 
		query.append("		urM.userid=? "); //-- link manager to userid (requesting)
		query.append("		AND");
		query.append("		( "); //-- user is either a Dept or corp management
		query.append("			((urM.usertype=5) AND (urO.usertype != 6)) "); //-- If dept, make sure owner isn't corp
		query.append("			OR");
		query.append("			(urM.usertype=6)");
		query.append("		)");
		query.append("		AND");
		query.append("		dr.userid=urM.userid "); //-- link department responsibilities with user role for management
		query.append("		AND");
		query.append("		urO.userid=d.ownerid "); //-- link the owner's id to their department
		query.append("		AND");
		query.append("		dr.responsabledept=urO.department"); //-- The manager is responsable for the owners department
		query.append("	)");
		query.append(")");
		PreparedStatement statement = connection.prepareStatement(query.toString());

		// ASSIGN Parameters
		statement.setString(1, docguid);
	
		statement.setInt(2, userid);
		statement.setInt(3, userid);
		
		
		ResultSet rs = statement.executeQuery();
		if (rs.next()) {
			if (rs.getString("docguid").equals(docguid)) {
				isAuthorized = true;
			} 
		}
		rs.close();
		statement.close();
		}
		catch (Exception e)
		{
			//TODO: log authorization check failure event
			System.out.println(e.getMessage());
			isAuthorized = false;
		}
		return isAuthorized;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
