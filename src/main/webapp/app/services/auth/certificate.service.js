(function () {
    'use strict';

    angular
        .module('loginappApp')
        .factory('Certificate', Certificate);

    Certificate.$inject = ['$resource'];

    function Certificate ($resource) {
        return $resource('api/certificate', {}, {});
    }
})();
