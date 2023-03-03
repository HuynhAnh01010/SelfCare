(function() {
    'use strict';

    angular
        .module('loginappApp')
        .controller('RequestResetController', RequestResetController);

    RequestResetController.$inject = ['$timeout', 'Auth','$state'];

    function RequestResetController ($timeout, Auth,$state) {
        var vm = this;

        vm.userType = "USERNAME";
        vm.userPlaceHolder = 'User Name';

        function gscroll (){
            $(".g-scrolling-carousel .items").gScrollingCarousel();
        }
        gscroll();

        vm.requestReset = requestReset;
        vm.resetAccount = {};
        vm.success = null;

        function requestReset () {
            vm.resetAccount.userType = vm.userType;
            Auth.resetPasswordInit(vm.resetAccount).then(function (res) {
                $state.go('finishReset', {key: btoa(vm.resetAccount.username),type: btoa(vm.resetAccount.userType),refer:res.responseID});

            }).catch(function (response) {
                console.log("INIT RQ RESET PASSWORD ERROR: ", response);
                vm.dialogMessage = {
                    isShow: true,
                    isError: true,
                    message: response.data.message || response.data.errorDescription || "ERROR SYSTEM"
                };
            });
        }
    }
})();
