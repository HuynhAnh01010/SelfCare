(function() {
    'use strict';

    angular
        .module('loginappApp')
        .controller('Policy2Controller', Policy2Controller);


    Policy2Controller.$inject = ['$translate', '$timeout', 'Auth','$state','deviceDetector','notifySv',"policy"];

    function Policy2Controller ($translate, $timeout, Auth,$state,deviceDetector,notifySv,policy) {
        var vm = this;

        if(!(policy.hasOwnProperty("acceptPolicy1") && policy.acceptPolicy1)){
            $state.go("policy1");
        }

        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;

        vm.acceptPolicy2 = acceptPolicy2;
        vm.back = back;
        vm.registerAccount = {};
        vm.success = null;

        function back() {
            $state.go("policy1")
        }

        vm.isScroll = true;

        function acceptPolicy2(){
            policy.acceptPolicy2 = true;
            // if(deviceDetector.isDesktop()){
            //     notifySv.warning("Biometric Identification only support mobile device");
            //    return;
            // }
            $state.go("authenticateEmailPhone")
        }

    }
})();
