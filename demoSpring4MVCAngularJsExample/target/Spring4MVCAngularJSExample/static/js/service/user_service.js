'use strict';
angular.module('myApp').factory('UserService', ['$http', '$q', function($http, $q){
    /// This is from eclipse
  /*  var REST_SERVICE_URI = 'http://localhost:8080/demoSpring4MVCAngularJsExample/user';*/

   var REST_SERVICE_URI = 'http://localhost:8080/user';
    var factory = {        
        fetchAllUsers: fetchAllUsers,
      /*  createUser: createUser,
        updateUser:updateUser,
        deleteUser:deleteUser*/
    }; 
	
    return factory;
    
    function fetchAllUsers() { 
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while fetching Users from service'); 
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }
	
}]);