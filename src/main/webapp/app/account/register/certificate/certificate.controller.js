(function () {
    'use strict';

    angular
        .module('loginappApp')
        .controller('RegisterCertificateController', RegisterCertificateController);


    RegisterCertificateController.$inject = ['$translate', '$timeout', 'Auth', '$state', 'deviceDetector', 'notifySv',
        'registerCertificateObject', 'RegisterCertificateService', 'sharedModes', 'authModes'];

    function RegisterCertificateController($translate, $timeout, Auth, $state, deviceDetector, notifySv,
                                           registerCertificateObject, RegisterCertificateService, sharedModes, authModes) {
        var vm = this;

        vm.changeCertificateAuthority = changeCertificateAuthority;
        vm.sharedModes = sharedModes;
        vm.authModes = authModes;
        // $('#multiSign').tooltip({'trigger':'focus', 'title': 'Password tooltip'});
        // $('[data-toggle="tooltip"]').tooltip({'trigger':'focus', 'title': 'Password tooltip'});
        console.log(sharedModes);

        vm.registerCertificateObject = registerCertificateObject;

        // vm.registerCertificateObject.

        vm.certificateAuthorities = [];
        vm.certificateProfiles = [];


        function _init() {
            RegisterCertificateService.certificateAuthorities(function (res) {
                if (res.code == 0) {
                    vm.certificateAuthorities = res.data.certificateAuthorities;
                }
            });

            RegisterCertificateService.signingProfiles(function (res) {
                if (res.code == 0) {
                    vm.profiles = res.data.profiles;
                }
            });
        }

        function changeCertificateAuthority() {
            console.log("change");
            vm.registerCertificateObject.certificateProfile = undefined;
            if (vm.registerCertificateObject.certificateAuthority) {
                console.log("change: ", vm.registerCertificateObject.certificateAuthority);
                RegisterCertificateService.certificateProfiles({name: vm.registerCertificateObject.certificateAuthority}, function (res) {
                    if (res.code == 0) {
                        vm.certificateProfiles = res.data.profiles;
                    }
                });
            }


        }

        _init();


    }
})();
