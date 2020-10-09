package com.su.text.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gzhc365.common.exceptions.BizException;
import com.gzhc365.common.utils.ReflectTool;
import com.gzhc365.component.excel.ExcelSheetEntity;
import com.gzhc365.component.excel.MergedRegionEntity;

/**
 * excel操作类
 *
 * @author zengdi
 */
public class ExcelUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public static final String EXCEL_TYPE_XLS = "xls";
    public static final String EXCEL_TYPE_XLSX = "xlsx";

    /**
     * 创建excel文档并写入outStream输出流中
     *  @param outStream
     * @param mainTitle
     * @param param
     * @param titles
     * @param contents
     */
    public static final void buildExcel(OutputStream outStream, String mainTitle, Map<String, String> param, String[] titles, List<String[]> contents, String path) {
        buildExcel(outStream, EXCEL_TYPE_XLS, mainTitle,param, titles, contents,path);// 默认导出xls格式文档
    }

    /**
     * 创建excel文档并写入outStream输出流中
     *
     * @param outStream
     * @param mainTitle
     * @param titles
     * @param contents
     */
    public static final void buildExcel(OutputStream outStream, String fileType, String mainTitle, Map<String,String> param, String[] titles,
                                        List<String[]> contents, String path) {
        int beginRow = 0;
        try {
            // 创建FWorkbook对象
            Workbook workbook = null;
            if (EXCEL_TYPE_XLS.equals(fileType)) {
                workbook = new HSSFWorkbook();
            } else if (EXCEL_TYPE_XLSX.equals(fileType)) {
                workbook = new XSSFWorkbook();
            } else {
                throw new BizException(BizException.DEFAULT_CODE, "无效的文件类型");
            }
            // 创建HSSFSheet对象
            Sheet sheet = workbook.createSheet("Sheet1");
            /** ***************excel大标题********************* */
            if (mainTitle != null && !Objects.equals(mainTitle, "")) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(beginRow, beginRow, 0, titles.length - 1);
                sheet.addMergedRegion(cellRangeAddress); // 合并大标题
                Row row = sheet.createRow(beginRow);
                row.setHeightInPoints(24);
                Cell cell = row.createCell(beginRow);
                cell.setCellStyle(getTitleStyle(workbook));
                cell.setCellValue(mainTitle);
                beginRow = beginRow + 1;
            }

            //统计消息
            CellStyle infoStyle = getInfoStyle(workbook);
            if (param != null){
                Row row1 = sheet.createRow(beginRow);
                row1.setHeightInPoints(18);
                Cell cell = row1.createCell(0);
                cell.setCellStyle(infoStyle);
                cell.setCellValue("查询条件");

                Cell cell1 = row1.createCell(1);
                cell1.setCellStyle(infoStyle);
                cell1.setCellValue("财务开始日期：");

                Cell cell2 = row1.createCell(2);
                cell2.setCellStyle(infoStyle);
                cell2.setCellValue(param.get("startTime"));

                Cell cell3 = row1.createCell(3);
                cell3.setCellStyle(infoStyle);
                cell3.setCellValue("财务结束日期：");

                Cell cell4 = row1.createCell(4);
                cell4.setCellStyle(infoStyle);
                cell4.setCellValue(param.get("endTime"));
                beginRow++;

                Row row2 = sheet.createRow(beginRow);
                row2.setHeightInPoints(18);
                Cell cell5 = row2.createCell(1);
                cell5.setCellStyle(infoStyle);
                cell5.setCellValue("渠道总收入：");

                Cell cell6 = row2.createCell(2);
                cell6.setCellStyle(infoStyle);
                cell6.setCellValue(param.get("totalFee"));

                Cell cell7 = row2.createCell(3);
                cell7.setCellStyle(infoStyle);
                cell7.setCellValue("渠道支付笔数：");

                Cell cell8 = row2.createCell(4);
                cell8.setCellStyle(infoStyle);
                cell8.setCellValue(param.get("total"));
                beginRow++;

                Row row3 = sheet.createRow(beginRow);
                row3.setHeightInPoints(18);

                Cell cell9 = row3.createCell(0);
                cell9.setCellStyle(infoStyle);
                cell9.setCellValue("数据总览");
                beginRow++;
            }

            /** ***************以下是EXCEL第一行列标题********************* */
            CellStyle columnTopStyle = getColumnTopStyle(workbook);
            if (titles != null && titles.length > 1) {
                Row row = sheet.createRow(beginRow);
                row.setHeightInPoints(18);
                for (int i = 0; i < titles.length; i++) {
                    // sheet.setColumnWidth(i,3766);
                    Cell cell = row.createCell(i);
                    cell.setCellStyle(columnTopStyle);
                    cell.setCellValue(titles[i]);
                }
                beginRow = beginRow + 1;
            }
            /** ***************以下是EXCEL正文数据********************* */
            CellStyle contentStyle = getCellStyle(workbook);
            DataFormat format = workbook.createDataFormat();
            contentStyle.setDataFormat(format.getFormat("@"));// 设置CELL格式为文本格式
            for (int i = 0; i < contents.size(); i++) {// row
                String[] rowContent = contents.get(i);
                Row row = sheet.createRow(beginRow);
                for (int j = 0; j < titles.length; j++) { // cell
                    String content = "";
                    if (j < rowContent.length) {
                        content = rowContent[j];
                    }
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(contentStyle);
                    cell.setCellValue(content);
                }
                beginRow = beginRow + 1;
            }
            if(titles != null) {
            	for (int j = 0; j < titles.length; j++) { // cell
            		sheet.autoSizeColumn(j, true);
            	}
            }


            CellStyle auditStyle = getAuditStyle(workbook);
            int auditRow = beginRow+1;
            Row row =sheet.createRow(auditRow);
            Cell cell = row.createCell((titles != null ? titles.length - 3 : 0));
            cell.setCellStyle(auditStyle);
            cell.setCellValue("审核人:");
            if (path != null && !"".equals(path)){
                //生成图片
                InputStream is = new FileInputStream(path);
                byte[] buffer = IOUtils.toByteArray(is);
                int pictureIdx = workbook.addPicture(buffer, Workbook.PICTURE_TYPE_JPEG);
                is.close();
                CreationHelper helper = workbook.getCreationHelper();
                Drawing drawing = sheet.createDrawingPatriarch();
                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1((titles != null ? titles.length-2 : 0));
                anchor.setRow1(beginRow+1);
                Picture pict = drawing.createPicture(anchor,pictureIdx);
                pict.resize(1,2);
            }
            workbook.write(outStream);
            workbook.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 创建excel文档并写入outStream输出流中
     *
     * @param outStream
     * @param mainTitle
     * @param titles
     * @param fieldNames
     * @param data
     */
    public static final void buildExcel(OutputStream outStream, String fileType, String mainTitle, List<String> titles,
            List<String> fieldNames, List<Object> data) {
        int beginRow = 0;
        try {
            // 创建FWorkbook对象
            Workbook workbook = null;
            if (EXCEL_TYPE_XLS.equals(fileType)) {
                workbook = new HSSFWorkbook();
            } else if (EXCEL_TYPE_XLSX.equals(fileType)) {
                workbook = new XSSFWorkbook();
            } else {
                throw new BizException(BizException.DEFAULT_CODE, "无效的文件类型");
            }
            // 创建HSSFSheet对象
            Sheet sheet = workbook.createSheet("Sheet1");
            /** ***************excel大标题********************* */
            if (mainTitle != null && !Objects.equals("", mainTitle)) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(beginRow, beginRow, 0, titles.size() - 1);
                sheet.addMergedRegion(cellRangeAddress); // 合并大标题
                Row row = sheet.createRow(beginRow);
                row.setHeightInPoints(24);
                Cell cell = row.createCell(beginRow);
                cell.setCellStyle(getTitleStyle(workbook));
                cell.setCellValue(mainTitle);
                beginRow = beginRow + 1;
            }
            /** ***************以下是EXCEL第一行列标题********************* */
            CellStyle columnTopStyle = getColumnTopStyle(workbook);
            if (titles != null && titles.size() > 1) {
                Row row = sheet.createRow(beginRow);
                row.setHeightInPoints(18);
                for (int i = 0; i < titles.size(); i++) {
                    // sheet.setColumnWidth(i,3766);
                    Cell cell = row.createCell(i);
                    cell.setCellStyle(columnTopStyle);
                    cell.setCellValue(titles.get(i));
                }
                beginRow = beginRow + 1;
            }
            /** ***************以下是EXCEL正文数据********************* */
            CellStyle contentStyle = getCellStyle(workbook);
            DataFormat format = workbook.createDataFormat();
            contentStyle.setDataFormat(format.getFormat("@"));// 设置CELL格式为文本格式
            if (!data.isEmpty()) {
                ReflectTool r = new ReflectTool(data.get(0), fieldNames);
                for (int i = 0; i < data.size(); i++) {// row
                    Object rowObject = data.get(i);
                    Row row = sheet.createRow(beginRow);
                    for (int j = 0; j < titles.size(); j++) { // cell
                        String content = String.valueOf(r.get(rowObject, fieldNames.get(j)));
                        if (StringUtils.isBlank(content) || "null".equals(content)) {
                            content = "";
                        }
                        Cell cell = row.createCell(j);
                        cell.setCellStyle(contentStyle);
                        cell.setCellValue(content);
                    }
                    beginRow = beginRow + 1;
                }
            }
            if(titles != null) {
            	for (int j = 0; j < titles.size(); j++) { // cell
            		sheet.autoSizeColumn(j, true);
            	}
            }
            workbook.write(outStream);
            workbook.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    
    /**
     * 创建excel文档并写入outStream输出流中
     *
     * @param outStream
     * @param mainTitle
     * @param titles
     * @param fieldNames
     * @param data
     */
    public static final void buildExcelHisTradeReport(OutputStream outStream, String fileType, String mainTitle, 
    		String titles1, List<String> titles2, List<String> titles3, List<String> titles4, List<List<String>> contents) {
        int beginRow = 0;
        try {
            // 创建FWorkbook对象
            Workbook workbook = null;
            if (EXCEL_TYPE_XLS.equals(fileType)) {
                workbook = new HSSFWorkbook();
            } else if (EXCEL_TYPE_XLSX.equals(fileType)) {
                workbook = new XSSFWorkbook();
            } else {
                throw new BizException(BizException.DEFAULT_CODE, "无效的文件类型");
            }
            // 创建HSSFSheet对象
            Sheet sheet = workbook.createSheet("Sheet1");
            /** ***************excel大标题********************* */
            if (mainTitle != null && !Objects.equals("", mainTitle)) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(beginRow, beginRow, 0, titles3.size()-1);
                sheet.addMergedRegion(cellRangeAddress); // 合并大标题
                Row row = sheet.createRow(beginRow);
                row.setHeightInPoints(24);
                Cell cell = row.createCell(beginRow);
                cell.setCellStyle(getTitleStyle(workbook));
                cell.setCellValue(mainTitle);
                beginRow = beginRow + 1;
            }
            /** ***************以下是EXCEL第一行列标题********************* */
            if (titles1 != null && !Objects.equals("", titles1)) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(beginRow, beginRow, 0, titles3.size()-1);
                sheet.addMergedRegion(cellRangeAddress); // 合并大标题
                Row row = sheet.createRow(beginRow);
                row.setHeightInPoints(24);
                Cell cell = row.createCell(0);
                cell.setCellStyle(getTitle1Style(workbook));
                cell.setCellValue(titles1);
                beginRow = beginRow + 1;
            }
            CellStyle columnTopStyle = getColumnTopStyle(workbook);
            /** ***************以下是EXCEL第二行列标题********************* */
            if (titles2 != null && titles2.size() > 1) {
                Row row = sheet.createRow(beginRow);
                row.setHeightInPoints(18);
                for (int i = 0; i < titles2.size(); i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellStyle(columnTopStyle);
                    cell.setCellValue(titles2.get(i));
                }
                CellRangeAddress cellRangeAddress2 = new CellRangeAddress(beginRow, beginRow+2, 0, 0);
                sheet.addMergedRegion(cellRangeAddress2); // 合并大标题
                if(titles2 != null) {
                	for (int i = 0; i < (titles2.size()-1)/6; i++) {
                		sheet.addMergedRegion(new CellRangeAddress(beginRow, beginRow, 6*i+1, 6*i+6)); // 合并大标题
                	}
                }
                beginRow = beginRow + 1;
            }
            /** ***************以下是EXCEL第三行列标题********************* */
            if (titles3 != null && titles3.size() > 1) {
                Row row = sheet.createRow(beginRow);
                row.setHeightInPoints(18);
                for (int i = 1; i < titles3.size(); i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellStyle(columnTopStyle);
                    cell.setCellValue(titles3.get(i));
                }
                for (int i = 0; i < (titles3.size()-1)/2; i++) {
                	sheet.addMergedRegion(new CellRangeAddress(beginRow, beginRow, 2*i+1, 2*i+2)); // 合并大标题
                }
                beginRow = beginRow + 1;
            }
            /** ***************以下是EXCEL第四行列标题********************* */
            if (titles4 != null && titles4.size() > 1) {
                Row row = sheet.createRow(beginRow);
                row.setHeightInPoints(18);
                for (int i = 1; i < titles4.size(); i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellStyle(columnTopStyle);
                    cell.setCellValue(titles4.get(i));
                }
                beginRow = beginRow + 1;
            }
            /** ***************以下是EXCEL正文数据********************* */
            CellStyle contentStyle = getCellStyle(workbook);
            DataFormat format = workbook.createDataFormat();
            contentStyle.setDataFormat(format.getFormat("@"));// 设置CELL格式为文本格式
            for (int i = 0; i < contents.size(); i++) {// row
                List<String> rowContent = contents.get(i);
                Row row = sheet.createRow(beginRow);
                for (int j = 0; j < titles3.size(); j++) { // cell
                    String content = "";
                    if (j < rowContent.size()) {
                        content = rowContent.get(j);
                    }
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(contentStyle);
                    cell.setCellValue(content);
                }
                beginRow = beginRow + 1;
            }
            if(titles3 != null) {
            	for (int j = 0; j < titles3.size(); j++) { // cell
            		sheet.autoSizeColumn(j, true);
            	}
            }
            workbook.write(outStream);
            workbook.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 创建excel文档并写入outStream输出流中 区分sheet
     *
     * @param outStream
     * @param fileType
     * @param sheetList
     */
    public static final void buildExcelWithSheets(OutputStream outStream, String fileType, List<ExcelSheetEntity> sheetList) {
        try {
            // 创建FWorkbook对象
            Workbook workbook = null;
            if (EXCEL_TYPE_XLS.equals(fileType)) {
                workbook = new HSSFWorkbook();
            } else if (EXCEL_TYPE_XLSX.equals(fileType)) {
                workbook = new XSSFWorkbook();
            } else {
                throw new BizException(BizException.DEFAULT_CODE, "无效的文件类型");
            }

            if (sheetList == null) {
                workbook.close();
                throw new BizException(BizException.DEFAULT_CODE, "参数错误");
            }

            if (sheetList.isEmpty()) {
                workbook.createSheet("Sheet1");
            } else
                for (int i0 = 0; i0 < sheetList.size(); i0++) {
                    ExcelSheetEntity sheetEntity = sheetList.get(i0);
                    String mainTitle = sheetEntity.getMainTitle();

                    LinkedHashMap<String, String> fieldNameMap = sheetEntity.getFieldNameMap();
                    List<String> fieldNames = new ArrayList<>(fieldNameMap.size());
                    List<String> titles = new ArrayList<>(fieldNameMap.size());
                    for (Map.Entry<String, String> entry : fieldNameMap.entrySet()) {
                        fieldNames.add(entry.getKey());
                        titles.add(entry.getValue());
                    }

                    List<?> data = sheetEntity.getDatas();

                    int beginRow = 0;
                    // 创建HSSFSheet对象
                    Sheet sheet = workbook.createSheet(sheetEntity.getSheetName());
                    /** ***************excel大标题********************* */
                    if (mainTitle != null && !Objects.equals(mainTitle, "")) {
                        CellRangeAddress cellRangeAddress = new CellRangeAddress(beginRow, beginRow, 0, titles.size() - 1);
                        sheet.addMergedRegion(cellRangeAddress); // 合并大标题
                        Row row = sheet.createRow(beginRow);
                        row.setHeightInPoints(24);
                        Cell cell = row.createCell(beginRow);
                        cell.setCellStyle(getTitleStyle(workbook));
                        cell.setCellValue(mainTitle);
                        beginRow = beginRow + 1;
                    }
                    /** ***************以下是EXCEL第一行列标题********************* */
                    CellStyle columnTopStyle = getColumnTopStyle(workbook);
                    if (titles != null && titles.size() > 1) {
                        Row row = sheet.createRow(beginRow);
                        row.setHeightInPoints(18);
                        for (int i = 0; i < titles.size(); i++) {
                            // sheet.setColumnWidth(i,3766);
                            Cell cell = row.createCell(i);
                            cell.setCellStyle(columnTopStyle);
                            cell.setCellValue(titles.get(i));
                        }
                        beginRow = beginRow + 1;
                    }
                    /** ***************以下是EXCEL正文数据********************* */
                    CellStyle contentStyle = getCellStyle(workbook);
                    DataFormat format = workbook.createDataFormat();
                    contentStyle.setDataFormat(format.getFormat("@"));// 设置CELL格式为文本格式
                    if (!data.isEmpty()) {
                        ReflectTool r = new ReflectTool(data.get(0), fieldNames);
                        for (int i = 0; i < data.size(); i++) {// row
                            Object rowObject = data.get(i);
                            Row row = sheet.createRow(beginRow);
                            for (int j = 0; j < titles.size(); j++) { // cell
                                String content = String.valueOf(r.get(rowObject, fieldNames.get(j)));
                                if (StringUtils.isBlank(content) || "null".equals(content)) {
                                    content = "";
                                }
                                Cell cell = row.createCell(j);
                                cell.setCellStyle(contentStyle);
                                if(sheetEntity.getWrapTextColumnSet() != null && sheetEntity.getWrapTextColumnSet().contains(j)){
                                    cell.getCellStyle().setWrapText(true);
                                }
                                cell.setCellValue(content);
                            }
                            beginRow = beginRow + 1;
                        }
                    }
                    /** ***************合并单元格********************* */
                    if(sheetEntity.getMergedList() != null && !sheetEntity.getMergedList().isEmpty()){
                        for(MergedRegionEntity regionEntity : sheetEntity.getMergedList()){
                            CellRangeAddress cra =new CellRangeAddress(regionEntity.getStartRow(),regionEntity.getEndRow(), regionEntity.getStartColumn(), regionEntity.getEndColumn()); // 起始行, 终止行, 起始列, 终止列
                            sheet.addMergedRegion(cra);
                        }
                    }
                    for (int j = 0; j < titles.size(); j++) { // cell
                        sheet.autoSizeColumn(j, true);
                    }
                }
            workbook.write(outStream);
            outStream.flush();
            workbook.close();
        } catch (Exception e) {
            logger.error("创建excel文档(区分sheet)异常", e);
        }
    }

    /*
     * 表头单元格样式
     */
    public static CellStyle getTitleStyle(Workbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        font.setBold(true);// 加粗
        // 设置字体大小
        font.setFontHeightInPoints((short) 18);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        CellStyle style = workbook.createCellStyle();
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);
        initBaseCellStyle(style);
        return style;
    }
    
    /*
     * 表头单元格样式
     */
    public static CellStyle getTitle1Style(Workbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        //font.setBold(true);// 加粗
        // 设置字体大小
        font.setFontHeightInPoints((short) 14);
        // 设置字体名字
        font.setFontName("宋体");
        // 设置样式;
        CellStyle style = workbook.createCellStyle();
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setFont(font);
        initBaseCellStyle(style);
        return style;
    }

    /**
     * 统计信息格式
     */
    public static CellStyle getInfoStyle(Workbook workbook){
        // 设置字体
        Font font = workbook.createFont();
        //font.setBold(true);// 加粗
        // 设置字体大小
        font.setFontHeightInPoints((short) 14);
        // 设置字体名字
        font.setFontName("宋体");
        // 设置样式;
        CellStyle style = workbook.createCellStyle();
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setFont(font);
        return style;
    }

    /*
     * 列头单元格样式
     */
    public static CellStyle getColumnTopStyle(Workbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        //font.setBold(true);// 加粗
        // 设置字体大小
        font.setFontHeightInPoints((short) 14);
        // 设置字体名字
        font.setFontName("宋体");
        // 设置样式;
        CellStyle style = workbook.createCellStyle();
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 在样式用应用设置的字体;
        style.setFont(font);
        initBaseCellStyle(style);
        return style;
    }

    /*
     * 列数据信息单元格样式
     */
    public static CellStyle getCellStyle(Workbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 设置字体名字
        font.setFontName("宋体");
        // 设置样式;
        CellStyle style = workbook.createCellStyle();
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.LEFT);
        initBaseCellStyle(style);
        return style;
    }

    private static CellStyle getAuditStyle(Workbook workbook){
        // 设置字体
        Font font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 设置字体名字
        font.setFontName("宋体");
        // 设置样式;
        CellStyle style = workbook.createCellStyle();
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置水平对齐的样式为右对齐;
        style.setAlignment(HorizontalAlignment.RIGHT);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(false);// 设置自动换行
        return style;
    }

    private static void initBaseCellStyle(CellStyle cellStyle) {
        // 加边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        // 设置垂直对齐的样式为居中对齐;
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(false);// 设置自动换行
    }

    /**
     * 读取excel文档，默认读取第0个sheel，从第1行数据读起（包括标题）
     *
     * @param inputStream 文件输入流
     * @param fileType 文件类型，xls，xlsx
     * @param columnNames 列对应头信息
     * @return
     */
    public static final List<Map<String, String>> readExcel(InputStream inputStream, String fileType, String[] columnNames) {
        return readExcel(inputStream, fileType, 0, 0, null, columnNames);
    }

    /**
     * 创建excel文档并写入outStream输出流中
     */
    @SuppressWarnings({"resource" })
    public static final List<Map<String, String>> readExcel(InputStream inputStream, String fileType, Integer sheetIndex,
            Integer beginRow, Integer endRow, String[] columnNames) {
        if (sheetIndex == null) {
            sheetIndex = 0;
        }
        try {
            Workbook workbook = null;
            if (EXCEL_TYPE_XLS.equals(fileType)) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (EXCEL_TYPE_XLSX.equals(fileType)) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                throw new BizException(BizException.DEFAULT_CODE, "无效的文件类型");
            }
            List<Map<String, String>> rowList = new ArrayList<>();
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if (sheet != null) {
                /**
                 * for (Row row : sheet) { int rowNum = row.getRowNum(); if
                 * (rowNum < beginRow) { continue; } //默认读取所有行 if (endRow !=
                 * null && rowNum > endRow) { break; } Map<String, String>
                 * columnMap = new HashMap<String, String>(); for (int i = 0; i
                 * < columnNames.length; i++) { Cell cell = row.getCell(i);
                 * cell.setCellType(CellType.STRING); if (cell != null) {
                 * columnMap.put(columnNames[i], cell.getStringCellValue()); } }
                 * rowList.add(columnMap); }
                 */
                int lastRowNum = sheet.getLastRowNum()
                        + 1;/** if (beginRow > lastRowNum) { } */
                if (endRow != null && lastRowNum > endRow) {
                    lastRowNum = endRow;
                }
                for (int rowNum = beginRow; rowNum < lastRowNum; rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    int lastCellNum = row.getLastCellNum();
                    if (lastCellNum > columnNames.length) {
                        lastCellNum = columnNames.length;
                    }
                    Map<String, String> columnMap = new HashMap<>();
                    int rowBlankCellCount = 0;
                    for (int colNum = 0; colNum < lastCellNum; colNum++) {
                        Cell cell = row.getCell(colNum);
                        if (cell != null) {
                            cell.setCellType(CellType.STRING);
                            String cellVal = cell.getStringCellValue();
                            if (cellVal == null || "".equals(cellVal.trim())) {
                                rowBlankCellCount++;
                            }
                            columnMap.put(columnNames[colNum], cellVal);
                        } else {
                            rowBlankCellCount++;
                        }
                    }
                    if (rowBlankCellCount == lastCellNum) {// 无效空行
                        break;
                    }
                    rowList.add(columnMap);
                }
            }
            return rowList;
        } catch (Exception e) {
            throw new BizException(BizException.DEFAULT_CODE, "解析EXCEL文件异常", e);
        }
    }

    public static boolean isNumeric(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        final String number = "0123456789.";
        for (int i = 0; i < str.length(); i++) {
            if (number.indexOf(str.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }

}
