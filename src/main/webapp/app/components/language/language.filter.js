(function () {
    'use strict';

    angular
        .module('loginappApp')
        .filter('findLanguageFromKey', findLanguageFromKey)
        .directive('findLanguageCurrent', findLanguageCurrent)
        .directive('myLanguage', myLanguage);

    function findLanguageFromKey() {
        return findLanguageFromKeyFilter;

        function findLanguageFromKeyFilter(lang) {
            return {
                'ca': {
                    'name': 'ca',
                    'desc': 'desc',
                    'img': 'img'
                },
                'cs': 'Český',
                'da': 'Dansk',
                'de': 'Deutsch',
                'el': 'Ελληνικά',
                'en': {
                    'name': 'English',
                    'desc': 'English',
                    'img': 'english.png'
                },
                'es': 'Español',
                'et': 'Eesti',
                'fr': 'Français',
                'gl': 'Galego',
                'hu': 'Magyar',
                'hi': 'हिंदी',
                'hy': 'Հայերեն',
                'it': 'Italiano',
                'ja': '日本語',
                'ko': '한국어',
                'mr': 'मराठी',
                'nl': 'Nederlands',
                'pl': 'Polski',
                'pt-br': 'Português (Brasil)',
                'pt-pt': 'Português',
                'ro': 'Română',
                'ru': 'Русский',
                'sk': 'Slovenský',
                'sr': 'Srpski',
                'sv': 'Svenska',
                'ta': 'தமிழ்',
                'th': 'ไทย',
                'tr': 'Türkçe',
                'vi': {
                    'name': 'Tiếng Việt',
                    'desc': 'Tiếng Việt',
                    'img': 'vietname.png'
                },
                'zh-cn': '中文（简体）',
                'zh-tw': '繁體中文'
            }[lang];
        }
    }

    myLanguage.$inject = ['$rootScope', '$mdDialog', '$injector','$translate','URL_NEED_LOAD'];
    findLanguageCurrent.$inject = ['$rootScope', '$mdDialog', '$filter', 'LanguageService'];

    function findLanguageCurrent($rootScope, $mdDialog, $filter, LanguageService) {
        return {
            // required:'LanguageController',
            template: '{{lang}}',
            link: function (scope, element, attr) {
                LanguageService.getCurrent().then(function (language) {
                    var userLanguage = $filter('findLanguageFromKey')(language);
                    scope.lang = userLanguage.name;
                });
                scope.$root.$on('$localeChangeSuccess', function () {
                    LanguageService.getCurrent().then(function (language) {
                        var userLanguage = $filter('findLanguageFromKey')(language);
                        scope.lang = userLanguage.name;
                    });
                });

            }
        };
    }

    function myLanguage($rootScope, $mdDialog, $injector,$translate,URL_NEED_LOAD) {
        return {
            // required:'LanguageController',
            link: function (scope, element, attr) {
                element.bind('click', function (event) {
                    var modalScope = $rootScope.$new();
                    $mdDialog.show({
                        controller: 'LanguageController',
                        templateUrl: 'app/components/language/language.html',
                        parent: angular.element(document.body),
                        // targetEvent: event,
                        clickOutsideToClose: true,
                        fullscreen: false, // Only for -xs, -sm breakpoints.
                        // preserveScope: true,
                    })
                        .then(function (answer) {
                            for(var i=0; i< URL_NEED_LOAD.length; i++){
                                if(window.location.hash.match(URL_NEED_LOAD[i])) {
                                    window.location.reload();
                                    break;
                                }
                            }
                        }, function () {
                            console.log("cancel: ");
                            // $scope.status = 'You cancelled the dialog.';
                        });
                });
            }
        };
    }

})();
