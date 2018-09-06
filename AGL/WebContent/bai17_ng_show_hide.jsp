<!doctype html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Ví dụ sử dụng Directive datetime-local</title>
        <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.2/angular.min.js"></script>
        <style>
            *{margin:0;padding:0}
            body{margin:50px}
        </style>
        <script>
            angular.module('ngShowHide', [])
                    .controller('myController', ['$scope', function($scope) {
                    $scope.your_age = 20;
                    $scope.checkAge = function()
                    {
                        if ($scope.your_age >= 20){
                            return true;
                        }
                        return false;
                    };
                }]);
        </script>
    </head>
    <body ng-app="ngShowHide">
        <div ng-controller="myController">
            <h3>Chào mừng bạn đến với website freetuts.net</h3>
            <h2>Hỏi Đáp: Bạn năm nay bao nhiêu tuổi?</h2>
            <input type="text" ng-model="your_age" ><br/>
            <div>
                <div ng-hide="checkAge()"> 
                 chua   du tuoi
                </div>
                <div ng-show="checkAge()">
                  du tuoi
                </div>
            </div>
        </div>
    </body>
</html>