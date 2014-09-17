
 <%@include file="header.jsp" %>

    <div class="container">
      <div class="row-fluid">

        <div class="span9">

		<div class="modal hide" id="requestkey">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">X</button>
			<h3>Decrypt and download</h3>
		  </div>
		  <form action="document/get" method="post">
		  <div class="modal-body">
			<p>
				<label>Decryption key (Should be exactly 16 digits)</label>
				<input type="text" name="key" maxlen="16"><br>
				<input type="hidden" name="doc" id="doc" value=""/><br>
			</p>
		  </div>
		  <div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">Cancel</a>
			<input type="submit" value="Decrypt & Download" class="btn btn-primary"/>
		  </div>
		  </form>
		</div>

            <h1>Document Revision History</h1>
            <p>
            <c:if test="${errormessage != null}">
            	<div class="alert alert-error">${errormessage}</div>
            </c:if>
				<span class="label label-info">Info</span> Version numbers increase (newer versions have a higher number and are listed first).
            	<table class="table table-striped">
				<thead>
					<tr>
						<td><b>Revision</b></td>
						<td><b>Author</b></td>
						<td><b>Commit ID</b></td>
						<td><b>Comment</b></td>
						<td><b>Date</b></td>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="revision" items="${revisions}">
						<tr>
							<td>${revision.version}</td>
							<td>
								${revision.getMUser().emailId }
							</td>
							<td><button class="btn" type="button" data-toggle="modal" data-target="#requestkey" onClick="document.getElementById('doc').value = '${revision.docguid }';">${revision.docname }</button></td>
							<td>${revision.comment}</td>
							<td><fmt:formatDate value="${revision.modifiedTime}" pattern="MM/dd/yyyy HH:mm:ss"/></td>
						</tr>
					</c:forEach>	
				</tbody>
				</table>
        </div><!--/span-->
      </div><!--/row-->

 <%@include file="footer.jsp" %>
