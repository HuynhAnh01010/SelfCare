(function () {
    'use strict';

    angular
        .module('loginappApp')
        .factory('RegisterCertificateService', RegisterCertificateService);

    RegisterCertificateService.$inject = ['SystemGetCertificateAuthoritiesService','Certificate',
        'SystemGetSigningProfilesService','SystemGetCertificateProfilesService','SystemGetStatesOrProvincesService'];

    function RegisterCertificateService(SystemGetCertificateAuthoritiesService,Certificate,
                                        SystemGetSigningProfilesService,SystemGetCertificateProfilesService ,SystemGetStatesOrProvincesService) {
        var service = {
            certificateAuthorities: certificateAuthorities,
            certificateProfiles: certificateProfiles,
            statesOrProvinces: statesOrProvinces,
            signingProfiles: signingProfiles,
            createCertificate: createCertificate,
        };
        return service;


        function certificateAuthorities(callback) {
            var cb = callback || angular.noop;

            return SystemGetCertificateAuthoritiesService.get({},
                function (res) {
                    return cb(res)
                },
                function (err) {
                    return cb(err);
                }
                    .bind(this)).$promise;

        }

        function certificateProfiles(key,callback) {
            var cb = callback || angular.noop;

            return SystemGetCertificateProfilesService.get(key,
                function (res) {
                    return cb(res)
                },
                function (err) {
                    return cb(err);
                }
                    .bind(this)).$promise;

        }

        function statesOrProvinces(key,callback) {
            var cb = callback || angular.noop;

            return SystemGetStatesOrProvincesService.get(key,
                function (res) {
                    return cb(res)
                },
                function (err) {
                    return cb(err);
                }
                    .bind(this)).$promise;

        }
        function signingProfiles(key,callback) {
            var cb = callback || angular.noop;

            return SystemGetSigningProfilesService.get(key,
                function (res) {
                    return cb(res)
                },
                function (err) {
                    return cb(err);
                }
                    .bind(this)).$promise;

        }

        function createCertificate(certificate, callback) {
            var cb = callback || angular.noop;

            return Certificate.save(certificate,
                function () {
                    return cb(certificate);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }
    }
})();
