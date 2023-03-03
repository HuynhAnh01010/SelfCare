(function () {
    'use strict';

    angular.module('loginappApp')
            .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('credential.list.detail.upgrade', {
            parent: 'credential.list.detail',
            url: '/upgrade',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.upgrade.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/credential/detail/upgrade/upgrade-md.html',
                    controller: 'UpgradeController',
                    controllerAs: 'vm'
                },
                'sideBar@credential.list.detail.upgrade': {
                    templateUrl: 'app/credential/detail/upgrade/sidebar.html'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('credential');
                        return $translate.refresh();
                    }]
            },
            link: {
                post: {
                    url: '/response'
                }
            }
            
        });

        
    }
})();