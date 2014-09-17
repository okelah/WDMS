package DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

public class Register {

	private DataSource dataSource;
	private ArrayList<String> queryParams;
	private DBWrapper obj = new DBWrapper();

	public boolean createAccount(String emailId, String password,
			String accountType,String deptId) {
		Boolean retval=false;
		try {
			obj.setDataSource(dataSource);
			queryParams = new ArrayList<String>();
			queryParams.add(emailId);
			String hash = Sha.hashCreator(password);
			queryParams.add(hash);
			queryParams.add("0");
			int r = obj.executeUpdate(DBQueries.CREATE_USER_ACCOUNT,
					queryParams);
			if (r > 0) {
				createRoleRequests(emailId, accountType,deptId);
				retval= true;
			}
			System.out.println("No rows Affected");
		} catch (SQLException e) {
			retval= false;
		}
		return retval;
	}

	public void createRoleRequests(String emailId, String accoutType,String deptId)
			throws SQLException {

		String userId = "";
		queryParams = new ArrayList<String>();
		queryParams.add(emailId);
		HashMap<String, ArrayList<String>> rs = obj.executeQuery(
				DBQueries.SELECT_USERID, queryParams);
		ArrayList<String> userIdList = rs.get("userid");
		userId = userIdList.get(0);
		queryParams.clear();
		queryParams.add(userId);

		Role role=null;
		if (accoutType.equals("G")) {
			role=Role.Guest;
		} else if (accoutType.equals("R")) {
			role=Role.RegularEmployee;
		} else if (accoutType.equals("C")) {
			role=Role.CorpManagement;
		} else if (accoutType.equals("S")) {
			role=Role.SysAdmin;
		} else if (accoutType.equals("D")) {
			role=Role.DeptManagement;
		}
		queryParams.add(Integer.toString(role.getRoleId()));
		queryParams.add(deptId);
		obj.executeUpdate(DBQueries.CREATE_USER_ROLEREQUEST, queryParams);
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
