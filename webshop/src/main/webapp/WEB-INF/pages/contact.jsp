<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<title>Shop</title>
<link href="styles/font-awesome.min.css" rel="stylesheet">
<link href="styles/prettyPhoto.css" rel="stylesheet">
<link href="styles/price-range.css" rel="stylesheet">
<link href="styles/animate.css" rel="stylesheet">
<link href="styles/main.css" rel="stylesheet">
<link href="styles/responsive.css" rel="stylesheet">
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
</head>
<body>
	<%
		if (session.getAttribute("checkOutOk") != null) {
			String checkOutOk = (String) session.getAttribute("checkOutOk");
		}
	%>

	<jsp:include page="header.jsp"></jsp:include>
	<div class="row">
		<div class="col-sm-12">
			<div class="contact-info">
				<h2 class="title text-center">Khoa-Tay Shop</h2>
				<div>
					<address
						style="text-align: center; font-size: 20px; font-style: oblique; margin-bottom: 10px;">
						<p>Contact info if you have any problem with product</p>
						<p>935 W. Webster Ave New Streets Chicago, IL 60614, NY</p>
						<p>Newyork USA</p>
						<p>Mobile: 0905211865</p>
						<p>Fax: 1-714-252-0026</p>
						<p>Email: hbkkhoa1@gmail.com</p>
					</address>
				</div>
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
		</div>
	</div>
	<c:if test="${checkOutOk != null}">
		<script type="text/javascript">
			swal("Successful!",
					"We will contact you soon , see more infomation about us",
					"success");
		</script>
	</c:if>
	<%
		if (session.getAttribute("checkOutOk") != null) {
			session.removeAttribute("checkOutOk");
		}
	%>


</body>
</html>