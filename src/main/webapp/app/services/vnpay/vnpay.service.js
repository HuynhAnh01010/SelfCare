(function() {
    'use strict';

    angular
        .module('loginappApp')
        .factory('VnPayService', VnPayService);


    VnPayService.$inject = ['$rootScope', '$state', '$sessionStorage', '$q', '$translate', 'Principal',
        'VnPayFactory',"API_PROFILE"];

    function VnPayService($rootScope, $state, $sessionStorage, $q, $translate, Principal,
                          VnPayFactory,API_PROFILE) {
        var service = {
            pay: pay,
            result: result,
        };
        return service;

        function pay(key, callback) {
            var cb = callback || angular.noop;

            return VnPayFactory.pay(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

        function result(key, callback) {
            var cb = callback || angular.noop;

            return VnPayFactory.result(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

    }
})();
