(function() {
    'use strict';

    angular
        .module('loginappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('pkicard', {
            parent: 'register',
            url: '/pkicard',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/register/pkicard/pkicard.html',
                    controller: 'PkiCardTokenController',
                    controllerAs: 'vm'
                },
                'leftContent@pkicard': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                }
                ,
                'languageBottom@pkicard': {
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
