'use strict';

(function () {
    angular.module('productMaintenanceUiApp').controller('NotFoundController', notFoundController);

    
    notFoundController.$inject = ["$stateParams"];

    
    function notFoundController( $stateParams) {
        var self = this;

        const PAGE_NOT_ACCESS = "User does not have access, please contact the service desk.";
        const PAGE_NOT_FOUND = "User does not have access for module, please contact the service desk.";
        self.message = '';

        self.$onInit = function() {
            if($stateParams && $stateParams.pageNotAccess){
                self.message = PAGE_NOT_ACCESS;
            } else{
                self.message = PAGE_NOT_FOUND;
            }
            console.log($stateParams.pageNotAccess);
        }
    }
})();
