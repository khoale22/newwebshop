<!doctype html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Ví dụ sử dụng Directive datetime-local</title>
<script
	src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.2/angular.min.js"></script>
<style>
* {
	margin: 0;
	padding: 0
}

body {
	margin: 50px
}
</style>
<script>
	angular.module('ngShowHide', []).controller('myController',
			function($scope) {
				$scope.checkAge = function() {
					
					if ($scope.age >= 20) {
                         return true ;
					}else{
						return false ;
					}

				};

			});
</script>
</head>
<body ng-app="ngShowHide">
	<div ng-controller="myController">
		<h1>Enter your age</h1>
		<input ng-model="age" type="number">
              {{age}}
		<div>
			<div ng-show="checkAge()"> ban du tuoi</div>
			<div ng-hide="checkAge()"> ban chua du tuoi  </div>
		</div>
	</div>
</body>
</html>