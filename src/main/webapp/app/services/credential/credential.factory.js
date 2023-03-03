(function () {
    'use strict';

    angular
        .module('loginappApp')
        .factory('CredentialInfoService', CredentialInfoService)
        .factory('CredentialHistoryService', CredentialHistoryService)
        .factory('CredentialListService', CredentialListService);

    CredentialInfoService.$inject = ['$resource', "API"];

    function CredentialInfoService($resource, API) {
        var service = $resource(`api/credentials/info/`, {}, {});

        return service;
    }


    /////////////// History
    CredentialHistoryService.$inject = ['$resource', "API"];

    function CredentialHistoryService($resource, API) {
        var service = $resource(`api/credentials/history`, {}, {});

        return service;
    }

    ////////////// List
    CredentialListService.$inject = ['$resource', "API"];

    function CredentialListService($resource, API) {
        var service = $resource(`api/credentials/list`, {}, {});

        return service;
    }


})();
