<%@page import="net.tanesha.recaptcha.ReCaptchaFactory"%>
<%@page import="net.tanesha.recaptcha.ReCaptcha"%>
<%@include file="header.jsp"%>

<div class="container">
	<div class="row-fluid">

		<div class="span9">

			<h1>Login</h1>
			${message}
			<p>
				<c:if test="${error == true}">
					<div class="alert alert-error">Invalid email address or
						password.</div>
				</c:if>
			<div class="alert">
				<button type="button" class="close" data-dismiss="alert">×</button>
				<strong>Warning!</strong> Click Login/Create Account, then "login" to login.
			</div>
			
			</p>


		</div>
		<!--/span-->
	</div>
	<!--/row-->

	<%@include file="footer.jsp"%>