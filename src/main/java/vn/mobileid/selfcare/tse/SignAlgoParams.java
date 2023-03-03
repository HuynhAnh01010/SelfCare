package vn.mobileid.selfcare.tse;


import vn.mobileid.selfcare.tse.exception.RSSP_InvalidParamException;

public enum SignAlgoParams {
    RSASSA_PSS("RSASSA-PSS", "1.2.840.113549.1.1.10", "BgkqhkiG9w0BAQo=");

    public final String algName;
    public final String oid;
    public final String derAsn1;

    private SignAlgoParams(String algName, String oid, String der) {
        this.algName = algName;
        this.oid = oid;
        this.derAsn1 = der;
    }

    public static SignAlgoParams valueOfOID(String oid) throws RSSP_InvalidParamException {
        if (Utils.isNullOrEmpty(oid)) {
            throw new RSSP_InvalidParamException("Not support Sign-Algorithm-oid: [" + oid + "]");
        } else {
            SignAlgoParams[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                SignAlgoParams signAlgo = var1[var3];
                if (oid.equalsIgnoreCase(signAlgo.oid)) {
                    return signAlgo;
                }
            }

            throw new RSSP_InvalidParamException("Not support Sign-Algorithm-oid: [" + oid + "]");
        }
    }

    public static SignAlgoParams valueOfDer(String der) throws RSSP_InvalidParamException {
        if (Utils.isNullOrEmpty(der)) {
            throw new RSSP_InvalidParamException("Not support Sign-Algorithm-oid: [" + der + "]");
        } else {
            SignAlgoParams[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                SignAlgoParams signAlgo = var1[var3];
                if (der.equalsIgnoreCase(signAlgo.derAsn1)) {
                    return signAlgo;
                }
            }

            throw new RSSP_InvalidParamException("Not support Sign-Algorithm-oid: [" + der + "]");
        }
    }
}
