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
        $stateProvider.state('registerCertificateDetail', {
            parent: 'registerCertificate',
            url: '/detail',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/register/certificate/detail/detail.html',
                    controller: 'RegisterCertificateDetailController',
                    controllerAs: 'vm'
                },
                'leftContent@registerCertificateDetail': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                },
                'languageBottom@registerCertificateDetail': {
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
