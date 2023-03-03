(function () {
    'use strict';

    angular
        .module('loginappApp')
        .controller('BiometricController', BiometricController);


    BiometricController.$inject = ['$translate', '$timeout', 'Auth', '$state', 'deviceDetector', 'registerCertificateObject'];

    function BiometricController($translate, $timeout, Auth, $state, deviceDetector, registerCertificateObject) {
        var vm = this;

        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;
        // vm.login = function () {
        //     $state.go('login');
        // };

        vm.registerAccount = {};
        vm.success = null;

    }
})();
