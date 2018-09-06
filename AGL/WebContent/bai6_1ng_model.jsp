<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/angularjs/1.2.15/angular.min.js"></script>

<script>
	angular.module('MyForm', []).controller('ExampleController',
			function($scope) {
			});
</script>
<style>
.my-input {
	-webkit-transition: all linear 0.5s;
	transition: all linear 0.5s;
	background: transparent;
}

.my-input.ng-invalid {
	color: white;
	background: red;
}
</style>

</head>
<body ng-app="MyForm">

	<form name="testForm" ng-controller="ExampleController">

		<input ng-model="val" ng-pattern="/^\d+$/" name="anim"
			class="my-input">

	</form>





</body>
</html>