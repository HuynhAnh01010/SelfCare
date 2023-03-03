package vn.mobileid.selfcare.tse;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import vn.mobileid.selfcare.tse.exception.CXI_ParseApduException;

public enum Enum_MechHashAlg {
    CXI_MECH_HASH_ALGO_SHA1(16),
    CXI_MECH_HASH_ALGO_RMD160(32),
    CXI_MECH_HASH_ALGO_SHA224(48),
    CXI_MECH_HASH_ALGO_SHA256(64),
    CXI_MECH_HASH_ALGO_MD5(80),
    CXI_MECH_HASH_ALGO_SHA384(96),
    CXI_MECH_HASH_ALGO_SHA512(112),
    CXI_MECH_HASH_ALGO_SHA3_224(128),
    CXI_MECH_HASH_ALGO_SHA3_256(144),
    CXI_MECH_HASH_ALGO_SHA3_384(160),
    CXI_MECH_HASH_ALGO_SHA3_512(176);

    public final int val;

    private Enum_MechHashAlg(int val) {
        this.val = val;
    }

    public static Enum_MechHashAlg valueOf(int tag) throws CXI_ParseApduException {
        Enum_MechHashAlg[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Enum_MechHashAlg cmd = var1[var3];
            if (cmd.val == tag) {
                return cmd;
            }
        }

        throw new CXI_ParseApduException(String.format("Unknown commands: 0x%04X", tag));
    }
}
