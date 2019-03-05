
    var app = angular.module('MainApp', [])
        .controller('MainCtrl', function MainCtrl() {
            this.hero = {
                name: 'Spawn'
            };
        })
        .component('outerComp', {
          bindings :{
              hero : '=',
              userName : '@'
          },
            controller : [function () {
              var $ctrl = this ; //// SU DUNG var self tuong tu ,va ở template vẫn là  {{$ctrl.userName }}
                $ctrl.$onInit = function () { // khong can $onInit $ctrl.userName = 'Nick1'; van ok
                    $ctrl.userName = 'Nick1';
                }
               /* $ctrl.addAName = function (name) {
                    $ctrl.hero1 = name;
                }*/
               // $ctrl.hero1 = $ctrl.hero.name;///khong hieu

            }],
                template : ' <h2>Iam outerComp</h2>'+
            '<p> userName : {{$ctrl.userName }}</p>'+
                    '<p> hero0 : {{$ctrl.hero }}</p>'+
                    '<p> hero1 : {{$ctrl.hero1 }}</p>'
                    /*'<p><button ng-click="$ctrl.addAName($ctrl.hero)">ADDNAME1</button></p>'*/
                  //  '<p> hero1 : {{$ctrl.hero1 }}</p>' ///khong hieu


        })
        .component('innerComp', {
            bindings :{},
            controller : [function () {}],
            template : ' <h2>Iam innercomp</h2>'
        })


    ;

    
