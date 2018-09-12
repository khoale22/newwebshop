'use strict';

angular.module('phonecatApp').
    config([ '$locationProvider' , '$routeProvider' ,
       function config($locationProvider , $routeProvider) {
           $locationProvider.hashPrefix('!');

           $routeProvider.
               when('/phones' ,{
                   template : ' <phone-list></phone-list>'
               }).
               when('/phones/:phoneId22' ,{
                    template: '{{$ctrl.phoneId}} <phone-detail></phone-detail>'
               }).
               otherwise('/phones');
       }


]);