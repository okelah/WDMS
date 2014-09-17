package DAO;

public class DBQueries {

	public static final String CREATE_USER_ACCOUNT = "INSERT into users (email,password,active) values(?,?,?)";
	public static final String SELECT_USERID = "SELECT userid from users where email=?";
	

	public static final String CREATE_USER_ROLEREQUEST = "INSERT into rolerequests (requestinguser,requestedrole,requesteddept) values(?,?,?)";

	public static final String SELECT_PASSWORD = "SELECT password from mydb.users where email = ?";

	public static final String INSERT_LOG = "INSERT into systemlog (comment,category,userid) values (?,?,?) ";
	
	public static final String DELETE_USER = "DELETE FROM users WHERE userid=?";
	public static final String UPDATE_ACTIVE = "UPDATE users SET active=? where userid=?";
	public static final String CREATE_USER_ROLE = "INSERT into userrole (userid,usertype,department) values(?,?,?)";
	public static final String DELETE_USER_ROLEREQUEST = "DELETE from rolerequests where requestinguser = ?";
	public static final String GET_USER_ROLEREQUEST = "SELECT requestedrole from rolerequests where requestinguser = ?";
	public static final String GET_USER_ROLE = "SELECT usertype from userrole where userid = ?";
	public static final String DELETE_USER_ROLE = "DELETE from userrole where userid = ?";
	public static final String GET_PENDING_USERS = "SELECT userid, email FROM users where active=?";
	
	public static final String GET_USER_BY_ID="SELECT * from users where userid= ?";
	public static final String UPDATE_USERROLE = "UPDATE userrole SET usertype=? where userid = ?";
	public static final String GET_DOCIDS_OWNED = "SELECT docguid FROM documents where ownerid=?";
	public static final String GET_DOCIDS_SHARED = "SELECT docguid FROM acl where userid=?";
	public static final String GET_DOC_READ_PERMISSION = "SELECT sharedread FROM acl where userid=? AND docguid=?";
	public static final String GET_DOC_UPDATE_PERMISSION = "SELECT sharedupdate FROM acl where userid=? AND docguid=?";
	public static final String GET_DOC_DELETE_PERMISSION = "SELECT shareddelete FROM acl where userid=? AND docguid=?";
	public static final String GET_DOC_LOCK_PERMISSION = "SELECT sharedlock FROM acl where userid=? AND docguid=?";
	public static final String SET_CHECKOUT = "UPDATE documents SET checkedoutuser=? where docguid=?";
	public static final String SET_LATEST_UPDATE = "UPDATE documents SET lastaccessuser=? where docguid=?";
	public static final String SET_REQUEST_ROLE = "INSERT rolerequests (requestinguser,requestedrole) values (?,?) ";
	public static final String DISABLE_USER = "UPDATE user SET active=? where userid=?";
	public static final String ENABLE_USER = "UPDATE user SET active=? where userid=?";
	public static final String GET_USER="SELECT userid, active from users where email= ?";
	public static final String UPDATE_PASS="UPDATE users set password = ? where email = ?";
	
	public static final String GET_USER_ROLEREQUEST_FOR_SYSADMIN = "SELECT requestedrole, requesteddept from rolerequests where requestinguser = ?";
	public static final String GET_USER_ROLE_FOR_SYSADMIN = "SELECT usertype, department from userrole where userid = ?";

}
