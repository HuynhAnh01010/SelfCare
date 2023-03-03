(function () {
    'use strict';

    angular
        .module('loginappApp')
        .controller('ReachEmailPhoneController', ReachEmailPhoneController);


    ReachEmailPhoneController.$inject = ['$scope', '$translate', '$timeout', 'Auth', '$state', 'deviceDetector', 'notifySv', 'policy', 'FacetecMain', 'registerCertificateObject', 'EIdentityService'];

    function ReachEmailPhoneController($scope, $translate, $timeout, Auth, $state, deviceDetector, notifySv, policy, FacetecMain, registerCertificateObject, EIdentityService) {
        var vm = this;

        if (!(policy.hasOwnProperty("acceptPolicy2") && policy.acceptPolicy2)) {
            $state.go("policy2");
        }

        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;

        vm.back = back;
        vm.success = null;
        vm.initializedSuccessfully = true;
        vm.isVerifyOtp = false;
        vm.validityPeriod = 5 * 60;
        vm.tryLeft = 5;
        vm.blockOtp = false;
        vm.sendOtp = sentOtp;
        vm.successOtp = false;

        vm.registerCertificateObject = registerCertificateObject;

        if(registerCertificateObject.cancelEarly){

            let msg = {
                status: -1,
                message: "SESSION EXITED EARLY, PLEASE DON'T CLICK OUTSIDE"
            };
            showError(msg);
            registerCertificateObject.cancelEarly = false;
        }

        function back() {
            $state.go("register");
        }

        FacetecMain._init(function (initializedSuccessfully) {
            console.log("initializedSuccessfully: ", initializedSuccessfully);
            vm.initializedSuccessfully = initializedSuccessfully;
            $scope.$applyAsync();
        });

        vm.resendOtp = function () {
            vm.blockOtp = false;
            vm.error = false;
            vm.errorOtp = false;
            vm.otp = undefined;
            vm.validityPeriod = 5 * 60;

            EIdentityService.processesCreate({
                "subject_id": vm.registerCertificateObject.subjectId,
                process_type: "MOBILEVERIFICATION"
            }, function (resp) {
                try {
                    if (resp.code == 0) {
                        if (resp.data.status == 0) {
                            vm.isVerifyOtp = true;
                            vm.registerCertificateObject.processId = resp.data.process_id;
                            stop();
                            countdown();
                            // resp.data.subject_id
                            // FacetecMain.onPhotoIDMatchPressed(vm.registerCertificateObject.subjectId);
                        } else {
                            showError();
                        }
                    } else {
                        showError();
                    }
                } catch (e) {
                    showError()
                }
            });
        };

        function sentOtp() {
            vm.blockOtp = false;
            vm.error = false;
            vm.isVerifyOtp = false;
            vm.errorOtp = false;
            EIdentityService.subjectCreate({
                email: vm.registerCertificateObject.email,
                mobile: vm.registerCertificateObject.phone.replace("+84", '0')
            }, function (resp) {
                try {
                    if (resp.code == 0) {
                        if (resp.data.status == 0) {
                            // vm.isVerifyOtp= true;
                            vm.registerCertificateObject.subjectId = resp.data.subject_id;
                            // EMAILVERIFICATION
                            EIdentityService.processesCreate({
                                "subject_id": vm.registerCertificateObject.subjectId,
                                process_type: "MOBILEVERIFICATION"
                            }, function (respPro) {
                                console.log("process create type: MOBILEVERIFICATION: ",respPro);
                                try {
                                    if (respPro.code == 0) {
                                        if (respPro.data.status == 0) {
                                            vm.isVerifyOtp = true;
                                            vm.registerCertificateObject.processId = respPro.data.process_id;
                                            stop();
                                            countdown();
                                            // resp.data.subject_id
                                            // FacetecMain.onPhotoIDMatchPressed(vm.registerCertificateObject.subjectId);
                                        } else {
                                            showError(respPro);
                                        }
                                    } else {
                                        showError(respPro);
                                    }
                                } catch (e) {
                                    console.log("e: ",e);
                                    showError(e)
                                }
                            },function(error){
                                console.log("error: ",error);
                            });
                        } else {
                            showError(resp);
                        }
                    } else {
                        showError(resp);
                    }
                } catch (e) {
                    console.log("e: ",e);
                    showError()
                }
            }, function(error){
                console.log("error crete : ", error);
            });
            // FacetecMain.onPhotoIDMatchPressed(vm.registerCertificateObject);
        };

        function showError(res) {
            console.log("res: ",res);
            vm.error = true;
            if(res){
                if(res && res.status == 400){
                    vm.errorMsg = res.data.message;
                }else{
                    vm.errorMsg = res.message;
                }
            }else{
                vm.errorMsg = "EXCEPTION";
            }
        }

        var stopped;
        let countdown = function () {
            stopped = $timeout(function () {
                if (vm.validityPeriod <= 1) {
                    //cancel
                    stop();
                    expiredOtp();
                    return;
                }
                vm.validityPeriod--;
                countdown();
            }, 1000);
        };


        let stop = function () {
            $timeout.cancel(stopped);
        };

        function expiredOtp() {
            vm.otp = undefined;
            vm.isVerifyOtp = false;
            vm.validityPeriod = 5 * 60;
            vm.blockOtp = false;
            vm.error = false;
            vm.isVerifyOtp = false;
            vm.errorOtp = false;
        }

        vm.expiredOtp = expiredOtp;

        vm.verifyOtp = function () {
            //Demo: skip check OTP
            initPhotoIdMatch();
            return;
            
            // vm.blockOtp = true;
            // $scope.$applyAsync();
            vm.errorOtp = false;
            let param = {
                otp: vm.otp,
                subject_id: vm.registerCertificateObject.subjectId,
                process_id: vm.registerCertificateObject.processId,
                type: 'OTP',
            };
            EIdentityService.verification(param, function (resp) {
                console.log("verification: OTP", resp);
                if (resp.code == 0) {
                    if (resp.data.status == 0) {
                        vm.successOtp = true;
                        stop();
                        vm.blockOtp = true;
                        initPhotoIdMatch();
                    } else {
                        console.log("1");
                        verifyOtpError(resp);
                    }
                } else {
                    console.log("2");
                    verifyOtpError(resp);
                }

            });

            function initPhotoIdMatch(){
                EIdentityService.processesCreate({
                    "subject_id": vm.registerCertificateObject.subjectId,
                    "provider":"MOBILE-ID", //MOBILE-ID //FACETEC
                    "process_type":"LIVENESS",
                    userAgent: FaceTecSDK.createFaceTecAPIUserAgentString(""),
                }, function (resp) {
                    console.log("process create type: LIVENESS: ",resp);

                    try {
                        if (resp.code == 0) {
                            if (resp.data.status == 0) {
                                vm.blockOtp = true;
                                registerCertificateObject.processId = resp.data.process_id;
                                registerCertificateObject.sessionToken = resp.data.provider_parameters.sessionToken;

                                FacetecMain.onPhotoIDMatchPressed(resp.data.provider_parameters.sessionToken);
                            } else {
                                showError(resp);
                            }
                        } else {
                            showError(resp);
                        }
                    } catch (e) {
                        console.log("error: ",e);
                        showError(e)
                    }
                });

            }

            function verifyOtpError(res) {
                vm.errorOtp = true;

                if (typeof res == "object") {
                    vm.errorMsgOtp = res.data.message;
                    if (res.data.data.retries_left == 0) {
                        console.log("res.data.data.retries_left:", res.data.data.retries_left);
                        vm.blockOtp = true;
                    }
                } else {
                    vm.errorMsgOtp = res;
                }
            }
        };


    }
})();
