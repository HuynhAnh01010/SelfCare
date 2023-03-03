(function() {
    'use strict';

    angular
        .module('loginappApp')
        .factory('TwoFactor', TwoFactor);

    TwoFactor.$inject = ['$resource'];

    function TwoFactor($resource) {
        var service = $resource('api/auth/two-factor/:username/:userType', {},{});
        return service;
    }
})();
