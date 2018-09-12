'use strict';
// Define the `phonecatApp` module
angular.module('phonecatApp', [
    // ...which depends on the `phoneList` module
    'ngRoute',
    'phoneDetail22',
    'phoneList'
    // By passing phoneList inside the dependencies array
    // when defining the phonecatApp module, AngularJS will make all
    // entities registered on phoneList available on phonecatApp as well.
]);