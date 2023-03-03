(function() {
    'use strict';

    angular
        .module('loginappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('history', {
            parent: 'app',
            url: '/history?page&size',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'global.navbar.actionHistory',
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
                    templateUrl: 'app/history/history.html',
                    controller: 'HistoryController',
                    controllerAs: 'vm'
                },
                'loadMore@history': {
                    templateUrl: 'app/layouts/common/load-more.html',
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('history');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
