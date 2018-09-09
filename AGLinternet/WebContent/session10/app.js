(function() {
	var app = angular.module('app', []);

	app.controller('filterDemoCtrl', filterDemoCtrl);

	filterDemoCtrl.$inject = [ '$scope' ];

	function filterDemoCtrl($scope) {
		var employees = [ {
			name : 'Bi',
			birthDate : new Date('12/3/1989'),
			salary : 15000000,
			gender : 'Male',
			status : 'true1'
		}, {
			name : 'Anh',
			birthDate : new Date('6/15/1990'),
			salary : 12000000,
			gender : 'Male',
			status : 'true1'
		}, {
			name : 'Dung',
			birthDate : new Date('1/14/1992'),
			salary : 17000000,
			gender : 'Male',
			status : 'false1'
		}, {
			name : 'Cuong',
			birthDate : new Date('12/3/1993'),
			salary : 12000000,
			gender : 'Male',
			status : 'true1'
		}, {
			name : 'Hương',
			birthDate : new Date('12/3/1995'),
			salary : 11000000,
			gender : 'Female',
			status : 'true1'
		}, {
			name : 'Thủy',
			birthDate : new Date('12/3/1988'),
			salary : 10000000,
			gender : 'Female',
			status : 'true1'
		} ];
		$scope.employees = employees;

		$scope.sortColumn = 'name';
		$scope.reverse = false;
		$scope.sortData = function(column) {
			if ($scope.sortColumn == column)
			// if(angular.equals($scope.sortColumn , column))
			{
				$scope.reverse = !$scope.reverse;
			} else {
				$scope.sortColumn = column;
				$scope.reverse = false;
			}
		}

	}

	// input o day chinh la =(NHIEU ) employee.status
	app.filter('status22', function() {
		return function(input) {
			// if (input === "true1")
			if (angular.equals(input, "true1"))
				return 'Kích hoạt';
			else
				return 'Khóa2';
		}
	});

})()