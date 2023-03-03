(function () {
    'use strict';

    angular
        .module('loginappApp').factory('TseFactory', TseFactory);
        // .factory('SystemGetCertificateAuthoritiesService', SystemGetCertificateAuthoritiesService)
        // .factory('SystemGetCertificateProfilesService', SystemGetCertificateProfilesService)
        // .factory('SystemGetSigningProfilesService', SystemGetSigningProfilesService)
        // .factory('SystemGetStatesOrProvincesService', SystemGetStatesOrProvincesService)


    // TseFactory.$inject = ['$resource'];

    TseFactory.$inject = ['$resource', "API"];

    function TseFactory($resource, API) {
        return $resource('', {}, {
            'ownerCreate': { method: 'POST', url: 'api/tse/owner/create' },
            'tokenRefresh': { method: 'POST', url: 'api/token/refresh' },
            'credentialChangeAuthInfo': { method: 'POST', url: 'api/tse/credentials/changeAuthInfo' },
            'credentialChangePassphrase': { method: 'POST', url: 'api/tse/credentials/changePassphrase' },
            'credentialResetPassphrase': { method: 'POST', url: 'api/tse/credentials/resetPassphrase' },
            'credentialChangeEmail': { method: 'POST', url: 'api/tse/credentials/changeEmail' },
            'credentialChangePhone': { method: 'POST', url: 'api/tse/credentials/changePhone' },
            'credentialSendOtp': { method: 'POST', url: 'api/tse/credentials/sendOTP' },
            'credentialInfo': { method: 'GET', url: 'api/credentials/info' },
            'credentialHistory': { method: 'GET', url: 'api/credentials/history' },
            'credentialList': { method: 'GET', url: 'api/credentials/list' },
            'ownerChangePassword': { method: 'POST', url: 'api/tse/owner/change-password' },
            'ownerHistory': { method: 'GET', url: 'api/owner/history' },
            'ownerInfo': { method: 'GET', ignoreLoadingBar: true, url: 'api/owner/info' },
            
//            systems
            'systemGetCertificateAuthorities': { method: 'GET', url: 'api/systems/getCertificateAuthorities' },
            'systemGetCertificateProfiles': { method: 'GET', url: 'api/systems/getCertificateProfiles/:name' },
            'systemGetSigningProfiles': { method: 'GET', url: 'api/systems/getSigningProfiles' },
            'systemsGetCountries': { method: 'GET', url: 'api/systems/getCountries' },
            'systemsGetStatesOrProvinces': { method: 'Post', url: 'api/systems/getStatesOrProvinces'},
            
//            systems
            'systemGetStatesOrProvinces': { method: 'GET', url: 'api/public/system/getStatesOrProvinces/:name' },
            'authTwoFactor': { method: 'GET', url: 'api/auth/two-factor/:username/:userType' },
            'authAuthenticate': { method: 'POST', cancellable: true, url: 'api/auth/authenticate' },
            'accountResetPasswordInit': { method: 'POST', url: 'api/account/reset_password/init' },
            'accountResetPasswordFinish': { method: 'POST', url: 'api/account/reset_password/finish' },
            'ownerChangeEmail': {method: 'POST', url: 'api/tse/owner/changeEmail'},
            'ownerChangeInfo': {method: 'POST', url: 'api/tse/owner/changeInfo'},
            
            'credentialsUpgrade': { method: 'POST', url: 'api/credentials/upgrade'},
            'ordersCheckout': { method: 'POST', url: 'api/orders/checkout'},
            'getPaymentProvider': { method: 'GET', url: 'api/systems/getPaymentProviders'},
            'credentialsRenew': { method: 'POST', url: 'api/credentials/renew'},
            'credentialsIssue': {method: 'POST', url: 'api/credentials/issue'},
            'credentialsEnroll':{method: 'POST', url: 'api/credentials/enroll'},
            'credentialsApprove': {method: 'POST', url: 'api/credentials/approve'},
            'credentialsImport': {method: 'POST', url: 'api/credentials/import'},
            
            'ownerSendOTP': {method: 'POST', url: 'api/owner/sendOTP'},
            
            'authPreLogin': {method: 'POST', url: 'api/auth/preLogin'},
            'authLoginSSO': {method: 'POST', url: 'api/auth/loginSSO'},
            
            'rePostData': {method: 'POST', url: 'api/request/redirectLoginSSO'},
            
            'getEnTerms': {method: 'GET', url: '/api/register/en/terms'}
            
            
        });

    }

})();
