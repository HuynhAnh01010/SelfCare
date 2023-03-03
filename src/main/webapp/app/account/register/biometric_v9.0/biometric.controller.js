(function() {
    'use strict';

    angular
        .module('loginappApp')
        .controller('BiometricController', BiometricController);


    BiometricController.$inject = ['$translate', '$timeout', 'Auth','$state','deviceDetector','notifySv','FacetecMain'];

    function BiometricController ($translate, $timeout, Auth,$state,deviceDetector,notifySv,FacetecMain) {
        var vm = this;

        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;
        vm.login = function(){
        	$state.go('login');
        };
        FacetecMain._init(function (initializedSuccessfully) {
            console.log("initializedSuccessfully: ", initializedSuccessfully);
            vm.initializedSuccessfully = initializedSuccessfully;
            // $scope.$applyAsync();
        });
        // vm.register = register;
        vm.registerAccount = {};
        vm.success = null;

    }
})();
