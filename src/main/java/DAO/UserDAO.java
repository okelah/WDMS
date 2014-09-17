package DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import userclasses.EditorUser;
import userclasses.GuestUser;
import userclasses.RegularUser;
import userclasses.TempUser;

public class UserDAO {

	private DBWrapper dbWrapper; 

	public ArrayList<String> getDocIdsOwned(int userId) throws SQLException{

		ArrayList<String> queryParams = new ArrayList<String>();
		queryParams.add(Integer.toString(userId));

		HashMap<String, ArrayList<String>> hmap=dbWrapper.executeQuery(DBQueries.GET_DOCIDS_OWNED, queryParams);
		return hmap.get("docguid");
	}

	public ArrayList<String> getDocIdsShared(int userId) throws SQLException{
		ArrayList<String> queryParams = new ArrayList<String>();
		queryParams.add(Integer.toString(userId));

		HashMap<String, ArrayList<String>> hmap=dbWrapper.executeQuery(DBQueries.GET_DOCIDS_SHARED, queryParams);
		return hmap.get("docguid");
	}


	public boolean getDocReadPermissions(String docId,int userId) throws SQLException{
		// TODO
		ArrayList<String> queryParams = new ArrayList<String>();
		queryParams.add(Integer.toString(userId));
		queryParams.add(docId);
		HashMap<String, ArrayList<String>> hmap=dbWrapper.executeQuery(DBQueries.GET_DOC_READ_PERMISSION, queryParams);
		return Boolean.parseBoolean(hmap.get("sharedread").get(0));
		// return true;
	}

	public boolean getDocUpdatePermissions(String docId,int userId) throws SQLException{
		// TODO
		ArrayList<String> queryParams = new ArrayList<String>();
		queryParams.add(Integer.toString(userId));
		queryParams.add(docId);
		HashMap<String, ArrayList<String>> hmap=dbWrapper.executeQuery(DBQueries.GET_DOC_UPDATE_PERMISSION, queryParams);
		return Boolean.parseBoolean(hmap.get("sharedupdate").get(0));
	}

	public boolean getDocDeletePermissions(String docId,int userId) throws SQLException{
		// TODO
		ArrayList<String> queryParams = new ArrayList<String>();
		queryParams.add(Integer.toString(userId));
		queryParams.add(docId);
		HashMap<String, ArrayList<String>> hmap=dbWrapper.executeQuery(DBQueries.GET_DOC_DELETE_PERMISSION, queryParams);
		return Boolean.parseBoolean(hmap.get("sharedelete").get(0));
	}

	public boolean getDocLockPermissions(String docId,int userId) throws SQLException{
		// TODO
		ArrayList<String> queryParams = new ArrayList<String>();
		queryParams.add(Integer.toString(userId));
		queryParams.add(docId);
		HashMap<String, ArrayList<String>> hmap=dbWrapper.executeQuery(DBQueries.GET_DOC_LOCK_PERMISSION, queryParams);
		return Boolean.parseBoolean(hmap.get("sharelock").get(0));
	}

	public void setCheckOut(String docId,int userId) throws SQLException{

		ArrayList<String> queryParams = new ArrayList<String>();
		queryParams.add(Integer.toString(userId));
		queryParams.add(docId);
		int rowNumber=dbWrapper.executeUpdate(DBQueries.SET_CHECKOUT, queryParams);
		if(rowNumber>0) System.out.println("Success");

		else
			System.out.println("No");
	}

	public void setCheckIn(String docId,int userId) throws SQLException{

		ArrayList<String> queryParams = new ArrayList<String>();
		queryParams.add("");
		queryParams.add(docId);
		int rowNumber=dbWrapper.executeUpdate(DBQueries.SET_CHECKOUT, queryParams);
		if(rowNumber>0) System.out.println("Success");

		else
			System.out.println("No");
	}

	public void setLatestUpdate(String docId,int userId)throws SQLException{

		ArrayList<String> queryParams = new ArrayList<String>();
		queryParams.add(Integer.toString(userId));
		queryParams.add(docId);
		int rowNumber=dbWrapper.executeUpdate(DBQueries.SET_CHECKOUT, queryParams);
		if(rowNumber>0) System.out.println("Success");

		else
			System.out.println("No");
	}


	public void setRequestRole(int userId,int roleId)throws SQLException{

		ArrayList<String> queryParams = new ArrayList<String>();
		queryParams.add(Integer.toString(userId));
		queryParams.add(Integer.toString(roleId));
		int rowNumber=dbWrapper.executeUpdate(DBQueries.SET_REQUEST_ROLE, queryParams);
		if(rowNumber>0) System.out.println("Success");

		else
			System.out.println("No");
	}

	public Role getRole(int userID) throws Exception{
		ArrayList<String> queryParams = new ArrayList<String>();
		queryParams.add(Integer.toString(userID));
		HashMap<String, ArrayList<String>> hmap =dbWrapper.executeQuery(DBQueries.GET_USER_ROLE, queryParams);

		Role role=null;
		if (hmap.size() == 1) {
			role = Role.setRole(Integer.parseInt(hmap.get("usertype").get(0)));
		} else {
			throw new Exception("Role does not exist");
		}
		return role;
	}

	public User getUser(String email) throws SQLException{
		ArrayList<String> queryParams = new ArrayList<String>();
		queryParams.add(email);
		HashMap<String, ArrayList<String>> hmap =dbWrapper.executeQuery(DBQueries.GET_USER, queryParams);
		if (hmap.get("active").size() == 1) {
			Role role=null;
			try {
				role=getRole(Integer.parseInt(hmap.get("userid").get(0)));
			} catch (Exception e) {
				role=Role.Temp;
			}
			User user=null;
			if(role==Role.Temp){
				user =new TempUser();
			}else if(role==Role.CorpManagement){
				user =new RegularUser();
			}else if(role==Role.Guest){
				user =new GuestUser();
			}else if(role==Role.RegularEmployee){
				user =new RegularUser();
			}else if(role==Role.SysAdmin){
				user =new SystemAdmin();
			}else if(role==Role.DeptManagement){
				user =new RegularUser();
			}
			
			user.setUserId(Integer.parseInt(hmap.get("userid").get(0)));
			user.setEmailId(email);
			user.setActive(hmap.get("active").get(0).equals("1")?true:false);
			user.setRole(role);
			
			return user;
		} else {
			return null;
		}
	}

	public User getUser(int userid) throws SQLException{
		ArrayList<String> queryParams = new ArrayList<String>();
		queryParams.add(Integer.toString(userid));
		HashMap<String, ArrayList<String>> hmap =dbWrapper.executeQuery(DBQueries.GET_USER_BY_ID, queryParams);
			Role role=null;
			try {
				role=getRole(Integer.parseInt(hmap.get("userid").get(0)));
			} catch (Exception e) {
				role=Role.Temp;
			}
			User user=null;
			if(role==Role.Temp){
				user =new TempUser();
			}else if(role==Role.CorpManagement){
				user =new RegularUser();
			}else if(role==Role.Guest){
				user =new GuestUser();
			}else if(role==Role.RegularEmployee){
				user =new RegularUser();
			}else if(role==Role.SysAdmin){
				user =new SystemAdmin();
			}else if(role==Role.DeptManagement){
				user =new RegularUser();
			}
			
			user.setUserId(Integer.parseInt(hmap.get("userid").get(0)));
			user.setEmailId(hmap.get("email").get(0));
			user.setActive(hmap.get("active").get(0).equals("1")?true:false);
			user.setRole(role);
			
			return user;
	}

	
	public void setDbWrapper(DBWrapper dbWrapper) {
		this.dbWrapper = dbWrapper;
	}
	
	public void modifyUserPswd(int userid,String password) throws SQLException{

		ArrayList<String> queryParams = new ArrayList<String>();
		String UPDATE_PSWD = "UPDATE users SET password=? where userid=?";
		queryParams.add(password);
		queryParams.add(Integer.toString(userid));
		int rowNumber=dbWrapper.executeUpdate(UPDATE_PSWD, queryParams);
		if (rowNumber > 0)
			System.out.println("Success");

		else
			System.out.println("No");
	}
	public boolean isActive(String email) throws Exception{
		User user = null;
		try
		{
			user = getUser(email);
		}
		catch (Exception E)
		{
			return false;
		}
		return (!(user.equals(null)) && user.getActive());	
	}


}