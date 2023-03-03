(function () {
    'use strict';

    angular
        .module('loginappApp')
        .factory('VnPayFactory', VnPayFactory);
    VnPayFactory.$inject = ['$resource', "API"];

    function VnPayFactory($resource, API) {
        return $resource('', {}, {
            'pay': {method: 'POST', url: 'api/vnpay/pay'},
            'result': {method: 'GET', url: 'api/vnpay/result'},
            'imagesUpload': {method: 'POST', url: 'api/e-identity/v1/images/upload'},
            'verification': {
                method: 'POST', url: 'api/e-identity/v1/verification',
                // interceptor: {
                //     response: function (response) {
                //         console.log("verification: ",response);
                //         // expose response
                //         return response.data;
                //     }
                // }
            },
            'processesGet': {method: 'POST', url: 'api/e-identity/v1/processes/get'},
        });
        // return {
        //     subjectCreate: $resource(`api/e-identity/v1/subjects/create`, {}, {}),
        //     processesCreate: $resource(`api/e-identity/v1/processes/create`, {}, {}),
        //     imagesUpload: $resource(`api/e-identity/v1/images/upload`, {}, {}),
        //     verification: $resource(`api/e-identity/v1/verification`, {}, {}),
        //     processesGet: $resource(`api/e-identity/v1/processes/get`, {}, {})
        // };
    }

    //
    // EIdentitySubjectCreateService.$inject = ['$resource', "API"];
    //
    // function EIdentitySubjectCreateService($resource, API) {
    //     var service = $resource(`api/e-identity/v1/subjects/create`, {}, {});
    //
    //     return service;
    // }
    //
    //
    // /////////////////////// -- Process Create -- ////////////////////////////
    // EIdentityProcessesCreateService.$inject = ['$resource', "API"];
    //
    // function EIdentityProcessesCreateService($resource, API) {
    //     var service = $resource(`api/e-identity/v1/processes/create`, {}, {});
    //
    //     return service;
    // }
    //
    // /////////////////////// -- Image Upload -- ////////////////////////////
    // EIdentityImagesUploadService.$inject = ['$resource', "API"];
    //
    // function EIdentityImagesUploadService($resource, API) {
    //     var service = $resource(`api/e-identity/v1/images/upload`, {}, {});
    //
    //     return service;
    // }
    //
    //
    // /////////////////////// -- Verification -- ////////////////////////////
    // EIdentityVerificationService.$inject = ['$resource', "API"];
    //
    // function EIdentityProcesseseGetService($resource, API) {
    //     var service = $resource(`api/e-identity/v1/verification`, {}, {});
    //
    //     return service;
    // }
    //
    //
    // /////////////////////// -- Process Get -- ////////////////////////////
    // EIdentityProcesseseGetService.$inject = ['$resource', "API"];
    //
    // function EIdentityVerificationService($resource, API) {
    //     var service = $resource(`api/e-identity/v1/processes/get`, {}, {});
    //
    //     return service;
    // }
})();
