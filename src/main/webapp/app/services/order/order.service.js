(function () {
    'use strict';
    
    angular
            .module('loginappApp')
            .factory('OrderService', OrderService);
    
    OrderService.$inject = ['OrderFactory'];
    
    function OrderService(OrderFactory) {
        var service = {
            orderList: orderList,
            orderDetail: orderDetail
        };
        return service;
        
        function orderList(key, callback) {
            var cb = callback || angular.noop;

            return OrderFactory.orderList(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }
        
        function orderDetail(key, callback) {
            var cb = callback || angular.noop;

            return OrderFactory.orderDetail(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }
    }
})();