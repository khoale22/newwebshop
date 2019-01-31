/*
 *   addNewVariantsComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';


/**
 * variants -> variants Info page component.
 *
 * @author vn87351
 * @since 2.15.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('addNewVariants', {
		// isolated scope binding
		require: {
			productDetail: '^productDetail'
		},
		bindings: {
			listOfProducts: '=',
			productMaster: '<',
			itemMaster: '<',
			variantData:'<'

		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/product/variants/addNew.html',
		// The controller that handles our component logic
		controller: AddNewVariantsController
	});

	AddNewVariantsController.$inject = ['productApi', '$scope', '$timeout', 'ProductFactory','variantsApi','ngTableParams','$rootScope'];


	function AddNewVariantsController(productApi, $scope, $timeout,  productFactory,variantsApi,ngTableParams,$rootScope) {

		var self = this;
		/**
		 * Whether or not the controller is waiting for data
		 * @type {boolean}
		 */
		self.isWaiting = false;

        /**
         * Represents state of Select All check box.
         */
        self.checkAllCheckBox = false;
		/**
		 * message susscess from api
		 */
		self.success = '';
		/**
		 * message error form api
		 */
		self.error = '';
		self.itemAddTemp = {
			upc: '',
			customerFriendlyDescription1: {},
			customerFriendlyDescription2: {},
			productDescription: ''
		}
		self.addVariantsData = [];

		self.stepAddNewSelected = 1;
		self.numberVariantsAdd = 0;

		self.CUSTOMER_FRIENDLY_DESCRIPTION_1='TAG1';
		self.CUSTOMER_FRIENDLY_DESCRIPTION_2='TAG2';
		self.LANGUAGE_TYPE_ENGLISH = "ENG";
		self.PRODUCT_TYPE_CODE_VARIANT='VARNT';

		self.loadAddNewTable = function(){
			$scope.addVariantsTableParams = new ngTableParams({
				page: 1,
				count: 20
			}, {
				counts: [],
				data: self.addVariantsData//self.initDataForNgTable()
			});
			$scope.addVariantsTableParams.reload();
		}
		self.addNewVariantStepNext = function(){
			self.error= '';
			if(self.stepAddNewSelected==1){
				if(self.numberVariantsAdd===undefined || parseInt(self.numberVariantsAdd) < 0 || parseInt(self.numberVariantsAdd) > 999){
					self.error = "The number of Variants must be greater than 0 and less than or equal to 999.";
				}
				else if(self.isSameAssortment===undefined || self.isSameAssortment===''){
					self.error = "Please select whether the variants within the assortment share the same UPC or not.";
				}
				else if( self.isSameAssortment!=''){
					if(self.numberVariantsAdd==0){
						self.numberVariantsAdd=5;
					}
					self.createCandidateWorkRequest();
				}
			}else if(!self.validateTab2()){
				self.stepAddNewSelected=3;
			}

		}
		self.validateTab2 = function(){
			var invalid = false;
			self.error= '';
			angular.forEach(self.addVariantsData, function(value,key){
				if(self.error===''){
					if(value.upc === undefined || value.upc==null || value.upc ===''){
						self.error = 'Variant UPC is a mandatory field';
						invalid = true;
					}else if(value.productDescription== undefined || value.productDescription===''){
						self.error = 'Product Description is a mandatory field';
						invalid = true;
					}
				}
			});
			return invalid;
		}
		self.previous = function(){
			self.error= '';
			self.stepAddNewSelected=1;
			$('#btnstep'+self.stepAddNewSelected).tab('show');
		}


		self.generateCandidateProductMaster = function(){
			angular.forEach(self.addVariantsData, function(value,key){
				var psProductMaster = {};
				psProductMaster.productId = self.productMaster.prodId;
				psProductMaster.candidateDescriptions = [];
				if(value.customerFriendlyDescription1!==undefined && value.customerFriendlyDescription1!=null
					&& value.customerFriendlyDescription1.description!==undefined && value.customerFriendlyDescription1.description!=='')
					psProductMaster.candidateDescriptions.push(value.customerFriendlyDescription1);
				if(value.customerFriendlyDescription2!==undefined && value.customerFriendlyDescription2!=null
					&& value.customerFriendlyDescription2.description!==undefined && value.customerFriendlyDescription2.description!=='')
					psProductMaster.candidateDescriptions.push(value.customerFriendlyDescription2);
				psProductMaster.description = value.productDescription;
				psProductMaster.productTypeCode=self.PRODUCT_TYPE_CODE_VARIANT;
				psProductMaster.productScanCodeVariant = parseInt(value.upc.upc);
				self.candidateWorkRequest.candidateProductMaster.push(psProductMaster);
			});
			self.isWaiting = true;
			variantsApi.updateCandidateWorkRequestVariants(self.candidateWorkRequest,function(response){
					self.isWaiting = false;
					self.closeAddNewForm('Variant product successfully added.');
				},
				self.callApiErrorAddForm
			);
		}
		self.createCandidateWorkRequest = function(){
			self.isWaiting = true;
			variantsApi.createCandidateWorkRequestVariants({
					productId:self.productMaster.prodId
				},function(response){
					self.isWaiting = false;
					self.candidateWorkRequest = response;
					self.addNewVariantStep1();
					self.stepAddNewSelected=2;
					$('#btnstep'+self.stepAddNewSelected).tab('show');
				},
				self.callApiErrorAddForm
			);
		}
		/**
		 * call api error and throw message
		 * @param error
		 */
		self.callApiErrorAddForm = function(error){
			self.isWaiting = false;
			if (error && error.data) {
				if (error.data.message) {
					self.error = error.data.message;
				} else {
					self.error = error.data.error;
				}
			}
			else {
				self.error = "An unknown error occurred.";
			}
		};
		/**
		 * add new variant
		 */
		self.addNewVariantStep1 = function () {
			self.addVariantsData = [];
			for(var i = 0 ; i<self.numberVariantsAdd;i++){
				var psProduct = angular.copy(self.itemAddTemp);
				psProduct.productDescription = self.productMaster.description;
				angular.forEach(self.productMaster.productDescriptions, function (value, key) {
					if (value.key.descriptionType === self.CUSTOMER_FRIENDLY_DESCRIPTION_1) {
						psProduct.customerFriendlyDescription1 = angular.copy(value);
					}
					var cdf2= {
						key: {
							descriptionType:self.CUSTOMER_FRIENDLY_DESCRIPTION_2,
							languageType:self.LANGUAGE_TYPE_ENGLISH
						},
						description:''
					};
					psProduct.customerFriendlyDescription2 = angular.copy(cdf2);

				});
				if (self.isSameAssortment === 'Y') {
					psProduct.upc = self.itemMaster.primaryUpc;
				}
				self.addVariantsData.push(psProduct);
			}
			self.loadAddNewTable();
		}
		/**
		 * close the new product popup
		 */
		self.closeAddNewForm = function(mess){
			$rootScope.$broadcast('closeAddNewPopup',mess);
		}
		/**
		 * delete item on add new
		 */
		self.deleteItem = function(){
			var tmpData = [];
			angular.forEach(self.addVariantsData, function(value,key){
				if(value.selected===undefined || !value.selected){
					tmpData.push(value);
				}
			});
			self.addVariantsData = tmpData;
			self.loadAddNewTable();
		}
		/**
		 * add new item
		 */
		self.addNewItem = function(){
			var psProduct = angular.copy(self.itemAddTemp);
			psProduct.productDescription = self.productMaster.description;
			angular.forEach(self.productMaster.productDescriptions, function (value, key) {
				if (value.key.descriptionType === self.CUSTOMER_FRIENDLY_DESCRIPTION_1) {
					psProduct.customerFriendlyDescription1 = angular.copy(value);
				}
				var cdf2= {
					key: {
						descriptionType:self.CUSTOMER_FRIENDLY_DESCRIPTION_2,
						languageType:self.LANGUAGE_TYPE_ENGLISH
					},
					description:''
				};
				psProduct.customerFriendlyDescription2 = angular.copy(cdf2);

			});
			if (self.isSameAssortment === 'Y') {
				psProduct.upc = self.itemMaster.primaryUpc;
			}
			self.addVariantsData.push(psProduct);
			self.loadAddNewTable();
		}
	}

})();
