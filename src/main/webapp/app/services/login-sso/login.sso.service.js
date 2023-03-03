(function () {
    'use strict';
    angular
            .module('loginappApp')
            .factory('LoginSSOService', LoginSSOService);
    LoginSSOService.$inject = ['$rootScope', '$state', '$sessionStorage', '$q', '$translate', 'Principal', 'LoginSSOFactory', '$timeout', '$interval'];
    function LoginSSOService($rootScope, $state, $sessionStorage, $q, $translate, Principal, LoginSSOFactory, $timeout, $interval) {
        let service = {
            authLoginSSO: authLoginSSO,
            authLoginQRCode: authLoginQRCode,
            authLoginQRGetResult: authLoginQRGetResult
        };
        return service;

        function authLoginSSO(key, callback) {
            var cb = callback || angular.noop;
            return LoginSSOFactory.get(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }

        function authLoginQRCode(key, callback) {
            let cb = callback || angular.noop;

            return LoginSSOFactory.authLoginQRCode(key,
                    function (res) {
                        return cb(res);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }
        ;


        function authLoginQRGetResult(key, callback) {
            let cb = callback || angular.noop;
            return LoginSSOFactory.authLoginQRGetResult(key,
                    function (res) {
                        return cb(res);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise
        }
        ;
    }
})();