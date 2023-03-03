(function() {
    'use strict';

    angular
        .module('loginappApp')
        .controller('HistoryController', HistoryController);

    HistoryController.$inject = ['$scope', 'Principal','$state','Auth','HistoryService','$stateParams'];

    function HistoryController ($scope, Principal, $state, Auth,HistoryService,$stateParams) {
        var vm = this;

        vm.page = 1;
        vm.size = 5;

        vm.ownerHistory = [];
        vm.data = [];

        vm.nextPage = function() {
            vm.page = vm.page + 1;
            getOwnerLogging();
        };
        vm.prevPage = function() {
            if (vm.page > 1) {
                $state.go('.', {page: vm.page - 1});
            }
        };

        getOwnerLogging();

        function getOwnerLogging() {
//            console.log("logging");
            HistoryService.ownerLogging({page: vm.page, size: vm.size}).then(function (res) {
                console.log(res);
                vm.ownerHistory = vm.ownerHistory.concat(res.data.data.records);

                vm.data = res.data.data;
                vm.error = null;
                vm.success = 'OK';
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });

        }


    }
})();
