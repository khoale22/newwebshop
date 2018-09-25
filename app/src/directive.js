/*
 *
 * directive.js
 *
 * Copyright (c) 2017 MagRabbit
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of MagRabbit.
 *
 */

'use strict';

/**
 * Collection of custom directives to aid in customizing the application.
 *
 * @author vn70529
 ~ @since 2.12
 */
(function () {
	angular.module('productMaintenanceUiApp')
		.directive('uploadFileModel', uploadFileModel)
		.directive('disableEnterBeginWithSpaceChar', disableEnterBeginWithSpaceChar)
		.directive('numericOnly', numericOnly)
		.directive('emptyValid', emptyValid)
		.directive('minLengthValid', minLengthValid)
		.directive('minValueValid', minValueValid)
		.directive('validationModel', validationModel)
		.directive('capitalize', capitalize);

	/**
	 * Create a directive for upload file.
	 * @param $parse
	 * @returns {{restrict: string, link: link}}
	 */
	function uploadFileModel($parse) {
		return {
			restrict: 'A',
			link: function (scope, element, attrs) {
				var model = $parse(attrs.uploadFileModel);
				var imagePreviewFilename = attrs.imagePreviewFilename;

				var modelSetter = model.assign;

				element.bind('change', function () {
					scope.$apply(function () {
						var file = element[0].files[0];
						modelSetter(scope, file);
						if(file) {
							$("." + imagePreviewFilename).val(file.name);
						}else{
							$("." + imagePreviewFilename).val("");
						}
					});
				});
			}
		};
	}

	/**
	 * Create a directive for disable enter begin with space char.
	 *
	 * @type {Array}
	 */
	disableEnterBeginWithSpaceChar.$inject = [];
	function disableEnterBeginWithSpaceChar( ) {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {

				element.on('keypress', function(event) {
					if (isSpaceCharFirst()) {
						event.preventDefault();
					}
					function isSpaceCharFirst() {
						var dataText = '';
						if(event.target.value != undefined && event.target.value != '') {
							dataText = event.target.value;
						}
						var key = event.which || event.keyCode;

						dataText += String.fromCharCode(key);

						return dataText.startsWith(' ');
					}
				});
				element.on('keyup', function(event) {
					if (isSpaceCharFirst() && $(element).val().length > 0) {
						$(element).val($(element).val().substr(1,$(element).val().length));
					}
					function isSpaceCharFirst() {
						var dataText = '';
						if(event.target.value != undefined && event.target.value != '') {
							dataText = event.target.value;
						}

						return dataText.startsWith(' ');
					}
				});
				element.on('paste', function() {
					if (isSpaceCharFirst()) {
						event.preventDefault();
					}
					function isSpaceCharFirst() {
						if(event.clipboardData != undefined) {
							var dataPaste = event.clipboardData.getData('text/plain');
							return dataPaste.startsWith(' ');
						}
						return false;
					}
				});
				element.on('drop', function() {
					if (isSpaceCharFirst()) {
						event.preventDefault();
					}
					function isSpaceCharFirst() {
						if(event.dataTransfer != undefined) {
							var dataDrop = event.dataTransfer.getData("text");
							return dataDrop.startsWith(' ');
						}
						return false;
					}
				});
				element.on('change', function() {
					if (isSpaceCharFirst()) {
						element.css("border", "1px solid red");
					}else{
						element.css("border", "");
					}
					function isSpaceCharFirst() {
						if(event.target.value != undefined && event.target.value != '') {
							var dataText = event.target.value;
							return dataText.startsWith(' ');
						}
						return false;
					}
				});
			}
		};
	}

	/**
	 * Create a directive for empty valid.
	 *
	 * @type {Array}
	 */
	emptyValid.$inject = [];
	function emptyValid( ) {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				element.on('keypress', function(event) {
					$(element).attr('title', '');
					$(element).removeClass('ng-touched');
					$(element).removeClass('ng-invalid');
				});
				element.on('keyup', function(event) {

					var dataText = '';
					if(event.target.value != undefined && event.target.value != '') {
						dataText = event.target.value;
					}
					if(dataText.length == 0){
						$(element).attr('title', attrs.emptyValid);
						$(element).addClass('ng-invalid ng-touched');
					}
				});
				element.on('blur', function (event) {
					var dataText = '';
					if(event.target.value != undefined && event.target.value != '') {
						dataText = event.target.value;
					}
					if(dataText.length == 0){
						$(element).attr('title', attrs.emptyValid);
						$(element).addClass('ng-invalid ng-touched');
					}
				});
			}
		};
	}
	/**
	 * Create a directive for min length valid.
	 *
	 * @type {Array}
	 */
	minLengthValid.$inject = [];
	function minLengthValid( ) {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				element.on('keypress', function(event) {
					$(element).attr('title', '');
					$(element).removeClass('ng-touched');
					$(element).removeClass('ng-invalid');
				});
				element.on('keyup', function(event) {

					var dataText = '';
					if(event.target.value != undefined && event.target.value != '') {
						dataText = event.target.value;
					}
					if(dataText.length < parseInt(attrs.minlength)){
						$(element).attr('title', attrs.minLengthValid);
						$(element).addClass('ng-invalid ng-touched');
					}
				});
			}
		};
	}

	/**
	 * Create a directive for min length valid.
	 *
	 * @type {Array}
	 */
	minValueValid.$inject = [];
	function minValueValid( ) {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				element.on('keypress', function(event) {
					$(element).attr('title', '');
					$(element).removeClass('ng-touched');
					$(element).removeClass('ng-invalid');
				});
				element.on('keyup', function(event) {

					var dataText = '';
					if(event.target.value != undefined && event.target.value != '') {
						dataText = event.target.value;
					}

					var value = parseInt(dataText);
					if(value) {
						$(element).val(value);
					}
					if(value < parseInt(attrs.min)){
						$(element).attr('title', attrs.minValueValid);
						$(element).addClass('ng-invalid ng-touched');
					}
				});
			}
		};
	}

	numericOnly.$inject = ['$timeout'];
	function numericOnly( $timeout) {
		return {
			require: 'ngModel',
			restrict: 'A',
			link: function (scope, element, attr, ctrl) {
				element.on('keydown', function(event) {
					var keyCode = event.which || event.keyCode;
					if(keyCode==32){
						event.preventDefault();
					}
				});
				function inputValue(val) {

					if (val) {
						var digits = val.replace(/[^0-9]/g, '');

						if (digits !== val) {
							ctrl.$setViewValue(digits);
							ctrl.$render();
							ctrl.$setValidity('numericOnly', false);
						} else {
							ctrl.$setValidity('numericOnly', true);
						}
						return parseInt(digits, 10);
					}
					return undefined;
				}

				ctrl.$parsers.push(inputValue);
				element.on("focus", function () {
					//Highlight text after parsed
					$timeout(function () {
						element[0].select();
					});
				});
				element.on("blur", function () {
					$timeout(function () {
						ctrl.$setValidity('numericOnly', true);
					});
				});
			}
		};
	}

	/**
	 * Sets the data to validate model.
	 *
	 * @param $parse
	 * @returns {{validationModel: string, link: link}}
	 */
	function validationModel($parse) {
		return {
			validationModel:'=',
			link: function(scope, element, attrs) {
				var model = $parse(attrs.validationModel);
				var modelSetter = model.assign;
				element.on('input', function() {
					modelSetter(scope, $(element).val());
				});
			}
		};
	};

	/**
	 * capitalize user input automatically
	 *
	 * @param $parse
	 * @returns {{validationModel: string, link: link}}
	 */
	function capitalize($parse) {
		return {
			require: 'ngModel',
			link: function(scope, element, attrs, modelCtrl) {
				var capitalize = function (inputValue) {
					if (inputValue == undefined) inputValue = '';
					var capitalized = inputValue.toUpperCase();
					if (capitalized !== inputValue) {
						// see where the cursor is before the update so that we can set it back
						var selection = element[0].selectionStart;
						modelCtrl.$setViewValue(capitalized);
						modelCtrl.$render();
						// set back the cursor after rendering
						element[0].selectionStart = selection;
						element[0].selectionEnd = selection;
					}
					return capitalized;
				};
				modelCtrl.$parsers.push(capitalize);
				capitalize(scope[attrs.ngModel]); // capitalize initial value
			}
		};
	}
})();
