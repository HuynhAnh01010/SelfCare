
(function () {
    'use strict';
    angular.module('loginappApp')
        .factory('AuthorizationTokenService', AuthorizationTokenService);
    AuthorizationTokenService.$inject = ['$q', '$injector'];

    function AuthorizationTokenService($q, $injector) {
        //Local storage for token
        var tokenVM = {
            accessToken: null,
            expiresAt: null
        };
        //Subscribed listeners which will get notified when new Access Token is available
        var subscribers = [];
        //Promise for getting new Access Token from backend
        var deferedRefreshAccessToken = null;
        var service = {
            getLocalAccessToken: getLocalAccessToken,
            refreshAccessToken: refreshAccessToken,
            isAccessTokenExpired: isAccessTokenExpired,
            subscribe: subscribe
        };

        return service;
        ////////////////////////////////////
        //Get the new Access Token from backend
        function refreshAccessToken() {
            //If already waiting for the Promise, return it.
            if(deferedRefreshAccessToken) {
                return deferedRefreshAccessToken.promise
            }
            else {
                deferedRefreshAccessToken = $q.defer();
                //Get $http service with $injector to avoid circular dependency
                var http = $injector.get('$http');
                http({
                    method: "POST",
                    url: "api/auth/refresh"
                })
                    .then(function mySucces(response) {
                        var data = response.data;
                        var Auth = $injector.get('Auth');
                        if(data){
                            //Save new Access Token
                            if(Auth.s(data.accessToken, data.expire)) {
                                //Resolve Promise
                                deferedRefreshAccessToken.resolve(data.accessToken);
                                //Notify all subscribers
                                notifySubscribersNewAccessToken(data.accessToken);
                                deferedRefreshAccessToken = null;
                            }
                        }
                    }, function myError(error) {
                        deferedRefreshAccessToken.reject(error);
                        deferedRefreshAccessToken = null;
                    });
                return deferedRefreshAccessToken.promise;
            }
        }

        function getLocalAccessToken() {
            //...
            //get accesstoken from storage
        }

        function isAccessTokenExpired() {
            //...
            //Check if expiresAt is older then current Date
        }

        function saveToken(accessToken, expiresAt) {
            //...
            //Save token to storage (in html view, local storage etc.)
        }

        //This function will call all listeners (callbacks) and notify them that new access token is available
        //This is used to notify the web socket that new access token is available
        function notifySubscribersNewAccessToken(accessToken) {
            angular.forEach(subscribers, function(subscriber) {
                subscriber(accessToken);
            });
        }

        //Subscribe to this service. Be notifyed when access token is renewed
        function subscribe(callback) {
            subscribers.push(callback);
        }
    }
})();
