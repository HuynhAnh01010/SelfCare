//
// Welcome to the annotated FaceTec Device SDK core code for performing secure Photo ID Scan.
//
//
// This is an example self-contained class to perform Photo ID Scans with the FaceTec SDK.
// You may choose to further componentize parts of this in your own Apps based on your specific requirements.
//
var PhotoIDMatchProcessor = /** @class */ (function () {
    function PhotoIDMatchProcessor(sessionToken, sampleAppControllerReference,EIdentityService,registerCertificateObject) {
        this.latestNetworkRequest = new XMLHttpRequest();
        //
        // DEVELOPER NOTE:  These properties are for demonstration purposes only so the Sample App can get information about what is happening in the processor.
        // In the code in your own App, you can pass around signals, flags, intermediates, and results however you would like.
        //
        this.success = false;
        this.sessionResult = null;
        this.sampleAppControllerReference = sampleAppControllerReference;
        this.registerCertificateObject = registerCertificateObject;
        this.latestSessionResult = null;
        this.latestIDScanResult = null;
        this.eIdentityService = EIdentityService;
        //
        // Part 1:  Starting the FaceTec Session
        //
        // Required parameters:
        // - FaceTecIDScanProcessor:  A class that implements FaceTecIDScanProcessor, which handles the IDScan when the User completes an ID Scan.  In this example, "this" implements the class.
        // - sessionToken:  A valid Session Token you just created by calling your API to get a Session Token from the Server SDK.
        //
        new FaceTecSDK.FaceTecSession(this, sessionToken);
    }
    //
    // Part 2:  Handling the Result of a FaceScan - First part of the Photo ID Scan
    //
    PhotoIDMatchProcessor.prototype.processSessionResultWhileFaceTecSDKWaits = function (sessionResult, faceScanResultCallback) {
        var _this_1 = this;
        var _this = this;
        _this.latestSessionResult = sessionResult;
        //
        // Part 3:  Handles early exit scenarios where there is no FaceScan to handle -- i.e. User Cancellation, Timeouts, etc.
        //
        if (sessionResult.status !== FaceTecSDK.FaceTecSessionStatus.SessionCompletedSuccessfully) {
            console.log("Session was not completed successfully, cancelling.  Session Status: " + FaceTecSDK.FaceTecSessionStatus[sessionResult.status]);
            this.latestNetworkRequest.abort();
            faceScanResultCallback.cancel();
            return;
        }
        // IMPORTANT:  FaceTecSDK.FaceTecSessionStatus.SessionCompletedSuccessfully DOES NOT mean the Enrollment was Successful.
        // It simply means the User completed the Session and a 3D FaceScan was created.  You still need to perform the Enrollment on your Servers.
        //
        // Part 4:  Get essential data off the FaceTecSessionResult
        //
        console.log("Send to server: ",sessionResult);
        // var parameters = {
        //     sessionId: sessionResult.sessionId,
        //     faceScan: sessionResult.faceScan,
        //     // externalDatabaseRefID: this.sampleAppControllerReference.getLatestEnrollmentIdentifier(),
        //     lowQualityAuditTrailImage: sessionResult.lowQualityAuditTrail[0],
        //     auditTrailImage: sessionResult.auditTrail[0]
        // };
        var parameters = {
            sessionId: sessionResult.sessionId,
            faceScan: sessionResult.videoScan,
            // faceScan: sessionResult.faceScan,
            lowQualityAuditTrailImage: sessionResult.lowQualityAuditTrail[0],
            auditTrailImage: sessionResult.auditTrail[0],
            subject_id: this.registerCertificateObject.subjectId,
            process_id: this.registerCertificateObject.processId,
            userAgent: FaceTecSDK.createFaceTecAPIUserAgentString(sessionResult.sessionId),
            type: 'LIVENESS'
        };

        // console.log("subjectId: ", this.registerCertificateObject.subjectId );
        var polling = {
            subject_id: this.registerCertificateObject.subjectId,
            process_id: this.registerCertificateObject.processId
        };
        this.eIdentityService.verification(parameters, function (resp) {
            // console.log("verification liveness: ", resp)
            try {
                if (resp.code == 0) {
                    if (resp.data.status == 0) {

                        $timeout(function () {
                            callPolling(polling);
                        }, 3000);
                    } else {

                        faceScanResultCallback.cancel()
                    }
                } else {
                    faceScanResultCallback.cancel()
                }
            } catch (e) {
                console.log("verification error: ", e);
                faceScanResultCallback.retry()
            }

        });


        let callPolling = function (parameters) {
            this.eIdentityService.processesGet(parameters, function (resp) {
                // console.log("POLLING get liveness: ", resp)
                if (resp.code == 0 && resp.data.status == 0) {
                    if (resp.data.process_status == "PROCESSING" ) {
                        $timeout(function () {
                            faceScanResultCallback.uploadMessageOverride("Still Uploading...");
                            callPolling(parameters);
                        }, 3000);
                    } else if (resp.data.process_status == "FINISHED") {
                        if (resp.data.verification_result.final_result.liveness) {
                            console.log("liveness == true");
                            FaceTecSDK.FaceTecCustomization.setOverrideResultScreenSuccessMessage("Liveness\r\nConfirmed");
                            faceScanResultCallback.succeed();
                        } else {
                            faceScanResultCallback.retry();
                        }

                    } else {
                        faceScanResultCallback.cancel();
                    }
                } else {
                    faceScanResultCallback.cancel();
                }

            });
        };

        window.setTimeout(function () {
            if (_this_1.latestNetworkRequest.readyState === XMLHttpRequest.DONE) {
                return;
            }
            faceScanResultCallback.uploadMessageOverride("Still Uploading...");
        }, 6000);
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
            idScanResultCallback.cancel();
            return;
        }
        // IMPORTANT:  FaceTecSDK.FaceTecIDScanStatus.Success DOES NOT mean the IDScan was Successful.
        // It simply means the User completed the Session and a 3D IDScan was created.  You still need to perform the ID-Check on your Servers.
        //
        // minMatchLevel allows Developers to specify a Match Level that they would like to target in order for success to be true in the response.
        // minMatchLevel cannot be set to 0.
        // minMatchLevel setting does not affect underlying Algorithm behavior.
        var MinMatchLevel = 3;
        //
        // Part 12:  Get essential data off the FaceTecIDScanResult
        //
        var parameters = {
            sessionId: idScanResult.sessionId,
            externalDatabaseRefID: this.sampleAppControllerReference.getLatestEnrollmentIdentifier(),
            idScan: idScanResult.idScan,
            idScanFrontImage: idScanResult.frontImages[0],
            minMatchLevel: MinMatchLevel
        };
        if (idScanResult.backImages[0]) {
            parameters.idScanBackImage = idScanResult.backImages[0];
        }
        //
        // Part 13:  Make the Networking Call to Your Servers.  Below is just example code, you are free to customize based on how your own API works.
        //
        this.latestNetworkRequest = new XMLHttpRequest();
        this.latestNetworkRequest.open("POST", Config.BaseURL + "/match-3d-2d-idscan");
        this.latestNetworkRequest.setRequestHeader("Content-Type", "application/json");
        this.latestNetworkRequest.setRequestHeader("X-Device-Key", Config.DeviceKeyIdentifier);
        this.latestNetworkRequest.setRequestHeader("X-User-Agent", FaceTecSDK.createFaceTecAPIUserAgentString(idScanResult.sessionId));
        this.latestNetworkRequest.onreadystatechange = function () {
            //
            // Part 14:  In our Sample, we evaluate a boolean response and treat true as success, false as "User Needs to Retry",
            // and handle all other non-nominal responses by cancelling out.  You may have different paradigms in your own API and are free to customize based on these.
            //
            if (_this_1.latestNetworkRequest.readyState === XMLHttpRequest.DONE) {
                try {
                    var responseJSON = JSON.parse(_this_1.latestNetworkRequest.responseText);
                    if (responseJSON.success === true) {
                        // CASE:  Success!  The Photo ID Scan was performed and the User Matched the Photo ID.
                        //
                        // DEVELOPER NOTE:  These properties are for demonstration purposes only so the Sample App can get information about what is happening in the processor.
                        // In the code in your own App, you can pass around signals, flags, intermediates, and results however you would like.
                        //
                        _this_1.success = true;
                        // Demonstrates dynamically setting the Success Screen Message.
                        FaceTecSDK.FaceTecCustomization.setOverrideResultScreenSuccessMessage("Your 3D Face\r\nMatched Your ID");
                        idScanResultCallback.succeed();
                    }
                    else if (responseJSON.success === false) {
                        var overrideMessage;
                        // Handle invalid ID by displaying custom message
                        // If we could not determine the ID was Fully Visible and that the ID was a Physical, alter the feedback message to the User.
                        if (responseJSON.fullIDStatusEnumInt === 1 || responseJSON.digitalIDSpoofStatusEnumInt === 1) {
                            overrideMessage = "Photo ID\nNot Fully Visible";
                        }
                        // CASE:  In our Sample code, "success" being present and false means that the User Needs to Retry.
                        // Real Users will likely succeed on subsequent attempts after following on-screen guidance.
                        // Attackers/Fraudsters will continue to get rejected.
                        console.log("User needs to retry, invoking retry.");
                        idScanResultCallback.retry(FaceTecSDK.FaceTecIDScanRetryMode.Front, overrideMessage);
                    }
                    else {
                        // CASE:  UNEXPECTED response from API.  Our Sample Code keys of a success boolean on the root of the JSON object --> You define your own API contracts with yourself and may choose to do something different here based on the error.
                        console.log("Unexpected API response, cancelling out.");
                        idScanResultCallback.cancel();
                    }
                }
                catch (_a) {
                    // CASE:  Parsing the response into JSON failed --> You define your own API contracts with yourself and may choose to do something different here based on the error.  Solid server-side code should ensure you don't get to this case.
                    console.log("Exception while handling API response, cancelling out.");
                    idScanResultCallback.cancel();
                }
            }
        };
        this.latestNetworkRequest.onerror = function () {
            // CASE:  Network Request itself is erroring --> You define your own API contracts with yourself and may choose to do something different here based on the error.
            console.log("XHR error, cancelling.");
            idScanResultCallback.cancel();
        };
        //
        // Part 15:  Demonstrates updating the Progress Bar based on the progress event.
        //
        this.latestNetworkRequest.upload.onprogress = function name(event) {
            var progress = event.loaded / event.total;
            idScanResultCallback.uploadProgress(progress);
        };
        //
        // Part 16:  Actually send the request.
        //
        var jsonStringToUpload = JSON.stringify(parameters);
        this.latestNetworkRequest.send(jsonStringToUpload);
        //
        // Part 17:  For better UX, update the User if the upload is taking a while.  You are free to customize and enhance this behavior to your liking.
        //
        window.setTimeout(function () {
            if (_this_1.latestNetworkRequest.readyState === XMLHttpRequest.DONE) {
                return;
            }
            idScanResultCallback.uploadMessageOverride("Still Uploading...");
        }, 6000);
    };
    //
    // Part 18:  This function gets called after the FaceTec SDK is completely done.  There are no parameters because you have already been passed all data in the processSessionWhileFaceTecSDKWaits function and have already handled all of your own results.
    //
    PhotoIDMatchProcessor.prototype.onFaceTecSDKCompletelyDone = function () {
        //
        // DEVELOPER NOTE:  onFaceTecSDKCompletelyDone() is called after you signal the FaceTec SDK with success() or cancel().
        // Calling a custom function on the Sample App Controller is done for demonstration purposes to show you that here is where you get control back from the FaceTec SDK.
        //
        var _this = this;
        // If enrollment was not successful, clear the enrollment identifier
        if (!this.success) {
            console.log(this.sampleAppControllerReference);
            this.sampleAppControllerReference.clearLatestEnrollmentIdentifier();
        }
        this.sampleAppControllerReference.onComplete(_this.latestSessionResult, _this.latestIDScanResult);
    };
    //
    // DEVELOPER NOTE:  This public convenience method is for demonstration purposes only so the Sample App can get information about what is happening in the processor.
    // In your code, you may not even want or need to do this.
    //
    PhotoIDMatchProcessor.prototype.isSuccess = function () {
        return this.success;
    };
    return PhotoIDMatchProcessor;
}());
var PhotoIDMatchProcessor = PhotoIDMatchProcessor;
