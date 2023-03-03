(function() {
    'use strict';

    angular
        .module('loginappApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal','$state','Auth'];

    function HomeController ($scope, Principal, $state, Auth) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = function(){
        	$state.go('login');
        };
        vm.logout = function(){
        	Auth.logout();
        	$state.go('login');
        };
        vm.history = history;
//        $scope.$on('authenticationSuccess', function() {
//             getAccount();
//        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                console.log("account: {}",vm.account);
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function history () {
            $state.go('history');
        }
    }
})();
