 <%@include file="header.jsp" %>
 
     <div class="container">
      <div class="row-fluid">

        <div class="span9">
 
		<h3>Document Upload:</h3>
		Uploading to current dir: <c:out value="${uploaddir}"/>
		Select a file to upload: <br />
		<form action="document/upload" method="post" enctype="multipart/form-data">
		<input type="file" name="file" id="file" size="50" />
		<br />
		<label>Comment:</label><input type="text" name="comment" id="comment" maxlength = "255"size="50" />
		<br />
		
		<label>Encryption key (optional)</label>
		<input type="text" name="key" id="key" maxlength="16">
		<br />
		<span class="label label-info">Restriction!</span>Encryption key must be EXACTLY 16 characters.<br><br>
		<input type="submit" value="Upload File" />
		<input type="hidden" name="location" id="location" value="<c:out value="${uploaddir}"/>" />
		
		</form>

        </div><!--/span-->
      </div><!--/row-->


 <%@include file="footer.jsp" %>