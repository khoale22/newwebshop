(function(){
	'use strict';

	angular.module('productMaintenanceUiApp')
		.controller('AuthenticationLoginController', AuthenticationLoginController)
		.controller('AuthenticationLogoutController', AuthenticationLogoutController)
		.directive('authenticationDirective', AuthenticationDirective)
		.run(runnable)
		.factory('AuthenticationService', AuthenticationService)
		.factory('AuthenticationInterceptor', AuthenticationInterceptor)
		.config(AuthenticationConfig);

	AuthenticationConfig.$inject = ['$httpProvider'];
	function AuthenticationConfig($httpProvider) {
		$httpProvider.interceptors.push('AuthenticationInterceptor');
	}

	AuthenticationInterceptor.$inject = ['$q', '$location'];
	function AuthenticationInterceptor($q, $location) {
		return {
			response: function (response) {
				if (response.status === 401) {
					console.log("Response 401");
				}
				// return the response or wrap it into a promise if it is blank
				return response || $q.when(response);
			},
			responseError: function (rejection) {
				if (rejection.status === 401 || rejection.status === 403) {
					var returnTo = $location.search().returnTo;
					if (returnTo === undefined && $location.path().lastIndexOf('/login', 0) !== 0) {
						returnTo = $location.path();
					}
					$location.path('/login').search('returnTo', returnTo);
				}

				return $q.reject(rejection);
			}
		};
	}

	AuthenticationService.$inject = ['$http', 'urlBase', '$rootScope', '$window'];
	function AuthenticationService($http, urlBase, $rootScope, $window) {
		return {
			invalidate: function () {
				delete $rootScope.currentUser;
				delete $window.sessionStorage["currentUser"];
			},
			//getCsrf: function() { return $http.get('/product-maintenance_api/login'); },
			login: function (username, password) {
				return $http.post(
					urlBase + '/login',
					$.param({'username': username, 'password': password}),
					{
						headers: {
							'Content-Type': 'application/x-www-form-urlencoded'
						}
					});
			},
			logout: function () {
				return $http.post(urlBase +'/logout');
			},
			hasRole: function (requiredRole) {
				var roles = requiredRole && requiredRole.split(',');
				// check that there is a user and that he/she has the required role
				return $rootScope.currentUser &&
					_.intersection($rootScope.currentUser.roles, roles).length > 0;
			}
		};
	}

	runnable.$inject = ['$rootScope', '$location', '$state', 'AuthenticationService', '$window', 'Idle'];

	function runnable($rootScope, $location, $state, AuthenticationService, $window, Idle) {
		// $stateChangeStart instead of $routeChangeStart since we're using ui-router
		$rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
			if (toState.data && toState.data.access && !AuthenticationService.hasRole(toState.data.access)) {
				event.preventDefault();

				if (fromState.url === '^' && !$rootScope.currentUser) {
					$state.go('login');
				}
			}
		});
		// restore currentUser from sessionStorage on refresh of page
		if(angular.isDefined($window.sessionStorage["currentUser"])) {
			//Set currentUser if in sessionStorage
			var currentUser = JSON.parse($window.sessionStorage["currentUser"]);
			// set user id, name, role
			$rootScope.currentUser = {
				id: currentUser.id,
				name: currentUser.name,
				roles: currentUser.roles
			};

			if ($rootScope.currentUser) {
				Idle.watch();
			}

		}
	}

	AuthenticationDirective.$inject = ['AuthenticationService'];
	function AuthenticationDirective(AuthenticationService) {
		return {
			restrict: 'A',
			link: function (scope, element, attrs) {
				var makeVisible = function () {
						element.removeClass('hidden');
					},
					makeHidden = function () {
						element.addClass('hidden');
					},
					determineVisibility = function (resetFirst) {
						if (resetFirst) {
							makeVisible();
						}

						if (AuthenticationService.hasRole(roles)) {
							makeVisible();
						} else {
							makeHidden();
						}
					},
					roles = attrs.permissions.split(',');

				if (roles.length > 0) {
					determineVisibility(true);
				}
			}
		}
	}

	AuthenticationLogoutController.$inject = ['$state','$scope', 'AuthenticationService'];
	function AuthenticationLogoutController($state, $scope,  AuthenticationService) {
		var vm = this;
		vm.loggedInAs = "You are logged in as ";
		vm.separator = " | ";
		vm.logoutLink = "Log Out";
		vm.helpLink = "Help";
		vm.logout = logout;

		$scope.$on('LogoutEvent', function(event) {
			vm.logout();
		});

		function logout() {
			vm.dataLoading = true;

			AuthenticationService.logout()
				.success(function () {
					vm.dataLoading = false;
					$state.go('login');
				})
				.error(function () {
					vm.dataLoading = false;
					$state.go('login');
				});
		}
	}

	AuthenticationLoginController.$inject = ['$scope', '$rootScope','$uibModalStack', '$location', '$state', 'Idle', 'AuthenticationService', '$window'];
	function AuthenticationLoginController($scope, $rootScope, $uibModalStack, $location, $state, Idle, AuthenticationService, $window) {
		var vm = this;
		// reset login status
		AuthenticationService.invalidate();

		vm.dataLoading = false;
		vm.login = login;
		vm.error = null;

		$uibModalStack.dismissAll();

		function login() {
			vm.dataLoading = true;
			if (!$scope.username || !$scope.password) {
				return;
			}

			AuthenticationService.login($scope.username, $scope.password).success(loginSuccess).error(loginFailure);

			function loginSuccess(data, status, headers, config) {
				// set user id, name, role
				$rootScope.currentUser = {
					id: data.id,
					name: data.name,
					roles: data.roles
				};

				$window.sessionStorage["currentUser"] = JSON.stringify($rootScope.currentUser);

				vm.dataLoading = false;
				var redirectTo = $location.search().returnTo;
				if (redirectTo === undefined) {
					$state.go('home');
				} else {
					$location.path(redirectTo).search({});
				}
				Idle.watch();
			}

			function loginFailure(data, status, headers, config) {
				vm.dataLoading = false;

				if (status == 401) {
					vm.error = "Invalid user name or password."
				} else {
					if (data.error != null) {
						vm.error = status + " " + data.error;
					} else {
						vm.error = "An unknown error occurred: " + status;
					}
				}
				return vm.error;
			}
		}
	}
})();
