'use strict';

angular.module('adminForm' , []).
component('adminForm' , {
    templateUrl : 'admin-function/admin.template.html',


    controller: ['$http', '$location' ,function loginController($http , $location) {
        var seft = this ;
        $http.get('data/books.json').then(function (response) {
            seft.books = response.data;
        });

    }]
});