<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.0-beta.17/angular.min.js"></script>
</head>
<body>
	<div ng-app=myapp>
		<div ng-controller="HeaderController">
		  <h2> Xin chao {{data.title}} den voi {{data.website}}!</h2>
		  <h2>{{list}}</h2>
		</div>
		 <div ng-controller="ContentController">
                <h2>Xin chao 11 {{data.title}} den voi  {{data.website}}!</h2>
            </div>
            
	</div>

	<script>
  
     angular.module("myapp" , []).controller("HeaderController" ,function($scope){
    	 
    	
    	 $scope.list = "abc2";
    	 
    	 $scope.data={
    			 title : "cac bans1" , 
    	         website : 'freetuts.new'
    	 }
 	 
     }).controller("ContentController", function($scope){
         $scope.data = {
                 title : 'quy vi',
                 website : 'hoc lap trinh freetuts.net' 
             };
         });
  
  </script>

</body>
</html>