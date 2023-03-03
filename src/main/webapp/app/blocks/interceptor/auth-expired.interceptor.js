(function() {
    'use strict';

    angular
        .module('loginappApp')
        .factory('authExpiredInterceptor', authExpiredInterceptor);

    authExpiredInterceptor.$inject = ['$rootScope', '$q', '$injector', '$localStorage', '$sessionStorage'];

    function authExpiredInterceptor($rootScope, $q, $injector, $localStorage, $sessionStorage) {
        var service = {
            responseError: responseError
        };

        return service;

        var cachedRequest = null;

        function responseError(response) {
            // console.log("response: ", response);
            var auth = $injector.get("AuthServerProvider");
            if (response.status === 401) {
                // delete $localStorage.token;
                // delete $sessionStorage.token;
                var Principal = $injector.get('Principal');
                if (Principal.isAuthenticated()) {
                    var Auth = $injector.get('Auth');
                    Auth.authorize(true);
                }
            }
            // return $q.reject(response);

            // if(!response.config.url.includes("api/auth/refresh")){
            //     switch (response.status) {
            //         //Detect if reponse error is 401 (Unauthorized)
            //         case 401:
            //             //Cache this request
            //             var deferred = $q.defer();
            //             if(!cachedRequest) {
            //                 var token = auth.getToken();
            //                 //
            //                 //Cache request for renewing Access Token and wait for Promise
            //                 if(token){
            //                     let data = {
            //                         refresh: token.refresh
            //                     };
            //                     cachedRequest = auth.refreshAccessToken(data);
            //                 }
            //
            //                 //When Promise is resolved, new Access Token is returend
            //                 cachedRequest.then(function(accessToken) {
            //                     cachedRequest = null;
            //                     // console.log("accessToken: ", accessToken);
            //                     if (accessToken) {
            //                         // deferred.reject();
            //                         // console.log("response: ", response);
            //                         //Resend this request when Access Token is renewed
            //                         $injector.get("$http")(response.config).then(function(resp) {
            //                             //Resolve this request (successfully this time)
            //                             deferred.resolve(resp);
            //                         },function(resp) {
            //                             deferred.reject();
            //                         });
            //                     } else {
            //                         //If any error occurs reject the Promise
            //                         deferred.reject();
            //                     }
            //                 }, function(response) {
            //                     var Principal = $injector.get('Principal');
            //                     if (Principal.isAuthenticated()) {
            //                         var Auth = $injector.get('Auth');
            //                         Auth.authorize(true);
            //                     }
            //                     cachedRequest = null;
            //                     deferred.reject(response);
            //                 });
            //                 return deferred.promise;
            //
            //             }
            //
            //     }
            // }

            //If any error occurs reject the Promise
            return $q.reject(response);

            //
            // console.log("config authExpiredInterceptor: {}", response);
            // if (response.status === 401) {
            //     delete $localStorage.token;
            //     delete $sessionStorage.token;
            //     var Principal = $injector.get('Principal');
            //     if (Principal.isAuthenticated()) {
            //         var Auth = $injector.get('Auth');
            //         Auth.authorize(true);
            //     }
            // }
            // return $q.reject(response);
        }
    }
})();
