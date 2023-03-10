(function () {
    'use strict';

    angular
        .module('loginappApp')
        .factory('LanguageService', LanguageService);

    LanguageService.$inject = ['$q', '$http', '$translate', 'LANGUAGES'];

    function LanguageService ($q, $http, $translate, LANGUAGES) {
        var service = {
            getAll: getAll,
            getCurrent: getCurrent,
            getLanguage: getLanguage
        };

        return service;

        function getAll () {
            var deferred = $q.defer();
            deferred.resolve(LANGUAGES);
            return deferred.promise;
        }

        function getCurrent () {
            var deferred = $q.defer();
            var language = $translate.storage().get('NG_TRANSLATE_LANG_KEY');

            deferred.resolve(language);

            return deferred.promise;
        }

        function getLanguage () {
            var language = $translate.storage().get('NG_TRANSLATE_LANG_KEY');

            return language;
        }
    }
})();
