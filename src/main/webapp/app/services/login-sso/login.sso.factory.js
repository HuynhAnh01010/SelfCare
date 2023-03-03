(function () {
    'use strict';

    angular
            .module('loginappApp')
            .factory('LoginSSOFactory', LoginSSOFactory);

    LoginSSOFactory.$inject = ['$resource', "API"];

    function LoginSSOFactory($resource, API) {

        return $resource('', {}, {
            'authLoginSSO': { method: 'GET', url: 'api/after-preLogin' },
            'authLoginQRCode': { method: 'POST', url: 'api/auth/login-with-qr' },
            'authLoginQRGetResult': { 
                method: 'POST', 
                url: 'api/auth/qr-getResult',
                ignoreLoadingBar: true
            }
        })
    }
})();