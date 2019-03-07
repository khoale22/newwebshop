
    var app = angular.module('MainApp', [])

        .component('parentComponent', {

            controller : ["$scope",function($scope) {
              var $ctrl = this ;
                $ctrl.ten = "khoa";
                $ctrl.OnClick = function (evt) {
                    $scope.$broadcast("SendDown", $ctrl.ten );
                }
                $scope.$on("SendDown", function (evt, data) {
                    $ctrl.Message = "Inside SendDown handler of MyController1 : " + data;
                });
                $scope.$on("SendDown", function (evt, data) {
                    $ctrl.Message = "Inside SendDown handler of MyController1 : " + data;
                });

                //handle SendUp event
                $scope.$on("SendUp", function (evt, data) {
                    $ctrl.Message = "Inside SendUp handler of MyController1 : " + data;
                });
            }],
                template : ' <h2>Iam parentComponent</h2>'+
            '<p> userName : {{$ctrl.Message }}</p>'+
                    '<p><button ng-click="$ctrl.addName = $ctrl.OnClick($event)">Broardcast</button></p>'+
            '<child-component></child-component>'
        })
        .component('childComponent', {
            controller : ["$scope",function($scope) {
                var $ctrl = this ;

                //handle SendDown event
                $scope.$on("SendDown", function (evt, data) {
                    $ctrl.Message = "Inside SendDown handler of MyController3 : " + data;
                });

                //emit SendUp event up
                $ctrl.OnClick = function (evt, data) {
                    $scope.$emit("SendUp", "some data");
                }

                //handle SendUp event
                $scope.$on("SendUp", function (evt, data) {
                    $ctrl.Message = "Inside SendUp handler of MyController3 : " + data;
                });
            }],
            template : ' <h2>Iam childComponent</h2>'+
               ' <h2>{{$ctrl.addName}}</h2>'+
                '<p> userName : {{$ctrl.Message }}</p>'+
                '<p><button ng-click="$ctrl.addName = $ctrl.OnClick($event)">Emit</button></p>'

        })
    ;


