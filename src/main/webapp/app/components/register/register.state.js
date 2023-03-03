(function () {
    'use strict';
    angular.module('loginappApp')
            .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('register', {
            parent: 'app',
            url: '/register',
            data: {
//                authorities: [],
                pageTitle: 'register.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/components/register/register.html',
                    controller: 'RegisController',
                    controllerAs: 'vm'
                },
                'leftContent@register': {
                    templateUrl: 'app/layouts/left-content/left-content.html',
                },
                'footerLanguage@register': {
                    templateUrl: 'app/layouts/footer/footerLanguageLogin.html',
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('register');
                        return $translate.refresh();
                    }]
            }
        });

        $stateProvider.state('register.terms', {
            parent: 'register',
            url: '/terms',
            data: {

            },
            onEnter: ['$stateParams', '$state', '$mdDialog', '$timeout', '$sce', '$cookies', '$window',
                function ($stateParams, $state, $mdDialog, $timeout, $sce, $cookies, $window) {
                    $mdDialog.show({
                        controller: function () {
                            let vm = this;

                            let lang = $cookies.getObject("NG_TRANSLATE_LANG_KEY");

                            if (lang == 'en') {
                                vm.srcTerms = $sce.trustAsResourceUrl("https://rssp.mobile-id.vn/en/terms");
                            } else if (lang == 'vi') {
                                vm.srcTerms = $sce.trustAsResourceUrl("https://rssp.mobile-id.vn/vi/terms");
                            }
                            vm.hide = function () {
                                hideModel();
                            }

                            function hideModel() {
                                $mdDialog.hide();
                                $state.go('register');
                            }
                        },
                        controllerAs: 'vm',
                        templateUrl: 'app/components/register/terms/terms.html',
                        parent: angular.element(document.body),
                        clickOutsideToClose: false,
                        fullscreen: false,
                    }).then(function (res) {

                    }, function () {

                    });
                }]
        });
    }


})();