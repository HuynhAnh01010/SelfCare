
(function() {
    'use strict';

    angular
        .module('loginappApp')
        .config(ngIntlTelInputConfig);

    ngIntlTelInputConfig.$inject = ['ngIntlTelInputProvider'];

    function ngIntlTelInputConfig(ngIntlTelInputProvider) {
//        console.log("ngIntlTelInputProvider: ");
        // ngIntlTelInputProvider.set({initialCountry: 'vn',nationalMode: false,onlyCountries:['vn'],separateDialCode:false});
        ngIntlTelInputProvider.set({
            initialCountry: 'vn',
            nationalMode: true,
            onlyCountries:['vn'],
            // preferredCountries: ['vn'],
            separateDialCode:true,
            autoPlaceholder: 'aggressive',
            customPlaceholder: 'eg: 0985160831',
            utilsScript:"node_modules/intl-tel-input/build/js/utils.js"});
        // ngIntlTelInputProvider.set({preferredCountries: ['vn']});
    }
})();
