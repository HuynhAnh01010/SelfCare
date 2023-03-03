/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.selfcare.service.util;

public class AppConstants {

    public static final Boolean isDev = true;

    public static final String CHAR_PATTERN = "[a-zA-Z\\s]+";
    public static final String ID_PATTERN = "[a-zA-Z0-9]+";
    public static final String CODE_PATTERN = "[0-9]{6}";
    public static final String MOBILE_PATTERN = "[0-9]{10}";
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final String CERTIFICATE_TYPE_PERSONAL = "PERSONAL";
    public static final String CERTIFICATE_TYPE_ENTERPRISE = "ENTERPRISE";
    public static final String CERTIFICATE_TYPE_STAFF = "STAFF";
    //
    public static final String CERTIFICATE_PREFIX_RQ = "-----BEGIN CERTIFICATE REQUEST-----";
    public static final String CERTIFICATE_SUFFIX_RQ = "-----END CERTIFICATE REQUEST-----";
    public static final String CERTIFICATE_PREFIX = "-----BEGIN CERTIFICATE-----";
    public static final String CERTIFICATE_SUFFIX = "-----END CERTIFICATE-----";

    public static final String[] strExtensionCertificate = {"txt", "cer", "crt", "pem"};

    //GENERAL POLICY
    public static final String MIN_LENGTH_OF_PASSWORD = "MIN_LENGTH_OF_PASSWORD";
    public static final String MAX_LENGTH_OF_PASSWORD = "MAX_LENGTH_OF_PASSWORD";
    public static final String TIMEOUT_DURATION = "TIMEOUT_DURATION";
    public static final String EXPIRATION_OF_PASSWORD = "EXPIRATION_OF_PASSWORD";
    public static final String UNBLOCK_DURATION_OF_PASSWORD = "UNBLOCK_DURATION_OF_PASSWORD";
    public static final String NUMBER_OF_PASSWORD_LIFECYCLE = "NUMBER_OF_PASSWORD_LIFECYCLE";
    public static final String MAX_COUNTER_FOR_PASSWORD = "MAX_COUNTER_FOR_PASSWORD";
    public static final String COMPLEXITY_OF_PASSWORD = "COMPLEXITY_OF_PASSWORD";
    public static final String AWS_V4_PROPERTIES = "AWS_V4_PROPERTIES";
    public static final String CA_PROPERTIES = "CA_PROPERTIES";
    public static final String BIP_CONNECTION_PROPERTIES = "BIP_CONNECTION_PROPERTIES";
//    public static final String CRL_TEMPLATE = "CRL_TEMPLATE";
//    public static final String RA_TEMPLATE = "RA_TEMPLATE";
    public static final String ETSI_102204_PROPERTIES = "ETSI_102204_PROPERTIES";
    public static final String SSL2_PROPERTIES = "SSL2_PROPERTIES";
    public static final String SMTP_PROPERTIES = "SMTP_PROPERTIES";
    public static final String SMSGW_PROPERTIES = "SMSGW_PROPERTIES";
    public static final String DMS_PROPERTIES = "DMS_PROPERTIES";
    public static final String CLOUD_PROPERTIES = "CLOUD_PROPERTIES";
    public static final String CLOUD_IP_ACCESS_PRIVILEGE = "CLOUD_IP_ACCESS_PRIVILEGE";
    public static final String RSSP_PROPERTIES = "RSSP_PROPERTIES";
    public static final String RSSP_IP_ACCESS_PRIVILEGE = "RSSP_IP_ACCESS_PRIVILEGE";
    public static final String SMART_ID_PROPERTIES = "SMART_ID_PROPERTIES";
    public static final String MOBILE_ID_PROPERTIES = "MOBILE_ID_PROPERTIES";
    public static final String TRUSTED_HUB_PROPERTIES = "TRUSTED_HUB_PROPERTIES";
    public static final String CACHE_PROPERTIES = "CACHE_PROPERTIES";
    public static final String POSTBACK_URI_PROPERTIES = "POSTBACK_URI_PROPERTIES";
    public static final String XSL_TEMPLATE_PROPERTIES = "XSL_TEMPLATE_PROPERTIES";
    public static final String XSL_TRANSFORMATIONS_PROPERTIES = "XSL_TRANSFORMATIONS_PROPERTIES";
    public static final String TSA_PROPERTIES = "TSA_PROPERTIES";
    public static final String HSM_PROFILE_PROPERTIES = "HSM_PROFILE_PROPERTIES";
    public static final String HSM_KEY_PROFILE_PROPERTIES = "HSM_KEY_PROFILE_PROPERTIES";
    public static final String WRAPPED_KEY_PROPERTIES = "WRAPPED_KEY_PROPERTIES";
    public static final String SIGNER_PROPERTIES = "SIGNER_PROPERTIES";
    public static final String SYSTEM_KEY_PROFILE_PROPERTIES = "SYSTEM_KEY_PROFILE_PROPERTIES";
    public static final String PARTY_PROPERTIES = "PARTY_PROPERTIES";
    public static final String MAX_COUNTER_FOR_PASSCODE = "MAX_COUNTER_FOR_PASSCODE";
    public static final String LENGHT_OF_OTP_PASSCODE = "LENGHT_OF_OTP_PASSCODE";
    public static final String TIMEOUT_DURATION_PASSCODE = "TIMEOUT_DURATION_PASSCODE";
    public static final String UNBLOCK_DURATION_OF_PASSCODE = "UNBLOCK_DURATION_OF_PASSCODE";
    public static final String METADATA_PROPERTIES = "METADATA_PROPERTIES";
    public static final String TIMEOUT_DURATION_BILLCODE = "TIMEOUT_DURATION_BILLCODE";
    public static final String IP_ACCESS_PRIVILEGE = "IP_ACCESS_PRIVILEGE";
    public static final String FUNCTION_ACCESS = "FUNCTION_ACCESS";
    public static final String META_DATA = "META_DATA";
    public static final String PROPERTIES = "PROPERTIES";
    public static final String PUSH_NOTIFICATION_PROPERTIES = "PUSH_NOTIFICATION_PROPERTIES";
    public static final String WEB_SERVICE_URLS = "WEB_SERVICE_URLS";
    public static final String PASSWORD_PROPERTIES = "PASSWORD_PROPERTIES";
    public static final String FMS_PROPERTIES = "FMS_PROPERTIES";
    public static final String EMAIL_TEMPLATE = "EMAIL_TEMPLATE";
    public static final String RSSP_SERVICE_URL = "RSSP_SERVICE_URL";
    public static final String TLS_TRUSTSTORE = "TLS_TRUSTSTORE";
    public static final String REGISTRATION_PARTY_PROPERTIES = "REGISTRATION_PARTY_PROPERTIES";

    //vcsp
    public static final String EMAIL_TEMPLATE_PASSCODE_NOTIFICATION_ISSUED = "EMAIL_TEMPLATE_PASSCODE_NOTIFICATION_ISSUED";
    public static final String EMAIL_TEMPLATE_PASSCODE_NOTIFICATION_CHANGEINFO = "EMAIL_TEMPLATE_PASSCODE_NOTIFICATION_CHANGEINFO";
    public static final String EMAIL_TEMPLATE_PASSCODE_NOTIFICATION_RENEWED = "EMAIL_TEMPLATE_PASSCODE_NOTIFICATION_RENEWED";
    public static final String EMAIL_TEMPLATE_OWNER_PASSWORD_NOTIFICATION = "EMAIL_TEMPLATE_OWNER_PASSWORD_NOTIFICATION";
    public static final String EMAIL_OTP_TEMPLATE_DEFAULT = "EMAIL_OTP_TEMPLATE_DEFAULT";
    public static final String SMS_OTP_TEMPLATE_DEFAULT = "SMS_OTP_TEMPLATE_DEFAULT";
    public static final String EMAIL_TEMPLATE_PASSCODE_RESET = "EMAIL_TEMPLATE_PASSCODE_RESET";
    public static final String EMAIL_TEMPLATE_OWNER_PASSWORD_RESET = "EMAIL_TEMPLATE_OWNER_PASSWORD_RESET";
    public static final String COMPLEXITY_OF_PASSCODE = "COMPLEXITY_OF_PASSCODE";

    //ENTITY NAME
    public static final String FEDERAL_IDP_ENTITY = "FEDERAL_IDP_ENTITY";
    public static final String CORE_CLOUD_ENTITY = "CORE_CLOUD_ENTITY";
    public static final String CORE_SMART_ID_ENTITY = "CORE_SMART_ID_ENTITY";
    public static final String CORE_MOBILE_ID_ENTITY = "CORE_MOBILE_ID_ENTITY";
    public static final String CORE_TRUSTED_HUB_ENTITY = "CORE_TRUSTED_HUB_ENTITY";
    public static final String REGISTRATION_ENTITY = "REGISTRATION_ENTITY";
    public static final String RELYING_ENTITY = "RELYING_ENTITY";
    public static final String CA_ENTITY = "CA_ENTITY";
    public static final String SMPP_ENTITY = "SMPP_ENTITY";
    public static final String BACKOFFICE_ENTITY = "BACKOFFICE_ENTITY";
    public static final String SELFCARE_ENTITY = "SELFCARE_ENTITY";
    public static final String DMS_ENTITY = "DMS_ENTITY";
    public static final String INTERNAL_EP_ENTITY = "INTERNAL_EP_ENTITY";
    public static final String EXTERNAL_EP_ENTITY = "EXTERNAL_EP_ENTITY";
    public static final String MNO_ENTITY = "MNO_ENTITY";
    public static final String P2P_ENTITY = "P2P_ENTITY";
    public static final String ADMIN_ENTITY = "ADMIN_ENTITY";
    public static final String GENERAL = "GENERAL";
    public static final String SYSTEM_CONFIG = "SYSTEM_CONFIG";

    public static final String APPLICATION_ZIP = "application/zip";

    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    // attributetype policy ws
    public static final String CONFIG_POLICY_WS_ATTRIBUTE_TYPE_SOAP_WS = "SOAP_WS";
    public static final String CONFIG_POLICY_WS_ATTRIBUTE_TYPE_HTTP_GET = "HTTP_GET";
    public static final String CONFIG_EXCEPTION_STRING_ERROR_NULL = "NULL";

}
