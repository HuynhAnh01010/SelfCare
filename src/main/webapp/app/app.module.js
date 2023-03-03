
(function () {
    'use strict';

    let app = angular.module('loginappApp', [
        'ngStorage',
        'tmh.dynamicLocale',
        'pascalprecht.translate',
        'ngResource',
        'ngCookies',
        'ngAria',
        'ngCacheBuster',
        'ui.bootstrap',
        'ui.bootstrap.datetimepicker',
        'ui.router',
        'angular-loading-bar',
        'ngMaterial', 'ngSanitize', 'ngMessages', 'ngAnimate',
        'ng.deviceDetector',
        'ngIntlTelInput',
        'ui.select',
        'ui.utils.masks',
    ])
            .config(['cfpLoadingBarProvider', function (cfpLoadingBarProvider) {
                    // cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
                    cfpLoadingBarProvider.spinnerTemplate = '<div id=\'loading\' class=\'overlay\' ng-show=\'$ctrl.loading.isLoading\'><div class=\'lds-ellipsis\'><div></div><div></div><div></div><div></div></div></div>';
//                    cfpLoadingBarProvider.includeBar = true;

                }])
            .run(run);



    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();
