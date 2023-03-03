(function () {
    'use strict';

    angular
            .module('loginappApp')
            .controller('ProfileController', ProfileController);

    ProfileController.$inject = ['$scope', 'Principal', '$state', 'Auth', 'storageService', '$cookies', '$window', '$timeout', '$mdDialog', '$q'];

    function ProfileController($scope, Principal, $state, Auth, storageService, $cookies, $window, $timeout, $mdDialog, $q) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = function () {
            $state.go('login');
        };
        vm.logout = function () {
            let deferred = $q.defer();
            Auth.logout();
//            Principal.identity(false).then(function (res) {
//                deferred.reject(res);
//            });
            $state.go('login');
        };
        vm.history = history;
        vm.popupChangePassword = popupChangePassword;
//        $scope.$on('authenticationSuccess', function() {
//             getAccount();
//        });

        vm.popupChangeEmail = popupChangeEmail;
//        vm.popupChangePhone = popupChangePhone;
        vm.popupChangeFullName = popupChangeFullName;
        vm.popupChangeIdentification = popupChangeIdentification;
        vm.popupChangeTwoFactorMethod = popupChangeTwoFactorMethod;
        vm.popupChangePhone = popupChangePhone;

        vm.plankChangePassword = plankChangePassword;
        vm.plankChangeFullname = plankChangeFullname;
        vm.plankChangeEmail = plankChangeEmail;


        function popupChangePassword() {
            storageService.setOwnerChangePassword(true);
            $state.go('account.profile.changePassword');
        }

//        getCookies();
//        
//        function getCookies() {
//            
//            console.log("Get Cokkie: ", x);
//        }

        getAccount();

        let saveData;

        function getAccount() {
            Principal.identity().then(function (res) {
                vm.account = res;
                console.log("account: ", vm.account);
//                let x = document.cookie;
//                console.log("cookies: ", x);
                storageService.setData(vm.account);
//                storageService.setUserName(res.username);
                saveData = vm.account;
                vm.isAuthenticated = Principal.isAuthenticated;
            }).catch(function (exception) {
                console.log(exception);

            });
        }

        function history() {
            $state.go('history');
        }

        function popupChangeEmail() {
            storageService.setOwnerChangeEmail(true);
            $state.go('account.profile.changeEmail');
        }

        function popupChangePhone() {
            storageService.setOwnerChangePhone(true);
            $state.go('account.profile.changePhone');
        }


        function popupChangeFullName() {
            storageService.setOwnerChangeInfo(true);
            $state.go('account.profile.changeFullName');
        }

        function popupChangeIdentification() {
            storageService.setOwnerChangeIdentifi(true);
            $state.go('account.profile.changeIdentifi');
        }

        function popupChangeTwoFactorMethod() {
            storageService.setOwnerChangeTwoFactorMethod(true);
            $state.go('account.profile.changeTwoFactorMethod');
        }

        function plankChangePassword() {
            let urlPlank = $window.location.host;
            console.log("CHeck link local: ", $window.location.host);
//            let href = 'https://' + 'iam.csign.cmc.vn' + '/auth/realms/user-manager/account/password';
            let href = 'https://' + 'iam.csign.cmc.vn' + '/auth/realms/rssp/account/password';
            $window.open(href);



//            href.target = '_blank';
        }

        function plankChangeFullname() {
//            let href = 'https://' + 'iam.csign.cmc.vn' + '/auth/realms/user-manager/account';
            let href = 'https://' + 'iam.csign.cmc.vn' + '/auth/realms/rssp/account';
            console.log("CHeck link local: ", $window.location.host);
            $window.open(href);
        }

        function plankChangeEmail() {
//            let href = 'https://' + 'iam.csign.cmc.vn' + '/auth/realms/user-manager/account';
            let href = 'https://' + 'iam.csign.cmc.vn' + '/auth/realms/rssp/account';
            console.log("CHeck link local: ", $window.location.host);
            $window.open(href);
        }

        $timeout(function () {
            window.scrollTo({top: 0, behavior: 'smooth'});
            $timeout(function () {
                Auth.logout();
                $state.go('login');
                cowndownLogout();
                clearTimeout($timeout);
            }, 1000);
        }, 30 * 60 * 1000);

        function cowndownLogout() {
            $mdDialog.show({
                controller: function () {
                    this.hide = function () {
                        hideModel();
                    };

                    function hideModel() {
                        $mdDialog.hide();
                    }
                },
                controllerAs: 'vm',
                templateUrl: 'app/account/profile/timeout/timeout.html',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: false
            }).then(function (res) {
            }).catch(function (err) {});
        }




    }
})();
