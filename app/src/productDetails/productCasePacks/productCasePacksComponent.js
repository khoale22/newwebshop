/*
 *   productCasePacksComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Component of Case Packs data table that is displayed on the Top of the Product detail page.
 *
 * @author vn40486
 * @since 2.2.0
 */
(function () {

	var app = angular.module('productMaintenanceUiApp').component('pdCasePacks', {
		// isolated scope binding
		bindings: {
			prodItems:  "<",
			onItemChange : '&',
			isCollapseCasePack:'=',
			isCollapseSellingUnit:'='
		},
		// Inline template which is bound to message variable in the component controller
		templateUrl: 'src/productDetails/productCasePacks/productCasePacks.html',
		// The controller that handles our component logic
		controller: productCasePacksController
	});

	productCasePacksController.$inject = ['$scope', '$rootScope'];

	/**
	 * Case Packs controller definition.
	 * @param $scope scope of the productCasePacksComponent.
	 */
	function productCasePacksController($scope, $rootScope) {
		var ctrl = this;
		ctrl.selectedItem;
		ctrl.selectedRow = null;
		/**
		 * header scroll style
		 * @returns {{"margin-right": string}}
		 */
		ctrl.getHeaderStyle=function() {
			var heightTable=$(document).find('#casePackTable:first').height();
			var marginRight='';
			if(heightTable>65){
				marginRight='17px';
			}else{
				marginRight='0px';
			}
			var styles = {
				'margin-right': marginRight
			};
			return styles;
		}
		/**
		 * grid scroll style
		 * @returns {{"overflow-y": string}}
		 */
		ctrl.getGridStyles=function() {
			var heightTable=$(document).find('#casePackTable:first').height();
			var overflow='';
			if(heightTable>65){
				overflow='auto';
			}else{
				overflow='hidden';
			}
			var styles = {
				'overflow-y': overflow
			};
			return styles;
		}

		/**
		 * Initializes the controller.
		 */
		this.$onInit = function () {
			console.log('Case Pack Controller - Initialized');
			if(ctrl.prodItems && ctrl.prodItems.length>0){
				resetRowSelection(ctrl.prodItems);
				ctrl.prodItems[0].isSelected = true;//select item first
				ctrl.selectedItem =ctrl.prodItems[0];
			}
			ctrl.onItemChange({item: ctrl.selectedItem});
		};

		/**
		 * Standard AngularJS function ($onChanges) implementation. Used to catch change events of the objects that
		 * bound to this component (bindings can be found listed in the component definition above).
		 * @param changesObj holds the details of change like name of the binding, current value, previous value etc.
		 */
		this.$onChanges = function (changesObj) {
			if (changesObj.prodItems) {
				// ctrl.selectedItem = null;//reset user selected row to none as whole table is reloaded.
				resetRowSelection(ctrl.prodItems);
				ctrl.prodItems[0].isSelected = true;
				ctrl.selectedItem =ctrl.prodItems[0];//select item first
				ctrl.onItemChange({item: ctrl.selectedItem });
			}
		};

		/**
		 * Pop up the confirmation modal before switching rows
		 * @param row
		 */
		ctrl.confirmRowSelect = function(row) {
			ctrl.selectedRow = row;
			if($rootScope.contentChangedFlag) {
				var result = document.getElementById("confirmationModal");
				var wrappedResult = angular.element(result);
				wrappedResult.modal("show");
			} else {
				ctrl.handleRowSelect(ctrl.selectedRow)
			}
		};

		/**
		 * Handles the results of the confirmation modal
		 */
		$rootScope.$on('modalResults', function(event, args){
			if(ctrl.selectedRow !== null && args.result){
				ctrl.handleRowSelect(ctrl.selectedRow)
			} else{
				ctrl.selectedRow = null;
			}
		});

		/**
		 * Expand case pack table
		 */
		ctrl.expandCasePack = function() {
			ctrl.isCollapseCasePack = false;
			//ctrl.isCollapseSellingUnit=true;

		};
		/**
		 * Collapse case pack table
		 */
		ctrl.collapseCasePack = function(w) {
			ctrl.isCollapseCasePack = true;
			ctrl.isCollapseSellingUnit=false;

		};
		/**
		 * Used to handle user's row select/click event on the data table.
		 * @param row	selected row.
		 */
		ctrl.handleRowSelect = function(row) {
			/** Deselect previously selected Row */
			resetRowSelection(ctrl.prodItems);
			if (ctrl.selectedItem) {
				ctrl.selectedItem.isSelected = false;
			}
			/** Set recently clicked row as Selected */
			row.isSelected = true;
			ctrl.selectedItem = row;
			$rootScope.contentChangedFlag = false;
			/** Fire new row(item) selected event. This allows Parent component to decide on appropriate action
			 * like switching to Case Pack Info related tab, reload of work area section etc.*/
			ctrl.onItemChange({item: row});
		};

		/**
		 * Used to reset all the row selection. Sets all the records as deselected.
		 * @param prodItems	data table referenced object.
		 */
		function resetRowSelection(prodItems) {
			_.forEach(prodItems, function(item){
				item.isSelected =false;
			});
		}

		/**
		 * Check an item is edc channel.
		 * @param psItemMaster
		 */
		$scope.checkEdcChannel = function (prodItem) {
			var isEdc = false;
			var message = (prodItem.itemMaster.key.itemType === 'ITMCD' ? 'WHS' : 'DSD');
			if(message === 'WHS'){
				if(prodItem.itemMaster.isCandidated){
					if(prodItem.itemMaster.newDataSw){
						if(prodItem.itemMaster.warehouseLocationItems != null && prodItem.itemMaster.warehouseLocationItems.length > 0){
							var check = true;
							angular.forEach(prodItem.itemMaster.warehouseLocationItems, function(warehouseLocationItem){
								if (warehouseLocationItem.key.warehouseNumber != 101) {
									check = false;
								}
							});
							isEdc = check;
						}
					}
				}else{
					isEdc = prodItem.itemMaster.dsdEdcStatus;
				}
			}
			if(isEdc){
				message = "WHS (DSD eDC)";
			}
			return message;
		};
	}
})();
