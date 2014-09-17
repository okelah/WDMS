package userclasses;

import java.sql.SQLException;
import java.util.ArrayList;

import DAO.Department;
import DAO.Logger;
import DAO.User;
import DAO.UserDAO;

public class EditorUser extends User{

	 private Department memberOfDepartment;
	 private ArrayList<Department> departmentsResponsibleForIds;
	 
	 
	 public boolean checkDocReadPermissions(String docId,int userId) throws SQLException{
	   
		 UserDAO udao = new UserDAO();
		 boolean permission= udao.getDocReadPermissions(docId, userId);
		 return permission;
	 }
	 
	 public boolean checkDocUpdatePermissions(String docId,int userId) throws SQLException{
		 UserDAO udao = new UserDAO();
		 boolean permission= udao.getDocUpdatePermissions(docId, userId);
		 return permission;
		 }
	 
	 public boolean checkDocDeletePermissions(String docId,int userId)throws SQLException{
		 UserDAO udao = new UserDAO();
		 boolean permission= udao.getDocDeletePermissions(docId, userId);
		 return permission;
		 }
	 
	 public boolean checkDocLockPermissions(String docId,int userId)throws SQLException{
		 UserDAO udao = new UserDAO();
		 boolean permission= udao.getDocLockPermissions(docId, userId);
		 return permission;
		 }
	 
	  
	 
	 public void updateDocument(String docId,int userId) throws SQLException{
		 UserDAO udao = new UserDAO();
		 udao.setLatestUpdate(docId, userId);
		 Logger.storeLog("updated  document id:  "+docId, 1, userId);
	 }
	 

	 public void checkOutDocument(String docId,int userId) throws SQLException{
	   
		 UserDAO udao = new UserDAO();
		 udao.setCheckOut(docId, userId);
		 Logger.storeLog("checked out document id:  "+docId, 1, userId);
	 }

	 public void checkInDocument(String docId,int userId) throws SQLException{
		 UserDAO udao = new UserDAO();
		 udao.setCheckIn(docId, userId);
		 Logger.storeLog("checked In document id:  "+docId, 1, userId);
	 } 
	 
	}