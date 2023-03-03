(function () {
    'use strict';

    angular
        .module('loginappApp')
        .controller('VnPayController', VnPayController);


    VnPayController.$inject = ['$translate', '$timeout', 'VnPayService', '$state', 'deviceDetector', 'notifySv'];

    function VnPayController($translate, $timeout, VnPayService, $state, deviceDetector, notifySv) {
        var vm = this;

        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;
        vm.login = function () {
            $state.go('login');
        };
        vm.vnpay_pay = vnpay_pay;
        vm.biometricIdentification = biometricIdentification;
        vm.registerAccount = {};
        vm.success = null;

        var latestEnrollmentIdentifier = "";
        var latestSuccessfulServerResult = null;
        var latestSessionResult = null;
        var latestIDScanResult = null;
        var latestProcessor;
        // notifySv.notify("You are <b>Create Account</b> successfully");
        function biometricIdentification() {
            $state.go("biometric")
        }



        function vnpay_pay() {
            let data= {
                ordertype: "billpayment",
                amount:10000,
                vnpOrderInfo: "Thanh toan don hang test",
                bankcode: "NCB",
                language: "vi",

            };

            VnPayService.pay(data).then(function (res) {
                console.log("res:: ",res);
                if (res.data.code === '00') {
                    if (window.vnpay) {
                        vnpay.open({width: 768, height: 600, url: res.data.data});
                    } else {
                        location.href = res.data.data;
                    }
                    return false;
                } else {
                   console.log("res: ",res);
                }
            }).catch(function (response) {
                console.log("response: ",response);

            });
        }

    }
})();
