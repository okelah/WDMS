
 <%@include file="header.jsp" %>

    <div class="container">
      <div class="row-fluid">

        <div class="span9">

	<h2>Password reset</h2>
	
	<c:if test="${emailsent == true}">
	<div class="alert alert-success">Email will be sent if you are exiting WBDS user.</div>
	</c:if>
	<c:if test="${invalidemail == true}">
	<div class="alert alert-error">Invalid email address.</div>
	</c:if>
	<form action="forgotpassword" method="post">
	<label>email address</label>
	<input type="text" name="email">
	<button type="submit" class="btn">Send reset link</button>
	</form>
        </div><!--/span-->
      </div><!--/row-->

 <%@include file="footer.jsp" %>
