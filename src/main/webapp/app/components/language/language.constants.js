(function () {
    'use strict';

    angular
        .module('loginappApp')
        .constant('LANGUAGES', [
            // { name: 'vi', desc: 'Vietnamese', img: 'vietname.png', enabled: false },
            // { name: 'en', desc: 'English', img: 'english.png', enabled: true },
            'en',
            'vi'
        ]
    );
})();
