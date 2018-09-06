<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Ví dụ sử dụng Directive</title>
        <style>*{margin:0}body{padding:20px}</style>
        <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.2/angular.min.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.2/angular-sanitize.js"></script>
        <script language="javascript">
            angular.module('myapp', ['ngSanitize'])
                    .controller('ExampleController', ['$scope', function($scope) {
                }]);
        </script>
    </head>
    <body ng-app="myapp">
        <div ng-controller="ExampleController">
            <input type="text" ng-model="myHTML2"/>
            <p ng-bind-html="myHTML2"></p>
        </div>
    </body>
</html>