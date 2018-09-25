/*
 *
 * checkstatusController.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author vn87351
 * @since 2.14.0
 */
'use strict';

/**
 * The controller for the check status Controller.
 */
(function() {

	angular.module('productMaintenanceUiApp').controller('checkstatusController', checkstatusController);

	checkstatusController.$inject = ['$sce','urlBase','$scope','$http','checkstatusApi', 'ngTableParams', '$log','DownloadService','$stateParams','$interval'];

	/**
	 * Constructs the controller.
	 */
	function checkstatusController($sce,urlBase,$scope,$http,checkstatusApi, ngTableParams, $log,downloadService,$stateParams,$interval) {

		var self = this;
		self.PAGE_SIZE = 10;
		self.WAIT_TIME = 120;
		self.downloading = false;
		$scope.actionUrl =urlBase+'/pm/batchUpload/assortment/download-template';
		/**
		 * Whether or not the controller is waiting for data
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * Attributes for each row in the table
		 * @type {Array}
		 */
		self.attributes = [];

		/**
		 * Used to keep track of the number of columns in the table
		 * @type {Array}
		 */
		self.columns = [];
		/**
		 * The maximum number of priorities an attribute has.
		 * @type {number}
		 */
		self.maxNumberofPriorities = 0;
		/**
		 * Used to keep track of the attribute name
		 * @type {Array}
		 */
		self.attributeNames = [];
		/**
		 * data source of tracking table
		 * @type {Array}
		 */
		self.data = null;
		/**
		 * the tracking selected
		 * @type {null}
		 */
		self.selectedItem=null;
		/**
		 * show deteail tracking
		 * @type {boolean}
		 */
		self.showDetail=false;
		/**
		 * tracking id on param
		 * @type {number}
		 */
		self.trackingId = -1;
		// store the interval promise in this variable
		self.promise;
		self.firstSearch=true;
		/**
		 * Initiates the construction of the Check statsus
		 */

		/**
		 * Request ID for filter.
		 */
		self.requestIdFilter = null;

		/**
		 * Data origin of check status function.
		 * @type {Array}
		 */
		self.originData = [];

		/**
		 * First load page check stutus.
		 * @type {boolean}
		 */
		self.firstLoad = true;

		self.init = function () {
			self.isWaiting=true;
			self.buildColumns();
			/**
			 * Ng-table params variable for maintaining data.
			 */
			if($stateParams && $stateParams.trackingId){
				self.trackingId = $stateParams.trackingId;
			}
			self.tableParams = self.buildTable();
			//$scope.start();
		};
		self.checkAutoRefresh = function(){
			var auto = document.getElementById("auto-refresh");
			if(auto.checked){
				$scope.start();
			}else{
				$scope.stop();
			}
		}
		// stops the interval when the scope is destroyed,
		// this usually happens when a route is changed and
		// the Controller $scope gets destroyed. The
		// destruction of the Controller scope does not
		// guarantee the stopping of any intervals, you must
		// be responsible for stopping it when the scope is
		// is destroyed.
		$scope.$on('$destroy', function() {
			$scope.stop();
		});
		self.autoRefresh = function(){
			var auto = document.getElementById("auto-refresh");
			if(auto.checked){
				self.tableParams.page(1);
				self.tableParams.reload()
			}
		}
		// stops the interval
		$scope.stop = function() {
			$interval.cancel(self.promise);
		};
		// starts the interval
		$scope.start = function() {
			// stops any running interval to avoid two intervals running at the same time
			$scope.stop();

			// store the interval promise
			self.promise=$interval(self.autoRefresh,10000);
		};
		/**
		 * refresh page function
		 */
		self.clearResult = function(){
			self.requestIdFilter = null;
			self.error = null;
			self.data=[];
			self.selectedItem=null;
			self.tableParams.page(1);
			self.dataResolvingParams.total(self.originData.recordCount);
			self.tableParams.reload();
	}
		self.manualRefresh = function(){
			self.error = null;
			self.tableParams.page(1);
			self.tableParams.reload()
		}
		/**
		 *If there is an error this will display the error
		 * @param error
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			self.data = null;
			if (error && error.data) {
				if (error.data.message != null && error.data.message != "") {
					self.setError(error.data.message);
				} else {
					self.setError(error.data.error);
				}
			}
			else {
				self.setError("An unknown error occurred.");
			}
		};

		/**
		 * Constructs the ng-table.
		 */
		self.buildTable = function () {
			return new ngTableParams(
				{
					page: 1,
					count: self.PAGE_SIZE
				}, {
					counts: [],
					getData: function ($defer, params) {
						self.data = null;
						self.isWaiting = true;
						self.defer = $defer;
						self.dataResolvingParams = params;
						self.includeCounts = false;
						if (self.firstSearch) {
							self.includeCounts = true;
							self.firstSearch = false;
						}
						self.getTrackingData(params.page()-1);
					}
				}
			)
		};
		/**
		 * get list tracking info from database with page number
		 * @param page
		 */
		self.getTrackingData = function (page) {
			if(self.trackingId < 0){
				var requestId = self.isNullOrEmpty(self.requestIdFilter) ? '' : self.requestIdFilter;
				checkstatusApi.getListTracking({
					includeCounts: self.includeCounts,
					page: page,
					pageSize: self.PAGE_SIZE,
					requestId: requestId
				}).$promise.then(self.loadData).catch(function(error) {
					console.log(error);
				}).finally(function(){
					PMCommons.displayLoading(self, false);
				});
			}else{
				checkstatusApi.getTrackingDetailById({
					trackingId: self.trackingId
				}).$promise.then(self.loadData).catch(function(error) {
					console.log(error);
				}).finally(function(){
					PMCommons.displayLoading(self, false);
				});
			}
		}
		/**
		 * Callback for a successful call to get list tracking data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function (results) {
			self.isWaiting = false;
			self.error = null;
			if (self.isNullOrEmpty(self.requestIdFilter) && self.firstLoad){
				self.originData  = angular.copy(results);
				self.firstLoad = false;
			}
			self.firstSearch=false;
			if(!self.searchAfterHandle){
				self.success = null;
			} else {
				self.success = self.messageWaiting;
			}

			// If this was the fist page, it includes record count and total pages .
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
			}
			if (results.data.length === 0) {
				self.isWaiting = false;
				self.data = null;
				self.dataResolvingParams.data = [];
				if(!self.searchAfterHandle){
					self.error = "No records found.";
				}
			} else {
				self.error = null;
				self.data = results.data;
				self.defer.resolve(results.data);
			}
			if(self.searchAfterHandle){
				self.searchAfterHandle = false;
			}
		};

		/**
		* selected tracking and show detail
		 * @author vn87351
		 **/
		self.selectTrackingId = function(data){
			self.selectedItem=data;
			self.showDetail=true;
		}
		/**
		 * Creates a list with all of the column names based on the priorities
		 * @param maxNumber
		 */
		self.buildColumns = function(){
			self.columns.push(
				{	title: 'Request ID',
					field: 'requestId',
					visible: true},
				{
					title: 'Attribute Selected',
					field: 'attrSelected',
					visible: true},
				{	title: 'Description',
					field: 'updtDescription',
					visible: true},
				{title: 'Date & Time of Request',
					field: 'dateTime',
					visible: true},
				{title: 'Status',
					field: 'status',
					visible: true},
				{title: 'Result',
					field: 'result',
					visible: true},
				{title: 'User',
					field: 'userId',
					visible: true});

		};

		/**
		 * Convert date string to datetime.
		 */
		self.convertStringToDateTime = function(dateString){
			return new Date(dateString.split(' ')[0] + ' ' + dateString.split(' ')[1].replace(/-/g,":"));
		};

		/**
		 * Check object null or empty.
		 *
		 * @param object
		 * @returns {boolean}
		 */
		self.isNullOrEmpty = function (object) {
			return object === undefined || object === null || !object || object === "";
		};

		/**
		 * Handle event when filter by request id.
		 */
		self.searchByID = function () {
			self.firstSearch = true;
			self.tableParams = self.buildTable();
		};
	}
})();
