<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="styles/cart.css">
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
		if (session.getAttribute("error") != null) {
			String error = (String) session.getAttribute("error");
		}
	%>
	<jsp:include page="header.jsp"></jsp:include>
	<h1 align="center">LOGIN</h1>
	<div style="width: 25%; margin: 0 auto;">
		<div class="account-wall">
			<div align="center">
				<img class="img-thumbnail" src="images/images2.jpeg">
			</div>
			<form action="login/authenticate" method="post">
				<input type="text" class="form-control" placeholder="UserId"
					name="userId" required autofocus> <input type="password"
					name="pass" class="form-control" placeholder="Password" required>
				<button class="btn btn-success btn-block" type="submit">Login</button>
				<!-- 				<button type="submit" class="btn btn-success btn-block" data-toggle="modal" -->
				<!-- data-target="#myModal">Login</button> -->
				<div class="form-check">
					<label class="form-check-label" for="check1"> <input
						type="checkbox" class="form-check-input" id="check1"
						name="option1" value="something" checked>Remember me
					</label>
				</div>
			</form>
		</div>
		<c:if test="${error != null}">
			<script type="text/javascript">
				swal("Error!", "Your pass or userid are not valid!", "success");
			</script>
		</c:if>

	</div>
	<%
		if (session.getAttribute("error") != null) {
			session.removeAttribute("error");
		}
	%>

</body>
</html>