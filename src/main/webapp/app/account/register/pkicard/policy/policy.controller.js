(function() {
    'use strict';

    angular
        .module('loginappApp')
        .controller('PkiCardPolicyController', PkiCardPolicyController);


    PkiCardPolicyController.$inject = ['$translate', '$timeout', 'Auth','$state','deviceDetector','notifySv','policy'];

    function PkiCardPolicyController ($translate, $timeout, Auth,$state,deviceDetector,notifySv,policy) {
        var vm = this;



        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;
        vm.isScroll = true;
        vm.agree = agree;
        vm.back = back;
        vm.registerAccount = {};
        vm.success = null;

        function back(){
            $state.go("register");
        }

        function agree(){
            policy.acceptPolicy1 = true;
            // if(deviceDetector.isDesktop()){
            //     notifySv.warning("Biometric Identification only support mobile device");
            //    return;
            // }
            $state.go("pkicard.policy.choosepin")
        }

        function register () {
            if (vm.registerAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = 'ERROR';
            } else {
                vm.registerAccount.langKey = $translate.use();
                vm.doNotMatch = null;
                vm.error = null;
                vm.errorUserExists = null;
                vm.errorEmailExists = null;

                Auth.createAccount(vm.registerAccount).then(function () {
                    vm.success = 'OK';
                }).catch(function (response) {
                    vm.success = null;
                    if (response.status === 400 && response.data === 'login already in use') {
                        vm.errorUserExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'email address already in use') {
                        vm.errorEmailExists = 'ERROR';
                    } else {
                        vm.error = 'ERROR';
                    }
                });
            }
        }
    }
})();
