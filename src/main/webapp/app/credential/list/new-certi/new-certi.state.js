(function () {
    'use strict';

    angular.module('loginappApp')
            .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('credential.list.newCerti', {
            parent: 'credential.list',
            url: '/new-certificate',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.newCerti.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/credential/list/new-certi/new-certi-md.html',
                    controller: 'newCertiController',
                    controllerAs: 'vm'
                },
                'sideBar@credential.list.newCerti': {
                    templateUrl: 'app/credential/list/new-certi/sidebar.html'
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