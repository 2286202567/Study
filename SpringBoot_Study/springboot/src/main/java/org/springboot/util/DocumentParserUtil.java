package org.springboot.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 通用文档解析工具类
 * 支持：PDF、Word (.docx)、Excel (.xlsx)
 */
public class DocumentParserUtil {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 从 InputStream 解析文档（支持 pdf, docx, xls, xlsx）
     * 先缓存为 byte[]，避免流被多次消费
     */
    public static String parseFromStream(InputStream inputStream, String fileType) throws IOException {
        // 1. 校验输入
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream 不能为空");
        }

        // 2. 将输入流读取为 byte[]，以便后续多次使用（虽然这里只用一次，但防止意外）
        byte[] fileBytes;
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            fileBytes = buffer.toByteArray();
        }

        // 3. 根据类型分发解析
        switch (fileType.toLowerCase()) {
            case "pdf":
                return parsePdfFromBytes(fileBytes);
            case "docx":
                return parseDocxFromBytes(fileBytes);
            case "xlsx":
            case "xls":
                return parseXlsxFromBytes(fileBytes, fileType);
            default:
                throw new IllegalArgumentException("不支持的文件类型: " + fileType +
                        "，支持的类型：pdf, docx, xls, xlsx");
        }
    }

    // ==================== 基于 byte[] 的解析方法 ====================

    private static String parsePdfFromBytes(byte[] fileBytes) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
             PDDocument document = PDDocument.load(bis)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document).trim();
        }
    }

    private static String parseDocxFromBytes(byte[] fileBytes) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
             XWPFDocument document = new XWPFDocument(bis)) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText();
                if (text != null && !text.trim().isEmpty()) {
                    sb.append(text).append("\n");
                }
            }
        }
        return sb.toString().trim();
    }

    private static String parseXlsxFromBytes(byte[] fileBytes, String fileType) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
             Workbook workbook = "xls".equalsIgnoreCase(fileType) ?
                     new HSSFWorkbook(bis) : new XSSFWorkbook(bis)) {

            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                boolean isEmptySheet = true;
                StringBuilder sheetContent = new StringBuilder();

                for (Row row : sheet) {
                    System.out.println("Processing row: " + row.getRowNum());
                    boolean isDataRow = false;
                    for (Cell cell : row) {
                        String cellValue = getCellValue(cell);
                        System.out.println("  Cell [" + cell.getAddress() + "]: '" + cellValue + "'");
                        if (cellValue.trim().length() > 0) {
                            isDataRow = true;
                            break;
                        }
                    }
                    if (isDataRow) {
                        isEmptySheet = false;
                        sheetContent.append("第").append(row.getRowNum() + 1).append("行: ");
                        for (Cell cell : row) {
                            sheetContent.append("[").append(getCellValue(cell)).append("] ");
                        }
                        sheetContent.append("\n");
                    }
                }

                if (!isEmptySheet) {
                    sb.append("\n=== 工作表: ").append(workbook.getSheetName(sheetIndex)).append(" ===\n");
                    sb.append(sheetContent);
                }
            }
        }
        return sb.toString().trim();
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }


    // ==================== 使用示例 main 方法 ====================
//    public static void main(String[] args) {
//        try {
//            // 示例：解析不同类型的文件
//            String examplePdf = "D:/doc/2.pdf";
//            String pdfText = DocumentParserUtil.parsePdfFromBytes(examplePdf, "pdf");
//            System.out.println("PDF 内容：\n" + pdfText.substring(0, Math.min(200, pdfText.length())) + "...\n");
//
//            String exampleDocx = "D:/doc/1.docx";
//            String docxText = DocumentParserUtil.parseDocument(exampleDocx, "docx");
//            System.out.println("Word 内容：\n" + docxText.substring(0, Math.min(200, docxText.length())) + "...\n");
//
//            String exampleXls = "D:/doc/3.xls";
//            String xlsxText = DocumentParserUtil.parseDocument(exampleXls, "xlsx");
//            System.out.println("Excel 内容：\n" + xlsxText.substring(0, Math.min(200, xlsxText.length())) + "...\n");
//
//            String exampleXlsx = "D:/doc/4.xlsx";
//            String xlsxText1 = DocumentParserUtil.parseDocument(exampleXlsx, "xlsx");
//
//            if (xlsxText1 != null && !xlsxText1.isEmpty()) {
//                int lengthToPrint = Math.min(200, xlsxText1.length());
//                System.out.println("Excel 内容：\n" + xlsxText1.substring(0, lengthToPrint) + "...\n");
//            } else {
//                System.out.println("Excel 内容为空或无法读取");
//            }
//
//            String exampleXls2 = "D:/doc/5.xls";
//            String xlsxText2 = DocumentParserUtil.parseDocument(exampleXls2, "xlsx");
//            System.out.println("Excel 内容：\n" + xlsxText2.substring(0, Math.min(200, xlsxText.length())) + "...\n");
//
//            // 拼接 Prompt
//            String prompt = "请分析以下文档内容，并总结关键信息：\n\n" + xlsxText;
//            System.out.println("提示词："+prompt);
//            // 此处可调用 Qwen API（参考前文）
//            System.out.println("最终 Prompt 长度: " + prompt.length());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}