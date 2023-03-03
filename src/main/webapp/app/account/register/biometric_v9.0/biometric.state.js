(function() {
    'use strict';

    angular
        .module('loginappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('biometric', {
            parent: 'register',
            url: '/biometric',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/register/biometric/biometric.html',
                    controller: 'BiometricController',
                    controllerAs: 'vm'
                },
                'leftContent@biometric': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                }
                ,
                'languageBottom@biometric': {
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
