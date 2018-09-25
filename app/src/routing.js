'use strict';

/*
 * Decides the application states and which pages implement those states.
 */
(function () {
	angular.module('productMaintenanceUiApp').config(configFunction);

	function configFunction($stateProvider, $urlRouterProvider, $locationProvider, appConstants, KeepaliveProvider, IdleProvider, TitleProvider) {
		IdleProvider.idle(7200); //time is in seconds
		IdleProvider.timeout(300);
		IdleProvider.autoResume('notIdle'); //this line prevents user activity like mouse mvmt from dismissing timeout
		KeepaliveProvider.interval(10);
		TitleProvider.enabled(false);

		$urlRouterProvider.otherwise('/login');

		$stateProvider
			.state(appConstants.LOGIN_STATE, {
				url: '/login',
				templateUrl: 'src/login/login.html',
				data: {
					pageTitle: 'Login'
				}
			})
			.state(appConstants.HOME_STATE, {
				url: '/home',
				templateUrl: 'src/home/home.html',
				data: {
					pageTitle: 'Home'
				}
			})
			.state(appConstants.PRODUCT_DISCONTINUE_STATE, {
				url: '/productDiscontinue',
				templateUrl: 'src/productDiscontinue/productDiscontinue.html',
				data: {
					pageTitle: 'Product Discontinue'
				}
			})
			.state(appConstants.DISCONTINUE_ADMIN_RULES, {
				url: '/discontinueAdminRules',
				templateUrl: 'src/productDiscontinue/discontinueParameters.html',
				data: {
					pageTitle: 'Discontinue Admin Rules'
				}
			})
			.state(appConstants.DISCONTINUE_EXCEPTION_RULES, {
				url: '/discontinueExceptionRules',
				templateUrl: 'src/productDiscontinue/discontinueExceptionParameters.html',
				data: {
					pageTitle: 'Discontinue Exception Rules'
				}
			})
			.state(appConstants.PRODUCTION_SUPPORT_STATE, {
				url: '/productionSupport',
				templateUrl: 'src/productionSupport/productionSupport.html',
				data: {
					pageTitle: 'Production Support'
				}
			})
			.state(appConstants.UPC_SWAP_STATE, {
				url: '/upcSwap',
				templateUrl: 'src/upcSwap/upcSwap.html',
				data: {
					pageTitle: 'UPC Swap'
				}
			})
			.state(appConstants.WAREHOUSE_TO_WAREHOUSE_STATE, {
				url: '/warehouseToWarehouse',
				templateUrl: 'src/upcMaintenance/warehouseToWarehouse.html',
				data: {
					pageTitle: 'Warehouse to Warehouse'
				}
			})
			.state(appConstants.WAREHOUSE_TO_WAREHOUSE_SWAP_STATE, {
				url: '/warehouseToWarehouseSwap',
				templateUrl: 'src/upcMaintenance/warehouseToWarehouseSwap.html',
				data: {
					pageTitle: 'Warehouse to Warehouse Swap'
				}
			})
			.state(appConstants.DSD_TO_BOTH_STATE, {
				url: '/dsdToBoth',
				templateUrl: 'src/upcMaintenance/dsdToBoth.html',
				data: {
					pageTitle: 'DSD to Both'
				}
			})
			.state(appConstants.BOTH_TO_DSD, {
				url: '/bothToDsd',
				templateUrl: 'src/upcMaintenance/bothToDsd.html',
				data: {
					pageTitle: 'Both to DSD'
				}
			})
			.state(appConstants.ADD_ASSOCIATE_STATE, {
				url: '/addAssociate',
				templateUrl: 'src/upcMaintenance/addAssociate.html',
				data: {
					pageTitle: 'Add Associate'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_UPC_MAINTENANCE, {
				url: '/scaleManagementUpcMaintenance',
				templateUrl: 'src/scaleManagement/scaleManagementUpcMaintenance.html',
				data: {
					pageTitle: 'UPC Maintenance'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_INGREDIENTS, {
				url: '/scaleManagementIngredients',
				templateUrl: 'src/scaleManagement/scaleManagementIngredients.html',
				data: {
					pageTitle: 'Ingredients'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_NUTRIENTS, {
				url: '/scaleManagementNutrients',
				templateUrl: 'src/scaleManagement/scaleManagementNutrients.html',
				data: {
					pageTitle: 'Nutrients'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_NUTRIENT_STATEMENT, {
				url: '/scaleManagementNutrientStatement',
				templateUrl: 'src/scaleManagement/scaleManagementNutrientStatement.html',
				data: {
					pageTitle: 'Nutrient Statement'
				},
				params: {ntrStmtCode: null}
			})
			.state(appConstants.SCALE_MANAGEMENT_NLEA16_NUTRIENT_STATEMENT, {
				url: '/scaleManagementNLEA16NutrientStatement',
				templateUrl: 'src/scaleManagement/NLEA16NutrientStatement.html',
				data: {
					pageTitle: 'NLEA 2016 Nutrient Statement'
				},
				params: {nleaNtrStmtCode: null}
			})
			.state(appConstants.SCALE_MANAGEMENT_NLEA16_NUTRIENT_STATEMENT_TEST, {
				url: '/scaleManagementNLEA16NutrientStatementTest',
				templateUrl: 'src/scaleManagement/NLEA16NutrientStatementTest.html',
				data: {
					pageTitle: 'NLEA 2016 Nutrient Statement'
				},
				params: {nleaNtrStmtCode: null}
			})
			.state(appConstants.SCALE_MANAGEMENT_CODE_AUDIT, {
				url: '/scaleManagementCodeAudit',
				templateUrl: 'src/scaleManagement/scaleManagementCodeAudit.html',
				data: {
					pageTitle: 'Code Audit'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_LABEL_FORMATS, {
				url: '/scaleManagementLabelFormats',
				templateUrl: 'src/scaleManagement/labelFormats.html',
				data: {
					pageTitle: 'Label Formats'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_REPORTS, {
				url: '/scaleManagementReports',
				templateUrl: 'src/scaleManagement/reports.html',
				data: {
					pageTitle: 'Reports'
				}
			})
			.state(appConstants.REPORT_INGREDIENTS, {
				url: '/ingredientsReport',
				templateUrl: 'src/reports/ingredients.html',
				data: {
					pageTitle: 'Ingredients Report'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_ACTION_CODES, {
				url: '/actionCodes',
				templateUrl: 'src/scaleManagement/actionCodes.html',
				data: {
					pageTitle: 'Action Codes'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_GRAPHIC_CODES, {
				url: '/scaleManagement/graphicsCode',
				templateUrl: 'src/scaleManagement/graphicsCode.html',
				data: {
					pageTitle: 'Scale Management / Graphic Codes'
				}
			})
			.state(appConstants.NUTRIENT_UOM_CODE, {
				url: '/scaleManagement/nutrientUomCode',
				templateUrl: 'src/scaleManagement/nutrientUomCode.html',
				data: {
					pageTitle: 'Scale Management / Nutrient Uom Codes'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_INGREDIENT_CATEGORY_CODES, {
				url: '/ingredientCategoryCodes',
				templateUrl: 'src/scaleManagement/ingredientCategory.html',
				data: {
					pageTitle: 'Ingredient Category Codes'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_INGREDIENT_STATEMENTS, {
				url: '/ingredientStatements',
				templateUrl: 'src/scaleManagement/ingredientStatements.html',
				data: {
					pageTitle: 'Ingredient Statements'
				},
				params: {ingStmtCode: null}
			})
			.state(appConstants.PRODUCT_DETAILS, {
				url: '/productDetails',
				templateUrl: 'src/productDetails/productDetail.html',
				data: {
					pageTitle: 'Product Detail'
				}
			})
			.state(appConstants.GDSN_VENDOR_SUBSCRIPTIONS, {
				url: '/gdsnVendorSubscriptions',
				templateUrl: 'src/gdsn/vendorSubscription.html',
				data: {
					pageTitle: 'Vendor Subscriptions'
				}
			})
			.state(appConstants.PRODUCT_HIERARCHY_ADMIN, {
				url: '/productHierarchy/admin',
				templateUrl: 'src/productHierarchy/productHierarchyAdmin.html',
				data: {
					pageTitle: 'Product Hierarchy'
				}
			})
			.state(appConstants.PRODUCT_HIERARCHY_DEFAULTS, {
				url: '/productHierarchy/defaults',
				templateUrl: 'src/productHierarchy/productHierarchyDefaults.html',
				data: {
					pageTitle: 'Product Hierarchy'
				}
			})
			.state(appConstants.CUSTOM_HIERARCHY_ADMIN, {
				url: '/customHierarchy',
				templateUrl: 'src/customHierarchy/customHierarchy.html',
				data: {
					pageTitle: 'Custom Hierarchy'
				},
				params: {customerHierarchyId: null}
			})
			.state(appConstants.SOURCE_PRIORITY, {
				url: '/sourcePriority',
				templateUrl: 'src/eCommerce/sourcePriority.html',
				data: {
					pageTitle: 'Source Priority'
				}
			})
			.state(appConstants.ATTRIBUTE_MAINTENANCE, {
				url: '/attributeMaintenance',
				templateUrl: 'src/eCommerce/attributeMaintenance.html',
				data: {
					pageTitle: 'Attribute Maintenance'
				}
			})
			.state(appConstants.UTILITIES_DICTIONARY, {
				url: '/dictionary',
				templateUrl: 'src/utilities/dictionary.html',
				data: {
					pageTitle: 'Dictionary'
				}
			})
			.state(appConstants.UTILITIES_CHECK_DIGIT, {
				url: '/checkDigitCalculator',
				templateUrl: 'src/utilities/checkDigitCalculator/checkDigitCalculator.html',
				data: {
					pageTitle: 'Check Digit Calculator'
				}
			})
			.state(appConstants.BATCH_UPLOAD_BY_CATEGORY_SPECIFIC_ATTRIBUTES, {
				url: '/batchUploadByCategorySpecificAttributes',
				templateUrl: 'src/batchUpload/categorySpecific/batchUploadByCategorySpecificAttributes.html',
				data: {
					pageTitle: 'Category Specific Attributes'
				}
			})
			.state(appConstants.ECOMMERCE_ATTRIBUTE, {
				url: '/batchUpload/ecommerceAttribute',
				templateUrl: 'src/batchUpload/eCommerceAttribute/eCommerceAttribute.html',
				data: {
					pageTitle: 'eCommerce Attributes'
				}
			})
			.state(appConstants.BATCH_UPLOAD_EXCEL_UPLOAD_BY_ASSORTMENT, {
				url: '/batchUpload/assortment',
				templateUrl: 'src/batchUpload/assortment/assortment.html',
				data: {
					pageTitle: 'assortment'
				}
			})
			.state(appConstants.BATCH_UPLOAD_EARLEY, {
				url: '/batchUpload/earley',
				templateUrl: 'src/batchUpload/earley/earleyUpload.html',
				data: {
					pageTitle: 'Earley'
				}
			})
			.state(appConstants.BATCH_UPLOAD_RELATED_PRODUCTS, {
				url: '/batchUpload/relatedProducts',
				templateUrl: 'src/batchUpload/relatedProducts/relatedProducts.html',
				data: {
					pageTitle: 'Related Products'
				}
			})
			.state(appConstants.CODE_TABLE, {
				url: '/code-table',
				templateUrl: 'src/codeTable/codeTableScreen.html',
				data: {
					pageTitle: 'Code Table '
				}
			})
			.state(appConstants.CHECK_STATUS, {
				url: '/utilities/checkstatus',
				templateUrl: 'src/utilities/checkstatus/checkstatus.html',
				data: {
					pageTitle: 'Status Checking'
				},
				params : {trackingId: null}
			})
			.state(appConstants.BATCH_UPLOAD_BY_NUTRITION_AND_INGREDIENTS, {
				url: '/batchUpload/batchUploadByNutritionAndIngredients',
				templateUrl: 'src/batchUpload/nutritionIngredients/batchUploadByNutritionAndIngredients.html',
				data: {
					pageTitle: 'Nutrition and Ingredients'
				},
				params: {trackingId: null}
			})
			.state(appConstants.BATCH_UPLOAD_BY_SERVICE_CASE_SIGNS, {
				url: '/batchUpload/serviceCaseSigns',
				templateUrl: 'src/batchUpload/serviceCaseSigns/batchUploadByServiceCaseSigns.html',
				data: {
					pageTitle: 'Service Case Signs'

				}
			})
			.state(appConstants.BATCH_UPLOAD_BY_PRIMO_PICK, {
				url: '/batchUpload/batchUploadByPrimoPick',
				templateUrl: 'src/batchUpload/primoPick/batchUploadByPrimoPick.html',
				data: {
					pageTitle: 'Primo Pick'
				}
			})
			.state(appConstants.PRODUCT_UPDATES_TASK, {
				url: '/task/productUpdatesTask',
				templateUrl: 'src/task/productUpdatesTask.html',
				data: {
					pageTitle: 'Product Updates Task'
				}
			})
			.state(appConstants.NUTRITION_UPDATES_TASK, {
				url: '/task/nutritionUpdatesTask',
				templateUrl: 'src/task/nutritionUpdatesTask.html',
				data: {
					pageTitle: 'Nutrition Updates Task'
				}
			})
			.state(appConstants.ECOMMERCE_TASK, {
				url: '/task/ecommerceTask/taskInfo/:taskId',
				templateUrl: 'src/task/ecommerceTaskDetail.html',
				data: {
					pageTitle: 'Ecommerce Task'
				}
			})
			.state(appConstants.IMAGE_MANAGEMENT, {
				url: '/imageUpload',
				templateUrl: 'src/imageUpload/imageUpload.html',
				data: {
					pageTitle: 'Image Upload'
				}
			})
			.state(appConstants.PRODUCT_GROUP, {
				url: '/productGroup',
				templateUrl: 'src/productGroup/productGroup.html',
				data: {
					pageTitle: 'Product Group'
				}
			})
			.state(appConstants.CASE_UPC_GENERATOR, {
				url: '/caseUPCGenerator',
				templateUrl: 'src/utilities/caseUPCGenerator/caseUPCGenerator.html',
				data: {
					pageTitle: 'Case UPC Generator'
				}
			})
			.state(appConstants.FIND_MY_ATTRIBUTE, {
				url: '/findMyAttribute',
				templateUrl: 'src/utilities/myAttribute/myAttribute.html',
				data: {
					pageTitle: 'Find My Attribute'
				}
			})
			.state(appConstants.SCALE_MAINTENANCE_LOAD, {
				url: '/scaleMaintenance/load',
				templateUrl: 'src/scaleManagement/scaleMaintenanceLoad.html',
				data: {
					pageTitle: 'Scale Maintenance Load'
				}
			})
			.state(appConstants.SCALE_MAINTENANCE_CHECK_STATUS, {
				url: '/scaleMaintenance/checkStatus',
				templateUrl: 'src/scaleManagement/scaleMaintenanceCheckStatus.html',
				data: {
					pageTitle: 'Scale Maintenance Check Status'
				},
				params: {transactionId: null}
			});
		$locationProvider.html5Mode(false);
	}
})();

/*
 .state(appConstants.JOBS_STATE, {
 url: '/',
 templateUrl: 'src/jobs/jobs.html'
 }).state(appConstants.EXECUTION_STATE, {
 url: '/execution',
 templateUrl: 'src/executions/executions.html',
 params: {
 jobName: null,
 windowId: null
 }
 }).state(appConstants.EXECUTION_DETAIL_STATE, {
 url: '/executionDetails',
 templateUrl: 'src/executionDetails/executionDetails.html',
 params: {
 jobId: null,
 parentWindowId: null
 }
 })
 */
