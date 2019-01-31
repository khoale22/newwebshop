//var app = angular.module('app', ['ui.select']);
angular.module('app', ['ui.select']).controller("myCtrl", function () {
    var self = this;
    self.values = [{
        'key': 22,
        'value2': 'Kevin'
    }, {
        'key': 24,
        'value2': 'Fiona'
    }];

    // self.values.unshift({'key': '', 'value2': 'Static'});
    
    self.handleChangeSalesChannel = function () {
        if(self.salesChannel.value2 == 'Kevin'){
            self.showIfNotNullKevin = true;
        }
        if(self.salesChannel.value2 == 'Fiona'){
            self.showIfNotNullFiona = true;
        }
        
    }


    ////@22222
    self.numbers = [3, 5, 7];
});