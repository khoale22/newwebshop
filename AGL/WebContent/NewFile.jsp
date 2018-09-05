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
//// bai 1
<div ng-app="">
<p>Input something in the input box:</p>
<p>Name: <input type="text" ng-model="name"></p>
<p ng-bind="name"></p>
<!-- cach 2 -->
<p>{{name}} </p>
<p> Test caculate : {{5 + 5}} </p>
</div>


//// bai 2
<!-- <div ng-app="" ng-init="firstName = 'khoa23'">  // only '' not ""
 <p>The name is <span ng-bind="firstName"> </span> </p>
 
 <p> Test caculate : {{5 + 5}} </p>
</div> -->

<!-- /// bai 2 cach khac -->
<!-- 
<div data-ng-app="" data-ng-init="firstName='John'">

<p>The name is <span data-ng-bind="firstName"></span></p>

</div>
 -->

</body>
</html>