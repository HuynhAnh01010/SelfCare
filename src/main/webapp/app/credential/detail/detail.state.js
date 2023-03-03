(function () {
    'use strict';

    angular
            .module('loginappApp')
            .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('credential.list.detail', {
            parent: 'credential.list',
            url: '/detail/:id',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.detail.title',
                navTitle: 'credential.nav.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/credential/detail/detail.html',
                    controller: 'DetailCredentialController',
                    controllerAs: 'vm'
                },
                'sideBar@credential.list.detail': {
                    templateUrl: 'app/credential/sidebar.html',
                    // controller: 'ListCredentialController',
                    // controllerAs: 'vm'
                },

            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('credential');
                        return $translate.refresh();
                    }]
            }
        });

        // Change Auth Mode
        $stateProvider.state('credential.list.detail.authMode', {
            parent: 'credential.list.detail',
            url: '/authMode',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.detail.changeAuthMode.title',
            },
            // trigger the modal to open when this route is active
            onEnter: ['$stateParams', '$state', '$mdDialog', 'storageService', 'TseService', '$timeout', 'TIMEOUT_HIDE_MODAL', 'Auth',
                function ($stateParams, $state, $mdDialog, storageService, TseService, $timeout, TIMEOUT_HIDE_MODAL, Auth) {
                    if ($stateParams.id == null || $stateParams.id == "" || storageService.getData() == undefined) {
                        $state.go("credential.list.detail");
                    } else {
                        checkInfo();
                        function checkInfo() {
                            TseService.ownerInfo().then(function (res) {
                                $mdDialog.show({
                                    controller: function () {
                                        var vm = this;

                                        var timer;
                                        vm.msgType = null;
                                        vm.timeoutMsg = true;
                                        vm.initRQMess = false;
                                        vm.logMess = false;

                                        vm.data = storageService.getData();
                                        vm.responseID = '';
                                        vm.request = {
                                            authMode: vm.data.authMode
                                        };

                                        vm.currentAuthMode = vm.data.authMode;

//                                console.log("CHANGE AUTHMODE DATA: ", vm.data.authMode);

                                        vm.initRequest = function () {
                                            vm.initDisabled = true;
                                            if (vm.data.authMode == 'EXPLICIT/OTP-EMAIL' || vm.data.authMode == 'EXPLICIT/OTP-SMS') {
                                                TseService.credentialSendOtp({
                                                    credentialID: $stateParams.id
                                                }).then(function (res) {
                                                    if (res.code == 0 && res.data.error == 0) {
                                                        vm.step = 1;
                                                        vm.initRequest = false;
                                                        vm.responseID = res.data.responseID;
                                                    }
                                                    vm.initRQMess = true;
//                                            timer = $timeout(function () {
//                                                vm.initRQMess = false;
//                                            }, TIMEOUT_HIDE_MODAL);
//                                            

                                                }).catch(function (ex) {
                                                    console.log(ex);
                                                    vm.dialogMessage = {
                                                        isShow: true,
                                                        isError: true,
                                                        message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                                                    };
                                                });
                                            } else {
                                                vm.step = 1;
                                                vm.initRequest = false;

//                                        vm.dialogMessage = {
//                                            isShow: true,
//                                            isError: false,
//                                            message: "SUCCESSFULLY"
//                                        };
                                            }
                                        };

                                        vm.confirmRequest = function () {
                                            TseService.credentialChangeAuthInfo({
                                                credentialID: $stateParams.id,
                                                authorizeCode: vm.request.authorizeCode,
                                                authMode: vm.request.authMode,
                                                scal: vm.data.SCAL,
                                                requestID: vm.responseID,
                                            }).then(function (res) {
                                                // console.log('res: ', res);
                                                vm.logMess = true;
                                                if (res.code == 0 && res.data.error == 0) {
                                                    vm.data.authMode = vm.request.authMode;
                                                    vm.step = 2;
                                                    timer = $timeout(function () {
                                                        hideModel()
                                                    }, TIMEOUT_HIDE_MODAL);
                                                }
                                                vm.dialogMessage = {
                                                    isShow: true,
                                                    isError: !(res.code == 0 && res.data.error == 0),
                                                    message: res.data.errorDescription
                                                };

                                                timer = $timeout(function () {
                                                    vm.logMess = false;
                                                }, TIMEOUT_HIDE_MODAL);

                                            }).catch(function (ex) {
                                                console.log("ERROR", ex);

                                                if (ex.data.data.error == 1004) {
                                                    vm.initRQMess = true;
                                                    vm.logMess = true;
                                                    timer = $timeout(function () {
                                                        vm.initRQMess = true;
                                                        vm.logMess = false;
                                                    }, TIMEOUT_HIDE_MODAL);
                                                } else {
                                                    timer = $timeout(function () {
                                                        vm.logMess = true
                                                        vm.dialogMessage = {
                                                            isShow: true,
                                                            isError: true,
                                                            message: ex.data.message
                                                        };
                                                    });

                                                }
                                            });
                                        }

                                        vm.hide = function () {
                                            $timeout.cancel(timer);
                                            hideModel();
                                        }

                                        function hideModel() {
                                            $mdDialog.hide();
                                            $state.go('credential.list.detail');
                                        }

                                        vm.showPassword = false;
                                        vm.toggleShowPassword = function () {
                                            vm.showPassword = !vm.showPassword;
                                        }
                                    },
                                    controllerAs: 'vm',
                                    templateUrl: 'app/credential/detail/change-authmode/authmode-md.html',
                                    parent: angular.element(document.body),
                                    // targetEvent: event,
                                    clickOutsideToClose: true,
                                    fullscreen: false, // Only for -xs, -sm breakpoints.
                                }).then(function (answer) {
                                    console.log("answer: ", answer);
                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You said the information was "' + answer + '".';
                                }, function () {
                                    console.log("cancel: ");
                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You cancelled the dialog.';
                                });
                            }).catch(function (err) {
                                if (err.status == 503) {
                                    Auth.logout();
                                    $state.go('login');
                                    $timeout(function () {
                                        cowndownLogout();
                                    }, 1000);

                                }
                            });
                        }

                        function cowndownLogout() {
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
                            }).then(function (res) {
                            }).catch(function (err) {});
                        }

                    }


                }
            ],
            onExit: ['$mdDialog', function ($mdDialog) {
                    console.log("abc");
                    $mdDialog.hide();

                }],

        });

        //Reset passphrase
        $stateProvider.state('credential.list.detail.resetPassphrase', {
            parent: 'credential.list.detail',
            url: '/resetPassphrase',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.detail.resetPassPhrase.title',
            },
            // trigger the modal to open when this route is active
            onEnter: ['$stateParams', '$state', '$mdDialog', 'storageService', 'TseService', '$timeout', 'TIMEOUT_HIDE_MODAL', 'Auth',
                function ($stateParams, $state, $mdDialog, storageService, TseService, $timeout, TIMEOUT_HIDE_MODAL, Auth) {
                    if ($stateParams.id == null || $stateParams.id == "") {
                        $state.go("credential.list.detail");
                    } else {
                        checkInfo();
                        function checkInfo() {
                            TseService.ownerInfo().then(function (res) {
                                $mdDialog.show({
                                    controller: function () {
                                        var vm = this;
                                        var timer;
                                        vm.sentOTPSuccess = false;


                                        vm.data = storageService.getData();
                                        vm.initReset = true;
                                        vm.initResetPassphrase = function () {
                                            TseService.credentialResetPassphrase({
                                                credentialID: $stateParams.id,
                                                requestType: 'request'
                                            })
                                                    .then(function (res) {
                                                        console.log("res: ", res);
                                                        if (res.code == 0 && res.data.error == 0) {
                                                            // this.msgType = 0;
                                                            vm.initReset = false;
                                                            vm.step = 1;
                                                            vm.responseID = res.data.responseID;

                                                        }
//                                                vm.dialogMessage = {
//                                                    isShow: true,
//                                                    isError: !(res.code == 0 && res.data.error == 0),
//                                                    message: res.data.errorDescription
//                                                };

                                                    }).catch(function (ex) {

                                                vm.dialogMessage = {
                                                    isShow: true,
                                                    isError: true,
                                                    message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                                                };
                                            });
                                        };
                                        vm.confirmResetPassphrase = function () {

                                            TseService.credentialResetPassphrase({
                                                credentialID: $stateParams.id,
                                                requestType: 'confirm',
                                                authorizeCode: vm.authorizeCode,
                                                requestID: vm.responseID,
                                            })
                                                    .then(function (res) {
                                                        // console.log("res: ",res);
                                                        if (res.code == 0 && res.data.error == 0) {
                                                            vm.step = 2;
                                                            vm.disabled = true;
                                                            timer = $timeout(function () {
                                                                hideModel()
                                                            }, TIMEOUT_HIDE_MODAL);

                                                        }
                                                        vm.dialogMessage = {
                                                            isShow: true,
                                                            isError: !(res.code == 0 && res.data.error == 0),
                                                            message: res.data.errorDescription
                                                        };

                                                    }).catch(function (ex) {
                                                if (ex.data.data.error == 1004) {
                                                    vm.sentOTPSuccess = true;
                                                    timer = $timeout(function () {
                                                        vm.sentOTPSuccess = false;
                                                    }, TIMEOUT_HIDE_MODAL);
                                                } else {
                                                    vm.dialogMessage = {
                                                        isShow: true,
                                                        isError: true,
                                                        message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                                                    };

                                                }
                                                ;
                                            });
                                        };
                                        this.hide = function () {
                                            $timeout.cancel(timer);
                                            hideModel();
                                        }

                                        function hideModel() {
                                            $mdDialog.hide();
                                            $state.go('credential.list.detail');
                                        }
                                    },
                                    controllerAs: 'ctrl',
                                    templateUrl: 'app/credential/detail/reset-passphrase/reset-passphrase-md.html',
                                    parent: angular.element(document.body),
                                    // targetEvent: event,
                                    clickOutsideToClose: true,
                                    fullscreen: false, // Only for -xs, -sm breakpoints.
                                }).then(function (answer) {
                                    console.log("answer: ", answer);
                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You said the information was "' + answer + '".';
                                }, function () {
                                    console.log("cancel: ");
                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You cancelled the dialog.';
                                });
                            }).catch(function (err) {
                                if (err.status == 503) {
                                    Auth.logout();
                                    $state.go('login');
                                    $timeout(function () {
                                        cowndownLogout();
                                    }, 1000);

                                }
                            });
                        }

                        function cowndownLogout() {
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
                            }).then(function (res) {
                            }).catch(function (err) {});
                        }

                    }


                }
            ],
            onExit: ['$mdDialog', function ($mdDialog) {
                    $mdDialog.hide();

                }],

        });

        //Change passphrase
        $stateProvider.state('credential.list.detail.changePassphrase', {
            parent: 'credential.list.detail',
            url: '/changePassphrase',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.detail.changePassPhrase.title',
            },
            // trigger the modal to open when this route is active
            onEnter: ['$stateParams', '$state', '$mdDialog', 'storageService', 'TseService', '$timeout', 'TIMEOUT_HIDE_MODAL', 'Auth',
                function ($stateParams, $state, $mdDialog, storageService, TseService, $timeout, TIMEOUT_HIDE_MODAL, Auth) {
                    if ($stateParams.id == null || $stateParams.id == "") {
                        $state.go("credential.list.detail");
                    } else {
                        checkInfo();
                        function checkInfo() {
                            TseService.ownerInfo().then(function (res) {
                                $mdDialog.show({
                                    controller: function () {
                                        var vm = this;
                                        var timer;
                                        vm.data = storageService.getData();
                                        vm.disabled = false;
                                        vm.changePassphrase = function () {
                                            TseService.credentialChangePassphrase({
                                                credentialID: $stateParams.id,
                                                oldPassphrase: vm.oldPassphrase,
                                                newPassphrase: vm.newPassphrase
                                            }).then(function (res) {
                                                console.log("RES CHANGE PASSPHRASE: ", res);

                                                if (res.code == 0 && res.data.error == 0) {
                                                    vm.disabled = true;
                                                    timer = $timeout(function () {
                                                        hideModel()
                                                    }, TIMEOUT_HIDE_MODAL);
                                                }

                                                vm.dialogMessage = {
                                                    isShow: true,
                                                    isError: !(res.code == 0 && res.data.error == 0),
                                                    message: res.data.errorDescription
                                                };

                                            }).catch(function (ex) {

                                                console.log("CHange PassPhrase ERROR: ", ex);
                                                if (ex.data.data.error == 1004) {
                                                    vm.msgType = 1;
                                                    timer = $timeout(function () {
                                                        vm.msgType = 0;
                                                    }, TIMEOUT_HIDE_MODAL);
                                                } else {
                                                    vm.dialogMessage = {
                                                        isShow: true,
                                                        isError: true,
                                                        message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                                                    };
                                                }


                                            });
                                        };
                                        this.hide = function () {
                                            $timeout.cancel(timer);
                                            hideModel();
                                        };

                                        function hideModel() {
                                            $mdDialog.hide();
                                            $state.go('credential.list.detail');
                                        }

                                        this.showOldPassword = false;

                                        this.toggleShowOldPassword = function () {

                                            this.showOldPassword = !this.showOldPassword;
                                        }

                                        this.showNewPassword = false;

                                        this.toggleShowNewPassword = function () {

                                            this.showNewPassword = !this.showNewPassword;
                                        }

                                        this.showReNewPassword = false;

                                        this.toggleShowReNewPassword = function () {

                                            this.showReNewPassword = !this.showReNewPassword;
                                        }
                                    },
                                    controllerAs: 'ctrl',
                                    templateUrl: 'app/credential/detail/change-passphrase/change-passphrase-md.html',
                                    parent: angular.element(document.body),
                                    // targetEvent: event,
                                    clickOutsideToClose: true,
                                    fullscreen: false, // Only for -xs, -sm breakpoints.
                                }).then(function (answer) {
//                                    console.log("answer: ", answer);
                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You said the information was "' + answer + '".';
                                }, function () {
//                                    console.log("cancel: ");
                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You cancelled the dialog.';
                                });
                            }).catch(function (err) {
                                if (err.status == 503) {
                                    Auth.logout();
                                    $state.go('login');
                                    $timeout(function () {
                                        cowndownLogout();
                                    }, 1000);

                                }
                            });
                        }

                        function cowndownLogout() {
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
                            }).then(function (res) {
                            }).catch(function (err) {});
                        }

                    }


                }
            ],
            onExit: ['$mdDialog', function ($mdDialog) {
                    $mdDialog.hide();

                }],

        });

        //Change email
        $stateProvider.state('credential.list.detail.changeEmail', {
            parent: 'credential.list.detail',
            url: '/changeEmail',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.detail.changeEmail.title',
            },
            // trigger the modal to open when this route is active
            onEnter: ['$stateParams', '$state', '$mdDialog', 'storageService', 'TseService', '$timeout', 'TIMEOUT_HIDE_MODAL', 'Auth',
                function ($stateParams, $state, $mdDialog, storageService, TseService, $timeout, TIMEOUT_HIDE_MODAL, Auth) {
                    if ($stateParams.id == null || $stateParams.id == "") {
                        $state.go("credential.list.detail");
                    } else {
                        checkInfo();
                        function checkInfo() {
                            TseService.ownerInfo().then(function () {
                                $mdDialog.show({
                                    controller: function () {
                                        var vm = this;
                                        var timer;
                                        vm.msgType = 0;
                                        vm.timeoutMsg = true;
                                        vm.otpSuccess = false;
                                        vm.logMess = false;

                                        vm.data = storageService.getData();
                                        vm.disabled = false;
                                        vm.initRequest = true;
                                        vm.initRequestChangeEmail = function () {

                                            TseService.credentialChangeEmail({
                                                credentialID: $stateParams.id,
                                                requestType: 'request',
                                                'newEmail': vm.request.newEmail
                                            })
                                                    .then(function (res) {
                                                        console.log("res: ", res);
                                                        if (res.code == 0 && res.data.error == 0) {
                                                            // this.msgType = 0;
                                                            vm.step = 1;
                                                            vm.initRequest = false;
                                                            vm.responseID = res.data.responseID;

                                                            vm.otpSuccess = true;

                                                            timer = $timeout(function () {
                                                                vm.msg = '';
                                                                vm.msgType = 0;
                                                                vm.otpSuccess = false;
                                                            }, TIMEOUT_HIDE_MODAL);
                                                        }




//                                                vm.dialogMessage = {
//                                                    isShow: true,
//                                                    isError: !(res.code == 0 && res.data.error == 0),
//                                                    message: res.data.errorDescription
//                                                };

                                                    }).catch(function (ex) {
                                                console.log("init changeEmail: ", ex);
                                                if (ex.data.data.error == 3008 && ex.status == 400) {
                                                    vm.logMess = true;
                                                    vm.initInvalid = true;
                                                    $timeout(function () {
                                                        vm.logMess = false;
                                                        vm.initInvalid = false;
                                                    }, TIMEOUT_HIDE_MODAL);
                                                } else {
                                                    vm.dialogMessage = {
                                                        isShow: true,
                                                        isError: true,
                                                        message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                                                    };
                                                }
                                            });
                                        };
                                        vm.confirmRequestChangeEmail = function () {

                                            TseService.credentialChangeEmail({
                                                credentialID: $stateParams.id,
                                                requestType: 'confirm',
                                                otpOldEmail: vm.request.otpCurrentEmail,
                                                otpNewEmail: vm.request.otpNewEmail,
                                                requestID: vm.responseID,
                                            })
                                                    .then(function (res) {

                                                        if (res.code == 0 && res.data.error == 0) {

                                                            console.log("res: ", res);
                                                            vm.step = 2;
                                                            vm.disabled = true;
                                                            vm.data.authorizationEmail = vm.request.newEmail;
                                                            timer = $timeout(function () {
                                                                hideModel()
                                                            }, TIMEOUT_HIDE_MODAL);
                                                        }
                                                        vm.dialogMessage = {
                                                            isShow: true,
                                                            isError: false,
                                                            message: res.message
                                                        };

                                                    }).catch(function (ex) {
                                                vm.logMess = true;
                                                vm.dialogMessage = {
                                                    isShow: true,
                                                    isError: true,
                                                    message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                                                };
                                                timer = $timeout(function () {
                                                    vm.logMess = false;
                                                }, TIMEOUT_HIDE_MODAL);
                                            });
                                        };
                                        this.hide = function () {
                                            $timeout.cancel(timer);
                                            hideModel();
                                        }

                                        function hideModel() {
                                            $mdDialog.hide();
                                            $state.go('credential.list.detail');
                                        }
                                    },
                                    controllerAs: 'ctrl',
                                    templateUrl: 'app/credential/detail/change-email/change-email-md.html',
                                    parent: angular.element(document.body),
                                    // targetEvent: event,
                                    clickOutsideToClose: true,
                                    fullscreen: false, // Only for -xs, -sm breakpoints.
                                }).then(function (answer) {
                                    console.log("answer: ", answer);
                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You said the information was "' + answer + '".';
                                }, function () {
                                    console.log("cancel: ");
                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You cancelled the dialog.';
                                });
                            }).catch(function (err) {
                                if (err.status == 503) {
                                    Auth.logout();
                                    $state.go('login');
                                    $timeout(function () {
                                        cowndownLogout();
                                    }, 1000);

                                }
                            })
                        }

                        function cowndownLogout() {
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
                            }).then(function (res) {
                            }).catch(function (err) {});
                        }

                    }


                }
            ],
            onExit: ['$mdDialog', function ($mdDialog) {
                    $mdDialog.hide();

                }],

        });

        //Change phone
        $stateProvider.state('credential.list.detail.changePhone', {
            parent: 'credential.list.detail',
            url: '/changePhone',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.detail.changePhone.title',
            },
            // trigger the modal to open when this route is active
            onEnter: ['$stateParams', '$state', '$mdDialog', 'storageService', 'TseService', '$timeout', 'TIMEOUT_HIDE_MODAL', 'Auth',
                function ($stateParams, $state, $mdDialog, storageService, TseService, $timeout, TIMEOUT_HIDE_MODAL, Auth) {
                    if ($stateParams.id == null || $stateParams.id == "") {
                        $state.go("credential.list.detail");
                    } else {
                        checkInfo();
                        function checkInfo() {
                            TseService.ownerInfo().then(function () {
                                $mdDialog.show({
                                    controller: function () {
                                        var vm = this;
                                        vm.data = storageService.getData();
                                        vm.disabled = false;
                                        vm.initRequest = true;
                                        var timer;
                                        vm.otpSuccess = false;
                                        vm.dialogFail = 1;

                                        vm.initRequestChangePhone = function () {

                                            TseService.credentialChangePhone({
                                                credentialID: $stateParams.id,
                                                requestType: 'request',
                                                newPhone: vm.request.newPhone
                                            })
                                                    .then(function (res) {
                                                        console.log("res: ", res);
                                                        if (res.code == 0 && res.data.error == 0) {
                                                            // this.msgType = 0;
                                                            vm.step = 1;
                                                            vm.initRequest = false;
                                                            vm.responseID = res.data.responseID;

                                                            vm.otpSuccess = true;
                                                            vm.dialogFail = 0;

                                                            timer = $timeout(function () {
                                                                vm.msg = '';
                                                                vm.msgType = 0;
                                                                vm.otpSuccess = false;
                                                            }, TIMEOUT_HIDE_MODAL);

                                                        }
//                                                vm.dialogMessage = {
//                                                    isShow: true,
//                                                    isError: !(res.code == 0 && res.data.error == 0),
//                                                    message: res.data.errorDescription
//                                                };

                                                    }).catch(function (ex) {
                                                vm.dialogMessage = {
                                                    isShow: true,
                                                    isError: true,
                                                    message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                                                };
                                            });
                                        };
                                        vm.confirmRequestChangePhone = function () {

                                            TseService.credentialChangePhone({
                                                credentialID: $stateParams.id,
                                                requestType: 'confirm',
                                                otpOldPhone: vm.request.otpCurrentPhone,
                                                otpNewPhone: vm.request.otpNewPhone,
                                                requestID: vm.responseID,
                                            })
                                                    .then(function (res) {
                                                        // console.log("res: ",res);
                                                        if (res.code == 0 && res.data.error == 0) {
                                                            vm.step = 2;
                                                            vm.disabled = true;
                                                            vm.data.authorizationPhone = vm.request.newPhone;
                                                            vm.dialogFail = 0;
                                                            timer = $timeout(function () {
                                                                hideModel()
                                                            }, TIMEOUT_HIDE_MODAL);
                                                        }
                                                        vm.dialogMessage = {
                                                            isShow: true,
                                                            isError: !(res.code == 0 && res.data.error == 0),
                                                            message: res.data.errorDescription
                                                        };

                                                    }).catch(function (ex) {
                                                vm.dialogMessage = {
                                                    isShow: true,
                                                    isError: true,
                                                    message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                                                };
                                            });
                                        };
                                        this.hide = function () {
                                            $timeout.cancel(timer);
                                            hideModel();
                                        }

                                        function hideModel() {
                                            $mdDialog.hide();
                                            $state.go('credential.list.detail');
                                        }
                                    },
                                    controllerAs: 'ctrl',
                                    templateUrl: 'app/credential/detail/change-phone/change-phone.html',
                                    parent: angular.element(document.body),
                                    // targetEvent: event,
                                    clickOutsideToClose: true,
                                    fullscreen: false, // Only for -xs, -sm breakpoints.
                                }).then(function (answer) {
                                    console.log("answer: ", answer);
                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You said the information was "' + answer + '".';
                                }, function () {
                                    console.log("cancel: ");
                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You cancelled the dialog.';
                                });
                            }).catch(function (err) {
                                if (err.status == 503) {
                                    Auth.logout();
                                    $state.go('login');
                                    $timeout(function () {
                                        cowndownLogout();
                                    }, 1000);

                                }
                            })
                        }

                        function cowndownLogout() {
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
                            }).then(function (res) {
                            }).catch(function (err) {});
                        }


                    }


                }
            ],
            onExit: ['$mdDialog', function ($mdDialog) {
                    $mdDialog.hide();

                }],

        });

        //Change Scal
        $stateProvider.state('credential.list.detail.changeScal', {
            parent: 'credential.list.detail',
            url: '/changeScal',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.detail.changeScal.title',
            },
            // trigger the modal to open when this route is active
            onEnter: ['$stateParams', '$state', '$mdDialog', 'storageService', 'TseService', '$timeout', 'TIMEOUT_HIDE_MODAL', 'Auth',
                function ($stateParams, $state, $mdDialog, storageService, TseService, $timeout, TIMEOUT_HIDE_MODAL, Auth) {
                    if ($stateParams.id == null || $stateParams.id == "") {
                        $state.go("credential.list.detail");
                    } else {
                        checkInfo();
                        function checkInfo() {
                            TseService.ownerInfo().then(function () {
                                $mdDialog.show({
                                    controller: function () {
                                        var vm = this;
                                        var timer;
                                        vm.data = storageService.getData();
                                        console.log("data: ", vm.data);
                                        // this.detail = angular.copy(storageService.getData());
                                        vm.disabled = false;
                                        vm.initRequest = true;
                                        vm.responseID = vm.data.responseID;
                                        vm.request = {
                                            scal: vm.data.SCAL
                                        };

                                        vm.logMess = false;

                                        vm.initRequest = function () {
                                            vm.step = 1;
                                            vm.disRadio = true;
                                            vm.initRequest = false;
                                        }


                                        vm.changeScal = function () {
                                            TseService.credentialChangeAuthInfo({
                                                credentialID: $stateParams.id,
                                                scal: vm.request.scal,
                                                requestID: vm.responseID,
                                                authorizeCode: vm.request.authorizeCode

                                            }).then(function (res) {
                                                // console.log('res: ', res);
                                                if (res.code == 0 && res.data.error == 0) {
                                                    vm.step = 2;
                                                    vm.data.SCAL = vm.request.scal;
                                                    vm.disRadio = true;
                                                    vm.disabled = true;

                                                    vm.dialogMessage = {
                                                        isShow: true,
                                                        isError: !(res.code == 0 && res.data.error == 0),
                                                        message: res.data.errorDescription
                                                    };
                                                    timer = $timeout(function () {
                                                        hideModel();
                                                    }, TIMEOUT_HIDE_MODAL);
                                                } else {

                                                    vm.dialogMessage = {
                                                        isShow: true,
                                                        isError: false,
                                                        message: res.data.errorDescription
                                                    };
                                                }
                                                vm.msg = res.data.errorDescription;

                                            }).catch(function (ex) {
                                                vm.logMess = true;
                                                vm.dialogMessage = {
                                                    isShow: true,
                                                    isError: true,
                                                    message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                                                };

                                                timer = $timeout(function () {
                                                    vm.logMess = false;
                                                }, TIMEOUT_HIDE_MODAL);
                                            });
                                        };

                                        this.hide = function () {
                                            $timeout.cancel(timer);
                                            hideModel();
                                        };

                                        function hideModel() {
                                            $mdDialog.hide();
                                            $state.go('credential.list.detail');
                                        }

                                        this.showPassword = false;
                                        this.toggleShowPassword = function () {
                                            this.showPassword = !this.showPassword;
                                        }
                                    },
                                    controllerAs: 'ctrl',
                                    templateUrl: 'app/credential/detail/change-scal/index.html',
                                    parent: angular.element(document.body),
                                    // targetEvent: event,
                                    clickOutsideToClose: true,
                                    fullscreen: false, // Only for -xs, -sm breakpoints.
                                }).then(function (answer) {
                                    console.log("answer: ", answer);
                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You said the information was "' + answer + '".';
                                }, function () {
                                    console.log("cancel: ");
                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You cancelled the dialog.';
                                });
                            }).catch(function (err) {
                                Auth.logout();
                                $state.go('login');
                                $timeout(function () {
                                    cowndownLogout();
                                }, 1000);
                            });
                        }

                        function cowndownLogout() {
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
                            }).then(function (res) {
                            }).catch(function (err) {});
                        }
                    }


                }
            ],
            onExit: ['$mdDialog', function ($mdDialog) {
                    $mdDialog.hide();

                }],

        });

        //Change multi
        $stateProvider.state('credential.list.detail.changeMultiSign', {
            parent: 'credential.list.detail',
            url: '/changeMultiSign',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.detail.changeMultiSign.title',
            },
            // trigger the modal to open when this route is active
            onEnter: ['$stateParams', '$state', '$mdDialog', 'storageService', 'multipleSignings', 'TseService', '$timeout', 'TIMEOUT_HIDE_MODAL', 'Auth',
                function ($stateParams, $state, $mdDialog, storageService, multipleSignings, TseService, $timeout, TIMEOUT_HIDE_MODAL, Auth) {
                    if ($stateParams.id == null || $stateParams.id == "") {
                        $state.go("credential.list.detail");
                    } else {
                        checkInfo();
                        function checkInfo() {
                            TseService.ownerInfo().then(function (res) {
                                $mdDialog.show({
                                    controller: function () {
                                        var vm = this;
                                        var timer;
                                        vm.data = storageService.getData();
                                        console.log("data: ", vm.data);
                                        // this.detail = angular.copy(storageService.getData());
                                        vm.disabled = false;
                                        vm.msg = "";
                                        vm.msgType = null;
                                        vm.initRequest = true;
                                        vm.request = {
                                            multisign: vm.data.multisign
                                        };

                                        vm.logMess = false;

                                        vm.sentOTPSuccess = false;

                                        vm.multipleSignings = multipleSignings;
                                        vm.initRequest = function () {
                                            if (vm.data.authMode == 'EXPLICIT/OTP-EMAIL' || vm.data.authMode == 'EXPLICIT/OTP-SMS') {
                                                TseService.credentialSendOtp({
                                                    credentialID: $stateParams.id
                                                }).then(function (res) {
                                                    // console.log('res: ', res);
                                                    if (res.code == 0 && res.data.error == 0) {
                                                        vm.step = 1;
                                                        vm.disRadio = true;
                                                        vm.initRequest = false;
                                                        vm.responseID = res.data.responseID;

//                                                vm.sentOTPSuccess = true;
//                                                timer = $timeout(function () {
//                                                    vm.logMess = false;
//                                                }, TIMEOUT_HIDE_MODAL);
                                                    }

                                                }).catch(function (ex) {
//                                            vm.logMess = true;
                                                    vm.dialogMessage = {
                                                        isShow: true,
                                                        isError: true,
                                                        message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                                                    };
//                                            timer = $timeout(function () {
//                                                vm.sentOTPSuccess = false;
//                                                vm.logMess = false;
//                                            }, TIMEOUT_HIDE_MODAL);
                                                });
                                            } else {
                                                vm.step = 1;
                                                vm.initRequest = false;
                                                vm.sentOTPSuccess = true;
//                                        vm.logMess = true;
//                                        timer = $timeout(function () {
//                                            vm.sentOTPSuccess = false;
//                                            vm.logMess = false;
//                                        }, TIMEOUT_HIDE_MODAL);

                                            }
                                        };

                                        vm.confirmRequest = function () {
                                            TseService.credentialChangeAuthInfo({
                                                credentialID: $stateParams.id,
                                                authorizeCode: vm.request.authorizeCode,
                                                multisign: vm.request.multisign,
                                                requestID: vm.responseID,
                                            }).then(function (res) {
                                                // console.log('res: ', res);
                                                if (res.code == 0 && res.data.error == 0) {

                                                    vm.data.multisign = vm.request.multisign;
                                                    vm.step = 2;
                                                    timer = $timeout(function () {
                                                        hideModel()
                                                    }, TIMEOUT_HIDE_MODAL);
                                                }
                                                vm.logMess = true;
                                                vm.dialogMessage = {
                                                    isShow: true,
                                                    isError: !(res.code == 0 && res.data.error == 0),
                                                    message: res.data.errorDescription
                                                };
                                                timer = $timeout(function () {
                                                    vm.logMess = false;
                                                }, TIMEOUT_HIDE_MODAL);

                                            }).catch(function (ex) {
                                                console.log("ERROR!");
                                                vm.logMess = true;
                                                vm.dialogMessage = {
                                                    isShow: true,
                                                    isError: true,
                                                    message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                                                };
                                                timer = $timeout(function () {
                                                    vm.logMess = false;
                                                }, TIMEOUT_HIDE_MODAL);
                                            });
                                        }
                                        this.hide = function () {
                                            $timeout.cancel(timer);
                                            hideModel();
                                        };

                                        function hideModel() {
                                            $mdDialog.hide();
                                            $state.go('credential.list.detail');
                                        }

                                        this.showPassword = false;
                                        this.toggleShowPassword = function () {
                                            this.showPassword = !this.showPassword;
                                        }
                                    },
                                    controllerAs: 'ctrl',
                                    templateUrl: 'app/credential/detail/change-multi-sign/index.html',
                                    parent: angular.element(document.body),
                                    // targetEvent: event,
                                    clickOutsideToClose: true,
                                    fullscreen: false, // Only for -xs, -sm breakpoints.
                                }).then(function (answer) {
                                    console.log("answer: ", answer);
                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You said the information was "' + answer + '".';
                                }, function () {
                                    console.log("cancel: ");
                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You cancelled the dialog.';
                                });
                            }).catch(function (err) {
                                if (err.status == 503) {
                                    Auth.logout();
                                    $state.go('login');
                                    $timeout(function () {
                                        cowndownLogout();
                                    }, 1000);

                                }
                            });
                        }

                        function cowndownLogout() {
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
                            }).then(function (res) {
                            }).catch(function (err) {});
                        }


                    }


                }
            ],
            onExit: ['$mdDialog', function ($mdDialog) {
                    $mdDialog.hide();

                }],

        });

        // Upload cts
        $stateProvider.state("credential.list.detail.upload", {
            parent: "credential.list.detail",
            url: "/uploads",
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.detail.generated.upload.title'
            },
            onEnter: ['$stateParams', '$state', '$mdDialog', 'storageService', 'TseService', '$timeout', 'TIMEOUT_HIDE_MODAL', 'Auth',
                function ($stateParams, $state, $mdDialog, storageService, TseService, $timeout, TIMEOUT_HIDE_MODAL, Auth) {
                    if ($stateParams.id == null || $stateParams.id == "") {
                        $state.go("credential.list.detail");
                    } else {
                        checkInfo();
                        function checkInfo() {
                            TseService.ownerInfo().then(function (res) {
                                $mdDialog.show({
                                    controller: function () {
                                        var vm = this;
                                        var timer;
                                        vm.data = storageService.getData();
                                        vm.disabled = false;

//                                vm.chooseFile = function () {
//                                    document.getElementById('buttonImport').click();
//                                }


                                        vm.import = function () {

                                            let file = document.getElementById("buttonImport").files[0];
                                            if (file == null) {
                                                vm.code = 1;
                                            } else {
                                                let r = new FileReader();
                                                var data = '';
                                                var dataFile = '';
                                                r.onloadend = function (e)
                                                {
                                                    data = e.target.result;

                                                    dataFile = data.toString()
                                                            .replace("-----BEGIN CERTIFICATE-----", "")
                                                            .replace("-----END CERTIFICATE-----", "")
                                                            .replace("\n", "")
                                                            .replace("\n", "")
                                                            ;
                                                    console.log("Data: ", dataFile);


                                                    TseService.credentialsImport({
                                                        id: $stateParams.id,
                                                        cts: dataFile
                                                    }).then(function (res) {
                                                        console.log("RES: ", res);
                                                        vm.dialogMessage = {
                                                            isShow: true,
                                                            isError: !(res.code == 0 && res.data.error == 0),
                                                            message: res.data.errorDescription
                                                        };

                                                        timer = $timeout(function () {
                                                            hideModel();
                                                        }, TIMEOUT_HIDE_MODAL);


                                                    }).catch(function (ex) {
                                                        console.log("ERROR: ", ex);

                                                        vm.dialogMessage = {
                                                            isShow: true,
                                                            isError: true,
                                                            message: typeof ex === 'string' ? ex : (typeof ex === 'object' ? ex.data.message : '401')
                                                        };
                                                    });
                                                }
                                                r.readAsBinaryString(file);
                                            }




                                        }
                                        this.hide = function () {
                                            $timeout.cancel(timer);
                                            hideModel();
                                        }

                                        function hideModel() {
                                            $mdDialog.hide();
                                            $state.go('credential.list.detail');
                                        }
                                    },
                                    controllerAs: "ctrl",
                                    templateUrl: "app/credential/detail/upload/upload-md.html",
                                    parent: angular.element(document.body),
                                    clickOutsideToClose: true,
                                    fullscreen: false, // Only for -xs, -sm breakpoints.
                                }).then(function (answer) {
                                    var timer;
                                    console.log("answer: ", answer);


                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You said the information was "' + answer + '".';
                                }, function () {
                                    console.log("cancel: ");
                                    $state.go('credential.list.detail');
                                    // $scope.status = 'You cancelled the dialog.';
                                });
                            }).catch(function (err) {
                                if (err.status == 503) {
                                    Auth.logout();
                                    $state.go('login');
                                    $timeout(function () {
                                        cowndownLogout();
                                    }, 1000);

                                }
                            });
                        }

                        function cowndownLogout() {
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
                            }).then(function (res) {
                            }).catch(function (err) {});
                        }


                    }
                }
            ],
            onExit: ['$mdDialog', function ($mdDialog) {
                    $mdDialog.hide();

                }],
        });
    }
})();
