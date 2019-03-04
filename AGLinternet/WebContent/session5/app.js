﻿
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

            }],
                template : ' <h2>Iam outerComp</h2>'+
            '<p> userName : {{$ctrl.userName }}</p>'+
                    '<p> hero : {{$ctrl.hero }}</p>'


        })
        .component('innerComp', {
            bindings :{},
            controller : [function () {}],
            template : ' <h2>Iam innercomp</h2>'
        })


    ;

    
