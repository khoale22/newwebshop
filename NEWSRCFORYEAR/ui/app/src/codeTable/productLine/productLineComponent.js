/*
 * productLineComponent.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author m314029
 * @since 2.26.0
 */

'use strict';

/**
 * Component to support the page that allows users to show brand Cost Owner T2T.
 *
 * @author vn70529
 * @since 2.12
 */
(function () {

	var app = angular.module('productMaintenanceUiApp');
	app.component('productLineComponent', {
		templateUrl: 'src/codeTable/productLine/productLine.html',
		bindings: {
			selected: '<'
		},
		controller: productLineController
	});

	productLineController.$inject = ['$rootScope', '$scope', 'ngTableParams', 'productLineApi'];
	/**
	 * Constructs for country code Controller.
	 */
	function productLineController($rootScope, $scope, ngTableParams, productLineApi) {

		var self = this;
		/**
		 * Start position of page that want to show on country code table
		 *
		 * @type {number}
		 */
		self.PAGE = 1;
		/**
		 * The number of records to show on the  country code table.
		 *
		 * @type {number}
		 */
		self.PAGE_SIZE = 20;
		self.data = [];
		self.firstSearch = false;
		self.tableParams = null;
		var previousDescriptionFilter = null;
		var previousIdFilter = null;

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.newSearch();
		};

		/**
		 * Resets the table with current filter. If the table has not been created, create the table. Else reload the
		 * table.
		 */
		self.newSearch = function() {
			self.isWaitingForResponse = true;
			self.firstSearch = true;
			if (self.tableParams == null) {
				createProductLinesTable();
			} else {
				self.tableParams.reload();
			}

		};

		/**
		 * Create product lines table.
		 */
		function createProductLinesTable() {
			self.tableParams = new ngTableParams({
				page: self.PAGE, /* show first page */
				count: self.PAGE_SIZE /* count per page */
			}, {
				counts: [],

				getData: function ($defer, params) {
					self.recordsVisible = 0;
					self.data = null;

					self.defer = $defer;
					self.dataResolvingParams = params;

					var includeCounts = false;

					var id = params.filter()["id"];
					var description = params.filter()["description"];

					if (typeof id === "undefined") {
						id = "";
					}
					if (typeof description === "undefined") {
						description = "";
					}

					if(id !== previousIdFilter || description !== previousDescriptionFilter) {
						self.firstSearch = true;
					}

					if (self.firstSearch) {
						includeCounts = true;
						params.page(1);
						self.firstSearch = false;
						self.startRecord = 0;
					}

					previousDescriptionFilter = description;
					previousIdFilter = id;
					self.fetchData(includeCounts, params.page() - 1, id, description);
				}
			});
		}

		/**
		 * Initiates a call to get the list of attribute maintenance records.
		 *
		 * @param includeCounts Whether or not to include getting record counts.
		 * @param page The page of data to ask for.
		 * @param id ID for attribute filtering.
		 * @param description Description for attribute filtering.
		 */
		self.fetchData = function(includeCounts, page, id, description) {
			productLineApi.findAll({
				id: id, description: description, page: page, pageSize: self.PAGE_SIZE, includeCount: includeCounts
			}, loadData, self.fetchError);
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.isWaitingForResponse = false;
			self.success = null;
			self.error = self.getErrorMessage(error);
			if(self.isReturnToTab){
				$rootScope.error = self.error;
				$rootScope.isEditedOnPreviousTab = true;
			}
			self.isReturnToTab = false;
		};

		/**
		 * Returns error message.
		 *
		 * @param error
		 * @returns {string}
		 */
		self.getErrorMessage = function (error) {
			if (error && error.data) {
				if (error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			}
			else {
				return self.UNKNOWN_ERROR;
			}
		};

		/**
		 * Clear filter.
		 */
		self.clearFilter = function () {
			self.dataResolvingParams.filter()["id"] = null;
			self.dataResolvingParams.filter()["description"] = null;
			self.tableParams.reload();
			self.error = '';
			self.success = '';
		};

		/**
		 * Determines if the filter has been cleared or not.
		 */
		self.isFilterCleared = function () {
			if(!self.dataResolvingParams) {
				return true;
			}
			if(!self.dataResolvingParams.filter()["id"] &&
				!self.dataResolvingParams.filter()["description"]) {
				return true
			} else {
				return false;
			}
		};

		/**
		 * Callback for when data is successfully returned from the backend.
		 *
		 * @param results The data returned from the backend.
		 */
		function loadData(results) {
			self.isWaitingForResponse = false;
			self.data = results.data;
			self.defer.resolve(self.data);

			if (results.complete) {
				self.totalPages = results.pageCount;
				self.totalRecords = results.recordCount;
				self.dataResolvingParams.total(self.totalRecords);
			}
		}
	}
})();
