var _0x3a609f = _0x1fcc = {};

var SHOULD_LOG_INFO = ![],
    SHOULD_LOG_ERRORS = !![];

function logInfo(_0xb0e7c) {
    var _0x3b290b = _0x1fcc;
    if (SHOULD_LOG_INFO == ![] || console === undefined || console === null || console[`log`] === undefined || console[`log`] === null) return;
    console[`trace`](`MainWASMWorker >` + '\x20' + _0xb0e7c);
}

function logError(_0x263cfb) {
    var _0x2a8ad8 = _0x1fcc;
    if (SHOULD_LOG_ERRORS == ![] || console === undefined || console === null || console[`error`] === undefined || console[`error`] === null) {
        if (`BnTCI` === `BnTCI`) return;
        else {
            function _0x4c4326() {
                var _0x5be123 = _0x2a8ad8;
                _0x551a20[`nextPhase`] = _0x11f78e[`nextPhase`][`value`];
            }
        }
    }
    console[`error`](`MainWASMWorker >` + '\x20' + _0x263cfb);
}
var moduleInitialized = ![],
    preloadComplete = ![],
    _0x54a9c7 = {};
_0x54a9c7[`locateFile`] = function(_0x1d61fb) {
    return './' + _0x1d61fb;
};
var Module = _0x54a9c7;
Module[`onRuntimeInitialized`] = function() {
    var _0x4bc683 = _0x3a609f;
    moduleInitialized = !![];
    var _0x522e66 = {};
    _0x522e66[`process`] = `onMainWorker_FileLoaded`, postMessage(_0x522e66);
}, Module[`setStatus`] = function(_0xce42ee) {
    var _0x2776d9 = _0x3a609f;
    logInfo(`moduleMessage: ` + _0xce42ee);
}, Module[`onAbort`] = function(_0x46278b) {
    var _0x7f557b = _0x3a609f;
    if (moduleInitialized == ![]) {
        var _0x30128a = {};
        _0x30128a[`process`] = `onMainWorker_FailedToLoad`, _0x30128a[`error`] = _0x46278b, postMessage(_0x30128a);
        return;
    } else logError(`FaceTec Browser SDK Error Code 832774: ` + _0x46278b);
};
try {
    var versionString = '';
    this && this[`location`] && this[`location`][`search`] && (versionString = this[`location`][`search`]);
    var workerUrl = `./011c90516755d702cfb4205ca9d93e21fe6683b8.js` + versionString;
    importScripts(workerUrl);
} catch (_0x103c6f) {
    logError(`FaceTec Browser SDK Error Code 832766: ` + _0x103c6f);
}
onmessage = function(_0x10d129) {
    var _0x2aecb6 = _0x3a609f;
    switch (_0x10d129[`data`][`process`]) {
        case `toMainWASM_startEngineLoad`:
            if (moduleInitialized && !preloadComplete) {
                preloadComplete = !![];
                try {
                    Module[`preload`]();
                } catch (_0x3ed7cc) {
                    logError(`FaceTec Browser SDK Error Code 832867: ` + _0x3ed7cc);
                }
                var _0xe6160b = ![];
                try {
                    var _0x4be84e = {};
                    _0x4be84e[`iat`] = _0x10d129[`data`][`iat`],
                        _0x4be84e[`ipk`] = _0x10d129[`data`][`ipk`],
                        _0xe6160b = Module[`initialize`](_0x4be84e);
                } catch (_0x52e446) {
                    logError(`FaceTec Browser SDK Error Code 832868: ` + _0x52e446), _0xe6160b = ![];
                }
                logInfo(`MAIN WASM WORKER: Calling back out with event: onMainWorker_CompletelyLoaded`);
                var _0x5630a5 = {};
                _0x5630a5[`process`] = `onMainWorker_CompletelyLoaded`, _0x5630a5[`initializeWasmResult`] = _0xe6160b, postMessage(_0x5630a5);
            }
            break;
        case `toMainWASM_validateLicense`:
            if (Module[`validateLicense`]) {
                if (`LxHZP` === `LxHZP`) try {
                    // var _0x1f8f15 = Module[`validateLicense`](_0x10d129[`data`][`apptoken`], _0x10d129[`data`][`license`]),
                    var _0x1f8f15 = true,
                        _0x59b537 = {};
                    _0x59b537[`process`] = `onMainWorker_LicenseValidationAttemptProcessed`,
                        _0x59b537[`validateLicenseResult`] = _0x1f8f15, postMessage(_0x59b537);
                } catch (_0x2b1313) {
                    var _0x3bec92 = {};
                    _0x3bec92[`process`] = `onMainWorker_LicenseValidationAttemptProcessed`, _0x3bec92[`validateLicenseResult`] = ![],
                        postMessage(_0x3bec92);
                } else {
                    function _0x268b29() {
                        var _0x578301 = _0x2aecb6;
                        _0x3eafeb(_0x4a5999, _0x5500dd), _0x37fa9a(`MAIN WASM WORKER: Calling back out with event: onMainWorker_EndSessionComplete`);
                        var _0x501874 = {};
                        _0x501874[`process`] = `onMainWorker_EndSessionComplete`, _0x501874[`wasmSessionResult`] = _0x3d337f, _0x27566b(_0x501874);
                    }
                }
            } else {
                var _0xca590b = {};
                _0xca590b[`process`] = `onMainWorker_LicenseValidationAttemptProcessed`, _0xca590b[`validateLicenseResult`] = ![], postMessage(_0xca590b), logError(`Warning: No license validation module was found.`);
            }
            break;
        case `toMainWASM_setCurrentPreSessionPhase`:
            try {
                Module[`setCurrentPreSessionPhase`](_0x10d129[`data`][`phase`]);
            } catch (_0x508222) {
                if (`zIugE` !== `zIugE`) {
                    function _0x4a419a() {
                        var _0x3359c6 = _0x2aecb6;
                        if (_0x4a8a52 == ![]) {
                            var _0x38d468 = {};
                            _0x38d468[`process`] = `onMainWorker_FailedToLoad`, _0x38d468[`error`] = _0x272dc5, _0x150a20(_0x38d468);
                            return;
                        } else _0xe8b3b6(`FaceTec Browser SDK Error Code 832774: ` + _0x3c6c73);
                    }
                } else logError(`Warning: toMainWASM_setCurrentPreSessionPhase >> Error trying to set current phase.`);
            }
            break;
        case `toMainWASM_startPreSession`:
            startSessionStage(`onMainWorker_StartPreSessionComplete`, `startPreSession`, _0x10d129);
            break;
        case `toMainWASM_startSession`:
            startSessionStage(`onMainWorker_StartRegularSessionComplete`, `startSession`, _0x10d129);
            break;
        case `toMainWASM_startPostSession`:
            startSessionStage(`onMainWorker_StartPostSessionComplete`, `startPostSession`, _0x10d129);
            break;
        case `toMainWASM_updateSession`:
            startSessionStage(`onMainWorker_UpdateSessionComplete`, `updateSession`, _0x10d129);
            break;
        case `toMainWASM_endSession`: {
            if (`rUnKz` === `WiVgJ`) {
                function _0x269bf0() {
                    var _0x1de955 = _0x2aecb6;
                    _0x4423ab = _0x587d83[`clearBackDocumentImages`]();
                }
            } else {
                var _0x22f419 = null,
                    _0x261f11, _0x318d31;
                try {
                    _0x261f11 = createVectorOfStringsFromArray(_0x10d129[`data`][`dca`]), _0x318d31 = createVectorOfStringsFromArray(_0x10d129[`data`][`dma`]), _0x22f419 = Module[`endSession`](createDeviceData(_0x10d129, _0x261f11, _0x318d31));
                } catch (_0x2e3dc2) {
                    logInfo(`MAIN WASM WORKER: Calling back out with event: onMainWorker_EndSessionException`), clearVectorOfStringsIfPossible(_0x261f11, _0x318d31);
                    var _0xa18661 = {};
                    _0xa18661[`process`] = `onMainWorker_EndSessionException`, postMessage(_0xa18661);
                    return;
                }
                if (_0x22f419 !== null) try {
                    if (`rWIVq` !== `vqCzp`) {
                        var _0xf374ec = _0x22f419[`facemap`],
                            _0x5c6144 = {};
                        _0x5c6144[`type`] = `application/octet-stream`;
                        var _0x5b49eb = new Blob([_0xf374ec], _0x5c6144),
                            _0x135b48 = _0x22f419[`diagnosticModel`],
                            _0x1282be = _0x22f419[`sessionId`],
                            _0x29940d = _0x22f419[`deviceTier`],
                            _0x3c4e57 = _0x22f419[`diagnosticModelEncrypted`],
                            _0x4f2791 = 0x0;
                        if (_0x22f419 && _0x22f419[`status`]) {
                            if (`ICPCZ` === `OuASq`) {
                                function _0x36ba1c() {
                                    var _0x315c73 = _0x2aecb6,
                                        _0x405156 = new _0x3e0b30[(`Vect`) + (`orSt`) + (`ring`) + 's']();
                                    for (var _0x50272f = 0x0; _0x50272f < _0x3e4e04[`length`]; _0x50272f++) {
                                        _0x405156[`push_back`](_0x283ea6[_0x50272f]);
                                    }
                                    return _0x405156;
                                }
                            } else _0x4f2791 = _0x22f419[`status`][`value`];
                        }
                        var _0x221952 = {};
                        _0x221952[`auditTrail`] = [], _0x221952[`lowQualityAuditTrail`] = [], _0x221952[`retryImage`] = undefined, _0x221952[`retryFeedbackType`] = undefined, _0x221952[`wasTimeout`] = _0x22f419[`wasTimeout`], _0x221952[`faceScan`] = _0x5b49eb, _0x221952[`diagnosticModel`] = _0x135b48, _0x221952[`ssi`] = _0x1282be, _0x221952[`deviceTier`] = _0x29940d, _0x221952[`diagnosticModelEncrypted`] = _0x3c4e57, _0x221952[`status`] = _0x4f2791;
                        var _0x3c67dc = _0x221952;
                        _0x22f419[`auditTrailImages`][`forEach`](function(_0x3f39bb) {
                            var _0x49a827 = _0x2aecb6;
                            _0x3c67dc[`auditTrail`][`push`](createBase64ImageFromIntArray(_0x3f39bb));
                        }), _0x22f419[`lowQualityAuditTrailImages`][`forEach`](function(_0x1a2a2e) {
                            var _0xafa613 = _0x2aecb6;
                            _0x3c67dc[`lowQualityAuditTrail`][`push`](createBase64ImageFromIntArray(_0x1a2a2e));
                        });
                        if (_0x22f419[`retryImage`] && typeof _0x22f419[`retryImage`] === `object`) {
                            if (`CbbNS` === `CbbNS`) _0x3c67dc[`retryImage`] = createBase64ImageFromIntArray(_0x22f419[`retryImage`]);
                            else {
                                function _0x32f4bd() {
                                    var _0x31f975 = _0x2aecb6;
                                    _0x5f5dc7(`FaceTec Browser SDK Error Code 832771: ` + _0x8e85ce);
                                }
                            }
                        }
                        if (_0x22f419[`retryFeedbackType`] && typeof _0x22f419[`retryFeedbackType`] === `object`) {
                            if (`mhtum` === `mhtum`) _0x3c67dc[`retryFeedbackType`] = _0x22f419[`retryFeedbackType`][`value`];
                            else {
                                function _0x466ea8() {
                                    var _0x2899f9 = _0x2aecb6,
                                        _0x2409ac = _0x4bc8aa(_0x1a8e5a[`data`][`dca`]),
                                        _0xa2e6a = _0x8fbfcb(_0x3df879[`data`][`dma`]),
                                        _0x1325da = _0x52edeb(_0x3000b3),
                                        _0x5a2c35 = _0x4fec33(_0x458619, _0x2409ac, _0xa2e6a);
                                    _0x41fc1f[_0x5cd2d9](_0x1325da, _0x5a2c35), _0x3010b6(_0x2409ac, _0xa2e6a), _0x6642ee(`MAIN WASM WORKER: Calling back out with event: ` + _0x58676a);
                                    var _0x37049f = {};
                                    _0x37049f[`process`] = _0x51e144, _0xcbf7d7(_0x37049f);
                                }
                            }
                        }
                        logInfo(`MAIN WASM WORKER: Calling back out with event: onMainWorker_EndSessionComplete`);
                        var _0x23d8c5 = {};
                        _0x23d8c5[`process`] = `onMainWorker_EndSessionComplete`, _0x23d8c5[`wasmSessionResult`] = _0x3c67dc, postMessage(_0x23d8c5), _0x22f419[`delete`](), clearVectorOfStringsIfPossible(_0x261f11, _0x318d31);
                    } else {
                        function _0x4229fa() {
                            var _0x267bcb = _0x2aecb6;
                            _0x188f2d[`delete`]();
                        }
                    }
                } catch (_0x41e301) {
                    if (`tTfME` !== `tTfME`) {
                        function _0x4549ca() {
                            var _0x3b0c8d = _0x2aecb6,
                                _0x3b70ae = '';
                            this && this[`location`] && this[`location`][`search`] && (_0x3b70ae = this[`location`][`search`]);
                            var _0x220717 = `./011c90516755d702cfb4205ca9d93e21fe6683b8.js` + _0x3b70ae;
                            _0x5ab46f(_0x220717);
                        }
                    } else {
                        logInfo(`MAIN WASM WORKER: Calling back out with event: onMainWorker_EndSessionException`);
                        var _0x35c2f3 = {};
                        _0x35c2f3[`process`] = `onMainWorker_EndSessionException`, postMessage(_0x35c2f3);
                    }
                } else {
                    clearVectorOfStringsIfPossible(_0x261f11, _0x318d31), logInfo(`MAIN WASM WORKER: Calling back out with event: onMainWorker_EndSessionComplete`);
                    var _0x69c37b = {};
                    _0x69c37b[`process`] = `onMainWorker_EndSessionComplete`, _0x69c37b[`wasmSessionResult`] = _0x22f419, postMessage(_0x69c37b);
                }
                break;
            }
        }
        case `processPreSessionFrame`:
            var _0x1934b8 = null,
                _0x1dc3c0;
            try {
                if (`gNDGI` !== `gNDGI`) {
                    function _0x1d9146() {
                        var _0x8d17b9 = _0x2aecb6;
                        _0x2bb9ec = this[`location`][`search`];
                    }
                } else _0x1dc3c0 = Module[`processFrame`](_0x10d129[`data`][`image`], _0x10d129[`data`][`imageWidth`], _0x10d129[`data`][`imageHeight`], _0x10d129[`data`][`landscapeImage`], _0x10d129[`data`][`landscapeImageWidth`], _0x10d129[`data`][`landscapeImageHeight`]);
            } catch (_0x5830d3) {
                _0x1dc3c0 = null, logError(`FaceTec Browser SDK Error Code 832775: ` + _0x5830d3);
            }
            if (_0x1dc3c0 !== null) {
                var _0x4245e4 = {};
                _0x4245e4[`frameIndex`] = _0x1dc3c0[`frameIndex`], _0x4245e4[`feedback`] = undefined, _0x4245e4[`fte`] = _0x1dc3c0[`fte`], _0x4245e4[`fteReason`] = _0x1dc3c0[`fteReason`], _0x4245e4[`nextPhase`] = undefined, _0x4245e4[`deviceTier`] = _0x1dc3c0[`deviceTier`], _0x4245e4[`sizeCategory`] = _0x1dc3c0[`sizeCategory`], _0x4245e4[`brightnessScore`] = _0x1dc3c0[`brightnessScore`], _0x4245e4[`darknessScore`] = _0x1dc3c0[`darknessScore`], _0x4245e4[`makingFaceScore`] = _0x1dc3c0[`makingFaceScore`], _0x4245e4[`eyesLookingAwayScore`] = _0x1dc3c0[`eyesLookingAwayScore`], _0x4245e4[`sunglassesScore`] = _0x1dc3c0[`sunglassesScore`], _0x4245e4[`glareInGlassesScore`] = _0x1dc3c0[`glareInGlassesScore`], _0x4245e4[`brightnessScoreCenterRegion`] = _0x1dc3c0[`brightnessScoreCenterRegion`], _0x4245e4[`lowLightDetected`] = _0x1dc3c0[`lowLightDetected`], _0x1934b8 = _0x4245e4;
                try {
                    _0x1934b8[`feedback`] = _0x1dc3c0[`feedback`][`value`];
                } catch (_0x1a7fac) {
                    if (`TIzIA` === `YTdtv`) {
                        function _0x43c134() {
                            var _0x333fb8 = _0x2aecb6;
                            _0x85ee93[`lowQualityAuditTrail`][`push`](_0x3cdf9e(_0x125e28));
                        }
                    } else logError(`FaceTec Browser SDK Error Code 832776: ` + _0x1a7fac);
                }
                try {
                    _0x1934b8[`nextPhase`] = _0x1dc3c0[`nextPhase`][`value`];
                } catch (_0x3fe28d) {
                    logError(`FaceTec Browser SDK Error Code 832777: ` + _0x3fe28d);
                }
                _0x1dc3c0[`delete`]();
            }
            logInfo(`MAIN WASM WORKER: Calling back out with event: onMainWorker_PreSessionFrameProcessed`);
            var _0x58591a = {};
            _0x58591a[`process`] = `onMainWorker_PreSessionFrameProcessed`, _0x58591a[`faceResult`] = _0x1934b8, postMessage(_0x58591a);
            break;
        case `processFrame`:
            var _0x2ca346 = this[`performance`][`now`](),
                _0x412fbd = null,
                _0x132bb7 = null;
            try {
                _0x132bb7 = Module[`processFrame`](_0x10d129[`data`][`image`], _0x10d129[`data`][`imageWidth`], _0x10d129[`data`][`imageHeight`], _0x10d129[`data`][`landscapeImage`], _0x10d129[`data`][`landscapeImageWidth`], _0x10d129[`data`][`landscapeImageHeight`]);
            } catch (_0x23d6a5) {
                _0x132bb7 = null, logError(`FaceTec Browser SDK Error Code 832778: ` + _0x23d6a5);
            }
            if (_0x132bb7 !== null) {
                if (`VbPfx` !== `oYUOI`) {
                    var _0xd5e998 = {};
                    _0xd5e998[`feedback`] = undefined, _0xd5e998[`fte`] = _0x132bb7[`fte`], _0xd5e998[`fteReason`] = _0x132bb7[`fteReason`], _0xd5e998[`nextPhase`] = undefined, _0xd5e998[`deviceTier`] = _0x132bb7[`deviceTier`], _0xd5e998[`sizeCategory`] = _0x132bb7[`sizeCategory`], _0xd5e998[`brightnessScore`] = _0x132bb7[`brightnessScore`], _0xd5e998[`brightnessScoreCenterRegion`] = _0x132bb7[`brightnessScoreCenterRegion`], _0xd5e998[`lowLightDetected`] = _0x132bb7[`lowLightDetected`], _0x412fbd = _0xd5e998;
                    try {
                        _0x412fbd[`feedback`] = _0x132bb7[`feedback`][`value`];
                    } catch (_0x104b62) {
                        if (`ZQsPU` === `ZQsPU`) logError(`FaceTec Browser SDK Error Code 832779: ` + _0x104b62);
                        else {
                            function _0x15fedc() {
                                var _0x32ad6b = _0x2aecb6,
                                    _0x1d6689 = {};
                                _0x1d6689[`iat`] = _0x5b600b[`data`][`iat`], _0x1d6689[`ipk`] = _0x2c76d4[`data`][`ipk`], _0x267322 = _0x230902[`initialize`](_0x1d6689);
                            }
                        }
                    }
                    try {
                        if (`tRuiu` === `mUVca`) {
                            function _0x556979() {
                                var _0x2f7a36 = _0x2aecb6;
                                _0x583151(`FaceTec Browser SDK Error Code 832766: ` + _0x45c846);
                            }
                        } else _0x412fbd[`nextPhase`] = _0x132bb7[`nextPhase`][`value`];
                    } catch (_0x4e7354) {
                        logError(`FaceTec Browser SDK Error Code 832780: ` + _0x4e7354);
                    }
                    _0x132bb7[`delete`]();
                } else {
                    function _0x3926db() {
                        var _0x5f4b70 = _0x2aecb6;
                        if (_0x19c517 == ![] || _0x4ffb72 === _0x39ff2b || _0x3ef0d9 === null || _0x196808[`error`] === _0x4a95c7 || _0x33a974[`error`] === null) return;
                        _0x1c1422[`error`](`MainWASMWorker >` + '\x20' + _0x4e1d3b);
                    }
                }
            }
            logInfo(`MAIN WASM WORKER: Calling back out with event: onMainWorker_RegularSessionFrameProcessed`);
            var _0x486841 = {};
            _0x486841[`process`] = `onMainWorker_RegularSessionFrameProcessed`, _0x486841[`faceResult`] = _0x412fbd, postMessage(_0x486841);
            break;
        case `processDocumentImage`:
            try {
                var _0x45e180 = `processFrontDocumentImage`;
                _0x10d129[`data`][`imageType`] === `back` && (_0x45e180 = `processBackDocumentImage`), Module[_0x45e180](_0x10d129[`data`][`image`], _0x10d129[`data`][`width`], _0x10d129[`data`][`height`], _0x10d129[`data`][`uuid`]);
            } catch (_0x13ce5a) {
                if (`bKkUx` === `bKkUx`) logError(`FaceTec Browser SDK Error Code 832769: ` + _0x13ce5a);
                else {
                    function _0x47445f() {
                        var _0x1d4402 = _0x2aecb6;
                        _0x17e87b = _0x5dc66e[`clearFrontDocumentImages`]();
                    }
                }
            }
            logInfo(`MAIN WASM WORKER: Calling back out with event: ` + _0x10d129[`data`][`process`]);
            var _0x322eae = {};
            _0x322eae[`process`] = _0x10d129[`data`][`process`], postMessage(_0x322eae);
            break;
        case `createIDScanMap`:
            var _0xc39dbe = null,
                _0x572789 = null,
                _0x1600d6 = '';
            try {
                if (`nBAll` === `nBAll`) {
                    _0xc39dbe = Module[`createIdentityDocument`](_0x10d129[`data`][`dzh`]);
                    var _0xc55228 = _0xc39dbe[`idScan`],
                        _0x49dac8 = {};
                    _0x49dac8[`type`] = `application/octet-stream`, _0x572789 = new Blob([_0xc55228], _0x49dac8), _0x1600d6 = _0xc39dbe[`uuid`];
                } else {
                    function _0x1eac41() {
                        var _0x3ce9b3 = _0x2aecb6;
                        return `data:image/jpeg;base64,` + _0x4b03a3(_0x29edda);
                    }
                }
            } catch (_0x3fa303) {
                logError(`FaceTec Browser SDK Error Code 832770: ` + _0x3fa303);
            }
            logInfo(`MAIN WASM WORKER: Calling back out with event: ` + _0x10d129[`data`][`process`]);
            var _0x543fed = {};
            _0x543fed[`process`] = _0x10d129[`data`][`process`], _0x543fed[`idScanMap`] = _0x572789, _0x543fed[`uuid`] = _0x1600d6, postMessage(_0x543fed);
            _0xc39dbe && _0xc39dbe[`delete`]();
            break;
        case `clearFrontDocumentImages`:
            var _0x66cd14;
            try {
                _0x66cd14 = Module[`clearFrontDocumentImages`]();
            } catch (_0x49714f) {
                logError(`FaceTec Browser SDK Error Code 832771: ` + _0x49714f);
            }
            logInfo(`MAIN WASM WORKER: Calling back out with event: ` + _0x10d129[`data`][`process`]);
            var _0xd3a2dd = {};
            _0xd3a2dd[`process`] = _0x10d129[`data`][`process`], postMessage(_0xd3a2dd);
            if (_0x66cd14) {
                if (`pVJkS` !== `pVJkS`) {
                    function _0x151c61() {
                        var _0x1c796e = _0x2aecb6;
                        _0x506882(`MAIN WASM WORKER: Calling back out with event: onMainWorker_EndSessionException`);
                        var _0x28038b = {};
                        _0x28038b[`process`] = `onMainWorker_EndSessionException`, _0x501204(_0x28038b);
                    }
                } else _0x66cd14[`delete`]();
            }
            break;
        case `clearBackDocumentImages`:
            var _0x2335e6;
            try {
                if (`eRCig` !== `yxyie`) _0x2335e6 = Module[`clearBackDocumentImages`]();
                else {
                    function _0x5b1a17() {
                        var _0x4a53b3 = _0x2aecb6;
                        _0x141891 = !![];
                        var _0xd426a5 = {};
                        _0xd426a5[`process`] = `onMainWorker_FileLoaded`, _0x1f21b0(_0xd426a5);
                    }
                }
            } catch (_0x35a6f7) {
                if (`qETXQ` === `KNaAe`) {
                    function _0x37bce3() {
                        var _0x36b122 = _0x2aecb6;
                        _0x2a5db3(`Warning: toMainWASM_setCurrentPreSessionPhase >> Error trying to set current phase.`);
                    }
                } else logError(`FaceTec Browser SDK Error Code 832772: ` + _0x35a6f7);
            }
            logInfo(`MAIN WASM WORKER: Calling back out with event: ` + _0x10d129[`data`][`process`]);
            var _0x56ac32 = {};
            _0x56ac32[`process`] = _0x10d129[`data`][`process`], postMessage(_0x56ac32);
            _0x2335e6 && _0x2335e6[`delete`]();
            break;
        default:
            logError(`Invalid process sent to worker:` + _0x10d129[`data`][`process`]);
            break;
    }
};

function createBase64ImageFromIntArray(_0x271c07) {
    var _0xf8581b = _0x3a609f;
    const _0x575e9c = _0x271c07[`reduce`](function(_0x29c75b, _0x315787) {
        var _0x445500 = _0xf8581b;
        if (`lnfdY` === `lnfdY`) return _0x29c75b + String[`fromCharCode`](_0x315787);
        else {
            function _0x2d3c58() {
                var _0x28f981 = _0x445500,
                    _0x980452 = `processFrontDocumentImage`;
                _0x482bb9[`data`][`imageType`] === `back` && (_0x980452 = `processBackDocumentImage`), _0x188433[_0x980452](_0x1f9210[`data`][`image`], _0x8cfdaf[`data`][`width`], _0x569023[`data`][`height`], _0x604b85[`data`][`uuid`]);
            }
        }
    }, '');
    if (_0x575e9c === '') return '';
    else {
        if (`nsiGF` === `BXFXc`) {
            function _0x2f0086() {
                var _0x40bf7e = _0xf8581b,
                    _0x488160 = {};
                _0x488160[`process`] = `onMainWorker_LicenseValidationAttemptProcessed`, _0x488160[`validateLicenseResult`] = ![], _0x7d4eca(_0x488160), _0x2d9caf(`Warning: No license validation module was found.`);
            }
        } else return `data:image/jpeg;base64,` + btoa(_0x575e9c);
    }
}

function startSessionStage(_0x38ea38, _0x452f90, _0x10e637) {
    var _0x21f080 = _0x3a609f;
    try {
        var _0x3c029e = createVectorOfStringsFromArray(_0x10e637[`data`][`dca`]),
            _0x46a59a = createVectorOfStringsFromArray(_0x10e637[`data`][`dma`]),
            _0x369624 = createSessionParams(_0x10e637),
            _0xcfbee9 = createDeviceData(_0x10e637, _0x3c029e, _0x46a59a);
        Module[_0x452f90](_0x369624, _0xcfbee9), clearVectorOfStringsIfPossible(_0x3c029e, _0x46a59a), logInfo(`MAIN WASM WORKER: Calling back out with event: ` + _0x38ea38);
        var _0x21aea8 = {};
        _0x21aea8[`process`] = _0x38ea38, postMessage(_0x21aea8);
    } catch (_0x10c2f6) {
        if (`SpyhZ` !== `PGnaI`) logError(`FaceTec Browser SDK Error Code 832773: ` + _0x10c2f6);
        else {
            function _0x463189() {
                var _0x4ed171 = _0x21f080;
                _0x108686[`feedback`] = _0x1058fa[`feedback`][`value`];
            }
        }
    }
}

function createVectorOfStringsFromArray(_0x3d71f9) {
    var _0xb77fe9 = _0x3a609f,
        _0x329b45 = new Module[(`Vect`) + (`orSt`) + (`ring`) + 's']();
    for (var _0x38fc2e = 0x0; _0x38fc2e < _0x3d71f9[`length`]; _0x38fc2e++) {
        _0x329b45[`push_back`](_0x3d71f9[_0x38fc2e]);
    }
    return _0x329b45;
}

function clearVectorOfStringsIfPossible(_0x223986, _0xf8f688) {
    var _0x1f6fad = _0x3a609f;
    _0x223986 && _0x223986[`delete`](), _0xf8f688 && _0xf8f688[`delete`]();
}

function createDeviceData(_0xb07805, _0x2441c5, _0x365bea) {
    var _0x4b8206 = _0x3a609f,
        _0x3c58ec = {};
    return _0x3c58ec[`dzh`] = _0xb07805[`data`][`dzh`], _0x3c58ec[`dii`] = _0xb07805[`data`][`dii`], _0x3c58ec[`dai`] = _0xb07805[`data`][`dai`], _0x3c58ec[`dzv`] = _0xb07805[`data`][`dzv`], _0x3c58ec[`dzt`] = _0xb07805[`data`][`dzt`], _0x3c58ec[`dzm`] = _0xb07805[`data`][`dzm`], _0x3c58ec[`dcl`] = _0xb07805[`data`][`dcl`], _0x3c58ec[`dca`] = _0x2441c5, _0x3c58ec[`dma`] = _0x365bea, _0x3c58ec[`dvs`] = _0xb07805[`data`][`dvs`], _0x3c58ec[`ctm`] = _0xb07805[`data`][`ctm`], _0x3c58ec[`ppd`] = _0xb07805[`data`][`ppd`], _0x3c58ec[`ddd`] = _0xb07805[`data`][`ddd`], _0x3c58ec[`hpp`] = _0xb07805[`data`][`hpp`], _0x3c58ec[`tld`] = _0xb07805[`data`][`tld`], _0x3c58ec[`ltu`] = _0xb07805[`data`][`ltu`], _0x3c58ec;
}

function createSessionParams(_0x1fa4bf) {
    var _0x35505d = _0x3a609f,
        _0x3fdfb6 = {};
    return _0x3fdfb6[`sae`] = _0x1fa4bf[`data`][`sae`], _0x3fdfb6[`scd`] = _0x1fa4bf[`data`][`scd`], _0x3fdfb6[`ssi`] = _0x1fa4bf[`data`][`ssi`], _0x3fdfb6[`sst`] = _0x1fa4bf[`data`][`sst`], _0x3fdfb6;
}
