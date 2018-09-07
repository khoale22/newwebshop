<!DOCTYPE html>
<html>
    <head>
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.2.15/angular.min.js"></script>
        <style>
            .show-scope-demo.ng-scope,
            .show-scope-demo .ng-scope  {
                border: 1px solid red;
                margin: 30px;
            }
            .show-scope-demo .ng-scope .ng-scope{
                border:solid 1px blue;
            }
        </style>
    </head>
    <body ng-app="myapp">
        <div class="show-scope-demo">
            <div ng-controller="TopController">
                <div ng-controller="GreetController">
                    Hello {{name}}!
                </div>
                <div ng-controller="ListController">
                    Hello {{name}}!
                </div>
                {{name}}
            </div>
        </div>
       <!--  Ca 3 deu ra demo -->
      <!--   <script>
            angular.module("myapp", [])
                    .controller('TopController', function($scope) {
                $scope.name = 'demo';
            })
                    .controller('GreetController', function($scope) {

            })
                    .controller('ListController', function($scope) {

            });
        </script> -->
        
        <script >
        angular.module("myapp", [])
        .controller('TopController', function ($scope){
            $scope.name = 'demo';
        })
        .controller('GreetController', function ($scope){
            $scope.name = 'freetuts';
        })
        .controller('ListController', function ($scope){
            $scope.name = 'Coder';
        });
        
        </script>
    </body>
</html>