(function () {
    'use strict';

    angular
            .module('loginappApp')
            .controller('DetailOrderController', DetailOrderController);

    DetailOrderController.$inject = ['$scope', '$state', '$stateParams', 'OrderService', 'TseService', '$window', 'storageService', '$timeout'];

    function DetailOrderController($scope, $state, $stateParams, OrderService, TseService, $window, storageService, $timeout) {
        var vm = this;


        getOrderDetail();
        function getOrderDetail() {
            OrderService.orderDetail({
                orderUUID: $stateParams.id
            }).then(function (res) {
                console.log("ORDER DETAIL: ", res.data.order);
                vm.orderDetail = res.data.order;

            }).catch(function (ex) {
//               console.log("EX ORDER DETAIL: ", ex); 
            });
        }

        vm.getPayment = false;
        vm.getPaymentt = false;
        vm.getPayment1 = false;

        vm.getPaymentProvider = function () {
            $timeout(function () {
                vm.getPayment = true;
            }, 0);


            TseService.getPaymentProvider({

            }).then(function (res) {

                console.log("Payment PROVIDER: ", res.data);

                vm.getPMProvider = res.data;
                storageService.setData(vm.getPMProvider);

                $timeout(function () {
                    vm.getPaymentt = true;
                    vm.getPayment1 = true;
                }, 500);

            }).catch(function (ex) {
//                console.log("EX: ", ex);

            });
        }

        vm.cancel = function () {

            vm.getPaymentt = false;

            $timeout(function () {
                vm.getPayment1 = false;
                vm.getPayment = false;
            }, 600);



        }

        vm.orderCheckout = function () {
            TseService.ordersCheckout({
                orderUUID: vm.orderDetail.orderUUID,
                paymentProvider: vm.request.item.name,
//                returnUrl: ('https://' + $window.location.host + '/selfcare/response'),
                returnUrl: ('https://' + $window.location.host + '/SelfCare/response'),

                ipnUrl: vm.request.item.ipnUrl,

            }).then(function (res) {
                vm.odCheckout = res.data;

//                console.log("CHEKCOUT: ", res.data);
                storageService.setData(vm.odCheckout);
                $window.location.href = vm.odCheckout.paymentUrl;
            }).catch(function (ex) {
//                console.log("EX ORDER CHECKOUT: ", ex);
            });
        }
    }
})();