'use strict';

(function() {
	var app = angular.module('productMaintenanceUiApp');
	app.directive('confirmationModal', confirm);
	confirm.$inject = [];

	function confirm() {
		return{
			restrict: 'E',
			templateUrl: 'src/utils/confirm/confirmationModal.directive.html',
			scope: {},
			link: function($scope, element, attrs) {
				/**
				 * Send the results of the confirmation modal to the rootScope
				 * @param result
				 */
				$scope.sendResults = function(result){
					$scope.$emit('modalResults', {result: result})
				}
			}
		}
	}
})();
