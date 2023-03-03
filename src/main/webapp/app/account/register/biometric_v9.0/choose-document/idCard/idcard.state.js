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
        $stateProvider.state('idcard', {
            parent: 'chooseDocument',
            url: '/idcard',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/register/biometric/choose-document/idCard/idcard.html',
                    controller: 'DocumentIdCardController',
                    controllerAs: 'vm'
                },
                'leftContent@idcard': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                },
                'languageBottom@idcard': {
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
