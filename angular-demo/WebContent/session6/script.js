var app = angular.module('TEDU', []);

app.controller('handleEventController', ['$scope', function ($scope) {
     $scope.technologies = [
        { Name: "C#", Likes: 0,Minus:0, Dislikes: 0 },
        { Name: "ASP.NET", Likes: 0,Minus:0, Dislikes: 0 },
        { Name: "SQL Server", Likes: 0, Minus: 0, Dislikes: 0 },
        { Name: "AngularJS", Likes: 0, Minus: 0, Dislikes: 0 }
    ];
  
    $scope.increaseLike = function (technology22) {
        technology22.Likes++;
        technology22.Minus = technology22.Likes - technology22.Dislikes;
    }
    $scope.increaseDislike = function (technology) {
        technology.Dislikes++;
        technology.Minus = technology.Likes - technology.Dislikes;
    }
}]);