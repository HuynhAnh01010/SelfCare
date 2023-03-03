///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package vn.mobileid.selfcare.dao;
//
//import java.sql.CallableStatement;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Types;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import javax.sql.DataSource;
//import javax.swing.text.html.parser.Entity;
//import lombok.extern.slf4j.Slf4j;
//import org.bouncycastle.asn1.dvcs.ServiceType;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//import vn.mobileid.selfcare.domain.User;
//import vn.mobileid.selfcare.service.dto.csr.AuthMode;
//import vn.mobileid.selfcare.service.dto.csr.CloudCertificateAttr;
//import vn.mobileid.selfcare.service.dto.csr.CloudCertificateAttrType;
//import vn.mobileid.selfcare.service.util.AppUtils;
//
///**
// *
// * @author Mobile ID 21
// */
//@Repository
//@Slf4j
//public class CloudCertificateDao {
//    @Autowired
//    private DataSource dataSource;
//
//
//    public List<CloudCertificateAttr> getCloudCertificateAttr(long id, boolean language) {
//        List<CloudCertificateAttr> cloudCertificateAttrs = new ArrayList<>();
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_ATTR_LIST", 2);
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            callStm.setLong("pCLOUD_CERTIFICATE_ID", id);
//            callStm.setBoolean("pLANGUAGE", language);
//
//            log.info("QUERY: {}", callStm.toString());
//            ResultSet rs = callStm.executeQuery();
//            while (rs.next()) {
//                CloudCertificateAttr attr = new CloudCertificateAttr();
//                attr.setId(rs.getLong("id"));
//                attr.setValue(rs.getString("value"));
//
//                CloudCertificateAttrType cloudCertificateAttrType = new CloudCertificateAttrType();
//                cloudCertificateAttrType.setName(rs.getString("cloud_certificate_attr_type_name"));
//                cloudCertificateAttrType.setId(rs.getLong("cloud_certificate_attr_type_id"));
//                attr.setCloudCertificateAttrType(cloudCertificateAttrType);
//
//                cloudCertificateAttrs.add(attr);
//            }
//            return cloudCertificateAttrs;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return null;
//    }
//
//    public CloudCertificate get(long id, Boolean language) {
//        String sql = "{call SP_BO_CLOUD_CERTIFICATE_DETAIL(?,?)}";
//        CloudCertificate item = new CloudCertificate();
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//
//            callStm.setLong("pCLOUD_CERTIFICATE_ID", id);
//            callStm.setBoolean(TableColumns.pLANGUAGE.name(), language);
//            log.info("QUERY: {}", callStm.toString());
//            ResultSet rs = callStm.executeQuery();
//            if (rs.next()) {
//
//                item.setId(rs.getLong(TableColumns.ID.name()));
//                item.setCertificateUuid(rs.getString("certificate_uuid"));
//
//                CloudAgreement cloudAgreement = new CloudAgreement();
//                cloudAgreement.setAgreementUuid(rs.getString("agreement_uuid"));
//                item.setCloudAgreement(cloudAgreement);
//
//                item.setEnterpriseId(rs.getString("enterprise_id"));
//                item.setPersonalId(rs.getString("personal_id"));
//                item.setPersonalName(rs.getString("personal_name"));
//                item.setCompanyName(rs.getString("company_name"));
//                item.setAmount(rs.getDouble("amount"));
//                item.setGovernmentAmount(rs.getDouble("government_amount"));
//                item.setEffectiveDt(rs.getTimestamp("EFFECTIVE_DT"));
//                item.setExpirationDt(rs.getTimestamp("EXPIRATION_DT"));
//                item.setDuration(rs.getLong("DURATION"));
//                item.setCertificateSn(rs.getString("CERTIFICATE_SN"));
//                item.setCertificateThumbprint(rs.getString("CERTIFICATE_THUMBPRINT"));
//                item.setSubject(rs.getString("SUBJECT"));
//                item.setAuthorisationPhone(rs.getString("AUTHORISATION_PHONE"));
//                item.setAuthorisationEmail(rs.getString("AUTHORISATION_EMAIL"));
//                item.setMultiSignature(rs.getInt("multi_signature"));
////                item.setHsmEnabled(rs.getBoolean(CloudCertificate.columns.HSM_ENABLED.name()));
//                item.setHsmProperties(rs.getString("HSM_PROPERTIES"));
//                item.setWrappedKeyProperties(rs.getString("WRAPPED_KEY_PROPERTIES"));
//                item.setSignerProperties(rs.getString("SIGNER_PROPERTIES"));
//                item.setCsr(rs.getString("CSR"));
//                item.setCertificate(rs.getString("certificate"));
//                item.setScal(rs.getInt("scal"));
//                item.setMsisdn(rs.getString("msisdn"));
//                item.setIccid(rs.getString("iccid"));
//                item.setMultiRelyingParty(rs.getInt("multi_relying_party"));
//
//
//                item.setCreatedDt(rs.getTimestamp(TableColumns.CREATED_DT.name()));
//                item.setModifiedDt(rs.getTimestamp(TableColumns.MODIFIED_DT.name()));
//                item.setEnrolledDt(rs.getTimestamp("ENROLLED_DT"));
//                item.setRevokedDt(rs.getTimestamp("REVOKED_DT"));
//
//                User userByCreatedBy = new User();
//                userByCreatedBy.setUsername(rs.getString(TableColumns.CREATED_BY_NAME.name()));
//                item.setUserByCreatedBy(userByCreatedBy);
//
//                User userByModifiedBy = new User();
//                userByModifiedBy.setUsername(rs.getString(TableColumns.MODIFIED_BY_NAME.name()));
//                item.setUserByModifiedBy(userByModifiedBy);
//
//                User userByEnrolledBy = new User();
//                userByEnrolledBy.setUsername(rs.getString("ENROLLED_BY_NAME"));
//                item.setUserByEnrolledBy(userByEnrolledBy);
//
//                User userByRevokedBy = new User();
//                userByRevokedBy.setUsername(rs.getString("REVOKED_BY_NAME"));
//                item.setUserByRevokedBy(userByRevokedBy);
//
//                CertificateProfile cerProfile = new CertificateProfile();
//                cerProfile.setName(rs.getString("CERTIFICATE_PROFILE_NAME"));
//                item.setCertificateProfile(cerProfile);
//
//                CertificateState cerState = new CertificateState();
//                cerState.setName(rs.getString("CERTIFICATE_STATE_NAME"));
//                item.setCertificateState(cerState);
//
//                CertificateType cerType = new CertificateType();
//                cerType.setId(rs.getLong("CERTIFICATE_TYPE_ID"));
//                cerType.setName(rs.getString("CERTIFICATE_TYPE_NAME"));
//                item.setCertificateType(cerType);
//
//                CertificateAuthority ca = new CertificateAuthority();
//                ca.setId(rs.getLong("CERTIFICATE_AUTHORITY_ID"));
//                ca.setName(rs.getString("CERTIFICATE_AUTHORITY_NAME"));
//                item.setCertificateAuthority(ca);
//
//                Entity entity = new Entity();
//                entity.setName(rs.getString("ENTITY_NAME"));
//                item.setEntity(entity);
//
//                RegistrationParty regisParty = new RegistrationParty();
//                regisParty.setName(rs.getString("REGISTRATION_PARTY_NAME"));
//                item.setRegistrationParty(regisParty);
//
//                SharedMode sharedMode = new SharedMode();
//                sharedMode.setName(rs.getString("SHARED_MODE_NAME"));
//                sharedMode.setId(rs.getLong("SHARED_MODE_ID"));
//                item.setSharedMode(sharedMode);
//
//                AuthMode authMode = new AuthMode();
//                authMode.setId(rs.getLong("auth_mode_id"));
//                item.setAuthMode(authMode);
//
//                ServiceType serviceType = new ServiceType();
//                serviceType.setName(rs.getString("SERVICE_TYPE_NAME"));
//                item.setServiceType(serviceType);
//
//                RelyingParty relyingParty = new RelyingParty();
//                relyingParty.setId(rs.getLong("created_relying_party_id"));
//                item.setRelyingParty(relyingParty);
//
//                CertificateSigningProfile certificateSigningProfile = new CertificateSigningProfile();
//                certificateSigningProfile.setId(rs.getLong("certificate_signing_profile_id"));
//                item.setCertificateSigningProfile(certificateSigningProfile);
//
//                item.setRemainingSigningCounter(rs.getInt("remaining_signing_counter"));
//
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//
//            log.error(ex.getMessage());
//            return null;
//        }
//        return item;
//    }
//
//
//    public int count(Boolean enabled, Boolean language) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public boolean updateMultiRelyingParty(CloudCertificate cloudCertificate) throws SqlExceptionCustom {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_UPDATE_MULTI_RELYING_PARTY", 3);
//        try (Connection connection = DataSourceUtils.getConnection(dataSource); CallableStatement callStm = connection.prepareCall(sql)) {
//
//            AppUtils.addNullOrLong(callStm, "pCLOUD_CERTIFICATE_ID", cloudCertificate.getId());
//            AppUtils.addNullOrInt(callStm, "pMULTI_RELYING_PARTY", cloudCertificate.getMultiRelyingParty());
//            AppUtils.addNullOrLong(callStm, "pMODIFIED_BY ", cloudCertificate.getUserByModifiedBy().getId());
//
//            log.info("QUERY: {}", callStm.toString());
//            callStm.execute();
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            throw new SqlExceptionCustom(ex.getMessage());
//        }
//    }
//
//
//    public List<CloudCertificate> getAll(Boolean enabled, Boolean language) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public int countConfirm(ConfirmCertificateS confirmCertificateS) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_CONFIRM_TOTAL", 7);
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//
//            if (confirmCertificateS.getConfirmState() == -1) {
//                callStm.setString("pCONFIRM_STATE", null);
//            } else {
//                callStm.setInt("pCONFIRM_STATE", confirmCertificateS.getConfirmState());
//            }
//
//            AppUtils.addBlankOrString(callStm, "pIDENTITY", confirmCertificateS.getIdentity());
//            callStm.setString("pAGREEMENT_UUID", "".equals(confirmCertificateS.getAgreementUuid()) ? null : confirmCertificateS.getAgreementUuid());
//            callStm.setString("pPERSONAL_NAME", "".equals(confirmCertificateS.getPersonalName()) ? null : confirmCertificateS.getPersonalName());
//            callStm.setString("pAUTHORISATION_PHONE", "".equals(confirmCertificateS.getAuthorisationPhone()) ? null : confirmCertificateS.getAuthorisationPhone());
//            AppUtils.addNullOrLong(callStm, "pUSER_ID", confirmCertificateS.getUserId());
//            callStm.registerOutParameter("pCOUNT", Types.INTEGER);
//            log.info("QUERY: {}", callStm.toString());
//            callStm.execute();
//            return callStm.getInt(TableColumns.pCOUNT.name());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return 0;
//    }
//
//
//    public boolean checkConfirm(Date date) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_CHECK_LIST_CONFIRM", 2);
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            callStm.setTimestamp(TableColumns.pFROM_DT.name(), AppDate.getTimeStampForMysql(date));
//
//            callStm.registerOutParameter("pCHECKED_FLAG", Types.BIT);
//            log.info("QUERY: {}", callStm.toString());
//            callStm.execute();
//            return callStm.getBoolean("pCHECKED_FLAG");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return false;
//    }
//
//    public List<CloudCertificate> checkListConfirm(Date from, Date to) {
//        List<CloudCertificate> cloudCertificates = new ArrayList<>();
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_UNCONFIRM_LIST", 3);
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            callStm.setTimestamp(TableColumns.pFROM_DT.name(), AppDate.getTimeStampForMysql(from));
//            callStm.setTimestamp(TableColumns.pTO_DT.name(), AppDate.getTimeStampForMysql(to));
//            callStm.setBoolean(TableColumns.pLANGUAGE.name(), true);
//
//            log.info("QUERY: {}", callStm.toString());
//            ResultSet rs = callStm.executeQuery();
//            while (rs.next()) {
//                CloudCertificate item = new CloudCertificate();
//                item.setId(rs.getLong("id"));
//                item.setPersonalId(rs.getString("personal_id"));
//                item.setEnterpriseId(rs.getString("enterprise_id"));
//                item.setPersonalName(rs.getString("personal_name"));
//                item.setCompanyName(rs.getString("company_name"));
//                item.setCreatedDt(rs.getTimestamp("created_dt"));
//                item.setEffectiveDt(rs.getTimestamp("effective_dt"));
//                item.setExpirationDt(rs.getTimestamp("expiration_dt"));
//                item.setAuthorisationPhone(rs.getString("authorisation_phone").replaceFirst("^\\+?840?", "0"));
//                item.setAuthorisationEmail(rs.getString("authorisation_email"));
//                cloudCertificates.add(item);
//            }
//            return cloudCertificates;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return null;
//    }
//
//    public List<ConfirmCloudCertificate> listConfirm(ConfirmCertificateS confirmCertificateS) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_CONFIRM_LIST", 10);
//        List<ConfirmCloudCertificate> cloudCertificates = new ArrayList<>();
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            if (confirmCertificateS.getConfirmState() == -1) {
//                callStm.setString("pCONFIRM_STATE", null);
//            } else {
//                callStm.setInt("pCONFIRM_STATE", confirmCertificateS.getConfirmState());
//            }
//            AppUtils.addBlankOrString(callStm, "pIDENTITY", confirmCertificateS.getIdentity());
//            callStm.setString("pAGREEMENT_UUID", "".equals(confirmCertificateS.getAgreementUuid()) ? null : confirmCertificateS.getAgreementUuid());
//            callStm.setString("pPERSONAL_NAME", "".equals(confirmCertificateS.getPersonalName()) ? null : confirmCertificateS.getPersonalName());
//            callStm.setString("pAUTHORISATION_PHONE", "".equals(confirmCertificateS.getAuthorisationPhone()) ? null : confirmCertificateS.getAuthorisationPhone());
//            AppUtils.addNullOrLong(callStm, "pUSER_ID", confirmCertificateS.getUserId());
//            AppUtils.addLanguageOrderbyPaging(callStm, confirmCertificateS.isLanguage(), confirmCertificateS.getOrderBy(), confirmCertificateS.getStart(), confirmCertificateS.getSize());
//            log.info("SQL: {}", callStm.toString());
//            ResultSet rs = callStm.executeQuery();
//            while (rs.next()) {
//                ConfirmCloudCertificate item = new ConfirmCloudCertificate();
//
//                item.setId(rs.getLong("cloud_certificate_id"));
//                item.setPersonalId(rs.getString("personal_id"));
//                item.setPersonalIdRegex(RegexUtils.regexPersonalId(rs.getString("personal_id"), ".*", 2));
//                item.setEnterpriseId(rs.getString("enterprise_id"));
//                item.setAuthorisationPhone(rs.getString("authorisation_phone").replaceFirst("^\\+?840?", "0"));
//                item.setPersonalName(rs.getString("personal_name"));
//                item.setCompanyName(rs.getString("company_name"));
//                item.setCreatedDt(rs.getTimestamp("created_dt"));
//                item.setEffectiveDt(rs.getTimestamp("effective_dt"));
//                item.setExpirationDt(rs.getTimestamp("expiration_dt"));
//                item.setModifiedDt(rs.getTimestamp("modified_dt"));
//                item.setConfirmState(rs.getInt("confirm_state"));
//                item.setConfirmMessage(rs.getString("confirm_message"));
//                item.setKycCode(rs.getString("kyc_code"));
//                item.setKycMessage(rs.getString("kyc_Message"));
//                item.setRpCode(rs.getString("rp_code"));
//                item.setRpMessage(rs.getString("rp_message"));
//                item.setAutoRejectEnabled(rs.getBoolean("auto_reject_enabled"));
//                item.setAutoConfirmEnabled(rs.getBoolean("auto_confirm_enabled"));
//                item.setUserId(rs.getLong("user_id"));
//                item.setUsername(rs.getString("username"));
//                item.setModifiedByUsername(rs.getString("MODIFIED_BY_USERNAME"));
//                item.setUserModified(rs.getLong("modified_by"));
//                cloudCertificates.add(item);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return cloudCertificates;
//    }
//
//    public ConfirmCloudCertificate infoConfirm(long id) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_CONFIRM_DETAIL", 1);
//        ConfirmCloudCertificate item = new ConfirmCloudCertificate();
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            AppUtils.addNullOrLong(callStm, "pCLOUD_CERTIFICATE_ID", id);
//            log.info("SQL: {}", callStm.toString());
//            ResultSet rs = callStm.executeQuery();
//            if (rs.next()) {
//                item.setSubject(rs.getString("subject"));
//                item.setId(rs.getLong("cloud_certificate_id"));
//                item.setPersonalId(rs.getString("personal_id"));
//                item.setPersonalIdRegex(RegexUtils.regexPersonalId(rs.getString("personal_id"), ".*", 2));
//                item.setAuthorisationPhone(rs.getString("authorisation_phone").replaceFirst("^\\+?840?", "0"));
//                item.setPersonalName(rs.getString("personal_name"));
//                item.setCreatedDt(rs.getTimestamp("created_dt"));
//                item.setAuthorisationEmail(rs.getString("authorisation_email"));
////                item.setEffectiveDt(rs.getTimestamp("effective_dt"));
////                item.setExpirationDt(rs.getTimestamp("expiration_dt"));
//                item.setModifiedDt(rs.getTimestamp("modified_dt"));
//                item.setConfirmState(rs.getInt("confirm_state"));
//                item.setConfirmMessage(rs.getString("confirm_message"));
//                item.setKycCode(rs.getString("kyc_code"));
//                item.setKycMessage(rs.getString("kyc_Message"));
//                item.setRpCode(rs.getString("rp_code"));
//                item.setRpMessage(rs.getString("rp_message"));
//                item.setAutoRejectEnabled(rs.getBoolean("auto_reject_enabled"));
//                item.setAutoConfirmEnabled(rs.getBoolean("auto_confirm_enabled"));
//                item.setUserId(rs.getLong("user_id"));
//                item.setUserModified(rs.getLong("modified_by"));
//
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return item;
//    }
//
//    public String billCodeForPrepareFileForSignCloud(int pCLOUD_CERTIFICATE_ID) {
//        String sql = "{call SP_BO_CLOUD_CERTIFICATE_GET_RESPONSE_BILLCODE(?)}";
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            callStm.setInt("pCLOUD_CERTIFICATE_ID", pCLOUD_CERTIFICATE_ID);
//
//            log.info("QUERY: {}", callStm.toString());
//            ResultSet rs = callStm.executeQuery();
//            if (rs.next()) {
//                return rs.getString("response_billcode");
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return null;
//    }
//
//
////    public int count(CloudCertificateSearch p) {
////        String sql = "{call SP_BO_CLOUD_CERTIFICATE_TOTAL(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
////        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
////            callStm.setTimestamp(TableColumns.pFROM_DT.name(), AppDate.strDate2timestamp(p.getStartDate(), null));
////            callStm.setTimestamp(TableColumns.pTO_DT.name(), AppDate.strDate2timestamp(p.getEndDate(), null));
////            if (p.getCertificateState() == null) {
////                callStm.setString("pCERTIFICATE_STATE_ID", null);
////            } else {
////                callStm.setLong("pCERTIFICATE_STATE_ID", p.getCertificateState());
////            }
////            if (p.getCertificateProfile() == null) {
////                callStm.setString("pCERTIFICATE_PROFILE_ID", null);
////            } else {
////                callStm.setLong("pCERTIFICATE_PROFILE_ID", p.getCertificateProfile());
////            }
////            if (p.getName() == null) {
////                callStm.setString("pCOMMON_NAME", null);
////            } else {
////                callStm.setString("pCOMMON_NAME", p.getName());
////            }
////            if (p.getIndentity() == null) {
////                callStm.setString("pIDENTITY", null);
////            } else {
////                callStm.setString("pIDENTITY", p.getIndentity());
////            }
////            if (p.getCertificateAuthority() == null) {
////                callStm.setString("pCERTIFICATE_AUTHORITY_ID", null);
////            } else {
////                callStm.setLong("pCERTIFICATE_AUTHORITY_ID", p.getCertificateAuthority());
////            }
////            if (p.getCertificateType() == null) {
////                callStm.setString("pCERTIFICATE_TYPE_ID", null);
////            } else {
////                callStm.setLong("pCERTIFICATE_TYPE_ID", p.getCertificateType());
////            }
////            if (p.getServiceType() == null) {
////                callStm.setString("pSERVICE_TYPE_ID", null);
////            } else {
////                callStm.setLong("pSERVICE_TYPE_ID", p.getServiceType());
////            }
////            if (p.getSharedMode() == null) {
////                callStm.setString("pSHARED_MODE_ID", null);
////            } else {
////                callStm.setLong("pSHARED_MODE_ID", p.getSharedMode());
////            }
////            if (p.getEntity() == null) {
////                callStm.setString("pENTITY_ID", null);
////            } else {
////                callStm.setLong("pENTITY_ID", p.getEntity());
////            }
////            if (p.getRegistrationParty() == null) {
////                callStm.setString("pREGISTRATION_PARTY_ID", null);
////            } else {
////                callStm.setLong("pREGISTRATION_PARTY_ID", p.getRegistrationParty());
////            }
////            callStm.registerOutParameter(TableColumns.pCOUNT.name(), java.sql.Types.INTEGER);
////            log.info("QUERY: {}", callStm.toString());
////            callStm.execute();
////            return callStm.getInt(TableColumns.pCOUNT.name());
////        } catch (Exception ex) {
////            ex.printStackTrace();
////            log.error(ex.getMessage());
////        }
////        return 0;
////    }
//
//
//    public List<CloudCertificate> getAll(CloudCertificateS p) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_LIST", 28);
//        List<CloudCertificate> lst = new ArrayList<>();
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            AppUtils.addTimestamp(callStm, p.getStartDate(), p.getEndDate());
//            callStm.setString("pPERSONAL_NAME", "".equals(p.getPersonalName()) ? null : p.getPersonalName());
//            callStm.setString("pCOMPANY_NAME", "".equals(p.getCompanyName()) ? null : p.getCompanyName());
//            callStm.setString("pPERSONAL_ID", "".equals(p.getPersonalId()) ? null : p.getPersonalId());
//            callStm.setString("pENTERPRISE_ID", "".equals(p.getEnterpriseId()) ? null : p.getEnterpriseId());
//            AppUtils.addBlankOrString(callStm, "pMSISDN", p.getMsisdn());
//            AppUtils.addBlankOrString(callStm, "pICCID", p.getIccid());
//            AppUtils.addBlankOrString(callStm, "pCERTIFICATE_UUID", p.getUuid());
//            AppUtils.addBlankOrString(callStm, "pCERTIFICATE_THUMBPRINT", p.getThumbprint());
//            AppUtils.addNullOrLong(callStm, "pUSER_ID ", p.getUserId());
//            if (p.getCertificateState() == null) {
//                callStm.setString("pCERTIFICATE_STATE_ID", null);
//            } else {
//                callStm.setLong("pCERTIFICATE_STATE_ID", p.getCertificateState());
//            }
//            if (p.getCertificateProfile() == null) {
//                callStm.setString("pCERTIFICATE_PROFILE_ID", null);
//            } else {
//                callStm.setLong("pCERTIFICATE_PROFILE_ID", p.getCertificateProfile());
//            }
//            if (p.getCertificateAuthority() == null) {
//                callStm.setString("pCERTIFICATE_AUTHORITY_ID", null);
//            } else {
//                callStm.setLong("pCERTIFICATE_AUTHORITY_ID", p.getCertificateAuthority());
//            }
//            if (p.getCertificateType() == null) {
//                callStm.setString("pCERTIFICATE_TYPE_ID", null);
//            } else {
//                callStm.setLong("pCERTIFICATE_TYPE_ID", p.getCertificateType());
//            }
//            if (p.getServiceType() == null) {
//                callStm.setString("pSERVICE_TYPE_ID", null);
//            } else {
//                callStm.setLong("pSERVICE_TYPE_ID", p.getServiceType());
//            }
//            if (p.getSharedMode() == null) {
//                callStm.setString("pSHARED_MODE_ID", null);
//            } else {
//                callStm.setLong("pSHARED_MODE_ID", p.getSharedMode());
//            }
//            if (p.getEntity() == null) {
//                callStm.setString("pENTITY_ID", null);
//            } else {
//                callStm.setLong("pENTITY_ID", p.getEntity());
//            }
//            if (p.getRegistrationParty() == null) {
//                callStm.setString("pREGISTRATION_PARTY_ID", null);
//            } else {
//                callStm.setLong("pREGISTRATION_PARTY_ID", p.getRegistrationParty());
//            }
//            AppUtils.addNullOrLong(callStm, "pRELYING_PARTY_ID ", p.getRelyingPartyId());
//            if ("".equalsIgnoreCase(p.getOwnerName())) {
//                callStm.setString("pUSERNAME", null);
//            } else {
//                callStm.setString("pUSERNAME", p.getOwnerName());
//            }
//            if (p.getCreatedBy() == null) {
//                callStm.setString("pCREATED_BY", null);
//            } else {
//                callStm.setLong("pCREATED_BY", p.getCreatedBy());
//            }
//            callStm.setString("pCERTIFICATE_SN", "".equals(p.getCertificateSn()) ? null : p.getCertificateSn());
//            callStm.setString("pDAY", "".equals(p.getFilterWarning()) ? null : p.getFilterWarning());
//
//            AppUtils.addLanguageOrderbyPaging(callStm, p.isLanguage(), p.getOrderBy(), p.getStart(), p.getSize());
//            log.info("SQL: {}", callStm.toString());
//            ResultSet rs = callStm.executeQuery();
//            while (rs.next()) {
//                CloudCertificate item = new CloudCertificate();
//                item.setCertificateUuid(rs.getString("certificate_uuid"));
//                item.setId(rs.getLong("id"));
//                item.setPersonalId(rs.getString("personal_id"));
//                item.setEnterpriseId(rs.getString("enterprise_id"));
//                item.setPersonalName(rs.getString("personal_name"));
//                item.setCompanyName(rs.getString("company_name"));
//                item.setCreatedDt(rs.getTimestamp("created_dt"));
//                item.setEffectiveDt(rs.getTimestamp("effective_dt"));
//                item.setExpirationDt(rs.getTimestamp("expiration_dt"));
//                item.setCertificateSn(rs.getString("certificate_sn"));
//                item.setAuthorisationEmail(rs.getString("authorisation_email"));
//                item.setAuthorisationPhone(rs.getString("authorisation_phone"));
//                item.setMsisdn(rs.getString("msisdn"));
//                item.setIccid(rs.getString("iccid"));
//                
////                CloudAgreement cloudAgreement = new CloudAgreement();
////                cloudAgreement.setAgreementUuid(rs.getTenTrongHo)
//
//                CertificateProfile cerProfile = new CertificateProfile();
//                cerProfile.setId(rs.getLong("certificate_profile_id"));
//                cerProfile.setName(rs.getString("certificate_profile_name"));
//                cerProfile.setDesc(rs.getString("certificate_profile_remark"));
//                item.setCertificateProfile(cerProfile);
//
//                CertificateState cerState = new CertificateState();
//                cerState.setName(rs.getString("certificate_state_name"));
//                cerState.setDesc(rs.getString("certificate_state_remark"));
//                item.setCertificateState(cerState);
//
//                CertificateType cerType = new CertificateType();
//                cerType.setId(rs.getLong("certificate_type_id"));
//                cerType.setName(rs.getString("certificate_type_name"));
//                cerType.setDesc(rs.getString("certificate_type_remark"));
//                item.setCertificateType(cerType);
//
//                CloudCertificateOwner cloudCertificateOwner = new CloudCertificateOwner();
//                cloudCertificateOwner.setUsername(rs.getString("username"));
//                item.setCloudCertificateOwner(cloudCertificateOwner);
//
//                CertificateAuthority certificateAuthority = new CertificateAuthority();
//                certificateAuthority.setId(rs.getLong("certificate_authority_id"));
//                item.setCertificateAuthority(certificateAuthority);
//
//                AuthMode authMode = new AuthMode();
//                authMode.setId(rs.getLong("auth_mode_id"));
//                authMode.setName(rs.getString("auth_mode_name"));
//                item.setAuthMode(authMode);
//
//                SharedMode sharedMode = new SharedMode();
//                sharedMode.setId(rs.getLong("shared_mode_id"));
//                item.setSharedMode(sharedMode);
//
//                lst.add(item);
//            }
//        } catch (SQLException ex) {
//            log.error(ex.getMessage());
//        }
//        return lst;
//    }
//    
//    public List<CloudAgreement> getCloudAgreement(Long id, boolean language) {
//        List<CloudAgreement> cloudAgreements = new ArrayList<>();
////    public String certificateGetAgreementUUID (Object object) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_GET_AGREEMENT_UUID",2);
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            callStm.setLong("pCLOUD_CERTIFICATE_ID", id);
//            callStm.setBoolean("pLANGUAGE", language);
//            
//            log.info("QUERY: {}", callStm.toString());
//            ResultSet rs = callStm.executeQuery();
//            while (rs.next()) {
//                CloudAgreement item = new CloudAgreement();
//                item.setAgreementUuid(rs.getString("agreement_uuid"));
//                RelyingParty rsRP = new RelyingParty();
//                rsRP.setName(rs.getString("RELYING_PARTY_NAME"));
//                item.setRelyingParty(rsRP);
//                cloudAgreements.add(item);
//            } return cloudAgreements;
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        } return null;
//    }
//
//    public int total(CloudCertificateS p) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_TOTAL", 25);
//        List<CloudCertificate> lst = new ArrayList<>();
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            AppUtils.addTimestamp(callStm, p.getStartDate(), p.getEndDate());
//
//            AppUtils.addNullOrLong(callStm, "pUSER_ID ", p.getUserId());
//            callStm.setString("pPERSONAL_NAME", "".equals(p.getPersonalName()) ? null : p.getPersonalName());
//            callStm.setString("pCOMPANY_NAME", "".equals(p.getCompanyName()) ? null : p.getCompanyName());
//            callStm.setString("pPERSONAL_ID", "".equals(p.getPersonalId()) ? null : p.getPersonalId());
//            callStm.setString("pENTERPRISE_ID", "".equals(p.getEnterpriseId()) ? null : p.getEnterpriseId());
//            AppUtils.addBlankOrString(callStm, "pMSISDN", p.getMsisdn());
//            AppUtils.addBlankOrString(callStm, "pICCID", p.getIccid());
//            AppUtils.addBlankOrString(callStm, "pCERTIFICATE_UUID", p.getUuid());
//            AppUtils.addBlankOrString(callStm, "pCERTIFICATE_THUMBPRINT", p.getThumbprint());
//            if (p.getCertificateState() == null) {
//                callStm.setString("pCERTIFICATE_STATE_ID", null);
//            } else {
//                callStm.setLong("pCERTIFICATE_STATE_ID", p.getCertificateState());
//            }
//            if (p.getCertificateProfile() == null) {
//                callStm.setString("pCERTIFICATE_PROFILE_ID", null);
//            } else {
//                callStm.setLong("pCERTIFICATE_PROFILE_ID", p.getCertificateProfile());
//            }
//            if (p.getCertificateAuthority() == null) {
//                callStm.setString("pCERTIFICATE_AUTHORITY_ID", null);
//            } else {
//                callStm.setLong("pCERTIFICATE_AUTHORITY_ID", p.getCertificateAuthority());
//            }
//            if (p.getCertificateType() == null) {
//                callStm.setString("pCERTIFICATE_TYPE_ID", null);
//            } else {
//                callStm.setLong("pCERTIFICATE_TYPE_ID", p.getCertificateType());
//            }
//            if (p.getServiceType() == null) {
//                callStm.setString("pSERVICE_TYPE_ID", null);
//            } else {
//                callStm.setLong("pSERVICE_TYPE_ID", p.getServiceType());
//            }
//            if (p.getSharedMode() == null) {
//                callStm.setString("pSHARED_MODE_ID", null);
//            } else {
//                callStm.setLong("pSHARED_MODE_ID", p.getSharedMode());
//            }
//            if (p.getEntity() == null) {
//                callStm.setString("pENTITY_ID", null);
//            } else {
//                callStm.setLong("pENTITY_ID", p.getEntity());
//            }
//            if (p.getRegistrationParty() == null) {
//                callStm.setString("pREGISTRATION_PARTY_ID", null);
//            } else {
//                callStm.setLong("pREGISTRATION_PARTY_ID", p.getRegistrationParty());
//            }
//            AppUtils.addNullOrLong(callStm, "pRELYING_PARTY_ID  ", p.getRelyingPartyId());
//            if (p.getCreatedBy() == null) {
//                callStm.setString("pCREATED_BY", null);
//            } else {
//                callStm.setLong("pCREATED_BY", p.getCreatedBy());
//            }
//            if ("".equalsIgnoreCase(p.getOwnerName())) {
//                callStm.setString("pUSERNAME", null);
//            } else {
//                callStm.setString("pUSERNAME", p.getOwnerName());
//            }
//            callStm.setString("pCERTIFICATE_SN", "".equals(p.getCertificateSn()) ? null : p.getCertificateSn());
//            callStm.setString("pDAY", "".equals(p.getFilterWarning()) ? null : p.getFilterWarning());
//
//            callStm.registerOutParameter(TableColumns.pCOUNT.name(), java.sql.Types.INTEGER);
//            log.info("QUERY: {}", callStm.toString());
//            callStm.execute();
//            return callStm.getInt(TableColumns.pCOUNT.name());
//        } catch (SQLException ex) {
//            log.error(ex.getMessage());
//        }
//        return 0;
//    }
//
////
////    @Override
////    public String save(CloudCertificate t) {
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
////    }
////
////    @Override
////    public String update(CloudCertificate t) {
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
////    }
////
////    @Override
////    public String delete(CloudCertificate t) {
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
////    }
//
//    public String confirmCertificate(CertificateConfirm certificateConfirm) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_CONFIRM", 5);
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            callStm.setInt("pCLOUD_CERTIFICATE_ID", certificateConfirm.getId());
//            callStm.setString("pCONFIRM_STATE", certificateConfirm.getCode());
//            callStm.setString("pCONFIRM_MESSAGE", certificateConfirm.getMessage());
//            callStm.setBoolean("pAUTO_CONFIRM_ENABLED", certificateConfirm.isAutoConfirm());
//            callStm.setLong("pMODIFIED_BY", certificateConfirm.getModifiedBy());
//
//            log.info("QUERY: {}", callStm.toString());
//            callStm.execute();
//            return "0";
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return "-1";
//    }
//
//    public String confirmCertificateKyc(CertificateConfirm certificateConfirm) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_CONFIRM_UPDATE_KYC_MESSAGE", 4);
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            callStm.setInt("pCLOUD_CERTIFICATE_ID", certificateConfirm.getId());
//            callStm.setString("pKYC_CODE", certificateConfirm.getCode());
//            callStm.setString("pKYC_MESSAGE", certificateConfirm.getMessage());
//            callStm.setLong("pMODIFIED_BY", certificateConfirm.getModifiedBy());
//
//            log.info("QUERY: {}", callStm.toString());
//            callStm.execute();
//            return "0";
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return "-1";
//    }
//
//    public String confirmCertificateRp(CertificateConfirm certificateConfirm, VPB_ApiResponse vpb_apiResponse) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_CONFIRM_UPDATE_RP_MESSAGE", 4);
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            callStm.setInt("pCLOUD_CERTIFICATE_ID", certificateConfirm.getId());
//            callStm.setString("pRP_CODE", vpb_apiResponse.getCode());
//            callStm.setString("pRP_MESSAGE", vpb_apiResponse.getMsg());
//            callStm.setLong("pMODIFIED_BY", certificateConfirm.getModifiedBy());
//
//            log.info("QUERY: {}", callStm.toString());
//            callStm.execute();
//            return "0";
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return "-1";
//    }
//
//    public String updateStatusSendMail(String listId, long id) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_CONFIRM_UPDATE_SEND_EMAIL_ENABLED", 2);
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            callStm.setString("pCLOUD_CERTIFICATE_ID_LIST", listId);
//            callStm.setLong("pMODIFIED_BY", id);
//            log.info("QUERY: {}", callStm.toString());
//            callStm.execute();
//            return "0";
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return "-1";
//    }
//
//    public boolean updateAuthMode(CloudCertificate cloudCertificate) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_UPDATE_AUTH_MODE", 3);
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            callStm.setLong("pCLOUD_CERTIFICATE_ID", cloudCertificate.getId());
//            callStm.setLong("pAUTH_MODE_ID", cloudCertificate.getAuthMode().getId());
//            callStm.setLong("pMODIFIED_BY", cloudCertificate.getUserByModifiedBy().getId());
//
//            log.info("QUERY: {}", callStm.toString());
//            callStm.execute();
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return false;
//    }
//
//    public boolean buySigningCounter(CloudCertificate cloudCertificate) {
//        String sql = AppUtils.addParamStore("SP_BO_CERTIFICATE_BUY_SIGNING_COUNTER_LOG_INSERT", 5);
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            callStm.setLong("pCLOUD_CERTIFICATE_ID", cloudCertificate.getId());
//            callStm.setLong("pCERTIFICATE_SIGNING_PROFILE_ID", cloudCertificate.getCertificateSigningProfile().getId());
//            callStm.setLong("pSIGNING_COUNTER", cloudCertificate.getCertificateSigningProfile().getSigningCounter());
//            callStm.setLong("pAMOUNT", cloudCertificate.getCertificateSigningProfile().getAmount());
//            callStm.setLong("pCREATED_BY", cloudCertificate.getUserByCreatedBy().getId());
//
//            log.info("QUERY: {}", callStm.toString());
//            callStm.execute();
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return false;
//    }
//
//    public boolean resetPassphase(CloudCertificate cloudCertificate) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_RESET_PASSCODE", 3);
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            callStm.setLong("pCLOUD_CERTIFICATE_ID", cloudCertificate.getId());
//            callStm.setString("pPASSCODE", cloudCertificate.getPassCode());
//            callStm.setLong("pMODIFIED_BY", cloudCertificate.getUserByModifiedBy().getId());
//
//            log.info("QUERY: {}", callStm.toString());
//            callStm.execute();
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return false;
//    }
//
//    public boolean updateAuthorization(CloudCertificate cloudCertificate) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_UPDATE_AUTHORISATION", 4);
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            callStm.setLong("pCLOUD_CERTIFICATE_ID", cloudCertificate.getId());
//            callStm.setString("pAUTHORISATION_PHONE", cloudCertificate.getAuthorisationPhone());
//            callStm.setString("pAUTHORISATION_EMAIL", cloudCertificate.getAuthorisationEmail());
//            callStm.setLong("pMODIFIED_BY", cloudCertificate.getUserByModifiedBy().getId());
//
//            log.info("QUERY: {}", callStm.toString());
//            callStm.execute();
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return false;
//    }
//
//    public boolean lockCertificate(CloudCertificate cloudCertificate) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_BLOCK", 2);
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            callStm.setLong("pCLOUD_CERTIFICATE_ID", cloudCertificate.getId());
//            callStm.setLong("pMODIFIED_BY", cloudCertificate.getUserByModifiedBy().getId());
//
//            log.info("QUERY: {}", callStm.toString());
//            callStm.execute();
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return false;
//    }
//
//    public boolean unlockCertificate(CloudCertificate cloudCertificate) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_UNBLOCK", 2);
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            callStm.setLong("pCLOUD_CERTIFICATE_ID", cloudCertificate.getId());
//            callStm.setLong("pMODIFIED_BY", cloudCertificate.getUserByModifiedBy().getId());
//
//            log.info("QUERY: {}", callStm.toString());
//            callStm.execute();
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return false;
//    }
//
//
//    public List<CloudCertificate> getAllByIds(String ids) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_GET_CSR_BY_CLOUD_CERTIFICATE_ID_LIST", 1);
//        List<CloudCertificate> lst = new ArrayList<>();
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//
//            callStm.setString("pCLOUD_CERTIFICATE_ID_LIST", ids);
//
//            log.info("SQL: {}", callStm.toString());
//            ResultSet rs = callStm.executeQuery();
//            while (rs.next()) {
//                CloudCertificate item = new CloudCertificate();
//                item.setId(rs.getLong("id"));
//                item.setPersonalId(rs.getString("personal_id"));
//                item.setEnterpriseId(rs.getString("enterprise_id"));
//                item.setPersonalName(rs.getString("personal_name"));
//                item.setCompanyName(rs.getString("company_name"));
//                item.setCreatedDt(rs.getTimestamp("created_dt"));
//                item.setSubject(rs.getString("subject"));
//                item.setCsr(rs.getString("csr"));
////                item.setEffectiveDt(rs.getTimestamp("effective_dt"));
////                item.setExpirationDt(rs.getTimestamp("expiration_dt"));
////                item.setCertificateSn(rs.getString("certificate_sn"));
//                item.setAuthorisationEmail(rs.getString("authorisation_email"));
//                item.setAuthorisationPhone(rs.getString("authorisation_phone"));
//
//                CertificateProfile cerProfile = new CertificateProfile();
//                cerProfile.setName(rs.getString("certificate_profile_name"));
////                cerProfile.setDesc(rs.getString("certificate_profile_remark"));
//                item.setCertificateProfile(cerProfile);
//
//                CloudAgreement cloudAgreement = new CloudAgreement();
//                cloudAgreement.setAgreementUuid(rs.getString("agreement_uuid"));
//                item.setCloudAgreement(cloudAgreement);
//
//                CertificateAuthority certificateAuthority = new CertificateAuthority();
//                certificateAuthority.setName(rs.getString("certificate_authority_name"));
//                item.setCertificateAuthority(certificateAuthority);
//
////                CertificateState cerState = new CertificateState();
////                cerState.setName(rs.getString("certificate_state_name"));
////                cerState.setDesc(rs.getString("certificate_state_remark"));
////                item.setCertificateState(cerState);
//
//                CertificateType cerType = new CertificateType();
//                cerType.setName(rs.getString("certificate_type_name"));
////                cerType.setDesc(rs.getString("certificate_type_remark"));
//                item.setCertificateType(cerType);
//
//                CloudCertificateOwner cloudCertificateOwner = new CloudCertificateOwner();
//                cloudCertificateOwner.setUsername(rs.getString("cloud_certificate_owner_username"));
//                item.setCloudCertificateOwner(cloudCertificateOwner);
//
//                RelyingParty relyingParty = new RelyingParty();
//                relyingParty.setName(rs.getString("relying_party_name"));
//                item.setRelyingParty(relyingParty);
//
//                lst.add(item);
//            }
//        } catch (SQLException ex) {
//            log.error(ex.getMessage());
//        }
//        return lst;
//    }
//
//    public ChartCount getCertificatesTransaction(String ptext) {
//
//        ChartCount chartCount = new ChartCount();
//        String sql = AppUtils.addParamStore("SP_BO_REMOTE_SIGNING_CERTIFICATES ", 3);
//
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//
//            callStm.setString("pTEXT", ptext);
//            callStm.registerOutParameter("pTOTAL_ISSUED", Types.INTEGER);
//            callStm.registerOutParameter("pTOTAL_REVOKED", Types.INTEGER);
////            log.info("Query: " + callStm.toString());
//            callStm.execute();
//
//            chartCount.setSuccess(callStm.getInt("pTOTAL_ISSUED"));
//            chartCount.setFailure(callStm.getInt("pTOTAL_REVOKED"));
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.info(ex.toString());
//        }
//
//        return chartCount;
//    }
//
//    public List<CloudCertificate> dashboardCertificateList(CloudCertificateS p) {
//
//        String sql = AppUtils.addParamStore("SP_BO_REMOTE_SIGNING_CERTIFICATES_LIST ", 7);
//        List<CloudCertificate> lst = new ArrayList<>();
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//
//            AppUtils.addTimestamp(callStm, p.getStartDate(), p.getEndDate());
//            callStm.setInt("pSTATUS", p.getPStatus());
//            AppUtils.addLanguageOrderbyPaging(callStm, p.isLanguage(), p.getOrderBy(), p.getStart(), p.getSize());
//            log.info("Query: " + callStm.toString());
//            ResultSet rs = callStm.executeQuery();
//
//            while (rs.next()) {
//                CloudCertificate item = new CloudCertificate();
//                item.setId(rs.getLong("id"));
//                item.setPersonalId(rs.getString("personal_id"));
//                item.setEnterpriseId(rs.getString("enterprise_id"));
//                item.setPersonalName(rs.getString("personal_name"));
//                item.setCompanyName(rs.getString("company_name"));
//                item.setCreatedDt(rs.getTimestamp("created_dt"));
//                item.setEffectiveDt(rs.getTimestamp("effective_dt"));
//                item.setExpirationDt(rs.getTimestamp("expiration_dt"));
//                item.setCertificateSn(rs.getString("certificate_sn"));
//                item.setAuthorisationEmail(rs.getString("authorisation_email"));
//                item.setAuthorisationPhone(rs.getString("authorisation_phone"));
//
//                CertificateProfile cerProfile = new CertificateProfile();
//                cerProfile.setName(rs.getString("certificate_profile_name"));
//                cerProfile.setDesc(rs.getString("certificate_profile_remark"));
//                item.setCertificateProfile(cerProfile);
//
//                CertificateState cerState = new CertificateState();
//                cerState.setName(rs.getString("certificate_state_name"));
//                cerState.setDesc(rs.getString("certificate_state_remark"));
//                item.setCertificateState(cerState);
//
//                CertificateType cerType = new CertificateType();
//                cerType.setName(rs.getString("certificate_type_name"));
//                cerType.setDesc(rs.getString("certificate_type_remark"));
//                item.setCertificateType(cerType);
//
//                CloudCertificateOwner cloudCertificateOwner = new CloudCertificateOwner();
//                cloudCertificateOwner.setUsername(rs.getString("username"));
//                item.setCloudCertificateOwner(cloudCertificateOwner);
//
//                lst.add(item);
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.info(ex.toString());
//        }
//
//        return lst;
//    }
//
//    public int countDashboardCertificateList(CloudCertificateS p) {
//
//        String sql = AppUtils.addParamStore("SP_BO_REMOTE_SIGNING_CERTIFICATES_TOTAL ", 4);
//
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//
//            AppUtils.addTimestamp(callStm, p.getStartDate(), p.getEndDate());
//            callStm.setInt("pSTATUS", p.getPStatus());
//            callStm.registerOutParameter("pCOUNT", Types.INTEGER);
//
//            log.info("Query: " + callStm.toString());
//
//            callStm.execute();
//            return callStm.getInt("pCOUNT");
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.info(ex.toString());
//        }
//        return 0;
//    }
//
//    public boolean updateMultiSigning(CloudCertificate p) {
//
//        String sql = AppUtils.addParamStore("SP_FO_CLOUD_CERTIFICATE_UPDATE_MULTI_SIGNATURE", 3);
//
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//
//            callStm.setLong("pCLOUD_CERTIFICATE_ID", p.getId());
//            callStm.setInt("pMULTI_SIGNATURE", p.getMultiSignature());
//            callStm.setLong("pMODIFIED_BY", p.getUserByModifiedBy().getId());
//
//            log.info("Query: " + callStm.toString());
//
//            callStm.execute();
//            return true;
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.info(ex.toString());
//        }
//        return false;
//    }
//
//    public boolean updateScal(CloudCertificate p) {
//
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_UPDATE_SCAL", 3);
//
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//
//            callStm.setLong("pCLOUD_CERTIFICATE_ID", p.getId());
//            callStm.setInt("pSCAL", p.getScal());
//            callStm.setLong("pMODIFIED_BY", p.getUserByModifiedBy().getId());
//
//            log.info("Query: " + callStm.toString());
//
//            callStm.execute();
//            return true;
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.info(ex.toString());
//        }
//        return false;
//    }
//
//    public boolean updateSharedMode(CloudCertificate p) {
//
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_UPDATE_SHARED_MODE", 3);
//
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//
//            callStm.setLong("pCLOUD_CERTIFICATE_ID", p.getId());
//            callStm.setLong("pSHARED_MODE_ID", p.getSharedMode().getId());
//            callStm.setLong("pMODIFIED_BY", p.getUserByModifiedBy().getId());
//
//            log.info("Query: " + callStm.toString());
//
//            callStm.execute();
//            return true;
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.info(ex.toString());
//        }
//        return false;
//    }
//
//    public List<CloudCertificate> listBySimcardId(Long userId, long id, boolean language) {
//        String sql = AppUtils.addParamStore("SP_BO_CLOUD_CERTIFICATE_LIST_BY_SIM_CARD", 3);
//        List<CloudCertificate> lst = new ArrayList<>();
//        try (Connection connection = dataSource.getConnection(); CallableStatement callStm = connection.prepareCall(sql)) {
//            AppUtils.addNullOrLong(callStm, "pSIM_CARD_MANAGER_ID", id);
//            AppUtils.addNullOrLong(callStm, "pUSER_ID ", userId);
//            callStm.setBoolean(TableColumns.pLANGUAGE.name(), language);
//            log.info("QUERY: {}", callStm.toString());
//            ResultSet rs = callStm.executeQuery();
//            while (rs.next()) {
//                CloudCertificate item = new CloudCertificate();
//                item.setId(rs.getLong("id"));
//                item.setPersonalId(rs.getString("personal_id"));
//                item.setEnterpriseId(rs.getString("enterprise_id"));
//                item.setPersonalName(rs.getString("personal_name"));
////                item.setTaxId(rs.getString("tax_id"));
////                item.setBudgetId(rs.getString("budget_id"));
//                item.setCompanyName(rs.getString("company_name"));
//                item.setCreatedDt(rs.getTimestamp("created_dt"));
//                item.setEffectiveDt(rs.getTimestamp("effective_dt"));
//                item.setExpirationDt(rs.getTimestamp("expiration_dt"));
//
//                CertificateProfile cerProfile = new CertificateProfile();
//                cerProfile.setName(rs.getString("certificate_profile_name"));
//                cerProfile.setDesc(rs.getString("certificate_profile_remark"));
//                item.setCertificateProfile(cerProfile);
//
//                CertificateState cerState = new CertificateState();
//                cerState.setName(rs.getString("certificate_state_name"));
//                cerState.setDesc(rs.getString("certificate_state_remark"));
//                item.setCertificateState(cerState);
//
//                CertificateType cerType = new CertificateType();
//                cerType.setName(rs.getString("certificate_type_name"));
//                cerType.setDesc(rs.getString("certificate_type_remark"));
//                item.setCertificateType(cerType);
//
//                AuthMode authMode = new AuthMode();
//                authMode.setId(rs.getLong("auth_mode_id"));
//                item.setAuthMode(authMode);
//
//                lst.add(item);
//            }
//        } catch (SQLException ex) {
//            log.error(ex.getMessage());
//        }
//        return lst;
//    }
//
//}
