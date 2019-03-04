
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
                $ctrl.addName = function (name) {
                    $ctrl.userName = name;
                }
            }],
                template : ' <h2>Iam outerComp</h2>'+
            '<p> userName : {{$ctrl.userName }}</p>'+
            '<inner-comp add-name="$ctrl.addName(name)"></inner-comp>'
        })
        .component('innerComp', {
            bindings :{
                addName : '&'
            },
            controller : [function () {
                var $ctrl = this ;
                $ctrl.addAName = function () {
                   // $ctrl.newNameTest = "khoa";
                    $ctrl.addName({name: $ctrl.newName});
                    $ctrl.newName = "";
                }
            }],
            template : ' <h2>Iam innercomp444</h2>'+
                '<p>New Name : <input ng-model="$ctrl.newName"/></p>'+
                '<p><button ng-click="$ctrl.addAName()">ADDNAME</button></p>'

        })


    ;


