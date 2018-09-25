'use strict';

(function () {
	angular.module('productMaintenanceUiApp').controller('HeaderController', headerController);

	headerController.$inject = ['appName', 'StatusApi'];

	function headerController(appName, statusApi) {

		var self = this;

		/**
		 * The version of the application as reported by the server.
		 *
		 * @type {string}
		 */
		self.version = "";

		/**
		 * The name of the application server that is serving up information.
		 *
		 * @type {string}
		 */
		self.applicationServerName = "";

		/**
		 * The name of the application.
		 *
		 * @type {string}
		 */
		self.appName = appName;

		/**
		 * Initializes the controller.
		 */
		self.init = function() {
			statusApi.get({}, self.setVersion)
		};

		/**
		 * Callback for the response for the backend.
		 *
		 * @param result The data passed from the backend.
		 */
		self.setVersion = function(result) {
			self.version = result.version;
			self.applicationServerName = result.applicationServerName;
		};
	}
})();
