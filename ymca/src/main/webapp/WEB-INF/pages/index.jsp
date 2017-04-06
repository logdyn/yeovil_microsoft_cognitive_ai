<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="models.modules.ModuleUtils"%>
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
		int columns = ModuleUtils.getColumns(modules.size());
		int rows = ModuleUtils.getRows(modules.size());
		final int stretchedRows = (rows * columns) - modules.size();
		if (Boolean.parseBoolean(request.getParameter("flip")))
		{
			final int temp = columns;
			columns = rows;
			rows = temp;
		}
		for (final String cssPath : ModuleUtils.getCssLinks(modules))
		{
			%>
			<link rel="stylesheet" type="text/css" href="<%=cssPath%>" />
			<%
		}
		%>
		<script src="js/LoggingEndpoint.js"></script>
		<script src="js/modules.js"></script>
		<%
		for (final String jsPath : ModuleUtils.getJavascriptLinks(modules))
		{
			%>
			<script src="<%=jsPath%>"></script>
			<%
		}
		%>
		<script> var sessionId = "<%= session.getId()%>";</script>
		<style>
			@media (min-width:768px)
			{
				.module
				{
					height: <%=870 / (rows == 0 ? 1 : rows) %>px;
					height: calc((100vh - 5em) / <%= rows == 0 ? 1 : rows %>);
				}
			}
		</style>
	</head>
	<body>
		<div class="container-fluid">
			<%@ include file="./includes/header.jspf" %>
			<div class="row">
			<%
			int c = 0; // current column index
			int r = 0; // current row index
			for (final Module module : modules)
			{
				if (c++ == columns)
				{
					c = 1;
					r++;
					if (rows - r <= stretchedRows)
					{
						c++;
					}
				}
				%>
				<div class="module col-sm-<%= 12 / (rows - r <= stretchedRows ? (columns - 1) : columns) %>">
					<div class="panel panel-default">
						<div class="panel-heading">
							<%=module.getName()%>
							<a class="close closeBtn" aria-label="close">&times;</a>
							<a class="close fullscreenBtn"><span class="glyphicon glyphicon-fullscreen"></span></a>
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