'use strict';


angular.module('bookAdd' ,[]).
component('bookAdd' , {
    templateUrl : 'bookAdd-function/bookAdd.template.html',


    controller: ['$http', '$location' ,function loginController($http , $location) {
        var seft = this ;
        $http.get('data/books.json').then(function (response) {
            seft.books = response.data;
        });

        seft.addBook = function(id, title, topic, author, cost) {
            seft.books.push({
                "bookId": id,
                "bookTitle": title,
                "topic": topic,
                "author": author,
                "cost": cost,
                "imgUrl": "imgs/DefaultBookImage.jpg"
            });
            $location.path('/home/admin');
        }
    }]
});

