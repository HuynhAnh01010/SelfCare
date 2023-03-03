(function () {
    'use strict';

    angular
            .module('loginappApp')
            .factory('TseService', TseService);

    TseService.$inject = ['TseFactory'];

    function TseService(TseFactory) {
        var service = {
            credentialChangeAuthInfo: credentialChangeAuthInfo,
            credentialResetPassphrase: credentialResetPassphrase,
            credentialChangePassphrase: credentialChangePassphrase,
            credentialChangeEmail: credentialChangeEmail,
            credentialChangePhone: credentialChangePhone,
            credentialSendOtp: credentialSendOtp,
            credentialList: credentialList,
            credentialInfo: credentialInfo,
            credentialHistory: credentialHistory,
            ownerChangePassword: ownerChangePassword,
            ownerCreate: ownerCreate,
            ownerInfo: ownerInfo,
            authTwoFactor: authTwoFactor,
            authAuthenticate: authAuthenticate,
            ownerChangeEmail: ownerChangeEmail,
            ownerChangeInfo: ownerChangeInfo,

//            systems
            systemGetCertificateAuthorities: systemGetCertificateAuthorities,
            systemGetCertificateProfiles: systemGetCertificateProfiles,
            systemGetSigningProfiles: systemGetSigningProfiles,
            systemsGetCountries: systemsGetCountries,
            systemsGetStatesOrProvinces: systemsGetStatesOrProvinces,
            credentialsUpgrade: credentialsUpgrade,
            ordersCheckout: ordersCheckout,
            getPaymentProvider: getPaymentProvider,
            credentialsRenew: credentialsRenew,
            ownerSendOTP: ownerSendOTP,
            credentialsIssue: credentialsIssue,
            credentialsEnroll: credentialsEnroll,
            credentialsApprove: credentialsApprove,
            credentialsImport: credentialsImport,
            
            authPreLogin: authPreLogin,
            authLoginSSO: authLoginSSO,
            
            rePostData: rePostData,
            
            getEnTerms: getEnTerms
        };
        return service;

        function authAuthenticate(credentials, callback) {
            var cb = callback || angular.noop;
            var deferred = $q.defer();

            TseFactory.authAuthenticate(credentials)
                    .then(function (data) {
                        console.log("loginThen", data);
                        // Principal.identity(true).then(function (account) {
                        //     console.log("loginThen account",account);
                        //     deferred.resolve(data);
                        // });
                        return cb();
                    })
                    .catch(function (err) {
                        this.logout();
                        deferred.reject(err);
                        return cb(err);
                    }.bind(this));

            return deferred;
            // var cb = callback || angular.noop;
            //
            // return TseFactory.authAuthenticate(credentials,
            //     function (res) {
            //         return cb(res)
            //     },
            //     function (err) {
            //         return cb(err);
            //     }
            //         .bind(this)).$promise;

        }

        function authTwoFactor(credentials, callback) {
            var cb = callback || angular.noop;

            return TseFactory.authTwoFactor({'username': credentials.username, 'userType': credentials.userType},
                    function (res) {
                        return cb(res)
                    },
                    function (err) {
                        return cb(err);
                    }
            .bind(this)).$promise;

        }

        function ownerInfo(key, callback) {
//            console.log("owner info");
            var cb = callback || angular.noop;

            return TseFactory.ownerInfo(key,
                    function (res) {
                        return cb(res)
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;

        }

        function ownerCreate(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.ownerCreate(key,
                    function (res) {
                        return cb(res)
                    },
                    function (err) {
                        return cb(err);
                    }
            .bind(this)).$promise;

        }

        function credentialChangeAuthInfo(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.credentialChangeAuthInfo(key,
                    function (res) {
                        return cb(res)
                    },
                    function (err) {
                        return cb(err);
                    }
            .bind(this)).$promise;
        }

        function credentialChangePassphrase(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.credentialChangePassphrase(key,
                    function (res) {
                        return cb(res)
                    },
                    function (err) {
                        return cb(err);
                    }
            .bind(this)).$promise;

        }

        function credentialChangeEmail(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.credentialChangeEmail(key,
                    function (res) {
                        return cb(res)
                    },
                    function (err) {
                        return cb(err);
                    }
            .bind(this)).$promise;

        }

        function credentialChangePhone(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.credentialChangePhone(key,
                    function (res) {
                        return cb(res)
                    },
                    function (err) {
                        return cb(err);
                    }
            .bind(this)).$promise;

        }


        function credentialResetPassphrase(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.credentialResetPassphrase(key,
                    function (res) {
                        return cb(res)
                    },
                    function (err) {
                        return cb(err);
                    }
            .bind(this)).$promise;

        }

        function credentialSendOtp(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.credentialSendOtp(key,
                    function (res) {
                        return cb(res)
                    },
                    function (err) {
                        return cb(err);
                    }
            .bind(this)).$promise;

        }

        function ownerChangePassword(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.ownerChangePassword(key,
                    function (res) {
                        return cb(res)
                    },
                    function (err) {
                        return cb(err);
                    }
            .bind(this)).$promise;

        }

        function credentialList(key, callback) {
            var cb = callback || angular.noop;

            return CredentialListService.get(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }

        function credentialInfo(key, callback) {
            var cb = callback || angular.noop;

            return CredentialInfoService.get(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }

        function credentialHistory(key, callback) {
            var cb = callback || angular.noop;

            return CredentialHistoryService.get(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }

        function ownerChangeEmail(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.ownerChangeEmail(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }

        function ownerChangeInfo(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.ownerChangeInfo(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }

        function systemGetCertificateAuthorities(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.systemGetCertificateAuthorities(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }


        function systemGetCertificateProfiles(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.systemGetCertificateProfiles(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }

        function systemGetSigningProfiles(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.systemGetSigningProfiles(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }

        function credentialsUpgrade(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.credentialsUpgrade(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }

        function ordersCheckout(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.ordersCheckout(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }

        function getPaymentProvider(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.getPaymentProvider(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }


        function credentialsRenew(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.credentialsRenew(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }


        function ownerSendOTP(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.ownerSendOTP(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }


        function credentialsIssue(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.credentialsIssue(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }


        function systemsGetCountries(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.systemsGetCountries(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }


        function systemsGetStatesOrProvinces(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.systemsGetStatesOrProvinces(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }


        function credentialsEnroll(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.credentialsEnroll(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }


        function credentialsApprove(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.credentialsApprove(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }

        function credentialsImport(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.credentialsImport(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }
        
        function authPreLogin(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.authPreLogin(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }
        
        function authLoginSSO(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.authLoginSSO(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }
        
        function rePostData(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.rePostData(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }
        
        
        function getEnTerms(key, callback) {
            var cb = callback || angular.noop;

            return TseFactory.getEnTerms(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
        }
    }
})();
