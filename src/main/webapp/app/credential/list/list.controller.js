(function () {
    'use strict';

    angular
            .module('loginappApp')
            .controller('ListCredentialController', ListCredentialController);

    ListCredentialController.$inject = ['$scope', 'Principal', '$state', 'Auth', 'CredentialService', '$stateParams', 'storageService'];

    function ListCredentialController($scope, Principal, $state, Auth, CredentialService, $stateParams, storageService) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        let certs = [];
        
        

        getCredetialList();

        function getCredetialList() {
            CredentialService.credentialList({

            }).then(function (res) {
                console.log(res);
                vm.certs = res.data.certs;
                vm.count;
                storageService.setData(vm.certs);
                certs = storageService.getData();


                $scope.searchTerm = "";
                $scope.numberToDisplay = 5;

                $scope.logEvents = [];
                
                for (var i = 0; i < certs.length; i++) {
//                    console.log("CERTS[i]: ", certs[i]);
                    if(certs[i].status != 'LOST' && certs[i].status != 'REVOKED' && certs[i].status != 'INITIALIZED') {
                        $scope.logEvents.push(certs[i]);
                    }
                }
                vm.size = $scope.logEvents.length;
                console.log("SIZE = ", vm.size);
                $scope.nextPage = function () {
                    if ($scope.numberToDisplay + 5 < $scope.logEvents.length) {
                        $scope.numberToDisplay += 5;
                        vm.count = $scope.logEvents.length - $scope.numberToDisplay;
                        console.log("vm.COUNT = ", vm.count);
                    } else {
                        $scope.numberToDisplay = $scope.logEvents.length;
                        vm.count = $scope.logEvents.length - $scope.numberToDisplay;
                        console.log("vm.COUNT = ", vm.count);
                    }
                };
                
                




                vm.error = null;
                vm.success = 'OK';
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }






    }
})();
