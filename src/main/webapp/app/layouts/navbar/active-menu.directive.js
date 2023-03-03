(function () {
    'use strict';

    angular
        .module('loginappApp')
        .directive('activeMenu', activeMenu)
        .directive('ngConfirmClick', ngConfirmClick);
        // .directive('myLanguage', myLanguage);

    activeMenu.$inject = ['$translate', '$locale', 'tmhDynamicLocale'];

    function activeMenu($translate, $locale, tmhDynamicLocale) {
        var directive = {
            restrict: 'A',
            link: linkFunc
        };

        return directive;

        function linkFunc(scope, element, attrs) {
            var language = attrs.activeMenu;

            scope.$watch(function () {
                return $translate.use();
            }, function (selectedLanguage) {
                if (language === selectedLanguage) {
                    tmhDynamicLocale.set(language);
                    element.addClass('selectedIndex');
                } else {
                    element.removeClass('selectedIndex');
                }
            });
        }
    }

    ngConfirmClick.$inject = ['$rootScope', '$mdDialog'];
    // myLanguage.$inject = ['$rootScope', '$mdDialog', '$injector'];

    function ngConfirmClick($rootScope, $mdDialog) {
        // http://plnkr.co/edit/hfxyWHidbB19PU5p3k9Z?p=preview&preview
        // http://plnkr.co/edit/xJJFxjYeeHmDixAYPu4c?p=preview&preview
        return {
            link: function (scope, element, attr) {
                var msg = attr.ngConfirmClick || "Do you want log out?";
                var clickAction = attr.confirmedClick;
                element.bind('click', function (event) {

                    var modalScope = $rootScope.$new();
                    var confirm = $mdDialog.confirm(
                        {
                            clickOutsideToClose: true,
                            scope: scope,
                            preserveScope: true,
                            template: '<md-dialog ng-cloak>\n' +
                                '  <form>\n' +
                                '    <md-dialog-content>\n' +
                                '      <div class="md-dialog-content">\n' +
                                '        <p class="title">' + msg + '</p>\n' +
                                '    </md-dialog-content>\n' +
                                '\n' +
                                '    <md-dialog-actions layout="row" align="end">\n' +
                                '      <span flex></span>\n' +
                                '      <md-button ng-click="dialog.abort()" class="md-raised">Cancel</md-button>\n' +
                                '      <md-button ng-click="dialog.hide()" class="md-raised md-warn md-button md-ink-ripple">Ok</md-button>\n' +
                                '    </md-dialog-actions>\n' +
                                '  </form>\n' +
                                '</md-dialog>',
                            // controller: DialogController
                        }
                    );
                    $mdDialog.show(confirm).then(function () {
                        scope.$eval(clickAction)
                    }, function () {
                    });

                });

                function DialogController($scope, $mdDialog) {
                    $scope.closeDialog = function () {
                        $mdDialog.hide();
                    }
                }
            }
        };
    }


})();
