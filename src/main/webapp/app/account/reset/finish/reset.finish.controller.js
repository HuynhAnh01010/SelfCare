(function () {
    'use strict';

    angular
            .module('loginappApp')
            .controller('ResetFinishController', ResetFinishController);

    ResetFinishController.$inject = ['$stateParams', '$timeout', 'Auth', '$state', 'registerCertificateObject', 'TIMEOUT_HIDE_MODAL'];

    function ResetFinishController($stateParams, $timeout, Auth, $state, registerCertificateObject, TIMEOUT_HIDE_MODAL) {
        var vm = this;
        vm.registerCertificateObject = registerCertificateObject;
        vm.resetAccount = {};
        if (!$stateParams.key) {
            $state.go('resetPass');
        } else {
            try {
                vm.resetAccount.username = atob($stateParams.key);
                // let username = atob($stateParams.key);
            } catch (ex) {
                $state.go('resetPass');
            }
        }
        if (!$stateParams.type) {
            $state.go('resetPass');
        } else {
            try {
                vm.resetAccount.userType = atob($stateParams.type);
                // let username = atob($stateParams.key);
            } catch (ex) {
                $state.go('resetPass');
            }
        }
        if (!$stateParams.refer) {
            $state.go('resetPass');
        } else {
            vm.resetAccount.requestId = $stateParams.refer;
        }

        vm.confirmPassword = null;
        vm.doNotMatch = null;
        vm.error = null;
        vm.finishReset = finishReset;
        vm.showDialog = false;

        vm.success = null;
        function finishReset() {
            vm.doNotMatch = null;
            vm.error = null;
            if (vm.resetAccount.password !== vm.resetAccount.confirmPassword) {
                vm.dialogMessage = {
                    isShow: true,
                    isError: true,
                    message: 'The password and its confirmation do not match'
                };
            } else {
                Auth.resetPasswordFinish(vm.resetAccount).then(function (res) {
                    console.log("RESET PASS CF: ", res);
                    vm.dialogMessage = {
                        isShow: true,
                        isError: false,
                        message: res.errorDescription
                    };
                    
                    $timeout(function () {
                        $state.go('login');
                    }, TIMEOUT_HIDE_MODAL);
                    
                }).catch(function (res) {
                    console.log("Finish Err: ", res);
                    vm.dialogMessage = {
                        isShow: true,
                        isError: true,
                        message: res.data.errorDescription
                    };
                });
            }
        }
    }
})();
