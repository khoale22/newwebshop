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
	<div class="container">
		<div id="demo" class="carousel slide" data-ride="carousel" style="border-top: 2px , solid , green; ">
			<!-- Indicators -->
			<ul class="carousel-indicators">
				<li data-target="#demo" data-slide-to="0" class="active"></li>
				<li data-target="#demo" data-slide-to="1"></li>
				<li data-target="#demo" data-slide-to="2"></li>
			</ul>

			<!-- The slideshow -->
			<div class="carousel-inner">
				<div class="carousel-item active">
					<img class="displayed" src="https://www.chotot.com/kinhnghiem/wp-content/uploads/2015/05/5-tieu-chi-quan-trong-de-chon-dien-thoai-chup-anh-dep-3.jpg" alt="samsung"
						width="800" height="300">
				</div>
				<div class="carousel-item">
					<img class="displayed" src="https://thegioidienmay.vn/wp-content/uploads/2018/05/sam-sung-03.png" alt="Iphone"
						width="800" height="300">
				</div>
				<div class="carousel-item">
					<img class="displayed" src="https://xdamobile.vn/data/afficheimg/mua-htc-m9-gia-re-qua-ngon-1521509043.jpg" alt="oppo"
						width="800" height="300">
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
			<div class="col-sm-3 col-md-3 col-lg-3">
                <div> 
                   <h1 style="color: #007bff;"> Category</h1>
                   <ul>
										<c:forEach var="cate" items="${listCategory}">
											<li><a 
												href="product?categoryId=${cate.categoryId}&page=1">${cate.categoryName}</a></li>
										</c:forEach>
									</ul>
                </div>
				<div> <img alt="" src="images/quangcao.jpg" width="200" height="400"> </div>
			</div>
			<div class="col-sm-3 col-md-3 col-lg-3">
				<img class="rounded-circle" src="https://dangcapdigital.com/images/p/-0.jpg"
					alt="Generic placeholder image" width="140" height="140">
				<h2>SamSung J7</h2>
				<p>A smartphone is a handheld personal computer. It possesses
					extensive computing capabilities</p>
				<p>
					<a class="btn btn-secondary" href="#" role="button">Product
						sale</a>
				</p>
			</div>
			<!-- /.col-lg-4 -->
			<div class="col-sm-3 col-md-3 col-lg-3">
				<img class="rounded-circle" src="https://dangcapdigital.com/images/p/-0.jpg"
					alt="Generic placeholder image" width="140" height="140">
				<h2>SamSung J7</h2>
				<p>A smartphone is a handheld personal computer. It possesses
					extensive computing capabilities</p>
				<p>
					<a class="btn btn-secondary" href="#" role="button">Product
						sale</a>
				</p>
			</div>
			<!-- /.col-lg-4 -->
			<div class="col-sm-3 col-md-3 col-lg-3">
				<img class="rounded-circle" src="https://dangcapdigital.com/images/p/-0.jpg"
					alt="Generic placeholder image" width="140" height="140">
				<h2>SamSung J7</h2>
				<p>A smartphone is a handheld personal computer. It possesses
					extensive computing capabilities</p>
				<p>
					<a class="btn btn-secondary" href="#" role="button"> Product
						sale</a>
				</p>
			</div>
		</div>
<!--  contanner -->

		<div class="row" style="margin-top: -230px;"> 
			<div class="col-sm-3 col-md-3 col-lg-3">				
            <!-- CHUA LAM -->			
			</div>
			<div class="col-sm-3 col-md-3 col-lg-3">
				<img class="rounded-circle" src="https://dangcapdigital.com/images/p/-0.jpg"
					alt="Generic placeholder image" width="140" height="140">
				<h2>SamSung J7</h2>
				<p>A smartphone is a handheld personal computer. It possesses
					extensive computing capabilities</p>
				<p>
					<a class="btn btn-secondary" href="#" role="button">Product
						sale</a>
				</p>
			</div>
			<!-- /.col-lg-4 -->
			<div class="col-sm-3 col-md-3 col-lg-3">
				<img class="rounded-circle" src="https://dangcapdigital.com/images/p/-0.jpg"
					alt="Generic placeholder image" width="140" height="140">
				<h2>SamSung J7</h2>
				<p>A smartphone is a handheld personal computer. It possesses
					extensive computing capabilities</p>
				<p>
					<a class="btn btn-secondary" href="#" role="button">Product
						sale</a>
				</p>
			</div>
			<!-- /.col-lg-4 -->
			<div class="col-sm-3 col-md-3 col-lg-3">
				<img class="rounded-circle" src="https://dangcapdigital.com/images/p/-0.jpg"
					alt="Generic placeholder image" width="140" height="140">
				<h2>SamSung J7</h2>
				<p>A smartphone is a handheld personal computer. It possesses
					extensive computing capabilities</p>
				<p>
					<a class="btn btn-secondary" href="#" role="button"> Product
						sale</a>
				</p>
			</div> 
		</div>
</div> 
	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>