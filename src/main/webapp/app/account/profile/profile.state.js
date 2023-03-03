(function () {
    'use strict';
    angular
            .module('loginappApp')
            .config(stateConfig);
    stateConfig.$inject = ['$stateProvider'];
    function stateConfig($stateProvider) {
        $stateProvider.state('account.profile', {
            parent: 'account',
            url: '/account/profile',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'accountProfile.title',
                navTitle: 'accountProfile.nav.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/profile/profile.html',
                    controller: 'ProfileController',
                    controllerAs: 'vm'
                },
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('account.profile');
                        return $translate.refresh();
                    }]
            }
        });


        //Change passWord
        $stateProvider.state('account.profile.changePassword', {
            parent: 'account.profile',
            url: '/changePassword',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'accountProfile.changePassword.title',
            },
            // trigger the modal to open when this route is active
            onEnter: ['$stateParams', '$state', '$mdDialog', 'storageService', 'TseService', '$timeout', 'TIMEOUT_HIDE_MODAL', 'Auth',
                function ($stateParams, $state, $mdDialog, storageService, TseService, $timeout, TIMEOUT_HIDE_MODAL, Auth) {
                    if (!storageService.getOwnerChangePassword()) {
//                        console.log("change: ", storageService.getOwnerChangePassword());
                        $state.go("account.profile");
                    } else {
                        checkInfo();
                        function checkInfo() {
                            TseService.ownerInfo().then(function (res) {
//                                console.log("check owner info: ", res);
                                $mdDialog.show({
                                    controller: function () {
                                        var vm = this;
                                        vm.disabled = false;
                                        vm.msg = "";
                                        vm.msgType = null;
                                        vm.timeoutMsg = true;
                                        var timer;
                                        vm.changePassword = function () {
                                            TseService.ownerChangePassword({
                                                oldPassword: vm.oldPassword,
                                                newPassword: vm.newPassword
                                            }).then(function (res) {
                                                if (res.code == 0 && res.data.error == 0) {
                                                    vm.msgType = 0;
                                                    vm.disabled = true;
                                                    vm.msg = res.data.errorDescription;
                                                    timer = $timeout(function () {
                                                        hideModel()
                                                    }, TIMEOUT_HIDE_MODAL);
                                                } else {
                                                    vm.msgType = 1;
                                                }
                                                vm.timeoutMsg = true;
                                                vm.msg = res.data.errorDescription;
                                            }).catch(function (ex) {
//                                                console.log("exception ......", ex);
                                                vm.msgType = 1;
                                                if (typeof ex === 'string') {
                                                    vm.msg = ex;
                                                } else if (typeof ex === 'object') {
                                                    vm.msg = ex.data.message;
                                                } else {
                                                    vm.msg = "401";
                                                }

                                                $timeout(function () {
//                                                    console.log("TimeOut 3s");
                                                    vm.msg = '';
                                                    vm.msgType = 0;
                                                    vm.timeoutMsg = false;
                                                }, TIMEOUT_HIDE_MODAL);
                                                vm.timeoutMsg = true;
                                            });
                                        };
                                        this.hide = function () {
                                            $timeout.cancel(timer);
                                            hideModel();
                                        };
                                        function hideModel() {
                                            $mdDialog.hide();
                                            storageService.setOwnerChangePassword(false);
                                            $state.go('account.profile');
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
                                    templateUrl: 'app/account/profile/change-password/change-password-md.html',
                                    parent: angular.element(document.body),
                                    // targetEvent: event,
                                    clickOutsideToClose: true,
                                    fullscreen: false, // Only for -xs, -sm breakpoints.
                                }).then(function (answer) {
//                                    console.log("answer: ", answer);
                                    storageService.setOwnerChangePassword(false);
                                    $state.go('account.profile');
                                    // $scope.status = 'You said the information was "' + answer + '".';
                                }, function () {
//                                    console.log("cancel: ");
                                    storageService.setOwnerChangePassword(false);
                                    $state.go('account.profile');
                                    // $scope.status = 'You cancelled the dialog.';
                                });
                            }).catch(function (err) {
//                                console.log("check ownerInfo err: ", err);
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
//        Change email

        $stateProvider.state('account.profile.changeEmail', {
            parent: 'account.profile',
            url: '/changeEmail',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'accountProfile.changeEmail.title',
            },
            // trigger the modal to open when this route is active
            onEnter: ['$stateParams', '$state', '$mdDialog', 'storageService', 'TseService', '$timeout', 'TIMEOUT_HIDE_MODAL', 'Auth',
                function ($stateParams, $state, $mdDialog, storageService, TseService, $timeout, TIMEOUT_HIDE_MODAL, Auth) {
                    if (!storageService.getOwnerChangeEmail()) {
//                        console.log("change: ", storageService.getOwnerChangeEmail());
                        $state.go("account.profile");
                    } else {
                        checkInfo();
                        function checkInfo() {
                            TseService.ownerInfo().then(function (res) {
                                $mdDialog.show({
                                    controller: function () {
                                        let vm = this;
                                        vm.disabled = false;
                                        vm.msg = "";
                                        vm.msgType = null;
                                        var timer;
                                        vm.data = storageService.getData();
//                                        console.log("data: ", vm.data);
                                        vm.timeoutMsg = false;
                                        vm.initRequest = true;
                                        vm.sentOTPSuccess = false;
                                        vm.OTPisInvalid = false;
                                        vm.emailInvalid = false;

                                        vm.initRequestChangeEmail = function () {
                                            TseService.ownerChangeEmail({
                                                credentialID: $stateParams.id,
                                                requestType: 'request',
                                                'newEmail': vm.request.newEmail

                                            }).then(function (res) {
//                                                console.log("res: ", res);
                                                if (res.code == 0 && res.data.error == 0) {
                                                    // this.msgType = 0;
                                                    vm.step = 1;
                                                    vm.initRequest = false;
                                                    vm.responseID = res.data.responseID;
                                                    vm.sentOTPSuccess = true;

                                                    $timeout(function () {
//                                                        console.log("TimeOut 3s");
                                                        vm.sentOTPSuccess = false;
                                                    }, TIMEOUT_HIDE_MODAL);

                                                } else {
                                                    vm.msgType = 1;
                                                    vm.msg = res.data.errorDescription;
                                                }

                                            }).catch(function (ex) {
//                                                console.log("exception ......", ex);

                                                if (ex.data.data.error == 3008) {
                                                    vm.emailInvalid = true;
                                                    $timeout(function () {
                                                        vm.emailInvalid = false;
                                                    }, TIMEOUT_HIDE_MODAL);
                                                } else {
                                                    vm.msgType = 1;
                                                    if (ex instanceof Object) {
                                                        vm.msg = ex.data.message;
                                                    } else {
                                                        vm.msg = ex;
                                                    }

                                                    $timeout(function () {
//                                                        console.log("TimeOut 3s");
                                                        vm.msg = '';
                                                        vm.msgType = 0;
                                                        vm.timeoutMsg = false;
                                                    }, TIMEOUT_HIDE_MODAL);
                                                    vm.timeoutMsg = true;
                                                }


                                            });
                                        };
                                        vm.confirmChangeEmail = function () {
                                            TseService.ownerChangeEmail({
                                                credentialID: $stateParams.id,
                                                requestType: 'confirm',
                                                otpOldEmail: vm.request.otpCurrentEmail,
                                                otpNewEmail: vm.request.otpNewEmail,
                                                requestID: vm.responseID,
                                            }).then(function (res) {
                                                // console.log("res: ",res);
                                                if (res.code == 0 && res.data.error == 0) {
                                                    vm.msgType = 0;
                                                    vm.step = 2;
                                                    vm.timeoutMsg = true;
                                                    vm.msg = res.data.errorDescription;
                                                    vm.disabled = true;
                                                    vm.data.email = vm.request.newEmail;
                                                    $timeout(function () {
                                                        hideModel()
                                                    }, TIMEOUT_HIDE_MODAL);
                                                } else {
                                                    vm.msgType = 1;
                                                    vm.msg = res.data.errorDescription;
                                                }

                                            }).catch(function (ex) {
//                                                console.log("exception ......", ex);

                                                if (ex.data.data.error == 1004) {
                                                    vm.OTPisInvalid = true;
                                                    timer = $timeout(function () {
                                                        vm.OTPisInvalid = false;
                                                    }, TIMEOUT_HIDE_MODAL);
                                                } else {
                                                    vm.msgType = 1;
                                                    if (typeof ex === 'string') {
                                                        vm.msg = ex;
                                                    } else if (typeof ex === 'object') {
                                                        vm.msg = ex.data.message;
                                                    } else {
                                                        vm.msg = "401";
                                                    }
                                                    timer = $timeout(function () {
//                                                        console.log("TimeOut 3s");
                                                        vm.msg = '';
                                                        vm.msgType = 0;
                                                        vm.timeoutMsg = false;
                                                    }, TIMEOUT_HIDE_MODAL);
                                                    vm.timeoutMsg = true;
                                                }


                                            });
                                        };
                                        this.hide = function () {
                                            $timeout.cancel(timer);
                                            hideModel();
                                        }

                                        function hideModel() {
                                            $mdDialog.hide();
                                            $state.go('account.profile');
                                        }
                                    },
                                    controllerAs: 'ctrl',
                                    templateUrl: 'app/account/profile/change-email/change-email-md.html',
                                    parent: angular.element(document.body),
                                    // targetEvent: event,
                                    clickOutsideToClose: true,
                                    fullscreen: false, // Only for -xs, -sm breakpoints.

                                }).then(function (answer) {
//                                    console.log("answer: ", answer);
                                    $state.go('account.profile');
                                    // $scope.status = 'You said the information was "' + answer + '".';
                                }, function () {
//                                    console.log("cancel: ");
                                    $state.go('account.profile');
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
            ]
        });
//        Change Phone

//        $stateProvider.sate('account.profile.changePhone', {
//           parent: 'account.profile',
//           url: '/changePhone'
//        });

//      Change fullname
        $stateProvider.state('account.profile.changeFullName', {
            parent: 'account.profile',
            url: '/changeFullName',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'accountProfile.changeFullName.title',
            },
            onEnter: ['$stateParams', '$state', '$mdDialog', 'storageService', 'TseService', '$timeout', 'TIMEOUT_HIDE_MODAL', 'Auth',
                function ($stateParams, $state, $mdDialog, storageService, TseService, $timeout, TIMEOUT_HIDE_MODAL, Auth) {
                    if (!storageService.getOwnerChangeInfo()) {
//                        console.log("change: ", storageService.getOwnerChangeInfo());
                        $state.go("account.profile");
                    } else {
                        checkInfo();
                        function checkInfo() {
                            TseService.ownerInfo().then(function (res) {
                                $mdDialog.show({
                                    controller: function () {
                                        var vm = this;
                                        vm.disabled = false;
                                        vm.msg = "";
                                        vm.msgType = null;
                                        var timer;
                                        vm.data = storageService.getData();
                                        vm.confirmChangeFullName = function () {
                                            TseService.ownerChangeInfo({
                                                fullname: vm.request.fullname

                                            }).then(function (res) {
//                                                console.log("res: ", res);
                                                if (res.code == 0 && res.data.error == 0) {
                                                    vm.msgType = 0;
                                                    vm.data.fullname = vm.request.fullname;
                                                    timer = $timeout(function () {
                                                        hideModel();
                                                    }, TIMEOUT_HIDE_MODAL);
                                                } else {
                                                    vm.msgType = 1;
                                                }
                                                vm.msg = res.data.errorDescription;
                                            }).catch(function (ex) {
//                                                console.log("exception ......", ex);
                                                vm.msgType = 1;
                                                if (ex instanceof Object) {
                                                    vm.msg = ex.message;
                                                } else {
                                                    vm.msg = ex;
                                                }
                                            });
                                        };
                                        this.hide = function () {
                                            $timeout.cancel(timer);
                                            hideModel();
                                        }

                                        function hideModel() {
                                            $mdDialog.hide();
                                            $state.go('account.profile');
                                        }

                                    },
                                    controllerAs: 'ctrl',
                                    templateUrl: 'app/account/profile/change-fullname/change-fullname-md.html',
                                    parent: angular.element(document.body),
                                    // targetEvent: event,
                                    clickOutsideToClose: true,
                                    fullscreen: false, // Only for -xs, -sm breakpoints.
                                }).then(function (answer) {
//                                    console.log("answer: ", answer);
                                    $state.go('account.profile');
                                    // $scope.status = 'You said the information was "' + answer + '".';
                                }, function () {
//                                    console.log("cancel: ");
                                    $state.go('account.profile');
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
            ]
        });
//        Change identification
        $stateProvider.state('account.profile.changeIdentifi', {
            parent: 'account.profile',
            url: '/changeIdentification',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'accountProfile.changeIdentifi.title',
            },
            onEnter: ['$stateParams', '$state', '$mdDialog', 'storageService', 'identificationTypes', 'TseService', '$timeout', 'TIMEOUT_HIDE_MODAL',
                function ($stateParams, $state, $mdDialog, storageService, identificationTypes, TseService, $timeout, TIMEOUT_HIDE_MODAL) {
                    if (!storageService.getOwnerChangeIdentifi()) {
//                        console.log("change: ", storageService.getOwnerChangeIdentifi());
                        $state.go("account.profile");
                    } else {
                        $mdDialog.show({
                            controller: function () {
                                let vm = this;
                                vm.disabled = false;
                                vm.msg = "";
                                vm.msgType = null;
                                vm.initRequest = true;
                                vm.identificationTypes = identificationTypes;
                                var timer;
                                vm.data = storageService.getData();
//                                console.log("data: ", vm.data);

                                vm.identifi = vm.data.identification;
                                vm.timeoutMsg = true;
                                vm.request = {
                                    identificationType: vm.data.identificationType
                                }
//                                console.log("IdentificationType: ", vm.request.identificationType);
                                vm.confirmChangeIdentifi = function () {
                                    TseService.ownerChangeInfo({
                                        identification: vm.request.identification,
                                        identificationType: vm.request.identificationType

                                    }).then(function (res) {
//                                        console.log("res: ", res);
                                        if (res.code == 0 && res.data.error == 0) {
                                            vm.msgType = 0;
                                            vm.data.identificationType = vm.request.identificationType;
                                            vm.data.identification = vm.request.identification;
                                            timer = $timeout(function () {
                                                hideModel();
//                                                window.location.reload();
                                            }, TIMEOUT_HIDE_MODAL);
                                        } else {
                                            vm.msgType = 1;
                                        }
                                        vm.timeoutMsg = true;
                                        vm.msg = res.data.errorDescription;
                                    }).catch(function (ex) {
//                                        console.log("exception ......", ex);
                                        vm.msgType = 1;
                                        if (typeof ex === 'string') {
                                            vm.msg = ex;
                                        } else if (typeof ex === 'object') {
                                            vm.msg = ex.data.message;
                                        } else {
                                            vm.msg = "401";
                                        }

                                        $timeout(function () {
//                                            console.log("TimeOut 3s");
                                            vm.msg = '';
                                            vm.msgType = 0;
                                            vm.timeoutMsg = false;
                                        }, TIMEOUT_HIDE_MODAL);
                                        vm.timeoutMsg = true;
                                    });
                                };
                                this.hide = function () {
                                    $timeout.cancel(timer);
                                    hideModel()
                                }

                                function hideModel() {
                                    $mdDialog.hide();
                                    $state.go('account.profile');
                                }
                            },
                            controllerAs: 'ctrl',
                            templateUrl: 'app/account/profile/change-identifi/change-identifi-md.html',
                            parent: angular.element(document.body),
                            // targetEvent: event,
                            clickOutsideToClose: true,
                            fullscreen: false, // Only for -xs, -sm breakpoints.
                        }).then(function (answer) {
//                            console.log("answer: ", answer);
                            $state.go('account.profile');
                            // $scope.status = 'You said the information was "' + answer + '".';
                        }, function () {
//                            console.log("cancel: ");
                            $state.go('account.profile');
                            // $scope.status = 'You cancelled the dialog.';
                        });
                    }
                }
            ]
        });
//        Change twoFactorMethod

        $stateProvider.state('account.profile.changeTwoFactorMethod', {
            parent: 'account.profile',
            url: '/changeTwoFactorMethod',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'accountProfile.changeTwoFactor.title',
            },
            onEnter: ['$stateParams', '$state', '$mdDialog', 'storageService', 'TseService', 'twoFactorMethods', '$timeout', 'TIMEOUT_HIDE_MODAL', 'Auth',
                function ($stateParams, $state, $mdDialog, storageService, TseService, twoFactorMethods, $timeout, TIMEOUT_HIDE_MODAL, Auth) {
                    if (!storageService.getOwnerChangeTwoFactorMethod()) {
//                        console.log("change: ", storageService.getOwnerChangeTwoFactorMethod());
                        $state.go("account.profile");
                    } else {
                        checkInfo();
                        function checkInfo() {
                            TseService.ownerInfo().then(function () {
                                $mdDialog.show({
                                    controller: function () {
                                        let vm = this;
                                        vm.disabled = false;
                                        vm.msg = "";
                                        vm.msgType = null;
                                        var timer;
                                        vm.initRequest = true;
                                        vm.twoFactorMethods = twoFactorMethods;
                                        vm.data = storageService.getData();
//                                        console.log("data: ", vm.data);
                                        vm.request = {
                                            twoFactorMethod: vm.data.twoFactorMethod
                                        }

                                        vm.confirmChangeTwoFactor = function () {
                                            TseService.ownerChangeInfo({
                                                twoFactorMethod: vm.request.twoFactorMethod
                                            }).then(function (res) {
//                                                console.log("res: ", res);
                                                if (res.code == 0 && res.data.error == 0) {
                                                    vm.msgType = 0;
                                                    vm.data.twoFactorMethod = vm.request.twoFactorMethod;
                                                    timer = $timeout(function () {
                                                        hideModel();
//                                                window.location.reload();
                                                    }, TIMEOUT_HIDE_MODAL);
                                                } else {
                                                    vm.msgType = 1;
                                                }
                                                vm.msg = res.data.errorDescription;
                                            }).catch(function (ex) {
//                                                console.log("exception ......", ex);
                                                vm.msgType = 1;
                                                if (ex instanceof Object) {
                                                    vm.msg = ex.message;
                                                } else {
                                                    vm.msg = ex;
                                                }
                                            });
                                        };
                                        this.hide = function () {
                                            $timeout.cancel(timer);
                                            hideModel();
                                        }

                                        function hideModel() {
                                            $mdDialog.hide();
                                            $state.go('account.profile');
                                        }
                                    },
                                    controllerAs: 'ctrl',
                                    templateUrl: 'app/account/profile/change-twoFactorMethod/change-twoFactorMethod-md.html',
                                    parent: angular.element(document.body),
                                    // targetEvent: event,
                                    clickOutsideToClose: true,
                                    fullscreen: false, // Only for -xs, -sm breakpoints.
                                }).then(function (answer) {
//                                    console.log("answer: ", answer);
                                    $state.go('account.profile');
                                    // $scope.status = 'You said the information was "' + answer + '".';
                                }, function () {
//                                    console.log("cancel: ");
                                    $state.go('account.profile');
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
            ]
        });
//        change Phone
        $stateProvider.state('account.profile.changePhone', {
            parent: 'account.profile',
            url: '/changePhone',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'accountProfile.changePhone.title',
            },
            onEnter: ['$stateParams', '$state', '$mdDialog', 'storageService', 'TseService', '$timeout', 'TIMEOUT_HIDE_MODAL',
                function ($stateParams, $state, $mdDialog, storageService, TseService, $timeout, TIMEOUT_HIDE_MODAL) {
                    if (!storageService.getOwnerChangePhone()) {
//                        console.log("change: ", storageService.getOwnerChangePhone());
                        $state.go("account.profile");
                    } else {
                        $mdDialog.show({
                            controller: function () {
                                let vm = this;
                                vm.data = storageService.getData();
                                var timer;
                                this.hide = function () {
                                    hideModel()
                                };
                                function hideModel() {
                                    $mdDialog.hide();
                                    storageService.setOwnerChangePassword(false);
                                    $state.go('account.profile');
                                }
                            },
                            controllerAs: 'ctrl',
                            templateUrl: 'app/account/profile/change-phone/change-phone-md.html',
                            parent: angular.element(document.body),
                            // targetEvent: event,
                            clickOutsideToClose: true,
                            fullscreen: false, // Only for -xs, -sm breakpoints.
                        }).then(function (answer) {
//                            console.log("answer: ", answer);
                            storageService.setOwnerChangePassword(false);
                            $state.go('account.profile');
                            // $scope.status = 'You said the information was "' + answer + '".';
                        }, function () {
//                            console.log("cancel: ");
                            storageService.setOwnerChangePassword(false);
                            $state.go('account.profile');
                            // $scope.status = 'You cancelled the dialog.';
                        });
                    }
                }
            ]
        })


    }


})();
