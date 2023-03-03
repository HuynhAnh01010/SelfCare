(function () {
    'use strict';

    angular
        .module('loginappApp')
        .controller('VnPayResultController', VnPayResultController);


    VnPayResultController.$inject = ['$translate', '$timeout', '$stateParams','VnPayService', '$state', 'deviceDetector', 'notifySv'];

    function VnPayResultController($translate, $timeout,$stateParams, VnPayService, $state, deviceDetector, notifySv) {
        var vm = this;

        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;
        vm.login = function () {
            $state.go('login');
        };

        console.log("$stateParams: ",$stateParams);

        vm.vnpay_pay = vnpay_result;
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



        function vnpay_result() {

            VnPayService.result($stateParams).then(function (res) {
                console.log("res:: ",res);

            }).catch(function (response) {
                console.log("response: ",response);

            });
        }

        vnpay_result();
    }
})();
