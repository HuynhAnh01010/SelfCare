(function () {
    'use strict';

    angular
            .module('loginappApp')
            .factory('AuthServerProvider', AuthServerProvider);

    AuthServerProvider.$inject = ['$http', '$localStorage', '$sessionStorage', '$q', '$state', '$mdDialog'];

    function AuthServerProvider($http, $localStorage, $sessionStorage, $q, $state, $mdDialog) {
        var deferedRefreshAccessToken = null;
        var service = {
            getToken: getToken,
            login: login,
            loginWithToken: loginWithToken,
            storeAuthenticationToken: storeAuthenticationToken,
            storeToken: storeToken,
            logout: logout,
            get2Factor: get2Factor,
            refreshAccessToken: refreshAccessToken,
            loginWithQr: loginWithQr
        };

        return service;

        function getToken() {
            return $localStorage.token || $sessionStorage.token || null;

        }

        function get2Factor(credentials) {
            var data = {
                username: credentials.username,
                password: credentials.password,
                rememberMe: credentials.rememberMe
            };
            return $http.post('api/auth/two-factor', data).success(twoFactorSuccess);

            function twoFactorSuccess(data, status, headers) {
                console.log("data: ", data);
                return data;

            }
        }


        function refreshAccessToken(data) {
            //If already waiting for the Promise, return it.
            if (deferedRefreshAccessToken) {
                return deferedRefreshAccessToken.promise
            } else {
                deferedRefreshAccessToken = $q.defer();
                //Get $http service with $injector to avoid circular dependency
                // var http = $injector.get('$http');
                $http({
                    method: "POST",
                    url: "api/auth/refresh",
                    data: data
                })
                        .then(function mySucces(response) {
                            console.log("refresh: ", response);
                            var data = response.data;
                            if (data) {
                                var token = service.getToken();
                                token.access = data.accessToken;
                                token.expire = Date.now() + parseInt(10) * 1000;// calculate time of expire

                                service.storeToken(token, token.rememberMe)
                                deferedRefreshAccessToken.resolve(data.accessToken);
                                //Notify all subscribers
                                // notifySubscribersNewAccessToken(data.accessToken);
                                deferedRefreshAccessToken = null;
                            }
                        }, function myError(error) {
                            console.log("error: ", error);
                            // deferedRefreshAccessToken.resolve(error);
                            deferedRefreshAccessToken.reject(error);
                            deferedRefreshAccessToken = null;
                        });
                return deferedRefreshAccessToken.promise;
            }
        }

        function login(credentials, showLoadingBar, promise) {
            if (!promise) {
                promise = 120;
            }
            return $http.post('api/auth/authenticate', credentials, {
                ignoreLoadingBar: showLoadingBar,
                timeout: promise
            }).success(authenticateSuccess);

            function authenticateSuccess(data, status, headers) {
                console.log('login: ', data);
                if (data.data.error == 0) {
                    var token = {
                        access: data.data.accessToken,
                        refresh: data.data.refreshToken,
                        rememberMe: credentials.rememberMe,
                        expire: Date.now() + parseInt(10) * 1000 // calculate time of expire
                                // expire: Date.now()+ parseInt(data.data.expiresIn)*1000 // calculate time of expire
                    };
                    // service.storeAuthenticationToken(data.data.accessToken, data.data.refreshToken);
                    service.storeToken(token, credentials.rememberMe);
                    return data.accessToken;
                }
                // var bearerToken = headers('Authorization');
                // if (angular.isDefined(bearerToken) && bearerToken.slice(0, 7) === 'Bearer ') {
                //     var jwt = bearerToken.slice(7, bearerToken.length);
                //     service.storeAuthenticationToken(jwt, credentials.rememberMe);
                //     return jwt;
                // }
            }
        }

        function loginWithToken(jwt, rememberMe) {
            var deferred = $q.defer();

            if (angular.isDefined(jwt)) {
                this.storeAuthenticationToken(jwt, rememberMe);
                deferred.resolve(jwt);
            } else {
                deferred.reject();
            }

            return deferred.promise;
        }

        function storeAuthenticationToken(jwt, refreshJwt, remember) {

            // if (refreshJwt) {
            //     $localStorage.authenticationToken = jwt;
            //     $localStorage.authenticationRrefreshToken = refreshJwt;
            // } else {
            //     $sessionStorage.authenticationToken = jwt;
            // }
        }

        function storeToken(token, remember) {
            // delete $localStorage.token;
            // delete $sessionStorage.token;
            // if (remember) {
            //     $localStorage.token = token;
            // } else {
            //     $sessionStorage.token = token;
            // }

        }

        function logout() {
            $http({
                method: "POST",
                url: "api/auth/revoke",
                // data: data,
                ignoreLoadingBar: true
            }).then(function mySucces(response) {
                console.log("revoke ok: ", response);
            }, function myError(error) {
                console.log("error: ", error);
            });

        }
        
        function loginWithQr(login) {
            var data = {
                active: login
            }
            $http({
               method: 'POST',
               url: 'api/auth/login-with-qr',
               data: data,
               ignoreLoadingBar: true
            }).then(function success(res) {
                console.log("Login with QR response data: ", res);
            }).catch(function (err) {
                console.log("Login with QR Error: ". err);
            });
        }

//        function logoutTimeout() {
//            $http({
//                method: "POST",
//                url: "api/auth/revoke",
//                ignoreLoadingBar: true
//            }).then(function (res) {
//                console.log("res revoke: ", res);
//                $state.go("account.profile");
//                cowndownLogout();
//            }).catch(function (error) {
//                console.log("Logout Timeout: ", error);
//            })
//        }
//
//        function cowndownLogout() {
//            $mdDialog.show({
//                controller: function () {
//                    this.hide = function () {
//                        hideModel();
//                    };
//
//                    function hideModel() {
//                        $mdDialog.hide();
//                    }
//                },
//                controllerAs: 'vm',
//                templateUrl: 'app/account/profile/timeout/timeout.html',
//                parent: angular.element(document.body),
//                clickOutsideToClose: false,
//                fullscreen: false
//            }).then(function (response) {
//                console.log("Timeout Res: ", response);
//                $state.go("login");
//            })
//        }
    }
}

)();
