(function () {
    'use strict';

    angular
        .module('loginappApp')
        .controller('RegisterCertificateDetailController', RegisterCertificateDetailController);


    RegisterCertificateDetailController.$inject = ['$translate', '$timeout', 'Auth', '$state', 'deviceDetector', 'notifySv',
        'registerCertificateObject','RegisterCertificateService'];

    function RegisterCertificateDetailController($translate, $timeout, Auth, $state, deviceDetector, notifySv,
                                                 registerCertificateObject,RegisterCertificateService) {
        var vm = this;

        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;
        // vm.register = register;
        // vm.biometricIdentification =  biometricIdentification;
        vm.registerAccount = {};
        vm.success = null;
        vm.registerCertificateObject = registerCertificateObject;
        vm.createCertificate = createCertificate;

        function _init() {



            RegisterCertificateService.statesOrProvinces({name: vm.registerCertificateObject.certDetails.country}, function (res) {
                if (res.code == 0) {
                    vm.provinces = res.data.provinces;
                }
            });
        }

        _init();

        if (!vm.registerCertificateObject.certificateProfile || !vm.registerCertificateObject.email
            || !vm.registerCertificateObject.phone) {
            $state.go("registerCertificate");
        }

        function createCertificate(){
            vm.error = null;
            console.log("certificate: ",vm.registerCertificateObject);

            RegisterCertificateService.createCertificate( vm.registerCertificateObject).then(function (res) {
                if(res.code == 0){
                    if(res.data.error == 0){
                        notifySv.notify("You are <b>create certificate</b> successfully");
                        $state.go('login');
                        return;
                    }else{
                        vm.error = true;
                        vm.errorMsg = res.data.errorDescription;
                    }
                }else{
                    vm.error = true;
                    vm.errorMsg = res.message;
                }


            }).catch(function (response) {
                vm.error = true;
                vm.errorMsg = "Error system";
            });
        }


    }
})();
