(function () {
    'use strict';

    angular.module('loginappApp')
            .controller('newCertiController', newCertiController);

    newCertiController.$inject = ['$scope', '$state', '$stateParams', 'storageService',
        'TseService', 'userTypes', 'scals', 'multipleSignings', 'authModes',
        'CredentialService', '$window', '$timeout', 'TIMEOUT_HIDE_MODAL', 'sharedModes', 'Auth', '$mdDialog'];

    function newCertiController($scope, $state, $stateParams, storageService, TseService,
            userTypes, scals, multipleSignings, authModes, CredentialService, $window,
            $timeout, TIMEOUT_HIDE_MODAL, sharedModes, Auth, $mdDialog) {
        let vm = this;
        let timer;

        vm.hide = function () {
            $timeout.cancel(timer);
        }

        vm.userTypes = userTypes;
        vm.scals = scals;
        vm.multipleSignings = multipleSignings;
        vm.authModes = authModes;
        vm.sharedModes = sharedModes;

//        vm.request.sharedMode = vm.sharedModes[0].value;

        vm.initCerProInfo = false;

        vm.cancel = function () {
            $state.go('credential.list');
        }

        vm.ownerSendOTP = function () {
            TseService
        }

        getCredetialInfo();

        function getCredetialInfo() {
            CredentialService.credentialInfo({id: $stateParams.id}).then(function (res) {

                vm.detail = res;
                storageService.setData(vm.detail);
                //console.log("getCredetialInfo DATA: ", vm.detail);
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
//                console.log("RES: ", res.profiles);

                for (let i = 0; i < res.profiles.length; i++) {
                    if (res.profiles[i].name != 'CUSTOM_PROFILE') {
                        let valueSN = res.profiles[i];
                        vm.SnProdata.push(valueSN);
                    }
                }

//                vm.SnProdata = res;
                storageService.setData(vm.SnProdata);
//                console.log("SIGNING PROFILE DATA: ", vm.SnProdata);
                vm.error = null;
                vm.success = 'OK';

            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }

        vm.ifSigningProfileCustomer = false;
        vm.fnChooseAM = false;
        vm.rqChooseSC = function () {
            let name = vm.request.signingProfile;
            vm.getChooseAM = '';

            TseService.systemGetSigningProfiles({
            }).then(function (res) {
                vm.setChooseAM = res;
                storageService.setData(vm.setChooseAM);
                //console.log("GET CHOOSE: ", vm.setChooseAM);
                for (let i in res.profiles) {
                    if (res.profiles[i].name == name) {
                        vm.getChooseAM = res.profiles[i];
                    }
                }


            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });

            //console.log("CHOOSE SC: ", name);
            if (name == 'UNLIMITED') {
                vm.fnChooseAM = false;
                vm.ifSigningProfileCustomer = false;
            } else if (name == 'CUSTOM_PROFILE') {
                vm.fnChooseAM = false;
                vm.ifSigningProfileCustomer = true;
            } else {
                vm.fnChooseAM = true;
                vm.ifSigningProfileCustomer = false;
            }
        }

        var array = [];
        vm.CerAuth = null;

        getCertificateAuthorities();
        function getCertificateAuthorities() {
            TseService.systemGetCertificateAuthorities().then(function (res) {
                for (var i in res.certificateAuthorities) {
                    if (res.certificateAuthorities[i].productionEnabled) {

                        array.push(res.certificateAuthorities[i]);

                    }
                }
                vm.CerAuth = array;

                storageService.setData(vm.CerAuth);


                //console.log("CERAUTH: ", vm.CerAuth);
//                TseService.systemGetCertificateProfiles({
//                    name: array[1].name
//                }).then(function (resp) {
//                    systemsGetCountries();
//
//                    vm.CerAuthProName = resp.data;
//                    storageService.setData(vm.CerAuthProName);
////                    //console.log("CERTIFICATE AUTHORITIES NAME 0: ", vm.CerAuthProName);
//                }).catch(function () {
//
//                    vm.success = null;
//                    vm.error = 'ERROR';
//                });


//                //console.log("CERTIFICATE AUTHORITIES: ", vm.CerAuth);
                vm.error = null;
                vm.success = 'OK';
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }


//        getCertificateProfileName();

        vm.getCertificateProfileName = function () {
            vm.rqChooseAD = false;
            if (vm.request.CerAuthName) {
                TseService.systemGetCertificateProfiles({
                    name: vm.request.CerAuthName.name
                }).then(function (resp) {
                    vm.initCerProInfo = false;
                    systemsGetCountries();
                    vm.CerAuthProName = resp;
                    storageService.setData(vm.CerAuthProName);
                    //console.log("CERTIFICATE AUTHORITIES: ", vm.CerAuthProName);
                }).catch(function () {

                    vm.success = null;
                    vm.error = 'ERROR';
                });
            }

        }

        vm.initRq = false;
        vm.initCFRQ = false;
        let nameUpercase = null;
        let nameEnterUpercase = null;
        let idenCMNDtype = null;
        let idenCMNDValue = null;

        vm.confirmRQ = false;
        vm.enrollRQ = false;

        vm.confirmRequestUpgrade = function () {
            nameUpercase = null;
            nameEnterUpercase = null;
            idenCMNDtype = null;
            idenCMNDValue = null;

            if (vm.request.commonName) {
                nameUpercase = (vm.request.commonName).toUpperCase();
            }

            if (vm.request.idenCMNDValue != '') {
                idenCMNDtype = vm.request.idenCMNDType;
                idenCMNDValue = vm.request.idenCMNDValue;
            }




            TseService.credentialsIssue({

                certificateProfile: vm.request.CerAuthProName.name,
                signingProfile: vm.request.signingProfile,
                signingProfileValue: vm.request.signingProfileValue,
                SCAL: vm.request.scal.value,
                authMode: vm.request.authMode,
                multisign: vm.request.multisign,
                email: vm.request.e,
                phone: vm.request.telephoneNumber,
                commonName: nameUpercase,
                organization: vm.request.organizationName,
                organizationUnit: vm.request.ou,
                title: vm.request.t,
                telephoneNumber: vm.request.telephoneNumber,
                location: vm.request.l,
                stateOrProvince: vm.request.s,
                country: vm.request.c,
                typeCMND: idenCMNDtype,
                valueCMND: idenCMNDValue,
                typeMST: vm.request.idenMSTType,
                valueMST: vm.request.idenMSTValue,
                sharedMode: vm.request.sharedMode


            }).then(function (res) {
                vm.dialogMessage = {
                    isShow: true,
                    isError: false,
                    message: res.data.errorDescription
                };
                //console.log("TRAVE: ", res);
                vm.successIssue = res.data.credentialID;
                timer = $timeout(function () {
                    let url = "/SelfCare/#/credential/list/detail/" + vm.successIssue;
//                    let url = "/selfcare/#/credential/list/detail/" + vm.successIssue;
                    //console.log("REDIRECT URL: ", url);
                    $window.location.href = url;
                }, TIMEOUT_HIDE_MODAL);
            }).catch(function (ex) {
                //console.log("EX: ", ex.data);
                vm.messRp = ex.data.data.error;
                if (ex.data.data.error == '3063') {

                    $timeout(function () {
                        vm.initCFRQ = true;
                        $timeout(function () {
                            vm.initRq = true;
                        }, 662);
                    }, 100);


                    vm.rqOrderUUID = ex.data.data;
                    //console.log("INIT REQUEST: ", vm.rqOrderUUID);
                    storageService.setData(vm.rqOrderUUID);
                    vm.disabled = true;


                } else if (ex.data.data.error == '3034') {
                    vm.confirmRQ = true;
                    $timeout(function () {
                        vm.initCFRQ = true;
                        $timeout(function () {
                            vm.initRq = true;
                        }, 662);
                    }, 100);

                    vm.dialogMessage = {
                        isShow: true,
                        isError: false,
                        message: ex.data.data.message
                    }

                    vm.requestApprove = function () {
                        TseService.credentialsApprove({
                            id: ex.data.data.credentialID
                        }).then(function (res) {
                            //console.log("TRAVE: ", res);
                            vm.confirmRQ = false;
                            vm.enrollRQ = true;

                            vm.dialogMessage = {
                                isShow: true,
                                isError: false,
                                message: res.message
                            }

                            vm.requestEnroll = function () {
                                TseService.credentialsEnroll({
                                    id: ex.data.data.credentialID
                                }).then(function (resEnroll) {
                                    //console.log("TRAVE: ", resEnroll);
                                    vm.dialogMessage = {
                                        isShow: true,
                                        isError: false,
                                        message: resEnroll.message
                                    }

                                }).catch(function (errorEnroll) {
                                    //console.log("ERROR: ", errorEnroll);
                                });
                            }
                        }).catch(function (error) {
                            //console.log("ERROR: ", error);
                        });
                    }

                } else {
                    vm.dialogMessage = {
                        isShow: true,
                        isError: !(ex.code == 0 && ex.data.error == 0),
                        message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                    };
                }

                if (ex.data.error) {
                    if (ex.data.error == '3063') {

                        $timeout(function () {
                            vm.initCFRQ = true;
                            $timeout(function () {
                                vm.initRq = true;
                            }, 662);
                        }, 100);


                        vm.rqOrderUUID = ex.data;
                        //console.log("INIT REQUEST: ", vm.rqOrderUUID);
                        storageService.setData(vm.rqOrderUUID);
                        vm.disabled = true;


                    } else if (ex.data.error == '3034') {
                        vm.confirmRQ = true;
                        $timeout(function () {
                            vm.initCFRQ = true;
                            $timeout(function () {
                                vm.initRq = true;
                            }, 662);
                        }, 100);

                        vm.dialogMessage = {
                            isShow: true,
                            isError: false,
                            message: ex.data.message
                        }

                        vm.requestApprove = function () {
                            TseService.credentialsApprove({
                                id: ex.data.credentialID
                            }).then(function (res) {
                                //console.log("TRAVE: ", res);
                                vm.confirmRQ = false;
                                vm.enrollRQ = true;

                                vm.dialogMessage = {
                                    isShow: true,
                                    isError: false,
                                    message: res.message
                                }

                                vm.requestEnroll = function () {
                                    TseService.credentialsEnroll({
                                        id: ex.data.credentialID
                                    }).then(function (resEnroll) {
                                        //console.log("TRAVE: ", resEnroll);
                                        vm.dialogMessage = {
                                            isShow: true,
                                            isError: false,
                                            message: resEnroll.message
                                        }

                                    }).catch(function (errorEnroll) {
                                        //console.log("ERROR: ", errorEnroll);
                                    });
                                }
                            }).catch(function (error) {
                                //console.log("ERROR: ", error);
                            });
                        }

                    } else {
                        vm.dialogMessage = {
                            isShow: true,
                            isError: !(ex.code == 0 && ex.data.error == 0),
                            message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                        };
                    }
                }
            }
            );
        }

        vm.commonName = '';
        vm.organization = '';
        vm.organizationUnit = '';
        vm.title = '';
        vm.email = '';
        vm.telephoneNumber = '';
        vm.location = '';
        vm.stateOrProvince = '';
        vm.country = '';
        vm.identificationsMST = '';
        vm.identificationsCMND = '';


        vm.disablED = true;

        vm.rqChooseAD = false;

        vm.chooseCertiProfile = function () {
            vm.disablED = false;

            vm.commonName = '';
            vm.organization = '';
            vm.organizationUnit = '';
            vm.title = '';
            vm.email = '';
            vm.telephoneNumber = '';
            vm.location = '';
            vm.stateOrProvince = '';
            vm.country = '';
            vm.identificationsMST = '';
            vm.identificationsCMND = '';

            vm.request.commonName = '';
            vm.request.organizationName = '';
            vm.request.ou = '';
            vm.request.t = '';
            vm.request.e = '';
            vm.request.telephoneNumber = '';
            vm.request.l = '';
            vm.request.idenMSTValue = '';
            vm.request.idenCMNDValue = '';

            if (vm.request.CerAuthProName) {

                vm.certDetail = vm.request.CerAuthProName.attributes;
                vm.certType = vm.request.CerAuthProName.type;
                vm.certAmount = vm.request.CerAuthProName;
                //console.log("CERTIFI DETAIL: ", vm.certDetail);
                //console.log("CERT AMOUNT: ", vm.certAmount);
                vm.rqChooseAD = true;
                for (let i in vm.certDetail) {
                    if ("CN" == (vm.certDetail[i].name)) {
                        vm.commonName = vm.certDetail[i];
                        vm.initCerProInfo = true;
                    }
                    if ("O" == (vm.certDetail[i].name)) {
                        vm.organization = vm.certDetail[i];
                    }
                    if ("OU" == (vm.certDetail[i].name)) {
                        vm.organizationUnit = vm.certDetail[i];
                    }
                    if ("T" == (vm.certDetail[i].name)) {
                        vm.title = vm.certDetail[i];
                    }
                    if ("E" == (vm.certDetail[i].name)) {
                        vm.email = vm.certDetail[i];
                    }
                    if ("telephoneNumber" == (vm.certDetail[i].name)) {
                        vm.telephoneNumber = vm.certDetail[i];
                    }
                    if ("L" == (vm.certDetail[i].name)) {
                        vm.location = vm.certDetail[i];
                    }
                    if ("ST" == (vm.certDetail[i].name)) {
                        vm.stateOrProvince = vm.certDetail[i];
                    }
                    if ("C" == (vm.certDetail[i].name)) {
                        vm.country = vm.certDetail[i];
                    }
                    if ("MST" == (vm.certDetail[i].name)) {
                        vm.identificationsMST = vm.certDetail[i];
                    }
                    if ("CMND" == (vm.certDetail[i].name)) {
                        vm.identificationsCMND = vm.certDetail[i];
                    }
                }
            }

        }



        vm.countries = [];

        function systemsGetCountries() {
            TseService.systemsGetCountries().then(function (res) {
                //console.log("COUNTRY: ", res.data);
                vm.countries = res.data.countries;
                storageService.setData(vm.countries);

                TseService.systemsGetStatesOrProvinces({
                    country: vm.countries[0].code
                }).then(function (res) {
//                    //console.log("ST: ", res.data);
                    vm.storPro = res.data;
                    storageService.setData(vm.storPro);
                    vm.error = null;
                    vm.success = 'OK';
                }).catch(function (ex) {
                    vm.dialogMessage = {
                        isShow: true,
                        isError: !(ex.code == 0 && ex.data.error == 0),
                        message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                    };
                });

                vm.error = null;
                vm.success = 'OK';
            }).catch(function (ex) {
                vm.dialogMessage = {
                    isShow: true,
                    isError: !(ex.code == 0 && ex.data.error == 0),
                    message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                };
            });
        }


        getPaymentProvider();

        function getPaymentProvider() {
            TseService.getPaymentProvider({

            }).then(function (res) {
                //console.log("Payment PROVIDER: ", res.data);
                vm.getPMProvider = res.data;

                storageService.setData(vm.getPMProvider);
            }).catch(function (ex) {
                //console.log("EX: ", ex);
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
//                returnUrl: ('https://' + $window.location.host + '/selfcare/response'),
                returnUrl: ('https://' + $window.location.host + '/SelfCare/response'),
                ipnUrl: vm.request.item.ipnUrl

//                returnUrl: ($window.location.href + '/response')
            }).then(function (res) {
                vm.odCheckout = res.data;

                //console.log("CHEKCOUT: ", res.data);
                storageService.setData(vm.odCheckout);
                $window.location.href = vm.odCheckout.paymentUrl;
            }).catch(function (ex) {
                //console.log("EX ORDER CHECKOUT: ", ex);
            })
        }

//        $timeout(function () {
//            window.scrollTo({top: 0, behavior: 'smooth'});
//            $timeout(function () {
//                Auth.logout();
//                $state.go('login');
//                cowndownLogout();
//                clearTimeout($timeout);
//            }, 1000);
//        }, 30 * 60 * 1000);

        function cowndownLogout() {
//            //console.log("then 5p log!");
            $mdDialog.show({
                controller: function () {
                    this.hide = function () {
                        hideModel();
                    };

                    function hideModel() {
                        $mdDialog.hide();
                    }
                },
                controllerAs: 'vm',
                templateUrl: 'app/account/profile/timeout/timeout.html',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: false
            }).then(function (response) {
//                //console.log("Timeout Res: ", response);
            })
        }




    }
})();