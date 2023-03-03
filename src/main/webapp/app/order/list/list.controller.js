(function () {
    'use strict';

    angular
            .module('loginappApp')
            .controller('orderController', orderController);

    orderController.$inject = ['$scope', '$state', '$stateParams', 'OrderService'];

    function orderController($scope, $state, $stateParams, OrderService) {
        let vm = this;

        vm.page = 1;
        vm.size = 5;

        vm.orderList = [];
        vm.data = [];

        vm.nextPage = function () {
            vm.page = vm.page + 1;
            getOrderList();
        };
        vm.prevPage = function () {
            if (vm.page > 1) {
                $state.go('.', {page: vm.page - 1});
            }
        };

        getOrderList();
        function getOrderList() {
            OrderService.orderList({
                page: vm.page,
                size: vm.size
            }).then(function (res) {
                console.log("ORDER LIST: ", res);
                vm.orderList = vm.orderList.concat(res.data.orders);
                vm.data = res.data;

            }).catch(function (ex) {
                console.log("EX ORDER: ", ex);
            });
        }
    }
})();