//
//(function () {
//    'use strict';
//
//    angular
//        .module('loginappApp')
//        .controller('RegisterController', RegisterController);
//
//
//    RegisterController.$inject = ['$translate', '$timeout', 'Auth', '$state', 'deviceDetector', 'notifySv','FacetecMain'];
//
//    function RegisterController($translate, $timeout, Auth, $state, deviceDetector, notifySv,FacetecMain) {
//        var vm = this;
//
//        vm.doNotMatch = null;
//        vm.error = null;
//        vm.errorUserExists = null;
//        vm.login = function () {
//            $state.go('login');
//        };
//        // vm.register = register;
//        vm.biometricIdentification = biometricIdentification;
//        vm.registerAccount = {};
//        vm.success = null;
//
//        var latestEnrollmentIdentifier = "";
//        var latestSuccessfulServerResult = null;
//        var latestSessionResult = null;
//        var latestIDScanResult = null;
//        var latestProcessor;
//
//        // notifySv.notify("You are <b>Create Account</b> successfully");
//        function biometricIdentification() {
//
//            $state.go("biometric")
//        }
//
//        //
//        // function register() {
//        //     if (vm.registerAccount.password !== vm.confirmPassword) {
//        //         vm.doNotMatch = 'ERROR';
//        //     } else {
//        //         vm.registerAccount.langKey = $translate.use();
//        //         vm.doNotMatch = null;
//        //         vm.error = null;
//        //         vm.errorUserExists = null;
//        //         vm.errorEmailExists = null;
//        //
//        //         Auth.createAccount(vm.registerAccount).then(function () {
//        //             vm.success = 'OK';
//        //         }).catch(function (response) {
//        //             vm.success = null;
//        //             if (response.status === 400 && response.data === 'login already in use') {
//        //                 vm.errorUserExists = 'ERROR';
//        //             } else if (response.status === 400 && response.data === 'email address already in use') {
//        //                 vm.errorEmailExists = 'ERROR';
//        //             } else {
//        //                 vm.error = 'ERROR';
//        //             }
//        //         });
//        //     }
//        // }
//    }
//})();
