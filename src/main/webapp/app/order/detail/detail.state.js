(function () {
    'use strict';
    
    angular
            .module('loginappApp')
            .config(stateConfig);
    
    stateConfig.$inject = ['$stateProvider'];
    
    function stateConfig ($stateProvider) {
        $stateProvider.state('order.list.detail', {
            parent: 'order.list',
            url: '/detail/:id',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'global.navbar.order',
                navTitle: 'global.navbar.order'
            },
            views: {
                'content@': {
                    templateUrl: 'app/order/detail/detail-md.html',
                    controller: 'DetailOrderController',
                    controllerAs: 'vm'
                },
                'sideBar@order.list.detail': {
                    templateUrl: 'app/order/detail/sidebar.html',
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('order');
                    return $translate.refresh();
                }]
            }
        });
    }
})();