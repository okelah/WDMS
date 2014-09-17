 <%@include file="header.jsp" %>

    <div class="container">
      <div class="row-fluid">

        <div class="span9">
			<p>
			<div>Not Authorized!</div>
			</p>
			<div>
			<c:if test="${message != null}">
            	<div class="alert alert-error">${message}</div>
            </c:if>
			</div>
        </div><!--/span-->
      </div><!--/row-->
	</div>
 <%@include file="footer.jsp" %>