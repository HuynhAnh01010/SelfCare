
(function () {
    'use strict';

    angular
        .module('loginappApp')
        .controller('MobileController', MobileController);


    MobileController.$inject = ['$translate', '$timeout', 'Auth', '$state', 'deviceDetector', 'notifySv','FacetecMain'];

    function MobileController($translate, $timeout, Auth, $state, deviceDetector, notifySv,FacetecMain) {
        var vm = this;

        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;
        FacetecMain._init(function (initializedSuccessfully) {
            if(initializedSuccessfully){
                FacetecMain.onPhotoIDMatchPressedTest("abc");
            }

        });
    }
})();
