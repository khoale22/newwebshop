'use strict';

angular.module('myApp').controller('UserController', ['$scope', 'UserService', function($scope, UserService) {
    var self = this;
    /*self.user={id:null,username:'',address:'',email:''};
    self.users=[];*/

 /*   self.submit = submit;
    self.edit = edit; 
    self.remove = remove;
    self.reset = reset;

*/
    fetchAllUsers();

    function fetchAllUsers(){
        UserService.fetchAllUsers()
            .then(
            function(d222) {
            	$scope.users = d222; 
            },
            function(errResponse){
                console.error('Error while fetching Users from controller');
            }
        );
    }
    
    
    
}]);
