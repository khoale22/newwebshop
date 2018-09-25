/*
 *   layoutCodeTableComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Product Detail page's main or top most parent component.
 *
 * @author vn40486
 * @since 2.3.0
 */
(function() {
	var myApp =angular.module('productMaintenanceUiApp');

	myApp.component('codeTableLayoutComponent', {
		// isolated scope binding
		bindings: {
			selectedItem: '='
		},
		// Inline template which is bound to message variable in the component controller
		templateUrl: 'src/codeTable/layoutCodeTable.html',
		// The controller that handles our component logic
		controller: CodeTableCntrl

	});


    CodeTableCntrl.$inject = ['$rootScope', '$scope', 'ProductGroupService', 'customHierarchyService'];
	/**
	 * control code table screen
	 */
	function CodeTableCntrl($rootScope,$scope, productGroupService, customHierarchyService) {
		var self = this;
		self.loadingData = false;
		self.error = '';
		self.success = '';
		self.itemSending = {};
		self.arraySending= [];
		self.messageSending= '';
		self.VALIDATE_COUNTRY_CODE = 'validateCountryCode';
		self.VALIDATE_PRODUCT_CATEGORY = 'validateProductCategory';
		self.VALIDATE_STATE_WARNING = 'validateStateWarning';
		self.VALIDATE_CHOICE_TYPE = 'validateChoiceType';
		self.VALIDATE_WINE_SCORING_ORGANIZATION = "validateWineScoringOrganization";
		self.VALIDATE_VARIETAL_TYPE = 'validateVarietalType';
		self.VALIDATE_VARIETAL = 'validateVarietal';
		self.VALIDATE_WINE_AREA = 'validateWineArea';
		self.VALIDATE_WINE_REGION = 'validateWineRegion';
		self.VALIDATE_PRODUCT_GROUP_TYPE = 'validateProductGroupType';
		self.VALIDATE_WINE_MAKER = 'validateWineMaker';
		self.VALIDATE_CHOICE_OPTION = 'validateChoiceOption';
		self.VALIDATE_FACTORY = 'validateFactory';
		self.VALIDATE_TOBACCO_PRODUCT_TYPE_CODE = 'validateTobaccoProductTypeCode';
		self.RETURN_TAB = 'returnTab';
		self.returnTab = null;
		this.$onInit = function () {
			if(productGroupService.getNavFromProdGrpTypePageFlag() || customHierarchyService.getNavigateFromProdGroupTypePage()){
				self.handleTabChange('product-group-type');
			}
		};

		/** Left Tab Variables */
		self.selectedTab  = self.DEFAULT_BRAND_COST_OWNER_T2T_TAB;
		self.DEFAULT_BRAND_COST_OWNER_T2T_TAB = {id:'brandCostOwnerTop2Top', title:'Brand - Cost Owner - Top 2 Top'};
		self.DEFAULT_WINE_SCORING_ORGANIZATION_TAB = {id:'wineScoringOrganizationComponent', title:'Wine - Scoring Organization'};
		self.WINE_VARIETAL_TAB = {id:'winevarietal', title:'Wine - Varietal'};
		self.WINE_VARIETAL_TYPE_TAB = {id:'winevarietalType', title:'Wine - Varietal Type'};
		self.WINE_AREA_TAB = {id:'wineArea', title:'Wine - Area'};
		self.WINE_REGION_TAB = {id:'wineRegion', title:'Wine - Region'};
		self.WINE_MAKER_TAB = {id:'wineMaker', title:'Wine - Maker'};
		self.FACTORY_TAB = {id:'factory', title:'Factory'};
		self.CHOICE_TYPE_TAB = {id:'choiceType', title:'Choice Type'};
		self.WIC_LEB_TAB = {id:'wicLebComponent', title:'WIC / LEB'};
		self.CHOICE_OPTION_TAB = {id:'choice', title:'Choice'};
		self.COUNTRY_CODE_TAB = {id:'countryCode', title:'Country Code'};
		self.PRODUCT_BRAND_TAB = {id:'productBrand', title:'Product Brand'};
		self.PRODUCT_SUB_BRAND_TAB = {id:'productSubBrand', title:'Sub-Brand'};
		self.STATE_WARNING_TAB = {id:'stateWarnings', title:'State Warnings'};
		self.PRODUCT_GROUP_TYPE_TAB = {id:'productGroupType', title:'Product Group Type'};
		self.DEFAULT_PRODUCT_CATEGORY_TAB = {id:'productCategory', title:'Product Category'};
		self.PRODUCT_CATEGORY_TAB = {id:'productCategory', title:'Product Category'};
		self.TOBACCO_PRODUCT_TYPE_CODE_TAB = {id:'tobaccoProductTypeCode',title:'Tobacco Product Type Code'};

		/**
		 * Handles tab change click action.
		 * @param msg
		 * @param $event
		 */
		self.handleTabChange = function(tab) {
			self.returnTab = tab;
			if(self.selectedTab !== null && self.selectedTab !== undefined &&
				self.selectedTab.id === self.COUNTRY_CODE_TAB.id){
				if(tab !== self.selectedTab.id){
					$rootScope.$broadcast(self.VALIDATE_COUNTRY_CODE, {
						tab: tab
					});
				}
			} else if(self.selectedTab !== null && self.selectedTab !== undefined &&
				self.selectedTab.id === self.PRODUCT_CATEGORY_TAB.id){
				if(tab !== self.selectedTab.id){
					$rootScope.$broadcast(self.VALIDATE_PRODUCT_CATEGORY, {
						tab: tab
					});
				}

			} else if(self.selectedTab !== null && self.selectedTab !== undefined &&
				self.selectedTab.id === self.STATE_WARNING_TAB.id){
				if(tab !== self.selectedTab.id){
					$rootScope.$broadcast(self.VALIDATE_STATE_WARNING, {
						tab: tab
					});
				}
			} else if(self.selectedTab !== null && self.selectedTab !== undefined &&
				self.selectedTab.id === self.CHOICE_TYPE_TAB.id){
				if(tab !== self.selectedTab.id){
					$rootScope.$broadcast(self.VALIDATE_CHOICE_TYPE, {
						tab: tab
					});
				}
			} else if(self.selectedTab !== null && self.selectedTab !== undefined &&
				self.selectedTab.id === self.DEFAULT_WINE_SCORING_ORGANIZATION_TAB.id){
				if(tab !== self.selectedTab.id){
					$rootScope.$broadcast(self.VALIDATE_WINE_SCORING_ORGANIZATION, {
						tab: tab
					});
				}
			} else if(self.selectedTab !== null && self.selectedTab !== undefined &&
				self.selectedTab.id === self.WINE_VARIETAL_TYPE_TAB.id){
				if(tab !== self.selectedTab.id){
					$rootScope.$broadcast(self.VALIDATE_VARIETAL_TYPE, {
						tab: tab
					});
				}
			} else if(self.selectedTab !== null && self.selectedTab !== undefined &&
				self.selectedTab.id === self.WINE_VARIETAL_TAB.id){
				if(tab !== self.selectedTab.id){
					$rootScope.$broadcast(self.VALIDATE_VARIETAL, {
						tab: tab
					});
				}
			} else if(self.selectedTab !== null && self.selectedTab !== undefined &&
				self.selectedTab.id === self.WINE_AREA_TAB.id){
				if(tab !== self.selectedTab.id){
					$rootScope.$broadcast(self.VALIDATE_WINE_AREA, {
						tab: tab
					});
				}
			} else if(self.selectedTab !== null && self.selectedTab !== undefined &&
				self.selectedTab.id === self.WINE_REGION_TAB.id){
				if(tab !== self.selectedTab.id){
					$rootScope.$broadcast(self.VALIDATE_WINE_REGION, {
						tab: tab
					});
				}
			} else if(self.selectedTab !== null && self.selectedTab !== undefined &&
				self.selectedTab.id === self.PRODUCT_GROUP_TYPE_TAB.id){
				if(tab !== self.selectedTab.id){
					$rootScope.$broadcast(self.VALIDATE_PRODUCT_GROUP_TYPE, {
						tab: tab
					});
				}
			} else if(self.selectedTab !== null && self.selectedTab !== undefined &&
				self.selectedTab.id === self.WINE_MAKER_TAB.id){
				if(tab !== self.selectedTab.id){
					$rootScope.$broadcast(self.VALIDATE_WINE_MAKER, {
						tab: tab
					});
				}
			} else if(self.selectedTab !== null && self.selectedTab !== undefined &&
				self.selectedTab.id === self.CHOICE_OPTION_TAB.id){
				if(tab !== self.selectedTab.id){
					$rootScope.$broadcast(self.VALIDATE_CHOICE_OPTION, {
						tab: tab
					});
				}
			} else if(self.selectedTab !== null && self.selectedTab !== undefined &&
				self.selectedTab.id === self.FACTORY_TAB.id){
				if(tab !== self.selectedTab.id){
					$rootScope.$broadcast(self.VALIDATE_FACTORY, {
						tab: tab
					});
				}
			} else if(self.selectedTab !== null && self.selectedTab !== undefined &&
				self.selectedTab.id === self.TOBACCO_PRODUCT_TYPE_CODE_TAB.id){
				if(tab !== self.selectedTab.id){
					$rootScope.$broadcast(self.VALIDATE_TOBACCO_PRODUCT_TYPE_CODE, {
						tab: tab
					});
				}
			}else {
				self.setSelectedTab(tab);
			}
		};
		/**
		 * Sets selected tab.
		 *
		 * @param tab
		 */
		self.setSelectedTab = function(tab){
			if (tab === 'brand-cost-t2t') {
				self.selectedTab = self.DEFAULT_BRAND_COST_OWNER_T2T_TAB;
			} else if (tab === 'wine-scroring-org') {
				self.selectedTab = self.DEFAULT_WINE_SCORING_ORGANIZATION_TAB;
			} else if (tab === 'wine-varietal') {
				self.selectedTab = self.WINE_VARIETAL_TAB;
			} else if (tab === 'brand-cost-t2t') {
				self.selectedTab = self.BRAND_COST_OWNER_T2T_TAB;
			} else if (tab === 'wine-varietal-type') {
				self.selectedTab = self.WINE_VARIETAL_TYPE_TAB;
			} else if (tab === 'wine-area') {
				self.selectedTab = self.WINE_AREA_TAB;
			} else if (tab === 'wine-region') {
				self.selectedTab = self.WINE_REGION_TAB;
			} else if (tab === 'wine-maker') {
				self.selectedTab = self.WINE_MAKER_TAB;
			} else if (tab === 'factory') {
				self.selectedTab = self.FACTORY_TAB;
			} else if (tab === 'choice-type') {
				self.selectedTab = self.CHOICE_TYPE_TAB;
			} else if (tab === 'wic-leb') {
				self.selectedTab = self.WIC_LEB_TAB;
			} else if (tab === 'choice') {
				self.selectedTab = self.CHOICE_OPTION_TAB;
			} else if (tab === 'countryCode') {
				self.selectedTab = self.COUNTRY_CODE_TAB;
			} else if (tab === 'product-brand') {
				self.selectedTab = self.PRODUCT_BRAND_TAB;
			} else if (tab === 'sub-brand') {
				self.selectedTab = self.PRODUCT_SUB_BRAND_TAB;
			} else if (tab === 'state-warnings') {
				self.selectedTab = self.STATE_WARNING_TAB;
			} else if (tab === 'product-group-type') {
				self.selectedTab = self.PRODUCT_GROUP_TYPE_TAB;
			} else if (tab === 'productCategory') {
				self.selectedTab = self.DEFAULT_PRODUCT_CATEGORY_TAB;
			} else if (tab==='tobacco-product-group-type-code'){
				self.selectedTab = self.TOBACCO_PRODUCT_TYPE_CODE_TAB;
			}
		}
		/**
		 * Sending data from tab to tab.
		 * @param dataSending
		 * @param itemSending
		 */
		self.sendingDataToAnotherTab = function (dataSending, itemSending) {
			self.arraySending =  angular.copy(dataSending);
			self.itemSending = angular.copy(itemSending);
		};

		/**
		 * Sending message from tab to tab
		 * @param message
		 */
		self.sendingMessage = function (message) {
			self.messageSending = message;
		}
		/**
		 * Listener return to tab from child component.
		 */
		$scope.$on(self.RETURN_TAB, function() {
			self.setSelectedTab(self.returnTab);
		});
	}
}());
