(function () {
    'use strict';

    angular.module('loginappApp')
            .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('credential.list.detail.renew', {
            parent: 'credential.list.detail',
            url: '/renew',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.renew.title'
            },
             views: {
                'content@': {
                    templateUrl: 'app/credential/detail/renew/renew-md.html',
                    controller: 'renewController',
                    controllerAs: 'vm'
                },
                'sideBar@credential.list.detail.renew': {
                    templateUrl: 'app/credential/detail/renew/renew-sidebar.html'
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