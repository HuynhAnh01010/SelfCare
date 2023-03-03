(function() {
    'use strict';

    angular
        .module('loginappApp')
        .factory('errorHandlerInterceptor', errorHandlerInterceptor);

    errorHandlerInterceptor.$inject = ['$q', '$rootScope'];

    function errorHandlerInterceptor ($q, $rootScope) {
        var service = {
            responseError: responseError
        };

        return service;

        function responseError (response ) {
            if(response.status == -1){
                response.statusText = "Can't connect to server, please check your connection."
            }
            // console.log("config errorHandlerInterceptor: {}", response);
            // $rootScope.$emit('loginappApp.httpError', response);
            // if(response){
            //     if (!(response.status === 401 && (response.data === '' ||
            //         (response.data.path && (response.data.path.indexOf('/api/owner/info') === 0))))) {
            //         $rootScope.$emit('loginappApp.httpError', response);
            //     }
            // }

            return $q.reject(response);
        }
    }
})();
