<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="styles/register.css">
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
</head>
<body>
	<%
		if (session.getAttribute("registerFail") != null) {
			String error = (String) session.getAttribute("registerFail");
		}
	%>

	<jsp:include page="header.jsp"></jsp:include>

	<form action="addUser" method="post">

		<div class="container" style="margin: 50px 300px;">
			<h1>Register</h1>
			<p>Please fill in this form to create an account.</p>
			<hr>
			<label for="email"><b>Name to Login</b></label> <input type="text"
				placeholder="Enter Your Name to login" name="user_id" required>
			<label for="email"><b>Full Name</b></label> <input type="text"
				placeholder="Enter Your Full Name " name="user_name" required>
			<label for="email"><b>Email</b></label> <input type="text"
				placeholder="Enter Your email" name="user_email" required> <label
				for="psw"><b>Password</b></label> <input type="password"
				placeholder="Enter Password" name="user_pass" required>
			<p>
				By creating an account you agree to our <a href="#">Terms &
					Privacy</a>.
			</p>
			<button type="submit" class="registerbtn">Register</button>
		</div>
	</form>
	<c:if test="${registerFail != null}">
		<script type="text/javascript">
			/* swal("Error!", "The name to login exists", "Ok"); */
			swal("Error!", "The name to login exists!", "success");
		</script>
	</c:if>
	<c:if test="${registerOk != null}">
		<script type="text/javascript">
			/* swal("Successful!", "click the button to comeback!", "Ok"); */
			swal("Successful!", "click the button to comeback!", "success");
		</script>
	</c:if>
	<%
		if (session.getAttribute("registerFail") != null) {
			session.removeAttribute("registerFail");
		}
	if (session.getAttribute("registerOk") != null) {
		session.removeAttribute("registerOk");
	}
	%>


</body>
</html>