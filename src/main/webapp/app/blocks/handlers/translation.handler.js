(function() {
    'use strict';

    angular
        .module('loginappApp')
        .factory('translationHandler', translationHandler);

    translationHandler.$inject = ['$rootScope', '$window', '$state', '$translate','TITLE'];

    function translationHandler($rootScope, $window, $state, $translate,TITLE) {
        return {
            initialize: initialize,
            updateTitle: updateTitle,
            updateNavTitle: updateNavTitle,
        };

        function initialize() {
            // $rootScope.$broadcast('changeLanguageSuccess');
            // if the current translation changes, update the window title
            var translateChangeSuccess = $rootScope.$on('$translateChangeSuccess', function() {
                updateTitle();
            });

            $rootScope.$on('$destroy', function () {
                if(angular.isDefined(translateChangeSuccess) && translateChangeSuccess !== null){
                    translateChangeSuccess();
                }
            });
        }

        // update the window title using params in the following
        // precedence
        // 1. titleKey parameter
        // 2. $state.$current.data.pageTitle (current state page title)
        // 3. 'global.title'
        function updateTitle(titleKey) {
            if (!titleKey && $state.$current.data && $state.$current.data.pageTitle) {
                titleKey = $state.$current.data.pageTitle;
            }
            $translate(titleKey || 'global.title').then(function (title) {
                $window.document.title = title + ' | ' + TITLE;

            });
            // if(titleKey){
            //     let titleT = '';
            //     $translate(titleKey ).then(function (title) {
            //         titleT = title;
            //     });
            //     $translate('global.title').then(function (title) {
            //         titleT = titleT + ' | ' + title;
            //     });
            //
            //     $window.document.title = titleT;
            // }else{
            //     $translate('global.title').then(function (title) {
            //         $window.document.title = title;
            //     });
            // }

        }

        function updateNavTitle(titleKey) {
            console.log('title ', titleKey);
            // $window.document.getElementById("navTitle").innerHTML = "abc";
            // console.log($window.document.getElementsByClassName("nav-title").textContent);
            if (!titleKey && $state.$current.data && $state.$current.data.navTitle) {
                titleKey = $state.$current.data.navTitle;
            }
            $translate(titleKey || 'global.nav.title').then(function (title) {
                $window.document.getElementById("navTitle").innerHTML= title;
            });
        }
    }
})();
