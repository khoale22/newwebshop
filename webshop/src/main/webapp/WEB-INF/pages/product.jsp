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
</head>


<body>
    
    
	<jsp:include page="header.jsp"></jsp:include>

	<h2>Slide in Overlay from the Top</h2>
	<p>Hover over the image to see the effect.</p>
	
	<div class="row">
	<c:forEach var="product" items="${listProduct}">
		<div class="col-sm-3">
			<div class="container">
				<img src="${product.productImage}" alt="Avatar" class="image">
				<div class="overlay">
					<div class="text">
						<span> Name: <strong>${product.productName} </strong></span> <span> Price : <strong>${product.productPrice}$
						</strong></span> <span> Quantity: <strong>${product.quantity} </strong></span>
 						<button type="submit" style="background-color: red;">AddToCart</button> -->
						<a href="cartadd?command=plus&productId=${product.productId}" class="btn btn-primary">AddToCart </a>  
					</div>
				</div>
			</div>
		</div>
		</c:forEach>
	</div>
	 
	<div class="container" >
		<ul class="pagination">
		    <% for(int i = 0; i < 3; i++) { %>
		     	<li><a href="product?categoryId=${categoryId}&page=<%=(i+1)%>"><%=(i+1)%></a></li> 
 			<% }%>  
		</ul>
	</div>

</body>
</html>