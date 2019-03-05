
    var app = angular.module('MainApp', [])

        .component('outerComp', {
          bindings :{

              userName : '@',
          },
            controller : [function () {
              var $ctrl = this ; //// SU DUNG var self tuong tu ,va ở template vẫn là  {{$ctrl.userName }}
                $ctrl.$onInit = function () { // khong can $onInit $ctrl.userName = 'Nick1'; van ok
                    $ctrl.userName = 'Nick1';
                }
               /* $ctrl.addAName(); GOI OK VI TU ! APP MainApp ra 2 companent con*/
                $ctrl.userName =  $ctrl.addAName();

               /* $ctrl.addAName = function (name) {
                    $ctrl.userName = name;
                }*/
            }],
                template : ' <h2>Iam outerComp</h2>'+
            '<p> userName : {{$ctrl.dfsfdsf }}</p>'
        })
        .component('innerComp', {
            bindings :{
               // addName : '&'
            },
            controller : [function () {
                var $ctrl = this ;
                $ctrl.dfsfdsf = "fdsf";
                $ctrl.addAName = function () {
                   return "khoa4324324230";
                }
            }],
            template : ' <h2>Iam innercomp444</h2>'+
                '<p>New Name : <input ng-model="$ctrl.newName"/></p>'+
                '<p><button ng-click="$ctrl.addAName()">ADDNAME</button></p>'

        })


    ;


