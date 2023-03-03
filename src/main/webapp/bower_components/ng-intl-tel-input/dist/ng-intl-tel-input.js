angular.module('ngIntlTelInput', []);
angular.module('ngIntlTelInput')
    .provider('ngIntlTelInput', function () {
      var me = this;
      var props = {};
      var setFn = function (obj) {
        // console.log("obj: ", obj);
        if (typeof obj === 'object') {
          for (var key in obj) {
            props[key] = obj[key];
          }
        }
      };
      me.set = setFn;

      me.$get = ['$log', function ($log) {
        // console.log("obj: ", props);
        return Object.create(me, {
          init: {
            value: function (elm) {
              if (!window.intlTelInputUtils) {
                $log.warn('intlTelInputUtils is not defined. Formatting and validation will not work.');
              }
              // elm.intlTelInput(props);
              // console.log("props: ", props);
              intlTelInput(elm[0], props);
            }
          },
          props : {
            value: props
          }
        });
      }];
    });
angular.module('ngIntlTelInput')
    .directive('ngIntlTelInput', ['ngIntlTelInput', '$log', '$window', '$parse',
      function (ngIntlTelInput, $log, $window, $parse) {
        return {
          restrict: 'A',
          require: 'ngModel',
          link: function (scope, elm, attr, ctrl) {
            // Warning for bad directive usage.
            if ((!!attr.type && (attr.type !== 'text' && attr.type !== 'tel')) || elm[0].tagName !== 'INPUT') {
              $log.warn('ng-intl-tel-input can only be applied to a *text* or *tel* input');
              return;
            }
            // Override default country.
            if (attr.initialCountry) {
              ngIntlTelInput.set({initialCountry: attr.initialCountry});
            }
            // Initialize.
            // console.log("eml: ", ngIntlTelInput.props);
            // ngIntlTelInput.init(elm);

            var iti =  intlTelInput(elm[0], ngIntlTelInput.props);
            // iti.setCountry("vn");

            // Set Selected Country Data.
            function setSelectedCountryData(model) {
              var getter = $parse(model);
              var setter = getter.assign;
              // iti.getSelectedCountryData()
              setter(scope, iti.getSelectedCountryData());
              // setter(scope, elm.intlTelInput('getSelectedCountryData'));
            }
            // Handle Country Changes.
            function handleCountryChange() {
              setSelectedCountryData(attr.selectedCountry);
            }
            // Country Change cleanup.
            function cleanUp() {
              angular.element($window).off('countrychange', handleCountryChange);
            }
            // Selected Country Data.
            if (attr.selectedCountry) {
              setSelectedCountryData(attr.selectedCountry);
              angular.element($window).on('countrychange', handleCountryChange);
              scope.$on('$destroy', cleanUp);
            }
            // Validation.
            ctrl.$validators.ngIntlTelInput = function (value) {

              // if phone number is deleted / empty do not run phone number validation
              if (value || elm[0].value.length > 0) {
                return iti.isValidNumber();
                // return elm.intlTelInput('isValidNumber');
              } else {
                return true;
              }
            };
            // Set model value to valid, formatted version.
            ctrl.$parsers.push(function (value) {
              return iti.getNumber();
              // return elm.intlTelInput('getNumber');
            });
            // Set input value to model value and trigger evaluation.
            ctrl.$formatters.push(function (value) {
              if (value) {
                if(value.charAt(0) !== '+') {
                  value = '+' + value;
                }
                iti.setNumber(value);
                // elm.intlTelInput('setNumber', value);
              }
              return value;
            });
          }
        };
      }]);
