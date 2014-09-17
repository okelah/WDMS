package DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.jasypt.util.text.BasicTextEncryptor;

import utils.MailUtils;
import utils.validation;

public class Login {

	private DataSource dataSource;

	private DBWrapper dbWrapper = new DBWrapper();

	private String enc = "asdfas141sad";

	public boolean sendPasswordResetLink(String userEmail) {
		if (!validation.validateEmail(userEmail)) {
			return false;
		}

		dbWrapper.setDataSource(dataSource);

		ArrayList<String> queryParams = new ArrayList<String>();

		queryParams.add(userEmail);

		HashMap<String, ArrayList<String>> hmap;
		try {
			hmap = dbWrapper.executeQuery(DBQueries.SELECT_USERID, queryParams);
		} catch (SQLException e) {
			return true;
		}

		if (hmap.size() == 0) {
			return true;
		}

		String toUserEmail = userEmail;

		String subject = "WBDS Group8 - Reset Password";

		BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();

		basicTextEncryptor.setPassword(enc);

		String resetURL = Base64.encodeBase64URLSafeString((basicTextEncryptor.encrypt(userEmail)).getBytes());

		String text = "To reset your password click the following link -  https://10.0.2.111/cse545_WBDS/resetpassword?id="
				+ resetURL;

		MailUtils.sendMail(toUserEmail, subject, text);

		return true;
	}

	public Boolean resetPassword(String id, String newPassword) {

		BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();

		basicTextEncryptor.setPassword(enc);

		String userEmail = basicTextEncryptor.decrypt(new String(Base64.decodeBase64(id.getBytes())));

		dbWrapper.setDataSource(dataSource);

		ArrayList<String> queryParams = new ArrayList<String>();

		queryParams.add(Sha.hashCreator(newPassword));

		queryParams.add(userEmail);

		int noRows;
		try {
			noRows = dbWrapper.executeUpdate(DBQueries.UPDATE_PASS, queryParams);
		} catch (SQLException e) {
			return false;
		}

		if (noRows == 1) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean validLoginCredential(String username, String password) throws Exception {

		ArrayList<String> queryParams;

		Boolean retval = false;
		dbWrapper.setDataSource(dataSource);
		queryParams = new ArrayList<String>();
		queryParams.add(username);

		HashMap<String, ArrayList<String>> rs = dbWrapper.executeQuery(DBQueries.SELECT_PASSWORD, queryParams);
		if (rs.get("password") == null) {
			return false;
		}

		ArrayList<String> passwordList = rs.get("password");

		if (passwordList.size() == 0) {
			return false;
		}

		String storedPassword = passwordList.get(0);

		boolean flag = (boolean) Sha.validatePassword(password, storedPassword);

		System.out.println(flag);

		if (flag) {
			retval = true;
		} else {
			retval = false;
		}
		return retval;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
