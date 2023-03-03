(function () {
    'use strict';
    angular
            .module('loginappApp')
            .controller('UpgradeController', UpgradeController);

    UpgradeController.$inject = ['$scope', '$state', '$stateParams', 'storageService', 'CredentialService', 'TseService', 'sharedModes', '$window'];

    function UpgradeController($scope, $state, $stateParams, storageService, CredentialService, TseService, sharedModes, $window) {
        let vm = this;

        vm.sharedModes = sharedModes;

//        if ($stateParams.id == null || $stateParams.id == "") {
//            $state.go("credential.list");
//        }
        vm.id = $stateParams.id;

        vm.showOldPassword = false;
        vm.toggleShowOldPassword = function () {
            vm.showOldPassword = !vm.showOldPassword;
        }


        vm.cancel = function () {
            $state.go('credential.list.detail');
        }

//        vm.response = function () {
//            $state.go('credential.upgrade.respone');
//        }

        getCredetialInfo();

        function getCredetialInfo() {
            CredentialService.credentialInfo({id: $stateParams.id}).then(function (res) {

                vm.detail = res.data;
                storageService.setData(vm.detail);
                console.log("UPGRADE DATA: ", vm.detail);
                vm.error = null;
                vm.success = 'OK';
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }
        vm.SnProdata = [];
        getSigningProfile();

        function getSigningProfile() {
            TseService.systemGetSigningProfiles({
            }).then(function (res) {

                for (let i = 0; i < res.profiles.length; i++) {
                    if (res.profiles[i].name != 'CUSTOM_PROFILE') {
                        let valueSN = res.profiles[i];
                        vm.SnProdata.push(valueSN);
                    }
                }
                storageService.setData(vm.SnProdata);
//                console.log("SIGNING PROFILE DATA: ", vm.SnProdata);
                vm.error = null;
                vm.success = 'OK';

            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }

        var array = [];

        getCertificateAuthorities();
        function getCertificateAuthorities() {
            TseService.systemGetCertificateAuthorities().then(function (res) {
                for (var i in res.certificateAuthorities) {
                    if (res.certificateAuthorities[i].productionEnabled) {
                        array.push(res.certificateAuthorities[i]);
//                        console.log("Name: ", res.data.certificateAuthorities[i].name);
//                        getCertificateProfiles();
//                        function getCertificateProfiles() {
//                            TseService.systemGetCertificateProfiles({
//                                name: res.data.certificateAuthorities[i].name
//                            }).then(function (resp) {
//
//                                vm.CerAuthProName1 = resp.data;
//                                storageService.setData(vm.CerAuthProName1);
//                                console.log("CERTIFICATE AUTHORITIES NAME 1: ", vm.CerAuthProName1);
//                            }).catch(function () {
//                                vm.success = null;
//                                vm.error = 'ERROR';
//                            });
//                        }
                    }
                }
                vm.CerAuth = array;
                storageService.setData(vm.CerAuth);
                console.log("CERTIFICATE AUTHORITIES: ", vm.CerAuth);
                vm.error = null;
                vm.success = 'OK';
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }

        vm.cerDis = true;

        vm.getCertificateProfileName = function () {
            vm.rqCerAuthProName = false;

//                console.log("getCertificateProfileName: ", vm.request.CerAuthName.name);
            TseService.systemGetCertificateProfiles({
                name: vm.request.CerAuthName.name
            }).then(function (resp) {
                vm.cerDis = false;
                vm.CerAuthProName = resp;
                storageService.setData(vm.CerAuthProName);
                console.log("CERTIFICATE AUTHORITIES NAME: ", vm.CerAuthProName);
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }




        vm.initRq = false;

        vm.confirmRequestUpgrade = function () {
            consolog.info("CerAuthProName.name: ", vm.request.CerAuthProName.name);
            consolog.info("CerAuthProName: ", vm.request.CerAuthProName);
            TseService.credentialsUpgrade({

                credentialID: $stateParams.id,
                certificateProfile: vm.request.CerAuthProName.name,
                signingProfile: vm.request.signingProfile,
//                sharedMode: vm.request.sharedMode.value
                authorizeCode: vm.request.authorizeCode

            }).then(function (res) {
                console.log("TRAVE: ", res);
                ctrl.step = 2;
                vm.dialogMessage2 = {
                    isShow: true,
                    isError: false,
                    message: res.data.errorDescription
                };

                vm.successIssue = res.data.credentialID;

                $timeout(function () {
                    console.log("REDIRECT URL: ", url);
                    let url = "/SelfCare/#/credential/list/detail/" + vm.successIssue;
                    $window.location.href = url;
                }, TIMEOUT_HIDE_MODAL);


            }).catch(function (ex) {
                if (ex.data.data.error) {
                    if (ex.data.data.error == '3063') {
                        vm.initRq = true;
                        console.log("ORDER URL: ", ex.data);
                        vm.rqOrderUUID = ex.data.data;
                        console.log("OrderUUID: ", vm.rqOrderUUID);
                        storageService.setData(vm.rqOrderUUID);
                        vm.disabled = true;
                    } else {
                        vm.dialogMessage = {
                            isShow: true,
                            isError: !(ex.code == 0 && ex.data.error == 0),
                            message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                        };
                    }
                }
                if (ex.data.error) {
                    if (ex.data.error == '3063') {
                        vm.initRq = true;
                        console.log("ORDER URL: ", ex.data);
                        vm.rqOrderUUID = ex.data;
                        console.log("OrderUUID: ", vm.rqOrderUUID);
                        storageService.setData(vm.rqOrderUUID);
                        vm.disabled = true;
                    } else {
                        vm.dialogMessage = {
                            isShow: true,
                            isError: !(ex.code == 0 && ex.data.error == 0),
                            message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                        };
                    }
                }

            });
        }

        getPaymentProvider();

        function getPaymentProvider() {
            TseService.getPaymentProvider({

            }).then(function (res) {
                console.log("Payment PROVIDER: ", res.data);
                vm.getPMProvider = res.data;

                storageService.setData(vm.getPMProvider);
            }).catch(function (ex) {
                console.log("EX: ", ex);
                vm.dialogMessage = {
                    isShow: true,
                    isError: true,
                    message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                };
            });
        }



        vm.orderCheckout = function () {
            TseService.ordersCheckout({
                orderUUID: vm.rqOrderUUID.orderUUID,
                paymentProvider: vm.request.item.name,
//                bankCode: vm.request.item.code,
//                returnUrl: ('https://' + $window.location.host + '/selfcare/response'),
                returnUrl: ('https://' + $window.location.host + '/SelfCare/response'),

                ipnUrl: vm.request.item.ipnUrl,

//                returnUrl: ($window.location.href + '/response')
            }).then(function (res) {
                vm.odCheckout = res.data;

                console.log("CHEKCOUT: ", res.data);
                storageService.setData(vm.odCheckout);
                $window.location.href = vm.odCheckout.paymentUrl;
            }).catch(function (ex) {
                console.log("EX ORDER CHECKOUT: ", ex);
            });
        }

        vm.rqCerAuthProName = false;

        vm.chooseCertiProfile = function () {
            if (vm.request.CerAuthProName) {
                vm.rqCerAuthProName = true;
                vm.certDetail = vm.request.CerAuthProName;
                console.log("CERTIFI DETAIL: ", vm.certDetail);
            }

        }

        vm.fnChooseAM = false;
        vm.rqChooseSC = function () {
            let name = vm.request.signingProfile;
            vm.getChooseAM = '';

            TseService.systemGetSigningProfiles({
            }).then(function (res) {
                console.log("GET Choose: ", res);
                vm.setChooseAM = res;
                console.log("GET CHOOSE: ", vm.setChooseAM);
                storageService.setData(vm.setChooseAM);

                for (let i in res.profiles) {
                    if (res.profiles[i].name == name) {
                        vm.getChooseAM = res.profiles[i];
                    }
                }


            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });

            console.log("CHOOSE SC: ", name);
            if (name == 'UNLIMITED') {
                vm.fnChooseAM = false;
            } else if (name == 'CUSTOM_PROFILE') {
                vm.fnChooseAM = false;
            } else {
                vm.fnChooseAM = true;
            }
        }

    }
})();