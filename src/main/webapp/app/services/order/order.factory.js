(function () {
    'use strict';

    angular
            .module('loginappApp')
            .factory('OrderFactory', OrderFactory);

    OrderFactory.$inject = ['$resource'];

    function OrderFactory($resource) {
        return $resource('', {}, {
           'orderList' : {method: 'POST', url: 'api/order/list'},
           'orderDetail': {method: 'POST', url: 'api/orders/info'}
        });
    }
})();