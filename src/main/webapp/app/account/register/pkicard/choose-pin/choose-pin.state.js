(function () {
    'use strict';

    angular
        .module('loginappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('pkicard.policy.choosepin', {
            parent: 'pkicard.policy',
            url: '/choosepin',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/register/pkicard/choose-pin/choose-pin.html',
                    controller: 'PkiCardChoosePinController',
                    controllerAs: 'vm'
                },
                'leftContent@pkicard.policy.choosepin': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                },
                'languageBottom@pkicard.policy.choosepin': {
                    templateUrl: 'app/components/language/language-bottom.html',
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('register');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
