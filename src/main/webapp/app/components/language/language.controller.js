(function() {
    'use strict';

    angular
        .module('loginappApp')
        .controller('LanguageController', LanguageController);

    LanguageController.$inject = ['$rootScope','$scope','$translate', 'LanguageService', 'tmhDynamicLocale','$mdDialog'];

    function LanguageController ($rootScope,$scope,$translate, LanguageService, tmhDynamicLocale,$mdDialog) {
        var vm = this;

        vm.changeLanguage = changeLanguage;
        vm.languages = null;

        LanguageService.getAll().then(function (languages) {
            // console.log("languages: ",languages);
            vm.languages = languages;
        });

        function changeLanguage (languageKey) {
            $mdDialog.hide();
            $translate.use(languageKey);
            tmhDynamicLocale.set(languageKey);
            $rootScope.useLanguage = languageKey;
            // $rootScope.$broadcast('test');
            // $scope.$broadcast('test');
        }
    }
})();
