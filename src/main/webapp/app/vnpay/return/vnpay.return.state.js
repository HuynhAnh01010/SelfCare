(function () {
    'use strict';

    angular
        .module('loginappApp')

        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('vnpay-result', {
            parent: 'vnpay',
            url: '/result?vnp_Amount&vnp_BankCode&vnp_BankTranNo&vnp_CardType&&vnp_OrderInfo&vnp_PayDate&vnp_ResponseCode' +
                '&vnp_TmnCode&vnp_TransactionNo&vnp_TxnRef&vnp_SecureHashType&vnp_SecureHash',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/vnpay/return/return.html',
                    controller: 'VnPayResultController',
                    controllerAs: 'vm'
                },

            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                    $translatePartialLoader.addPart('register');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
