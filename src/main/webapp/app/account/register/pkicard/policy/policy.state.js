(function() {
    'use strict';

    angular
        .module('loginappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('pkicard.policy', {
            parent: 'pkicard',
            url: '/policy',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },

            views: {
                'content@': {
                    templateUrl: 'app/account/register/pkicard/policy/policy.html',
                    controller: 'PkiCardPolicyController',
                    controllerAs: 'vm'
                },
                'leftContent@pkicard.policy': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                },
                'languageBottom@pkicard.policy': {
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
