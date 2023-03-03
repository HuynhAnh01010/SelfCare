(function() {
    'use strict';

    angular
        .module('loginappApp')
        .factory('stateHandler', stateHandler);

    stateHandler.$inject = ['$rootScope', '$state', '$sessionStorage', '$translate', 'LanguageService', 'translationHandler', '$window',
        'Auth', 'Principal', 'VERSION','TITLE'];

    function stateHandler($rootScope, $state, $sessionStorage, $translate, LanguageService, translationHandler, $window,
        Auth, Principal, VERSION,TITLE) {
        return {
            initialize: initialize
        };

        function initialize() {
            $rootScope.VERSION = VERSION;

            var stateChangeStart = $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams, fromState) {
                $rootScope.toState = toState;
                $rootScope.toStateParams = toStateParams;
                $rootScope.fromState = fromState;

                // Redirect to a state with an external URL (http://stackoverflow.com/a/30221248/1098564)
                if (toState.external) {
                    event.preventDefault();
                    $window.open(toState.url, '_self');
                }

                if (Principal.isIdentityResolved()) {
                    Auth.authorize();
                }

                // Update the language
                LanguageService.getCurrent().then(function (language) {
//                    console.log("language: ",language);
                    $translate.use(language);
                    $rootScope.useLanguage = language;

                });
            });

            var stateChangeSuccess = $rootScope.$on('$stateChangeSuccess',  function(event, toState, toParams, fromState, fromParams) {
                // console.log("languageCurrent: ",$translate.use());
                var titleKey = 'global.title' ;

                // Set the page title key to the one configured in state or use default one
                if (toState.data.pageTitle) {
                    titleKey = toState.data.pageTitle;
                }
                translationHandler.updateTitle(titleKey);

                // var navKey = 'global.nav.title' ;
                //
                // // Set the page title key to the one configured in state or use default one
                // if (toState.data.navTitle) {
                //     navKey = toState.data.navTitle;
                // }
                // translationHandler.updateNavTitle(navKey);
            });

            $rootScope.$on('$destroy', function () {
                if(angular.isDefined(stateChangeStart) && stateChangeStart !== null){
                    stateChangeStart();
                }
                if(angular.isDefined(stateChangeSuccess) && stateChangeSuccess !== null){
                    stateChangeSuccess();
                }
            });
        }
    }
})();
