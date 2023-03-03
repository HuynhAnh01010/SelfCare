(function() {
    'use strict';

    angular
        .module('loginappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('credential.list.history', {
            parent: 'credential.list',
            url: '/history/:id?page&size',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.history.title',
                navTitle: 'credential.nav.title'
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                size: {
                    value: '30',
                    squash: true
                }
            },
            views: {
                'content@': {
                    templateUrl: 'app/credential/history/history.html',
                    controller: 'HistoryCredentialController',
                    controllerAs: 'vm'
                },
                'sideBar@credential.list.history': {
                    templateUrl: 'app/credential/sidebar.html',
                    // controller: 'ListCredentialController',
                    // controllerAs: 'vm'
                },
                'loadMore@credential.list.history': {
                    templateUrl: 'app/layouts/common/load-more.html',
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('credential');
                    return $translate.refresh();
                }]
            }
        }).state('credential.list.detail.history', {
            parent: 'credential.list',
            url: '/detail/history/:id?page&size',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.history.title'
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                size: {
                    value: '30',
                    squash: true
                }
            },
            views: {
                'content@': {
                    templateUrl: 'app/credential/history/history.html',
                    controller: 'HistoryCredentialController',
                    controllerAs: 'vm'
                },
                'sideBar@credential.list.detail.history': {
                    templateUrl: 'app/credential/sidebar.html',
                    // controller: 'ListCredentialController',
                    // controllerAs: 'vm'
                },
                'loadMore@credential.list.detail.history': {
                    templateUrl: 'app/layouts/common/load-more.html',
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('credential');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
