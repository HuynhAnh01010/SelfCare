(function () {
    'use strict';

    angular.module('loginappApp')
            .factory('LoginQRService', LoginQRService);

    LoginQRService.$inject = ['$q', 'Principal', 'LoginQRFactory'];

    function LoginQRService($q, Principal, LoginQRFactory) {
        let service = {
            authLoginQRGetResult: authLoginQRGetResult
        };
        return service;

        function authLoginQRGetResult(data, callback) {
            var cb = callback || angular.noop;
            var deferred = $q.defer();

            LoginQRFactory.authLoginQRGetResult(data)
                    .then(function (data) {
                        console.log("Login Then: ", data);
                        Principal.identity(true).then(function (account) {
                            console.log("loginThen account", account);
                            // After the login the language will be changed to
                            // the language selected by the user during his registration
//                    if (account!== null) {
//                        $translate.use(account.langKey).then(function () {
//                            $translate.refresh();
//                        });
//                    }
                            deferred.resolve(data);
                        });
                        return cb();
                    }).catch(function (err) {
                this.logout();
                deferred.reject(err);
                return cb(err);
            }.bind(this));
        }
    }
})();