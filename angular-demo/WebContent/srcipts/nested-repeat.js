//(function () {
    var app = angular.module('app', []);
    app.controller('nestedRepeatController', ['$scope', function ($scope) {
        var countries = [ 
        	          {
        	        	  name : "vietnam" ,
        	        	  cities: [
        	        		  {name : "hanoi"},
        	        		  {name: "danang"},
        	        		  {name: "hoian"}
        	        	  ]	  
        	          },
                      {
                          name: "UK",
                          cities: [
                              { name: "London1" },
                              { name: "Birmingham" },
                              { name: "Manchester" }
                          ]
                      },
                      {
                          name: "USA",
                          cities: [
                              { name: "Los Angeles" },
                              { name: "Chicago" },
                              { name: "Houston" }
                          ]
                      },
                      {
                          name: "India",
                          cities: [
                              { name: "Hyderabad" },
                              { name: "Chennai" },
                              { name: "Mumbai" }
                          ]
                      }
                     
        ];
        $scope.countries = countries;
    }]);
// })();
