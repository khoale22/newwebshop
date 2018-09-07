<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular.min.js"></script>
</head>
<body>

<div ng-app="myApp" ng-controller="myCtr1">

  First Name : <input type="text" name="firstName" ng-model="firstName"> <br> // name="firstName"  khong co nhiue y nghia trong agl
  Last Name : <input type="text" ng-model="lastName"> <br>   
  Fullname : {{firstName + " " +  lastName + " " +tentuyy}}
     
     
</div>

<script>
var app = angular.module('myApp' , [])

app.controller("myCtr1" , function($scope) {
	$scope.firstName = 'khoa23';
	$scope.lastName = "le";
	$scope.tentuyy = "abcd";
})
</script>	

<!-- <div ng-app="myApp" ng-controller="myCtrl">
{{ firstName + " " + lastName }}
</div>

<script>
var app = angular.module("myApp", []);
app.controller("myCtrl", function($scope) {
    $scope.firstName = "John";
    $scope.lastName = "Doe";
});
</script> -->


</body>
</html>