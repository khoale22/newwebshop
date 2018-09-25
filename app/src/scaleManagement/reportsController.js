/*
 * reportsController.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Controller to support the ingredients report.
 *
 * @author d116773
 * @since 2.0.7
 */
(function() {
		angular.module('productMaintenanceUiApp').controller('ScaleReportController', scaleReportController);

		scaleReportController.$inject = ['urlBase', 'DownloadService'];

		/**
	 	* Creates the controller for the ingredients report.
	 	*/
		function scaleReportController(urlBase, downloadService) {

			var self = this;

			self.success = null;
			self.error = null;

			self.ingredients = null;
			self.isWaiting = false;

			self.init = function() {};

			/**
			 * Initiates an export.
			 */
			self.doExportIngredientReport = function() {
				self.isWaiting = true;
				var exportUrl = urlBase + "/pm/scaleReports/ingredient?ingredient=" + self.ingredients;
				var fileName = "IngredientExport.csv";
				downloadService.export(exportUrl, fileName, 1200, function() {self.isWaiting = false;});
			}
		}
	}
)();
