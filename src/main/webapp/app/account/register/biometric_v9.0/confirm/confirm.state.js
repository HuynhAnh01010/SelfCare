(function() {
    'use strict';

    angular
        .module('loginappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('userConfirm', {
            parent: 'reachEmailPhone',
            url: '/confirm',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },

            views: {
                'content@': {
                    templateUrl: 'app/account/register/biometric/confirm/confirm.html',
                    controller: 'UserConfirmController',
                    controllerAs: 'vm'
                },
                'leftContent@userConfirm': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                },
                'languageBottom@userConfirm': {
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
