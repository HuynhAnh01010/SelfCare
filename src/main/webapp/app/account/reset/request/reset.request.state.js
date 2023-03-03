(function() {
    'use strict';

    angular
        .module('loginappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('resetPass', {
            parent: 'public',
            url: '/account/reset/password',
            data: {
                authorities: [],
                pageTitle: 'reset.request.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/reset/request/reset.request.html',
                    controller: 'RequestResetController',
                    controllerAs: 'vm'
                },
                'leftContent@resetPass': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                },
                'footerLanguage@resetPass': {
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
