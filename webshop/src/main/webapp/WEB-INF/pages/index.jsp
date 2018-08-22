<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
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
<style>
IMG.displayed {
	display: block;
	margin-left: auto;
	margin-right: auto
}
</style>

</head>
<body>

	<jsp:include page="header.jsp"></jsp:include>
	<!-- 	<div class="container"> -->
	<!-- 		<div id="demo" class="carousel slide" data-ride="carousel"> -->
	<!-- 			<!-- Indicators -->
	-->
	<!-- 			<ul class="carousel-indicators"> -->
	<!-- 				<li data-target="#demo" data-slide-to="0" class="active"></li> -->
	<!-- 				<li data-target="#demo" data-slide-to="1"></li> -->
	<!-- 				<li data-target="#demo" data-slide-to="2"></li> -->
	<!-- 			</ul> -->

	<!-- 			<!-- The slideshow -->
	-->
	<!-- 			<div class="carousel-inner"> -->
	<!-- 				<div class="carousel-item active"> -->
	<!-- 					<img src="images/index1.jpeg" alt="Los Angeles" width="50%" -->
	<!-- 						height="100%" style="align-content: center;"> -->
	<!-- 				</div> -->
	<!-- 				<div class="carousel-item"> -->
	<!-- 					<img src="images/index2.jpeg" alt="Chicago" width="50%" -->
	<!-- 						height="100%" style="align-content: center;"> -->
	<!-- 				</div> -->
	<!-- 			</div> -->

	<!-- 			<!-- Left and right controls -->
	-->
	<!-- 			<a class="carousel-control-prev" href="#demo" data-slide="prev"> -->
	<!-- 				<span class="carousel-control-prev-icon"></span> -->
	<!-- 			</a> <a class="carousel-control-next" href="#demo" data-slide="next"> -->
	<!-- 				<span class="carousel-control-next-icon"></span> -->
	<!-- 			</a> -->
	<!-- 		</div> -->
	<!-- 	</div> -->
	<!-- 	<div class="container"> -->
	<!-- 		<div class="row"> -->
	<!-- 			<div class="col-lg-4"> -->
	<!-- 				<img class="rounded-circle" -->
	<!-- 					src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" -->
	<!-- 					alt="Generic placeholder image" width="140" height="140"> -->
	<!-- 				<h2>Heading</h2> -->
	<!-- 				<p>Donec sed odio dui. Etiam porta sem malesuada magna mollis -->
	<!-- 					euismod. Nullam id dolor id nibh ultricies vehicula ut id elit. -->
	<!-- 					Morbi leo risus, porta ac consectetur ac, vestibulum at eros. -->
	<!-- 					Praesent commodo cursus magna.</p> -->
	<!-- 				<p> -->
	<!-- 					<a class="btn btn-secondary" href="#" role="button">View -->
	<!-- 						details &raquo;</a> -->
	<!-- 				</p> -->
	<!-- 			</div> -->
	<!-- 			<!-- /.col-lg-4 -->
	-->
	<!-- 			<div class="col-lg-4"> -->
	<!-- 				<img class="rounded-circle" -->
	<!-- 					src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" -->
	<!-- 					alt="Generic placeholder image" width="140" height="140"> -->
	<!-- 				<h2>Heading</h2> -->
	<!-- 				<p>Duis mollis, est non commodo luctus, nisi erat porttitor -->
	<!-- 					ligula, eget lacinia odio sem nec elit. Cras mattis consectetur -->
	<!-- 					purus sit amet fermentum. Fusce dapibus, tellus ac cursus commodo, -->
	<!-- 					tortor mauris condimentum nibh.</p> -->
	<!-- 				<p> -->
	<!-- 					<a class="btn btn-secondary" href="#" role="button">View -->
	<!-- 						details &raquo;</a> -->
	<!-- 				</p> -->
	<!-- 			</div> -->
	<!-- 			<!-- /.col-lg-4 -->
	-->
	<!-- 			<div class="col-lg-4"> -->
	<!-- 				<img class="rounded-circle" -->
	<!-- 					src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" -->
	<!-- 					alt="Generic placeholder image" width="140" height="140"> -->
	<!-- 				<h2>Heading</h2> -->
	<!-- 				<p>Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, -->
	<!-- 					egestas eget quam. Vestibulum id ligula porta felis euismod semper. -->
	<!-- 					Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum -->
	<!-- 					nibh, ut fermentum massa justo sit amet risus.</p> -->
	<!-- 				<p> -->
	<!-- 					<a class="btn btn-secondary" href="#" role="button">View -->
	<!-- 						details &raquo;</a> -->
	<!-- 				</p> -->
	<!-- 			</div> -->
	<!-- 		</div> -->
	<!-- 	</div> -->

	<div class="container">
		<div id="demo" class="carousel slide" data-ride="carousel">
			<!-- Indicators -->
			<ul class="carousel-indicators">
				<li data-target="#demo" data-slide-to="0" class="active"></li>
				<li data-target="#demo" data-slide-to="1"></li>
				<li data-target="#demo" data-slide-to="2"></li>
			</ul>

			<!-- The slideshow -->
			<div class="carousel-inner">
				<div class="carousel-item active">
					<img class="displayed" src="images/index1.jpeg" alt="samsung"
						width="280" height="300">
				</div>
				<div class="carousel-item">
					<img class="displayed" src="images/index1.jpeg" alt="Iphone"
						width="300" height="300">
				</div>
				<div class="carousel-item">
					<img class="displayed" src="images/index1.jpeg" alt="oppo"
						width="280" height="300">
				</div>
			</div>

			<!-- Left and right controls -->
			<a class="carousel-control-prev" href="#demo" data-slide="prev">
				<span class="carousel-control-prev-icon"></span>
			</a> <a class="carousel-control-next" href="#demo" data-slide="next">
				<span class="carousel-control-next-icon"></span>
			</a>
		</div>
	</div>
	<div class="container" style="margin-top: 50px;">
		<div class="row">
			<div class="col-sm-2">
				<div class="left-sidebar">
					<div class="panel-group category-products" id="accordian">
						<!--category-productsr-->
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a data-toggle="collapse" data-parent="#accordian"
										href="#sportswear"> <span class="badge pull-right"><i
											class="fa fa-plus"></i></span> Sportswear
									</a>
								</h4>
							</div>
							<div id="sportswear" class="panel-collapse collapse">
								<div class="panel-body">
									<ul>
									     <c:forEach var="cate" items="${listCategory}">
										<li><a href="product?categoryId=${cate.categoryId}&page=1">${cate.categoryName}</a></li>
										</c:forEach>
									</ul>
								</div>
								
				
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-lg-3">
				<img class="rounded-circle" src="images/index1.jpeg"
					alt="Generic placeholder image" width="140" height="140">
				<h2>SamSung j7 Pro</h2>
				<p>A smartphone is a handheld personal computer. It possesses
					extensive computing capabilities, including high-speed access to
					the Internet using both Wi-Fi and mobile broadband. Most, if not
					all, smartphones are also built with support for Bluetooth and
					satellite navigation.</p>
				<p>
					<a class="btn btn-secondary" href="#" role="button">View
						details &raquo;</a>
				</p>
			</div>
			<!-- /.col-lg-4 -->
			<div class="col-lg-3">
				<img class="rounded-circle" src="images/index1.jpeg"
					alt="Generic placeholder image" width="140" height="140">
				<h2>SamSung j7 Pro</h2>
				<p>A smartphone is a handheld personal computer. It possesses
					extensive computing capabilities, including high-speed access to
					the Internet using both Wi-Fi and mobile broadband. Most, if not
					all, smartphones are also built with support for Bluetooth and
					satellite navigation.</p>
				<p>
					<a class="btn btn-secondary" href="#" role="button">View
						details &raquo;</a>
				</p>
			</div>
			<!-- /.col-lg-4 -->
			<div class="col-lg-3">
				<img class="rounded-circle" src="images/index1.jpeg"
					alt="Generic placeholder image" width="140" height="140">
				<h2>SamSung j7 Pro</h2>
				<p>A smartphone is a handheld personal computer. It possesses
					extensive computing capabilities, including high-speed access to
					the Internet using both Wi-Fi and mobile broadband. Most, if not
					all, smartphones are also built with support for Bluetooth and
					satellite navigation.</p>
				<p>
					<a class="btn btn-secondary" href="#" role="button">View
						details &raquo;</a>
				</p>
			</div>
		</div>
	</div>

	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>