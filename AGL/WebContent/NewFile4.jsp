<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script
	src="http://ajax.googleapis.com/ajax/libs/angularjs/1.2.15/angular.min.js"></script>
</head>
<body>
	<div ng-app="myapp">
		<div ng-controller="MyController">
			Nhập tên của bạn: <input type="text" value="" ng-model="username"> 
			<button ng-click="sayHello()">In thong bao</button>
			<hr>
			{{greeting}}
		</div>
	</div>
	<script>
		angular.module("myapp", []).controller("MyController", function($scope) {
					$scope.sayHello = function() {
						$scope.greeting = "Xin chao2 " + $scope.username + '!';
					};
				});
	</script>
</body>



