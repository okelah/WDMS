package DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SystemAdmin extends User {

	private DBWrapper dbWrapper;

	public void setDbWrapper(DBWrapper dbWrapper) {
		this.dbWrapper = dbWrapper;
	}

	public ArrayList<User> getPendingUsers() throws SQLException {

		ArrayList<String> queryParams = new ArrayList<String>();
		queryParams.add("0");
		HashMap<String, ArrayList<String>> hmap = dbWrapper.executeQuery(DBQueries.GET_PENDING_USERS, queryParams);

		if (hmap.size() == 0) {
			return new ArrayList<User>();
		}
		ArrayList<User> userList = new ArrayList<User>();
		for (int itr = 0; itr < hmap.get("email").size(); itr++) {
			User user = new User();

			user.setEmailId(hmap.get("email").get(itr));
			
			queryParams.clear();

			queryParams.add(hmap.get("userid").get(itr).toString());

			HashMap<String, ArrayList<String>> hmap1  = dbWrapper.executeQuery(DBQueries.GET_USER_ROLEREQUEST, queryParams);

			String requestedRole;
			if (hmap1.get("requestedrole").size() == 1) {
				requestedRole = hmap1.get("requestedrole").get(0);
			} else {
				continue;
			}
			
			
			Role role = null;
			if (requestedRole.equals("1")) {
				role = Role.Temp;
			} else if (requestedRole.equals("2")) {
				role = Role.Guest;
			} else if (requestedRole.equals("3")) {
				role = Role.SysAdmin;
			} else if (requestedRole.equals("4")) {
				role = Role.RegularEmployee;
			} else if (requestedRole.equals("5")) {
				role = Role.DeptManagement;
			} else if (requestedRole.equals("6")) {
				role = Role.CorpManagement;
			}

			user.setRole(role);

			userList.add(user);
		}

		return userList;

	}

	public Boolean addUser(String userName, String password, String roleType) throws SQLException {
		int userID;
		ArrayList<String> queryParams = new ArrayList<String>();

		queryParams.add(userName);
		queryParams.add(password);
		queryParams.add("1");
		int r = dbWrapper.executeUpdate(DBQueries.CREATE_USER_ACCOUNT, queryParams);
		if (r > 0) {
			queryParams.clear();

			queryParams.add(userName);

			HashMap<String, ArrayList<String>> hmap = dbWrapper.executeQuery(DBQueries.SELECT_USERID, queryParams);

			if (hmap.size() == 0) {
				return false;
			}

			if (hmap.get("userid").size() == 1) {
				userID = Integer.parseInt(hmap.get("userid").get(0));
			} else {
				return false;
			}

			queryParams.clear();

			queryParams.add(Integer.toString(userID));
			Role role = null;
			if (roleType.equals("G")) {
				role = Role.Guest;
			} else if (roleType.equals("R")) {
				role = Role.RegularEmployee;
			} else if (roleType.equals("C")) {
				role = Role.CorpManagement;
			} else if (roleType.equals("S")) {
				role = Role.SysAdmin;
			} else if (roleType.equals("D")) {
				role = Role.DeptManagement;
			}
			queryParams.add(Integer.toString(role.getRoleId()));
			int rowNumber = dbWrapper.executeUpdate(DBQueries.CREATE_USER_ROLE, queryParams);
			if (rowNumber > 0) {
				Logger.storeLog("User " + userName + " added", 1, this.getUserId());
				return true;
			} else {
				Logger.storeLog("Adding User " + userName + " failed", 2, this.getUserId());
				return false;
			}
		} else {
			Logger.storeLog("Adding User " + userName + " failed", 2, this.getUserId());
			return false;
		}
	}

	public Boolean deleteUser(String email) throws SQLException {
		ArrayList<String> queryParams = new ArrayList<String>();

		queryParams.add(email);

		HashMap<String, ArrayList<String>> hmap = dbWrapper.executeQuery(DBQueries.SELECT_USERID, queryParams);

		if (hmap.size() == 0) {
			return false;
		}

		int userID;
		if (hmap.get("userid").size() == 1) {
			userID = Integer.parseInt(hmap.get("userid").get(0));
		} else {
			return false;
		}

		queryParams.clear();

		queryParams.add(Integer.toString(userID));
		int rowNumber = dbWrapper.executeUpdate(DBQueries.DELETE_USER, queryParams);
		if (rowNumber > 0) {
			Logger.storeLog("User " + email + " deleted", 1, this.getUserId());
			return true;
		} else {
			Logger.storeLog("Deleting User " + email + " failed", 2, this.getUserId());
			return false;
		}
	}

	public Boolean updateUser(String userEmail, Role role) throws SQLException {
		if (userEmail.equals("sysadmin@email.com")) {
			Logger.storeLog("Update User " + userEmail + " failed", 2, this.getUserId());
			return false;
		}

		ArrayList<String> queryParams = new ArrayList<String>();

		queryParams.add(userEmail);

		HashMap<String, ArrayList<String>> hmap = dbWrapper.executeQuery(DBQueries.SELECT_USERID, queryParams);

		if (hmap.size() == 0) {
			Logger.storeLog("Update User " + userEmail + " failed", 2, this.getUserId());
			return false;
		}

		String userId;
		if (hmap.get("userid").size() == 1) {
			userId = hmap.get("userid").get(0);
		} else {
			Logger.storeLog("Update User " + userEmail + " failed", 2, this.getUserId());
			return false;
		}

		if (role == Role.SysAdmin) {
			Directory rootDirectory = Directory.getRootDirectory(Integer.parseInt(userId));
			rootDirectory.Delete();
		}

		queryParams.clear();

		queryParams.add(Integer.toString(role.getRoleId()));

		queryParams.add(userId);

		int rowNumber = dbWrapper.executeUpdate(DBQueries.UPDATE_USERROLE, queryParams);
		if (rowNumber > 0) {
			Logger.storeLog("Updated User " + userEmail, 1, this.getUserId());
			return true;
		} else {
			Logger.storeLog("Update User " + userEmail + " failed", 2, this.getUserId());
			return false;
		}
	}

	public Boolean deactivateUser(String userEmail) throws SQLException {
		ArrayList<String> queryParams = new ArrayList<String>();

		queryParams.add(userEmail);

		HashMap<String, ArrayList<String>> hmap = dbWrapper.executeQuery(
				DBQueries.SELECT_USERID, queryParams);

		if (hmap.size() == 0) {
			Logger.storeLog("Deactivating User " + userEmail + " failed", 2,
					this.getUserId());
			return false;
		}

		String userId;
		if (hmap.get("userid").size() == 1) {
			userId = hmap.get("userid").get(0);
		} else {
			Logger.storeLog("Deactivating User " + userEmail + " failed", 2,
					this.getUserId());
			return false;
		}

		queryParams.clear();

		queryParams.add(userId);

		hmap = dbWrapper.executeQuery(DBQueries.GET_USER_ROLE_FOR_SYSADMIN, queryParams);
		
		if (hmap.size() == 0) {
			Logger.storeLog("Deactivating User " + userEmail + " failed", 2,
					this.getUserId());
			return false;
		}

		String userRole;
		String userDept;

		if (hmap.get("usertype").size() == 1) {
			userRole = hmap.get("usertype").get(0);
			userDept = hmap.get("department").get(0);

		} else {
			Logger.storeLog("Deactivating User " + userEmail + " failed", 2,
					this.getUserId());
			return false;
		}

		dbWrapper.executeUpdate(DBQueries.DELETE_USER_ROLE, queryParams);

		queryParams.clear();

		queryParams.add(userId);

		queryParams.add(userRole);
		queryParams.add(userDept);

		dbWrapper.executeUpdate(DBQueries.CREATE_USER_ROLEREQUEST, queryParams);

		queryParams.clear();

		queryParams.add(Integer.toString(0));

		queryParams.add(userId);

		dbWrapper.executeUpdate(DBQueries.UPDATE_ACTIVE, queryParams);

		Logger.storeLog("Deactivated User " + userEmail, 1, this.getUserId());

		return true;

	}
	
	public Boolean activateUser(String userEmail) throws SQLException {
		ArrayList<String> queryParams = new ArrayList<String>();

		queryParams.add(userEmail);

		HashMap<String, ArrayList<String>> hmap = dbWrapper.executeQuery(
				DBQueries.SELECT_USERID, queryParams);

		if (hmap.size() == 0) {
			Logger.storeLog("Activating User " + userEmail + " failed", 2,
					this.getUserId());
			return false;
		}

		String userId;
		if (hmap.get("userid").size() == 1) {
			userId = hmap.get("userid").get(0);
		} else {
			Logger.storeLog("Activating User " + userEmail + " failed", 2,
					this.getUserId());
			return false;
		}

		queryParams.clear();

		queryParams.add(userId);

		hmap = dbWrapper.executeQuery(DBQueries.GET_USER_ROLEREQUEST_FOR_SYSADMIN,
				queryParams);
		
		if (hmap.size() == 0) {
			Logger.storeLog("Activating User " + userEmail + " failed", 2,
					this.getUserId());
			return false;
		}

		String requestedRole;
		String requestedDept;

		if (hmap.get("requestedrole").size() == 1) {
			requestedRole = hmap.get("requestedrole").get(0);
			requestedDept = hmap.get("requesteddept").get(0);
		} else {
			Logger.storeLog("Activating User " + userEmail + " failed", 2,
					this.getUserId());
			return false;
		}

		dbWrapper.executeUpdate(DBQueries.DELETE_USER_ROLEREQUEST, queryParams);

		queryParams.clear();

		queryParams.add(userId);

		queryParams.add(requestedRole);
		queryParams.add(requestedDept);
		dbWrapper.executeUpdate(DBQueries.CREATE_USER_ROLE, queryParams);

		queryParams.clear();

		queryParams.add(Integer.toString(1));

		queryParams.add(userId);

		dbWrapper.executeUpdate(DBQueries.UPDATE_ACTIVE, queryParams);

		Logger.storeLog("Activated User " + userEmail, 1, this.getUserId());

		return true;
	}

	public void getViewLog(String filename) throws SQLException {

		dbWrapper.getSystemLog(filename);
	}
}