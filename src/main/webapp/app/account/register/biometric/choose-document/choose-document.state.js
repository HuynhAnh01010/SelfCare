(function () {
    'use strict';

    angular
        .module('loginappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('chooseDocument', {
            parent: 'reachEmailPhone',
            url: '/document',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/register/biometric/choose-document/choose-document.html',
                    controller: 'ChooseDocumentController',
                    controllerAs: 'vm'
                },
                'leftContent@chooseDocument': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                },
                'languageBottom@chooseDocument': {
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
