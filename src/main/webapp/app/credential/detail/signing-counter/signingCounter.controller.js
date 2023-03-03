(function () {
    'use strict';

    angular
            .module('loginappApp')
            .controller('sncController', sncController);

    sncController.$inject = ['TseService', 'CredentialService', '$stateParams', '$scope', 'storageService'];

    function sncController(TseService, CredentialService, $stateParams, $scope, storageService) {
        let vm = this;
        let val = '';

        getSigningProfile();

        function getSigningProfile() {
            TseService.systemGetSigningProfiles({
            }).then(function (res) {
                vm.SnProdata = res.data;
//                storageService.setData(vm.SnProdata);
                console.log(" RENEW: SIGNING PROFILE DATA: ", vm.SnProdata);
                vm.error = null;
                vm.success = 'OK';

            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }

        var array = [];

//        getCertificateAuthorities();
//        function getCertificateAuthorities() {
//            TseService.systemGetCertificateAuthorities().then(function (res) {
//                for (var i in res.data.certificateAuthorities) {
//                    if (res.data.certificateAuthorities[i].productionEnabled) {
//                        array.push(res.data.certificateAuthorities[i]);
//                    }
//                }
//                vm.CerAuth = array;
////                storageService.setData(vm.CerAuth);
//                console.log("CERTIFICATE AUTHORITIES: ", vm.CerAuth);
//                vm.error = null;
//                vm.success = 'OK';
//            }).catch(function () {
//                vm.success = null;
//                vm.error = 'ERROR';
//            });
//        }

        vm.cerDis = true;
        vm.certPN = [];
        var certArray = [];


        getCertificateProfileName();
        function getCertificateProfileName() {

            CredentialService.credentialInfo({id: $stateParams.id}).then(function (res) {

                vm.detail = res.data;
                console.log("RENEW DATA: ", vm.detail);
                storageService.setCertPN(vm.detail);

                let issuerCN = vm.detail.cert.issuerDN;

                issuerCN = issuerCN + ",TEMP=";

                val = issuerCN.match(/.*CN=(.*?),(TEMP|L|ST|C|E|O|OU|telephoneNumber|0.9.2342.19200300.100.1.1)=/);
                vm.issuerCN = val[1];
                console.log("ISSUER CN: ", vm.issuerCN);
                storageService.setCertA(vm.issuerCN);

                console.log("RENEW DATA2: ", storageService.getCertPN());

                vm.rqCerAuthProName = false;
                certArray.length = 0;
                vm.certPN.length = 0;
                let nameCert = vm.issuerCN;
                let nameCertPN = storageService.getCertPN();

                console.log("NAME CERT A: ", nameCert);
                console.log("NAME CERT PN: ", nameCertPN);

                TseService.systemGetCertificateProfiles({
                    name: nameCert
                }).then(function (resp) {
                    vm.cerDis = false;
                    vm.getData = nameCertPN;
                    console.log("GETDATA GETCERTPN: ", vm.getData);
                    if (vm.getData) {
                        vm.getType = vm.getData.cert.certificateProfile.type;
                        console.log("Click Choose CA: ", vm.getType);
                        vm.CerAuthProName = resp.data.profiles;

                        for (let i = 0; i < vm.CerAuthProName.length; i++) {
                            if (vm.CerAuthProName[i].type == vm.getType) {
                                certArray.push(vm.CerAuthProName[i]);
                            }
                        }

                        vm.certPN = certArray;

                        console.log("CERT PN: ", vm.certPN);
                    }


                }).catch(function () {
                    vm.success = null;
                    vm.error = 'ERROR';
                });

            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });


        }
        
        getPaymentProvider();

        function getPaymentProvider() {
            TseService.getPaymentProvider({

            }).then(function (res) {
                console.log("Payment PROVIDER: ", res.data);
                vm.getPMProvider = res.data;

//                storageService.setData(vm.getPMProvider);
            }).catch(function (ex) {
                console.log("EX: ", ex);
                vm.dialogMessage = {
                    isShow: true,
                    isError: true,
                    message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                };
            });
        }
        
        $scope.showPassword = false;
        $scope.toggleShowPassword = function () {
            $scope.showPassword = !$scope.showPassword;
        }
    }
})();