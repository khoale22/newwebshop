<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.model.Cart"%>
<%@page import="com.entity.User"%>
<%@page import="com.service.CategoryService"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>

<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
</head>
<body>
	<%
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			cart = new Cart();
			session.setAttribute("cart", cart);

		} else {
			cart = (Cart) session.getAttribute("cart");
		}

		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
		}
		CategoryService categoryService = new CategoryService();
	%>

	<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
		<!-- Brand -->
		<div style="float: left; width: 80%;">
			<!-- Links -->
			<ul class="navbar-nav">
				<li class="nav-item"><a class="nav-link"
					href="${pageContext.request.contextPath}/">Home</a></li>
				<c:if test="${user == null}">
					<li class="nav-item"><a class="nav-link" href="login">Login</a></li>
					<li class="nav-item"><a class="nav-link" href="register">Register</a></li>
				</c:if>
				<li class="nav-item"><a class="nav-link" href="cart">Cart</a></li>
			</ul>
		</div>
		<c:if test="${user != null}">
			<div style="float: right; width: 20%; text-align: right;">
				<a class="navbar-brand" href="#" style="float: left; font-size: 23px;"> <strong>
						Hello ${user.userName} </strong></a> <a class="nav-link" href="logOut"
					style="background-color: white; float: right;">Logout</a>
			</div>
		</c:if>
	</nav>
	<br>
</body>
</html>