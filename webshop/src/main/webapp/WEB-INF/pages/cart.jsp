<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.model.Item"%>
<%@page import="java.util.Map"%>
<%@page import="com.model.Cart"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" type="text/css" href="styles/login.css">
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
		if (session.getAttribute("outOfProduct") != null) {
			String outOfProduct = (String) session.getAttribute("outOfProduct");
		}
	%>
	<jsp:include page="header.jsp"></jsp:include>
	<div class="container">
		<h2>Striped Rows</h2>
		<c:if test="${outOfProduct != null}">
			<div class="alert alert-warning alert-dismissible">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>${outOfProduct}</strong>
			</div>
		</c:if>
		<table class="table table-striped">
			<thead style="background-color: #808080;">
				<tr>
					<td>Image</td>
					<th>Product_Name</th>
					<th>Price</th>
					<th>Quantity</th>
					<th>Total</th>
					<th>delete</th>
				</tr>
			</thead>

			<%
				for (Map.Entry<String, Item> list : cart.getCartItems().entrySet()) {
			%>
			<tbody>
				<tr>
					<td><img src="images/images1.png" width="50px" height="50px"></td>
					<td><%=list.getValue().getProduct().getProductName()%></td>
					<td><%=list.getValue().getProduct().getProductPrice()%></td>
					<th><%=list.getValue().getQuanlity()%></th>
					<td><%=list.getValue().getQuanlity() * list.getValue().getProduct().getProductPrice()%></td>
					<%-- 					<td><a href="cart/add?command=remove&productId=<%=list.getValue().getProduct().getProductId()%>">detele </a> </td> --%>
					<td><a href="cart/remove?command=remove&productId=pro1">detele
					</a></td>
				</tr>
			</tbody>
			<%
				}
			%>
			<tr>
			</tr>
		</table>
		<form action="checkout/" method="post">
			<div style="background-color: #f2f2f2; margin-top: 0px;"> 
				<span style="font-size: 50px;"> Sum is : <strong> <%=cart.totalCart()%>$
				</strong></span>
				<div style="float: right;">
					<button type="button" class="btn btn-success" style="background-color: #FE980F">Payment</button>
					<select name="payment" style="margin-top: 20px;">
						<option value="Bank Transfer">Bank Transfer</option>
						<option value="Live">Live</option>
					</select>
				</div>
			</div>
		    
			<button type="submit" class="btn btn-dark btn-lg">Checkout</button>
			
		</form>
	</div>
	<%
		if (session.getAttribute("outOfProduct") != null) {
			session.removeAttribute("outOfProduct");
		}
	%>
	<%-- <jsp:include page="footer.jsp"></jsp:include> --%>

</body>
</html>