(function () {
    'use strict';

    angular
        .module('loginappApp')
        .controller('ReachEmailPhoneController', ReachEmailPhoneController);


    ReachEmailPhoneController.$inject = ['$rootScope', '$scope', '$translate', '$timeout', 'Auth', '$state', 'deviceDetector', 'notifySv', 'policy', 'registerCertificateObject', 'EIdentityService', 'FacetecMain'];

    function ReachEmailPhoneController($rootScope, $scope, $translate, $timeout, Auth, $state, deviceDetector, notifySv, policy, registerCertificateObject, EIdentityService, FacetecMain) {
        var vm = this;

        if (!(policy.hasOwnProperty("acceptPolicy2") && policy.acceptPolicy2)) {
            $state.go("policy2");
        } else {
            // console.log("status: ",FaceTecSDK.FaceTecSessionStatus.SessionCompvaredSuccessfully);
            FacetecMain._init(function (initializedSuccessfully) {
                // FaceTecSDK.configureLocalization(FaceTecLocalizationStrings);
            });

        }

        vm.step = 1;

        vm.back = back;
        // vm.isVerifyOtp = false;
        vm.validityPeriod = 5 * 60;
        vm.tryLeft = 5;


        vm.registerCertificateObject = registerCertificateObject;
        vm.registerCertificateObject.phone = null;
        vm.registerCertificateObject.email = null;


        vm.requestOtp = requestOtpFunc;
        vm.requestResendOtp = requestResendOtpFunc;
        vm.requestVerifyOtp = requestVerifyOtpFunc;
        vm.cancelAuthenOtp = cancelAuthenOtp;

        //Event kyc
        $scope.$on('$kycEvent', function (event, data) {
            // vm.isVerifyOtp = false;
            cancelAuthenOtp();
            if (data.status != 0) {
                showError(data);
            }else{
                data.message = "You were cancel KYC"
                showError(data);
            }
            $scope.$applyAsync();
        });
        $scope.$on('$kycInit', function (event, data) {
            FacetecMain._init(function (initializedSuccessfully) {
                $scope.$applyAsync();
            });

        });

        function back() {
            $state.go("register");
        }

        function requestResendOtpFunc() {
            vm.otpCode = undefined;
            vm.validityPeriod = 5 * 60;

            EIdentityService.processesCreate({
                "subject_id": vm.registerCertificateObject.subjectId,
                process_type: "MOBILEVERIFICATION"
            }, function (resp) {
                try {
                    if (resp.code == 0) {
                        if (resp.data.status == 0) {
                            // vm.isVerifyOtp = true;
                            vm.step = 2;
                            vm.registerCertificateObject.processId = resp.data.process_id;
                            cancelTimeout();
                            countdown();
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

        function requestOtpFunc() {

            EIdentityService.subjectCreate({
                email: vm.registerCertificateObject.email,
                mobile: vm.registerCertificateObject.phone
            }, function (resp) {
                if (resp.code == 0) {
                    if (resp.data.status == 0) {
                        vm.registerCertificateObject.subjectId = resp.data.subject_id;
                        EIdentityService.processesCreate({
                            "subject_id": vm.registerCertificateObject.subjectId,
                            process_type: "MOBILEVERIFICATION"
                        }, function (respPro) {
                            if (respPro.code == 0) {
                                if (respPro.data.status == 0) {
                                    // vm.isVerifyOtp = true;
                                    vm.step = 2;
                                    vm.registerCertificateObject.processId = respPro.data.process_id;
                                    cancelTimeout();
                                    countdown();
                                }
                                vm.dialogMessage = {
                                    isShow: true,
                                    isError: false,
                                    message: resp.data.message
                                };
                            } else {
                                vm.dialogMessage = {
                                    isShow: true,
                                    isError: true,
                                    message: resp.data.message
                                };
                            }

                        }, function (error) {
                            vm.dialogMessage = {
                                isShow: true,
                                isError: true,
                                message: resp.message
                            };
                        });
                    } else {
                        vm.dialogMessage = {
                            isShow: true,
                            isError: true,
                            message:  resp.message || resp.data.message
                        };
                    }
                } else {
                    vm.dialogMessage = {
                        isShow: true,
                        isError: true,
                        message: resp.message
                    };
                }
            }, function (error) {
                vm.dialogMessage = {
                    isShow: true,
                    isError: true,
                    message: "EXCEPTION"
                };
            });
            // FacetecMain.onPhotoIDMatchPressed(vm.registerCertificateObject);
        };

        function showError(res) {
            console.log("res:",res);
            vm.dialogMessage = {
                isShow: true,
                isError: true,
                message: res.message  ||  res.data.message || "EXCEPTION"
            };
        }

        var stopped;
        var countdown = function () {
            stopped = $timeout(function () {
                if (vm.validityPeriod <= 1) {
                    //cancel
                    cancelTimeout();
                    cancelAuthenOtp();
                    return;
                }
                vm.validityPeriod--;
                countdown();
            }, 1000);
        };


        var cancelTimeout = function () {
            $timeout.cancel(stopped);
        };

        function cancelAuthenOtp() {
            vm.step =1;
            vm.registerCertificateObject.phone = null;
            vm.registerCertificateObject.email = null;
            vm.otpCode = undefined;
            vm.validityPeriod = 5 * 60;
        }


        function requestVerifyOtpFunc() {
            //Demo: skip check OTP
            // initPhotoIdMatch();
            // return;

            // vm.blockOtp = true;
            // $scope.$applyAsync();
            vm.errorOtp = false;
            var param = {
                otp: vm.otpCode,
                subject_id: vm.registerCertificateObject.subjectId,
                process_id: vm.registerCertificateObject.processId,
                type: 'OTP',
            };
            EIdentityService.verification(param, function (resp) {
                if (resp.code == 0) {
                    if (resp.data.status == 0) {
                        cancelTimeout();
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
        };

        function initPhotoIdMatch() {
            vm.step = 3;
            FacetecMain.onPhotoIDMatchPressedTest(registerCertificateObject.subjectId);
        }

        function verifyOtpError(res) {
            vm.otpCode= undefined;
            vm.dialogMessage = {
                isShow: true,
                isError: true,
                message:  res.message ||res.data.message || "EXCEPTION"
            };

        }

    }
})();
