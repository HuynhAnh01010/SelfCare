(function () {
    'use strict';

    angular
            .module('loginappApp')
            .controller('HistoryCredentialController', HistoryCredentialController);

    HistoryCredentialController.$inject = ['$scope', 'Principal', '$state', 'Auth', 'CredentialService', '$stateParams'];

    function HistoryCredentialController($scope, Principal, $state, Auth, CredentialService, $stateParams) {
        var vm = this;
        vm.historis = [];
        vm.credentialHisBack = function () {
            $state.go("credential.list");
        }
        if ($stateParams.id == null || $stateParams.id == "") {
            $state.go("credential.list");
        }
        vm.id = $stateParams.id;
        vm.page = 1;
        vm.size = 15;
        vm.ownerHistory = [];
        vm.nextPage = function () {
            vm.page += 1;
            getCredetialHistory();
        };
        vm.prevPage = function () {
            if (vm.page > 1) {
                $state.go('.', {page: vm.page - 1});
            }
        };
        getCredetialHistory();
        function getCredetialHistory() {
            CredentialService.credentialHistory({page: vm.page, size: vm.size, id: $stateParams.id}).then(function (res) {
//                console.log(res);
                vm.historis = vm.historis.concat(res.data.records);
                vm.data = res.data;
                vm.error = null;
                vm.success = 'OK';
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }



    }
})();
