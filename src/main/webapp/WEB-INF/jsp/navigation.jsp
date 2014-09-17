<%@page import="net.tanesha.recaptcha.ReCaptchaFactory"%>
<%@page import="net.tanesha.recaptcha.ReCaptcha"%>
<%@page import="org.springframework.context.annotation.Import"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import = "DAO.Role" %>

<c:choose>
<c:when test='${user == null}'> 
 
    	<div class="modal hide" id="myModal">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">X</button>
			<h3>Log in</h3>
		  </div>
		  <form action="login" method="post">
		  <div class="modal-body">
			<p>
				<label>Username</label>
				<input type="text" name="username" value=""><br>
				<label>Password</label>
				<input type="password" name="password" value=""><br>
				
				<c:if test="${!disablecap}">
					<script type="text/javascript" src="https://www.google.com/recaptcha/api/challenge?k=6Lc449gSAAAAAIA4OU3n1rWhTlEoVbPzAjoEGJvs"></script>
	
					<noscript>
				     <iframe src="https://www.google.com/recaptcha/api/noscript?k=6Lc449gSAAAAAIA4OU3n1rWhTlEoVbPzAjoEGJvs"
				         height="300" width="500" frameborder="0"></iframe><br>
				     <textarea name="recaptcha_challenge_field" rows="3" cols="40">
				     </textarea>
				     <input type="hidden" name="recaptcha_response_field"
				         value="manual_challenge">
				  	</noscript>
				</c:if>
				
				<hr>
				<a href="/cse545_WBDS/forgotpassword">Forgot Password</a>
				
				
			</p>
		  </div>
		  <div class="modal-footer">
		  	
			<a href="#" class="btn" data-dismiss="modal">Cancel</a>
			<input type="submit" value="Login" class="btn btn-primary"/>
		  </div>
		  </form>
		</div>
	    <div class="navbar navbar-fixed-top">
	      <div class="navbar-inner">
	        <div class="container">
	          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	          </a>





	          <a class="brand" href="#">WDMS : Web Document Management System</a>

	          <div class="btn-group pull-right">
	          	
	            <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
	            							
							                <i class="icon-user"></i> <c:if test="${!disablecap}">Login /</c:if> Create Account
							                <span class="caret"></span>
							              </a>
							              <ul class="dropdown-menu">
							              	<li> <a href="/cse545_WBDS/"><i class="icon-home"></i> Home</a></li>
							                <c:if test="${!disablecap}">
							       
								                <li><a data-toggle="modal" href="#myModal" ><i class="icon-circle-arrow-right"></i> Login</a></li>
								                <li class="divider"></li>
							                </c:if>
							                <li><a href="/cse545_WBDS/register"><i class="icon-pencil"></i> Create Account</a></li>
			              </ul>

	          </div>
	          <div class="nav-collapse">

	          </div><!--/.nav-collapse -->
	        </div>
	      </div>
    	</div>
	</c:when>
	<c:otherwise>
     <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="#">WDMS : Web Document Management System</a>
          <div class="btn-group pull-right">
            <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
              <i class="icon-user"></i> <c:out value="${user.getEmailId()}"/><br/>
              <span class="caret"></span>
            </a>
            <ul class="dropdown-menu">
              <li><a href="/cse545_WBDS/profile">Profile</a></li>
              <li class="divider"></li>
              <li><a href="/cse545_WBDS/login/signout">Sign Out</a></li>
            </ul>
          </div>
          <div class="nav-collapse">
            <ul class="nav">

         
		   	<c:if test="${(user.getRole() == 'RegularEmployee') || (user.getRole() == 'Guest') || (user.getRole() == 'DeptManagement') || (user.getRole() == 'CorpManagement')}">
           		<li><a href="/cse545_WBDS/document">Documents</a></li>
           	</c:if>
           	
           	<c:if test="${(user.getRole() == 'RegularEmployee') || (user.getRole() == 'DeptManagement') || (user.getRole() == 'CorpManagement')}">
           		<li><a href="/cse545_WBDS/upload">Upload Document(s)</a></li>
           	</c:if>
   
		   	<c:if test="${user.getRole() == 'SysAdmin'}">
           		<li><a href="/cse545_WBDS/sysadmin/viewlog">Download Log</a></li>
           	</c:if>
           
		   
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
    
	</c:otherwise>
	</c:choose>