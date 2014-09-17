<%@page import="net.tanesha.recaptcha.ReCaptchaFactory"%>
<%@page import="net.tanesha.recaptcha.ReCaptcha"%>
<%@include file="header.jsp"%>

<div class="container">
	<div class="row-fluid">

		<div class="span9">

			<h1>Create Account</h1>
			${message}
			<p>
			<form class="form-horizontal" action="/cse545_WBDS/register"
				method="POST">
				<div class="control-group">
					<label class="control-label" for="inputEmail">Email</label>
					<div class="controls">
						<input type="text" name="inputEmail" placeholder="Email">
					</div>
				</div>
				<div class="control-group">
					<div class="alert alert-info">
				    <b>Password requirements</b> <br><br>It must contain 8-16 characters, with:
				    <ul>
				    	<li>no whitespaces</li>
				    	<li>At least - 1 lowercase</li>
				    	<li>At least 1 upper case</li>
				    	<li>At least 1 non-alphanumeric character</li>
				    </ul>
			   		</div>
					<label class="control-label" for="inputPassword">Password</label>
					<div class="controls">
						<input type="password" name="inputPassword" placeholder="Password">
					</div>
				</div>
				<div class="control-group">
					
					<label class="control-label" for="inputPassword">Account
						Type</label>
					<div class="controls">


						<select name="selectionField">
							<option value="G">Guest</option>
							<option value="R">Regular Employee</option>
							<option value="D">Department Manager</option>
							<option value="C">Corporate Manager</option>
							<option value="S">System Administrator</option>
						</select> <br>
						<br>
					</div>
					<label class="control-label" for="inputPassword">Department
						Name</label>
					<div class="controls">
						<select name="departmentField">
							<option value="1">Human Resources</option>
							<option value="2">Logistic and supply</option>
							<option value="3">IT support</option>
							<option value="4">Sales and promotion</option>
							<option value="5">Research & Development</option>
							<option value="">Finance</option>
							<option value="S">Company Management</option>
						</select>
					</div>

				</div>




					<script type="text/javascript" src="https://www.google.com/recaptcha/api/challenge?k=6Lc449gSAAAAAIA4OU3n1rWhTlEoVbPzAjoEGJvs"></script>
	
					<noscript>
				     <iframe src="https://www.google.com/recaptcha/api/noscript?k=6Lc449gSAAAAAIA4OU3n1rWhTlEoVbPzAjoEGJvs"
				         height="300" width="500" frameborder="0"></iframe><br>
				     <textarea name="recaptcha_challenge_field" rows="3" cols="40">
				     </textarea>
				     <input type="hidden" name="recaptcha_response_field"
				         value="manual_challenge">
				  	</noscript>
					<input type="submit" value="Create My Account" />


				
		</div>
		</form>


		</p>


	</div>
	<!--/span-->
</div>
<!--/row-->

<%@include file="footer.jsp"%>
