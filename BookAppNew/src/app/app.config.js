'use strict';

angular.module('BookApp').config(['$locationProvider', '$routeProvider',
    function config($locationProvider, $routeProvider) {
        $locationProvider.hashPrefix('!');
        $routeProvider
            .when('/login', {
                // day chinh la component component('phoneList', { ..... studentForm
                template: ' <login-form></login-form>'
            }).when('/home/student', {

            template: ' <student-form></student-form>'
        }).when('/home/admin', {

            template: ' <admin-form></admin-form>'
        }).when('/detail/:bookId', {

            template: ' <book-detail></book-detail>'
        }).otherwise('/login');
    }

]);



