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
 * Selling Unit -> Image Info component.
 *
 * @author s753601
 * @since 2.13.0
 */
(function () {

		angular.module('productMaintenanceUiApp').component('imageInfo', {
			// isolated scope binding
			bindings: {
				sellingUnit: '<',
				productMaster: '<'
			},
			// Inline template which is binded to message variable in the component controller
			templateUrl: 'src/productDetails/productSellingUnits/imageInfo.html',
			// The controller that handles our component logic
			controller: ImageInfoController
		});

		ImageInfoController.$inject = ['productSellingUnitsApi', 'customHierarchyService', '$state', '$rootScope',
			'appConstants', 'imageInfoService', 'PermissionsService', 'ProductSearchService', 'DownloadImageService'];

		/**
		 * UPC Info component's controller definition.
		 * @constructor
		 * @param productSellingUnitsApi
		 * @param customHierarchyService
		 * @param $state
		 * @param $rootScope
		 * @param appConstants
		 * @param imageInfoService
		 * @param permissionsService
		 * @param productSearchService
		 */
		function ImageInfoController(productSellingUnitsApi, customHierarchyService, $state, $rootScope, appConstants,
									 imageInfoService, permissionsService, productSearchService, downloadImageService) {

			var self = this;

			/**
			 * When attempting to make an invalid upload request these messages will tell you what the cause of the error is
			 * @type {string}
			 */
			var INVALID_FILE_UPLOAD = "Attempting to upload an invalid file type";
			var MISSING_OR_INVALID_FILE = "Missing or Invalid file type cannot upload";
			var MISSING_IMAGE_SOURCE = "Missing Image Source";
			var MISSING_IMAGE_CATEGORY = "Missing Image Category";
			var IMAGE_UPLOAD_FAIL_TO_LAUNCH = "Failed to upload Image because of the following error(s)\n ";
			var MISSING_DESTINATION_FOR_UPLOAD="Missing Destination Domain";
			var MISSING_DESTINATION_DOMAIN="Destination Domain is mandatory";
			var APPROVED_STATUS_CODE="A";
			var STATUS_FOR_REVIEW = 'S';
			var STATUS_REJECTED = 'R';
			var PRIMARY_PRIORITY = "P";
			var PRIMARY_ALTERNATE = 'A';
			var PRIMARY_NOT_APPLICABLE = 'NA';
			var CATEGORY_SWATCHES = 'SWAT';
			var MISSING_PRIMARY_STRING="Alternate Images cannot be \" Active Online\" without a Primary Image that is \"Active\".";

			/**
			 * Validation messages
			 */
			const ONLY_ONE_PRIMARY_FOR_PRODUCT = "Only image can be Primary for a product.";
			const IMG_ACTIVE_ONLINE_IN_ALTERNATE
				= 'Alternate Images cannot be "Active Online" without a Primary Image that is "Active Online".';
			const IMG_STATUS_CHANGE_TO_REVIEW
				= 'An image in "Approved" or  "Rejected" status cannot be moved to "For Review" Status.';
			const IMG_ACTIVE_ONLINE_IN_CATEGORY_SWATCH
				= 'Swatch images cannot be "Active Online" without a Primary image that is "Active Online".';
			const IMG_PRIMARY_REJECTED = "An image in 'Rejected' status cannot be set as 'Primary'";
			const IMG_PRIMARY_FOR_REVIEW_ACTONLINE = 'An image in "For review" status can not be made "Active Online".';
			const IMG_STATUS_REJECTED = 'An image in "Rejected" status cannot be made "Active Online".';
			const IMG_ACTIVE_ONLINE_IS_NONE_CATEGORY_OR_PRIMARY_ALTERNATE
				= 'An image cannot be "Active Online" without the "Image Category" or the "Primary/Alternate" defined.';
			const MESSAGE_NO_DATA_CHANGE
				= "There are no changes on this page to be saved. Please make any changes to update.";
			/**
			 * Boolean for if images are present
			 * @type {boolean}
			 */
			self.imageInfo = false;

			/**
			 * List of Images before they are sorted
			 * @type {Array}
			 */
			self.allImages = [];

			/**
			 * List of Approved Images
			 * @type {Array}
			 */
			self.approvedImageInfo = [];

			/**
			 * List of Pending and Rejected Images
			 * @type {Array}
			 */
			self.pendingImageInfo = [];

			/**
			 * List of URIs to be sent to webservice for image retrieval
			 * @type {Array}
			 */
			self.imageURIs = [];

			/**
			 * Flag for indicating that the front end is still waiting on data from the backend
			 */
			self.isWaitingForResponse = false;

			/**
			 * Success message to be displayed
			 */
			self.success = null;

			/**
			 * Error message to be displayed
			 */
			self.error = null;
			/**
			 * Whenever the user attempts a change that is not valid this message will be displayed
			 * @type {null}
			 */
			self.invalidChange=null;

			/**
			 * List of categories for the dropdown
			 */
			self.categories = [];

			/**
			 * List of statuses for the dropdown
			 */
			self.statuses = [];

			/**
			 * List of destinations for the dropdown
			 \         */
			self.destinations = [];

			/**
			 * List of priorities for the dropdown
			 */
			self.priority = [];
			/**
			 * List of iamge sources for a dropdown
			 * @type {{}}
			 */
			self.sources = {};

			/**
			 * Flag for displaying rejected images.
			 */
			self.showRejected = false;

			/**
			 * String for the show rejected button
			 */
			self.showRejectedDisplay = "Show Rejected";

			/**
			 * Image category for new image candidate
			 * @type {string}
			 */
			self.uploadImageCategory = "";
			/**
			 * Arrays of original data used for comparison and for data reset
			 * @type {Array}
			 */
			self.originalImages = [];
			self.originalActive = [];
			self.originalInactive = [];

			/**
			 * These flags are used to tell the component what part of the updated it is still waiting on
			 * @type {boolean}
			 */
			self.waitingForAddingDestintions = false;
			self.waitingForRemovingDestinations = false;
			self.waitingForDataUpdates = false;
			/**
			 * A list of changes that need to be made to the upc's image info.
			 * @type {Array}
			 */
			self.destinationUpdates = [];


			/**
			 * Image source for new image candidate
			 * @type {string}
			 */
			self.uploadImageSource = "";

			/**
			 * List of destinations for new image candidate
			 * @type {{}}
			 */
			self.uploadImageDestinations = {};

			/**
			 * Byte[] for new image candidate
			 * @type {string}
			 */
			self.uploadImageImage = "";

			/**
			 * Current image loading.
			 * @type {string}
			 */
			self.imageBytes = '';

			/**
			 * Current image format.
			 * @type {string}
			 */
			self.imageFormat = '';

			/**
			 * This flag states if the current file is of a valid file type
			 * @type {boolean}
			 */
			self.invalidFileType = false;

			/**
			 * List of valid file types
			 * @type {[string,string,string,string,string]}
			 */
			self.validFileTypes = ["jpg", "jpeg", "png"];

			/**
			 * This object handles all of the image editing functionality
			 * @type {null}
			 */
			self.cropper = null;
			/**
			 * This variable keeps track of the number of degrees the image has been spinned
			 * @type {number}
			 */
			self.currentAngle = 0;
			/**
			 * This variable keeps track how if the x axis has been flipped
			 * @type {number}
			 */
			self.scaleX = 1;
			/**
			 * This variable keeps track if the y axis has been flipped
			 * @type {number}
			 */
			self.scaleY = 1;
			/**
			 * This variable holds the data for the new cropped image
			 * @type {null}
			 */
			self.editedImage = null;
			/**
			 * This variable is used to send the new cropped image to the backend.
			 * @type {null}
			 */
			self.imageToEditData = null;

			/**
			 * Flag stating if the image information is missing a destination domain
			 * @type {boolean}
			 */
			self.missingDestination=false;

			/**
			 * A list of selected images to possibly be copied to an hierarchy
			 * @type {Array}
			 */
			self.selectedImages = [];
			/**
			 * Flag for if all the images are selected
			 * @type {boolean}
			 */
			self.selectAllImages = false;
			/**
			 * A flag to state if either a single or all entities in the default path need to be copied
			 * @type {null}
			 */
			self.hierarchyLevels=null;
			/**
			 * The default hierarchy path for a product
			 * @type {Array}
			 */
			self.hierarchyPath=[];
			/**
			 * The string used to display the default path of a product
			 * @type {null}
			 */
			self.hierarchyString=null;
			/**
			 * The list of upcs attached to a product and used to get all of the images attached to the product
			 * @type {Array}
			 */
			self.upcs=[];
			/**
			 * Flag stating if there is a default hierarchy path available.
			 * @type {boolean}
			 */
			self.isDisableHierarchy=false;
			/**
			 * This flag is set to true when the images status is in an illegal state
			 * @type {boolean}
			 */
			self.invalidStatus=false;
			/**
			 * This string is set when the images status is in an illegal state
			 * @type {string}
			 */
			self.invalidStatusString="";

			self.isFisrtLoad = true;
			/**
			 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
			 * (or re-initialized).
			 */
			this.$onInit = function () {
				self.disableReturnToList = productSearchService.getDisableReturnToList();
				self.allImages = [];
				self.imageInfo = false;
				productSellingUnitsApi.getImageCategories(self.loadCategoryData, self.fetchError);
				productSellingUnitsApi.getImageStatuses(self.loadStatusData, self.fetchError);
				productSellingUnitsApi.getImageDestinations(self.loadDestinationData, self.fetchError);
				productSellingUnitsApi.getImagePriorities(self.loadPriorityData, self.fetchError);
				productSellingUnitsApi.getImageSources(self.loadSourceData, self.fetchError);
				self.cropper = window.Cropper;
				var image = document.getElementById('imageToEdit');
				self.cropper = new Cropper(image, {
					minContainerWidth: 1000,
					minContainerHeight: 750
				});
			};

			/**
			 * Component will reload the vendor data whenever the item is changed in casepack.
			 */
			this.$onChanges = function () {
				self.error = null;
				self.success = null;
				self.invalidChange = null;
				if(self.productMaster !== null && self.productMaster !== undefined){
					self.isWaitingForResponse = true;
					self.allImages = [];
					self.imageInfo = false;
					self.upcs = [];
					angular.forEach(self.productMaster.sellingUnits, function(sellingUnit){
						self.upcs.push(sellingUnit.upc);
					});
					productSellingUnitsApi.getImageInformation(self.upcs, self.loadData, self.fetchError);
				}
			};

			/**
			 * Sets up category code table after getting it from the backend
			 * @param results
			 */
			self.loadSourceData = function (results) {
				self.sources = results;
			};

			/**
			 * Sets up category code table after getting it from the backend
			 * @param results
			 */
			self.loadCategoryData = function (results) {
				self.categories = results;
			};

			/**
			 * Sets up status code table after getting it from the backend
			 * @param results
			 */
			self.loadStatusData = function (results) {
				self.statuses = results;
			};

			/**
			 * Sets up destination code table after getting it from the backend
			 * @param results
			 */
			self.loadDestinationData = function (results) {
				self.destinations = results;
				self.uploadImageDestinations=results;
			};

			/**
			 * Sets up priority code table after getting it from the backend
			 * @param results
			 */
			self.loadPriorityData = function (results) {
				self.priority = results;
			};

			/**
			 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
			 */
			this.$onDestroy = function () {
			};

			/**
			 * After updating the database get the latest ItemNaster
			 * @param results
			 */
			self.loadData = function (results) {
				if(results.message !== null){
					self.success = results.message;
				}
				self.originalImages = [];
				self.allImages = [];
				angular.forEach(results.data,function (data) {
					if(data.key.id === self.sellingUnit.upc || data.activeOnline == true){
						data.altTag = self.trimData(data.altTag);
						self.allImages.push(data);
					}
				})
				self.imageURIs = [];
				if (results.data.length > 0) {
					self.imageInfo = true;
				}
				var index=0;angular.forEach(self.allImages, function (image) {
					image.selected = false;
					image.useImageData = false;
					image.imagePriorityCode = self.trimData(image.imagePriorityCode);
					image.imageStatusCode = self.trimData(image.imageStatusCode);
					index++;
				});
				self.originalImages = angular.copy(self.allImages);
				self.isWaitingForResponse=false;
				self.loadImage();
			};

			self.loadImage = function(){
				angular.forEach(self.allImages,function (image) {
					if((image.image === null || self.isImageUndefined(image)) && (self.trimData(image.imageStatusCode)!=='R' || self.showRejected)){
						self.getSingleImage(image, self.allImages.indexOf(image));
					}
				})
			};

			/**
			 * This method makes a call to get all of the images for the image info individually
			 * @param image
			 */
			self.getSingleImage=function (image, index) {
				productSellingUnitsApi.getImages(image.imageURI,
					function (results) {
						var imageData = "";
						var useImageData = false;
						if(image.imageFormat.indexOf("TIF") != -1){
							image.useImageData = false;
						} else if (results.data.length !== 0) {
							imageData = "data:image/jpg;base64," + results.data;
							useImageData = true;
						} else {
							useImageData = false;
						}
						image.imagePriorityCode = self.trimData(image.imagePriorityCode);
						image.imageStatusCode = self.trimData(image.imageStatusCode);
						image.image = imageData;
						image.useImageData = useImageData;
						self.originalImages[index]=angular.copy(self.allImages[index]);
					},
					self.fetchError);
			};

			/**
			 * Fetches the error from the back end.
			 * @param error
			 */
			self.fetchError = function (error) {
				self.isWaitingForResponse = false;
				if (error && error.data) {
					self.isWaitingForResponse = false;
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
			 * @param error The error message.
			 */
			self.setError = function (error) {
				self.error = error;
			};

			/**
			 * Sends the user to the custom hierarchy for that image
			 * @param entityId
			 */
			self.navigateToCustomHierarchy = function (entityId) {
				imageInfoService.setEntityId(entityId);
				customHierarchyService.setEntityId(entityId);
				$state.go(appConstants.CUSTOM_HIERARCHY_ADMIN);
			};

			/**
			 * This method flips the show rejected switch
			 */
			self.updateShowRejected = function () {
				self.showRejected = !self.showRejected;
				if (self.showRejected) {
					self.showRejectedDisplay = "Hide Rejected";
				} else {
					self.showRejectedDisplay = "Show Rejected";
				}
				if (self.isFisrtLoad){
					self.loadImage();
					self.isFirstLoad = false;
				}
			};

			/**
			 * This method converts the list of destinations into a more readable form
			 * @param destinations the list of destinations
			 * @returns {string} a human readable list of destinations
			 */
			self.getDestinations = function (destinations) {
				var string = "";
				if (destinations.length > 0) {
					string = destinations[0].description;
					for (var index = 1; index < destinations.length; index++) {
						string += ", " + destinations[index].description;
					}
				}
				return string;
			};

			/**
			 * This method checks to make sure all required information is present to create a new image record
			 * @returns {boolean}
			 */
			self.validUpload = function () {
				var invalidUpload = false;
				var invalidComponents = IMAGE_UPLOAD_FAIL_TO_LAUNCH;
				if (angular.equals(self.uploadImageCategory, "")) {
					invalidComponents = invalidComponents + MISSING_IMAGE_CATEGORY;
					invalidUpload = true;
				}
				if (angular.equals(self.uploadImageSource, "")) {
					if (invalidUpload) {
						invalidComponents = invalidComponents + "\n ";
					}
					invalidComponents = invalidComponents + MISSING_IMAGE_SOURCE;
					invalidUpload = true;
				}
				if (self.uploadImageDestinations.length ===0) {
					if (invalidUpload) {
						invalidComponents = invalidComponents + "\n ";
					}
					invalidComponents = invalidComponents + MISSING_DESTINATION_FOR_UPLOAD;
					invalidUpload = true;
				}
				if (self.invalidFileType) {
					if (invalidUpload) {
						invalidComponents = invalidComponents + "\n ";
					}
					invalidComponents = invalidComponents + MISSING_OR_INVALID_FILE;
					invalidUpload = true;
				}
				if (invalidUpload) {
					alert(invalidComponents);
					return false;
				}
				return true;
			};

			/**
			 * After successfully uploading an image resets the fields
			 */
			self.resetUploadFields = function () {
				document.getElementById("uploadImageData").value = null;
				self.uploadImageCategory = "";
				self.uploadImageSource = "";
				self.uploadImageDestinations = self.destinations;
				self.uploadImageImage = "";
			};

			/**
			 * This method will check if the upload request is valid and then make a call to the api to upload the image
			 * This method will also close the modal
			 */
			self.uploadImage = function () {
				self.error = null;
				self.success = null;
				if (self.validUpload()) {
					$('.modal').modal('hide');
					var uploadImage = {
						upc: self.sellingUnit.upc,
						imageCategoryCode: self.uploadImageCategory,
						imageSourceCode: self.uploadImageSource,
						destinationList: self.uploadImageDestinations,
						imageData: self.uploadImageImage,
						imageName: self.uploadImageName
					};
					productSellingUnitsApi.uploadImage(uploadImage, self.uploadResult, self.fetchError);
					self.isWaitingForResponse = true;
				}
			};

			/**
			 * On successful upload reset the view
			 * @param results
			 */
			self.uploadResult = function (results) {
				self.success = 'Image "' + self.uploadImageName
					+ '" is uploaded successfully and associated with the UPC "'
					+ self.sellingUnit.upc + '"';
				self.resetUploadFields();
				productSellingUnitsApi.getImageInformation(self.upcs, self.loadData, self.fetchError);
			};


			/**
			 * Initializes the modal and sets the onchange for the file input to check the file type
			 */
			self.initModal = function () {
				var uploadImageData = document.getElementById("uploadImageData");
				var invalidSpan = document.getElementById("invalidSpan");
				var imageTypeCode = "";
				self.invalidFileType = true;
				uploadImageData.addEventListener('change', function (e) {
					self.uploadImageName = "";
					var imageData = uploadImageData.files[0];
					imageTypeCode = imageData.type.split('/').pop();
					if (self.validFileTypes.includes(imageTypeCode.toLowerCase())) {
						invalidSpan.style.display = 'none';
						self.uploadImageName = imageData.name;
						self.invalidFileType = false;
						var reader = new FileReader();
						reader.onloadend = function () {
							self.uploadImageImage = reader.result.split(',').pop();
						};
						reader.readAsDataURL(imageData);
					} else {
						self.invalidFileType = true;
						invalidSpan.style.display = 'block';
						alert(INVALID_FILE_UPLOAD);
					}
				});
			};

			/**
			 * This method resets all of the image info data to its original value
			 */
			self.resetImageInfo = function () {
				self.allImages = angular.copy(self.originalImages);
				self.selectedImages=[];
				self.error = null;
				self.success = null;
				self.invalidStatusString = '';
			};

			/**
			 * This method is used to determine if there is a change to save
			 * @returns {boolean}
			 */
			self.detectChange = function () {
				var current = angular.copy(self.allImages);
				var original = angular.copy(self.originalImages);
				angular.forEach(current, function (currentData) {
					currentData.selected = false;
					currentData.useImageData = false;
					currentData.imageData=null;
				});
				angular.forEach(original, function (originalData) {
					originalData.selected = false;
					originalData.useImageData = false;
					originalData.imageData=null;
				});
				if(angular.toJson(current) !== angular.toJson(original)){
					$rootScope.contentChangedFlag= true;
					return true;
				} else{
					$rootScope.contentChangedFlag= false;
					return false;
				}
			};

			/**
			 * This method will take determine the changes to be made then send the changes to the backend for updating
			 */
			self.saveImageInfo = function () {
				self.isWaitingForResponse = true;
				self.error = "";
				self.success = null;
				self.missingDestination=false;
				self.invalidStatus=false;
				var diff = self.getDifferences();

				if(self.detectChange()){
					if(self.missingDestination){
						self.error=MISSING_DESTINATION_DOMAIN;
						self.isWaitingForResponse=false;
					} else if(self.invalidStatus || !self.hasOnlyOnePrimary() || self.altImagesWithoutPrimaryImage()
						|| !self.validationImages()) {

						self.error = self.invalidStatusString;
						self.isWaitingForResponse = false;
					} else {
						if (diff.length > 0) {
							self.waitingForDataUpdates = true;
							productSellingUnitsApi.updateImageInfo(diff, self.loadUpdateData, self.fetchError);
						}
						if (self.destinationUpdates.length > 0) {
							self.waitingForAddingDestintions = true;
							productSellingUnitsApi.addDestinations(self.destinationUpdates, self.updatedDestinations, self.fetchError);
						}
					}
				} else {
					self.error = MESSAGE_NO_DATA_CHANGE;
					self.isWaitingForResponse = false;
				}
			};

			self.hasOnlyOnePrimary = function () {
				var count = 0;
				angular.forEach(self.allImages, function (image) {
					if (self.trimData(image.imagePriorityCode)===PRIMARY_PRIORITY) {
						count++;
					}
				});
				if (count > 1){
					self.invalidStatusString = ONLY_ONE_PRIMARY_FOR_PRODUCT;
					return false;
				} else {
					return true;
				}
			};

			self.isNotExistActiveAndPrimary = function () {
				var flagCheck = true;
				angular.forEach(self.allImages, function (image) {
					if (image.activeOnline && self.trimData(image.imagePriorityCode)===PRIMARY_PRIORITY) {
						flagCheck = false;
					}
				});
				return flagCheck;
			};


			self.altImagesWithoutPrimaryImage = function(){
				var flagCheck = false;
				if (self.isNotExistActiveAndPrimary()){
					angular.forEach(self.allImages, function (image) {
						if (image.activeOnline){
							if(self.trimData(image.imagePriorityCode) === PRIMARY_ALTERNATE){
								self.invalidStatusString = IMG_ACTIVE_ONLINE_IN_ALTERNATE;
								flagCheck = true;
							} else if (self.trimData(image.imageCategoryCode) === CATEGORY_SWATCHES){
								self.invalidStatusString = IMG_ACTIVE_ONLINE_IN_CATEGORY_SWATCH;
								flagCheck = true;
							}
						}
					});
				}
				return flagCheck;
			};

			self.validationImages = function(){
				var flagCheck = true;
				angular.forEach(self.allImages, function (image) {
					if (image.imageStatusCode === STATUS_FOR_REVIEW) {
						angular.forEach(self.originalImages, function (originalImage) {
							if (originalImage.key.sequenceNumber === image.key.sequenceNumber
								&& self.trimData(originalImage.imageStatusCode) !== STATUS_FOR_REVIEW) {
								self.invalidStatusString = IMG_STATUS_CHANGE_TO_REVIEW;
								flagCheck = false;
							}
						});
					} else if (self.trimData(image.imageStatusCode) === STATUS_REJECTED && self.trimData(image.imagePriorityCode) === PRIMARY_PRIORITY){
						self.invalidStatusString = IMG_PRIMARY_REJECTED;
						flagCheck = false;
					}
					if (image.activeOnline) {
						if (self.trimData(image.imageStatusCode) === STATUS_FOR_REVIEW) {
							self.invalidStatusString = IMG_PRIMARY_FOR_REVIEW_ACTONLINE;
							flagCheck = false;
						}
						if (self.trimData(image.imageStatusCode) === STATUS_REJECTED) {
							self.invalidStatusString = IMG_STATUS_REJECTED;
							flagCheck = false;
						}
						if (self.trimData(image.imagePriorityCode) === PRIMARY_NOT_APPLICABLE
							&& self.trimData(image.imageCategoryCode) !== CATEGORY_SWATCHES) {
							self.invalidStatusString = IMG_ACTIVE_ONLINE_IS_NONE_CATEGORY_OR_PRIMARY_ALTERNATE;
							flagCheck = false;
						}
					}
				});
				return flagCheck;
			};

			/**
			 * When destinations have been successfully updated
			 * @param results
			 */
			self.updatedDestinations = function (results) {
				self.success = results.message;
				self.error = "";
				self.waitingForAddingDestintions = false;
				self.showUpdatedData();
			};

			/**
			 * For when image info data successfully updates
			 * @param results
			 */
			self.loadUpdateData = function (results) {
				self.success = results.message;
				self.error = "";
				self.invalidChange = null;
				self.waitingForDataUpdates = false;
				self.showUpdatedData();
			};

			/**
			 * This determines if the UI is still waiting on a response from the api, after its nolong waiting it will
			 * get the updated information
			 */
			self.showUpdatedData = function () {
				if (!self.waitingForDataUpdates && !self.waitingForRemovingDestinations && !self.waitingForAddingDestintions) {
					self.allImages = [];
					self.imageURIs = [];
					self.imageInfo = false;
					productSellingUnitsApi.getImageInformation(self.upcs, self.loadData, self.fetchError);
				}
			};

			/**
			 * Get the differences between the current image information and the original image information
			 * @returns {Array}
			 */
			self.getDifferences = function () {
				self.destinationUpdates = [];
				var differences = [];

				var isDifferent = false;
				for (var index = 0; index < self.allImages.length; index++) {
					var currentImage = {
						key: {
							id: self.allImages[index].key.id,
							sequenceNumber: self.allImages[index].key.sequenceNumber,
						},
						productScanImageBannerList: []
					};
					isDifferent = false;
					if (!angular.equals(self.trimData(self.allImages[index].altTag), self.trimData(self.originalImages[index].altTag))) {
						currentImage.altTag = self.allImages[index].altTag;
						isDifferent = true;
					}
					if (!angular.equals(self.allImages[index].imageCategoryCode, self.originalImages[index].imageCategoryCode)) {
						currentImage.imageCategoryCode = self.allImages[index].imageCategoryCode;
						isDifferent = true;
					}
					if (!angular.equals(self.allImages[index].imagePriorityCode, self.originalImages[index].imagePriorityCode)) {
						currentImage.imagePriorityCode = self.allImages[index].imagePriorityCode;
						isDifferent = true;
					}

					if (!angular.equals(self.allImages[index].destinations, self.originalImages[index].destinations)) {
						self.getDestinationDifference(index);
					}

					if (!angular.equals(self.allImages[index].activeOnline, self.originalImages[index].activeOnline)) {
						currentImage.activeOnline = self.allImages[index].activeOnline;
						isDifferent = true;
					}
					if(self.allImages[index].activeOnline){
						if(!angular.equals(APPROVED_STATUS_CODE, self.trimData(self.allImages[index].imageStatusCode))){
							self.invalidStatus=true;
							var badStatus="";
							angular.forEach(self.statuses, function(status){
								if(angular.equals(self.allImages[index].imageStatusCode, status.id)){
									badStatus=status.description;
								}
							});
						}
						self.invalidStatusString="An image in \'"+badStatus+"\' status cannot be made \"Active Online\"";
					}
					if (!angular.equals(self.allImages[index].imageStatusCode, self.originalImages[index].imageStatusCode)) {
						currentImage.imageStatusCode = self.allImages[index].imageStatusCode;
						isDifferent = true;
					}
					if (isDifferent) {
						differences.push(currentImage);
					}

					if (isDifferent && self.allImages[index].destinations.length === 0){
						self.missingDestination = true;
					}
				}
				return differences;
			};

			/**
			 * Trim data.
			 */
			self.trimData = function (dataString){
				return (dataString == null || dataString == undefined)?'':dataString.trim();
			}

			/**
			 * Gets all the changes in the image destinations
			 * @param index
			 */
			self.getDestinationDifference = function (index) {
				var arrayIndex;
				if (self.allImages[index].destinations.length === 0) {
					self.missingDestination = true;
				}
				var destinationChanges = {
					upc: self.allImages[index].key.id,
					sequenceNumber: self.allImages[index].key.sequenceNumber,
					activeSwitch: self.allImages[index].activeSwitch,
					destinationsAdded: [],
					destinationsRemoved: []

				};
				angular.forEach(self.allImages[index].destinations, function (destination) {
					var originalDestinations = self.originalImages[index].destinations;
					var foundBanner = false;
					if (originalDestinations.length > 0) {
						for (arrayIndex = 0; arrayIndex < originalDestinations.length; arrayIndex++) {
							if (angular.equals(destination.id, originalDestinations[arrayIndex].id)) {
								foundBanner = true;
								break;
							}
						}
					}
					if (!foundBanner) {
						destinationChanges.destinationsAdded.push(destination.id);
					}
				});
				angular.forEach(self.originalImages[index].destinations, function (destination) {
					var newDestinations = self.allImages[index].destinations;
					var foundBanner = false;
					if (newDestinations.length > 0) {
						for (arrayIndex = 0; arrayIndex < newDestinations.length; arrayIndex++) {
							if (angular.equals(destination.id, newDestinations[arrayIndex].id)) {
								foundBanner = true;
								break;
							}
						}
					}
					if (!foundBanner) {
						destinationChanges.destinationsRemoved.push(destination.id)
					}
				});
				self.destinationUpdates.push(destinationChanges);
			};

			/**
			 * Updates the cropper to a new image
			 * @param imageData
			 */
			self.setImageToEdit = function (imageData) {
				self.imageToEditData = imageData;
				var modal = document.getElementById("editImageModalDialog");
				modal.style.width = "" + 1030 + "px";
				self.cropper.replace(imageData.image);
			};

			/**
			 * Show full image for view.
			 * @param image
			 */
			self.showFullImage  = function (image) {
				self.imageBytes = image.image;
				self.imageFormat = self.trimData(image.imageFormat);

				if (self.imageBytes !== ''){
					$('#imagePopup').modal({ backdrop: 'static', keyboard: true });
				}

			};

			/**
			 * Spin the editing image
			 * @param degrees
			 */
			self.spinCropper = function (degrees) {
				self.currentAngle += degrees;
				self.cropper.rotateTo(self.currentAngle);
			};

			/**
			 * Set the movement mode to crop(create new crop box or move it) on the editing image
			 */
			self.cropMode = function () {
				self.cropper.setDragMode("crop");
			};

			/**
			 * Set the movement mode to move (move image or crop box) on the editing image
			 */
			self.moveMode = function () {
				self.cropper.setDragMode("move");
			};

			/**
			 * Zoom into the editing image
			 */
			self.zoomIn = function () {
				self.cropper.zoom(0.1);
			};

			/**
			 * Zoom out of editing image
			 */
			self.zoomOut = function () {
				self.cropper.zoom(-0.1);
			};

			/**
			 * Flip image on its X axis
			 */
			self.flipImageX = function () {
				self.scaleX = self.scaleX * -1;
				self.cropper.scale(self.scaleX, self.scaleY);
			};

			/**
			 * Flip image on its Y axis
			 */
			self.flipImageY = function () {
				self.scaleY = self.scaleY * -1;
				self.cropper.scale(self.scaleX, self.scaleY);
			};

			/**
			 * preview the proposed changes to the image
			 */
			self.previewCroppedImage = function () {
				self.editedImage = self.cropper.getCroppedCanvas().toDataURL();
				var preview = document.getElementById("previewImage");
				var previewModal = document.getElementById("previewImageModalDialog");
				previewModal.style.width = "1000px";
				preview.src = self.editedImage;
			};

			/**
			 *Save new cropped image and send it to the backend to update the database
			 */
			self.saveImage = function () {
				$('.modal').modal('hide');
				var uploadImage = {
					upc: self.imageToEditData.key.id,
					imageCategoryCode: self.imageToEditData.imageCategoryCode,
					imageSourceCode: self.imageToEditData.imageSource.id,
					imageData: self.editedImage.split(',').pop(),
					imageName: "" + self.imageToEditData.key.id + "." + self.imageToEditData.imageType.imageFormat + ""
				};
				productSellingUnitsApi.uploadImage(uploadImage, self.uploadResult, self.fetchError);
				self.isWaitingForResponse = true;
			};
			/**
			 * This method will check the category of an image and if it is set to 'Swatches' it will automatically
			 * set the priority of the image to Not Applicable
			 * @param image
			 */
			self.checkCategory = function (image) {
				if (angular.equals(image.imageCategoryCode, "SWAT")) {
					image.imagePriorityCode = 'NA'
				}
			};
			/**
			 * This method will update the selectedImages list
			 * @param image the image to be added or removed
			 * @param selected whether to add(true) or remove (false) the image
			 */
			self.updateSelectedImage = function (image, selected) {
				if (selected) {
					self.selectedImages.push(image);
				} else {
					var index = self.selectedImages.length;
					while (index--) {
						if (angular.equals(image.id, self.selectedImages[index].id)) {
							if(angular.equals(image.sequenceNumber, self.selectedImages[index].sequenceNumber)){
								self.selectedImages.splice(index, 1);
							}
						}
					}
				}
			};

			/**
			 * This method will add or remove all of the images from the selected images list
			 */
			self.selectAll = function () {
				self.selectedImages = [];
				angular.forEach(self.allImages, function(image){
					image.selected=self.selectAllImages;
					if(self.selectAllImages) {
						self.updateSelectedImage(image, image.selected);
					}
				});
			};
			/**
			 * This method is called to initialize the copy to hierarchy modal
			 */
			self.copyToHierarchyModalInit =function () {
				if(permissionsService.getPermissions('CP_IMG_01', 'EDIT')) {
					var params = {
						prodId: self.productMaster.prodId,
					};
					productSellingUnitsApi.copyToHierarchyPath(params,
						function (results) {
							self.hierarchyPath = results.data;
							if (self.hierarchyPath.length > 0) {
								self.hierarchyString = "Nominate image for the hierarchy " + self.hierarchyPath[self.hierarchyPath.length - 1].displayText;
								for (var index = self.hierarchyPath.length - 2; index >= 0; index--) {
									self.hierarchyString = self.hierarchyString + "→" + self.hierarchyPath[index].displayText;
								}
								self.hierarchyString = self.hierarchyString + " at the level:";
								self.isDisableHierarchy = false;
							} else {
								self.isDisableHierarchy = true;
							}
						},
						self.fetchError);
				}
			};

			self.getDisableHierarchy = function(){
				return  (!self.selectedImages.length>0);
			};
			/**
			 * This method will validate that all of the images to be copied to the customer hierarchy are valid
			 * @returns {boolean}
			 */
			self.validHierarchy =function () {
				var isValid=true;
				if(self.selectedImages.length===0){
					isValid=false;
				}
				angular.forEach(self.selectedImages, function(hierarchyCandidate){
					if(!angular.equals(hierarchyCandidate.imageStatusCode, "A")){
						isValid= false;
					}
				});
				if(isValid){
					return true;
				} else {
					return false;
				}
			};
			/**
			 * This method will copy an image's infromation to the customer hierarchy
			 */
			self.copyToHierarchy = function () {
				$('.modal').modal('hide');
				var path;
				if(angular.equals(self.hierarchyLevels, "single")){
					path=[
						self.hierarchyPath[0]
					];
				} else {
					path=self.hierarchyPath;
				}
				var copyToHierarchyRequest={
					imageInfo :self.selectedImages,
					genericEntities : path,
					upcs : self.upcs
				};
				self.isWaitingForResponse = true;
				productSellingUnitsApi.copyToHierarchy(copyToHierarchyRequest, self.loadData, self.fetchError);
				self.selectedImages=[];
				angular.forEach(self.allImages, function(images){
					images.selected=false;
				});
			};
			/**
			 * This method is used to determine what image to display
			 * @param imageInfo
			 * @returns {boolean} true if imageInfo is still waiting for image from api false if the api has returned image info
			 */
			self.isImageUndefined=function(imageInfo){
				return typeof imageInfo['image']=== 'undefined';
			};

			/**
			 * This method checks to make sure that there is a primary image before allowing an alternate image becoming
			 * active online
			 * @param currentImage
			 */
			self.checkForCurrentPrimary = function(currentImage){
				self.invalidChange = null;
				var foundPrimary = false;
				if(currentImage.imagePriority.id !== PRIMARY_PRIORITY){
					for(var index = 0; index<self.allImages.length; index++){
						if(self.allImages[index].imagePriority.id === PRIMARY_PRIORITY){
							foundPrimary = true;
							break
						}
					}
					if(!foundPrimary){
						self.invalidChange=MISSING_PRIMARY_STRING;
						currentImage.activeOnline = false;
					}
				}
			}
			/**
			 * handle when click on return to list button
			 */
			self.returnToList = function(){
				$rootScope.$broadcast('returnToListEvent');
			};

			/**
			 * Download current image.
			 */
			self.downloadImage = function (imageBytes, imageFormat) {
				if(imageBytes !== '' ){
					downloadImageService.download(imageBytes, imageFormat);
				}
			};
		}
	}
)();
