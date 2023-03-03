(function () {
    'use strict';
    angular
            .module('loginappApp')
            .controller('RegisController', RegisController);

    RegisController.$inject = ['$scope', 'storageService', 'TseService', '$translate', 'AuthDocs', '$state'];

    function RegisController($scope, storageService, TseService, $translate, AuthDocs, $state) {
        let vm = this;
        vm.step = 2;
        
        
        
        vm.AuthDocs = AuthDocs;
//        console.log("AuthDocs: ", vm.AuthDocs);
        
        vm.getOTP = function () {
            vm.step = 1;
        }
        
        vm.cancelStep1 = function () {
            vm.step = 0;
        }
        
        vm.confirmOTP = function () {
            vm.step = 2;
        }
        
        vm.cancelStep2 = function () {
            vm.step = 1;
        }
        
        vm.cancelStep3 = function () {
            vm.step = 2;
        }
        
        vm.confirmIMG = function () {
            vm.step = 3;
        }
        
        vm.dropdown = false;
        vm.dropdownRegis = function () {
            if(vm.dropdown == false) {
                vm.dropdown = true;
            } else {
                vm.dropdown = false;
            }
        }
        
        vm.chooseInputFont = function () {
            document.getElementById("inputFont").click();
        }
        
        vm.chooseInputBack = function () {
            document.getElementById("inputBack").click();
            
        }
        
        vm.popupTerms = popupTerms;
        
        function popupTerms() {
            $state.go('register.terms')
        }
    }
})();