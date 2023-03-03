(function () {
    'use strict';
    angular
            .module('loginappApp')
            .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('response', {
            parent: 'app',
            url: '/response?param',
            data: {
                authorities: [],
                isAuthenticated: true,
                pageTitle: 'credential.upgrade.title'
            },
            params: {
                vnp_Amount: {
                    value: "1",
                    squash: true
                },
                vnp_BankCode: {
                    value: "1",
                    squash: true
                },
                vnp_BankTranNo: {
                    value: "1",
                    squash: true
                },
                vnp_CardType: {
                    value: "1",
                    squash: true
                },
                vnp_OrderInfo: {
                    value: "1",
                    squash: true
                },
                vnp_PayDate: {
                    value: "1",
                    squash: true
                },
                vnp_ResponseCode: {
                    value: "1",
                    squash: true
                },
                vnp_TmnCode: {
                    value: "1",
                    squash: true
                },
                vnp_TransactionNo: {
                    value: "1",
                    squash: true
                },
                vnp_TxnRef: {
                    value: "1",
                    squash: true
                },
                vnp_SecureHash: {
                    value: "1",
                    squash: true
                }
            },
            views: {
                'content@': {
                    templateUrl: 'app/credential/detail/upgrade/resTemplate/res-md-new.html'
                },
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
            }
        });
    }
})();