(function () {
    'use strict';

    angular
        .module('loginappApp')
        .service('PhotoIDMatchProcessor', PhotoIDMatchProcessor);

    PhotoIDMatchProcessor.$inject = ['$injector', 'EIdentityService', 'registerCertificateObject', '$timeout', 'storageService','$translate'];

    function PhotoIDMatchProcessor($injector, EIdentityService, registerCertificateObject, $timeout, storageService,$translate) {
        // var sessionToken = null;
        function PhotoIDMatchProcessor(sessionToken, sampleAppControllerReference) {
            this.latestNetworkRequest = new XMLHttpRequest();
            this.success = false;
            this.error = false;
            this.message = null;
            this.sessionResult = null;
            this.sampleAppControllerReference = sampleAppControllerReference;
            this.latestSessionResult = null;
            this.latestIDScanResult = null;
            new FaceTecSDK.FaceTecSession(this, sessionToken);
        }

        //
        // Part 2:  Handling the Result of a FaceScan - First part of the Photo ID Scan
        //
        PhotoIDMatchProcessor.prototype.processSessionResultWhileFaceTecSDKWaits = function (sessionResult, faceScanResultCallback) {
            var _this_1 = this;
            var _this = this;
            // $translate('global.title').then(function (title) {
            //     console.log("title: ", title);
            // });
            _this.latestSessionResult = sessionResult;
            if (sessionResult.status !== FaceTecSDK.FaceTecSessionStatus.SessionCompletedSuccessfully) {
                console.log("status session: ",sessionResult.status);
                console.log("Session was not completed successfully, cancelling.  Session Status: " + FaceTecSDK.FaceTecSessionStatus[sessionResult.status]);
                this.latestNetworkRequest.abort();
                $translate('register.biometricIdentification.messagesKyc.sessionNotComplete').then(function (msg) {
                    _this.message = msg + FaceTecSDK.FaceTecSessionStatus[sessionResult.status];
                });
                // _this.message = "Session was not completed successfully, cancelling.  Session Status: " + FaceTecSDK.FaceTecSessionStatus[sessionResult.status];
                _this.error = true;
                faceScanResultCallback.cancel();
                return;
            }

            var createProcess = {
                "subject_id": registerCertificateObject.subjectId,
                "provider":"MOBILE-ID", //MOBILE-ID //FACETEC
                "process_type":"LIVENESS",
                userAgent: FaceTecSDK.createFaceTecAPIUserAgentString(""),
            };

            EIdentityService.processesCreate(createProcess, function (resp) {
                try {
                    if (resp.code == 0) {
                        if (resp.data.status == 0) {
                            var parameters = {
                                sessionId: sessionResult.sessionId,
                                faceScan: sessionResult.videoScan,
                                // faceScan: sessionResult.faceScan,
                                lowQualityAuditTrailImage: sessionResult.lowQualityAuditTrail[0],
                                auditTrailImage: sessionResult.auditTrail[0],
                                subject_id: registerCertificateObject.subjectId,
                                process_id: resp.data.process_id,
                                userAgent: FaceTecSDK.createFaceTecAPIUserAgentString(sessionResult.sessionId),
                                type: 'LIVENESS'
                            };
                            let pollingData = {
                                subject_id: registerCertificateObject.subjectId,
                                process_id: resp.data.process_id
                            };
                            EIdentityService.verification(parameters, function (respVerify) {
                                // console.log("verification liveness: ", respVerify)
                                try {
                                    if (respVerify.code == 0) {
                                        if (respVerify.data.status == 0) {

                                            $timeout(function () {
                                                callPolling(pollingData);
                                            }, 3000);
                                        } else {
                                            $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                                                _this.message = msg ;
                                            });
                                            // _this.message = "Can't connect to Server LIVENESS, please try again.";
                                            faceScanResultCallback.cancel()
                                        }
                                    } else {
                                        $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                                            _this.message = msg ;
                                        });
                                        faceScanResultCallback.cancel()
                                    }
                                } catch (e) {
                                    $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                                        _this.message = msg ;
                                    });
                                    faceScanResultCallback.retry()
                                }

                            });
                        } else {
                            $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                                _this.message = msg ;
                            });
                            faceScanResultCallback.cancel()
                        }
                    } else {
                        $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                            _this.message = msg ;
                        });
                        faceScanResultCallback.cancel()
                    }
                } catch (e) {
                    $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                        _this.message = msg ;
                    });
                    faceScanResultCallback.cancel()
                }
            }, function (error) {
                $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                    _this.message = msg ;
                });
                faceScanResultCallback.cancel()
            });

            let callPolling = function (parameters) {
                EIdentityService.processesGet(parameters, function (resp) {
                    // console.log("POLLING get liveness: ", resp)
                    if (resp.code == 0) {
                        if (resp.data.status == 0) {
                            switch (resp.data.process_status) {
                                case "PROCESSING":
                                    $timeout(function () {
                                        callPolling(parameters);
                                    }, 3000);
                                    break;
                                case "FINISHED":
                                    if (resp.data.verification_result.final_result.liveness) {
                                        $translate('register.biometricIdentification.messagesKyc.livenessConfirm').then(function (msg) {
                                            FaceTecSDK.FaceTecCustomization.setOverrideResultScreenSuccessMessage(msg);
                                        });
                                        // FaceTecSDK.FaceTecCustomization.setOverrideResultScreenSuccessMessage("Liveness\r\nConfirmed");
                                        faceScanResultCallback.succeed();
                                    } else {
                                        faceScanResultCallback.retry();
                                    }
                                    break;
                                default:
                                    faceScanResultCallback.retry();
                            }
                        } else {
                            $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                                _this.message = msg ;
                            });
                            faceScanResultCallback.cancel()
                        }
                    } else {
                        $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                            _this.message = msg ;
                        });
                        faceScanResultCallback.cancel();
                    }

                });
            };

            window.setTimeout(function () {
                $translate('register.biometricIdentification.messagesKyc.stillUpload').then(function (msg) {
                    faceScanResultCallback.uploadMessageOverride(msg);
                });
                // faceScanResultCallback.uploadMessageOverride("Still Uploading...");
            }, 7000);
        };
        //
        // Part 10:  Handling the Result of a IDScan
        //
        PhotoIDMatchProcessor.prototype.processIDScanResultWhileFaceTecSDKWaits = function (idScanResult, idScanResultCallback) {
            var _this_1 = this;
            var _this = this;
            _this.latestIDScanResult = idScanResult;
            //
            // Part 11:  Handles early exit scenarios where there is no IDScan to handle -- i.e. User Cancellation, Timeouts, etc.
            //
            if (idScanResult.status !== FaceTecSDK.FaceTecIDScanStatus.Success) {
                console.log("ID Scan was not completed successfully, cancelling.");
                this.latestNetworkRequest.abort();
                this.latestNetworkRequest = new XMLHttpRequest();
                // this.message = "ID Scan was not completed successfully";
                $translate('register.biometricIdentification.messagesKyc.idScanNotComplete').then(function (msg) {
                    _this.message = msg ;
                });
                idScanResultCallback.cancel();
                return;
            }

            let processCreateParam = {
                subject_id: registerCertificateObject.subjectId,
                "provider": "MOBILE-ID",//MOBILE-ID //FACETEC
                "process_type": "DOCUMENTVIDEO",
                userAgent: FaceTecSDK.createFaceTecAPIUserAgentString(""),
            };
            EIdentityService.processesCreate(processCreateParam, function (resp) {
                try {
                    if (resp.code == 0) {
                        if (resp.data.status == 0) {
                            registerCertificateObject.processId = resp.data.process_id;
                            var parameters = {
                                sessionId: idScanResult.sessionId,
                                idScan: idScanResult.idScan,
                                idScanFrontImage: idScanResult.frontImages[0],
                                // idScanFrontImage: storageService.getDataPP(),
                                subject_id: registerCertificateObject.subjectId,
                                process_id: resp.data.process_id,
                                userAgent: FaceTecSDK.createFaceTecAPIUserAgentString(),
                                type: 'DOCUMENTVIDEO',
                            };
                            if (idScanResult.backImages[0]) {
                                // parameters.idScanFrontImage = idScanResult.frontImages[0],
                                parameters.idScanBackImage = idScanResult.backImages[0];
                            }

                            registerCertificateObject.process_id = resp.data.process_id;

                            let pollingData = {
                                subject_id: registerCertificateObject.subjectId,
                                process_id: resp.data.process_id
                            };
                            EIdentityService.verification(parameters, function (respVerify) {
                                console.log("verification 2d-3d: ", respVerify);
                                try {
                                    if (respVerify.code == 0) {
                                        if (respVerify.data.status == 0) {
                                            $timeout(function () {
                                                callPolling(pollingData);
                                            }, 3000);
                                        } else {
                                            $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                                                _this.message = msg ;
                                            });
                                            // _this.message = "Can't connect to Server DOCUMENTVIDEO, please try again.";
                                            idScanResultCallback.cancel()
                                        }
                                    } else {
                                        $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                                            _this.message = msg ;
                                        });
                                        idScanResultCallback.cancel()
                                    }
                                } catch (e) {
                                    $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                                        _this.message = msg ;
                                    });
                                    idScanResultCallback.cancel()
                                }

                            });
                        } else {
                            $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                                _this.message = msg ;
                            });
                            idScanResultCallback.cancel();
                        }
                    } else {
                        $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                            _this.message = msg ;
                        });
                        idScanResultCallback.cancel();
                    }
                } catch (e) {
                    $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                        _this.message = msg ;
                    });
                    idScanResultCallback.cancel();
                }

            }, function (error) {
                $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                    _this.message = msg ;
                });
                idScanResultCallback.cancel()
            });


            let callPolling = function (parameters) {
                EIdentityService.processesGet(parameters, function (resp) {
                    if (resp.code == 0) {
                        if (resp.data.status == 0) {
                            switch (resp.data.process_status) {
                                case "PROCESSING":
                                    $timeout(function () {
                                        // idScanResultCallback.uploadMessageOverride("Still Uploading...");
                                        callPolling(parameters);
                                    }, 3000);
                                    break;
                                case "FINISHED":
                                    if (resp.data.verification_result.final_result.match_result) {
                                        registerCertificateObject.fullname = resp.data.verification_result.final_result.name;
                                        registerCertificateObject.identificationType = resp.data.verification_result.final_result.document_type;
                                        registerCertificateObject.identification = resp.data.verification_result.final_result.document_number;
                                        registerCertificateObject.location = resp.data.verification_result.final_result.address;
                                        registerCertificateObject.dob = resp.data.verification_result.final_result.dob;
                                        registerCertificateObject.gender = resp.data.verification_result.final_result.gender;
                                        registerCertificateObject.country = resp.data.verification_result.final_result.nationality;
                                        registerCertificateObject.stateOrProvince = resp.data.verification_result.final_result.city_province;
                                        registerCertificateObject.loa = "EXTEND";
                                        registerCertificateObject.kycEvidence = resp.data.verification_result.final_result.claim_sources.JWT;
                                        // registerCertificateObject.respFinal  = resp.data;

                                        _this.success = true;
                                        $translate('register.biometricIdentification.messagesKyc.your3dMatched').then(function (msg) {
                                            FaceTecSDK.FaceTecCustomization.setOverrideResultScreenSuccessMessage(msg);
                                        });

                                        idScanResultCallback.succeed();
                                    } else {
                                        $translate('register.biometricIdentification.messagesKyc.cantMatchPhotoWithLiveness').then(function (msg) {
                                            idScanResultCallback.retry(FaceTecSDK.FaceTecIDScanRetryMode.Front, msg);
                                        });
                                        // let overrideMessage = "Can't matching photo \n with your liveness";
                                        // // let overrideMessage = "Photo ID\nNot Fully Visible";
                                        // idScanResultCallback.retry(FaceTecSDK.FaceTecIDScanRetryMode.Front, overrideMessage);
                                    }
                                    break;
                                default:
                                    $translate('register.biometricIdentification.messagesKyc.imagePoorQuality').then(function (msg) {
                                        idScanResultCallback.retry(FaceTecSDK.FaceTecIDScanRetryMode.Front, msg);
                                    });
                                    // let overrideMessage = "The image is of poor quality (blur,dark/bright)";
                                    // idScanResultCallback.retry(FaceTecSDK.FaceTecIDScanRetryMode.Front, overrideMessage);
                            }
                        } else {
                            $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                                _this.message = msg ;
                            });
                            idScanResultCallback.cancel()
                        }
                    } else {
                        $translate('register.biometricIdentification.messagesKyc.cantConnect').then(function (msg) {
                            _this.message = msg ;
                        });
                        idScanResultCallback.cancel();
                    }

                });
            };
            window.setTimeout(function () {
                $translate('register.biometricIdentification.messagesKyc.stillUpload').then(function (msg) {
                    idScanResultCallback.uploadMessageOverride(msg);
                });
                idScanResultCallback.uploadMessageOverride("Still Uploading...");
            }, 7000);

        };
        //
        // Part 18:  This function gets called after the FaceTec SDK is completely done.  There are no parameters because you have already been passed all data in the processSessionWhileFaceTecSDKWaits function and have already handled all of your own results.
        //
        PhotoIDMatchProcessor.prototype.onFaceTecSDKCompletelyDone = function () {

            var _this = this;
            this.sampleAppControllerReference.onComplete(_this.latestSessionResult, _this.latestIDScanResult,
                _this.success, _this.message, _this.error);
        };
        //
        // DEVELOPER NOTE:  This public convenience method is for demonstration purposes only so the Sample App can get information about what is happening in the processor.
        // In your code, you may not even want or need to do this.
        //
        PhotoIDMatchProcessor.prototype.isSuccess = function () {
            return this.success;
        };
        return PhotoIDMatchProcessor;
    }
})();
