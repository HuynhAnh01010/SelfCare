(function () {
    'use strict';

    angular
        .module('loginappApp')
        .factory('Auth', Auth);

    Auth.$inject = ['$rootScope', '$state', '$sessionStorage', '$q', '$translate', 'Principal', 'AuthServerProvider',
         'Register', 'TwoFactor','PasswordResetInit','PasswordResetFinish'];

    function Auth($rootScope, $state, $sessionStorage, $q, $translate, Principal, AuthServerProvider,
                  Register,  TwoFactor,PasswordResetInit,PasswordResetFinish ) {
        var service = {
            activateAccount: activateAccount,
            authorize: authorize,
            changePassword: changePassword,
            createAccount: createAccount,
            getPreviousState: getPreviousState,
            login: login,
            getTwoFactor: getTwoFactor,
            logout: logout,
            loginWithToken: loginWithToken,
            resetPasswordFinish: resetPasswordFinish,
            resetPasswordInit: resetPasswordInit,
            resetPreviousState: resetPreviousState,
            storePreviousState: storePreviousState,
            // updateAccount: updateAccount,
            refreshToken: refreshToken
        };

        return service;

        function getTwoFactor(credentials, callback) {
            var cb = callback || angular.noop;

            return TwoFactor.get({'username': credentials.username,'userType':credentials.userType},
                function (res) {
                    return cb(res)
                },
                function (err) {
                    return cb(err);
                }
                    .bind(this)).$promise;

        }

        function refreshToken (key, callback) {
            var cb = callback || angular.noop;
            // var deferred = $q.defer();
            // return AuthServerProvider.refreshToken(key).then(function(res){
            //     console.log(res);
            //     return res;
            // });
            return AuthServerProvider.refreshToken(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

        function activateAccount(key, callback) {
            var cb = callback || angular.noop;

            return Activate.get(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

//         function refreshToken(key, callback) {
//             var cb = callback || angular.noop;
//
//             return AuthServerProvider.refreshToken().then(refreshTokenThen)
//                 .catch(function (err) {
//                     this.logout();
//                     deferred.reject(err);
//                     return cb(err);
//                 }.bind(this));
//
//             function refreshTokenThen(data) {
//                 console.log(data);
//                 Principal.identity(true).then(function (account) {
//                     // After the login the language will be changed to
//                     // the language selected by the user during his registration
// //                    if (account!== null) {
// //                        $translate.use(account.langKey).then(function () {
// //                            $translate.refresh();
// //                        });
// //                    }
//                     deferred.resolve(data);
//                 });
//                 return cb();
//             }
//
//         }

        function authorize(force) {
            var authReturn = Principal.identity(force).then(authThen);
            // console.log("authReturn",authReturn);
            return authReturn;

            function authThen() {
                var isAuthenticated = Principal.isAuthenticated();
                // $state.go('biometric');
                // return;
                // an authenticated user can't access to login and register pages
                // && $rootScope.toState.parent === 'account'
                if (isAuthenticated && ($rootScope.toState.name === 'login' || $rootScope.toState.name === 'register')) {
                    $state.go('account.profile');
                }

                // recover and clear previousState after external login redirect (e.g. oauth2)
                if (isAuthenticated && !$rootScope.fromState.name && getPreviousState()) {
                    var previousState = getPreviousState();
                    resetPreviousState();
                    $state.go(previousState.name, previousState.params);
                }
                if (!isAuthenticated) {
                    if($rootScope.toState.name === 'accessdenied' ){
                        $state.go('login');
                    }
                    if($rootScope.toState.data.isAuthenticated){
                        // storePreviousState($rootScope.toState.name, $rootScope.toStateParams);
                        storePreviousState($rootScope.toState.name, {});

                        // now, send them to the signin state so they can log in
                        $state.go('accessdenied').then(function () {
                            $state.go('login');
                        });
                    }

                }
            }
        }

        function changePassword(newPassword, callback) {
            var cb = callback || angular.noop;

            return Password.save(newPassword, function () {
                return cb();
            }, function (err) {
                return cb(err);
            }).$promise;
        }

        function createAccount(account, callback) {
            var cb = callback || angular.noop;

            return Register.save(account,
                function () {
                    return cb(account);
                },
                function (err) {
                    this.logout();
                    return cb(err);
                }.bind(this)).$promise;
        }

        function login(credentials, ignoreLoadingBar, callback) {
            // console.log("ignoreLoadingBar: ", ignoreLoadingBar);
            var cb = callback || angular.noop;
            var deferred = $q.defer();

            AuthServerProvider.login(credentials, ignoreLoadingBar, deferred.promise)
                .then(function (data) {
                    console.log("loginThen",data);
                    Principal.identity(true).then(function (account) {
                        console.log("loginThen account",account);
                        // After the login the language will be changed to
                        // the language selected by the user during his registration
//                    if (account!== null) {
//                        $translate.use(account.langKey).then(function () {
//                            $translate.refresh();
//                        });
//                    }
                        deferred.resolve(data);
                    });
                    return cb();
                }).catch(function (err) {
                    this.logout();
                    deferred.reject(err);
                    return cb(err);
                }.bind(this));

            return deferred;
        }

        function loginWithToken(jwt, rememberMe) {
            return AuthServerProvider.loginWithToken(jwt, rememberMe);
        }

        function logout() {
            AuthServerProvider.logout();
            Principal.authenticate(null);
        }

        function resetPasswordFinish(keyAndPassword, callback) {
            var cb = callback || angular.noop;

            return PasswordResetFinish.save(keyAndPassword, function () {
                return cb();
            }, function (err) {
                return cb(err);
            }).$promise;
        }

        function resetPasswordInit(username, callback) {
            var cb = callback || angular.noop;

            return PasswordResetInit.save(username, function () {
                return cb();
            }, function (err) {
                return cb(err);
            }).$promise;
        }

        // function updateAccount(account, callback) {
        //     var cb = callback || angular.noop;
        //
        //     return Account.save(account,
        //         function () {
        //             return cb(account);
        //         },
        //         function (err) {
        //             return cb(err);
        //         }.bind(this)).$promise;
        // }

        function getPreviousState() {
            var previousState = $sessionStorage.previousState;
            return previousState;
        }

        function resetPreviousState() {
            delete $sessionStorage.previousState;
        }

        function storePreviousState(previousStateName, previousStateParams) {
            var previousState = {"name": previousStateName, "params": previousStateParams};
            $sessionStorage.previousState = previousState;
        }
    }
})();
