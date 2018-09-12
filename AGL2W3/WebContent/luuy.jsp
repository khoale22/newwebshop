<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
   <h1> TAT CA NHUNG GI KHAI BAO BANG ng-?? DEU NAM TRONG $scope</h1>
<!--    ng-init XEM BAI nested-repeaet.html  khong can thong qua scope tao bien trong <body> <body>
   
   
<form  name="myForm">

 //PHAI CO NAME = "MYNAME O DAY"
<input name="myName" ng-model="myName" required>  

<span ng-show="myForm.myName.$touched && myForm.myName.$invalid">The name is required.</span>
 
    -->
    
 <!-- /////BAI2
 
 <div ng-app="myApp" ng-controller="myCtr1">

  First Name : <input type="text" name="firstName" ng-model="firstName"> <br> // name="firstName"  khong co nhiue y nghia trong agl
  Last Name : <input type="text" ng-model="lastName"> <br>  
  ten the p test <p > </p> 
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
     -->
     
     
     
     <form  name="myForm">

 //PHAI CO NAME = "MYNAME O DAY"

 

<input name="myName1" ng-model="myName1" required> // PHAI SU DUNG CA HAI  name="myName1" ng-model="myName1"


<span ng-show="myForm.myName1.$touched && myForm.myName1.$invalid">The name is required.</span>


/// bai 4

 $scope.products.splice(3, 2); /// 3 O DAY LA BAT DAU BANG INDEX NAO , 2 LA XOA BAO NHIEU PHAN TU BAT DAU BANG INDEX DO

</form>
     
</body>
</html>




<!-- $scope.items = [ "khoa", "trung", "nga" ];
		
		   BAI 3
		   $scope.data ={
				  name : 'turngphan',
				  age : 3
				  
		   }; -->
<!-- 		   $scope.cars = [
    {model : "Ford Mustang", color : "red"},
    {model : "Fiat 500", color : "white"},
    {model : "Volvo XC90", color : "black"}
]; -->



  <!-- VI BAT DAU BANG '$locationProvider' NEN PHAI CO NGOAC VUONG  -->
<!--   LUU Y NEW $LOCATIONPROVIDER KHONG CO '' THI DUNG  () -->
  
<!--   config([ '$locationProvider' , '$routeProvider' ,
       function config($locationProvider , $routeProvider) {
           $locationProvider.hashPrefix('!');

           $routeProvider.
               when('/phones' ,{
                   template : ' <phone-list></phone-list>'
               }).
               when('/phones/:phoneId22' ,{
                    template: '{{$ctrl.phoneId}} <phone-detail></phone-detail>'
               }).
               /// tuc la khi chay ung dung len se khong co duong dan url nao cu the nen se duoc chuyen ve   when('/phones' ,{
             //template : ' <phone-list></phone-list>'
             //}).
               otherwise('/phones');
       }

]); -->




bai 10  khi nao dung , va ;
 templateUrl : 'phone-list/phone-list.template.html',
    var seft = this;
      seft.phones = response.data;