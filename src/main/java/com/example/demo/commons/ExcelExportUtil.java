package com.example.demo.commons;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Excel导出组件
 * @author 00236640
 * @version $Id: ExcelImportUtil.java, v 0.1 2015年8月17日 上午11:44:13 00236640 Exp $*/


public class ExcelExportUtil {
    
    private static final String SHEET_NAME ="sheet1";

    private static final String TYPE_XLS = "xls";

    private static final String TYPE_XLSX = "xlsx";
    
    private static final String TYPE_CSV = "csv";
    
    private static final String DATE_FORMAT = "yyyy-MM-dd";

/**
     * 数据写入excel文件
     * @param fileName Excel文件名
     * @param labels excel文件的表头显示名
     * @param fields excel文件的数据字段名
     * @param dataList 待写入数据
     * @param sheetName 工作表的名称
     * @return
     * @throws IOException*/


    public static Workbook createExcelByMap(String fileName, String[] labels, String[] fields, List<Map<String, Object>> dataList,
                                            String sheetName, boolean noData) throws IOException {
        return createExcelByMap(fileName, Arrays.asList(labels), Arrays.asList(fields),dataList,sheetName,noData);
    }

/*****************************/

    public static SXSSFWorkbook createSXExcelByMap(String fileName, String[] labels, String[] fields, List<Map<String, Object>> dataList,
                                                   String sheetName, boolean noData) throws IOException {
        return createSXSSFExcelByMap(fileName, Arrays.asList(labels), Arrays.asList(fields),dataList,sheetName,noData);
    }
/******************************/

    
    public static Workbook createExcelByMap(String fileName, String[] labels, String[] fields, List<Map<String, Object>> dataList,
                                            String sheetName) throws IOException {
        return createExcelByMap(fileName, labels,fields, dataList, sheetName,false);
    }
    
    public static Workbook createExcelByMapString(String fileName, String[] labels, String[] fields, List<Map<String, String>> dataList,
                                                  String sheetName) throws IOException {
        return createExcelByMapString(fileName, Arrays.asList(labels), Arrays.asList(fields),dataList,sheetName,false);
    }
/**
     * 数据写入excel文件
     * @param fileName Excel文件名
     * @param labels excel文件的表头显示名
     * @param fields excel文件的数据字段名
     * @param dataList 待写入数据
     * @param sheetName 工作表的名称
     * @return
     * @throws IOException*/


    public static Workbook createExcelByVo(String fileName, String[] labels, String[] fields, List dataList,
                                           String sheetName) throws Exception {
        return createExcelByVo(fileName, Arrays.asList(labels), Arrays.asList(fields),dataList,sheetName,false);
    }
    
/**
     * 数据写入excel文件
     * @param fileName Excel文件名
     * @param labels excel文件的表头显示名
     * @param fields excel文件的数据字段名
     * @param dataList 待写入数据
     * @param sheetName 工作表的名称
     * @return
     * @throws IOException*/



    public static SXSSFWorkbook  createSXSSFExcelByMap(String fileName, List<String> labels, List<String> fields, List<Map<String, Object>> dataList,
                                                       String sheetName, boolean noData) throws IOException {
        XSSFWorkbook workbook1 = createSSFExcelCommon(fileName, labels, fields,dataList,sheetName,noData);

        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(workbook1, 100);
        Sheet first = sxssfWorkbook.getSheetAt(0);
        // 工作表
        Sheet sheet = sxssfWorkbook.getSheetAt(0);
        // 工作表的每一行
        Row row = null;
        // 每一行中的每一个单元格
        Cell cell = null;

        CellStyle cellStyle = sxssfWorkbook.createCellStyle();
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        // 写数据
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> dataMap = dataList.get(i);
            // 从第二行开始创建数据行
            row = sheet.createRow(i + 1);
            // 每行单元格的索引位置
            int index = 0;
            for(String key:fields){
                Object value = dataMap.get(key);
                if (null == value) {
                    value = "";
                }
                if (value instanceof String) {
                    cell = row.createCell(index,Cell.CELL_TYPE_STRING);
                    cell.setCellValue((String) value);
                } else if (value instanceof Date) {
                    cell = row.createCell(index,Cell.CELL_TYPE_NUMERIC);
                    Date date = (Date) value;
                    cell.setCellValue(new SimpleDateFormat(DATE_FORMAT).format(date));
                } else if (value instanceof Boolean) {
                    cell = row.createCell(index,Cell.CELL_TYPE_BOOLEAN);
                    cell.setCellValue((Boolean) value);
                }else if(value instanceof Integer){
                    cell = row.createCell(index,Cell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(Integer.valueOf(value.toString()));
                }else if (value instanceof Character || value instanceof Short
                        || value instanceof Long || value instanceof Double || value instanceof BigDecimal) {
                    cell = row.createCell(index,Cell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(Double.parseDouble(value.toString()));
                    cell.setCellStyle(cellStyle);
                } else {
                    cell = row.createCell(index);
                    cell.setCellValue(value.toString());
                }
//                eidtCell(value, cell);
                index++;
            }
        }

        return sxssfWorkbook;
    }



    public static Workbook createExcelByMap(String fileName, List<String> labels, List<String> fields, List<Map<String, Object>> dataList,
                                            String sheetName, boolean noData) throws IOException {
        Workbook workbook = createExcelCommon(fileName, labels, fields,dataList,sheetName,noData);
        if(null==workbook){
            return workbook;
        }
        // 工作表
        Sheet sheet = workbook.getSheetAt(0);
        // 工作表的每一行
        Row row = null;
        // 每一行中的每一个单元格
        Cell cell = null;
        
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        // 写数据
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> dataMap = dataList.get(i);
            // 从第二行开始创建数据行
            row = sheet.createRow(i + 1);
            // 每行单元格的索引位置
            int index = 0;
            for(String key:fields){
                Object value = dataMap.get(key);
                if (null == value) {
                    value = "";
                }
                if (value instanceof String) {
                    cell = row.createCell(index,Cell.CELL_TYPE_STRING);
                    cell.setCellValue((String) value);
                } else if (value instanceof Date) {
                    cell = row.createCell(index,Cell.CELL_TYPE_NUMERIC);
                    Date date = (Date) value;
                    cell.setCellValue(new SimpleDateFormat(DATE_FORMAT).format(date));
                } else if (value instanceof Boolean) {
                    cell = row.createCell(index,Cell.CELL_TYPE_BOOLEAN);
                    cell.setCellValue((Boolean) value);
                }else if(value instanceof Integer){
                    cell = row.createCell(index,Cell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(Integer.valueOf(value.toString()));
                }else if (value instanceof Character || value instanceof Short
                        || value instanceof Long || value instanceof Double || value instanceof BigDecimal) {
                    cell = row.createCell(index,Cell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(Double.parseDouble(value.toString()));
                    cell.setCellStyle(cellStyle);
                } else {
                    cell = row.createCell(index);
                    cell.setCellValue(value.toString());
                }
//                eidtCell(value, cell);
                index++;
            }
        }
        return workbook;
    }
    
    public static Workbook createExcelByMapString(String fileName, List<String> labels, List<String> fields, List<Map<String, String>> dataList,
                                                  String sheetName, boolean noData) throws IOException {
        Workbook workbook = createExcelCommon(fileName, labels, fields,dataList,sheetName,noData);
        if(null==workbook){
            return workbook;
        }
        // 工作表
        Sheet sheet = workbook.getSheetAt(0);
        // 工作表的每一行
        Row row = null;
        // 每一行中的每一个单元格
        Cell cell = null;
        // 写数据
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, String> dataMap = dataList.get(i);
            // 从第二行开始创建数据行
            row = sheet.createRow(i + 1);
            // 每行单元格的索引位置
            int index = 0;
            for(String key:fields){
            	String value = dataMap.get(key);
                cell = row.createCell(index);
                eidtCell(value, cell);
                index++;
            }
        }
        return workbook;
    }
/**
     * 数据写入excel文件
     * @param fileName Excel文件名
     * @param labels excel文件的表头显示名
     * @param fields excel文件的数据字段名
     * @param dataList 待写入数据
     * @param sheetName 工作表的名称
     * @return
     * @throws IOException*/


    public static Workbook createExcelByVo(String fileName, List<String> labels, List<String> fields, List dataList,
                                           String sheetName, boolean noData) throws Exception {
        Workbook workbook = createExcelCommon(fileName, labels, fields,dataList,sheetName,noData);
        if(null==workbook){
            return workbook;
        }
        Sheet sheet = workbook.getSheetAt(0);
        // 工作表的每一行
        Row row = null;
        // 每一行中的每一个单元格
        Cell cell = null;
        
        // 写数据
        for (int i = 0; i < dataList.size(); i++) {
            // 从第二行开始创建数据行
            row = sheet.createRow(i + 1);
            // 数据对象
            Object bean = dataList.get(i);
            // 获取数据对象的所有属性
            Field[] fieldList = bean.getClass().getDeclaredFields();
            // 每行单元格的索引位置
            int index = 0;
            for(String key:fields){
                for(Field field:fieldList){
                    if(key.equals(field.getName())){
                        field.setAccessible(true);
                        Object value =field.get(bean);
                        cell = row.createCell(index);
                        eidtCell(value,cell);
                        index++;
                        break;
                    }
                }
            }
        }
        return workbook;
    }
/*
*
     * 设置工作表的名称和数据的字段名
     * @param fileName
     * @param labels
     * @param fields
     * @param dataList
     * @param sheetName
     * @return
     * @throws IOException*/


    private static XSSFWorkbook createSSFExcelCommon(String fileName, List<String> labels, List<String> fields, List dataList,
                                                     String sheetName, boolean noData) throws IOException {
        if(!check(labels, fields,dataList,noData)){
            return null;
        }
        // 获取文件后缀名
        String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 创建工作薄
        //XSSFWorkbook workbook = new XSSFWorkbook(extensionName);
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 创建工作表
        Sheet sheet = workbook.createSheet();
        // 如果不指定工作薄名称、则默认设置为'sheet1'
        if (null == sheetName || "".equals(sheetName.trim())) {
            sheetName = SHEET_NAME;
        }
        // 设置工作表名
        workbook.setSheetName(0, sheetName);
        // 工作表中创建第一行
        Row row = sheet.createRow(0);
        // 设置标题
        CellStyle style = workbook.createCellStyle();
        Font font =  workbook.createFont();
        // 设置单元格粗体
        font.setBold(true);
        style.setFont(font);

        Cell cell = null;
        for (int i=0; i<labels.size();i++) {
            cell = row.createCell(i);
            cell.setCellValue(labels.get(i));
            cell.setCellStyle(style);
        }
        return workbook;
    }


    private static Workbook createExcelCommon(String fileName, List<String> labels, List<String> fields, List dataList,
                                              String sheetName, boolean noData) throws IOException {
        if(!check(labels, fields,dataList,noData)){
            return null;
        }
        // 获取文件后缀名
        String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);

        // 创建工作薄
        Workbook workbook = createWorkbook(extensionName);

        // 创建工作表
        Sheet sheet = workbook.createSheet();
        // 如果不指定工作薄名称、则默认设置为'sheet1'
        if (null == sheetName || "".equals(sheetName.trim())) {
            sheetName = SHEET_NAME;
        }
        // 设置工作表名
        workbook.setSheetName(0, sheetName);
        // 工作表中创建第一行
        Row row = sheet.createRow(0);
        // 设置标题
        CellStyle style = workbook.createCellStyle();
        Font font =  workbook.createFont();
        // 设置单元格粗体
        font.setBold(true);
        style.setFont(font);
        
        Cell cell = null;
        for (int i=0; i<labels.size();i++) {
            cell = row.createCell(i);
            cell.setCellValue(labels.get(i));
            cell.setCellStyle(style);
        }
        return workbook;
    }
/*

*
     * 设置每个单元格
     * @param value
     * @param cell
*/


    private static void eidtCell(Object value, Cell cell){
        if (null == value) {
            value = "";
        }
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Date) {
            Date date = (Date) value;
            cell.setCellValue(new SimpleDateFormat(DATE_FORMAT).format(date));
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Character || value instanceof Short || value instanceof Integer
                || value instanceof Long || value instanceof Double || value instanceof BigDecimal) {
            cell.setCellValue(value.toString());
         
            
        } else {
            cell.setCellValue(value.toString());
        }
    }

/**
     * 创建空的Workbook对象
     * @param extensionName
     * @return*/


    private static Workbook createWorkbook(String extensionName) {
        // 创建工作薄
        Workbook workbook = null;
        if (TYPE_XLS.equals(extensionName.toLowerCase())) {
            // 创建xls工作薄
            workbook = new HSSFWorkbook();
        } else if (TYPE_XLSX.equals(extensionName.toLowerCase())) {
            // 创建xlsx工作薄
            workbook = new XSSFWorkbook();
        }else if (TYPE_CSV.equals(extensionName.toLowerCase())) {
            workbook = new HSSFWorkbook();
        }
        return workbook;
    }
    
/**
     * 标题、字段、及数据非空校验
     * @param labels 显示标题
     * @param fields 显示字段
     * @param dataList 传入数据
     * @return*/


    private static boolean check(List<String> labels, List<String> fields, List dataList, boolean nodata) {
        // 如果没有传入显示标题、则操作结束
        if (null == labels || labels.size() == 0) {
            return false;
        }
        
        // 如果没有传入显示字段、则操作结束
        if (null == fields || fields.size() == 0) {
            return false;
        }
        
        // 如果没有传入数据、则操作结束
        if (null == dataList) {
            return false;
        }
        if(dataList.size()==0){
        	return nodata;
        }
        return true;
    }
}
