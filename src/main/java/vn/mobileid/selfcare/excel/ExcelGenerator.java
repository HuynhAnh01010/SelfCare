///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package vn.mobileid.selfcare.excel;
//
//import com.sun.rowset.internal.Row;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import javafx.scene.control.Cell;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.CreationHelper;
//import org.apache.poi.ss.usermodel.DateUtil;
//import org.apache.poi.ss.usermodel.IndexedColors;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.util.CellRangeAddressList;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFDataValidation;
//import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
//import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
//import org.apache.poi.xssf.usermodel.XSSFFont;
//import org.apache.poi.xssf.usermodel.XSSFName;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import vn.mobileid.selfcare.service.dto.Province;
//import vn.mobileid.selfcare.service.dto.csr.model.AuthMode;
//import vn.mobileid.selfcare.service.dto.csr.model.CloudCertificate;
//import vn.mobileid.selfcare.service.dto.csr.model.CertificateProfile;
//import vn.mobileid.selfcare.service.dto.csr.model.ReportCertificateSearch;
//import vn.mobileid.selfcare.service.dto.csr.model.SharedMode;
//import vn.mobileid.selfcare.service.util.AppConstants;
//
//import vn.mobileid.selfcare.service.dto.csr.model.*;
//
///**
// *
// * @author Mobile ID 21
// */
//@Slf4j
//public class ExcelGenerator {
//
//    public static String DDMMYYYY_HHMMSS = "dd-MM-yyyy HH:mm:ss";
//
//    public static byte[] reportCertificateCustomer(List<CloudCertificate> cloudCertificates, ReportCertificateSearch reportCertificateSearch) throws IOException {
//        String[] COLUMNs = {"INDEX", "NAME", "IDENTITY", "CERTIFICATE AUTHORITY", "TYPE", "SN", "EFFECTIVE DT", "EXPIRATION DT",
//            "CREATED DT", "AUTH MODE", "STATE"};
//        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
////            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
////            wb.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
////
////            XSSFSheet sheet = wb.getSheetAt(0);
//            CreationHelper createHelper = workbook.getCreationHelper();
//            XSSFSheet sheet = workbook.createSheet("REPORT");
//
//            XSSFFont headerFont = workbook.createFont();
//            headerFont.setBold(true);
//            headerFont.setColor(IndexedColors.BLUE.getIndex());
//
//            CellStyle headerCellStyle = workbook.createCellStyle();
//            headerCellStyle.setFont(headerFont);
//
//            // Row for Header
//            XSSFRow headerRow = sheet.createRow(0);
//
//            // Header
//            for (int col = 0; col < COLUMNs.length; col++) {
//                XSSFCell cell = headerRow.createCell(col);
//                cell.setCellValue(COLUMNs[col]);
//                cell.setCellStyle(headerCellStyle);
//            }
//            // CellStyle for Age
//            CellStyle dateTimeCellStyle = workbook.createCellStyle();
//            dateTimeCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(DDMMYYYY_HHMMSS));
////            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
//
//            int rowCount = 1;
//            int i = 1;
//            for (CloudCertificate item : cloudCertificates) {
//
//                XSSFRow userRow = sheet.createRow(rowCount++);
//                int j = 0;
//                userRow.createCell(j++).setCellValue(i++);
//                switch (item.getCertificateType().getName()) {
//                    case AppConstants.CERTIFICATE_TYPE_PERSONAL:
//                        userRow.createCell(j++).setCellValue(item.getPersonalName());
//                        userRow.createCell(j++).setCellValue(item.getPersonalId());
//                        break;
//                    case AppConstants.CERTIFICATE_TYPE_ENTERPRISE:
//                        userRow.createCell(j++).setCellValue(item.getCompanyName());
//                        userRow.createCell(j++).setCellValue(item.getEnterpriseId());
//                        break;
//                    default:
//                        userRow.createCell(j++).setCellValue(item.getPersonalName() + "/" + item.getCompanyName());
//                        userRow.createCell(j++).setCellValue(item.getPersonalId() + "/" + item.getEnterpriseId());
//                }
////                {"Index", "Name", "Identity", "Certificate Authority", "Type", "SN", "Effective Dt", "Expiration Dt",
////                        "Created Dt", "Auth Mode", "State"};
//                userRow.createCell(j++).setCellValue(item.getCertificateAuthority().getName());
//                userRow.createCell(j++).setCellValue(item.getCertificateType().getName());
//                userRow.createCell(j++).setCellValue(item.getCertificateSn());
//
//                XSSFCell cell = userRow.createCell(j++);
//                cell.setCellValue(item.getEffectiveDt());
//                cell.setCellStyle(dateTimeCellStyle);
//
//                XSSFCell cell2 = userRow.createCell(j++);
//                cell2.setCellValue(item.getExpirationDt());
//                cell2.setCellStyle(dateTimeCellStyle);
//
//                XSSFCell cell3 = userRow.createCell(j++);
//                cell3.setCellValue(item.getCreatedDt());
//                cell3.setCellStyle(dateTimeCellStyle);
//
//                userRow.createCell(j++).setCellValue(item.getAuthMode().getName());
//                userRow.createCell(j++).setCellValue(item.getCertificateState().getName());
//            }
//
//            workbook.write(out);
//            return out.toByteArray();
//        }
//    }
//
//    public static byte[] reportTransactions(List<CloudLog> cloudLogs, AgreementReportSigning agreementReportSigning) throws IOException {
//        String[] COLUMNs = {"INDEX", "AGREEMENT UUID", "CLOUD FUNCTION", "RESPONSE CODE", "CREATED DATE"};
//        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//            CreationHelper createHelper = workbook.getCreationHelper();
//            XSSFSheet sheet = workbook.createSheet("Report");
//
//            XSSFFont headerFont = workbook.createFont();
//            headerFont.setBold(true);
//            headerFont.setColor(IndexedColors.BLUE.getIndex());
//
//            CellStyle headerCellStyle = workbook.createCellStyle();
//            headerCellStyle.setFont(headerFont);
//
//            // Row for Header
//            XSSFRow headerRow = sheet.createRow(0);
//
//            // Header
//            for (int col = 0; col < COLUMNs.length; col++) {
//                XSSFCell cell = headerRow.createCell(col);
//                cell.setCellValue(COLUMNs[col]);
//                cell.setCellStyle(headerCellStyle);
//            }
//            // CellStyle for Age
//            CellStyle dateTimeCellStyle = workbook.createCellStyle();
//            dateTimeCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(DDMMYYYY_HHMMSS));
////            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
//
//            int rowCount = 1;
//            int i = 1;
//            for (CloudLog item : cloudLogs) {
//
//                XSSFRow userRow = sheet.createRow(rowCount++);
//                int j = 0;
//                userRow.createCell(j++).setCellValue(i++);
//                userRow.createCell(j++).setCellValue(item.getAgreementUuid());
//                userRow.createCell(j++).setCellValue(item.getCloudFunction().getName());
//                userRow.createCell(j++).setCellValue(item.getResponseCode().getName() + " - " + item.getResponseCode().getDesc());
//                XSSFCell cell2 = userRow.createCell(j++);
//                cell2.setCellValue(item.getCreatedDt());
//                cell2.setCellStyle(dateTimeCellStyle);
//
//            }
//
//            workbook.write(out);
//            return out.toByteArray();
//        }
//    }
//
//    public static byte[] reportCertificate(List<CloudCertificate> cloudCertificates, ReportCertificateSearch reportCertificateSearch) throws IOException {
//        String[] COLUMNs = {"INDEX", "NAME", "IDENTITY", "CERTIFICATE AUTHORITY", "TYPE", "SN", "EFFECTIVE DT", "EXPIRATION DT"};
//        try (Workbook workbook = new HSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//            CreationHelper createHelper = workbook.getCreationHelper();
//            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Report");
//
//            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
//            headerFont.setBold(true);
//            headerFont.setColor(IndexedColors.BLUE.getIndex());
//
//            CellStyle headerCellStyle = workbook.createCellStyle();
//            headerCellStyle.setFont(headerFont);
//
//            // Row for Header
//            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
//
//            // Header
//            for (int col = 0; col < COLUMNs.length; col++) {
//                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(col);
//                cell.setCellValue(COLUMNs[col]);
//                cell.setCellStyle(headerCellStyle);
//            }
//            // CellStyle for Age
//            CellStyle dateTimeCellStyle = workbook.createCellStyle();
//            dateTimeCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(DDMMYYYY_HHMMSS));
////            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
//
//            int rowCount = 1;
//            int i = 1;
//            for (CloudCertificate item : cloudCertificates) {
//                log.info("i: {}", i);
//                org.apache.poi.ss.usermodel.Row userRow = sheet.createRow(rowCount++);
//                int j = 0;
//                userRow.createCell(j++).setCellValue(i++);
//                switch (item.getCertificateType().getName()) {
//                    case AppConstants.CERTIFICATE_TYPE_PERSONAL:
//                        userRow.createCell(j++).setCellValue(item.getPersonalName());
//                        userRow.createCell(j++).setCellValue(item.getPersonalId());
//                        break;
//                    case AppConstants.CERTIFICATE_TYPE_ENTERPRISE:
//                        userRow.createCell(j++).setCellValue(item.getCompanyName());
//                        userRow.createCell(j++).setCellValue(item.getEnterpriseId());
//                        break;
//                    default:
//                        userRow.createCell(j++).setCellValue(item.getPersonalName() + "/" + item.getCompanyName());
//                        userRow.createCell(j++).setCellValue(item.getPersonalId() + "/" + item.getEnterpriseId());
//                }
//                userRow.createCell(j++).setCellValue(item.getCertificateAuthority().getName());
//                userRow.createCell(j++).setCellValue(item.getCertificateType().getName());
//                userRow.createCell(j++).setCellValue(item.getCertificateSn());
//
//                org.apache.poi.ss.usermodel.Cell cell = userRow.createCell(j++);
//                cell.setCellValue(item.getEffectiveDt());
//                cell.setCellStyle(dateTimeCellStyle);
//
//                org.apache.poi.ss.usermodel.Cell cell2 = userRow.createCell(j++);
//                cell2.setCellValue(item.getExpirationDt());
//                cell2.setCellStyle(dateTimeCellStyle);
//            }
//
//            workbook.write(out);
//            return out.toByteArray();
//        }
//    }
//
//    public static ByteArrayInputStream csrToExcel(List<CloudAgreementCsv> cloudAgreementCsvs) throws IOException {
////        String[] COLUMNs = {"Index", "UUID", "Relying Party", "Certificate Authority", "SubjectDN", "CSR", "Certificate"};
//        String[] COLUMNs = {"Index", "AGREEMENT UUID", "RELYING PARTY", "SUBJECT DN", "CSR"};
////        try (Workbook workbook = new XSSFWorkbook();
//        try (Workbook workbook = new HSSFWorkbook();
//                ByteArrayOutputStream out = new ByteArrayOutputStream();) {
//            CreationHelper createHelper = workbook.getCreationHelper();
//
//            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("CSR");
//
//            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
//            headerFont.setBold(true);
//            headerFont.setColor(IndexedColors.BLUE.getIndex());
//
//            CellStyle headerCellStyle = workbook.createCellStyle();
//            headerCellStyle.setFont(headerFont);
//
//            // Row for Header
//            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
//
//            // Header
//            for (int col = 0; col < COLUMNs.length; col++) {
//                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(col);
//                cell.setCellValue(COLUMNs[col]);
//                cell.setCellStyle(headerCellStyle);
//            }
//
//            // CellStyle for Age
//            CellStyle ageCellStyle = workbook.createCellStyle();
//            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
//
//            int rowCount = 1;
//            int i = 1;
//            for (CloudAgreementCsv item : cloudAgreementCsvs) {
//
//                org.apache.poi.ss.usermodel.Row userRow = sheet.createRow(rowCount++);
//                userRow.createCell(0).setCellValue(i++);
//                userRow.createCell(1).setCellValue(item.getUuid());
//                userRow.createCell(2).setCellValue(item.getRelyingParty());
////                userRow.createCell(4).setCellValue(item.getCertificateAuthority());
////                if(agreementParam.getCmnd() != null){
////                    fileName = "CMND_" + agreementParam.getCmnd() + "_" + (i) + ".csr";
////                }else if(agreementParam.getMst() != null){
////                    fileName =  "MST_" + agreementParam.getMst() + "_" + (i) + ".csr";
////                }
//                userRow.createCell(3).setCellValue(item.getSubjectDN());
//                userRow.createCell(4).setCellValue(item.getCsr());
////                userRow.createCell(6).setCellValue(item.getCertificate());
//
//            }
//
//            workbook.write(out);
//            return new ByteArrayInputStream(out.toByteArray());
//        }
//    }
//
//    public static ByteArrayInputStream exportCsrFromCertificateIds(List<CloudCertificate> cloudCertificates) throws IOException {
//        String[] COLUMNs = {"Index", "AGREEMENT UUID", "RELYING PARTY", "SUBJECT DN", "CSR"};
////        try (Workbook workbook = new XSSFWorkbook();
//        try (Workbook workbook = new HSSFWorkbook();
//                ByteArrayOutputStream out = new ByteArrayOutputStream();) {
//            CreationHelper createHelper = workbook.getCreationHelper();
//
//            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("CSR");
//
//            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
//            headerFont.setBold(true);
//            headerFont.setColor(IndexedColors.BLUE.getIndex());
//
//            CellStyle headerCellStyle = workbook.createCellStyle();
//            headerCellStyle.setFont(headerFont);
//
//            // Row for Header
//            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
//
//            // Header
//            for (int col = 0; col < COLUMNs.length; col++) {
//                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(col);
//                cell.setCellValue(COLUMNs[col]);
//                cell.setCellStyle(headerCellStyle);
//            }
//
//            // CellStyle for Age
////            CellStyle ageCellStyle = workbook.createCellStyle();
////            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
//            int rowCount = 1;
//            int i = 1;
//            for (CloudCertificate item : cloudCertificates) {
//                org.apache.poi.ss.usermodel.Row userRow = sheet.createRow(rowCount++);
//                userRow.createCell(0).setCellValue(i++);
//                userRow.createCell(1).setCellValue(item.getCloudAgreement().getAgreementUuid());
//                userRow.createCell(2).setCellValue(item.getRelyingParty().getName());
////                userRow.createCell(3).setCellValue(item.getCertificateAuthority().getName());
//                userRow.createCell(3).setCellValue(item.getSubject());
//                userRow.createCell(4).setCellValue(item.getCsr());
////                userRow.createCell(6).setCellValue(item.getCertificate());
//
//            }
//
//            workbook.write(out);
//            return new ByteArrayInputStream(out.toByteArray());
//        }
//    }
//
//    public static ByteArrayInputStream createTemplateRegisterAgreementMultiple(List<CloudCertificate> cloudCertificates) throws IOException {
//        String[] COLUMNS_TYPE = {"TYPE", "RELYING PARTY", "CERTIFICATE PROFILE", "SIGNING PROFILE", "AUTH MODE"};
//        String[] COLUMNS_INFO_OWRNER = {"EMAIL", "MSISDN", "USERNAME", "PASSWORD"};
//        String[] COLUMNS_CERTIFICATE_INFO = {"TYPE ENTERPRISE", "ENTERPRISE ID", "TYPE PERSONAL", "PERSONAL ID", "COMMON NAME", "ORGANIZATION", "TITLE", "EMAIL", "TELEPHONE", "LOCATION", "STATE OR PROVINCE"};
////        try (Workbook workbook = new XSSFWorkbook();
//
//        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
//            CreationHelper createHelper = wb.getCreationHelper();
//
//            XSSFSheet sheet = wb.createSheet("INFO");
//            XSSFFont headerFont = wb.createFont();
//            headerFont.setBold(true);
//            headerFont.setColor(IndexedColors.BLUE.getIndex());
//
//            CellStyle headerCellStyle = wb.createCellStyle();
//            headerCellStyle.setFont(headerFont);
//
//            // Row for Header
//            XSSFRow headerRow = sheet.createRow(0);
//
//            // Header
//            for (int col = 0; col < COLUMNS_TYPE.length; col++) {
//                XSSFCell cell = headerRow.createCell(col);
//                cell.setCellValue(COLUMNS_TYPE[col]);
//                cell.setCellStyle(headerCellStyle);
//            }
//
//            XSSFRow headerRowValueType = sheet.createRow(1);
//            headerRowValueType.createCell(0).setCellValue("VALUE");
//            headerRowValueType.createCell(1).setCellValue("FEC");
//            headerRowValueType.createCell(2).setCellValue("MOBILE.1M");
//            headerRowValueType.createCell(3).setCellValue("4K");
//            headerRowValueType.createCell(4).setCellValue("TSE");
//
//            int rowCount = 1;
//            int i = 1;
//            for (CloudCertificate item : cloudCertificates) {
//                XSSFRow userRow = sheet.createRow(rowCount++);
//                userRow.createCell(0).setCellValue(i++);
//                userRow.createCell(1).setCellValue(item.getCloudAgreement().getAgreementUuid());
//                userRow.createCell(2).setCellValue(item.getRelyingParty().getName());
//                userRow.createCell(3).setCellValue(item.getCertificateAuthority().getName());
//                userRow.createCell(4).setCellValue(item.getSubject());
//                userRow.createCell(5).setCellValue(item.getCsr());
//                userRow.createCell(6).setCellValue(item.getCertificate());
//
//            }
//
////            COLUMNS_TYPE.write(out);
//            return new ByteArrayInputStream(out.toByteArray());
//        }
//    }
//
//    public static List<CloudAgreementCsv> excelToObject(InputStream inputStream) throws IOException {
//
//        List<CloudAgreementCsv> cloudAgreementCsvs = new ArrayList<>();
//        // Đối tượng workbook cho file XSL.
//        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
//
//        // Lấy ra sheet đầu tiên từ workbook
//        HSSFSheet sheet = workbook.getSheetAt(0);
//
//        // Lấy ra Iterator cho tất cả các dòng của sheet hiện tại.
//        Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();
//
//        int i = 0;
//        while (rowIterator.hasNext()) {
//            org.apache.poi.ss.usermodel.Row row = rowIterator.next();
//            if (i == 0) {
//                i = 1;
//                continue;
//            }
//
//            CloudAgreementCsv cloudAgreementCsv = new CloudAgreementCsv();
//            for (int cn = 0; cn < row.getLastCellNum(); cn++) {
//                org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn);
//                if (cell == null) {
//                    // This cell is empty/blank/un-used, handle as needed
//                } else {
//                    switch (cn) {
//                        case 0:
//                            cloudAgreementCsv.setIndex((int) row.getCell(0).getNumericCellValue());
//                            break;
//                        case 1:
//                            cloudAgreementCsv.setUuid(row.getCell(1).getStringCellValue());
//                            break;
//                        case 2:
//                            cloudAgreementCsv.setRelyingParty(row.getCell(2).getStringCellValue());
//                            break;
//                        case 3:
//                            cloudAgreementCsv.setCertificateAuthority(row.getCell(3).getStringCellValue());
//                            break;
//                        case 4:
//                            cloudAgreementCsv.setSubjectDN(row.getCell(4).getStringCellValue());
//                            break;
//                        case 5:
//                            cloudAgreementCsv.setCsr(row.getCell(5).getStringCellValue());
//                            break;
//                        case 6:
//                            cloudAgreementCsv.setCertificate(row.getCell(6).getStringCellValue());
//                            break;
//                    }
//                }
//            }
//
//            cloudAgreementCsvs.add(cloudAgreementCsv);
//
//            // Lấy Iterator cho tất cả các cell của dòng hiện tại.
//            Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
//
////            while (cellIterator.hasNext()) {
////                Cell cell = cellIterator.next();
////
////                // Đổi thành getCellType() nếu sử dụng POI 4.x
////                CellType cellType = cell.getCellTypeEnum();
////
//        }
//        return cloudAgreementCsvs;
//    }
//
//    private static String getStringValueFromCell(org.apache.poi.ss.usermodel.Cell cell) {
//        if (cell == null) {
//            return null;
//        }
//        try {
//            switch (cell.getCellTypeEnum()) {
//                case NUMERIC:
//                    if (DateUtil.isCellDateFormatted(cell)) {
//                        return null;
//                    } else {
//                        return String.valueOf((long) cell.getNumericCellValue());
//                    }
//                case STRING:
//                    return cell.getStringCellValue().trim();
//                default:
//                    return null;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            ;
//            log.error(ex.getMessage());
//        }
//        return null;
//
//    }
//
//    private static int getIntValueFromCell(org.apache.poi.ss.usermodel.Cell cell) {
//        try {
//            switch (cell.getCellTypeEnum()) {
////                    case _NONE:
////                        break;
////                    case BOOLEAN:
////                        break;
////                    case BLANK:
////                        break;
////                    case FORMULA:
////                        break;
//                case NUMERIC:
//                    if (DateUtil.isCellDateFormatted(cell)) {
//                        return 0;
//                    }
//                    return (int) cell.getNumericCellValue();
//                case STRING:
//                    return Integer.parseInt(cell.getStringCellValue());
//                default:
//                    return 0;
////                    case ERROR:
////                        break;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//
//        }
//        return 0;
//
//    }
//
//    private static long getLongCell(org.apache.poi.ss.usermodel.Cell cell) {
//        if (cell == null) {
//            return 0;
//        }
//        try {
//            switch (cell.getCellTypeEnum()) {
//                case NUMERIC:
//                    if (DateUtil.isCellDateFormatted(cell)) {
//                        return cell.getDateCellValue().getTime();
//                    }
//                    return (long) cell.getNumericCellValue();
//                case STRING:
//                    return Long.parseLong(cell.getStringCellValue());
//                default:
//                    return 0;
//            }
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//            ex.printStackTrace();
//        }
//        return 0;
//
//    }
//
//    public static Map<String, Object> readFileExcelToObjectRegisterAgreement(InputStream inputStream, String extension) {
//
//        List<AgreementParam> agreementParams = new ArrayList<>();
//        AgreementParam agreementParamType = new AgreementParam();
//        try {
//            Iterator<Row> rowIterator = null;
//            if ("xls".equalsIgnoreCase(extension)) {
//                rowIterator = readXls(inputStream);
//            } else if ("xlsx".equalsIgnoreCase(extension)) {
//                rowIterator = readXlsx(inputStream);
//            }
//
//            int rowIndex = 0;
//            while (rowIterator.hasNext()) {
//                org.apache.poi.ss.usermodel.Row row = (org.apache.poi.ss.usermodel.Row) rowIterator.next();
//                rowIndex++;
//                if (row == null) {
//                    continue;
//                }
//                if (rowIndex == 2) {
//                    for (int cn = 0; cn < row.getLastCellNum(); cn++) {
//                        org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn);
//                        if (cell == null) {
//                            // This cell is empty/blank/un-used, handle as needed
//                        } else {
//                            switch (cn) {
//                                case 1:
//                                    agreementParamType.setRelyingParty(getStringValueFromCell(cell));
//                                    break;
//                                case 2:
//                                    agreementParamType.setCertificateProfile(getStringValueFromCell(cell));
//                                    break;
//                                case 3:
//                                    agreementParamType.setSigningProfile(getStringValueFromCell(cell));
//                                    break;
//                                case 4:
//                                    agreementParamType.setSharedModeType(getStringValueFromCell(cell));
//                                    break;
//                                case 5:
//                                    agreementParamType.setAuthMode(getStringValueFromCell(cell));
////                                    agreementParamType.setAuthMode(getStringValueFromCell(cell));
//                                    break;
//                                case 6:
//                                    agreementParamType.setScal(getIntValueFromCell(cell));
//                                    break;
//                                case 7:
//                                    agreementParamType.setMultipleSignature(getIntValueFromCell(cell));
//                                    break;
//                                case 8:
//                                    agreementParamType.setTwoFactorMethod(getStringValueFromCell(cell));
//                                    break;
//                            }
//                        }
//                    }
//                }
//                if (rowIndex >= 6) {
//                    AgreementParam agreementParam = new AgreementParam();
//                    agreementParam.setAgreementUuid(UUID.randomUUID().toString());
//
//                    for (int cn = 0; cn < row.getLastCellNum(); cn++) {
//                        org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn);
//                        if (cell == null) {
//                            // This cell is empty/blank/un-used, handle as needed
//                        } else {
//                            switch (cn) {
//                                case 0:
//                                    agreementParam.setI(getLongCell(cell));
//                                    break;
//                                case 1:
//                                    agreementParam.setAuthorisationEmail(getStringValueFromCell(cell));
//                                    break;
//                                case 2:
//                                    agreementParam.setAuthorisationPhone(getStringValueFromCell(cell));
//                                    break;
//                                case 3:
//                                    agreementParam.setOwnerUsername(getStringValueFromCell(cell));
//                                    break;
//                                case 4:
//                                    agreementParam.setOwnerPassword(getStringValueFromCell(cell));
//                                    break;
//                                case 5:
//                                    agreementParam.setWithOwner(getStringValueFromCell(cell));
//                                    break;
//                                case 6:
//                                    agreementParam.setSlmst(getStringValueFromCell(cell));
//                                    break;
//                                case 7:
//                                    agreementParam.setMst(getStringValueFromCell(cell));
//                                    break;
//                                case 8:
//                                    agreementParam.setSlcmnd(getStringValueFromCell(cell));
//                                    break;
//                                case 9:
//                                    agreementParam.setCmnd(getStringValueFromCell(cell));
//                                    break;
//                                case 10:
//                                    agreementParam.setCn(getStringValueFromCell(cell));
//                                    break;
//                                case 11:
//                                    agreementParam.setO(getStringValueFromCell(cell));
//                                    break;
//                                case 12:
//                                    agreementParam.setOu(getStringValueFromCell(cell));
//                                    break;
//                                case 13:
//                                    agreementParam.setT(getStringValueFromCell(cell));
//                                    break;
//                                case 14:
//                                    agreementParam.setE(getStringValueFromCell(cell));
//                                    break;
//                                case 15:
//                                    agreementParam.setTelephonenumber(getStringValueFromCell(cell));
//                                    break;
//                                case 16:
//                                    agreementParam.setL(getStringValueFromCell(cell));
//                                    break;
//                                case 17:
//                                    agreementParam.setSt(getStringValueFromCell(cell));
//                                    break;
//                                case 18:
//                                    agreementParam.setC(getStringValueFromCell(cell));
//                                    break;
//                            }
//                        }
//                    }
//                    agreementParams.add(agreementParam);
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("agreementParams", agreementParams);
//        map.put("agreementParamType", agreementParamType);
//
//        return map;
//    }
//
//    public static List<CloudAgreementCsv> readFileExcelToObject(InputStream inputStream, String extension) {
//
//        List<CloudAgreementCsv> cloudAgreementCsvs = new ArrayList<>();
//        try {
//
//            Iterator<Row> rowIterator = null;
//            if ("xls".equalsIgnoreCase(extension)) {
//                rowIterator = readXls(inputStream);
//            } else if ("xlsx".equalsIgnoreCase(extension)) {
//                rowIterator = readXlsx(inputStream);
//            }
//
//            int i = 0;
//            while (rowIterator.hasNext()) {
//                org.apache.poi.ss.usermodel.Row row = rowIterator.next();
//                if (i == 0) {
//                    i = 1;
//                    continue;
//                }
//                if (row == null) {
//                    continue;
//                }
//
//                CloudAgreementCsv cloudAgreementCsv = new CloudAgreementCsv();
//
//                for (int cn = 0; cn < row.getLastCellNum(); cn++) {
//                    org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn);
//                    if (cell == null) {
//                        // This cell is empty/blank/un-used, handle as needed
//                    } else {
//                        switch (cn) {
//                            case 0:
//                                cloudAgreementCsv.setIndex((int) row.getCell(0).getNumericCellValue());
//                                break;
//                            case 1:
//                                cloudAgreementCsv.setUuid(row.getCell(1).getStringCellValue());
//                                break;
//                            case 2:
//                                cloudAgreementCsv.setRelyingParty(row.getCell(2).getStringCellValue());
//                                break;
////                            case 3:
////                                cloudAgreementCsv.setCertificateAuthority(row.getCell(3).getStringCellValue());
////                                break;
////                            case 4:
////                                cloudAgreementCsv.setSubjectDN(row.getCell(4).getStringCellValue());
////                                break;
////                            case 5:
////                                cloudAgreementCsv.setCsr(row.getCell(5).getStringCellValue());
////                                break;
//                            case 3:
//                                cloudAgreementCsv.setCertificate(row.getCell(3).getStringCellValue());
//                                break;
//                        }
//                    }
//                }
//                cloudAgreementCsv.setStatus(true);
//                cloudAgreementCsvs.add(cloudAgreementCsv);
//
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//
//        return cloudAgreementCsvs;
//    }
//
//    public static List<SimCardSearch> readFileImportSimcardToObject(InputStream inputStream, String extension) {
//
//        List<SimCardSearch> simCardSearches = new ArrayList<>();
//        try {
//
//            Iterator<Row> rowIterator = null;
//            if ("xls".equalsIgnoreCase(extension)) {
//                rowIterator = readXls(inputStream);
//            } else if ("xlsx".equalsIgnoreCase(extension)) {
//                rowIterator = readXlsx(inputStream);
//            }
//
//            int i = 0;
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//                if (i == 0) {
//                    i = 1;
//                    continue;
//                }
//                if (row == null) {
//                    continue;
//                }
//
//                SimCardSearch simCardSearch = new SimCardSearch();
//
//                for (int cn = 0; cn < row.getLastCellNum(); cn++) {
//                    Cell cell = row.getCell(cn);
//                    if (cell == null) {
//                        // This cell is empty/blank/un-used, handle as needed
//                    } else {
//                        switch (cn) {
//                            case 0:
//                                simCardSearch.setIndex(getIntValueFromCell(cell));
//                                break;
//                            case 1:
//                                simCardSearch.setMsisdn(getStringValueFromCell(cell));
//                                break;
//                            case 2:
//                                simCardSearch.setIccid(getStringValueFromCell(cell));
//                                break;
//                            case 3:
//                                simCardSearch.setImsi(getStringValueFromCell(cell));
//                                break;
//                            case 4:
//                                simCardSearch.setKicKey(getStringValueFromCell(cell));
//                                break;
//                            case 5:
//                                simCardSearch.setKidKey(getStringValueFromCell(cell));
//                                break;
//                            case 6:
//                                simCardSearch.setSsdKicKey(getStringValueFromCell(cell));
//                                break;
//                            case 7:
//                                simCardSearch.setSsdKidKey(getStringValueFromCell(cell));
//                                break;
//                            case 8:
//                                simCardSearch.setEki(getStringValueFromCell(cell));
//                                break;
//                        }
//                    }
//                }
//                simCardSearches.add(simCardSearch);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//
//        return simCardSearches;
//    }
//
//    protected static Iterator<Row> readXls(InputStream inputStream) throws IOException {
//
//        // Đối tượng workbook cho file XSL.
//        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
//        workbook.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//        // Lấy ra sheet đầu tiên từ workbook
//        HSSFSheet sheet = workbook.getSheetAt(0);
//
//        // Lấy ra Iterator cho tất cả các dòng của sheet hiện tại.
////        Iterator<Row> rowIterator = sheet.iterator();
//        return sheet.iterator();
//    }
//
//    protected static Iterator<Row> readXlsx(InputStream inputStream) throws IOException {
//
//        //creating Workbook instance that refers to .xlsx file
//        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
//        wb.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//
//        XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
//
//        return sheet.iterator();
//    }
//
//    public static ByteArrayOutputStream readFileTemplateRegisterAgreement(InputStream inputStream, List<RelyingParty> relyingParties,
//            List<CertificateProfile> certificateProfiles, List<CertificateSigningProfile> certificateSigningProfiles,
//            List<AuthMode> authModes, List<Province> provinces,
//            List<Type> typesEnterprise, List<Type> typesPersonal, List<Type> typesCountries,
//            List<SharedMode> sharedModes, List<TwoFactorMethod> twoFactorMethods,
//            List<EntityProperties.MultipleSignature> multipleSignatures, List<Type> scals) {
//
//        try {
//            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
//            wb.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
//            Row rowInfo = sheet.getRow(1);
//
//            XSSFSheet sheetRelyingParty = wb.getSheetAt(1);
//            if (relyingParties.size() > 0) {
//                rowInfo.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(relyingParties.get(0).getName());
//
//                for (int i = 0; i < relyingParties.size(); i++) {
//                    Row row = sheetRelyingParty.createRow(i + 1);
//                    row.createCell(0).setCellValue(i + 1);
//                    row.createCell(1).setCellValue(relyingParties.get(i).getName());
//                    row.createCell(2).setCellValue(relyingParties.get(i).getDesc());
//                }
//
//                XSSFName name = wb.createName();
//                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
//                XSSFDataValidationConstraint dvConstraint;
//                CellRangeAddressList addressList;
//
//                name.setNameName("Relying_Party_List");
//                name.setRefersToFormula("'" + sheetRelyingParty.getSheetName() + "'!$B$2:$B$" + (relyingParties.size() + 1));
//
//                XSSFDataValidationConstraint dvConstraintRelying = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("Relying_Party_List");
//
//                CellRangeAddressList addressListRelying = new CellRangeAddressList(1, 1, 1, 1);
//                XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraintRelying, addressListRelying);
//                validation.setSuppressDropDownArrow(true);
//                validation.setShowErrorBox(true);
//                sheet.addValidationData(validation);
//            }
//
//            XSSFSheet sheetCertificateProfile = wb.getSheetAt(2);
//            if (certificateProfiles.size() > 0) {
//                rowInfo.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(certificateProfiles.get(0).getName());
//
//                for (int i = 0; i < certificateProfiles.size(); i++) {
//                    Row row = sheetCertificateProfile.createRow(i + 1);
//                    row.createCell(0).setCellValue(i + 1);
//                    row.createCell(1).setCellValue(certificateProfiles.get(i).getName());
//                    row.createCell(2).setCellValue(certificateProfiles.get(i).getDesc());
//                    row.createCell(3).setCellValue(certificateProfiles.get(i).getCertificateAuthority().getName());
//                }
//                XSSFName name = wb.createName();
//                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
//                XSSFDataValidationConstraint dvConstraint;
//                CellRangeAddressList addressList;
//                XSSFDataValidation validation;
//
//                name.setNameName("Certificate_Profile_List");
//                name.setRefersToFormula("'" + sheetCertificateProfile.getSheetName() + "'!$B$2:$B$" + (certificateProfiles.size() + 1));
//                dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("Certificate_Profile_List");
//
//                addressList = new CellRangeAddressList(1, 1, 2, 2);
//                validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
//                validation.setSuppressDropDownArrow(true);
//                validation.setShowErrorBox(true);
//                sheet.addValidationData(validation);
//            }
//
//            XSSFSheet sheetSigningProfile = wb.getSheetAt(3);
//            if (certificateSigningProfiles.size() > 0) {
//                rowInfo.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(certificateSigningProfiles.get(0).getName());
//
//                for (int i = 0; i < certificateSigningProfiles.size(); i++) {
//                    Row row = sheetSigningProfile.createRow(i + 1);
//                    row.createCell(0).setCellValue(i + 1);
//                    row.createCell(1).setCellValue(certificateSigningProfiles.get(i).getName());
//                    row.createCell(2).setCellValue(certificateSigningProfiles.get(i).getDesc());
//                }
//                XSSFName name = wb.createName();
//                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
//                XSSFDataValidationConstraint dvConstraint;
//                CellRangeAddressList addressList;
//                XSSFDataValidation validation;
//
//                name.setNameName("Signing_Profile_List");
//                name.setRefersToFormula("'" + sheetSigningProfile.getSheetName() + "'!$B$2:$B$" + (certificateSigningProfiles.size() + 1));
//                dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("Signing_Profile_List");
//
//                addressList = new CellRangeAddressList(1, 1, 3, 3);
//                validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
//                validation.setSuppressDropDownArrow(true);
//                validation.setShowErrorBox(true);
//                sheet.addValidationData(validation);
//            }
//
//            XSSFSheet sheetCommon = wb.getSheetAt(4);
//            if (sharedModes.size() > 0) {
//                rowInfo.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(sharedModes.get(0).getName());
//
//                for (int i = 0; i < sharedModes.size(); i++) {
//                    Row row = sheetCommon.getRow(i + 2);
//                    if (row == null) {
//                        row = sheetCommon.createRow(i + 2);
//                    }
//                    row.createCell(0).setCellValue(i + 1);
//                    row.createCell(1).setCellValue(sharedModes.get(i).getName());
//                    row.createCell(2).setCellValue(sharedModes.get(i).getDesc());
//                }
//                XSSFName name = wb.createName();
//                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
//                XSSFDataValidationConstraint dvConstraint;
//                CellRangeAddressList addressList;
//                XSSFDataValidation validation;
//
//                name.setNameName("SHARE_MODE_List");
//                name.setRefersToFormula("'" + sheetCommon.getSheetName() + "'!$B$3:$B$" + (sharedModes.size() + 2));
//                dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("SHARE_MODE_List");
//
//                addressList = new CellRangeAddressList(1, 1, 4, 4);
//                validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
//                validation.setSuppressDropDownArrow(true);
//                validation.setShowErrorBox(true);
//                sheet.addValidationData(validation);
//            }
//
////            XSSFSheet sheetShareMode = wb.getSheetAt(4);
////            if (sharedModes.size() > 0) {
////                rowInfo.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(sharedModes.get(0).getName());
////
////                for (int i = 0; i < sharedModes.size(); i++) {
////                    Row row = sheetShareMode.createRow(i + 1);
////                    row.createCell(0).setCellValue(i + 1);
////                    row.createCell(1).setCellValue(sharedModes.get(i).getName());
////                    row.createCell(2).setCellValue(sharedModes.get(i).getDesc());
////                }
////                XSSFName name = wb.createName();
////                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
////                XSSFDataValidationConstraint dvConstraint;
////                CellRangeAddressList addressList;
////                XSSFDataValidation validation;
////
////                name.setNameName("SHARE_MODE_List");
////                name.setRefersToFormula("'" + sheetShareMode.getSheetName() + "'!$B$2:$B$" + (sharedModes.size() + 1));
////                dvConstraint = (XSSFDataValidationConstraint)
////                        dvHelper.createFormulaListConstraint("SHARE_MODE_List");
////
////                addressList = new CellRangeAddressList(1, 1, 4, 4);
////                validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
////                validation.setSuppressDropDownArrow(true);
////                validation.setShowErrorBox(true);
////                sheet.addValidationData(validation);
////            }
////            XSSFSheet sheetAuthMode = wb.getSheetAt(5);
//            if (authModes.size() > 0) {
//                rowInfo.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(authModes.get(0).getName());
//
//                for (int i = 0; i < authModes.size(); i++) {
//                    Row row = sheetCommon.getRow(i + 2);
//                    if (row == null) {
//                        row = sheetCommon.createRow(i + 2);
//                    }
//                    row.createCell(3).setCellValue(i + 1);
//                    row.createCell(4).setCellValue(authModes.get(i).getName());
//                    row.createCell(5).setCellValue(authModes.get(i).getDesc());
//                }
//                XSSFName name = wb.createName();
//                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
//                XSSFDataValidationConstraint dvConstraint;
//                CellRangeAddressList addressList;
//                XSSFDataValidation validation;
//
//                name.setNameName("Auth_Mode_List");
//                name.setRefersToFormula("'" + sheetCommon.getSheetName() + "'!$E$3:$E$" + (authModes.size() + 2));
//                dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("Auth_Mode_List");
//
//                addressList = new CellRangeAddressList(1, 1, 5, 5);
//                validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
//                validation.setSuppressDropDownArrow(true);
//                validation.setShowErrorBox(true);
//                sheet.addValidationData(validation);
//            }
//
////            XSSFSheet sheetScal = wb.getSheetAt(6);
//            if (scals.size() > 0) {
//                rowInfo.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(scals.get(0).getName());
//
//                for (int i = 0; i < scals.size(); i++) {
//                    Row row = sheetCommon.getRow(i + 2);
//                    if (row == null) {
//                        row = sheetCommon.createRow(i + 2);
//                    }
//                    row.createCell(6).setCellValue(i + 1);
//                    row.createCell(7).setCellValue(scals.get(i).getName());
//                    row.createCell(8).setCellValue(scals.get(i).getDesc());
//                }
//                XSSFName name = wb.createName();
//                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
//                XSSFDataValidationConstraint dvConstraint;
//                CellRangeAddressList addressList;
//                XSSFDataValidation validation;
//
//                name.setNameName("Scal_List");
//                name.setRefersToFormula("'" + sheetCommon.getSheetName() + "'!$H$3:$H$" + (scals.size() + 2));
//                dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("Scal_List");
//
//                addressList = new CellRangeAddressList(1, 1, 6, 6);
//                validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
//                validation.setSuppressDropDownArrow(true);
//                validation.setShowErrorBox(true);
//                sheet.addValidationData(validation);
//            }
//
////            XSSFSheet sheetMultipleSignature = wb.getSheetAt(7);
//            if (multipleSignatures.size() > 0) {
//                rowInfo.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(multipleSignatures.get(0).getName());
//
//                for (int i = 0; i < multipleSignatures.size(); i++) {
//                    Row row = sheetCommon.getRow(i + 2);
//                    if (row == null) {
//                        row = sheetCommon.createRow(i + 2);
//                    }
//                    row.createCell(9).setCellValue(i + 1);
//                    row.createCell(10).setCellValue(multipleSignatures.get(i).getValue());
//                    row.createCell(11).setCellValue(multipleSignatures.get(i).getName());
////                    row.createCell(2).setCellValue(scals.get(i).getDesc());
//                }
//                XSSFName name = wb.createName();
//                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
//                XSSFDataValidationConstraint dvConstraint;
//                CellRangeAddressList addressList;
//                XSSFDataValidation validation;
//
//                name.setNameName("MultipleSignature_List");
//                name.setRefersToFormula("'" + sheetCommon.getSheetName() + "'!$K$3:$K$" + (multipleSignatures.size() + 2));
//                dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("MultipleSignature_List");
//
//                addressList = new CellRangeAddressList(1, 1, 7, 7);
//                validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
//                validation.setSuppressDropDownArrow(true);
//                validation.setShowErrorBox(true);
//                sheet.addValidationData(validation);
//            }
////            rowInfo.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(multipleSignature);
//
////            XSSFSheet sheetTwoFactor = wb.getSheetAt(8);
//            if (twoFactorMethods.size() > 0) {
//                rowInfo.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(twoFactorMethods.get(0).getName());
//
//                for (int i = 0; i < twoFactorMethods.size(); i++) {
//                    Row row = sheetCommon.getRow(i + 2);
//                    if (row == null) {
//                        row = sheetCommon.createRow(i + 2);
//                    }
//                    row.createCell(12).setCellValue(i + 1);
//                    row.createCell(13).setCellValue(twoFactorMethods.get(i).getName());
//                    row.createCell(14).setCellValue(twoFactorMethods.get(i).getDesc());
//                }
//                XSSFName name = wb.createName();
//                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
//                XSSFDataValidationConstraint dvConstraint;
//                CellRangeAddressList addressList;
//                XSSFDataValidation validation;
//
//                name.setNameName("Two_factor_List");
//                name.setRefersToFormula("'" + sheetCommon.getSheetName() + "'!$N$3:$N$" + (twoFactorMethods.size() + 2));
//                dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("Two_factor_List");
//
//                addressList = new CellRangeAddressList(1, 1, 8, 8);
//                validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
//                validation.setSuppressDropDownArrow(true);
//                validation.setShowErrorBox(true);
//                sheet.addValidationData(validation);
//            }
//
//            XSSFSheet sheetCertificateRefer = wb.getSheetAt(5);
//            if (provinces.size() > 0) {
//
//                for (int i = 0; i < provinces.size(); i++) {
//                    Row row = sheetCertificateRefer.getRow(i + 2);
//                    if (row == null) {
//                        row = sheetCertificateRefer.createRow(i + 2);
//                    }
//                    row.createCell(0).setCellValue(i + 1);
//                    row.createCell(1).setCellValue(provinces.get(i).getDesc());
//                    row.createCell(2).setCellValue(provinces.get(i).getDesc());
//                }
//                XSSFName name = wb.createName();
//                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
//                XSSFDataValidationConstraint dvConstraint;
//                CellRangeAddressList addressList;
//                XSSFDataValidation validation;
//
//                name.setNameName("Povince_List");
//                name.setRefersToFormula("'" + sheetCertificateRefer.getSheetName() + "'!$B$3:$B$" + (provinces.size() + 2));
//                dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("Povince_List");
//
//                addressList = new CellRangeAddressList(6, 7, 17, 17);
//                validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
//                validation.setSuppressDropDownArrow(true);
//                validation.setShowErrorBox(true);
//                sheet.addValidationData(validation);
//
//            }
//
//            if (typesEnterprise.size() > 0) {
//                for (int i = 0; i < typesEnterprise.size(); i++) {
//                    Row row = sheetCertificateRefer.getRow(i + 2);
//                    if (row == null) {
//                        row = sheetCertificateRefer.createRow(i + 2);
//                        row.createCell(0).setCellValue(i + 1);
//                    }
//                    row.createCell(3).setCellValue(typesEnterprise.get(i).getName());
//                    row.createCell(4).setCellValue(typesEnterprise.get(i).getDesc());
//                }
//                XSSFName name = wb.createName();
//                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
//                XSSFDataValidationConstraint dvConstraint;
//                CellRangeAddressList addressList;
//                XSSFDataValidation validation;
//
//                name.setNameName("Enterprise_List");
//                name.setRefersToFormula("'" + sheetCertificateRefer.getSheetName() + "'!$D$3:$D$" + (typesEnterprise.size() + 2));
//                dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("Enterprise_List");
//
//                addressList = new CellRangeAddressList(6, 7, 6, 6);
//                validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
//                validation.setSuppressDropDownArrow(true);
//                validation.setShowErrorBox(true);
//                sheet.addValidationData(validation);
//            }
//
//            if (typesPersonal.size() > 0) {
//                for (int i = 0; i < typesPersonal.size(); i++) {
//                    Row row = sheetCertificateRefer.getRow(i + 2);
//                    if (row == null) {
//                        row = sheetCertificateRefer.createRow(i + 2);
//                        row.createCell(0).setCellValue(i + 1);
//                    }
//                    row.createCell(5).setCellValue(typesPersonal.get(i).getName());
//                    row.createCell(6).setCellValue(typesPersonal.get(i).getDesc());
//                }
//                XSSFName name = wb.createName();
//                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
//                XSSFDataValidationConstraint dvConstraint;
//                CellRangeAddressList addressList;
//                XSSFDataValidation validation;
//
//                name.setNameName("Personal_List");
//                name.setRefersToFormula("'" + sheetCertificateRefer.getSheetName() + "'!$F$3:$F$" + (typesPersonal.size() + 2));
//                dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("Personal_List");
//
//                addressList = new CellRangeAddressList(
//                        6, 7, 8, 8);
//                validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
//                validation.setSuppressDropDownArrow(true);
//                validation.setShowErrorBox(true);
//                sheet.addValidationData(validation);
//            }
//
//            if (typesCountries.size() > 0) {
//                for (int i = 0; i < typesCountries.size(); i++) {
//                    Row row = sheetCertificateRefer.getRow(i + 2);
//                    if (row == null) {
//                        row = sheetCertificateRefer.createRow(i + 2);
//                        row.createCell(0).setCellValue(i + 1);
//                    }
//                    row.createCell(7).setCellValue(typesCountries.get(i).getName());
//                    row.createCell(8).setCellValue(typesCountries.get(i).getDesc());
//                }
//                XSSFName name = wb.createName();
//                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
//                XSSFDataValidationConstraint dvConstraint;
//                CellRangeAddressList addressList;
//                XSSFDataValidation validation;
//
//                name.setNameName("Country_List");
//                name.setRefersToFormula("'" + sheetCertificateRefer.getSheetName() + "'!$H$3:$H$" + (typesPersonal.size() + 2));
//                dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("Country_List");
//
//                addressList = new CellRangeAddressList(
//                        6, 7, 18, 18);
//                validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
//                validation.setSuppressDropDownArrow(true);
//                validation.setShowErrorBox(true);
//                sheet.addValidationData(validation);
//            }
//
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            wb.write(out);
//            return out;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return null;
//    }
//
//    public static ByteArrayOutputStream writeFileRegisterAgreementResult(InputStream inputStream, String extension, List<AgreementParam> agreementParams) {
//
//        try {
//
//            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
//            wb.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
//            Iterator<Row> rowIterator = sheet.iterator();
//
//            int rowIndex = 0;
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//                rowIndex++;
//                if (row == null) {
//                    continue;
//                }
//                if (rowIndex >= 6) {
//                    row.createCell(19).setCellValue(agreementParams.get(rowIndex - 6).getCode());
//                    row.createCell(20).setCellValue(agreementParams.get(rowIndex - 6).getMsg());
//                }
//            }    //creating a Sheet object to retrieve object
//
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            wb.write(out);
//            return out;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return null;
//    }
//
//    public static ByteArrayOutputStream writeFileImportSimCardResult(InputStream inputStream, String extension, List<SimCardSearch> simCardSearches) {
//
//        try {
//            Iterator<Row> rowIterator = null;
//            if ("xls".equalsIgnoreCase(extension)) {
//                HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
//                workbook.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//                // Lấy ra sheet đầu tiên từ workbook
//                HSSFSheet sheet = workbook.getSheetAt(0);
//                rowIterator = sheet.iterator();
//                int i = 0;
//                int index = 0;
//                while (rowIterator.hasNext()) {
//                    Row row = rowIterator.next();
//                    if (i == 0) {
//                        i = 1;
//                        continue;
//                    }
//                    if (row == null) {
//                        continue;
//                    }
//                    row.createCell(9).setCellValue(simCardSearches.get(index).getCode());
//                    row.createCell(10).setCellValue(simCardSearches.get(index).getMsg());
//                    index++;
//                }    //creating a Sheet object to retrieve object
//
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                workbook.write(out);
//                return out;
//            } else if ("xlsx".equalsIgnoreCase(extension)) {
//                XSSFWorkbook wb = new XSSFWorkbook(inputStream);
//                wb.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//                XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
//                rowIterator = sheet.iterator();
//                int i = 0;
//                int index = 0;
//                while (rowIterator.hasNext()) {
//                    Row row = rowIterator.next();
//                    if (i == 0) {
//                        i = 1;
//                        continue;
//                    }
//                    if (row == null) {
//                        continue;
//                    }
//                    row.createCell(9).setCellValue(simCardSearches.get(index).getCode());
//                    row.createCell(10).setCellValue(simCardSearches.get(index).getMsg());
//                    index++;
//                }    //creating a Sheet object to retrieve object
//
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                wb.write(out);
//                return out;
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return null;
//    }
//
//    public static ByteArrayOutputStream reportAgreement(InputStream inputStream, List<ReportRPAgreement> reportRPAgreements) {
//
//        try {
//
//            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
//            CreationHelper createHelper = wb.getCreationHelper();
//            wb.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
//            Iterator<Row> rowIterator = sheet.iterator();
//
//            CellStyle dateTimeCellStyle = wb.createCellStyle();
//            dateTimeCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(DDMMYYYY_HHMMSS));
//            int i = 2;
//
//            for (ReportRPAgreement reportRPAgreement : reportRPAgreements) {
//                Row row = sheet.createRow(i++);
//                int j = 0;
//                row.createCell(j++).setCellValue(i - 2);
//
//                Cell cell1 = row.createCell(j++);
//                cell1.setCellValue(reportRPAgreement.getDateReport());
//                cell1.setCellStyle(dateTimeCellStyle);
//
//                row.createCell(j++).setCellValue(reportRPAgreement.getCountry());
//                row.createCell(j++).setCellValue(reportRPAgreement.getStateOrProvince());
//                row.createCell(j++).setCellValue(reportRPAgreement.getLocality());
//                row.createCell(j++).setCellValue(reportRPAgreement.getPersonalName());
//                row.createCell(j++).setCellValue(reportRPAgreement.getPersonalId());
//                row.createCell(j++).setCellValue(reportRPAgreement.getPhoneNo());
//
//                Cell cell2 = row.createCell(j++);
//                cell2.setCellValue(reportRPAgreement.getReceivedDt());
//                cell2.setCellStyle(dateTimeCellStyle);
//
//                row.createCell(j++).setCellValue(reportRPAgreement.getCertificateSN());
//                row.createCell(j++).setCellValue(reportRPAgreement.getIssuerSubject());
//                row.createCell(j++).setCellValue(reportRPAgreement.getAgreementUuid());
//
//                row.createCell(j++).setCellValue(reportRPAgreement.getAgreementNo());
//                Cell cell3 = row.createCell(j++);
//                cell3.setCellValue(reportRPAgreement.getAuthorizationDt());
//                cell3.setCellStyle(dateTimeCellStyle);
//
//            }
//
////            wb.write(out);
////            return out.toByteArray();
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            wb.write(out);
//            return out;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return null;
//    }
//}
