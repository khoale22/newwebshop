(function () {
    var app = angular.module('app', []);

    app.controller('filterDemoCtrl', filterDemoCtrl);

    filterDemoCtrl.$inject = ['$scope'];

    function filterDemoCtrl($scope) {
        var employees = [
            { name: 'Hải', birthDate: new Date('12/3/1989'), salary: 15000000, gender: 'Male', status: 'true1'},
            { name: 'Hoàng', birthDate: new Date('6/15/1990'), salary: 12000000, gender: 'Male', status: 'true1' },
            { name: 'Long', birthDate: new Date('1/14/1992'), salary: 17000000, gender: 'Male', status: 'false1' },
            { name: 'Trung', birthDate: new Date('12/3/1993'), salary: 12000000, gender: 'Male', status: 'true1' },
            { name: 'Hương', birthDate: new Date('12/3/1995'), salary: 11000000, gender: 'Female', status: 'true1' },
            { name: 'Thủy', birthDate: new Date('12/3/1988'), salary: 10000000, gender: 'Female', status: 'true1' }
        ];
        $scope.employees = employees;
        $scope.limitRow = 3;

        $scope.item = {};
        $scope.items = [
            { name: 'Item1', Code: 'Code1', },
            { name: 'Item2', Code: 'Code3'},
            { name: 'Item3',  Code: 'Code4'},
            { name: 'Item4',  Code: 'Code4' },
            { name: 'Item5', Code: 'Code5' },
        ];

        $scope.item.selected = $scope.items[0] //here you can set the item selected
    }

    // input o day chinh la =(NHIEU ) employee.status
    app.filter('status22', function () {
        return function (input) {
           // if (input === "true1")
        	if(angular.equals(input , "true1"))
                return 'Kích hoạt';
            else
                return 'Khóa2';
        }
    });
    
    
    
})()