
<%@include file="header.jsp"%>

<div class="container">
	<div class="row-fluid">

		<div class="span9">
			<br />${message}


			<h3>Users to Activate</h3>
			<table class="table table-striped">
				<thead>
					<tr>
						<td><input type="checkbox" id="inlineCheckbox1"
							value="option1"> <b>Email address</b></td>
						<td><b>Requested Account Type</b></td>
						<td><b>Activate</b></td>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="user" items="${userList}">
						<tr>
							<td><c:out value="${user.emailId}" /></td>
							<td>${user.getRole()}</td>
							<td><a
								href="/cse545_WBDS/sysadmin/activate?email=${user.emailId}">Activate</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>


			<h3>Deactivate User Account</h3>
			<form class="form-horizontal"
				action="/cse545_WBDS/sysadmin/deactivate" method="POST">
				<div class="control-group">
					<label class="control-label" for="inputEmail">Email</label>
					<div class="controls">
						<input type="text" name="inputEmail" placeholder="Email">
						<br> <br> <input type="submit"
							value="Deactivate Account">
					</div>
				</div>
			</form>


			<h3>Create User Account</h3>
			<form class="form-horizontal"
				action="/cse545_WBDS/sysadmin/createuser" method="POST">
				<div class="control-group">
					<label class="control-label" for="inputEmail">Email</label>
					<div class="controls">
						<input type="text" name="inputEmail" placeholder="Email">
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="inputPassword">Password</label>
					<div class="controls">
						<input type="password" name="inputPassword" placeholder="Password">
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="Account">Account Type</label>
					<div class="controls">
						<select name="selectionField">
							<option value="R">Regular Employee</option>
							<option value="D">Department Manager</option>
							<option value="C">Corporate Manager</option>
							<option value="S">System Administrator</option>
						</select> <br> <br> <input type="submit"
							value="Create My Account" />
					</div>
				</div>
			</form>


			<h3>DeleteUser</h3>
			<form class="form-horizontal"
				action="/cse545_WBDS/sysadmin/deleteuser" method="POST">
				<div class="control-group">
					<label class="control-label" for="inputEmail">Email</label>
					<div class="controls">
						<input type="text" name="inputEmail" placeholder="Email">
						<br> <br> <input type="submit" value="Delete Account">
					</div>
				</div>
			</form>


			<h3>Update User</h3>
			<form class="form-horizontal"
				action="/cse545_WBDS/sysadmin/updateuser" method="POST">
				<div class="control-group">
					<label class="control-label" for="inputEmail">Email</label>
					<div class="controls">
						<input type="text" name="inputEmail" placeholder="Email">
					</div>
				</div>
				<div class="control-group">

					<label class="control-label" for="Account">Account Type</label>
					<div class="controls">
						<select name="selectionField">
							<option value="G">Guest</option>
							<option value="R">Regular Employee</option>
							<option value="D">Department Manager</option>
							<option value="C">Corporate Manager</option>
							<option value="S">System Administrator</option>
						</select> <br> <br> <input type="submit" value="Update Account">
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<%@include file="footer.jsp"%>