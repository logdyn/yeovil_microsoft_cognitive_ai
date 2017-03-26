<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="models.modules.Module"%>
<%@ page import="java.util.Collection"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<%@ include file="./includes/bootstrapImports.jspf" %>
		<title>Home</title>
		<%
		final Collection<Module> modules = (Collection<Module>) request.getAttribute("modules");
		final int columns = (int) Math.ceil(Math.sqrt(modules.size()));
		final int rows = (int) Math.ceil(modules.size() / (double) columns);
		final int stretchedRows = (rows * columns) - modules.size();
		%>
	</head>
	<body>
		<div class="container-fluid">
			<%@ include file="./includes/header.jspf" %>
			<h1>Hello World!</h1>
			<div class="row">
			<%
			int c = 0; // current column index
			int r = 0; // current row index
			if (r < stretchedRows)
			{
				c++;
			}
			for (final Module module : modules)
			{
				if (c++ >= columns)
				{
					c = 1;
					r++;
					if (r < stretchedRows)
					{
						c++;
					}
				}
				%>
				<div class="module col-sm-<%= 12 / (r < stretchedRows ? (columns - 1) : columns) %>">
					<div class="panel panel-default">
						<div class="panel-heading">
							<%=module.getName()%>
							<a class="close" aria-label="close">&times;</a>
						</div>
						<div class="panel-body">
							<jsp:include page="<%=module.getContentpath()%>" />
						</div>
					</div>
				</div>
			<%
			}
			%>
			</div>
		</div>
	</body>
</html>