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
            alert( "fsdf" + id);
            seft.books.push({
                "bookId": id,
                "bookTitle": title,
                "topic": topic,
                "author": author,
                "cost": cost,
                "imgUrl": "imgs/DefaultBookImage.jpg"
            });
       ///// test
            var dataObj = {
                 bookId: id,
                bookTitle: title,
                topic: topic,
                author: author,
                cost: cost,
                imgUrl : "imgs/DefaultBookImage.jpg"
            };
            var res = $http.post('data/phones.json', dataObj);
            res.success(function(data, status, headers, config) {
                $scope.message = data;
            });
            res.error(function(data, status, headers, config) {
                alert( "Can not push data to json file");
       ///// test
            });
            $location.path('/home/admin');
        }
    }]
});

