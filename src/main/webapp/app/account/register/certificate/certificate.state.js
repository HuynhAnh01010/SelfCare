(function () {
    'use strict';

    angular
        .module('loginappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('registerCertificate', {
            parent: 'register',
            url: '/certificate',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/register/certificate/certificate.html',
                    controller: 'RegisterCertificateController',
                    controllerAs: 'vm'
                },
                'leftContent@registerCertificate': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                },
                'languageBottom@registerCertificate': {
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
