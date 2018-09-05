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
	<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
</head>
<body>
	<%
		Cart cart = (Cart) session.getAttribute("cart");
		if (session.getAttribute("outOfProduct") != null) {
			String outOfProduct = (String) session.getAttribute("outOfProduct");
		}
		if (session.getAttribute("entryCart") != null) {
			int entryCart = (Integer) session.getAttribute("entryCart");
		}
	%>
	<jsp:include page="header.jsp"></jsp:include>
	<div class="container">
		<h2>Cart</h2>
		<c:if test="${outOfProduct != null}">
			<%-- <div class="alert alert-warning alert-dismissible">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>${outOfProduct}</strong>
			<%-- </div> --%> --%>
			<script type="text/javascript">
			swal("Error!", "This product is out of quantity ,please buy other!");
		</script>
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
					<td><img
						src="<%=list.getValue().getProduct().getProductImage()%>"
						width="50px" height="50px"></td>
					<td><%=list.getValue().getProduct().getProductName()%></td>
					<td><%=list.getValue().getProduct().getProductPrice()%></td>
					<th><%=list.getValue().getQuanlity()%></th>
					<td><%=list.getValue().getQuanlity() * list.getValue().getProduct().getProductPrice()%></td>
					<td><a
						href="cartRemove?command=remove&productId=<%=list.getValue().getProduct().getProductId()%>&quantityOfBuy=<%=list.getValue().getQuanlity()%>">
							<img src="images/delete.jpeg" width="20px" height="20px">
					</a></td>
				</tr>
			</tbody>
			<%
				}
			%>
			<tr>
			</tr>
		</table>

		<form action="checkout" method="post">
			<div style="background-color: #f2f2f2; margin-top: -10px;">
				<span style="font-size: 50px;"> Sum is : <strong> <%=cart.totalCart()%>$
				</strong></span>
				<div style="float: right;">
					<label
						style="background-color: #FE980F; color: white; padding: 3px;">Payment</label>
					<select name="payment" style="margin-top: 20px;">
						<option value="Bank Transfer">Bank Transfer</option>
						<option value="Live">Live</option>
					</select>
				</div>
			</div>
			<!-- <button type="submit" class="btn btn-dark btn-lg">Checkout</button> -->
			<c:if test="${entryCart != 0}">
				<button type="button" class="btn btn-dark btn-lg"
					data-toggle="modal" data-target="#myModal">Checkout</button>
			</c:if>
			<!-- Modal checkout -->
			<div class="modal fade" id="myModal">
				<div class="modal-dialog">
					<div class="modal-content">

						<!-- Modal Header -->
						<div class="modal-header">
							<h4 class="modal-title"
								style="text-align: center; color: #228B22;">Please
								enter infomation and confirm </h4>
							<button type="button" class="close" data-dismiss="modal">×</button>
						</div>

						<!-- Modal body -->
						<div class="modal-body">

							<div class="form-group">
								<label for="email">Phone:</label> <input type="number"
									class="form-control" placeholder="Example : 0905211876"
									name="phone" required="required">
							</div>
							<div class="form-group">
								<label for="pwd">Address:</label> <input type="text"
									class="form-control" id="pwd"
									placeholder="Example : 15 ham nghi" name="address_payment"
									required="required">
							</div>
						</div>

						<!-- Modal footer -->
						<div class="modal-footer">
							<button type="submit" class="btn btn-dark ">Confirm</button>
							<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
						</div>

					</div>
				</div>
			</div>
		</form>
	</div>
	<%
		if (session.getAttribute("outOfProduct") != null) {
			session.removeAttribute("outOfProduct");
		}
		if (session.getAttribute("entryCart") != null) {
			session.removeAttribute("entryCart");
		}
	%>
</body>
</html>