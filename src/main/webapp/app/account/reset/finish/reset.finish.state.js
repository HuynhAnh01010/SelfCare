(function() {
    'use strict';

    angular
        .module('loginappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('finishReset', {
            parent: 'public',
            url: '/account/reset/finish/?key&type&refer',
            data: {
                authorities: [],
                pageTitle: 'reset.finish.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/reset/finish/reset.finish.html',
                    controller: 'ResetFinishController',
                    controllerAs: 'vm'
                },
                'leftContent@finishReset': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                },
                'footerLanguage@finishReset': {
                    templateUrl: 'app/layouts/footer/footerLanguageLogin.html',
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('login');
                    $translatePartialLoader.addPart('reset');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
