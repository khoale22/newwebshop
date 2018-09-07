<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Ví dụ sử dụng Directive Form</title>
        <style>
            .my-form {
                -webkit-transition:all linear 0.5s;
                transition:all linear 0.5s;
                background: transparent;
            }
              .my-form.ng-invalid {
                color: red;
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.2/angular.min.js"></script>
        <script>
            angular.module('myapp', [])
                .controller('FormController', ['$scope', function($scope) {
                }]);
        </script>
    </head>
    <body ng-app="myapp">
        <form name="myForm" ng-controller="FormController" class="my-form">
            Thông báo: <input name="input2" ng-model="userType" required><br/>
            <tt>myForm.input.$valid = {{myForm.input2.$valid}}</tt><br>
            <tt>myForm.input.$error = {{myForm.input2.$error}}</tt><br>
            <tt>myForm.$valid = {{myForm.$valid}}</tt>
        </form>
    </body>
</html>