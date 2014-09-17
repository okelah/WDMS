
<%@include file="header.jsp"%>

${message}

<form action="/cse545_WBDS/resetpassword?id=${id}" method="post">
	<div class="modal-body">
		<div class="control-group">
			<div class="controls">
				<label class="control-label" for="inputNewPassword">Input New
					Password</label> <input type="password" name="inputNewPassword"
					id="inputNewPassword" placeholder="NewPassword">
			</div>
			<input type="submit" value="Reset Password" class="btn btn-primary" />
		</div>
	</div>

</form>

<%@include file="footer.jsp"%>