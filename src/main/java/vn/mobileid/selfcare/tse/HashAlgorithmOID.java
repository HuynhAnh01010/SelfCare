package vn.mobileid.selfcare.tse;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import vn.mobileid.selfcare.tse.exception.RSSP_InvalidParamException;

public enum HashAlgorithmOID {
    SHA1("SHA-1", "1.3.14.3.2.26", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA1),
    SHA224("SHA-224", "2.16.840.1.101.3.4.2.4", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA224),
    SHA256("SHA-256", "2.16.840.1.101.3.4.2.1", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA256),
    SHA384("SHA-384", "2.16.840.1.101.3.4.2.2", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA384),
    SHA512("SHA-512", "2.16.840.1.101.3.4.2.3", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA512),
    SHA224ENC("SHA-224-ENC", "", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA224),
    SHA256ENC("SHA-256-ENC", "", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA256),
    SHA384ENC("SHA-384-ENC", "", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA384),
    SHA512ENC("SHA-512-ENC", "", Enum_MechHashAlg.CXI_MECH_HASH_ALGO_SHA512);

    public final String name;
    public final String oid;
    public final Enum_MechHashAlg mech;

    private HashAlgorithmOID(String name, String oid, Enum_MechHashAlg mech) {
        this.name = name;
        this.oid = oid;
        this.mech = mech;
    }

    public static HashAlgorithmOID isSupportHashAlg(String hashAlg) {
        HashAlgorithmOID[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            HashAlgorithmOID hashSupported = var1[var3];
            if (hashAlg.equalsIgnoreCase(hashSupported.name) || hashAlg.equalsIgnoreCase(hashSupported.name())) {
                return hashSupported;
            }
        }

        return null;
    }

    public static HashAlgorithmOID valueOfName(String name) throws RSSP_InvalidParamException {
        if (Utils.isNullOrEmpty(name)) {
            throw new RSSP_InvalidParamException("Not support hash-algorithm-name: [" + name + "]");
        } else {
            HashAlgorithmOID[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                HashAlgorithmOID hashSupported = var1[var3];
                if (name.equalsIgnoreCase(hashSupported.name)) {
                    return hashSupported;
                }
            }

            throw new RSSP_InvalidParamException("Not support hash-algorithm-name: [" + name + "]");
        }
    }

    public static HashAlgorithmOID valueOfOID(String oid) throws RSSP_InvalidParamException {
        if (Utils.isNullOrEmpty(oid)) {
            throw new RSSP_InvalidParamException("Not support hash-algorithm-oid: [" + oid + "]");
        } else {
            HashAlgorithmOID[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                HashAlgorithmOID hashSupported = var1[var3];
                if (oid.equalsIgnoreCase(hashSupported.oid)) {
                    return hashSupported;
                }
            }

            throw new RSSP_InvalidParamException("Not support hash-algorithm-oid: [" + oid + "]");
        }
    }
}
