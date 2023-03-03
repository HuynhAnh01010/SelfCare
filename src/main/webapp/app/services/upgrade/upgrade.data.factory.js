//(function () {
//    'use strict';
//    
//    angular
//            .module('loginappApp')
//            .factory('UpgradeFactory',UpgradeFactory);
//    
//    UpgradeFactory.$inject = ['$resource', "API"];
//    
//    function UpgradeFactory($resouce, API) {
//        return $resource('', {}, {
//            'certificateProfile': { method: 'POST', url: 'api/public/systems/getCertificateProfiles'},
//            'getCertificateAuthorities': { method: 'POST', url: 'api/public/systems/getCertificateAuthorities'},
//            'getSigningProfiles': { method: 'POST', url: 'api/public/systems/getSigningProfiles'}
//        });
//    }
//})();