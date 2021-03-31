package com.north.util;

import cn.afterturn.easypoi.csv.CsvExportUtil;
import cn.afterturn.easypoi.csv.entity.CsvExportParams;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.afterturn.easypoi.handler.inter.IWriter;
import com.north.base.exception.NorthImportException;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-26
 */
public class ExcelUtil {

    private final static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    private static final int fastExcelNum = 50000;
    private static final int bigExcelNum = 500000;


    /**
     * 导出，根据数据量不同用不同的文件格式导出
     *
     * @param title
     * @param sheetName
     * @param entity
     * @param list
     * @return
     * @throws IOException
     */
    public static byte[] export(String title, String sheetName, List<ExcelExportEntity> entity, List<?> list) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Workbook workbook = null;
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        if (list.size() < fastExcelNum) {
            //5W以为的数据用excel快速导出
            workbook = ExcelExportUtil.exportExcel(exportParams, entity, list);
            workbook.write(outputStream);
        } else if (list.size() < bigExcelNum) {
            //50W以内的数据用excel大容量导出
            IWriter<Workbook> writer = ExcelExportUtil.exportBigExcel(exportParams, entity);
            writer.write(list);
            workbook = writer.get();
            workbook.write(outputStream);
        } else {
            //大于50W的数据用CSV导出
            CsvExportParams csvExportParams = new CsvExportParams();
            csvExportParams.setEncoding(CsvExportParams.GBK);
            IWriter<Void> writer = CsvExportUtil.exportCsv(csvExportParams, entity, outputStream);
            writer.write(list);
        }
        return outputStream.toByteArray();
    }

    /**
     * 通过数据量获取文件后缀
     *
     * @param dataSize
     * @return
     */
    public static String getSuffix(int dataSize) {
        if (dataSize < fastExcelNum) {
            return ".xlsx";
        } else if (dataSize < bigExcelNum) {
            return ".xlsx";
        } else {
            return ".csv";
        }
    }

    /**
     * 导入不校验数据
     *
     * @param inputStream
     * @return
     */
    public static List<Map<String, Object>> importExcel(InputStream inputStream) {
        ImportParams importParams = new ImportParams();
        ExcelImportResult<Map<String, Object>> importResult = null;
        try {
            importResult = ExcelImportUtil.importExcelMore(inputStream, Map.class, importParams);
        } catch (Exception e) {
            logger.error("导入异常", e);
            throw new NorthImportException();
        }
        return importResult.getList();
    }

    /**
     * 导入并校验数据
     *
     * @param inputStream
     * @param verifyHandler
     * @param <T>
     * @return
     */
    public static <T extends IExcelVerifyHandler> List<Map<String, Object>> importExcel(InputStream inputStream, T verifyHandler) {
        ImportParams importParams = new ImportParams();
        importParams.setNeedVerify(true);
        importParams.setVerifyHandler(verifyHandler);

        ExcelImportResult<Map<String, Object>> importResult = null;
        try {
            importResult = ExcelImportUtil.importExcelMore(inputStream, Map.class, importParams);
        } catch (Exception e) {
            logger.error("导入异常", e);
            throw new NorthImportException();
        }
        if (importResult.isVerifyFail()) {
            throw new NorthImportException("导入异常,输出错误Excel", importResult);
        }
        return importResult.getList();
    }

    /**
     * 获取解析的Map中的String
     *
     * @param map
     * @param name
     * @return
     */
    public static String getMapStringValue(Map<String, Object> map, String name) {
        Object o = map.get(name);
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    /**
     * 获取解析的Map中的整形
     *
     * @param map
     * @param name
     * @return
     */
    public static Long getMapLongValue(Map<String, Object> map, String name) {
        Object o = map.get(name);
        if (o == null) {
            return null;
        }
        return Long.valueOf(o.toString());
    }

    /**
     * 获取解析的Map中的浮点型
     *
     * @param map
     * @param name
     * @return
     */
    public static Double getMapDoubleValue(Map<String, Object> map, String name) {
        Object o = map.get(name);
        if (o == null) {
            return null;
        }
        return Double.valueOf(o.toString());
    }

    /**
     * 获取解析的Map中的Date
     *
     * @param map
     * @param name
     * @return
     */
    public static LocalDateTime getMapDateValue(Map<String, Object> map, String name) {
        Object o = map.get(name);
        if (o == null) {
            return null;
        }
        String expiredTimeStr = ExcelUtil.getMapStringValue(map, name);
        LocalDateTime expiredTime = LocalDateTime.parse(expiredTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return expiredTime;
    }
}
