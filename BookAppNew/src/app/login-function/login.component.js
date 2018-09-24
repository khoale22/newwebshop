'use strict';

angular.module('loginForm' , []).
    component('loginForm' , {
        templateUrl : 'login-function/login.template.html',

    controller: ['$http', '$location' ,function loginController($http , $location) {
        var seft = this ;
        $http.get('data/roles.json').then(function (response) {
             seft.data1 = response.data;
        });
        seft.validate = function () {
            /// THIS IS FUNCTION , HAVE TO BUTTON TO SHOW IT
            angular.forEach(seft.data1, function(user) {
                if (user.username == seft.name && user.password == seft.pass) {
                     // seft.role = user.role;
                      if(user.role == "student"){
                          alert("Welcome  " + seft.name) ; //or user.username
                          $location.path('/home/student')
                      }else{
                          if(user.role == "admin"){
                              alert("Welcome  " + seft.name) ;
                              $location.path('/home/admin')
                          }
                      }
                }
            });
        };
    }]
}).directive('highlight', function() {
    return {
        restrict: 'EA',
       // link: function(scope, element, attr) {
        link: function(scope, element1) {
            element1.bind('mouseover', mouseover22);
            element1.bind('mouseout', mouseout22);

            function mouseover22() {
                this.classList.add('highlight');
            }
            function mouseout22() {
                /// The classList property returns the class name(s) of an element, as a DOMTokenList object.
                this.classList.remove('highlight');
            }
        }
    }
});