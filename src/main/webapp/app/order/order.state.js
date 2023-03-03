(function () {
    'use strict';

    angular
            .module('loginappApp')
            .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('order', {
            abstract: true,
            parent: 'app',
            url: '/order'
        });
    }
})();