
function  cancelLogin() {
//    window.location.replace("https://192.168.5.112:8643/selfcare/#/");
    window.location.replace("https://rssp.mobile-id.vn/SelfCare/#/");
}

let count = null;

window.addEventListener('load', function () {
    count = $('#validityPeriod').val();
    countDown();
//    postLoginSSO();
});



function countDown() {

    var timer = document.getElementById("vTimer");
    if (count > 0) {
        count--;
        timer.innerHTML = count;

        if (count == 0) {
//            window.location.replace("https://192.168.5.112:8643/selfcare/#/");
            window.location.replace("https://rssp.mobile-id.vn/SelfCare/#/");
        }

        setTimeout("countDown()", 1000);
    }
}


