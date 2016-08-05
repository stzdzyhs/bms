package com.db.bms.utils.poi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * POI导出报表的工具类
 *
 */
public class ExportExcelUtil {
	
	private XSSFWorkbook xwb = null;
	private XSSFSheet xsheet = null;	
	private HSSFWorkbook hwb = null;
	private HSSFSheet hsheet = null;
	
	// 记录Excel表格的总列数
	private Integer colNumber = 0;
	
	/**
	 * @param wb
	 * @param sheet
	 */
	public ExportExcelUtil(XSSFWorkbook wb, XSSFSheet sheet ,Integer colNumber) {
		super();
		this.xwb = wb;
		this.xsheet = sheet;
		this.colNumber = colNumber;
	}
	
	public ExportExcelUtil(HSSFWorkbook wb,HSSFSheet sheet,Integer colNumber){
		super();
		this.hwb = wb;
		this.hsheet = sheet;
		this.colNumber = colNumber;
	}	

	public XSSFWorkbook getXwb() {
		return xwb;
	}

	public void setXwb(XSSFWorkbook xwb) {
		this.xwb = xwb;
	}

	public XSSFSheet getXsheet() {
		return xsheet;
	}

	public void setXsheet(XSSFSheet xsheet) {
		this.xsheet = xsheet;
	}

	public HSSFWorkbook getHwb() {
		return hwb;
	}

	public void setHwb(HSSFWorkbook hwb) {
		this.hwb = hwb;
	}

	public HSSFSheet getHsheet() {
		return hsheet;
	}

	public void setHsheet(HSSFSheet hsheet) {
		this.hsheet = hsheet;
	}

//--------------------------------------Ｘ---------------------------------------------
	
	/**
	 * 创建通用EXCEL头部
	 * @param headString 头部显示的字符
	 * @param colIndex 该报表的列数
	 * @param rowHeight 表头的行高
	 */
	public void createHeadX(String headString, int colIndex , short rowHeight) {
		// 创建第一行
		XSSFRow row = xsheet.createRow(0);
		// 设置行高
		if(rowHeight > 1) row.setHeight(rowHeight);
		// 指定合并区域
		xsheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colIndex));
		// 设置第一行
		XSSFCell cell = row.createCell(0);
		// 定义单元格为字符串类型 -- 显示字符串类型
		cell.setCellType(XSSFCell.CELL_TYPE_STRING); 
		// 设置单元格内容
		cell.setCellValue(new XSSFRichTextString(headString));
		// 设置单元格样式
		XSSFCellStyle cellStyle = xwb.createCellStyle();
		// 指定单元格水平居中对齐
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); 
		// 指定单元格垂直居中对齐
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 指定单元格自动换行
		cellStyle.setWrapText(true);
		// 设置单元格字体
		XSSFFont font = xwb.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		cellStyle.setFont(font);		
		cell.setCellStyle(cellStyle);
	}
	
	/**
	 * 创建通用报表的统计汇总行
	 * @param params 统计条件数组
	 * @param colIndex 需要合并到的列索引
	 */
	public void createCountRowX(String [] params, String countName , 
			int colIndex, int rowIndex, short rowHeight) {
		// 创建统计行
		XSSFRow rowCount = xsheet.createRow(rowIndex);
		// 设置行高 -- Excel默认高度
		if(rowHeight > 1) rowCount.setHeight(rowHeight);
		// 取得行第一列
		XSSFCell cell = rowCount.createCell(0);
		// 设置列内容
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new XSSFRichTextString(countName));
		// 指定合并区域
		xsheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, colIndex));
		// 设置单元格样式
		XSSFCellStyle cellStyle = xwb.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定单元格自动换行
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		// 设置单元格字体
		XSSFFont font = xwb.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		cellStyle.setFont(font);
		// 设置统计行的文字列
		cell.setCellStyle(cellStyle);
		
		// 设置统计行的数据列
		if(params != null && params.length > 0){
			int eachNum = Math.min(colNumber-colIndex, params.length+1);
			// 对统计列数据进行设置值
			for(int i=1 ; i < eachNum ; i++){
				cell = rowCount.createCell(colIndex + i);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new XSSFRichTextString(params[i-1]));
			}
		}
	}
	
	/**
	 * 创建内容单元格 -- 创建的只是单个单元格
	 * @param row XSSFRow
	 * @param col short型的列索引
	 * @param align 对齐方式
	 * @param val 列值
	 */
	public void cteateDataCellX(XSSFRow row ,int colIndex, 
								XSSFCellStyle cellstyle, String val) {
		XSSFCell cell = row.createCell(colIndex);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(cellstyle);
		cell.setCellValue(new XSSFRichTextString(val));
	}
	
	/**
	 * 设置报表标题
	 * @param columHeader 标题字符串数组
	 * @param rownum 列表标题放第几行
	 * @param headerHeight 表头行高
	 * @param fontHeight 表头字体高度
	 */
	public void createColumHeaderX(String[] columHeader ,int rowIndex ,
					short headerHeight ,short fontHeight) {
		// 设置列头
		XSSFRow rowCH = createXSSFRowX(rowIndex, headerHeight);
		// 创建表头
		XSSFCell cell3 = null;
		XSSFCellStyle cellStyle = createCellStyleX(fontHeight);
		for (int i = 0; i < columHeader.length; i++) {
			cell3 = rowCH.createCell(i);
			// 设置列宽
			xsheet.setColumnWidth(i, 4400);
			cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell3.setCellStyle(cellStyle);
			cell3.setCellValue(new XSSFRichTextString(columHeader[i]));
		}
	}
	
	/**
	 * 创建Excel单元格样式
	 * @param align
	 * @param fontHeight
	 * @return
	 */
	public XSSFCellStyle createCellStyleX(short fontHeight) {
		// 设置表头的样式
		XSSFCellStyle cellStyle = xwb.createCellStyle();
		// 指定单元格居中对齐
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); 
		// 指定单元格垂直居中对齐
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 指定单元格自动换行
		cellStyle.setWrapText(true);
		// 设置单元格字体
		XSSFFont font = xwb.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);	
		font.setFontName("宋体");
//		if(fontHeight > 1) font.setFontHeight(fontHeight);
		cellStyle.setFont(font);
		
		// 设置单元格的边框
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);

		// 设置单元格背景色
		cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		return cellStyle;
	}
	
	/**
	 * 创建行并设置行高与字体大小等属性
	 * @param rowIndex
	 * @param headerHeight
	 * @param fontHeight
	 * @return
	 */
	public XSSFRow createXSSFRowX(int rowIndex, short headerHeight){
		XSSFRow rowCH = xsheet.createRow(rowIndex);
		if(headerHeight > 1) rowCH.setHeight(headerHeight);
		return rowCH;
	}
	
	/**
	 * 创建分组的Excel表格数据
	 * @param dataMap
	 * @param rowStart
	 * @param colStart
	 * @param groupNum
	 */
	public void createGroupSellX(Map<String,List<String []>> dataMap , int rowStart,
				int colStart){
		if(dataMap != null){
			// 创建表格的样式
			XSSFCellStyle cellStyle = xwb.createCellStyle();
			cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
			cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
			cellStyle.setWrapText(true);// 指定单元格自动换行
			
			// 设置单元格边框
			cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
			cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
			
			//记录行和列
			XSSFRow rowCH = null;
			XSSFCell cell = null;
			for(Map.Entry<String, List<String []>> entry : dataMap.entrySet()){
				List<String []> data = entry.getValue();
				for(int i=0;i<data.size();i++){
					// 创建单元行数据
					rowCH = xsheet.createRow(rowStart+i);
					String [] values = data.get(i);
					for(int k=0;k<values.length;k++){
						// 第一列填充的是分组信息,数据填充从第二列开始
						cell = rowCH.createCell(colStart+k+1);
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(new XSSFRichTextString(values[k]));
						cell.setCellStyle(cellStyle);
					}
				}
				// 合并行,设置分组信息
				xsheet.addMergedRegion(new CellRangeAddress(rowStart, 
									rowStart+data.size()-1, colStart, colStart));
				rowCH = xsheet.getRow(rowStart);
				cell = rowCH.createCell(colStart);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new XSSFRichTextString(entry.getKey()));
				cell.setCellStyle(cellStyle);
				// 重置开始行
				rowStart += data.size();
			}
		}
	}
	
	/**
	 * 输入EXCEL文件
	 * @param fileName 文件名
	 */
	public void outputExcelX(OutputStream output) {
		try {
			if(output != null) xwb.write(output);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(output != null)
				try {
					output.close();
				} catch (IOException e) {
				}
		}
	}
	
	//--------------------------------------Ｈ---------------------------------------------
	/**
	 * 创建通用EXCEL头部
	 * @param headString 头部显示的字符
	 * @param colIndex 该报表的列数
	 * @param rowHeight 表头的行高
	 */
	public void createHeadH(String headString, int colIndex , short rowHeight){
		HSSFRow row = hsheet.createRow(0);
		if(rowHeight > 1) row.setHeight(rowHeight);
		hsheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colIndex));
		HSSFCell cell = row.createCell(0);
		// 定义单元格为字符串类型 -- 显示字符串类型
		cell.setCellType(XSSFCell.CELL_TYPE_STRING); 
		// 设置单元格内容
		cell.setCellValue(new HSSFRichTextString(headString));
		// 设置单元格样式
		HSSFCellStyle cellStyle = hwb.createCellStyle();
		// 指定单元格水平居中对齐
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
		// 指定单元格垂直居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 指定单元格自动换行
		cellStyle.setWrapText(true);
		// 设置单元格字体
		HSSFFont font = hwb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		cellStyle.setFont(font);		
		cell.setCellStyle(cellStyle);
		
	}
	

	
	/**
	 * 创建通用报表的统计汇总行
	 * @param params 统计条件数组
	 * @param colIndex 需要合并到的列索引
	 */
	public void createCountRowH(String [] params, String countName , 
			int colIndex, int rowIndex, short rowHeight) {
		// 创建统计行
		HSSFRow rowCount = hsheet.createRow(rowIndex);
		// 设置行高 -- Excel默认高度
		if(rowHeight > 1) rowCount.setHeight(rowHeight);
		// 取得行第一列
		HSSFCell cell = rowCount.createCell(0);
		// 设置列内容
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(countName));
		// 指定合并区域
		hsheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, colIndex));
		// 设置单元格样式
		HSSFCellStyle cellStyle = hwb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定单元格自动换行
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); 
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置单元格字体
		HSSFFont font = hwb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		cellStyle.setFont(font);
		// 设置统计行的文字列
		cell.setCellStyle(cellStyle);
		
		// 设置统计行的数据列
		if(params != null && params.length > 0){
			int eachNum = Math.min(colNumber-colIndex, params.length+1);
			// 对统计列数据进行设置值
			for(int i=1 ; i < eachNum ; i++){
				cell = rowCount.createCell(colIndex + i);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(params[i-1]));
			}
		}
	}
	
	
	/**
	 * 设置报表标题
	 * @param columHeader 标题字符串数组
	 * @param rownum 列表标题放第几行
	 * @param headerHeight 表头行高
	 * @param fontHeight 表头字体高度
	 */
	public void createColumHeaderH(String[] columHeader ,int rowIndex ,
					short headerHeight ,short fontHeight) {
		// 设置列头
		HSSFRow rowCH = createXSSFRowH(rowIndex, headerHeight);
		// 创建表头
		HSSFCell cell3 = null;
		HSSFCellStyle cellStyle = createCellStyleH(fontHeight);
		for (int i = 0; i < columHeader.length; i++) {
			cell3 = rowCH.createCell(i);
			// 设置列宽
			hsheet.setColumnWidth(i, 4400);
			cell3.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell3.setCellStyle(cellStyle);
			cell3.setCellValue(new HSSFRichTextString(columHeader[i]));
		}
	}

	
	/**
	 * 创建内容单元格 -- 创建的只是单个单元格
	 * @param row HSSFRow
	 * @param col short型的列索引
	 * @param align 对齐方式
	 * @param val 列值
	 */
	public void cteateDataCellH(HSSFRow row ,int colIndex,HSSFCellStyle cellstyle, String val) {
		HSSFCell cell = row.createCell(colIndex);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(cellstyle);
		cell.setCellValue(new HSSFRichTextString(val));
	}
	
	
	
	/**
	 * 创建Excel单元格样式
	 * @param align
	 * @param fontHeight
	 * @return
	 */
	public HSSFCellStyle createCellStyleH(short fontHeight) {
		// 设置表头的样式
		HSSFCellStyle cellStyle = hwb.createCellStyle();
		// 指定单元格居中对齐
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
		// 指定单元格垂直居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 指定单元格自动换行
		cellStyle.setWrapText(true);
		// 设置单元格字体
		HSSFFont font = hwb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);	
		font.setFontName("宋体");
//		if(fontHeight > 1) font.setFontHeight(fontHeight);
		cellStyle.setFont(font);
		
		// 设置单元格的边框
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); 
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

		// 设置单元格背景色
		cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return cellStyle;
	}
	
	
	
	/**
	 * 创建行并设置行高与字体大小等属性
	 * @param rowIndex
	 * @param headerHeight
	 * @param fontHeight
	 * @return
	 */
	public HSSFRow createXSSFRowH(int rowIndex, short headerHeight){
		HSSFRow rowCH = hsheet.createRow(rowIndex);
		if(headerHeight > 1) rowCH.setHeight(headerHeight);
		return rowCH;
	}
	
	
	/**
	 * 创建分组的Excel表格数据
	 * @param dataMap
	 * @param rowStart
	 * @param colStart
	 * @param groupNum
	 */
	public void createGroupSellH(Map<String,List<String []>> dataMap , int rowStart,
				int colStart){
		if(dataMap != null){
			// 创建表格的样式
			HSSFCellStyle cellStyle = hwb.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
			cellStyle.setWrapText(true);// 指定单元格自动换行
			
			// 设置单元格边框
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); 
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			
			//记录行和列
			HSSFRow rowCH = null;
			HSSFCell cell = null;
			for(Map.Entry<String, List<String []>> entry : dataMap.entrySet()){
				List<String []> data = entry.getValue();
				for(int i=0;i<data.size();i++){
					// 创建单元行数据
					rowCH = hsheet.createRow(rowStart+i);
					String [] values = data.get(i);
					for(int k=0;k<values.length;k++){
						// 第一列填充的是分组信息,数据填充从第二列开始
						cell = rowCH.createCell(colStart+k+1);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(new HSSFRichTextString(values[k]));
						cell.setCellStyle(cellStyle);
					}
				}
				// 合并行,设置分组信息
				hsheet.addMergedRegion(new CellRangeAddress(rowStart, 
									rowStart+data.size()-1, colStart, colStart));
				rowCH = hsheet.getRow(rowStart);
				cell = rowCH.createCell(colStart);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString(entry.getKey()));
				cell.setCellStyle(cellStyle);
				// 重置开始行
				rowStart += data.size();
			}
		}
	}
	
	/**
	 * 输入EXCEL文件
	 * 
	 */
	public void outputExcelH(OutputStream output) {
		try {
			if(output != null) hwb.write(output);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(output != null)
				try {
					output.close();
				} catch (IOException e) {
				}
		}
	}
}
