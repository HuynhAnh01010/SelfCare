(function() {
    'use strict';

    angular
        .module('loginappApp')
        .factory('RefreshToken', RefreshToken);

    RefreshToken.$inject = ['$resource'];

    function RefreshToken ($resource) {
        var service = $resource('api/token/refresh',
            {},
            {
                'refresh': {method: 'POST', params: {},interceptor: {
                        response: function(response) {
                            // expose response
                            return response;
                        }
                    }}
            });

        // var service = $resource('api/token/refresh', {},
        //     {
        //     'post': { method: 'POST', params: {}, isArray: false,
        //         interceptor: {
        //             response: function(response) {
        //                 // expose response
        //                 return response;
        //             }
        //         }
        //     }
        // });

        return service;
    }
})();
