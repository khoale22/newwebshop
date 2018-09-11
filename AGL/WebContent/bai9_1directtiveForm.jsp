<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Ví dụ sử dụng Directive Form</title>
        <style>
           
            .my-form.ng-invalid {
                background: red;
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
            Username: <input name="input" ng-model="username" required>
            <span >Required!</span><br>
            <tt>userType = {{username}}</tt>
        </form>
    </body>
</html>



















