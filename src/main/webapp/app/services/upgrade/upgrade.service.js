//(function () {
//    'use strict';
//    
//    angular
//            .module('loginappApp')
//            .factory('UpgradeService', UpgradeService);
//    
//    UpgradeService.$inject = ['UpgradeFactory'];
//    
//    function UpgradeService(UpgradeFactory) {
//        var service = {
//            certificateProfile: certificateProfile,
//            getCertificateAuthorities: getCertificateAuthorities,
//            getSigningProfiles: getSigningProfiles,
//        }; 
//        
//        return service;
//        
//        function certificateProfile(key, callback) {
//            console.log("owner info");
//            var cb = callback || angular.noop;
//
//            return UpgradeFactory.certificateProfile(key,
//                function (res) {
//                    return cb(res)
//                },
//                function (err) {
//                    return cb(err);
//                }.bind(this)).$promise;
//
//        }
//        
//        function getCertificateAuthorities(key, callback) {
//            console.log("owner info");
//            var cb = callback || angular.noop;
//
//            return UpgradeFactory.getCertificateAuthorities(key,
//                function (res) {
//                    return cb(res)
//                },
//                function (err) {
//                    return cb(err);
//                }.bind(this)).$promise;
//
//        }
//        
//        function getSigningProfiles(key, callback) {
//            console.log("owner info");
//            var cb = callback || angular.noop;
//
//            return UpgradeFactory.getSigningProfiles(key,
//                function (res) {
//                    return cb(res)
//                },
//                function (err) {
//                    return cb(err);
//                }.bind(this)).$promise;
//
//        }
//    }
//})();