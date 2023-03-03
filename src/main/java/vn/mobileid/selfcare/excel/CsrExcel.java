///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package vn.mobileid.selfcare.excel;
//
//
//import org.apache.poi.ss.usermodel.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import vn.mobileid.selfcare.service.dto.csr.model.CloudAgreementCsv;
//import vn.mobileid.selfcare.service.util.AppDate;
//
//public  class CsrExcel  {
//    public static void buildExcelDocument(Map<String, Object> model,
//                                      Workbook workbook,
//                                      HttpServletRequest request,
//                                      HttpServletResponse response,
//                                      List<CloudAgreementCsv> cloudAgreementCsvs ) {
//
//        Date d = new Date();
//        String fileName = "CSR_EXPORT_" + AppDate.convertDate2Str(d, "yyyyMMddHHmmss") + ".xls";
//
//        // change the file name
//        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//        // create excel xls sheet
//        Sheet sheet = workbook.createSheet("Agreement Export CSR");
//        sheet.setDefaultColumnWidth(30);
//
//        // create style for header cells
//        CellStyle style = workbook.createCellStyle();
//        Font font = workbook.createFont();
//        font.setFontName("Arial");
//        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        font.setBold(true);
//        font.setColor(IndexedColors.WHITE.getIndex());
//        style.setFont(font);
//
//        // create header row
//        Row header = sheet.createRow(0);
//        header.createCell(0).setCellValue("Index");
//        header.getCell(0).setCellStyle(style);
//        header.createCell(1).setCellValue("UUID");
//        header.getCell(1).setCellStyle(style);
//        header.createCell(2).setCellValue("Relying Party");
//        header.getCell(2).setCellStyle(style);
//        header.createCell(3).setCellValue("Certificate Authority");
//        header.getCell(3).setCellStyle(style);
//        header.createCell(4).setCellValue("SubjectDN");
//        header.getCell(4).setCellStyle(style);
//        header.createCell(5).setCellValue("CSR");
//        header.getCell(5).setCellStyle(style);
//        header.createCell(6).setCellValue("Certificate");
//
//        int rowCount = 1;
//
//        int i = 1;
//        for (CloudAgreementCsv item : cloudAgreementCsvs) {
//            Row userRow = sheet.createRow(rowCount++);
//            userRow.createCell(0).setCellValue(i++);
//            userRow.createCell(1).setCellValue(item.getUuid());
//            userRow.createCell(2).setCellValue(item.getRelyingParty());
//            userRow.createCell(3).setCellValue(item.getCertificateAuthority());
//            userRow.createCell(4).setCellValue(item.getSubjectDN());
//            userRow.createCell(5).setCellValue(item.getCsr());
//            userRow.createCell(6).setCellValue(item.getCertificate());
//
//        }
//
//    }
//}
//
