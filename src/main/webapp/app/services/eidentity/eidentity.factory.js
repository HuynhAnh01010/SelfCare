(function () {
    'use strict';

    angular
        .module('loginappApp')
        .factory('EIdentityFactory', EIdentityFactory);

    /////////////////////// -- Subject Create -- ////////////////////////////
    EIdentityFactory.$inject = ['$resource', "API"];

    function EIdentityFactory($resource, API) {
        return $resource('', {}, {
            // 'subjectCreate': {method: 'POST', url: 'api/e-identity/v1/subjects/create/error',
            'subjectCreate': {method: 'POST', url: 'api/e-identity/v1/subjects/create'},
            'processesCreate': {method: 'POST', ignoreLoadingBar: true,url: 'api/e-identity/v1/processes/create'},
            'processesCreateAndVerification': {method: 'POST', ignoreLoadingBar: true,url: 'api/e-identity/v1/processes/create-verification'},
            'imagesUpload': {method: 'POST', ignoreLoadingBar: true,url: 'api/e-identity/v1/images/upload'},
            'verification': {
                method: 'POST', url: 'api/e-identity/v1/verification',
            },
            'processesGet': {method: 'POST',ignoreLoadingBar: true, url: 'api/e-identity/v1/processes/get'},
            'processesSelfRevise': {method: 'POST',ignoreLoadingBar: false, url: 'api/e-identity/v1/processes/self-revise'},
            'processesSelfReviseAndTseRegister': {method: 'POST',ignoreLoadingBar: false, url: 'api/e-identity/v1/processes/self-revise-and-tse-register'},
        });
    }

})();
