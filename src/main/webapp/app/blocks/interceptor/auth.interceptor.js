(function() {
    'use strict';

    angular
        .module('loginappApp')
        .factory('authInterceptor', authInterceptor);

    authInterceptor.$inject = ['$rootScope', '$q', '$location', '$localStorage', '$sessionStorage'];

    function authInterceptor ($rootScope, $q, $location, $localStorage, $sessionStorage) {
        var service = {
            request: request
        };

        return service;

        function request (config) {
            /*jshint camelcase: false */
            config.headers = config.headers || {};
            // angular.merge(config.headers, { "Content-Type": "application/json","Access-Control-Allow-Origin":"*"});

            // var token = $localStorage.token || $sessionStorage.token;
            // if (token) {
            //     config.headers.Authorization = 'Bearer ' +  token.access;
            // }
            // return config;

            //SKIP
            // if(config.url.indexOf("auth/refresh") !== -1) {
            //     // console.log("refresh");
            //     return config;
            // }
            // //If request if for API attach Authorization header with Access Token
            // else if (config.url.indexOf("api") != -1) {
            //     // console.log("!refresh");
            //     var token = $localStorage.token || $sessionStorage.token;
            //     if (token) {
            //         config.headers.Authorization = 'Bearer ' + token.access;
            //     }
            // }
            return config;
            //
            // var token = $localStorage.token || $sessionStorage.token;
            // if (token) {
            //     config.headers.Authorization = 'Bearer ' + token.access;
            // }
            // return config;
        }
    }
})();
