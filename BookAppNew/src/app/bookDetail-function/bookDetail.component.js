'use strict';

angular.module('bookDetail').
component('bookDetail' ,{
   templateUrl : 'bookDetail-function/bookDetail.template.html',
   controller : ['$http' , '$routeParams' ,'$location', function bookDetailController($http ,$routeParams , $location) {
      var self = this ;



       $http.get('data/books.json').then(function (response) {
           self.books = response.data;

           angular.forEach(self.books, function (book) {
               if ($routeParams.bookId != null) {
                   alert("bookId not null")
               } else {
                   alert("bookId null")
               }

               if (book.bookId == $routeParams.bookId) {
                   self.eachBook = book;
               }
           })
       });


        //// CAN NOT RUN WHY ??????
        /*   angular.forEach(self.books, function (book) {
               if ($routeParams.bookId != null) {
                   alert("bookId not null")
               } else {
                   alert("bookId null")
               }

               if (book.bookId == $routeParams.bookId) {
                   self.eachBook = book;
               }
           })*/

   }]

});