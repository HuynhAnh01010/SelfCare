(function () {
    'use strict';

    angular
            .module('loginappApp')
            .controller('DetailCredentialController', DetailCredentialController);

    DetailCredentialController.$inject = ['$scope', 'Principal', '$state', 'Auth', 'CredentialService', '$stateParams', '$mdDialog', 'storageService', 'TseService'];

    function DetailCredentialController($scope, Principal, $state, Auth, CredentialService, $stateParams, $mdDialog, storageService, TseService) {
        var vm = this;

        vm.popupChangeScal = popupChangeScal;
        vm.popupChangeAuthMode = popupChangeAuthMode;
        vm.popupChangeMultiSign = popupChangeMultiSign;
        vm.popupChangeEmail = popupChangeEmail;
        vm.popupChangePhone = popupChangePhone;
//        vm.popupPayment = popupPayment;
        vm.pupupUpload = pupupUpload;

        vm.popupEnroll = popupEnroll;

        vm.popupDownloadCSR = popupDownloadCSR;

        vm.popupDownloadCTS = popupDownloadCTS;
        
        vm.pupupGoOrder = pupupGoOrder;

        function popupChangeScal() {
            $state.go('credential.list.detail.changeScal');
        }

        function popupChangeAuthMode() {
            $state.go('credential.list.detail.authMode');
        }

        function popupChangeMultiSign() {
            $state.go('credential.list.detail.changeMultiSign');
        }

        function popupChangeEmail() {
            $state.go('credential.list.detail.changeEmail');
        }

        function popupChangePhone() {
            $state.go('credential.list.detail.changePhone');
        }

        function popupEnroll() {
            $state.go('credential.list.detail.enroll');
        }

        function pupupUpload() {
            $state.go("credential.list.detail.upload");
        }
        
        function pupupGoOrder() {
            $state.go("order.list");
        }


//        function popupPayment() {
//            $state.go('vnpay');
//        }

        // vm.changeAuthMode = changeAuthmode;

        if ($stateParams.id == null || $stateParams.id == "") {
            $state.go("credential.list");
        }

        vm.id = $stateParams.id;
        //
        // vm.title = "Angularjs Bootstrap Modal Directive Example";
        vm.showModal1 = false;
        vm.showModal2 = false;

        getCredetialInfo();

        function getCredetialInfo() {
            CredentialService.credentialInfo({
                id: $stateParams.id
            }).then(function (res) {

                vm.detail = res.data;
                storageService.setData(vm.detail);
                console.log("Detail DATA: ", vm.detail);
                vm.error = null;
                vm.success = 'OK';
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }

        function popupDownloadCSR() {
            var csrfilename = storageService.getData().cert.subjectDN;
            csrfilename = csrfilename + ",TEMP=";
            var val = '';
            val = csrfilename.match(/.*CN=(.*?),(TEMP|L|ST|C|E|O|OU|telephoneNumber|0.9.2342.19200300.100.1.1)=/);
//            console.log("CN file name: ", val);
            var fileCSR = val[1];

            var filename = 'CSR - ' + fileCSR + '.csr';
            var CSRname = storageService.getData().cert.csr;
//            console.log("CSRname: ", CSRname);

            console.log("Xuống dòng: ", CSRname.split('\n'));
            CSRname = CSRname.split('\n');
            
            CSRname = "-----BEGIN NEW CERTIFICATE REQUEST-----\n\n" + CSRname + "\n\n-----END NEW CERTIFICATE REQUEST-----";

            var blob = new Blob([CSRname], {type: 'text/plain'});
            
//            var lines = blob.length();
            

            if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveOrOpenBlob(blob, filename);
            } else {
                var e = document.createEvent('MouseEvents'),
                        a = document.createElement('a');
                a.download = filename;
                a.href = window.URL.createObjectURL(blob);
                a.dataset.downloadurl = ['text/json', a.download, a.href].join(':');
                e.initEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
                a.dispatchEvent(e);
                // window.URL.revokeObjectURL(a.href); // clean the url.createObjectURL resource
            }
        }

        function popupDownloadCTS() {
            var csrfilename = storageService.getData().cert.subjectDN;
            csrfilename = csrfilename + ",TEMP=" ;
            var val = '';
            val = csrfilename.match(/.*CN=(.*?),(TEMP|L|ST|C|E|O|OU|telephoneNumber|0.9.2342.19200300.100.1.1)=/);
//            console.log("CN file name: ", val);
            var fileCSR = val[1];

            var filename = 'CTS - ' + fileCSR + '.cer';

            var CTS = storageService.getData().cert.certificates[0];
            
            CTS = "-----BEGIN CERTIFICATE-----\n\n"  + CTS + "\n\n-----END CERTIFICATE-----";
            
            console.log("CTS: ", CTS);
            


            var blob = new Blob([CTS], {type: ''});
            if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveOrOpenBlob(blob, filename);
            } else {
                var e = document.createEvent('MouseEvents'),
                        a = document.createElement('a');
                a.download = filename;
                a.href = window.URL.createObjectURL(blob);
                a.dataset.downloadurl = ['', a.download, a.href].join(':');
                e.initEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
                a.dispatchEvent(e);

            }
        }
        
        

    }
})();
