'use strict';
angular.module('myApp').factory('UserService', ['$http', '$q', function($http, $q){
    /// This is from eclipse
  /*  var REST_SERVICE_URI = 'http://localhost:8080/demoSpring4MVCAngularJsExample/user';*/

   var REST_SERVICE_URI = 'http://localhost:8080/user/';
    var factory = {
        fetchAllUsers: fetchAllUsers,
        updateUser  : updateUser,
       createUser: createUser,
       /* updateUser:updateUser,
        deleteUser:deleteUser*!/*/
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


    function updateUser(id ,user) {
        var deferred = $q.defer();
        $http.put(REST_SERVICE_URI+id , user)
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

    function createUser(user) {
        var deferred = $q.defer();
        $http.post(REST_SERVICE_URI , user)
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