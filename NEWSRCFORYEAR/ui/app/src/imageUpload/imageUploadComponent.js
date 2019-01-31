/*
 *   kitsComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';


/**
 * Kits -> Kits Info page component.
 *
 * @author m594201
 * @since 2.8.0
 */
(function () {

	angular.module('productMaintenanceUiApp').controller('ImageUploadController', imageUploadController);

	imageUploadController.$inject = ['productSellingUnitsApi', 'ngTableParams'];

	function imageUploadController(productSellingUnitsApi, ngTableParams) {

		var self = this;

		/**
		 * This list holds all of the possible image categories
		 * @type {Array}
		 */
		self.imageCategories=[];
		/**
		 * The currently selected image category
		 * @type {null}
		 */
		self.imageCategory=null;

		/**
		 * This list holds all of the possible image sources
		 * @type {Array}
		 */
		self.imageSources =[];
		/**
		 * The currently selected image source
		 * @type {null}
		 */
		self.imageSource = null;

		/**
		 * This list holds all of the possible destination domains
		 * @type {Array}
		 */
		self.destinationDomains = [];
		/**
		 * The list of currently selected destination domains
		 * @type {Array}
		 */
		self.selectedDestinations = [];
		/**
		 * Flag for when all images are selected to be uploaded
		 * @type {boolean}
		 */
		self.uploadAllChecked=false;
		/**
		 * Flag for when all images are selected to replace the current primary
		 * @type {boolean}
		 */
		self.primaryAllChecked=false;
		/**
		 * Flag for whether or not to continue uploading images
		 * @type {boolean}
		 */
		self.continueUploads=false;
		/**
		 * Flag for if uploads are currently occurring
		 * @type {boolean}
		 */
		self.isUploading = false;
		/**
		 * List of images to be potentially uploaded
		 * @type {Array}
		 */
		self.imageData = [];
		/**
		 * Code table for Existing primary action dropdown
		 * These actions are reserved for what to do with the current primary image
		 * if the new image is set to replace the current primary
		 * @type {*[]}
		 */
		self.existingPrimaryAction=[
			{
				description: 'Inactivate',
				keyword: 'INACT'
			},
			{
				description: 'Reject',
				keyword: 'REJ'
			},
			{
				description: 'Alternate',
				keyword: 'ALT'
			}
		];
		/**
		 * The currently selected existing primary header.
		 * @type {Object}
		 */
		self.existingPrimaryHeader = null;
		/**
		 * These variables are parameters for the paginated table, this is the start page and the number of elements per page
		 * @type {number}
		 */
		self.START_PAGE =1;
		self.PAGE_SIZE =7;

		/**
		 * The paramaters that define the table showing the report.
		 * @type {ngTableParams}
		 */
		self.tableParams = null;

		/**
		 * Swatches Category Code
		 * @type {string}
		 */
		self.CATEGORY_SWATCHES = 'SWAT';

		/**
		 * List of valid file types
		 * @type {[string,string,string,string,string]}
		 */
		self.validFileTypes = ["jpg", "jpeg", "png"];

		var START_INDEX = 0;

		/**
		 * Initialize the controller.
		 */
		this.$onInit = function () {
			self.existingPrimaryHeader = self.existingPrimaryAction[0];
			productSellingUnitsApi.getImageCategories(self.loadCategories, self.fetchError);
			productSellingUnitsApi.getImageSources(self.loadSources, self.fetchError);
			productSellingUnitsApi.getImageDestinations(self.loadDestinations, self.fetchError);
			self.addFileCheckToBrowse();
			self.buildTable();
		};

		/**
		 * This method will create an event listener to the upload button so that when files are added to the input,
		 * all of the files are check to see if they are valid
		 */
		self.addFileCheckToBrowse = function () {
			var uploadImageData = document.getElementById("selectedFiles");
			var imageTypeCode = "";
			self.invalidFileType = true;
			uploadImageData.addEventListener('change', function (e) {
				self.uploadImageName = "";
				var imageDataOrg = angular.copy(self.imageData);
				angular.forEach(uploadImageData.files, function (imageData) {
					imageTypeCode = imageData.type.split('/').pop();
					if (self.validFileTypes.indexOf(imageTypeCode.toLowerCase()) > -1) {
						self.uploadImageName = imageData.name;
						self.invalidFileType = false;
						var reader = new FileReader();
						reader.onloadend = function () {
							var imageCandidate={
								name: imageData.name,
								size: (imageData.size/1000).toFixed(2) + ' Kb',
								status: self.determineStatus(imageData.name.split('.')[0]),
								success: null,
								setToPrimary: false,
								existingPrimary: null,
								destinationDomain: self.destinationDomains,
								toUpload:false,
								isUploading:false,
								data: reader.result.split(',').pop()
							};
							if(self.imageUnique(imageCandidate)){
								self.imageData.push(imageCandidate);
							}
							if(angular.toJson(self.imageData) !== angular.toJson(imageDataOrg)){//reset if data is changed
								self.updateImageDataTable();
								var loadFirstTime = true;
								angular.forEach(imageDataOrg, function(image){
									if(self.isValidToChange(image)){
										loadFirstTime = false;
									}
								});
								if(loadFirstTime){
									self.existingPrimaryHeader = self.existingPrimaryAction[0];
								}
							}
						};
						reader.readAsDataURL(imageData);
					} else {
						self.invalidFileType = true;
					}
					self.firstSearch=true;
				});
				angular.element(uploadImageData).val(null);
			});
		};
		/**
		 * This method checks if the image is unique based on name and data;
		 * @param image
		 * @returns {boolean}
		 */
		self.imageUnique = function (image) {
			var notFound = true;
			for(var index = 0; index<self.imageData.length; index++){
				if(angular.equals(image.name, self.imageData[index].name) && angular.equals(image.data, self.imageData[index].data)){
					notFound = false;
					break;
				}
			}
			return notFound;
		};

		/**
		 * This method does a series of regex tests to see if the name of the file is valid
		 * @param name
		 * @returns {string}
		 */
		self.determineStatus= function (name) {
			var status = "";
			var letters = /[a-zA-Z]/;
			var legalCharactersAndLetters = /\w/;
			if(name.match(letters) !== null  && name.match(legalCharactersAndLetters)){
				status = "Invalid image name, must start with possible UPC"
			}
			return status;
		};

		/**
		 * Call to the backend to the list of image categories
		 * @param results
		 */
		self.loadCategories = function (results) {
			self.imageCategories = results;
		};

		/**
		 * Call to the backend to get the list of image sources
		 * @param results
		 */
		self.loadSources = function (results) {
			self.imageSources = results;
		};

		/**
		 * Call to the backend to get the list of image destination domains
		 * @param results
		 */
		self.loadDestinations = function (results) {
			self.destinationDomains = results;
			self.selectedDestinations = results;
		};

		/**
		 * Component will reload the kits data whenever the item is changed in casepack.
		 */
		this.$onChanges = function () {
		};

		/**If there is an error this will display the error
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
		 * Sets the error
		 * @param error
		 */
		self.setError = function (error) {
			self.error = error;
		};

		/**
		 * This method handles changes of changing an image candidates 'Set As Primary' status
		 * @param index
		 */
		self.changePrimary = function (index) {
			if(!self.imageData[index].setToPrimary){
				self.imageData[index].existingPrimary=null;
			} else {
				self.imageData[index].existingPrimary = self.existingPrimaryHeader;
			}
			if(self.imageData[index].setToPrimary !== self.primaryAllChecked){
				self.primaryAllChecked = false;
				if(self.imageData[index].setToPrimary){
					var allSelectedItems = true;
					angular.forEach(self.imageData, function(image){
						if(!image.setToPrimary && self.isValidToChange(image)){
							allSelectedItems = false;
						}
					});
					self.primaryAllChecked = allSelectedItems;
				}
			}
		};

		/**
		 * This method handles the users request to remove an image form the list
		 * @param index
		 */
		self.removeImage = function (index) {
			self.imageData.splice(index, 1);
			self.resetImageDataTable();
			if(self.imageData != null && self.imageData.length > 0){
				var imageArray = [];
				angular.forEach(self.imageData, function(image){
					if(self.isValidToChange(image)){
						imageArray.push(image);
					}
				});
				if(imageArray != null && imageArray.length > 0){
					var allSelectedPrimary = true;
					var allSelectedItems = true;
					angular.forEach(imageArray, function(image){
						if(!image.setToPrimary){
							allSelectedPrimary = false;
						}
						if(!image.toUpload){
							allSelectedItems = false;
						}
					});
					self.primaryAllChecked = allSelectedPrimary;
					self.uploadAllChecked = allSelectedItems;
				}
			}else{
				self.primaryAllChecked = false;
				self.uploadAllChecked = false;
			}
		};

		/**
		 * This method removes all images from the image candidate table
		 */
		self.clearList = function () {
			self.imageData = [];
			self.existingPrimaryHeader = self.existingPrimaryAction[0];
			self.updateImageDataTable();
		};

		/**
		 * This method makes all the calls to refresh the image candidate table
		 */
		self.updateImageDataTable = function () {
			self.uploadAllChecked = false;
			self.primaryAllChecked=false;
			self.tableParams.total(self.imageData.length);
			self.tableParams.count(self.imageData.length);
			self.tableParams.reload();
		};

		/**
		 * This method makes all the calls to reset the image candidate table
		 */
		self.resetImageDataTable = function () {
			self.tableParams.total(self.imageData.length);
			self.tableParams.count(self.imageData.length);
			self.tableParams.reload();
		};

		/**
		 * This method starts the image upload process
		 */
		self.startUploads = function () {
			    if(self.validateDestination()){
					self.continueUploads = true;
					self.uploadImages(START_INDEX);
				}
				else {
			    	$('#confirmSelectDestinationsModal').modal("show");
				}
		};

		/**
		 * This method will signal the api to stop uploading images
		 */
		self.stopUploads = function () {
			self.continueUploads = false;
		};

		/**
		 * This method tests to see if the user still wants to upload an image, then checks the current image is valid
		 * to upload, for that to happen it needs to have an empty status and not already been uploaded
		 * @param index
		 */
		self.uploadImages = function (index) {
			if(index<self.imageData.length && self.continueUploads){
				self.isUploading = true;
				if (self.imageData[index].toUpload) {
					self.imageData[index].status = self.validateImage(self.imageData[index]);
					if (angular.equals("", self.imageData[index].status) && angular.equals(null, self.imageData[index].success)) {
						var existingPrimary = null;
						if (self.imageData[index].existingPrimary !== null) {
							existingPrimary = self.imageData[index].existingPrimary.keyword
						}
						var upc ="";
						if(self.imageData[index].name.match(/_/) !== null){
							upc=Number(self.imageData[index].name.split('_')[0]);
						} else{
							upc=Number(self.imageData[index].name.split('.')[0]);
						}
						var imageToUpload = {
							upc: upc,
							imageCategoryCode: self.imageCategory.id,
							imageSourceCode: self.imageSource.id,
							imageName: self.imageData[index].name,
							destinationList: self.imageData[index].destinationDomain,
							imageData: self.imageData[index].data,
							primary: self.imageData[index].setToPrimary,
							existingPrimary: existingPrimary
						};
						self.imageData[index].isUploading = true;
						self.uploadSingleImage(imageToUpload, index)
					} else {
						self.uploadImages(++index);
					}
				} else {
					self.uploadImages(++index);
				}
			} else {
				self.isUploading = false;
			}
		};

		/**
		 * This method makes the call to the api and handles the response.
		 * @param image
		 * @param index
		 */
		self.uploadSingleImage=function(image, index){
			productSellingUnitsApi.uploadImage(image,
				function(results){
					self.imageData[index].isUploading = false;
					self.imageData[index].toUpload = false;
					self.imageData[index].success = results.message;
					self.uploadAllChecked = false;
					self.uploadImages(++index);
				},
				function (error) {
					self.imageData[index].isUploading = false;
					if (error && error.data) {
						if (error.data.message != null && error.data.message != "") {
							self.imageData[index].status = error.data.message;
						} else {
							self.imageData[index].status = error.data.error;
						}
					}
					else {
						self.imageData[index].status = "An unknown error occurred.";
					}
					self.updateImageDataTable();
					self.uploadImages(++index);
			});
		};

		/**
		 * This method ensures that all of the fields are populated before uploading
		 * @param image
		 * @returns {string}
		 */
		self.validateImage = function (image) {
			var status = "";
			if(self.imageCategory === null){
				status += "Missing Image Category\n";
			}
			if(self.imageSources === null){
				status += "Missing Image Source\n";
			}
			if(image.setToPrimary){
				if(image.existingPrimary === null){
					status += "Missing Existing Primary Action"
				}
			}
			return status;
		};

		/**
		 * This method will check all rows
		 */
		self.checkAllRows= function (columnNumber) {
			if(columnNumber === 1){
					for(var index=0; index<self.imageData.length; index++){
						if(self.isValidToChange(self.imageData[index])){
							self.imageData[index].toUpload=self.uploadAllChecked;
						}
					}
			} else if(columnNumber === 4){
				for(var index=0; index<self.imageData.length; index++){
					if(self.isValidToChange(self.imageData[index])) {
						self.imageData[index].setToPrimary = self.primaryAllChecked;
						if (self.primaryAllChecked) {
							self.imageData[index].existingPrimary = self.existingPrimaryHeader;
						} else {
							self.imageData[index].existingPrimary = null;
						}
					}
				}
			} else if(columnNumber === 5){
				for(var index=0; index<self.imageData.length; index++){
					if(self.isValidToChange(self.imageData[index])) {
						if (self.imageData[index].setToPrimary) {
							self.imageData[index].existingPrimary = self.existingPrimaryHeader;
						}
					}
				}
			}
		};
		/**
		 * This method will handle changes to selecting an image to upload
		 * @param index
		 */
		self.selectImageToUpload=function (index) {
			if(self.imageData[index].toUpload !== self.uploadAllChecked){
				self.uploadAllChecked = false;
				if(self.imageData[index].toUpload){
					var allSelectedItems = true;
					angular.forEach(self.imageData, function(image){
						if(!image.toUpload && self.isValidToChange(image)){
							allSelectedItems = false;
						}
					});
					self.uploadAllChecked = allSelectedItems;
				}
			}
		};
		/**
		 * This method will confirm the image category and source are valid before allowing the user to press upload
		 * @returns {boolean}
		 */
		self.validToUpload = function () {
			var isDisabled = true;
			if(self.imageSource !== null && self.imageCategory !== null && self.imageData.length > 0){
				isDisabled = false;
			}
			return isDisabled;
		};

		/**
		 * Constructs the table that shows the report.
		 */
		self.buildTable = function() {
			self.tableParams = new ngTableParams({
				page:1,
				count: self.imageData.length,
				}, {
					counts:[],
					total: self.imageData.length,
					getData: function ($defer, params) {
						self.data = self.imageData;
						$defer.resolve(self.data);
					}
				}
			);
		};

		/**
		 * This function will take all of the selected images and update their destination domains.
		 */
		self.massFill =  function () {
			angular.forEach(self.imageData, function(image){
				if(image.toUpload && self.isValidToChange(image)){
					image.destinationDomain = self.selectedDestinations
				}
			});
		};

		/**
		 * This method will check to see if the image is currently available to make changes to.
		 */
		self.isValidToChange = function (image) {
			if(image.status !== "" || image.success != null){
				return false;
			} else {
				return true;
			}
		};

		self.isHeaderEnable = function () {
			for(var index = 0; index<self.imageData.length; index++){
				if(self.imageData[index].success === null){
					return true;
				}
			}
			return false
		};

		/**
		 * Check Destination
		 *
		 * @returns {boolean}
		 */
		self.validateDestination = function () {
			var flag = true;
			for (var i = 0; i <self.imageData.length;i++) {
				if (self.imageData[i].destinationDomain.length===0){
					flag = false;
					break;
				}
			}
			return flag;
		};

		/**
		 * Handle on change category.
		 */
		self.onChangeCategory = function () {
			if (self.imageData !== null && self.imageData.length > 0
				&& self.imageCategory !== null &&self.imageCategory.id===self.CATEGORY_SWATCHES) {
				self.primaryAllChecked = false;
				angular.forEach(self.imageData, function(image){
					image.setToPrimary = false;
				});
			}
		};
		
		/**
		 * This method will show existing primary header dropdown.
		 * @returns {boolean}
		 */
		self.showExistingPrimaryHeader = function () {
			return (self.imageData != null && self.imageData.length > 0);
		};

		/**
		 * This method will disable existing primary header dropdown.
		 * @returns {boolean}
		 */
		self.disableExistingPrimaryHeader = function () {
			var categorySwatches = false;
			if (self.imageCategory !== null){
				categorySwatches = (self.imageCategory.id === self.CATEGORY_SWATCHES);
			}
			return (!self.isHeaderEnable() || categorySwatches);
		};
	}
})();
