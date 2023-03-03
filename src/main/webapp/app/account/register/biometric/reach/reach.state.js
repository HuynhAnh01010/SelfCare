(function() {
    'use strict';

    angular
        .module('loginappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('reachEmailPhone', {
            parent: 'register',
            // parent: 'authenticateEmailPhone',
            url: '/reach',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },

            views: {
                'content@': {
                    templateUrl: 'app/account/register/biometric/reach/reach.html',
                    controller: 'ReachEmailPhoneController',
                    controllerAs: 'vm'
                },
                'leftContent@reachEmailPhone': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                },
                'footerLanguage@reachEmailPhone': {
                    templateUrl: 'app/layouts/footer/footerLanguageLogin.html',
                },
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('register');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
