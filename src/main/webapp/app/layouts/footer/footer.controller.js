(function() {
    'use strict';

    angular
        .module('loginappApp')
        .controller('FooterController', FooterController);

    FooterController.$inject = ['$state', 'Auth', 'Principal'];

    function FooterController ($state, Auth, Principal) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

    }
})();
