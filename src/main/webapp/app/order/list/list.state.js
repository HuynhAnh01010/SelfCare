(function () {
    'use strict';
    
    angular
            .module('loginappApp')
            .config(stateConfig);
    
    stateConfig.$inject = ['$stateProvider'];
    
    function stateConfig($stateProvider) {
        $stateProvider.state('order.list', {
            parent: 'order',
            url: '/list?page&size',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'global.navbar.order',
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
                    templateUrl: 'app/order/list/list-md.html',
                    controller: 'orderController',
                    controllerAs: 'vm'
                },
                'sideBar@order.list': {
                    templateUrl: 'app/order/sidebar.html'
                },
                'loadMore@order.list': {
                    templateUrl: 'app/layouts/common/load-more.html'
                },
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('order');
                    return $translate.refresh();
                }]
            }
        })
    }
})();