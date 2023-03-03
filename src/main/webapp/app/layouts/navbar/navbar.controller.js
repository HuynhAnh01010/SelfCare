(function() {
    'use strict';

    angular
        .module('loginappApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal'];

    function NavbarController ($state, Auth, Principal) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        vm.login = login;
        vm.logout = logout;
        vm.home = home;
        vm.history = history;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        function home() {
            collapseNavbar();
            $state.go('home');
        }
        function history() {
            collapseNavbar();
            $state.go('history');
        }
        function login() {
            collapseNavbar();
            $state.go('login');
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('login');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            // $('html').removeClass('nav-open');
            if (nowuiDashboard.misc.navbar_menu_visible == 1) {
                $('html').removeClass('nav-open');
                nowuiDashboard.misc.navbar_menu_visible = 0;
                setTimeout(function() {
                    $('.navbar-toggle').removeClass('toggled');
                    $('#bodyClick').remove();
                }, 550);

            }
            vm.isNavbarCollapsed = true;
        }
    }
})();
