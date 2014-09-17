
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

		<div class="modal hide" id="newFolder">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">X</button>
			<h3>Create a new folder</h3>
		  </div>
		  <form action="directory/create" method="post">
		  <div class="modal-body">
			<p>
				<label>Folder name</label>
				<input type="text" name="name"><br>
				<input type="hidden" name="location" value="<c:out value="${currentLocation }"/>"/><br>
			</p>
		  </div>
		  <div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">Cancel</a>
			<input type="submit" value="Create" class="btn btn-primary"/>
		  </div>
		  </form>
		</div>

		<div class="modal hide" id="sharefiles">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">X</button>
			<h3>Share file(s)</h3>
		  </div>
		  <form action="document/share" method="post">
		  <div class="modal-body">
			<p>
				<label>Share with (email address)</label>
				<input type="text" name="shareemail" id="shareemail"><br>
				<table>
					<tr>
						<td><input type="checkbox" name="read" value="read">read</td>
						<td><input type="checkbox" name="update" value="update">update</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="delete" value="delete">delete</td>
						<td><input type="checkbox" name="lock" value="lock">lock</td>
					</tr>
				</table>
				<input type="hidden" name="filenames" id="filenames"><br>
			</p>
		  </div>
		  <div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">Cancel</a>
			<input type="submit" value="Share" class="btn btn-primary"/>
		  </div>
		  </form>
		</div>

            <h1>My Documents</h1>
            <p>


				<div class="btn-group">
				  
				  <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">Action<span class="caret"></span></a>
				  <ul class="dropdown-menu">
					<c:choose>
						<c:when test="${currentLocation != null && currentLocation != 1 }">
				    		<li><a data-toggle="modal" href="#newFolder" ><i class="icon-folder-open"></i> Create Folder Here</a></li>
				    		<li><a href="/cse545_WBDS/upload?dir=<c:out value="${currentLocation }"/>"><i class="icon-upload"></i> Upload files here</a></li>
				  		</c:when>
				  	</c:choose>
				  </ul>
				</div>


            	<table class="table table-striped">
				<thead>
					<tr>
						<td>
  						<b>Name</b></td>
						<td><b>Owner</b></td>
						<td><b>Department</b></td>
						<td><b>Creation Date</b></td>
						<td><b>Last Updated</b></td>
						<td><b>Last Accessed</b></td>
						<td><b>Checked Out to</b></td>
						<td><b>Delete</b></td>
						<td><b>Check-out</b></td>
						<td><b>Share</b></td>
						<td><b>version(s)</b></td>

					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${parentDirectory != 0 && parentDirectory != -1 }">
						<tr>
							<td>
							<a href="document?dir=${parentDirectory }">Parent Directory</a>							</td>
							<td></td>
							<td>-</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
						</c:when>
						<c:when test="${parentDirectory == -1 }">

						</c:when>
						<c:otherwise>
						<tr>
							<td>
							<a href="document">Parent Directory</a>							</td>
							<td></td>
							<td>-</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
						</c:otherwise>
					</c:choose>
					<c:forEach var="directory" items="${directories}">
						<c:choose>
						<c:when test="${directory.hasReadAccess(currentUser)}">
						<tr>
							<td>
							<a href="document?dir=${directory.directoryID }">${directory.directoryName }</a></td>
							<td>${ directory.owner.emailId }</td>
							<td>-</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<c:choose>
								<c:when test="${directory.getParentDirectoryID()!=1}">
									<td><a href="directory/delete?dir=${directory.directoryID }&current=${currentLocation }">Delete</a></td>
								</c:when>
								<c:otherwise>
									<td></td>
								</c:otherwise>
							</c:choose>
							<td></td>
							<td></td>
							<td></td>
						</tr>
						</c:when>
						</c:choose>
					</c:forEach>	
					<c:forEach var="document" items="${documents}">
						<c:choose>
						<c:when test="${auth.canRead(document.docguid, currentUser, document.groupid)}">
						<tr>
							<td>
							<button class="btn" type="button" data-toggle="modal" data-target="#requestkey" onClick="document.getElementById('doc').value = '${document.docguid }';">${document.docname }</button>
							</td>
							<td>${document.owner.emailId }</td>
							<td>-</td>
							<td><fmt:formatDate value="${document.creationTime}" pattern="MM/dd/yyyy HH:mm:ss"/></td>
							<td><fmt:formatDate value="${document.modifiedTime}" pattern="MM/dd/yyyy HH:mm:ss"/></td>
							<td><fmt:formatDate value="${document.lastAccessTime}" pattern="MM/dd/yyyy HH:mm:ss"/></td>
							<c:choose>
								<c:when test="${document.getCheckedOutUser() != 0}">
									<td>${document.getCUser().emailId }</td>
								</c:when>
								<c:otherwise>
									<td>-</td>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${auth.canDelete(document.docguid, currentUser, document.groupid) && ((document.getCheckedOutUser() != 0 && document.getCheckedOutUser() == currentUser) || document.getCheckedOutUser() == 0)}">
									<td><a href="document/delete?doc=${document.docguid }&dir=${currentLocation }">Delete</a></td>
								</c:when>
								<c:otherwise>
									<td>-</td>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${auth.canLock(document.docguid, currentUser, document.groupid)}">
									<c:choose>
										<c:when test="${document.getCheckedOutUser() != 0 && document.getCheckedOutUser() == currentUser}">
											<td><a href="document/checkin?doc=${document.docguid }&dir=${currentLocation }">Check-in</a></td>
										</c:when>
										<c:when test="${document.getCheckedOutUser() != 0 && document.getCheckedOutUser() != currentUser}">
											<td>-</td>
										</c:when>
										<c:otherwise>
											<td><a href="document/checkout?doc=${document.docguid }&dir=${currentLocation }">Check-out</a></td>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<td>-</td>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${auth.canShare(document.docguid, currentUser)}">
									<td><button class="btn" type="button" data-toggle="modal" data-target="#sharefiles" onClick="document.getElementById('filenames').value = '${document.docguid }';">Share</button></td>
								</c:when>
								<c:otherwise>
									<td></td>
								</c:otherwise>
							</c:choose>
							<td><a href="revisions?groupid=${document.groupid}">view</a></td>
						</tr>
						</c:when>
						</c:choose>
					</c:forEach>	
				</tbody>
				</table>
        </div><!--/span-->
      </div><!--/row-->

 <%@include file="footer.jsp" %>
