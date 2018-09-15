'use strict';

angular.module('studentForm').
    component('studentForm' , {
        templateUrl : 'student-function/student.template.html',

    controller: ['$http', '$location' ,function loginController($http , $location) {
        var seft = this ;
        $http.get('data/books.json').then(function (response) {
            seft.books = response.data;
        });
    }]
});
