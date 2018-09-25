/*
 *   casePackInfoComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';


/**
 * MrtInfo -> Mrt Info page component.
 *
 * @author m594201
 * @since 2.5.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('mrtInfo', {
		// isolated scope binding
		bindings: {
			itemMaster: '<',
			productMaster: '<'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/productCasePacks/mrtInfo.html',
		// The controller that handles our component logic
		controller: MrtInfoController
	});

	MrtInfoController.$inject = ['ProductCasePackApi', 'ProductDetailAuditModal','ProductSearchService', '$rootScope'];


	function MrtInfoController(productCasePackApi, ProductDetailAuditModal,productSearchService, $rootScope) {

		/** All CRUD operation controls of Case pack Info page goes here */

		var self = this;

		/**
		 * Whether or not to show audit
		 *
		 * @type {boolean}
		 */
		self.showMrtAuditPanel = false;

		/**
		 * Data being received
		 *
		 * @type {null}
		 */
		self.data = null;

		/**
		 * The list of shipper in an MRT
		 *
		 * @type {Array}
		 */
		self.shipper = [];

		/**
		 * List of flagged shippers to delete
		 *
		 * @type {Array}
		 */
		self.shippersToDelete = [];

		/**
		 * Whether or not fields are disabled
		 *
		 * @type {boolean}
		 */
		self.isDisabled = true;

		/**
		 * Whether or not a new upc is being added to the current MRT
		 *
		 * @type {boolean}
		 */
		self.isAddingNew = false;

		/**
		 * Whether or not the quantity of existing MRT data is being edited
		 *
		 * @type {boolean}
		 */
		self.isEditing = false;

		/**
		 * Page title
		 *
		 * @type {string}
		 */
		self.title = "MRT";

		/**
		 * Value that is the sum of all quantities in an MRT
		 *
		 * @type {number}
		 */
		self.totalQuantity = 0;

		/**
		 * Keeps track of whether front end is waiting for back end response.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * Modified MRT data to send to backend
		 *
		 * @type {null}
		 */
		var mrtSaveData = null;

		/**
		 * Error message.
		 * @type {null}
		 */
		self.error = null;


		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.data = null;
			self.disableReturnToList = productSearchService.getDisableReturnToList();
		};

		this.$onChanges = function () {
			if(self.itemMaster.mrt){
				self.calculateQuantityTotal(self.itemMaster.primaryUpc.shipper);
				self.original = angular.copy(self.itemMaster);
			}
		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			/** Execute component destroy events if any. */
		};

		/**
		 * Callback for a successful call to get mrt info from backend
		 * @param results
		 */
		self.loadData = function (results) {
			self.error = null;
			self.totalQuantity = 0;
			self.isWaitingForResponse = false;
			self.success = results.message;
			self.shippersToDelete = [];
			self.isAddingNew = false;
			self.calculateQuantityTotal(results.data);
			self.itemMaster.primaryUpc.shipper = results.data;
			self.originalShipperList = angular.copy(results.data);
		};

		/**
		 * Sends modified mrt to the backend for saving
		 */
		self.saveMrtInfo = function () {
			self.success = null;
			self.error = null;
			var addToMrtData = [];
			self.isEditing = false;
			self.isWaitingForResponse = true;
			if(self.isAddingNew) {
				angular.forEach(self.itemMaster.primaryUpc.shipper, function (value, index) {
					//Check to see if the data is a new row.  If it is push it onto array that will be sent to backend.
					if(angular.isDefined(value.newRow) && value.newRow === true){
						addToMrtData.push(self.itemMaster.primaryUpc.shipper[index]);
						for(var i = 0; i < addToMrtData.length ; i++){
							addToMrtData[i].key.upc = self.itemMaster.primaryUpc.upc;
						}
					}
				});
				productCasePackApi.saveNewToMrt(addToMrtData, self.loadData, self.fetchError);

			} else {
				mrtSaveData = angular.copy(self.itemMaster);
				productCasePackApi.saveMrt(mrtSaveData, self.loadData, self.fetchError)
			}
		};


		/**
		 * Set values to original value
		 */
		self.reset = function () {
			self.error = null;
			self.success = false;
			self.itemMaster = angular.copy(self.original);
			self.isAddingNew = false;
			self.isEditing = false;
			self.shippersToDelete = [];

		};

		/**
		 * Add new row to Add UPC to an MRT
		 */
		self.addRow = function (){
			if(self.itemMaster.primaryUpc === null) {
				self.itemMaster.primaryUpc = [];
				self.itemMaster.primaryUpc.shipper = [];
			}
			self.itemMaster.primaryUpc.shipper.push({
				newRow: true
			});
			self.isAddingNew = true;
		};

		/**
		 * Checks to see if necessary fields have been filled to enable the save button.
		 *
		 * @returns {boolean} disabled flag.  This determines if the save button is active.
		 */
		self.disableSave = function () {

			var newData = [];

			if(self.isAddingNew){

				var disabled = true;

				angular.forEach(self.itemMaster.primaryUpc.shipper, function (value, index) {
					//Check to see if the data is a new row.  If it is push it onto array that will be checked for defenition.
					if(angular.isDefined(value.newRow) && value.newRow === true){
						$rootScope.contentChangedFlag = true;
						newData.push(self.itemMaster.primaryUpc.shipper[index]);
					}
				});

				// if new row does not have a shipperQuantity and a key/upc disable the save button
				angular.forEach(newData, function (value, index) {
					disabled = !angular.isDefined(value.shipperQuantity && value.key);
				});

				return disabled;

				//else check if its an edit and if quantity has been filed
			} else {
				if((angular.toJson(self.original.primaryUpc.shipper) !== angular.toJson(self.itemMaster.primaryUpc.shipper))){
					$rootScope.contentChangedFlag = true;
					return false;
				} else{
					return true;
				}
			}
		};

		self.newRow = function (index, shipper) {
			return angular.isUndefined(shipper.key) && self.isAddingNew === true;
		};

		/**
		 * Sets editing flag to enable quantity fields
		 */
		self.editMrt = function () {
			self.reset();
			self.isEditing = true;
		};

		/**
		 * Creates a blank row to enter a new upc into an mrt
		 * @param shipper
		 */
		self.rowChecked = function (shipper) {
			if(shipper.isChecked.isDefined || shipper.isChecked === true) {
				this.shippersToDelete.push(shipper);
			}

		};

		/**
		 * Populates dynamic list of checked rows.  Then sent to back end for removal from MRT.
		 */
		self.deleteMrt = function () {

			var modifiedDeletedShippers = this.shippersToDelete;

			angular.forEach(this.shippersToDelete, function (value, index) {

				if(modifiedDeletedShippers[index].isChecked.isUndefined || modifiedDeletedShippers[index].isChecked === false) {
					modifiedDeletedShippers.splice(index, 1);
				}
			});

			if(modifiedDeletedShippers !== null) {
				productCasePackApi.deleteFromMrt(modifiedDeletedShippers, self.loadData, self.fetchError);
			}
		};

		/**
		 * Fetches the error from the back end.
		 *
		 * @param error
		 */
		self.fetchError = function(error) {
			self.isWaitingForResponse = false;
			if(error && error.data) {
				self.isLoading = false;
				if (error.data.message) {
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
		 * Sets the controller's error message.
		 *
		 * @param error The error message.
		 */
		self.setError = function(error) {
			self.error = error;
		};

		/**
		 * Shows the MRT audit panel
		 */
		self.showMrtAuditInfo = function() {
			self.getMrtAuditInfo = productCasePackApi.getMrtAuditInformation;
			self.auditParams = {upc: self.itemMaster.primaryUpc.upc};
			ProductDetailAuditModal.open(self.getMrtAuditInfo, self.auditParams, self.title);
		};

		/**
		 * Takes in a list of shippers and calculates the sum of the quantities
		 * @param shipper list
		 */
		self.calculateQuantityTotal = function(shipper) {
			self.totalQuantity=0;
			angular.forEach(shipper, function (value, index) {
				self.totalQuantity = self.totalQuantity + shipper[index].shipperQuantity;
			});
		};
		/**
		 * handle when click on return to list button
		 */
		self.returnToList = function(){
			$rootScope.$broadcast('returnToListEvent');
		};
	}

})();
