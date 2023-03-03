(function () {
    'use strict';

    angular
        .module('loginappApp')
        .directive('logoutDirective', logoutDirective)
        .directive('autoFocus', autoFocus)
        .directive('inputLetterNumber', inputLetterNumber)
        .directive('messageDialog', messageDialog);


    inputLetterNumber.$inject = [];
    autoFocus.$inject = ['$timeout'];
    logoutDirective.$inject = ['$rootScope', '$mdDialog', 'Auth', '$state'];
    messageDialog.$inject = ['$animate', '$compile','$timeout','TIMEOUT_HIDE_MODAL'];


    function messageDialog($animate, $compile,$timeout,TIMEOUT_HIDE_MODAL) {
        return {
            restrict: "E",
            transclude: true,
            scope: {
                obj:'='
            },
            templateUrl: 'app/layouts/temp/show-message-notification-temp.html',
            link: function (scope, element, attrs) {
                var hideMessage;

                var countdownHideMessage = function () {
                    hideMessage = $timeout(function () {
                        if(scope && scope.obj != undefined){
                            scope.obj.isShow = false;
                        }

                    }, TIMEOUT_HIDE_MODAL);
                };
                scope.$watch('obj',function(newVal, oldVal){
                    $timeout.cancel(hideMessage);
                    countdownHideMessage();

                });
            }
        }
    }

    function inputLetterNumber() {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ngModelCtrl) {
                function fromUser(text) {
                    var transformedInput = text.replace(/[^A-Za-z0-9]/g, '');
                    if (transformedInput !== text) {
                        ngModelCtrl.$setViewValue(transformedInput);
                        ngModelCtrl.$render();
                    }
                    return transformedInput;
                }

                ngModelCtrl.$parsers.push(fromUser);
            }
        };
    }

    function autoFocus($timeout) {
        return {
            link: function (scope, element, attrs) {
                attrs.$observe("autoFocus", function (newValue) {
                    if (newValue === "true")
                        $timeout(function () {
                            element.focus()
                        });
                });
            }
        };
    }

    // modal.$inject = [];

    function logoutDirective($rootScope, $mdDialog, Auth, $state) {
        return {
            // required:'LanguageController',
            link: function (scope, element, attr) {
                element.bind('click', function (event) {
                    var modalScope = $rootScope.$new();
                    $mdDialog.show({
                        controller: function () {
                            var vm = this;
                            vm.confirm = function () {
                                $mdDialog.hide();
                                Auth.logout();
                                $state.go('login');
                            };

                            vm.abort = function () {
                                $mdDialog.hide();
                                // $state.go('credential.list.detail');
                            }
                        },
                        controllerAs: 'vm',
                        templateUrl: 'app/components/logout/logout-md.html',
                        parent: angular.element(document.body),
                        // targetEvent: event,
                        clickOutsideToClose: true,
                        fullscreen: false, // Only for -xs, -sm breakpoints.
                        // preserveScope: true,
                    })
                        .then(function (answer) {
                            console.log("zo answer");
                        }, function () {
                            console.log("cancel: ");
                            // $scope.status = 'You cancelled the dialog.';
                        });
                });
            }
        };
    }

// .component("formError", {
//         templateUrl: "temp/form-error-template",
//         bindings: {
//             elm: "<",
//             attribute: "@",
//             minlength: "@",
//             maxlength: "@",
//             min: "@",
//             max: "@",
//             equal:"@",
//             regex: "@",
//             unique: "@",
//         }
//     })
})();
