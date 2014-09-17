package userclasses;

import java.sql.SQLException;

import DAO.Logger;
import DAO.User;
import DAO.UserDAO;

public class TempUser extends User{

	 public void requestRole(int userId,int requestedRole) throws SQLException{
		// TODO
		 UserDAO udao = new UserDAO();
		 udao.setRequestRole(userId,requestedRole);
		 Logger.storeLog("requested role :"+requestedRole, 1, userId);
	 }
	 
	 
	}