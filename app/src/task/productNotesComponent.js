/*
 *   productNotesComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';
/**
 * Used to display and create notes (candidate comments) for any particular product (candidate or a work request).
 *
 * @author vn40486
 * @since 2.15.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('productNotes', {
		// isolated scope binding
		bindings: {
			workRequest: '<'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/task/productNotes.html',
		// The controller that handles our component logic
		controller: productNotesController
	});
	productNotesController.$inject = ['$scope','EcommerceTaskApi', 'ngTableParams'];
	function productNotesController($scope, ecommerceTaskApi, ngTableParams) {
		var self = this;
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
		 * Whether or not a user is adding a new note.
		 * @type {boolean}
		 */
		self.isAdding = false;
		
		/**
		 * Constructs the ng-table based data table.
		 */
		self.buildTable = function (workRequest) {
			return new ngTableParams(
				{},
				{
					counts: [],
					getData: function ($defer, params) {
						self.isWaiting = true;
						self.isNoRecordsFound = false;
						self.defer = $defer;
						self.dataResolvingParams = params;
						self.fetchData(workRequest);
					}
				}
			)
		};

		/**
		 * Fetches all the noted specific to a product(candidate) from database.
		 */
		self.fetchData = function(workRequest) {
			ecommerceTaskApi.getProductNotes({workRequestId : workRequest.workRequestId},self.loadData,self.handleError);
		}

		/**
		 * Callback for a successful call to get data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function (results) {
			$scope.data = results;
			self.defer.resolve(results);
			if (results && results.length === 0) {
				self.isNoRecordsFound = true;
			}
			self.isWaiting = false;
		};

		/**
		 * Callback that will respond to errors sent from the backend.
		 *
		 * @param error The object with error information.
		 */
		self.handleError = function(error){
			if (error && error.data) {
				self.displayMessage(error.data.error, true);
			} else {
				self.displayMessage("An unknown error occurred.", true);
			}
			self.isWaiting = false;
		};

		/**
		 * Used to reload table data.
		 */
		self.reloadTable = function (clearMsg) {
			if(clearMsg) {self.clearMessage();}
			if(self.tableParams!=null) {
				self.tableParams.reload();
			}
		}

		/**
		 * Used to display any success of failure messages above the data table.
		 * @param message message to be displayed.
		 * @param isError is error or not; True - message displayed in red. False- message get displayed in blue.
		 */
		self.displayMessage = function(message, isError) {
			self.isError = isError;
			self.message = message;
		}

		/**
		 * Used to clear message displayed out of last action by user.
		 */
		self.clearMessage = function() {
			self.isError = false;
			self.message = '';
		}

		/**
		 * Used to handle Add note button click event. Creates a new Row in the data table for user to enter the new
		 * description.
		 */
		self.addNote = function() {
			if(!self.isAdding){
				self.isAdding = true;
				var newResource  = generateNewRow();
				$scope.data.unshift(newResource);
			}
		}

		/**
		 * Generates new angular resource for the new row.
		 * @returns {*}
		 */
		function generateNewRow() {
			var newRow = {comments:'',time:null,userId:null,isEditing : true};
			return angular.extend(newRow, ecommerceTaskApi.prototype);
		}

		/**
		 * Handles when editing is being cancelled.
		 *
		 * @param index Index of data to be refreshed.
		 */
		self.cancel = function(){
			if(self.isAdding) {
				$scope.data.shift();
			}
			self.isAdding = false;
		};

		/**
		 * Handles when reset click event. Reloads data table with the data fetched afresh from db.
		 *
		 * @param index Index of data to be refreshed.
		 */
		self.reset = function(){
			self.isAdding = false;
			self.reloadTable(true);
		};

		/**
		 * Handles save of newly created notes/comment.
		 *
		 * @param index Index of data to be refreshed.
		 */
		self.save = function($event){
			var $btn = self.displayLoadingText(event);
			var comment = $scope.data[0];
			if (self.isAdding && self.validateToAdd(comment)) {
				ecommerceTaskApi.addProductNotes(
					{key : {workRequestId: self.workRequest.workRequestId, sequenceNumber:-1}, comments : comment.comments},
					function (result) {
						self.hideLoadingText($btn);
						self.isAdding = false;
						self.displayMessage('Note added Successfully!!', false);
						self.reloadTable(false);
					},
					function(error) {
						self.hideLoadingText($btn);
						self.handleError(error);
					}
				);
			} else {
				self.hideLoadingText($btn);
				self.displayMessage('Please enter a comment to save.', true);
			}
		};

		/**
		 * Validates whether user entered a comment. Returns FALSE if no comment is entered.
		 * @param comment
		 * @returns {boolean}
		 */
		self.validateToAdd = function(comment) {
			return (comment.comments && comment.comments.length > 0);
		}

		/**
		 * Used to display the loading text based on user's action over the given save/submit buttons.
		 * @param event
		 * @returns {*|jQuery}
		 */
		self.displayLoadingText = function(event) {
			return $(event.target).button('loading');
		}

		/**
		 * Hides and reverts the button state back to the normal, display the original text like save/submit.
		 * @param $btn
		 */
		self.hideLoadingText = function($btn) {
			$btn.button('reset');
		}
		/**
		 * Listen the reset product note event.
		 */
		$scope.$on('openProductNotes', function() {
			self.clearMessage();
			self.isAdding = false;
			$scope.data = [];
			if(self.workRequest) {
				self.tableParams = self.buildTable(self.workRequest);
			}
			$('#productNotesModal').modal('show');
		});
	}
})();
