(function () {
    'use strict';

    angular
            .module('loginappApp')
            .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('credential.list.detail.remakeSigningCounter', {
            parent: 'credential.list.detail',
            url: '/remake-signing-counter',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.remake.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/credential/detail/signing-counter/signingCounter-md.html',
                    controller: 'sncController',
                    controllerAs: 'vm'
                },
                'sideBar@credential.list.detail.remakeSigningCounter': {
                    templateUrl: 'app/credential/detail/signing-counter/signingCounter-sidebar.html'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('credential');
                        return $translate.refresh();
                    }]
            }
        });
    }
})();