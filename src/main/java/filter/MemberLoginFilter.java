package filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import DAO.Authorization;
import DAO.Register;
import DAO.User;

/**
 * Servlet Filter implementation class MemberLoginFilter
 */
public class MemberLoginFilter implements Filter {
    protected ServletContext servletContext;
    protected HttpServletRequest servletRequest;
    private DataSource dataSource;
    private Connection connect;
    

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
    
    /**
     * Default constructor. 
     */
    public MemberLoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	public boolean authorizedToViewPage(String pagename, Integer userid, Connection connection) throws SQLException {

		// By default we are unauthorized to view any page.
		boolean isAuthorized = false;
		
		// Check if the user is of a type which is authorized to view this page.
		PreparedStatement statement = connection.prepareStatement("SELECT pg.modelandviewname FROM pagepermissions pgp, pages pg, users u, userrole ur WHERE u.userid=ur.userid AND ur.usertype=pgp.userrole AND pgp.pageid=pg.pageid AND pg.modelandviewname=? AND u.userid=?;");

		statement.setString(1, pagename);
		statement.setInt(2, userid);
		ResultSet rs = statement.executeQuery();
		if (rs.next()) {
			if (rs.getString("modelandviewname").equals(pagename)) {
				isAuthorized = true;
			} 
		}
		rs.close();
		statement.close();
		
		return isAuthorized;
		
	}

	
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		
		HttpServletResponse resp = (HttpServletResponse)response;
        servletRequest = (HttpServletRequest)request;
		
		try
		{
	       String path = servletRequest.getRequestURI();
	       
	       User user = (User)servletRequest.getSession(true).getAttribute("user");
	  
	       if ((! path.equals("/cse545_WBDS/login/signout")) && (! path.equals("/cse545_WBDS/unauthorized")) && (! path.startsWith("/cse545_WBDS/img/")) && (! path.startsWith("/cse545_WBDS/css/")) && (! path.startsWith("/cse545_WBDS/js/")) && (! path.startsWith("/cse545_WBDS/assets/")) && (! path.equals("/cse545_WBDS/")) && (! path.equals("/cse545_WBDS/register")) && (! path.equals("/cse545_WBDS/login")) && (! path.equals("/cse545_WBDS/forgotpassword"))&& (! path.equals("/cse545_WBDS/resetpassword")) )
	       {
		       // Check if they are not logged in yet and accessing a page which requires login.
		       if (user == null)  {
		    	   //System.out.println("Redirecting: User not logged in request=" + path);
		    	   resp.sendRedirect("/cse545_WBDS/login");
		    	   return; //break filter chain, requested JSP/servlet will not be executed		
		       }
		       // Check if they are permitted access to the page they are requesting
		       Class.forName("com.mysql.jdbc.Driver");
		       connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "NUswA3Wu");
		       
		       String pagename = path.substring(13);
		       
		       if (! authorizedToViewPage(pagename, user.getUserId(), connect))
		       {
		    	   resp.sendRedirect("/cse545_WBDS/unauthorized");
		    	   return; //break filter chain, requested JSP/servlet will not be executed	
		       }
	       }
	       
		}
		catch (Exception E)
		{
			System.out.println("Exception encountered while in filter login" + E.getMessage());
			resp.sendRedirect("/cse545_WBDS/unauthorized");
			
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
