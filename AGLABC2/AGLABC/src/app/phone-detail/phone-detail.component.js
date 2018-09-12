'use strict' ;

angular.module('phoneDetail').
    component('phoneDetail' , {
        templateUrl : 'phone-detail/phone-detail.template.html',

        controller : [ '$routeParams' , '$http'  ,  function controllerListDetail($routeParams ,$http ) {
            var self = this ;



            self.setImage = function(imgUrt){
                self.imageOfUrt = imgUrt;
            }



            $http.get('phones/'+$routeParams.phoneId22+'.json').then(function (response) {
                self.phone = response.data ;
                self.imageOfUrt = self.phone.images[0];

            })
        }]





});