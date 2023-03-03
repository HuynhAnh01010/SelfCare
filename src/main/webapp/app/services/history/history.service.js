(function() {
    'use strict';

    angular
        .module('loginappApp')
        .factory('HistoryService', HistoryService);


    HistoryService.$inject = ['$rootScope', '$state', '$sessionStorage', '$q', '$translate', 'Principal', 'OwnerLoggingFactory'];

    function HistoryService($rootScope, $state, $sessionStorage, $q, $translate, Principal, OwnerLoggingFactory) {
        var service = {
            ownerLogging: ownerLogging,
        };
        return service;

        function ownerLogging(key, callback) {
            var cb = callback || angular.noop;

            return OwnerLoggingFactory.get(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }
    }
})();
