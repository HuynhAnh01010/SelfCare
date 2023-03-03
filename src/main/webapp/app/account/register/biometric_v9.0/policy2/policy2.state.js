(function() {
    'use strict';

    angular
        .module('loginappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('policy2', {
            parent: 'policy1',
            url: '/policy2',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },
           
            views: {
                'content@': {
                    templateUrl: 'app/account/register/biometric/policy2/policy2.html',
                    controller: 'Policy2Controller',
                    controllerAs: 'vm'
                },
                'leftContent@policy2': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                }
                ,
                'languageBottom@policy2': {
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
