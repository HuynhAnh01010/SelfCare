(function () {
    'use strict';

    angular
            .module('loginappApp')
            .filter('convertStringDateTime', function () {
                ///cut with length https://stackoverflow.com/questions/18095727/limit-the-length-of-a-string-with-angularjs
                return function (value) {
                    if (!value)
                        return '';
                    var day = moment("12-25-1995", "MM-DD-YYYY");
                    // console.log('day: ', day);
                    let dateArr = value.replace(
                            /^(\d{4})(\d\d)(\d\d)(\d\d)(\d\d)(\d\d)$/,
                            '$1,$2,$3,$4,$5,$6')
                            .split(",");
                    return $1, $2, $3, $4, $5, $6;
                    // let dateArr =  value.replace(
                    //     /^(\d{4})(\d\d)(\d\d)(\d\d)(\d\d)(\d\d)$/,
                    //     function replacer(match, p1, p2, p3,p4,p5,p6, offset, string) {
                    //         // p1 is nondigits, p2 digits, and p3 non-alphanumerics
                    //         return [p1,p2,p3,p4,p5,p6];
                    //     }).split(",");
                    console.log(dateArr[0], dateArr[1], dateArr[2], dateArr[3], dateArr[4], dateArr[5]);
                    return new Date(Date.UTC(dateArr[0], dateArr[1], dateArr[2], dateArr[3], dateArr[4], dateArr[5])).getTime();
                };
            })
            .filter('convertString2DateTime', function () {
                ///cut with length https://stackoverflow.com/questions/18095727/limit-the-length-of-a-string-with-angularjs
                return function (value) {
                    if (!value)
                        return '';

                    let dateArr = value.replace(
                            /^(\d{4})(\d\d)(\d\d)(\d\d)(\d\d)(\d\d)$/,
                            '$1,$2,$3,$4,$5,$6')
                            .split(",");

                    var day = moment(`${dateArr[0]}-${dateArr[1]}-${dateArr[2]} ${dateArr[3]}-${dateArr[4]}-${dateArr[5]}`, 'YYYY-MM-DD hh:mm:ss').add(7, 'hours')
                    // console.log('day: ',day);
                    return day.valueOf();
                };
            }).filter('convertSubjectDN', function () {
        ///cut with length https://stackoverflow.com/questions/18095727/limit-the-length-of-a-string-with-angularjs
        return function (value, prefix) {
            // https://stackoverflow.com/questions/18095727/limit-the-length-of-a-string-with-angularjs
            if (!value)
                return '';
            value = value + ",TEMP=";
            // value = '0.9.2342.19200300.100.1.1=MST:0101335193,0.9.2342.19200300.100.1.1=CMND:12345678,CN=NGÔ VI MẠNH,O=TỔNG CÔNG TY CP BẢO HIỂM NGÂN HÀNG TMCP CÔNG THƯƠNG VIỆT NAM,T=Nhân viên,ST=HÀ NỘI,C=VN'
            let val = '';
            switch (prefix) {
                case 'CN':
                    val = value.match(/.*CN=(.*?),(TEMP|L|ST|C|E|O|OU|telephoneNumber|0.9.2342.19200300.100.1.1)=/);
                case 'ST':
                    val = value.match(/.*ST=(.*?),(TEMP|L|CN|C|E|O|OU|telephoneNumber|0.9.2342.19200300.100.1.1)=/);
                default:
                    val = value.match(/.*CN=(.*?),(TEMP|L|ST|C|E|O|OU|telephoneNumber|0.9.2342.19200300.100.1.1)=/);
            }
            // console.log("val: ",val);
            return val[1];
        };
    }).filter('convertIssuerDN', function () {
        ///cut with length https://stackoverflow.com/questions/18095727/limit-the-length-of-a-string-with-angularjs
        return function (value, prefix) {
            // https://stackoverflow.com/questions/18095727/limit-the-length-of-a-string-with-angularjs
            if (!value)
                return '';
            value = value + ",TEMP=";
            let val = '';
            switch (prefix) {
                case 'CN':
                    val = value.match(/.*CN=(.*?),(TEMP|L|ST|C|E|O|OU|telephoneNumber|0.9.2342.19200300.100.1.1)=/);
                case 'ST':
                    val = value.match(/.*ST=(.*?),(TEMP|L|ST|C|E|O|OU|telephoneNumber|0.9.2342.19200300.100.1.1)=/);
                default:
                    val = value.match(/.*CN=(.*?),(TEMP|L|ST|C|E|O|OU|telephoneNumber|0.9.2342.19200300.100.1.1)=/);
            }
            return val[1];
        };
    }).directive('readMore', function () {
        return {
            restrict: 'E',
            scope: {
                scroll: '=',
                idContent: '@',
            },
            template: '<a  href=""  class="scroll-down" ng-class="{\'scroll-down-disabled\': !scroll}" address="true"  ng-click="readMore()">\n' +
                    '                                <div class="scroll-down-bg"></div>\n' +
                    '                                <span class="scroll-arrow-first"></span>\n' +
                    '                                <span class="scroll-arrow-last"></span>\n' +
                    '                            </a>',
            link: function (scope, element, attrs) {

                scope.readMore = function () {
                    let p = $('.content-readmore').scrollTop();//current
                    let h = $('.content-readmore')[0].scrollHeight;//max-height
                    let hContent = $('.content-readmore').height();//height

                    if (scope.scroll) {
                        if ((p + hContent + 10) > h) {
                            scope.scroll = false;
                            return;
                        } else if ((p + hContent * 1.5 + 10) > h) {
                            scope.scroll = false;
                            $('#readmore').animate({scrollTop: '+=' + $('.content-readmore').height() / 2}, 'slow');
                        } else {
                            $('#readmore').animate({scrollTop: '+=' + $('.content-readmore').height() / 2}, 'slow');
                        }
                    }
                };
            }
        };
    }).directive('ngMouseWheel', ['deviceDetector', function (deviceDetector) {
            return {
                scope: {
                    scroll: '=',
                },
                link: function (scope, element, attrs) {
                    if (!deviceDetector.isDesktop()) {
                        $("#readmore").css({"overflow": "auto"});
                        // console.log("is Desktoop");
                    }
                    element.bind("DOMMouseScroll mousewheel onmousewheel", function (event) {

                        let p = $('.content-readmore').scrollTop();//current

                        let h = $('.content-readmore')[0].scrollHeight;//max-height
                        let hContent = $('.content-readmore').height();//height
                        if ((p + hContent + 10) > h) {
                            scope.scroll = false;
                            scope.$applyAsync();
                            $('.scroll-down').addClass('scroll-down-disabled');
                            $('.scroll-up').removeClass('scroll-up-disabled');
                        } else {
                            scope.scroll = true;
                            scope.$applyAsync();
                            $('.scroll-down').removeClass('scroll-down-disabled');
                            $('.scroll-up').addClass('scroll-up-disabled');
                        }



                        var event = window.event || event; // old IE support
//                        console.log("event:", event.deltaY);
//                        var delta;
//                        if (event.type == "wheel") {
////                        delta = Math.max(-1, Math.min(1, (event.wheelDelta || -event.detail)));
//                            delta = event.deltaY;
//                        } else if (event.type == "touchmove") {
//
//                        }

//                        console.log("Wheel Delta: ", delta);

                        if (event.deltaY < 0) {
//                            console.log("mouse wheel up");
                            $('#readmore').animate({scrollTop: '-=' + 90}, 1);
                        } else {
//                            console.log("mouse wheel down");
                            $('#readmore').animate({scrollTop: '+=' + 90}, 1);
                        }

                    });

                    // element.bind('touchstart, touchend,touchmove', function (e) {
                    //     start = e.changedTouches[0];
                    // });

                    element.bind('touchmove', function (e) {
                        let p = $('.content-readmore').scrollTop();//current
                        let h = $('.content-readmore')[0].scrollHeight;//max-height
                        let hContent = $('.content-readmore').height();//height

                        if ((p + hContent + 35) > h) {
                            scope.scroll = false;
                            scope.$applyAsync();
                            $('.scroll-down').addClass('scroll-down-disabled');
                        } else {
                            scope.scroll = true;
                            scope.$applyAsync();
                            $('.scroll-down').removeClass('scroll-down-disabled');
                        }
                    });

                }
            };
        }]).directive('tooltipInput', function () {
        ///cut with length https://stackoverflow.com/questions/18095727/limit-the-length-of-a-string-with-angularjs
        return {
            restrict: 'A',
            // transclude: 'true',
            link: function (scope, element, attrs) {
                let el = $(element);
                // el.tooltip({'trigger': 'focus', 'title': attrs.placeholder});

            }
        };
    }).directive('certificateAttribute', function () {
        return {
            restrict: 'E',
            scope: {
                elmNa: '',
                idContent: '@',
            },
            template: '<a  href=""  class="scroll-down" ng-class="{\'scroll-down-disabled\': !scroll}" address="true"  ng-click="readMore()">\n' +
                    '                                <div class="scroll-down-bg"></div>\n' +
                    '                                <span class="scroll-arrow-first"></span>\n' +
                    '                                <span class="scroll-arrow-last"></span>\n' +
                    '                            </a>',
            link: function (scope, element, attrs) {
                scope.readMore = function () {
                    let p = $('.content-readmore').scrollTop();//current
                    let h = $('.content-readmore')[0].scrollHeight;//max-height
                    let hContent = $('.content-readmore').height();//height

                    if (scope.scroll) {
                        if ((p + hContent + 10) > h) {
                            scope.scroll = false;
                            return;
                        } else if ((p + hContent * 1.5 + 10) > h) {
                            scope.scroll = false;
                            console.log("p + hContent/2");
                            $('#readmore').animate({scrollTop: '+=' + $('.content-readmore').height() / 2}, 'slow');
                        } else {
                            $('#readmore').animate({scrollTop: '+=' + $('.content-readmore').height() / 2}, 'slow');
                        }
                    }
                };
            }
        };
    }).directive("errorMessages", function () {
        return {
            restrict: 'AE',
            templateUrl: "app/layouts/temp/error-messages-templates.html",
            scope: {
                name: "@",
                field: "=",
                attribute: "@",
                minlength: "@",
                maxlength: "@",
                min: "@",
                max: "@",
                regex: "@",
                unique: "@",
            }
        }
    });
})();
