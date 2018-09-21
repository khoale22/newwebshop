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
  //  self.edit = edit;
    fetchAllUsers();

    function fetchAllUsers(){
        UserService.fetchAllUsers()
            .then(
            function(d222) {
                self.users = d222;
            },
            function(errResponse){
                console.error('Error while fetching Users from controller');
            }
        );
    }

    /*self.edit = function edit(getU) {
        self.getU22 = getU ;
    }*/


   self.edit = function edit(id){
        console.log('id to be edited', id);
        for(var i = 0; i < self.users.length; i++){
            if(self.users[i].id === id) {
                self.user = angular.copy(self.users[i]);
                break;
            }
        }
    };

    self.submit = function() {
       if(self.user.id == null){
           createUser(self.user);
           console.log("createUser with user :" + self.user);
       }else{
           updateUser(self.user.id , self.user);
           console.log("update with user :" + self.user.id);
       }
   };

    function updateUser(id , user){
        console.log("update runinto :" + self.user.id);
        console.log("update runinto :" + id);
        UserService.updateUser(id , user)
            .then(
                fetchAllUsers, // goi cai nay de thay doi lai du lieu form o duoi
                function(errResponse){
                    console.error('Error while updating User');
                }
            );
    }


    function createUser(user){
        console.log("create runinto :" + user);
        UserService.createUser(user)
            .then(
                fetchAllUsers, // goi cai nay de thay doi lai du lieu form o duoi
                function(errResponse){
                    console.error('Error while create User');
                }
            );

    }

   self.reset = function () {
       self.user = null;
   }



}]);
