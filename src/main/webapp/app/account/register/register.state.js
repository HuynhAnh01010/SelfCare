//(function () {
//    'use strict';
//
//    angular
//        .module('loginappApp')
//
//        .config(stateConfig);
//
//    stateConfig.$inject = ['$stateProvider'];
//
//    function stateConfig($stateProvider) {
//        $stateProvider.state('register', {
//            parent: 'account',
//            url: '/user/register',
//            data: {
//                // authorities: [],
//                // isAuthenticated: true,
//                pageTitle: 'register.title'
//            },
//            views: {
//                'content@': {
//                    templateUrl: 'app/account/register/register.html',
//                    controller: 'RegisterController',
//                    controllerAs: 'vm'
//                },
//                'leftContent@register': {
//                    templateUrl: 'app/layouts/left-content/left-content.html',
//                },
//                'footerLanguage@register': {
//                    templateUrl: 'app/layouts/footer/footerLanguageLogin.html',
//                },
//            },
//            resolve: {
//                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
//                    $translatePartialLoader.addPart('register');
//                    return $translate.refresh();
//                }]
//            }
//        });
//    }
//})();
