'use strict';
// Define the `phonecatApp` module
angular.module('BookApp', [
    // ...which depends on the `phoneList` module
    'ngRoute',
    'loginForm',
    'studentForm',
    'adminForm',
    'bookDetail'
]);