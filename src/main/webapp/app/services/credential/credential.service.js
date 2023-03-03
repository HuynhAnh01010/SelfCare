(function() {
    'use strict';

    angular
        .module('loginappApp')
        .factory('CredentialService', CredentialService);


    CredentialService.$inject = ['$rootScope', '$state', '$sessionStorage', '$q', '$translate', 'Principal',
        'CredentialListService','CredentialInfoService', 'CredentialHistoryService',
        "API_PROFILE"];

    function CredentialService($rootScope, $state, $sessionStorage, $q, $translate, Principal,
                               CredentialListService,CredentialInfoService,CredentialHistoryService,
                               API_PROFILE) {
        var service = {
            credentialList: credentialList,
            credentialInfo: credentialInfo,
            credentialHistory: credentialHistory,
        };
        return service;

        function credentialList(key, callback) {
            var cb = callback || angular.noop;

            return CredentialListService.get(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

        function credentialInfo(key, callback) {
            var cb = callback || angular.noop;

            return CredentialInfoService.get(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

        function credentialHistory(key, callback) {
            var cb = callback || angular.noop;

            return CredentialHistoryService.get(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }
    }
})();
