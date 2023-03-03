(function() {
    'use strict';

    angular
        .module('loginappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('authenticateEmailPhone', {
            parent: 'policy2',
            url: '/authenticate-email-phone',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },

            views: {
                'content@': {
                    templateUrl: 'app/account/register/biometric/authenticate/authenticate.html',
                    controller: 'AuthenticateEmailPhoneController',
                    controllerAs: 'vm'
                },
                'leftContent@authenticateEmailPhone': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                },
                'languageBottom@authenticateEmailPhone': {
                    templateUrl: 'app/components/language/language-bottom.html',
                }
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
