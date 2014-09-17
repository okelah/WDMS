package controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import utils.validation;
import DAO.DBWrapper;
import DAO.Logger;
import DAO.Role;
import DAO.Sha;
import DAO.SystemAdmin;

@Controller
@RequestMapping(value = "/sysadmin")
public class SystemAdminController {

	@Autowired
	private DBWrapper sysadminDbWrapper;
	private static final int BUFSIZE = 4096;
	private static final String DESTINATION_DIR_PATH = "c:/tmp/syslogs/";

	private SystemAdmin getSysAdminFromSession(HttpSession session) {
		try {
			SystemAdmin systemAdmin = (SystemAdmin) session.getAttribute("user");
			systemAdmin.setDbWrapper(sysadminDbWrapper);
			return systemAdmin;
		} catch (ClassCastException e) {
			return null;
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getListOfUsers(HttpSession session) {

		SystemAdmin systemAdmin = getSysAdminFromSession(session);

		if (systemAdmin == null) {
			return new ModelAndView("unauthorized");
		}

		ModelAndView retval = new ModelAndView("listusers");

		try {
			retval.addObject("userList", systemAdmin.getPendingUsers());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return retval;

	}

	@RequestMapping(value = "activate", method = RequestMethod.GET)
	public ModelAndView activateUser(HttpSession session, @RequestParam("email") String email) {

		SystemAdmin systemAdmin = getSysAdminFromSession(session);

		if (systemAdmin == null) {
			return new ModelAndView("unauthorized");
		}

		Boolean userActivated;
		try {
			userActivated = systemAdmin.activateUser(email);
		} catch (SQLException e) {
			userActivated = false;
			e.printStackTrace();
		}

		ModelAndView retval = new ModelAndView("listusers");

		try {
			retval.addObject("userList", systemAdmin.getPendingUsers());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (userActivated) {
			retval.addObject("message", "The user is activated");
		} else {
			retval.addObject("message", "The user is not activated");
		}

		return retval;
	}

	@RequestMapping(value = "deactivate", method = RequestMethod.POST)
	public ModelAndView deactivateUser(HttpSession session, @RequestParam("inputEmail") String email) {

		SystemAdmin systemAdmin = getSysAdminFromSession(session);

		if (systemAdmin == null) {
			return new ModelAndView("unauthorized");
		}

		Boolean userDeactivated;
		try {
			userDeactivated = systemAdmin.deactivateUser(email);
		} catch (SQLException e) {
			userDeactivated = false;
			e.printStackTrace();
		}

		ModelAndView retval = new ModelAndView("listusers");

		try {
			retval.addObject("userList", systemAdmin.getPendingUsers());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (userDeactivated) {
			retval.addObject("message", "The user is deactivated");
		} else {
			retval.addObject("message", "The user is not deactivated");
		}

		return retval;
	}

	@RequestMapping(method = RequestMethod.POST, value = "createuser")
	public ModelAndView addUser(HttpSession session, HttpServletRequest httpServletRequest,
			@RequestParam(value = "selectionField") String accountType,
			@RequestParam(value = "inputPassword") String password, @RequestParam(value = "inputEmail") String username) {

		SystemAdmin systemAdmin = getSysAdminFromSession(session);

		if (systemAdmin == null) {
			return new ModelAndView("unauthorized");
		}

		ModelAndView retval;
		if (!validation.validateEmail(username)) {
			retval = new ModelAndView("listusers");
			try {
				retval.addObject("userList", systemAdmin.getPendingUsers());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			retval.addObject("message", "Invalid Email");
			return retval;
		}
		if (!validation.validatePassword(password)) {
			retval = new ModelAndView("listusers");
			try {
				retval.addObject("userList", systemAdmin.getPendingUsers());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			retval.addObject("message", "Password should be Alphanumeric,8-16 characters,"
					+ " no whitespaces, atleast - 1 lowercase & 1upper case & 1 non alphanumeric character");
			return retval;
		}

		String hash = Sha.hashCreator(password);
		retval = new ModelAndView("listusers");
		retval.addObject("message", "User Created");
		try {
			systemAdmin.addUser(username, hash, accountType);
		} catch (SQLException e) {
			retval.addObject("message", "Unable to create user");
			e.printStackTrace();
		}
		try {
			retval.addObject("userList", systemAdmin.getPendingUsers());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retval;
	}

	@RequestMapping(value = "deleteuser", method = RequestMethod.POST)
	public ModelAndView deleteUser(HttpSession session, @RequestParam("inputEmail") String email) {

		SystemAdmin systemAdmin = getSysAdminFromSession(session);

		if (systemAdmin == null) {
			return new ModelAndView("unauthorized");
		}

		Boolean deleted;
		try {
			deleted = systemAdmin.deleteUser(email);
		} catch (SQLException e) {
			deleted = false;
			e.printStackTrace();
		}
		ModelAndView retval = new ModelAndView("listusers");
		try {
			retval.addObject("userList", systemAdmin.getPendingUsers());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (deleted) {
			retval.addObject("message", "The user with email id " + email + " is deleted");
		} else {
			retval.addObject("message", "Could not delete user with email id " + email);
		}

		return retval;
	}

	@RequestMapping(value = "updateuser", method = RequestMethod.POST)
	public ModelAndView updateUser(HttpSession session, @RequestParam("inputEmail") String email,
			@RequestParam(value = "selectionField") String roleType) {

		SystemAdmin systemAdmin = getSysAdminFromSession(session);

		if (systemAdmin == null) {
			return new ModelAndView("unauthorized");
		}

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

		ModelAndView retval = new ModelAndView("listusers");
		try {
			retval.addObject("userList", systemAdmin.getPendingUsers());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			if (systemAdmin.updateUser(email, role)) {
				retval.addObject("message", "User updated");
			} else {
				retval.addObject("message", "user update failed");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			retval.addObject("message", "user update failed");
		}

		return retval;
	}

	@RequestMapping(value = "viewlog", method = RequestMethod.GET)
	protected ModelAndView getDocument(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		SystemAdmin systemAdmin = getSysAdminFromSession(session);

		if (systemAdmin == null) {
			return new ModelAndView("unauthorized");
		}

		Date tempDate = new Date();

		String filename = Long.toString(tempDate.getTime()) + ".log";
		systemAdmin.getViewLog(filename);
		try {

			int length = 0;
			File f = new File(DESTINATION_DIR_PATH + filename);

			ServletOutputStream outStream = response.getOutputStream();
			String mimetype = "application/octet-stream";

			response.setContentType(mimetype);
			response.setContentLength((int) f.length());
			response.setHeader("Content-Disposition", "attachment;filename=\"" + "WBDS_system.log" + "\"");

			byte[] byteBuffer = new byte[BUFSIZE];
			DataInputStream in = new DataInputStream(new FileInputStream(f));
			while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
				outStream.write(byteBuffer, 0, length);
			}
			in.close();
			outStream.close();
			Logger.storeLog("System admin downloaded the system log", 1, systemAdmin.getUserId());

		} catch (Exception ex) {
			ex.printStackTrace();
			Logger.storeLog("Exception occured in downloading the system log document", 1, systemAdmin.getUserId());
			ModelAndView retval = new ModelAndView("listusers");
			try {
				retval.addObject("userList", systemAdmin.getPendingUsers());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return retval;

		}
		return null;
	}
}
