// (function() {
//     'use strict';
//
//     angular
//         .module('loginappApp')
//         .factory('Account', Account);
//
//     Account.$inject = ['$resource'];
//
//     function Account ($resource) {
//         var service = $resource('api/owner/info', {}, {
//             'get': { method: 'GET', params: {}, isArray: false,ignoreLoadingBar: true,
//                 interceptor: {
//                     response: function(response) {
//                         console.log(response);
//                         // expose response
//                         return response;
//                     }
//                 }
//             }
//         });
//
//         return service;
//     }
// })();
