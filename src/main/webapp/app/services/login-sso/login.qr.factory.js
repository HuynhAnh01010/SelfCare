(function () {
    'use strict';
    
    angular.module('loginappApp')
            .factory('LoginQRFactory', LoginQRFactory);
    
    LoginQRFactory.$inject = ['$http', '$localStorage', '$sessionStorage', '$q', '$state', '$mdDialog'];
    
    function LoginQRFactory($http, $localStorage, $sessionStorage, $q, $state, $mdDialog) {
        let service = {
            authLoginQRGetResult: authLoginQRGetResult,
            storeToken: storeToken
        };
        
        return service;
        
        function authLoginQRGetResult(data) {
            
            return $http.post('api/auth/qr-getResult', data)
                    .success(authLoginQRSuccess);
            
            function authLoginQRSuccess(data, status, headers) {
                console.log("LoginSuccess: ", data);
                if(data.data.error == 0) {
                    let token = {
                        access: data.data.accessToken,
                        refresh: data.data.refreshToken,
                        rememberMe: true,
                        expire: data.data.expiresIn
                    }
                    
                    service.storeToken(token, true);
                    return data.accessToken;
                }
            }
            
        }
        
        function storeToken(token, remember) {}
    }
})();