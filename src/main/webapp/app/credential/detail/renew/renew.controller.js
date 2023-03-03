(function () {
    'use strict';
    angular.module('loginappApp')
            .controller('renewController', renewController);
    renewController.$inject = ['$scope', '$state', '$stateParams', 'storageService', 'CredentialService', 'TseService', '$window', '$filter', '$timeout', 'TIMEOUT_HIDE_MODAL'];
    function renewController($scope, $state, $stateParams, storageService, CredentialService, TseService, $window, $filter, $timeout, TIMEOUT_HIDE_MODAL) {
        let vm = this;
        vm.cancel = function () {
            $state.go('credential.list.detail');
//            vm.rqPayment2 = false;
//            vm.disabled = false;
//            vm.reqCheck3 = false;
//            vm.disabled2 = false;
//            vm.reqConf2 = false;
        }


//        ///////////////////////////////////////////////////////////////////////
        vm.SnProdata2 = [];
        getSigningPro2();
        function getSigningPro2() {
            TseService.systemGetSigningProfiles({
            }).then(function (res) {
//                console.log("Số lần ký: ", res);

                for (let i = 0; i < res.profiles.length; i++) {
                    if (res.profiles[i].name != 'CUSTOM_PROFILE') {
                        let valueSN = res.profiles[i];
                        vm.SnProdata2.push(valueSN);
                    }
                }
//                console.log("RES: ", vm.SnProdata2);

                vm.error = null;
                vm.success = 'OK';
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }

        getPaymentProvider2();
        function getPaymentProvider2() {
            TseService.getPaymentProvider({

            }).then(function (res) {
//                console.log("Payment PROVIDER: ", res.data);
                vm.getPMProvider2 = res.data;
//                storageService.setData(vm.getPMProvider);
            }).catch(function (ex) {
//                console.log("EX: ", ex);
                vm.dialogMessage = {
                    isShow: true,
                    isError: true,
                    message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                };
            });
        }

        let val = '';
        vm.step = 1;
        getCredetialInfo();
        function getCredetialInfo() {
            CredentialService.credentialInfo({id: $stateParams.id}).then(function (res) {
                vm.detail2 = res.data;
//                console.log("change RN data: ", vm.detail2);
                let issuerCN = vm.detail2.cert.certificateAuthority.name;
                storageService.setDetail(vm.detail2.cert.certificateProfile.type);
//                issuerCN = issuerCN + ",TEMP=";
//                val = issuerCN.match(/.*CN=(.*?),(TEMP|L|ST|C|E|O|OU|telephoneNumber|0.9.2342.19200300.100.1.1)=/);

                let caType = vm.detail2.cert.certificateProfile.type;
//                console.log("caType: ", caType);
//
//                console.log("VALUE ISSUER: ", issuerCN);
//                vm.issuerCN2 = val[1];
//                console.log("ISSUER CN: ", vm.issuerCN2);
//                storageService.setCertPN(vm.issuerCN2);
                vm.request.cerAuthName2 = issuerCN;
                TseService.systemGetCertificateProfiles({
                    name: issuerCN
                }).then(function (res) {
//                    console.log("COUNT PN: ", res);
                    if (res.profiles) {
                        let count = res.profiles;
                        let arraylist = [];
                        for (let i = 0; i < count.length; i++) {
                            if (count[i].type == caType) {
                                arraylist.push(count[i]);
                            }
                        }

                        vm.resCertPN2 = arraylist;
//                        console.log("res12 CertPN2: ", vm.resCertPN2);
                        storageService.setCertArr(vm.resCertPN2);
                        vm.request.CerAuthProName2 = vm.resCertPN2[0].name;
                    } else if (res.data.profiles) {
                        let count = res.data.profiles;
                        let arraylist = [];
                        for (let i = 0; i < count.length; i++) {
                            if (count[i].type == caType) {
                                arraylist.push(count[i]);
                            }
                        }

                        vm.resCertPN2 = arraylist;
//                        console.log("res12 CertPN2: ", vm.resCertPN2);
                        storageService.setCertArr(vm.resCertPN2);
                        vm.request.CerAuthProName2 = vm.resCertPN2[0].name;
                    }

                }).catch(function () {

                })


            }).catch(function (error) {

            })
        }


        var array2 = [];
        getCertificateAuthorities2();
        function getCertificateAuthorities2() {
            TseService.systemGetCertificateAuthorities().then(function (res) {
//                console.log("getCertificateAuthorities2: ", res);

                if (res.certificateAuthorities != 'underfind') {
                    for (var i in res.certificateAuthorities) {
                        if (res.certificateAuthorities[i].productionEnabled) {
                            array2.push(res.certificateAuthorities[i]);
                        }
                    }
                    vm.CerAuth2 = array2;
//                    console.log("CERTAUTH 2: ", vm.CerAuth2);
                    vm.error = null;
                    vm.success = 'OK';
                } else if (res.data.certificateAuthorities != 'underfind') {
                    for (var i in res.data.certificateAuthorities) {
                        if (res.data.certificateAuthorities[i].productionEnabled) {
                            array2.push(res.data.certificateAuthorities[i]);
                        }
                    }
                    vm.CerAuth2 = array2;
//                    console.log("CERTAUTH 2: ", vm.CerAuth2);
                    vm.error = null;
                    vm.success = 'OK';
                }


            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }



        vm.getCertificateProfileName2 = function () {
//            console.log("CerAuthName2: ", vm.request.cerAuthName2);
            TseService.systemGetCertificateProfiles({
                name: vm.request.cerAuthName2
            }).then(function (res) {

//                console.log("RES DATA: ", res);
                var arrayCAPN = [];
                let count = res.profiles;
//                console.log("Count data: ", count);

                let caType = storageService.getDetail();
//                console.log("caType Lick: ", caType);

                for (let i = 0; i < count.length; i++) {
                    if (count[i].type == caType) {

                        arrayCAPN.push(count[i]);
                    }
                }

                storageService.setCertArr(arrayCAPN);
                vm.step = 2;
                vm.resCertPN2 = arrayCAPN;
//                console.log("Array CAPN: ", arrayCAPN);
                vm.request.CerAuthProName2 = vm.resCertPN2[0].name;
                vm.certDetail2 = arrayCAPN[0];
            }).catch(function () {

            })
        }

        vm.rqCerAuthProName2 = false;
        vm.chooseCertiProfile2 = function () {
            vm.step = 2;
            vm.rqCerAuthProName2 = true;
            let count = storageService.getCertArr();
//            console.log("COunt: ", count);

            for (let i = 0; i < count.length; i++) {
                if (vm.request.CerAuthProName2 == count[i].name) {
                    vm.certDetail2 = count[i];
                }
            }

        }

        vm.ifSigningProfileCustomer = false;
        vm.fnChooseAM2 = false;
        vm.rqChooseSC2 = function () {
            let name = vm.request.signingProfile2;
            vm.getChooseAM2 = '';
            TseService.systemGetSigningProfiles({
            }).then(function (res) {
                vm.setChooseAM2 = res.data;
                storageService.setData(vm.setChooseAM2);
//                console.log("GET CHOOSE2: ", vm.setChooseAM2);
                for (let i in res.profiles) {
                    if (res.profiles[i].name == name) {
                        vm.getChooseAM2 = res.profiles[i];
                    }
                }


            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
//            console.log("CHOOSE SC: ", name);
            if (name == 'UNLIMITED') {
                vm.fnChooseAM2 = false;
                vm.ifSigningProfileCustomer = false;
            } else if (name == 'CUSTOM_PROFILE') {
                vm.fnChooseAM2 = false;
                vm.ifSigningProfileCustomer = true;
            } else {
                vm.fnChooseAM2 = true;
                vm.ifSigningProfileCustomer = false;
            }
        }

        vm.reqConf2 = false;
//        console.log("VM>REQCONF2: ", vm.reqConf2);

        vm.confirmRequestUpgrade2 = function () {
            vm.reqCheck3 = false;
            vm.reqConf2 = false;
//            console.log("certificateProfile.name: ", vm.request.CerAuthProName2.name);
//            console.log("certificateProfile: ", vm.request.CerAuthProName2);

//            $timeout(function () {
//                vm.rqPayment2 = true;
//                vm.disabled = true;
//                vm.reqCheck3 = true;
//                vm.disabled2 = true;
//
//                $timeout(function () {
//                    vm.reqConf2 = true;
//                }, 684);
//            }, 100);


            TseService.credentialsRenew({
                credentialID: $stateParams.id,
                certificateProfile: vm.request.CerAuthProName2,
                signingProfile: vm.request.signingProfile2,
                signingProfileValue: vm.request.signingProfileValue,
                authorizeCode: vm.request.authorizeCode2
            }).then(function (res) {
                console.log("TRAVE 2: ", res);
                vm.disabled2 = true;
                vm.dialogMessage2 = {
                    isShow: true,
                    isError: false,
                    message: res.data.errorDescription
                };

                vm.successIssue = res.data.credentialID;

                $timeout(function () {
//                    console.log("REDIRECT URL: ", url);
                    let url = "/SelfCare/#/credential/list/detail/" + vm.successIssue;
                    $window.location.href = url;
                }, TIMEOUT_HIDE_MODAL);

            }).catch(function (exc) {
                console.log("ERROR TRAVE 2: ", exc);
                if (exc.data.error) {
                    if (exc.data.error == '3063') {
                        vm.rqOrderUUID2 = exc.data.data;
                        $timeout(function () {
                            vm.rqPayment2 = true;
                            vm.disabled = true;
                            vm.reqCheck3 = true;
                            vm.disabled2 = true;
                            $timeout(function () {
                                vm.reqConf2 = true;
                            }, 662);
                        }, 100);


                    } else {
                        vm.dialogMessage2 = {
                            isShow: true,
                            isError: !(exc.code == 0 && exc.data.error == 0),
                            message: typeof exc === 'string' ? exc : (typeof exc === 'object' ? exc.data.message : '401')
                        };
                    }
                }
                if (exc.data.data.error) {
                    if (exc.data.data.error == '3063') {
                        vm.rqOrderUUID2 = exc.data.data;
                        $timeout(function () {
                            vm.rqPayment2 = true;
                            vm.disabled = true;
                            vm.reqCheck3 = true;
                            vm.disabled2 = true;
                            $timeout(function () {
                                vm.reqConf2 = true;
                            }, 662);
                        }, 100);


                    } else {
                        vm.dialogMessage2 = {
                            isShow: true,
                            isError: !(exc.code == 0 && exc.data.data.error == 0),
                            message: typeof exc === 'string' ? exc : (typeof exc === 'object' ? exc.data.data.message : '401')
                        };
                    }
                }
            });
        }

        vm.reqInit2 = false;
        vm.rqPayment2 = false;
        vm.reqConf2 = false;
        vm.reqCheck3 = false;
//        vm.initRequest2 = function () {
//            vm.reqInit2 = true;
//            $timeout(function () {
//                vm.disabled = true;
//                vm.reqConf2 = true;
//            }, 470);
//        }

        vm.orderCheckout2 = function () {
            TseService.ordersCheckout({
                orderUUID: vm.rqOrderUUID2.orderUUID,
                paymentProvider: vm.request.item2.name,
//                bankCode: vm.request.item.code,
//                returnUrl: ('https://' + $window.location.host + '/selfcare/response'),
                returnUrl: ('https://' + $window.location.host + '/SelfCare/response'),
                ipnUrl: vm.request.item2.ipnUrl
            }).then(function (res) {
                vm.odCheckout = res.data;
//                console.log("CHEKCOUT: ", res.data);
//                storageService.setData(vm.odCheckout);
                $window.location.href = vm.odCheckout.paymentUrl;
            }).catch(function (exc) {
//                console.log("EX ORDER CHECKOUT: ", exc);
            });
        }

        $scope.showPassword2 = false;
        $scope.toggleShowPassword2 = function () {
            $scope.showPassword2 = !$scope.showPassword2;
        }

    }
})();