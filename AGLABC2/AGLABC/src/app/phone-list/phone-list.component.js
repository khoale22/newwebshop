'use strict' ;

angular.module('phoneList' , []).component('phoneList', {
    templateUrl : 'phone-list/phone-list.template.html',

    controller : [ '$http' , function phoneListController($http) {
          var seft = this;
          this.startSortByName = 'name' ;
        $http.get('phones/phones.json').then(function (response) {
            seft.phones = response.data;
        });
    }]

});


