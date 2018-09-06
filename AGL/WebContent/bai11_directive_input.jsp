<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.2/angular.min.js"></script>

<style>
.ng-invalid{
color: red; 
}


</style>

<script>
 angular.module('MyApp' ,[]).controller('MyController' , function($scope) {
	$scope.login = function() {
	
		if(	$scope.username == "admin" && 	$scope.password == 123){
			alert("dang nhap thanh cong");
		}else{
			alert("dang nhap that bai");
		}
		
	};
	 
 $scope.game = 'KHONG22';
	 
});



</script>

</head>
<body ng-app="MyApp">
  <form ng-submit="login()" ng-controller="MyController"  name="myForm" class="myForm">
     
   User name: <br/>
            <input type="text"  ng-model="username" ng-trim="true" required ng-minlength="3"/> <br/>
            Password: <br/>
            <input type="text" ng-model="password" ng-trim="true" required ng-minlength="3" ng-maxlength="10" > <br/><br/>
            <input type="submit" value='Login'/> <br>
  <!-- /// bai 12 checkbox  LUU Y : ng-true-value="'co'"  -->
  
   Choi game : <input type="checkbox" ng-model="game" ng-true-value="'co'" ng-false-value="'khong'" value="tenhienthi" name="abcd"	> 
   <h2> Playgame = {{game}}</h2> <br>
   
   <!-- /// bai 17 ng_show_ng_hide -->
   
   check se show ra :<input type="checkbox" ng-model="willbeshow">
      <div ng-show="willbeshow">
      
           hien thi o day
      
      </div>
   
   
    <br/><br/>
    check se an di :<input type="checkbox" ng-model="willbehide">
      <div ng-hide="willbehide">
      
           hien thi o day
      
      </div>
 
  </form> 
  
 
  
  
  

</body>
</html>