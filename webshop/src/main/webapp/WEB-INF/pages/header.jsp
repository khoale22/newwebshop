<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.model.Cart"%>
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
		
		 CategoryService categoryService = new CategoryService();
	%>

	<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
		<!-- Brand -->
		<a class="navbar-brand" href="${pageContext.request.contextPath}/">Home</a>

		<!-- Links -->
		<ul class="navbar-nav">
			<li class="nav-item"><a class="nav-link" href="login">Login</a></li>
			<li class="nav-item"><a class="nav-link" href="#">Register</a></li>
			<li class="nav-item"><a class="nav-link" href="#">UserName</a></li>
			<!-- Dropdown -->
			<li class="nav-item dropdown"><a
				class="nav-link dropdown-toggle" href="#" id="navbardrop"
				data-toggle="dropdown"> Category </a>
				<div class="dropdown-menu">
					<c:forEach var="cate" items="${listCategory}">
						<a class="dropdown-item"
							href="product?categoryId=${cate.categoryId}&page=1">${cate.categoryName}</a>
					</c:forEach>
				</div></li>
			<li class="nav-item"><a class="nav-link" href="checkout">Cart</a></li>
		</ul>
	</nav>
	<br>
</body>
</html>