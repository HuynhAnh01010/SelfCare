(function() {
    'use strict';

    angular
        .module('loginappApp')
        .factory('EIdentityService', EIdentityService);


    EIdentityService.$inject = ['$rootScope', '$state', '$sessionStorage', '$q', '$translate', 'Principal',
        'EIdentityFactory',"API_PROFILE"];

    function EIdentityService($rootScope, $state, $sessionStorage, $q, $translate, Principal,
                              EIdentityFactory,API_PROFILE) {
        var service = {
            subjectCreate: subjectCreate,
            processesCreate: processesCreate,
            imagesUpload: imagesUpload,
            verification: verification,
            processesGet: processesGet,
            processesSelfRevise: processesSelfRevise,
            processesSelfReviseAndTseRegister: processesSelfReviseAndTseRegister,
        };
        return service;

        function subjectCreate(key, callback) {
            var cb = callback || angular.noop;

            return EIdentityFactory.subjectCreate(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

        function processesCreate(key, callback) {
            var cb = callback || angular.noop;

            return EIdentityFactory.processesCreate(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

        function imagesUpload(key, callback) {
            var cb = callback || angular.noop;

            return EIdentityFactory.imagesUpload(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

        function verification(key, callback) {
            var cb = callback || angular.noop;

            return EIdentityFactory.verification(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

        function processesGet(key, callback) {
            var cb = callback || angular.noop;

            return EIdentityFactory.processesGet(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

        function processesSelfRevise(key, callback) {
            var cb = callback || angular.noop;

            return EIdentityFactory.processesSelfRevise(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

        function processesSelfReviseAndTseRegister(key, callback) {
            var cb = callback || angular.noop;

            return EIdentityFactory.processesSelfReviseAndTseRegister(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }
    }
})();
