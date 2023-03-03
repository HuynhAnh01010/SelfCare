package vn.mobileid.selfcare.tse;


import vn.mobileid.selfcare.tse.exception.RSSP_InvalidParamException;

public enum SignAlgo {
    RSA("NONEwithRSA", "1.2.840.113549.1.1.1", (Enum_MechHashAlg)null),
    RSA_SHA1("SHA1withRSA", "1.2.840.113549.1.1.5", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA1),
    RSA_SHA224("SHA224withRSA", "1.2.840.113549.1.1.14", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA224),
    RSA_SHA256("SHA256withRSA", "1.2.840.113549.1.1.11", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA256),
    RSA_SHA384("SHA384withRSA", "1.2.840.113549.1.1.12", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA384),
    RSA_SHA512("SHA512withRSA", "1.2.840.113549.1.1.13", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA512),
    RSA_SHA3_224("SHA3-224withRSA", "2.16.840.1.101.3.4.3.13", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA3_224),
    RSA_SHA3_256("SHA3-256withRSA", "2.16.840.1.101.3.4.3.14", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA3_256),
    RSA_SHA3_384("SHA3-384withRSA", "2.16.840.1.101.3.4.3.15", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA3_384),
    RSA_SHA3_512("SHA3-512withRSA", "2.16.840.1.101.3.4.3.16", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA3_512);

    public final String algName;
    public final String oid;
    public final Enum_MechHashAlg mech;

    private SignAlgo(String algName, String oid, Enum_MechHashAlg mech) {
        this.algName = algName;
        this.oid = oid;
        this.mech = mech;
    }

    public static SignAlgo valueOfOID(String oid) throws RSSP_InvalidParamException {
        if (Utils.isNullOrEmpty(oid)) {
            throw new RSSP_InvalidParamException("Not support Sign-Algorithm-oid: [" + oid + "]");
        } else {
            SignAlgo[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                SignAlgo signAlgo = var1[var3];
                if (oid.equalsIgnoreCase(signAlgo.oid)) {
                    return signAlgo;
                }
            }

            throw new RSSP_InvalidParamException("Not support Sign-Algorithm-oid: [" + oid + "]");
        }
    }
}
