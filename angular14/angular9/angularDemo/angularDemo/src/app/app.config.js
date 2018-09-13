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
               /// tuc la khi chay ung dung len se khong co duong dan url nao cu the nen se duoc chuyen ve   when('/phones' ,{
             //template : ' <phone-list></phone-list>'
             //}).
               otherwise('/phones');
       }

]);



