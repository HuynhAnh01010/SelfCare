(function() {
    'use strict';

    angular
        .module('loginappApp')
        .controller('AuthenticateEmailPhoneController', AuthenticateEmailPhoneController);


    AuthenticateEmailPhoneController.$inject = ['$translate', '$timeout', 'Auth','$state','deviceDetector','notifySv','policy'];

    function AuthenticateEmailPhoneController ($translate, $timeout, Auth,$state,deviceDetector,notifySv,policy) {
        var vm = this;

        // if(!(policy.hasOwnProperty("acceptPolicy2") && policy.acceptPolicy2)){
        //     $state.go("policy2");
        // }

        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;

        vm.gotoReachEmailPhone = gotoReachEmailPhone;

        vm.back = back;
        vm.registerAccount = {};
        vm.success = null;

        function back(){
            $state.go("policy2");
        }

        function gotoReachEmailPhone(){
            policy.authenticateEmailPhone = true;
            $state.go("reachEmailPhone")
        }
    }
})();
