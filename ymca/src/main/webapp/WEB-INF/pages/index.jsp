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
		int columns = (int) Math.ceil(Math.sqrt(modules.size()));
		int rows = (int) Math.ceil(modules.size() / (double) columns);
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
		for (final String jsPath : ModuleUtils.getJavascriptLinks(modules))
		{
			%>
			<script src="<%=jsPath%>"></script>
			<%
		}
		%>
		<script src="js/modules.js"></script>
		<script> var sessionId = "<%= session.getId()%>";</script>
		<style>
			@media (min-width:768px)
			{
				.module
				{
					height: <%=1100 / rows %>px;
					height: calc((100vh - 10em) / <%=rows%>);
				}
			}
			.module .panel
			{
				height:95%;
				height: calc(100% - 15px);
			}
			.module .panel-heading
			{
				padding-top: 1px;
				padding-bottom: 1px;
			}
			.close span.glyphicon
			{
				top: 0.55em;
				right: 0.5em;
				font-size: 0.5em;
			}
		</style>
	</head>
	<body>
		<div class="container-fluid">
			<%@ include file="./includes/header.jspf" %>
			<h1>Hello World!</h1>
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