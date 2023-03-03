(function() {
    'use strict';

    angular
        .module('loginappApp')
        .factory('OwnerLoggingFactory', OwnerLoggingFactory);

    OwnerLoggingFactory.$inject = ['$resource'];

    function OwnerLoggingFactory ($resource) {
        var service = $resource('api/owner/history', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {method: 'GET',
                interceptor: {
                    response: function(response) {
                        // expose response
                        return response;
                    }
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });

        return service;
    }
})();
