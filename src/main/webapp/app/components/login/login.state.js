(function () {
    'use strict';

    angular
            .module('loginappApp')
            .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('login', {
            parent: 'app',
            url: '/?msg',
            data: {
                authorities: [],
                pageTitle: 'login.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/components/login/login-cmc.html',
                    controller: 'LoginController',
                    controllerAs: 'vm'
                },
                'leftContent@login': {
                    templateUrl: 'app/layouts/left-content/left-content-cmc.html',
                },
                'footerLanguage@login': {
                    templateUrl: 'app/layouts/footer/footerLanguageRegister.html',
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('login');
                        return $translate.refresh();
                    }]
            }
        });


    }
})();
