<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Ví dụ sử dụng Directive</title>
        <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.2/angular.min.js"></script>
        <script language="javascript">
                angular.module('myapp', [])
                .controller('MyController', function($scope) {
                    $scope.name = 'Hello';
                });
        </script>
    </head>
    <body ng-app="myapp">
        <div ng-controller="MyController">
            <h2>Enter</h2>
            <input ng-model="name"> <br/>
            <h2>Enter</h2>
            <span ng-bind="name"></span>
      </div>
    </body>
</html>