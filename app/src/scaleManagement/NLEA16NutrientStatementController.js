/*
 *
 * NLEA16NutrientStatementController.js
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

'use strict';

/**
 * The controller for the Scale Management NLEA Nutrients Controller.
 */
(function() {
	angular.module('productMaintenanceUiApp').controller('NLEA16NutrientStatementController', nutrientStatementController);

	nutrientStatementController.$inject = ['ScaleManagementApi', 'ngTableParams', 'PermissionsService','urlBase','DownloadService'];

	/**
	 * Constructs the controller.
	 * @param scaleManagementApi The API to fetch data from the backend.
	 * @param ngTableParams The API to set up the report table.
	 * @param permissionsService The service that gives access to a user's permissions.
	 */
	function nutrientStatementController(scaleManagementApi, ngTableParams, permissionsService, urlBase, downloadService) {

		var self = this;

		/**
		 * Wheterer or not the controller is waiting for data.
		 * @type {boolean}
		 */
		self.isWaiting = false;
		/**
		 * Tracks whether or not the user is waiting for a download.
		 *
		 * @type {boolean}
		 */
		self.downloading = false;
		/**
		 * Max time to wait for excel download.
		 *
		 * @type {number}
		 */
		self.WAIT_TIME = 1200;

		/**
		 * Whether or not the search search panel is collapsed or open.
		 *
		 * @type {boolean}
		 */
		self.searchPanelVisible = true;

		/**
		 * The source system reference id.
		 * @type {String}
		 */
		self.sourceSystemReferenceId = null;

		/**
		 * Wheterer or not the controller is waiting for search panel in add new panel modal.
		 * @type {boolean}
		 */
		self.isWaitingSearchPanel = false;

		/**
		 * Whether or not this is the first search with the current parameters.
		 * @type {boolean}
		 */
		self.firstSearch = true;

		/**
		 * The ngTable object that will be waiting for data while the report is being refreshed.
		 * @type {?}
		 */
		self.defer = null;

		/**
		 * The paramaters passed from the ngTable when it is asking for data.
		 * @type {?}
		 */
		self.dataResolvingParams = null;

		/**
		 * Whether or not the user is searching for all nutrients.
		 * @type {boolean}
		 */
		self.findAll = false;

		/**
		 * Original state of nutrient code being edited.
		 * @type {null}
		 */
		self.currentEditingObject = null;

		/**
		 * The number of records to show on the report.
		 * @type {number}
		 */
		self.PAGE_SIZE = 16;

		/**
		 * The data being shown in the report.
		 * @type {Array}
		 */
		self.data = null;

		/**
		 * Whether or not to ask the backed for the number of records and pages are available.
		 * @type {boolean}
		 */
		self.includeCounts = true;

		/**
		 * Current String for the serving size label
		 *
		 * @type {string}
		 */
		self.servingLabel = "";

		/**
		 * Empty String
		 *
		 * @type {string}
		 */
		self.EMPTY_STRING = "";

		/**
		 * Remaining available characters for the serving label.
		 *
		 * @type {number}
		 */
		self.servingTextCharacterRemainingCharacterCount = 22;

		/**
		 * Initialize the controller.
		 */
		self.init = function(){
			self.searchSelection = null;
		};

		/**
		 * Returns whether or not the user is allowed to edit NLEA nutrient statements.
		 *
		 * @returns {boolean} Whether or not the user is allowed to edit NLEA nutrient statements.
		 */
		self.canEditNutrientStatement = function() {
			return permissionsService.getPermissions("SM_NLEA_01", "EDIT");
		};

		/**
		 * Clears the search box.
		 */
		self.clearSearch = function () {
			self.searchSelection = null;
		};

		/**
     	 * get nutrient panel by source system reference id.
		 */
		self.getNutrientPanelBySourceSystemReferenceId = function(){
			self.errorSearchPanel = null;
			self.isWaitingSearchPanel = true;
			scaleManagementApi.getNutrientPanelBySourceSystemReferenceId({
					sourceSystemReferenceId: self.sourceSystemReferenceId
				},
				self.handleAddNewPanelResult,
				self.handleAddNewPanelError);

		};

		/**
		 * Handle go to add new or edit panel page after search data in add new panel.
		 */
		self.handleAddNewPanelResult = function (results){
			self.isWaitingSearchPanel = false;
			$('#addNewPanelModal').modal("hide");
			$('#viewPanelModal').modal({backdrop: 'static', keyboard: true});
			if(results == null || results.length == 0 ){
				self.viewPanelModalTitle = "Create NLEA 2016 Nutrient Statements";
			}else{
				self.viewPanelModalTitle = "Edit NLEA 2016 Nutrient Statements";

			}
		}

		/**
		 * Callback for when has an error.
		 *
		 * @param error The error.
		 */
		self.handleAddNewPanelError = function(error) {
			self.isWaitingSearchPanel = false;
			if (error && error.data) {
				if (error.data.message !== null && error.data.message !== "") {
					self.errorSearchPanel = error.data.message;
				} else {
					self.errorSearchPanel = error.data.error;
				}
			}
			else {
				self.errorSearchPanel = "An unknown error occurred.";
			};
		};
		/**
		 * Initiates a new search.
		 */
		self.newSearch = function(findAll){
			self.firstSearch = true;
			self.findAll = findAll;
			self.tableParams.page(1);
			self.tableParams.reload();
		};

		/**
		 * Issue call to newSearch to call back end to fetch all nutrition codes.
		 */
		self.searchAll = function (){
			self.searchSelection = null;
			self.newSearch(true);
		};

		/**
		 * Checks whether searchSelection is null and if the find all option is not selected
		 * @returns {boolean} return true
		 */
		self.isCurrentStateNull = function(){
			return (!self.findAll && self.searchSelection == null);

		};

		/**
		 * Constructs the table that shows the report.
		 */
		self.buildTable = function()
		{
			return new ngTableParams(
				{
					// set defaults for ng-table
					page: 1,
					count: self.PAGE_SIZE
				}, {

					// hide page size
					counts: [],

					/**
					 * Called by ngTable to load data.
					 *
					 * @param $defer The object that will be waiting for data.
					 * @param params The parameters from the table helping the function determine what data to get.
					 */
					getData: function ($defer, params) {
						self.defer = $defer;

						if(self.isCurrentStateNull()) {
							self.defer = $defer;
							return;
						}

						self.isWaiting = true;
						self.data = null;

						// Save off these parameters as they are needed by the callback when data comes back from
						// the back-end.
						self.defer = $defer;
						self.dataResolvingParams = params;

						// If this is the first time the user is running this search (clicked the search button,
						// not the next arrow), pull the counts and the first page. Every other time, it does
						// not need to search for the counts.
						if(self.firstSearch){
							self.includeCounts = true;
							self.firstSearch = false;
						} else {
							self.includeCounts = false;
						}

						// Issue calls to the backend to get the data.
						self.getReport(params.page() - 1);
					}
				}
			);
		};

		/**
		 * The parameters that define the table showing the report.
		 */
		self.tableParams = self.buildTable();

		/**
		 *  Calls the method to get data .
		 *
		 * @param page The page to get.
		 */
		self.getReport = function (page) {
			if (self.findAll) {
				self.getReportByAll(page);
				return;
			}else if(self.selectionType == self.STATEMENT_ID){
				self.getReportByStatementId(page);
				return;
			}
		};

		/**
		 * Calls PI method to get dat a based on statement id.
		 *
		 * @param page
		 */
		self.getReportByStatementId = function (page) {
			var searchSelection = [];
			searchSelection = self.searchSelection.split(/[\n,]/g);
			scaleManagementApi.findAllNutrientStatementPanelsByIds({
					nutrientStatementId: searchSelection,
					page: page,
					pageSize: self.PAGE_SIZE,
					includeCounts: self.includeCounts
				},
				self.loadData,
				self.fetchError
			);
		};
		/**
		 * Calls the API method to get data for all.
		 *
		 * @param page
		 */
		self.getReportByAll = function (page) {
			scaleManagementApi.findAllNutrientStatementPanels({
					page: page,
					pageSize: self.PAGE_SIZE,
					includeCounts: self.includeCounts
				},
				self.loadData,
				self.fetchError);
		};

		/**
		 * Callback for a successful call to get data from the backend. It requires the class level defer
		 * and dataResolvingParams object is set.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function(results){
			self.error = null;
			self.success = null;
			self.isWaiting = false;

			// // If this was the first page, it includes record count and total pages .
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
			}
			if (results.data.length === 0) {
				self.data = null;
				self.error = "No records found.";
			}

			else {
				self.resultMessage = self.getResultMessage(results.data.length, results.page);
				self.error = null;
				self.data = results.data;
				self.defer.resolve(results.data);
			}
		};
		/**
		 * Generates the message that shows how many records and pages there are and what page the user is currently
		 * on.
		 *
		 * @param dataLength The number of records there are.
		 * @param currentPage The current page showing.
		 * @returns {string} The message.
		 */
		self.getResultMessage = function(dataLength, currentPage){
			return "Result " + (self.PAGE_SIZE * currentPage + 1) +
				" - " + (self.PAGE_SIZE * currentPage + dataLength) + " of " +
				self.totalRecordCount.toLocaleString();
		};

		/**
		 * Initiates a download of all the records.
		 */
		self.export = function() {
			var encodedUri = self.generateExportUrl();
			if(encodedUri !== self.EMPTY_STRING) {
				self.downloading = true;
				downloadService.export(encodedUri, 'NLEANutrientStatements.csv', self.WAIT_TIME,
					function () {
						self.downloading = false;
					});
			}
		};

		/**
		 * Generates the URL to ask for the export.
		 *
		 * @returns {string} The URL to ask for the export.
		 */
		self.generateExportUrl = function() {
			if (self.selectionType == self.STATEMENT_ID && !self.findAll){
				return urlBase + '/pm/NLEA16NutrientStatement/exportNutrientStatementPanelByIdsToCsv?nutrientStatements=' +
					encodeURI(self.searchSelection) + "&totalRecordCount=" + self.totalRecordCount;
			} else if (self.findAll){
				return urlBase + '/pm/NLEA16NutrientStatement/exportAllNutrientStatementPanelToCsv?totalRecordCount=' +
					self.totalRecordCount;
			} else {
				return self.EMPTY_STRING;
			}
		};
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function(error) {
			self.isWaiting = false;
			self.message = null;
			if (error && error.data) {
				if (error.data.message !== null && error.data.message !== "") {
					self.error = error.data.message;
				} else {
					self.error = error.data.error;
				}
			}
			else {
				self.error = "An unknown error occurred.";
			};
		};

		/**
		 * Handle view nutrientStatement full panel popup.
		 * @param nutrientStatement nutrientStatement selected.
		 */
		self.viewFullPanel = function (nutrientStatement) {
			self.nutrientStatementDetail = angular.copy(nutrientStatement);
			self.viewPanelModalTitle = "Nutrient Statement Detail";
			self.updateServingLabelComponents();
			$("#viewPanelModal").modal("show");
		};

		/**
		 * Updates all of the information for the serving size label.
		 */
		self.updateServingLabelComponents= function () {
			self.servingLabel="";
			var bothPresent = false;
			if(self.nutrientStatementDetail.measureQuantity !== null && self.nutrientStatementDetail.nutrientImperialUom !== null){
				self.formatMeasureQuantity(self.nutrientStatementDetail.measureQuantity);
				self.servingTextCharacterRemainingCharacterCount -= self.nutrientStatementDetail.nutrientImperialUom.uomDisplayName.length;
				self.servingLabel += " " + self.nutrientStatementDetail.nutrientImperialUom.uomDisplayName.trim();
				bothPresent = true;
			} else {
				self.servingLabel+="";
			}
			if(self.nutrientStatementDetail.metricQuantity !== null && self.nutrientStatementDetail.nutrientMetricUom !== null){
				var metricPart = ""+ Math.round(self.nutrientStatementDetail.metricQuantity)+ self.nutrientStatementDetail.nutrientMetricUom.uomDisplayName.trim();
				if(bothPresent){
					metricPart = " (" + metricPart + ")";
				}
				self.servingLabel += metricPart;
			} else {
				self.servingLabel+="";
			}
			self.colorServingLabel();
		};

		/**
		 * This method will determine which characters are in bounds (black) and which ones are out of bounds
		 * (anything over 22, red).
		 */
		self.colorServingLabel = function () {
			var servingLabelTextField = $("#scaleManagementNutrientStatementLabel");
			self.servingTextCharacterRemainingCharacterCount = 22 - self.servingLabel.length;
			if(self.servingLabel.length > 22){
				self.servingLabel = self.servingLabel.substring(0, 22).fontcolor("black") + self.servingLabel.substring(22).fontcolor("red");
			}
			servingLabelTextField.innerHTML = self.servingLabel;
		};

		/**
		 * Breaks up number to whole number and decimal components
		 * The first round for decimal is to correct for binary rounding errors.
		 *
		 * @param number
		 */
		self.formatMeasureQuantity = function (number) {
			var wholeNumber = Math.floor(Number(number));
			var decimal = Math.round(((number%1)*1000))/1000;
			self.convertDecimalToFraction(wholeNumber, Math.floor(((decimal)*100))/100);
		};

		/**
		 * This method will determine if the decimal is to be converted into a fraction or not.
		 *
		 * @param wholeNumber whole number component of fraction
		 * @param decimal decimal component to be possibly converted to fraction.
		 */
		self.convertDecimalToFraction = function (wholeNumber, decimal) {
			var decimalPart = "";
			var converted = false;
			switch(decimal){
				case(0.0):
					decimalPart = "";
					converted = true;
					break;
				case(0.08):
					decimalPart = "1/12 ";
					converted = true;
					break;
				case(0.10):
					decimalPart = "1/10";
					converted = true;
					break;
				case(0.12):
					decimalPart = "1/8";
					converted = true;
					break;
				case(0.16):
					decimalPart = "1/6";
					converted = true;
					break;
				case(0.20):
					decimalPart = "1/5";
					converted = true;
					break;
				case(0.25):
					decimalPart = "1/4";
					converted = true;
					break;
				case(0.33):
					decimalPart = "1/3";
					converted = true;
					break;
				case(0.50):
					decimalPart = "1/2";
					converted = true;
					break;
				case(0.66):
					decimalPart = "2/3";
					converted = true;
					break;
				case(0.75):
					decimalPart = "3/4";
					converted = true;
					break;
				default:
					decimalPart = "" + decimal;
					break;
			}
			if(wholeNumber>0) {
				if(converted){
					if (decimalPart) {
						decimalPart = wholeNumber + " " +decimalPart;
					} else {
						decimalPart = wholeNumber;
					}
				} else {
					decimalPart = wholeNumber + decimalPart.substring(1);
				}
			}
			self.servingLabel += decimalPart;
		};
	}
})();
