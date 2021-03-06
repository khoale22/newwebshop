<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="styles/indexabc.css">
<link
	href="https://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script src="js/jquery-1.11.1.min.js"></script>
</head>
<body>

	<div style="height: 96; font-size: 16px;">
		<jsp:include page="header.jsp"></jsp:include>
	</div>
	<div class="container">
		<div class="card" style="background-color: #ddd;">
			<div class="container-fliud">
				<div class="wrapper row">
					<div class="preview col-md-6">

						<div class="preview-pic tab-content">
							<div class="tab-pane active" id="pic-1">
								<img style="width: 250px; height: 300px;"
									src="${product.productImage}" alt="">
							</div>
						</div>
					</div>
					<div class="details col-md-6">
						<h1 class="product-title">${product.productName}</h1>
						<div class="rating">
							<div class="stars">
								<span class="fa fa-star checked"></span> <span
									class="fa fa-star checked"></span> <span
									class="fa fa-star checked"></span> <span class="fa fa-star"></span>
								<span class="fa fa-star"></span>
							</div>
						</div>
						<h4 class="price">Price: ${product.productPrice}</h4>
						<h4 class="price">Quantity: ${product.quantity}</h4>
						<h4 class="price">Screen: ${product.screen}</h4>
						<h4 class="price">Version: ${product.version}</h4>
						<h4 class="price">Ram: ${product.ram}</h4>
						<h5 class="colors">
							Color: <span class="color orange not-available"
								data-toggle="tooltip" title="Not In store"></span> <span
								class="color green"></span> <span class="color blue"></span>
						</h5>
						<div class="action">
							<a href="cartAdd?command=plus&productId=${product.productId}">
								<button class="add-to-cart btn btn-default" type="submit">ADD
									TO CART</button>
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>