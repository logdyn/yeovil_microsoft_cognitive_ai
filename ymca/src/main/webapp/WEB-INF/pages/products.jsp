<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="storage.ProductCache"%>
<%@page import="models.Product"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<%@ include file="./includes/bootstrapImports.jspf" %>
		<title>Yeovil Healthcare Products</title>
		<style>
			.product
			{
				outline: black solid 1px;
				margin: 1em 0px;
				padding: 1em 0px;
			}
			@media (min-width: 992px)
			{
				.product
				{
					display: flex;
					align-items: center;
				}
			}
		</style>
		<script src="./js/xhttp.js"></script>
		<script>
			function addToOrder(id)
			{
				xhttp.sendRequest("productid=" + id, responseHandler, '/AddProductToOrder');
			}
			function responseHandler(responseText)
			{
				alert(responseText);
			}
		</script>
	</head>
	<body>
		<div class="container-fluid">
			<%@ include file="./includes/header.jspf" %>
			<% final boolean grid = "grid".equals(request.getParameter("view")); %>
			<h1>Products</h1>
			<%for (Product product : ProductCache.getInstance().getProducts()) { %>
			<div class="col-sm-6<%=grid ?"":" col-md-12" %> product">
				<div class="col-sm-12<%=grid ?"":" col-md-2" %>">
					<img src="http://placehold.it/300x300?text=<%=product.getName() %>" class="img-responsive"></img>
				</div>
				<div class="col-sm-8<%=grid ?"":" col-md-2" %>">
					<%=product.getName() %>
				</div>
				<div class="col-sm-4<%=grid ?"":" col-md-push-2 col-md-2" %>">
					Quantity: <%=product.getQuantity() %>
				</div>
				<div class="col-sm-12<%=grid ?"":" col-md-pull-2 col-md-2" %>">
					<%=product.getDescription() %>
				</div>
				<div class="col-sm-6<%=grid ?"":" col-md-2" %>">
					Price: <%=product.getPriceString() %>
				</div>
				<div class="col-sm-6<%=grid ?"":" col-md-2" %>">
					<button type="button" class="btn btn-success btn-block" onclick="addToOrder('<%=product.getId() %>');">Add to cart<span class="glyphicon glyphicon-shopping-cart" /></button>
				</div>
			</div>
			<% } %>
		</div>
	</body>
</html>