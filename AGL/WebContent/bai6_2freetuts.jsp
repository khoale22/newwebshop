<html>
    <head>
        <title>Angular JS Model</title>
        <meta charset='utf8'/>
        <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.2.15/angular.min.js"></script>
        <style>
            *{margin:0;}
            body{padding: 20px}
            .my-input {
                -webkit-transition:all linear 0.5s;
                transition:all linear 0.5s;
                background: transparent;
            }
            .my-input.ng-invalid {
                color:white;
                background: red;
            }
        </style>
        <script>
            angular.module('MyForm', [])
                    .controller('ExampleController', function($scope)
                {
                    // Khởi tạo giá trị ban đầu
                    $scope.message = {
                        title: 'Trò Chơi Tính Toán',
                        num1: 'Số thứ nhất',
                        num2: 'Số thứ Hai',
                        phep_cong: "Cộng hai số:",
                        phep_tru: "Trừ hai số:",
                        phep_nhan: "Nhân hai số:",
                        phep_chia: "Chia hai số:" 
                    };
                    $scope.styleresult = 'display:none';
 
                    // Khi nhập các số vào các input thì gọi sự kiện này
                    $scope.show_result = function()
                    {
                        // Nếu validate form đúng
                        if ($scope.calForm.$valid) {
                            $scope.styleresult = 'display:block';
                            $scope.result = {
                                phep_cong: parseInt($scope.so_thu_nhat) + parseInt($scope.so_thu_hai),
                                phep_tru: parseInt($scope.so_thu_nhat) - parseInt($scope.so_thu_hai),
                                phep_nhan: parseInt($scope.so_thu_nhat) * parseInt($scope.so_thu_hai),
                                phep_chia: parseInt($scope.so_thu_nhat) / parseInt($scope.so_thu_hai)
                            };
                        }
                        // nếu validate form sai thì ẩn result
                        else {
                            $scope.styleresult = 'display:none';
                        }
                    };
                });
        </script>
 
    </head>
    <body ng-app="MyForm">
        <form name="calForm" ng-controller="ExampleController">
            <h2>{{message.title}}</h2>
            <h5>{{message.num1}}:</h5>
            <input ng-model="so_thu_nhat" ng-required="true" ng-pattern="/^[0-9]+$/" class="my-input" ng-keyup="show_result()" />
            <h5>{{message.num2}}:</h5>
            <input ng-model="so_thu_hai" ng-required="true" ng-pattern="/^[0-9]+$/" class="my-input" ng-keyup="show_result()" />
            <div style='{{styleresult}}'>
                {{message.phep_cong}} {{result.phep_cong}}<br/>
                {{message.phep_tru}}  {{result.phep_tru}}<br/>
                {{message.phep_nhan}} {{result.phep_nhan}}<br/>
                {{message.phep_chia}} {{result.phep_chia}}
            </div>
        </form>
    </body>
</html>