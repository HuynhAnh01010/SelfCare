(function () {
    'use strict';

    angular
        .module('loginappApp')

        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('mobile', {
            parent: 'account',
            url: '/user/mobile',
            data: {
                // authorities: [],
                // isAuthenticated: true,
                pageTitle: 'register.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/mobile/mobile.html',
                    controller: 'MobileController',
                    controllerAs: 'vm'
                },
                // 'leftContent@register': {
                //     templateUrl: 'app/layouts/left-content/left-content.html',
                // },
                // 'footerLanguage@register': {
                //     templateUrl: 'app/layouts/footer/footerLanguageLogin.html',
                // },
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
