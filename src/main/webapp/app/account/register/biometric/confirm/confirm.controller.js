(function () {
    'use strict';

    angular
        .module('loginappApp')
        .controller('UserConfirmController', UserConfirmController);


    UserConfirmController.$inject = ['$scope', 'TseService', '$translate', '$timeout', 'Auth', '$state',
        'deviceDetector', 'notifySv', 'policy', 'registerCertificateObject', 'EIdentityService'];

    function UserConfirmController($scope, TseService, $translate, $timeout, Auth, $state,
                                   deviceDetector, notifySv, policy, registerCertificateObject, EIdentityService) {
        var vm = this;

        vm.back = back;
        vm.tryIt = tryIt;
        vm.registerCertificateObject = registerCertificateObject;
        vm.registerCertificateObject.status = false;

        if(!vm.registerCertificateObject.hasOwnProperty("latestIDScanResult")){
            $state.go("reachEmailPhone");
        }

        vm.registerCertificateObject.username = vm.registerCertificateObject.phone ? vm.registerCertificateObject.phone.replace("+", '') : '';


        let ocrFirst = angular.copy(vm.registerCertificateObject);
        delete ocrFirst['username'];


        function back() {
            $state.go("reachEmailPhone");
        }

        function tryIt() {
            let param = angular.copy(vm.registerCertificateObject);

            var changeColumn = [];
            if (ocrFirst.location !== param.location) changeColumn.push('location');
            if (ocrFirst.dob !== param.dob) changeColumn.push('dob');
            if (ocrFirst.gender !== param.gender) changeColumn.push('gender');
            if (ocrFirst.fullname !== param.fullname) changeColumn.push('fullname');
            if (ocrFirst.identification !== param.identification) changeColumn.push('identification');
            param.changeColumn = changeColumn;
            // return;

            // console.log("request : ", param);
            // param.subject_id =
            EIdentityService.processesSelfReviseAndTseRegister(param, function (resp) {
                console.log("resp: ", resp);
                if (resp.code === 0 && resp.data.ownerUUID != null) {
                    vm.registerCertificateObject.status = true;
                    vm.registerCertificateObject.msg = resp.data.errorDescription;
                    // notifySv.notify("You are <b>Create Account</b> successfully");
                    $state.go("login");
                }

                vm.dialogMessage = {
                    isShow: true,
                    isError: !(resp.code === 0 && resp.data.ownerUUID != null),
                    message: resp.data.errorDescription || resp.data.message
                };
            }, function (error) {
                console.log("resp error: ", error);
                vm.dialogMessage = {
                    isShow: true,
                    isError: true,
                    message: "ERROR"
                };
            });

        }

    }
})();
