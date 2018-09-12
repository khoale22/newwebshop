'use strict';
// Register `phoneDetail` component, along with its associated controller and template
angular.module('phoneDetail').
component('phoneDetail' ,{
   /*template : 'TBD: Detail view for <span>{{$ctrl.phoneId}}</span>',
    controller : ['$routeParams',
        function PhoneDetailController($routeParams) {
            this.phoneId = $routeParams.phoneId22;
        }


    ]*/
    templateUrl: 'phone-detail/phone-detail.template.html',
    controller : [ '$http' , '$routeParams', function PhoneDetailController( $http , $routeParams) {
            var self = this ;

        self.setImage = function setImage(imageUrl) {
            self.mainImageUrl = imageUrl;
        };

            $http.get('phones/' + $routeParams.phoneId22 + '.json').then(function (response) {
                self.phone = response.data;
                // DAY LA LOI GOI FUNCTION
                self.setImage(self.phone.images[0]);
            })

        }]


});