(function () {
    'use strict';
    angular
            .module('loginappApp')
            .controller('LoginController', LoginController);
    LoginController.$inject = ['$scope', '$rootScope', '$location', '$state', '$timeout',
        'Auth', 'notifySv', '$q', '$stateParams', 'registerCertificateObject', 'TseService',
        '$translate', 'TIMEOUT_HIDE_MODAL', '$window', 'storageService', '$mdDialog', 'LoginSSOService', '$cookies', 'Principal', '$http'];
    function LoginController($scope, $rootScope, $location, $state, $timeout, Auth, notifySv, $q,
            $stateParams, registerCertificateObject, TseService, $translate, TIMEOUT_HIDE_MODAL,
            $window, storageService, $mdDialog, LoginSSOService, $cookies, Principal, $http) {
//        loaded.isLoading = false;
        var vm = this;
        vm.popupRegisStun = popupRegisStun;
        vm.__csrf = "";
        vm.dialogMessage = {
            isShow: false,
            isError: false,
            message: null
        };
        $timeout(function () {
            $(".g-scrolling-carousel .items").gScrollingCarousel();
        }, 1000);
        vm.registerCertificateObject = registerCertificateObject;
        // console.log("vm.registerCertificateObject:",vm.registerCertificateObject);
        if (vm.registerCertificateObject.status) {
            vm.dialogMessage = {
                isShow: true,
                isError: false,
                message: vm.registerCertificateObject.msg
            };
        } else {
            vm.success = false;
        }
        vm.showDialog = false;
        vm.error = false;
        vm.errorMsg = "";
        vm.cancel = cancel;
        vm.credentials = {};
        vm.rememberMe = false;
        vm.showVc = false;
        vm.requestResetPassword = requestResetPassword;
        // vm.username = "bao_tse";
        // vm.password = "09265430";

        // vm.username = "baotrv";
        // vm.password = "91773924";
//        vm.username = "84985160831002";
//        vm.password = "?q(M3(";
        // vm.username = "vupx20200911";
        // vm.password = "12345678";

        vm.username = "";
        vm.password = "";
//        vm.username = "vacuibap";
//        vm.password = "T@mic@8x";

        // vm.username = "baotv";
        // vm.password = "97575783";

        var stopped;
        var countdown = function () {
            stopped = $timeout(function () {
                if (vm.validityPeriod < 0) {
                    vm.cancelLogin("Request timeout");
                    stop();
                    return;
                }
                vm.validityPeriod--;
                countdown();
            }, 1000);
        };
        var hideMessage;
        var countdownHideMessage = function () {
            $timeout.cancel(hideMessage ? hideMessage : null);
            hideMessage = $timeout(function () {
                vm.success = false;
            }, TIMEOUT_HIDE_MODAL);
        };
        var stop = function () {
            $timeout.cancel(stopped);
        };
        function cancel(isError, msg) {
//            console.log("ERROR MES: ", msg);
            if (msg) {
                if (msg.data) {
                    if (msg.data.error == 3039) {
                        vm.dialogMessage = {
                            isShow: true,
                            isError: true,
                            message: msg.message
                        };
                    } else {
                        if (!isError) {
                            vm.credentials = {
                                username: null,
                                password: null,
                                rememberMe: true
                            };
                            vm.dialogMessage = {
                                isShow: false,
                                isError: false,
                                message: null
                            };
                        } else {
                            if (msg.data.error == 3001) {
                                vm.dialogMessage = {
                                    isShow: true,
                                    isError: true,
                                    message: msg.message
                                };
                            } else {
                                vm.msgType = 0;
                                vm.timeoutMsg = true;
                                var timer;
//                                console.log("LOGIN ERROR: ", msg);
                                timer = $timeout(function () {
                                    vm.msg = '';
                                    vm.msgType = 0;
                                    vm.timeoutMsg = false;
                                }, TIMEOUT_HIDE_MODAL);
                            }
                        }
                    }

                    if (msg.data.error == 3002 && msg.data.tempLockoutDuration == 208) {
                        vm.dialogMessage = {
                            isShow: true,
                            isError: true,
                            message: msg.data.message
                        };
                    } else {
                        if (!isError) {
                            vm.credentials = {
                                username: null,
                                password: null,
                                rememberMe: true
                            };
                            vm.dialogMessage = {
                                isShow: false,
                                isError: false,
                                message: null
                            };
                        } else {
                            if (msg.data.error == 3001) {
                                vm.dialogMessage = {
                                    isShow: true,
                                    isError: true,
                                    message: msg.message
                                };
                            } else {
                                vm.msgType = 0;
                                vm.timeoutMsg = true;
                                var timer;
//                                console.log("LOGIN ERROR: ", msg);
                                timer = $timeout(function () {
                                    vm.msg = '';
                                    vm.msgType = 0;
                                    vm.timeoutMsg = false;
                                }, TIMEOUT_HIDE_MODAL);
                            }

                        }
                    }
                } else {
                    if (!isError) {
                        vm.credentials = {
                            username: null,
                            password: null,
                            rememberMe: true
                        };
                        vm.dialogMessage = {
                            isShow: false,
                            isError: false,
                            message: null
                        };
                    } else {
                        vm.msgType = 0;
                        vm.timeoutMsg = true;
                        var timer;
//                        console.log("LOGIN ERROR: ", msg);
                        timer = $timeout(function () {
                            vm.msg = '';
                            vm.msgType = 0;
                            vm.timeoutMsg = false;
                        }, TIMEOUT_HIDE_MODAL);
                    }
                }
            } else {
                if (!isError) {
                    vm.credentials = {
                        username: null,
                        password: null,
                        rememberMe: true
                    };
                    vm.dialogMessage = {
                        isShow: false,
                        isError: false,
                        message: null
                    };
                } else {
                    vm.msgType = 0;
                    vm.timeoutMsg = true;
                    var timer;
//                    console.log("LOGIN ERROR: ", msg);
                    timer = $timeout(function () {
                        vm.msg = '';
                        vm.msgType = 0;
                        vm.timeoutMsg = false;
                    }, TIMEOUT_HIDE_MODAL);
                }
            }

            vm.showVc = false;
        }

        vm.ssoEnabled = true;
        vm.checkLG = false;
        vm.checkStep = false;
        vm.disabledStep = false;
        vm.preLogin = function (event) {
            event.preventDefault();
            vm.pre = {
                user: vm.username,
                userType: vm.userType,
                ssoInfo: true
            }
            let fullLocationLink = $window.location.origin + $window.location.pathname;
//            console.log("LINK: ", fullLocationLink);
            let login_hint = vm.username;
            TseService
                    .authPreLogin(vm.pre)
                    .then(function (res) {
//                        loaded.isLoading = false;

//                        console.log("LOGINSSO: ", res);
                        storageService.setDataLoginSSO(res);
                        if (res.ssoEnabled == "true") {
                            vm.ssoEnabled = false;
                            vm.checkLG = true;
                            vm.checkStep = true;
                            vm.disabledStep = true;
                            storageService.setPreLogin(res);
//                            vm.rePost = {
//                                locationLink: fullLocationLink,
//                                client_id: res.clientId,
////                                redirect_uri: "https://192.168.5.112:8643/SelfCare/after-preLogin",
//                                redirect_uri: fullLocationLink + "after-preLogin",
//                                state: res.state,
//                                response_type: res.code,
//                                scope: res.scope,
//                                authUrl: res.authUrl,
//                                tokenUrl: res.tokenUrl
//                            }
//
//                            TseService.rePostData(vm.rePost)
//                                    .then(function (res) {
////                                        console.log("rePostData: ", res);
//                                        $window.location.href = res.authUrl
//                                                + "?" + "client_id=" + res.client_id + "&"
//                                                + "redirect_uri=" + res.redirect_uri + "&"
//                                                + "state=" + res.state + "&"
//                                                + "response_type=code" + "&"
//                                                + "scope=" + res.scope + "&"
//                                                + "login_hint=" + login_hint;
//                                    }).catch(function (err) {
////                                console.log(err);
//                            });
                        } else {
                            vm.ssoEnabled = false;
                            vm.checkLG = true;
                            vm.checkStep = true;
                            vm.disabledStep = true;
                            storageService.setPreLogin(res);
                        }
                    })
                    .catch(function (err) {
//                        loaded.isLoading = false;
//                        console.log("ERROR LOGINSSO: ", err);
                        if (err.status == -1) {
                            vm.dialogMessage = {
                                isShow: true,
                                isError: true,
                                message: err.statusText
                            }
                        } else {
                            if (err.data.message == "") {
                                vm.dialogMessage = {
                                    isShow: true,
                                    isError: true,
                                    message: err.data.error
                                }
                            } else {
                                vm.dialogMessage = {
                                    isShow: true,
                                    isError: true,
                                    message: err.data.errorDescription
                                }
                            }
                        }


                    });
        }

        vm.cancelStep1 = function () {
            vm.checkStep = false;
            vm.checkLG = false;
            vm.ssoEnabled = true;
            vm.disabledStep = false;
        }

        vm.loginStep2 = function (event) {
            cancel(false);
            vm.credentials = {
                username: vm.username,
                password: vm.password,
                rememberMe: vm.rememberMe,
                userType: vm.userType
            };
            event.preventDefault();
            let res = storageService.getPreLogin();
//            console.log("RES: ", res);
            if (res.method == 'TSE' || (res.method == 'NONE' && !vm.password)) {
                //TSE
                vm.showVc = true;
                vm.credentials.vc = res.vc;
                vm.credentials.hashValue = res.hashValue;
                vm.credentials.validityPeriod = res.validityPeriod;
                vm.credentials.tse = true;
                vm.vc = res.vc;
                vm.validityPeriod = res.validityPeriod;
                loginTse(vm.credentials, true);
                vm.ssoEnabled = true;
                vm.checkLG = false;
                vm.checkStep = false;
                vm.disabledStep = false;
            } else {
                //other
                loginFunc(vm.credentials);
                vm.ssoEnabled = true;
                vm.checkLG = false;
                vm.checkStep = false;
                vm.disabledStep = false;
            }

        }


//        vm.get2Factor = function (event) {
//
//
//
//            cancel(false);
//
//            vm.credentials = {
//                username: vm.username,
//                password: vm.password,
//                rememberMe: vm.rememberMe,
//                userType: vm.userType
//            };
//
//            event.preventDefault();
//
//            TseService.authTwoFactor(vm.credentials).then(function (res) {
//                console.log(res);
//                if (res.code == 0) {
//                    if (res.data && (res.data.method == 'TSE' || (res.data.method == 'NONE' && !vm.password))) {
//                        //TSE
//                        console.log("show vc");
//                        vm.showVc = true;
//                        vm.credentials.vc = res.vc;
//                        vm.credentials.hashValue = res.hashValue;
//                        vm.credentials.validityPeriod = res.validityPeriod;
//                        vm.credentials.tse = true;
//
//                        vm.vc = res.vc;
//                        vm.validityPeriod = res.validityPeriod;
//                        loginTse(vm.credentials, true);
//
//                    } else {
//                        //Other
//                        loginFunc(vm.credentials);
//                    }
//
//                } else {
//                    console.log("FONT ERROR: ", res.message);
//                    if (res.code == 400) {
//                        vm.dialogMessage = {
//                            isShow: true,
//                            isError: true,
//                            message: res.message
//                        }
//                    } else if (res.data.error == 3002) {
//                        vm.dialogMessage = {
//                            isShow: true,
//                            isError: true,
//                            message: res.message
//                        }
//                    } else {
//                        cancel(true, res.message);
//                    }
//
//                }
//            }).catch(function () {
//
//
//                cancel(true);
//            });
//        };

        var rq;
        function loginTse(credentials, ignoreLoadingBar) {
            countdown();
            (rq = Auth.login(credentials, ignoreLoadingBar)).promise.then(function (res) {
//                console.log(res);
                if (res == 'CANCEL') {
                    stop();
                    cancel(false);
                    return;
                }
                if (res.data.code == 0) {
                    $state.go('account.profile');
                    // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                    // since login is successful, go to stored previousState and clear previousState
                    if (Auth.getPreviousState()) {
                        var previousState = Auth.getPreviousState();
                        Auth.resetPreviousState();
                        $state.go(previousState.name, previousState.params);
                    }
                }
            }).catch(function (exception) {
                stop();
//                console.log(exception);
                if (exception.hasOwnProperty("data") && exception.data.hasOwnProperty("message")) {
                    cancel(true, exception.data);
                } else {
                    cancel(true);
                }

//                $timeout(function () {
//                    window.localtion.reload();
//                }, TIMEOUT_HIDE_MODAL);
            });
        }

        vm.cancelLogin = function (msg) {
            vm.showVc = false;
            if (rq) {
                rq.resolve("CANCEL");
            }
            stop();
        };
        function loginFunc(credentials) {
            vm.success = false;
            Auth.login(credentials, false).promise.then(function (res) {
//                console.log("FONT AUTH LOGIN: ", res);
                vm.error = false;
                if (res.data.code == 0) {
                    // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                    // since login is successful, go to stored previousState and clear previousState
                    if (Auth.getPreviousState()) {
                        var previousState = Auth.getPreviousState();
                        Auth.resetPreviousState();
                        $state.go(previousState.name, previousState.params);
                    }
                    $state.go('account.profile');
                }
            }).catch(function (exception) {
//                console.log("CATCH LOGINFUNC: ", exception);
                if (exception.hasOwnProperty("data") && exception.data.hasOwnProperty("message")) {
                    cancel(true, exception.data.message);
                } else {
                    cancel(true);
                }

//                $timeout(function () {
//                    window.localtion.reload();
//                }, TIMEOUT_HIDE_MODAL);
            });
            // });

        }

        function requestResetPassword() {
            $state.go('requestReset');
        }

        $scope.showPassword = false;
        $scope.toggleShowPassword = function () {
            $scope.showPassword = !$scope.showPassword;
        }

        function popupRegisStun() {
            $mdDialog.show({
                controller: function () {
                    this.hide = function () {
                        hideModel();
                    }

                    function hideModel() {
                        $mdDialog.hide();
                    }
                },
                controllerAs: 'vm',
                templateUrl: 'app/components/register/regisStun.html',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: false
            }).then(function (res) {

            });
        }

        checkError3001();
        function checkError3001() {
            let checkCK1 = document.cookie.split(";");
//            console.log("check Err1: ", checkCK1);
            let checkErr = '';
            let checkErrMess = ''

            for (let i = 0; i < checkCK1.length; i++) {
                let cookiePair = checkCK1[i].split("=");
                if (cookiePair[0].trim() == "loginSSOErr") {
//                    console.log("check ERROR: ", cookiePair[1]);
                    checkErr = cookiePair[1];
                }
                if (cookiePair[0].trim() == "loginSSOErrMess") {
//                    console.log("check ERROR: ", cookiePair[1]);
                    checkErrMess = cookiePair[1];
                }
            }
            if (checkErr == 3001) {
                vm.dialogMessage = {
                    isShow: true,
                    isError: true,
                    message: checkErrMess
                }

                $timeout(function () {
                    $cookies.remove('loginSSOErr');
                    $cookies.remove('loginSSOErrMess');
                }, TIMEOUT_HIDE_MODAL);
            }
        }

        vm.rqCode;
        vm.loginQr;
        vm.loginWithQr = function () {
            vm.loginQr = true;
            vm.onBlur = false;
//            console.log("loginQR: ", vm.loginQr);
            LoginSSOService.authLoginQRCode({
                action: 'LOGIN'
            }).then(function (res) {
                vm.loginQR = false;
//                console.log(":", res);
                vm.rqCode = "data:image/png;base64," + res.data.qrCode;
                if (res.data.error == 0) {
//                    console.log("Error = 0");
                    loginQRGetResult(res);
                }
            }).catch(function (err) {
//                console.log(":err:", err);
            })
        }

        let callPolling = null;
        vm.onBlur = false;
        vm.ifQrNotAllowed = true;
        function loginQRGetResult(res) {
            LoginSSOService.authLoginQRGetResult({
                qrCodeUUID: res.data.qrCodeUUID,
                requestID: res.data.responseID
            }).then(function (resp) {
//                console.log("loginQRGetResult: ", resp);
                if (resp.data.error == 3037 || resp.data.error == 3035) {
                    if (vm.loginQr == true) {
                        callPolling = $timeout(function () {
//                            console.log("loginQRGetResult 5s");
                            loginQRGetResult(res);
                        }, 5 * 1000)
                    } else {
                        $timeout.cancel(callPolling);
                    }
                } else if (resp.data.error == 3039 || resp.data.error == 3254) {
                    vm.onBlur = true;
                } else if (resp.data.error == 3017) {
                    vm.ifQrNotAllowed = false;
                    vm.dialogMessage = {
                        isShow: true,
                        isError: false,
                        message: resp.data.errorDescription
                    };

                    $timeout(function () {
                        vm.loginQr = false;
                        vm.username = "";
                        vm.password = "";
                        vm.ssoEnabled = true;
                        vm.disabledStep = false;
                        vm.checkLG = false;
                        vm.ifQrNotAllowed = true;
                    }, 3000);
                } else {
                    $state.go('account.profile');
                    $window.location.reload();
                }


            }).catch(function (err) {
//                console.log("Err loginQRGetResult: ", err);
            });
        }



        vm.changeLoginQr = function () {
            $timeout.cancel(callPolling);
            vm.loginQr = false;
            vm.username = "";
            vm.password = "";
            vm.ssoEnabled = true;
            vm.disabledStep = false;
            vm.checkLG = false;
//            console.log("loginQR: ", vm.loginQr);

        }
    }
}
)();
