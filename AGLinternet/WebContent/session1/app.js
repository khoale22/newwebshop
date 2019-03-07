
    var app = angular.module('MainApp', [])

        .component('outerComp', {
          bindings :{
              //userName : '=',
             // addName : '='
          },
            controller : [function () {
              var $ctrl = this ; //// SU DUNG var self tuong tu ,va ở template vẫn là  {{$ctrl.userName }}
               $ctrl.userName = "khoaLe1";

            }],
                template : ' <h2>Iam outerComp</h2>'+
            '<p> userName : {{$ctrl.userName }}</p>'+
            '<inner-comp add-name="$ctrl.userName"></inner-comp>'
        })
        .component('innerComp', {
            bindings :{
                addName : '='
            },
            controller : [function () {
                var $ctrl = this ;
                $ctrl.ten = "ngatran";
            }],
            template : ' <h2>Iam innercomp444</h2>'+
               ' <h2>{{$ctrl.addName}}</h2>'+
               // '<p>New Name : <input ng-model="$ctrl.newName"/></p>'+
                '<p><button ng-click="$ctrl.addName = $ctrl.ten">ADDNAME</button></p>'

        })
    ;


