 <%@include file="header.jsp" %>

    <div class="container">
      <div class="row-fluid">

        <div class="span9">
        
        
        
       
        
        <div class="modal hide" id="modifypswdmodal">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">X</button>
			<h3>Modify Your Password</h3>
			
		  </div>
		  <p>
		  <form action="profile/modifypswd" method="post">
		    <div class="modal-body">
		    <div class="alert alert-info">
			    <b>Password requirements</b> <br><br>It must contain 8-16 characters, with:
			    <ul>
			    	<li>no whitespaces</li>
			    	<li>At least - 1 lowercase</li>
			    	<li>At least 1 upper case</li>
			    	<li>At least 1 non-alphanumeric character</li>
			    </ul>
		    </div>
			 <div class="control-group">
		   	 <label class="control-label" for="inputOldPassword"><span style="color: #FF0000">* </span>Old Password</label>
				<div class="controls">
					<input type="password" name="inputOldPassword" placeholder="OldPassword">
				</div>
			 </div>
			<div class="control-group">
			<label class="control-label" for="inputNewPassword"><span style="color: #FF0000">* </span>New Password</label>
			    <div class="controls">
			      <input type="password" name="inputNewPassword" id="inputNewPassword" placeholder="NewPassword">
			    </div>
			  </div>
		  </div>
		  
		  <div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">Cancel</a>
			<input type="submit" value="Modify" class="btn btn-primary"/>
		  </div>
		  
		  </form>
		</div>
		</p>
		
				<div>
				<h3><b>User Profile</b></h3>
				</div>
				 <li class="divider"></li>
				<div>
				<label><b>UserEmail</b></label>
				<label>${user.emailId }</label>
				</div>
				<div>
				<label><b>UserId</b></label>
				</div>
				<label>${user.userId }</label>
				<div>
				<label><b>UserType</b></label>
				<label>${user.role }</label>
				</div>
				
				<div>
				<label><b>Active Status</b></label>
				<c:choose>
						<c:when test="${user.active}">
				<label>active</label>
				</c:when>
				<c:otherwise>
				<label>not active</label>
				</c:otherwise>
				</c:choose>
				</div>
				 <li class="divider"></li>
  
		
				    		<a data-toggle="modal" href="#modifypswdmodal" ></i>ModifyPassword</a>
				    		${message}
				
        
        </div><!--/span-->
      </div><!--/row-->

 <%@include file="footer.jsp" %>
        