(function() {
    'use strict';

    angular
        .module('loginappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('policy1', {
            parent: 'biometric',
            url: '/policy1',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },

            views: {
                'content@': {
                    templateUrl: 'app/account/register/biometric/policy1/policy1.html',
                    controller: 'Policy1Controller',
                    controllerAs: 'vm'
                },
                'leftContent@policy1': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                }
                ,
                'footerLanguage@policy1': {
                    templateUrl: 'app/layouts/footer/footerLanguageLogin.html',
                },
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
