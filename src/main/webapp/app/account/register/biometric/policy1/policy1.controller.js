(function () {
    'use strict';

    angular
        .module('loginappApp')
        .controller('Policy1Controller', Policy1Controller);


    Policy1Controller.$inject = ['$translate', '$timeout', 'Auth', '$state', 'deviceDetector', 'notifySv', 'policy'];

    function Policy1Controller($translate, $timeout, Auth, $state, deviceDetector, notifySv, policy) {
        var vm = this;


        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;

        vm.agree = agree;
        vm.back = back;
        vm.success = null;
        // vm.readMore = readMore;
        vm.isScroll = true;
        
        if(!deviceDetector.isDesktop()){
            // $("#readmore").css({"overflow":"auto"});
            // console.log("is Desktoop");
        }
        // notifySv.notify("You are <b>Create Account</b> successfully");

        //
        // function readMore(){
        //     let p = $('.content-readmore').scrollTop();//current
        //     let h = $('.content-readmore')[0].scrollHeight;//max-height
        //     let hContent = $('.content-readmore').height();//height
        //     console.log("h: ",h);
        //
        //     if(vm.isScroll){
        //         console.log("p + hContent: ", p + hContent);
        //         if((p + hContent + 10)> h){
        //             vm.isScroll = false;
        //             console.log("p + hContent");
        //             return;
        //         }else if((p + hContent *1.5+ 10)> h){
        //             console.log("p + hContent/2: ",p + hContent/2);
        //             vm.isScroll = false;
        //             console.log("p + hContent/2");
        //             $('#readmore').animate({scrollTop: '+=' + $('.content-readmore').height()/2}, 'slow');
        //         }else{
        //             $('#readmore').animate({scrollTop: '+=' + $('.content-readmore').height()/2}, 'slow');
        //         }
        //     }
        //
        // }

        function back() {
            $state.go("register");
        }

        function agree() {
            policy.acceptPolicy1 = true;
            // if(deviceDetector.isDesktop()){
            //     notifySv.warning("Biometric Identification only support mobile device");
            //    return;
            // }
            $state.go("policy2")
        }


    }
})();
