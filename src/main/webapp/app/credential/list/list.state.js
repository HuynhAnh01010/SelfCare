(function () {
    'use strict';
    angular
            .module('loginappApp')
            .config(stateConfig);
    stateConfig.$inject = ['$stateProvider', ];
    function stateConfig($stateProvider) {
        $stateProvider.state('credential.list', {
            parent: 'credential',
            url: '/list',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'global.navbar.credential',
            },
            views: {
                'content@': {
                    templateUrl: 'app/credential/list/list.html',
                    controller: 'ListCredentialController',
                    controllerAs: 'vm'
                },
                'sideBar@credential.list': {
                    templateUrl: 'app/credential/sidebar.html',
                },
                'loadMore@credential.list': {
                    templateUrl: 'app/layouts/paging/paging.html'
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
