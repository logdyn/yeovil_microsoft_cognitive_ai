<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="java.util.UUID"%>
<%@page import="storage.UserCache"%>
<%@page import="models.user.User"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<%@ include file="./includes/bootstrapImports.jspf" %>
		<title>Account</title>
		<style>
			input.text[readonly]
			{
				border: none;
				background-color: inherit;
				box-shadow: none;
				color: inherit;
			}
			.form-group label
			{
				width: 100%;
			}
		</style>
		<%
			final boolean isEditing = "edit".equals(request.getParameter("mode"));
			final String userID = request.getParameter("user");
			final User editUser = userID==null ? null : 
				UserCache.getInstance().getUser(UUID.fromString(userID));
		%>
		<script src="./js/xhttp.js"></script>
		<script language="javascript">
			function sendUser()
			{
				var formData = {<%if (userID != null){ %>userid:'<%= userID%>'<%}%>};
				var inputs = document.getElementById('userForm').getElementsByTagName('input');
				for (var i = 0; i < inputs.length; i++)
				{
					formData[inputs[i].name] = encodeURIComponent(inputs[i].value);
				}
				xhttp.sendRequest('newUser=' + JSON.stringify(formData), function(responseText)
				{
					var response = JSON.parse(responseText);
					if (typeof response.error !== 'undefined')
					{
						//if the server responseds with a error then display it to the user.
						alert(response.error);
					}
					else
					{
						//if no error then go to the viewer page.
						location.assign("./Account?user=" + response.userid);
					}
				});
			}
		
		</script>
	</head>
	<body>
		<div class="container-fluid">
			<%@ include file="./includes/header.jspf" %>
			<%!
			private String getUserAttribute(boolean isEditing, String attributeName, String attributeValue)
			{
				return getUserAttribute(isEditing, attributeName, attributeValue, null);
			}
			
			private String getUserAttribute(boolean isEditing, String attributeName, String attributeValue, String type)
			{
				final String classType = isEditing? "":" text";
				final String readOnly = isEditing? "":" readonly='readonly'";
				final String inputType = type != null ? type : "text";
				final String textType = inputType != "password" ? "value" : "placeholder";
				
				//Ternary operator didn't seem to like attributeValue == null ? attributeValue : "";
				if (null == attributeValue)
				{
					attributeValue = ""; 
				}
				
				return String.format("<label>%s: <input %s='%s' type='%s' class='form-control%s' name='%s' title='%s'%s/></label>", 
						attributeName, textType, attributeValue, inputType, classType, attributeName.toLowerCase(), attributeName, readOnly);
			}
			%>
			<div class="col-sm-6 col-sm-offset-3">
				<form id="userForm" method="post">
					<div class="form-group">
						<%=getUserAttribute(isEditing, "Username", editUser==null?null:editUser.getUsername())%>
					</div>
					<%	if(editUser == null)
						{
							%><div class="form-group">
								<%=getUserAttribute(isEditing, "Password", "User password here", "password")%>
							</div>
							<div class="form-group">
								<%=getUserAttribute(isEditing, "Confirm Password", "User password here", "password")%>
							</div><%
						}%>
					<div class="form-group">
						<%=getUserAttribute(isEditing, "Forename", editUser==null?null:editUser.getForename())%>
					</div>
					<div class="form-group">
						<%=getUserAttribute(isEditing, "Surname", editUser==null?null:editUser.getSurname())%>
					</div>
					<fieldset>
						<legend>Address:</legend>
						<div class="form-group">
							<%=getUserAttribute(isEditing, "Address 1", editUser==null?null:editUser.getAddress().getBuilding())%>
						</div>
						<div class="form-group">
							<%=getUserAttribute(isEditing, "Address 2", editUser==null?null:editUser.getAddress().getStreet())%>
						</div>
						<div class="form-group">
							<%=getUserAttribute(isEditing, "Address 3", editUser==null?null:editUser.getAddress().getCity_town())%>
						</div>
						<div class="form-group">
							<%=getUserAttribute(isEditing, "Address 4", editUser==null?null:editUser.getAddress().getArea_code())%>
						</div>
						<div class="form-group">
							<%=getUserAttribute(isEditing, "Address 5", editUser==null?null:editUser.getAddress().getCountry())%>
						</div>
					</fieldset>
					<% if (!isEditing) {%>
					<a class="btn btn-warning" href="?mode=edit&user=<%=userID %>">Edit</a>
					<% } else { %>
					<button type="button" class="btn btn-success" onclick="sendUser();">Submit</button>
					<% if (editUser != null) {%>
					<a class="btn btn-danger pull-right" href="?user=<%=userID %>">Cancel</a>
					<% } } %>
				</form>
			</div>
		</div>
	</body>
</html>