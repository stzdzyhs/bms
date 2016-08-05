package com.db.bms.utils.poi;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.db.bms.utils.StringUtils;



public class WriteExcel {
	private static XSSFWorkbook xwb = new XSSFWorkbook();
	private static XSSFSheet xsheet = xwb.createSheet();
	private static HSSFWorkbook hwb = new HSSFWorkbook();
	private static HSSFSheet hsheet = hwb.createSheet();
	//导出excel 2007
	public  void exportToXlsx(HttpServletResponse response,String tjName,String tjType,String header, String[] body, String separator){
		try {
			xwb = new XSSFWorkbook();
			xsheet = xwb.createSheet();			
			// 设定ColumnHeaders
			String [] headers = header.split(",");
			ExportExcelUtil util = new ExportExcelUtil(xwb, xsheet, headers.length);
			// 设置Excel标题
			util.createHeadX(tjType == null ? tjName : tjName+"/"+tjType, headers.length-1, Short.valueOf("600"));
			// 设置表头
			util.createColumHeaderX(headers, 1, (short)500, (short)15);
			if(body.length > 0){
				 XSSFCellStyle cellStyle = util.createCellStyleX((short)14);
				 for(int i=0 ; i< body.length ;i++){
					 XSSFRow row = util.createXSSFRowX(i+2, (short)450);
					 String[] bodyTempArr = body[i].split(separator);// 文本
					 for(int j = 0;j<bodyTempArr.length;j++){
						 //if(!StringUtils.isEmpty(bodyTempArr[j])){
							 util.cteateDataCellX(row, j, cellStyle, bodyTempArr[j]);
						 //}						 
					 }
				 }
			}
			ServletOutputStream output;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			String fileName = (new SimpleDateFormat("yyyyMMddHHmm")).format(new Date());
			
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;Filename="+fileName+".xlsx");
			response.setCharacterEncoding("UTF-8");		  
			
			output = response.getOutputStream();
			util.outputExcelX(out);
			output.write(out.toByteArray());
			output.flush();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}	 
	} 
		
	//导出excel 2003
	public  void exportToXls(HttpServletResponse response,String tjName,String tjType,String header, String[] body, String separator){
			try {
				hwb = new HSSFWorkbook();
				hsheet = hwb.createSheet();			
				// 设定ColumnHeaders
				String [] headers = header.split(",");
				ExportExcelUtil util = new ExportExcelUtil(hwb, hsheet, headers.length);
				// 设置Excel标题
				util.createHeadH(tjType == null ? tjName : tjName+"/"+tjType, headers.length-1, Short.valueOf("600"));
				// 设置表头
				util.createColumHeaderH(headers, 1, (short)500, (short)15);
				if(body.length > 0){
					 HSSFCellStyle cellStyle = util.createCellStyleH((short)14);
					 for(int i=0 ; i< body.length ;i++){
						 HSSFRow row = util.createXSSFRowH(i+2, (short)450);
						 String[] bodyTempArr = body[i].split(separator);// 文本
						 for(int j = 0;j<bodyTempArr.length;j++){
							// if(!StringUtils.isEmpty(bodyTempArr[j])){
								 util.cteateDataCellH(row, j, cellStyle, bodyTempArr[j]);
							// }
						 }
					 }
				}
				ServletOutputStream output;
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				
				response.setContentType("application/ms-excel");
				String fileName = (new SimpleDateFormat("yyyyMMddHHmm")).format(new Date());
				
				response.setHeader("Content-Disposition", "attachment;Filename="+ fileName +".xls");
				response.setCharacterEncoding("UTF-8");				
				output = response.getOutputStream();
				util.outputExcelH(out);
				output.write(out.toByteArray());
				output.flush();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}	 
	}
}
