(function() {
    'use strict';

    angular
        .module('loginappApp')
        .controller('UserConfirmController', UserConfirmController);


    UserConfirmController.$inject = ['$scope','TseService','$translate', '$timeout', 'Auth','$state','deviceDetector','notifySv','policy','registerCertificateObject'];

    function UserConfirmController ($scope,TseService,$translate, $timeout, Auth,$state,deviceDetector,notifySv,policy,registerCertificateObject) {
        var vm = this;

        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;

        vm.back = back;
        vm.success = false;
        vm.error = false;
        vm.errorMsg = "";
        vm.initializedSuccessfully = false;
        vm.tryIt = tryIt;
        vm.checkAfterEditUsername = checkAfterEditUsername;
        vm.registerCertificateObject = registerCertificateObject;

        console.log("vm.registerCertificateObject: ",vm.registerCertificateObject);

        if(!vm.registerCertificateObject.hasOwnProperty("latestIDScanResult")){
            $state.go("reachEmailPhone");
        }

        function makeid(length) {
            var result           = '';
            var characters       = '0123456789';
            var charactersLength = characters.length;
            for ( var i = 0; i < length; i++ ) {
                result += characters.charAt(Math.floor(Math.random() * charactersLength));
            }
            return result;
        }

        // vm.registerCertificateObject.phone = "84985160831";
        // vm.registerCertificateObject.email = "baotrv@gmail.com";
        // vm.registerCertificateObject.identification = "84985160831";
        // vm.registerCertificateObject.fullname = "Trần Văn Bảo";

        vm.registerCertificateObject.phone = vm.registerCertificateObject.phone? vm.registerCertificateObject.phone.replace("+",''): '';
        vm.registerCertificateObject.username = vm.registerCertificateObject.phone;

        // let param = angular.copy(vm.registerCertificateObject);
        // param.phone =  vm.registerCertificateObject.phone.replace("+",'0');
        //
        // param.username = vm.registerCertificateObject.phone.replace("+",'0'); //+'_' +makeid(5);
        // param.identification = vm.registerCertificateObject.identification;// +'_' +makeid(5);



        function back(){
            $state.go("reachEmailPhone");
        }

        function tryIt(){
            let param = angular.copy(vm.registerCertificateObject);
            vm.error = false;
            vm.success = false;
            vm.errorMsg = "";

            console.log("request : ",param);
            TseService.ownerCreate(param, function (resp) {
                console.log("ownerCreate resp", resp);
                if(resp.code == 0){
                    vm.registerCertificateObject.status = true;
                    vm.registerCertificateObject.msg = "YOU ARE CREATE ACCOUNT SUCCESSFULLY";
                    // notifySv.notify("You are <b>Create Account</b> successfully");
                    $state.go("login");
                    return;
                }else{
                    try{
                        vm.error = true;
                        vm.errorMsg = resp.data.message;
                    }catch (e) {
                        vm.error = true;
                        vm.errorMsg = "ERROR SERVER WHILE ENROLLING CERTIFICATE";
                    }


                }
            },function success(resp){
                console.log("resp success: ",resp);
            },function error(err){
                console.log("resp error",err);
            });
        }

        function checkAfterEditUsername() {

        }

    }
})();
