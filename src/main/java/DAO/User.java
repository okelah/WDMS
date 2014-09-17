package DAO;

import java.sql.SQLException;
import java.util.ArrayList;

//import projectResources.*;
public class User {
	
	private int userId;
	private String emailId;
	private String password;
	private boolean active;
	private Role role;

	public User() {
	}

/*	public User(String email) throws Exception {
		
		this.userId=user.getUserId();
		this.emailId=email;
		this.active=user.getActive();
		this.role=user.getRole();
	}
*/
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setpassword(String password) {
		this.password = password;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getUserId() {
		return userId;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getPassowrdd() {
		return password;
	}

	public boolean getActive() {
		return active;
	}

	public Role getRole() {
		return role;
	}

/*	public ArrayList<String> getDocumentsOwned() throws SQLException {
		return userDAO.getDocIdsOwned(userId);

	}

	public ArrayList<String> getDocumentsShared() throws SQLException {
		return userDAO.getDocIdsShared(userId);
	}
*/	
	// login and logout method removed as all the logger are invoked from controller
}