'use strict';
// Register `phoneDetail` component, along with its associated controller and template
angular.module('phoneDetail22').
component('phoneDetail' ,{
   /*template : 'TBD: Detail view for <span>{{$ctrl.phoneId}}</span>',
    controller : ['$routeParams',
        function PhoneDetailController($routeParams) {
            this.phoneId = $routeParams.phoneId22;
        }


    ]*/

   /// LUU Y '$HTTP'  '$ROUTEPARAMS' PHAI THEO THU TU   $http , $routeParams
    /// angular.js:14199 TypeError: $http.get is not a function
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