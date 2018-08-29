<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link href="styles/product.css" rel="stylesheet" type="text/css"
	media="all">

<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
<meta name="description" content="">
<meta name="author" content="">
<title>Shop | E-Shopper</title>
<link href="styles/bootstrap.min.css" rel="stylesheet">
<link href="styles/font-awesome.min.css" rel="stylesheet">
<link href="styles/prettyPhoto.css" rel="stylesheet">
<link href="styles/price-range.css" rel="stylesheet">
<link href="styles/animate.css" rel="stylesheet">
<link href="styles/main.css" rel="stylesheet">
<link href="styles/responsive.css" rel="stylesheet">
<body>
	<jsp:include page="header.jsp"></jsp:include>


	<div class="container">
		<div class="row">
			<div class="col-sm-12">
				<div class="features_items">
					<!--features_items-->
					<h2 class="title text-center">Features Items</h2>
					<c:forEach var="product" items="${listProduct}">
						<div class="col-sm-2"> 
							<div class="product-image-wrapper">
								<div class="single-products">
									<div class="productinfo text-center">
										<img src="${product.productImage}" alt="" />		
										<h2>${product.productName}</h2>
										<h2>${product.productPrice}$</h2>
										<p>Quantity :${product.quantity}</p>
										<a href="cartAdd?command=plus&productId=${product.productId}"
											class="btn btn-default add-to-cart"><i
											class="fa fa-shopping-cart"></i>Add to cart</a>
									</div>
									<div class="product-overlay">
										<div class="overlay-content">
											<h2>${product.productPrice}$</h2>
											<p>Quantity :${product.quantity}</p>
											<a href="cartAdd?command=plus&productId=${product.productId}"
												class="btn btn-default add-to-cart"><i
												class="fa fa-shopping-cart"></i>Add to cart</a>
										</div>
									</div>
								</div>
								<div class="choose" style="text-align: center; color: black;">
									<a href="productDetail?productId=${product.productId}">Detail product</a>
								</div>
							</div>
						</div>
					</c:forEach>

				</div>
			</div>
			<!-- DANG LAM -->
			 <!-- <div class="col-sm-4">
				<div class="contact-info">
					<h2 class="title text-center">Khoa-Tay Shop</h2>
					<address>
						<p>Contact info if you have any problem with product</p>
						<p>935 W. Webster Ave New Streets Chicago, IL 60614, NY</p>
						<p>Newyork USA</p>
						<p>Mobile: 0905211865</p>
						<p>Fax: 1-714-252-0026</p>
						<p>Email: hbkkhoa1@gmail.com</p>
					</address>
					<div class="social-networks">
						<h2 class="title text-center">Social Networking</h2>
						<ul>
							<li><a href="#"><i class="fa fa-facebook"></i></a></li>
							<li><a href="#"><i class="fa fa-twitter"></i></a></li>
							<li><a href="#"><i class="fa fa-google-plus"></i></a></li>
							<li><a href="#"><i class="fa fa-youtube"></i></a></li>
						</ul>
					</div>
				</div>
			</div> -->
		</div>
	</div>
	<!--features_items-->

	<div class="container">
		<ul class="pagination">
			<%
				float countProduct = (Float) session.getAttribute("countProduct");
				for (int i = 0; i < countProduct; i++) {
			%>
			<li><a href="product?categoryId=${categoryId}&page=<%=(i+1)%>"><%=(i + 1)%></a></li>
			<%
				}
			%>
		</ul>
	</div>
	<%
		if (session.getAttribute("countProduct") != null) {
			session.removeAttribute("countProduct");
		}
	%>


	<script src="js/jquery.js"></script>
	<script src="js/price-range.js"></script>
	<script src="js/jquery.scrollUp.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery.prettyPhoto.js"></script>
	<script src="js/main.js"></script>

</body>
</html>