/*
 * nutritionUpdatesTaskController.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Controller to handle lifecycle of nutrition updates task screen and handle its associated functions.
 *
 * @author vn40486
 * @since 2.11.0
 */
(function(){
	angular.module('productMaintenanceUiApp')
		.controller('NutritionUpdatesTaskController', nutritionUpdatesTaskController);
	nutritionUpdatesTaskController.$inject = ['$scope', '$location', 'NutritionUpdatesTaskApi', 'ngTableParams', 'ProductSearchService', '$state',
		'appConstants', 'DownloadService', 'urlBase'];

	/**
	 * TaskSummaryController definition.
	 * @param taskSummaryApi api used for data server communication.
	 */
	function nutritionUpdatesTaskController($scope, $location, nutritionUpdatesTaskApi, ngTableParams, productSearchService, $state,
			appConstants, downloadService, urlBase) {
		var self = this;

		/**
		 * Denotes whether back end should fetch total counts or not. Accordingly server side will decide on executing
		 * additional logic to fetch count information.
		 *
		 * @type {boolean}
		 */
		self.includeCounts = false;

		/**
		 * The number of rows per page constant.
		 * @type {int}
		 */
		self.PAGE_SIZE = 15;
		/**
		 * The number of rows per page constant when navigate to Home page.
		 * @type {int}
		 */
		self.NAVIGATE_PAGE_SIZE = 100;

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
		 * The total number of records in the report.
		 * @type {int}
		 */
		self.totalRecordCount = null;

		/**
		 * The parameters passed to the application from ng-tables getData method. These need to be stored
		 * until all the data are fetched from the backend.
		 *
		 * @type {null}
		 */

		self.dataResolvingParams = null;
		/**
		 * The promise given to the ng-tables getData method. It should be called after data is fetched.
		 *
		 * @type {null}
		 */
		self.defer = null;

		/**
		 * The data being shown in the report.
		 * @type {Array}
		 */
		$scope.data = null;

		/**
		 * The message to display about the number of records viewing and total (eg. Result 1-100 of 130).
		 * @type {String}
		 */
		self.resultMessage = null;

		/**
		 * Keeps track of selected selected Alerts/Rows.
		 * @type {Array}
		 */
		self.selectedAlerts = [];

		/**
		 * Keeps track of excluded Alerts/Rows when "Select All" is checked.
		 * @type {Array}
		 */
		self.excludedAlerts = [];

		/**
		 * maintains state of data call.
		 * @type {boolean}
		 */
		self.firstFetch = true;

		/**
		 * Represents state of Select All check box.
		 */
		self.allAlerts = false;

		/**
		 * Search type product for ecommerce view
		 * @type {String}
		 */
		const SEARCH_TYPE = "basicSearch";
		/**
		 * Selection type product for ecommerce view
		 * @type {String}
		 */
		const SELECTION_TYPE = "Product ID";
		/**
		 * Nutrition Fact tab
		 * @type {String}
		 */
		const NUTRITION_FACT_TAB = 'nutritionFactsTab';

		/**
		 * Constructs the ng-table based data table.
		 */
		self.buildTable = function () {
			return new ngTableParams(
				{
					page: 1,
					count: self.PAGE_SIZE
				}, {
					counts: [],
					getData: function ($defer, params) {
						self.isWaiting = true;
						self.defer = $defer;
						self.dataResolvingParams = params;
						self.includeCounts = false;
						if (self.firstFetch) {
							self.includeCounts = true;
						}
						self.getAllActiveNutritionUpdates(params.page() - 1);
					}
				}
			)
		};

		/**
		 * Fetches all the nutrition updates from database with pagination.
		 * @param page  selected page number.
		 */
		self.getAllActiveNutritionUpdates = function(page) {
			nutritionUpdatesTaskApi.getAllActiveNutritionUpdates(
				{
					includeCounts: self.includeCounts,
					page : page,
					pageSize : self.PAGE_SIZE
				},
				self.loadData,self.handleError);
		};

		/**
		 * Ng-table params variable for maintaining data table configurations.
		 */
		self.tableParams = self.buildTable();

		/**
		 * Callback for a successful call to get data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function (results) {
			// If this was the fist page, it includes record count and total pages.
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
				self.firstFetch = false;
			}
			self.resultMessage = self.getResultMessage(results.data.length, self.tableParams.page() -1);
			self.renderUserSelection(results.data, self.allAlerts, self.selectedAlerts, self.excludedAlerts);
			$scope.data = results.data;
			self.defer.resolve(results.data);
			if (results.data && results.data.length === 0) {
				self.error = "No records found.";
			}
			self.isWaiting = false;
		};

		/**
		 * Used to maintain the state previous selections by the user.
		 * @param alertsDataArr	new list of data fetched from backend.
		 * @param allAlertsChecked is select all checked; true/false.
		 * @param selectedAlerts list of selected alert ids.
		 * @param excludedAlerts list of excluded alerts.
		 */
		self.renderUserSelection = function(alertsDataArr, allAlertsChecked, selectedAlerts, excludedAlerts) {
			if (allAlertsChecked) {
				_.forEach(alertsDataArr, function (alert) {
					var index = _.findIndex(excludedAlerts, function(o) { return o == alert.alertID;});
					if (index > -1) {
						alert.checked = false;
					} else {
						alert.checked = true;
					}
				});
			} else {
				_.forEach(alertsDataArr, function (alert) {
					var index = _.findIndex(selectedAlerts, function(o) { return o == alert.alertID;});
					if (index > -1) {
						 alert.checked = true;
					} else {
						alert.checked = false;
					}
				});
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
			return "" + (self.PAGE_SIZE * currentPage + 1) +
				" - " + (self.PAGE_SIZE * currentPage + dataLength) + "  of  " +
				self.totalRecordCount.toLocaleString();
		};

		/**
		 * Used to reload table data.
		 */
		self.refreshTable = function (page, clearMsg) {
			if(clearMsg) {self.clearMessage();}
			self.firstFetch = true;
			self.resetAlertSelection(true);
			self.tableParams.page(page);
			self.tableParams.reload();
		};

		/**
		 * Resets all previous row selections when "Select All" is checked/unchecked.
		 */
		self.resetAlertSelection = function(includeSelectAll) {
			if (includeSelectAll) {
				self.allAlerts = false;
			}
			self.selectedAlerts = [];
			self.excludedAlerts = [];
		};

		/**
		 * Keeps track of row selection and exclusions in consideration to the state of "Select All" option.
		 *
		 * @param alertChecked selected row state. True/False.
		 * @param alert	data (Alert) object of the row that was modified.
		 */
		self.toggleAlertSelection = function(alert) {
			if (self.allAlerts) {//is "Select All" checked?
				!alert.checked ? self.excludedAlerts.push(alert.alertID) : _.pull(self.excludedAlerts, alert.alertID);
			} else {
				alert.checked ? self.selectedAlerts.push(alert.alertID) : _.pull(self.selectedAlerts, alert.alertID)
			}
		};

		/**
		 * Handle select alert to reject.
		 */
		self.handleRejectAlert = function (){
			if((self.allAlerts && self.excludedAlerts.length < self.totalRecordCount)||(self.selectedAlerts.length > 0)){
				$('#rejectReasonModal').modal({ backdrop: 'static', keyboard: true });
			}else {
				$('#confirmSelectAlertModal').modal({ backdrop: 'static', keyboard: true });
			}
		};

		/**
		 * Send alert id to back end to be deleted.
		 *
		 * @param index Index of data to be removed.
		 */
		self.doRejectAlert = function(rejectReason){
			self.clearMessage();
			var data = {};
			if (self.allAlerts) {
				self.isWaiting = true;
				data = {'rejectReason' : rejectReason,'excludedAlertIds' : self.excludedAlerts};
				nutritionUpdatesTaskApi.rejectAllUpdates(data,self.handleSuccess,self.handleError);
			} else {
				self.isWaiting = true;
				data = {'rejectReason' : rejectReason,'alertIds' : self.selectedAlerts};
				nutritionUpdatesTaskApi.rejectUpdates(data,self.handleSuccess,self.handleError);
			}
		};

		/**
		 * Backup search condition product  for ecommerce View
		 */
		self.navigateToNutritionFact = function(productId, alertId) {
			//Set search condition
			productSearchService.setSearchType(SEARCH_TYPE);
			productSearchService.setSelectionType(SELECTION_TYPE);
			productSearchService.setSearchSelection(parseFloat(productId));
			productSearchService.setListOfProducts($scope.data);
			//Backup alert id
			productSearchService.setAlertId(alertId);
			//Set selected tab is ecommerceViewTab tab to navigate ecommerce view page
			productSearchService.setSelectedTab(NUTRITION_FACT_TAB);
			//productGroupService.setProductGroupId(self.cusProductGroup.customerProductGroup.custProductGroupId);
			//Set from page navigated to
			productSearchService.setFromPage(appConstants.NUTRITION_UPDATES_TASK);
			productSearchService.setDisableReturnToList(false);

			productSearchService.productUpdatesTaskCriteria = {};
			productSearchService.productUpdatesTaskCriteria.pageIndex = Math.floor((((self.tableParams.page()-1)*self.PAGE_SIZE)+(self.PAGE_SIZE-1))/self.NAVIGATE_PAGE_SIZE)+1;
			productSearchService.productUpdatesTaskCriteria.pageSize = self.NAVIGATE_PAGE_SIZE;

			$state.go(appConstants.HOME_STATE);
		};

		/**
		 * Return of back end after a user removes an alert. Based on the size of totalRecordCount,
		 * this method will either:
		 * 	--reset view to initial state
		 * 	--re-issue call to back end to update information displayed
		 *
		 * @param results Results from back end.
		 */
		self.handleSuccess = function(data){
			self.displayMessage(data.message, false);
			self.refreshTable(self.tableParams.page(), false);
		};

		/**
		 * Callback that will respond to errors sent from the backend.
		 *
		 * @param error The object with error information.
		 */
		self.handleError = function(error){
			if (error && error.data) {
				self.displayMessage(error.data, true);
			} else {
				self.displayMessage("An unknown error occurred.", true);
			}
			self.isWaiting = false;
		};

		/**
		 * Used to display any success of failure messages above the data table.
		 * @param message message to be displayed.
		 * @param isError is error or not; True - message displayed in red. False- message get displayed in blue.
		 */
		self.displayMessage = function(message, isError) {
			self.isError = isError;
			self.message = message;
		};

		self.clearMessage = function() {
			self.isError = false;
			self.message = '';
		};

		/**
		 * Initiates a download of all the records.
		 */
		self.export = function() {
			var encodedUri = self.generateExportUrl();
			if(encodedUri !== self.EMPTY_STRING) {
				self.downloading = true;
				downloadService.export(encodedUri, 'nutritionUpdates.csv', self.WAIT_TIME,
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
			return urlBase + '/pm/task/nutritionUpdates/exportNutritionUpdatesToCsv';
		};
	}
})();
