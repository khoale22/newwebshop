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
	angular.module('myapp', []).controller('ExampleController',
			[ '$scope', function($scope) {
				// Khởi tạo giá trị ban đầu cho list = [];
				$scope.list = [];

				// Khi submit form thì chạy hàm này
				$scope.submit22 = function() {
					// nếu người dùng có nhập nội dung thì lưu nó vào list
					if ($scope.text) {
						$scope.list.push(this.text);

						// đồng thời xóa dữ liệu trong thẻ input
						$scope.text = '';
					}
				};
			} ]);
</script>
</head>
<body ng-app="myapp">
	<form ng-submit="submit22()" ng-controller="ExampleController">
		<h3>Nhap ten sinh vien</h3>
		<input type="text" ng-model="text"/> <input type="submit" value="them" />

		<pre>list={{list}}</pre>


	</form>

</body>
</html>