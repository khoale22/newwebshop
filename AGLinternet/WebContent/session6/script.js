angular.module('MyApp' , []).controller('MyCtl' , function($scope) {
	$scope.technologies = [
		{ name : "C+++" ,like :0 , dislike : 0 , minus : 0},
		{ name : "Java" ,like :0 , dislike : 0 , minus : 0}
	];	
	
	$scope.likeFunction = function(getTech) {
		getTech.like++;
		getTech.minus++
	};
});