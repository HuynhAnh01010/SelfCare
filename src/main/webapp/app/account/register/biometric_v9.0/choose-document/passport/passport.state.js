(function () {
    'use strict';

    angular
        .module('loginappApp')
        .factory("policy", function () {
            return {};
        })
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('passport', {
            parent: 'chooseDocument',
            url: '/passport',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/register/biometric/choose-document/passport/passport.html',
                    controller: 'DocumentPassportController',
                    controllerAs: 'vm'
                },
                'leftContent@passport': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                },
                'languageBottom@passport': {
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
