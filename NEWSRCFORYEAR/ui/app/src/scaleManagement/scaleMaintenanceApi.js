/*
 *
 * scaleMaintenanceApi.js
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

'use strict';

/**
 * Constructs the API to call the backend for scale maintenance api.
 */
(function () {

	angular.module('productMaintenanceUiApp').factory('ScaleMaintenanceApi', scaleMaintenanceApi);

	scaleMaintenanceApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function scaleMaintenanceApi(urlBase, $resource) {
		urlBase = urlBase + '/pm/scaleMaintenance';
		return $resource(urlBase, null, {
			// Submit load to store(s) for plu(s).
			'submitLoadToStores' : {
				method: 'POST',
				url: urlBase + '/load',
				isArray:false
			},
			// Get available stores to send load to.
			'getAvailableLoadStores' : {
				method: 'GET',
				url: urlBase + '/load/availableStores',
				isArray:true
			},
			// Get selected scale transaction.
			'checkStatusFindOneTransaction' : {
				method: 'GET',
				url: urlBase + '/checkStatus/:transactionId',
				isArray:false
			},
			// Get all scale transactions.
			'checkStatusFindAllTransactions' : {
				method: 'GET',
				url: urlBase + '/checkStatus/findAllTransactions',
				isArray:false
			},
			// Get all scale transmits.
			'checkStatusFindAllTransmits' : {
				method: 'GET',
				url: urlBase + '/checkStatus/findAllTransmits',
				isArray:false
			},
			// Get all scale retail information.
			'checkStatusFindAllRetails' : {
				method: 'GET',
				url: urlBase + '/checkStatus/findAllRetails',
				isArray:false
			},
			// Get all scale retail information.
			'findAllScaleMaintenanceByStoreAndTransactionId' : {
				method: 'GET',
				url: urlBase + '/checkStatus/findAllScaleMaintenanceByStoreAndTransactionId',
				isArray:false
			},
			// Get serving size description by nutrient statement.
			'findServingSizeDescriptionByNutrientStatement' : {
				method: 'POST',
				url: urlBase + '/checkStatus/findServingSizeDescriptionByNutrientStatement',
				isArray:false
			}
		});
	}
})();
