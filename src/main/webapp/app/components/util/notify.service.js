(function() {
    'use strict';

    angular.module('loginappApp')
        .factory('notifySv', notifySv);

    function notifySv() {

        var service = {
            notify: notify,
            // clear: clear,
            // error: error,
            // info: info,
            // remove: remove,
            // success: success,
            warning: warning,
            // refreshTimer: refreshTimer
        };

        return service;

        /* Public API */
        function notify(message,from,align) {
            from = 'top';
            align = 'center';

            $.notify({
                icon: "now-ui-icons ui-1_bell-53",
                message: message
            }, {
                type: 'success',
                timer: 3000,
                placement: {
                    from: from,
                    align: align
                }
            });

        }
        function warning(message,from,align) {
            from = from || 'top';
            align = align || 'center';

            $.notify({
                icon: "now-ui-icons ui-1_bell-53",
                message: message
            }, {
                type: 'warning',
                timer: 10000,
                placement: {
                    from: from,
                    align: align
                }
            });

        }

    }
}());
