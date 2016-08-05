package com.db.bms.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.IOUtils;

import com.db.bms.entity.LogResult;

/**
 * 文件操作的工具类
 * @date 2012-3-8
 */

public class FileUtils {

	/**
	 * 读取文本文件内容，以行的形式读取
	 * @param String  filePathAndName 带有完整绝对路径的文件名
	 * @return String 返回文本文件的内容
	 */
	public static String readFileContent(String filePathAndName)
			throws IOException {
		return readFileContent(filePathAndName, null, null, 1024);
	}

	/**
	 * 读取文本文件内容，以行的形式读取
	 * 
	 * @param String
	 *            filePathAndName 带有完整绝对路径的文件名
	 * @param String
	 *            encoding 文本文件打开的编码方式 例如 GBK,UTF-8
	 * @return String 返回文本文件的内容
	 */
	public static String readFileContent(String filePathAndName, String encoding)
			throws IOException {
		return readFileContent(filePathAndName, encoding, null, 1024);
	}

	/**
	 * 读取文本文件内容，以行的形式读取
	 * 
	 * @param String
	 *            filePathAndName 带有完整绝对路径的文件名
	 * @param int
	 *            bufLen 设置缓冲区大小
	 * @return String 返回文本文件的内容
	 */
	public static String readFileContent(String filePathAndName, int bufLen)
			throws IOException {
		return readFileContent(filePathAndName, null, null, bufLen);
	}

	/**
	 * 读取文本文件内容，以行的形式读取
	 * 
	 * @param String
	 *            filePathAndName 带有完整绝对路径的文件名
	 * @param String
	 *            encoding 文本文件打开的编码方式 例如 GBK,UTF-8
	 * @param String
	 *            sep 分隔符 例如：#，默认为空格
	 * @return String 返回文本文件的内容
	 */
	public static String readFileContent(String filePathAndName,
			String encoding, String sep) throws IOException {
		return readFileContent(filePathAndName, encoding, sep, 1024);
	}

	/**
	 * 读取文本文件内容，以行的形式读取
	 * 
	 * @param String
	 *            filePathAndName 带有完整绝对路径的文件名
	 * @param String
	 *            encoding 文本文件打开的编码方式 例如 GBK,UTF-8
	 * @param String
	 *            sep 分隔符 例如：#，默认为/n;
	 * @param int
	 *            bufLen 设置缓冲区大小
	 * @return String 返回文本文件的内容
	 */
	public static String readFileContent(String filePathAndName,
			String encoding, String sep, int bufLen) throws IOException {
		if (filePathAndName == null || filePathAndName.equals("")) {
			return "";
		}
		if (sep == null || sep.equals("")) {
			sep = System.getProperty("line.separator");
		}
		if (!new File(filePathAndName).exists()) {
			return "";
		}
		StringBuffer str = new StringBuffer("");
		FileInputStream fs = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			fs = new FileInputStream(filePathAndName);
			if (encoding == null || encoding.trim().equals("")) {
				isr = new InputStreamReader(fs);
			} else {
				isr = new InputStreamReader(fs, encoding.trim());
			}
			br = new BufferedReader(isr, bufLen);

			String data = "";
			int i = 0;
			String X = "" + '\ufeff';
			String X2 = "" + '\ufffe';
			while ((data = br.readLine()) != null) {
				i++;
				if(i<3) {
					// remove BOM special char 
					data = data.replace(X, "");
					data = data.replace(X2, "");	
				}
				if(!str.toString().isEmpty()){
					str.append(sep);
				}
				str.append(data);
			}

		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
				if (fs != null)
					fs.close();
			} catch (IOException e) {
				throw e;
			}

		}
		return str.toString();
	}

	/**
	 * 新建一个文件并写入内容
	 * 
	 * @param String
	 *            path 文件全路径
	 * @param String
	 *            fileName 文件名
	 * @param StringBuffer
	 *            content 内容
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean newFile(String path, String fileName,
			StringBuffer content) throws IOException {

		return newFile(path, fileName, content, 1024, false);
	}

	/**
	 * 新建一个文件并写入内容
	 * 
	 * @param String
	 *            path 文件全路径
	 * @param String
	 *            fileName 文件名
	 * @param StringBuffer
	 *            content 内容
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean newFile(String path, String fileName,
			StringBuffer content, boolean isWrite) throws IOException {

		return newFile(path, fileName, content, 1024, isWrite);
	}

	/**
	 * 新建一个文件并写入内容
	 * 
	 * @param String
	 *            path 文件全路径
	 * @param String
	 *            fileName 文件名
	 * @param StringBuffer
	 *            content 内容
	 * @param int
	 *            bufLen 设置缓冲区大小
	 * @param boolean
	 *            isWrite 是否追加写入文件
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean newFile(String path, String fileName,
			StringBuffer content, int bufLen, boolean isWrite)
			throws IOException {
		if (path == null || path.equals("") || content == null
				|| content.equals(""))
			return false;
		boolean flag = false;
		FileWriter fw = null;
		BufferedWriter bw = null;

		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();

		}
		try {
			fw = new FileWriter(path + File.separator + fileName, isWrite);
			bw = new BufferedWriter(fw, bufLen);
			bw.write(content.toString());
			flag = true;
		} catch (IOException e) {
			System.out.println("写入文件出错");
			flag = false;
			throw e;
		} finally {
			if (bw != null) {
				bw.flush();
				bw.close();
			}
			if (fw != null)
				fw.close();
		}

		return flag;
	}

	public static boolean newFile(String path, String fileName, byte[] content)
			throws IOException {
		if (path == null || path.equals("") || content == null
				|| content.equals(""))
			return false;
		boolean flag = false;
		new File(path).mkdirs();
		DataOutputStream outputs = new DataOutputStream(
				new BufferedOutputStream(new FileOutputStream(path + fileName,
						false)));
		outputs.write(content);
		outputs.close();
		return flag;
	}

	/**
	 * 新建一个文件并写入内容
	 * 
	 * @param String
	 *            path 文件全路径
	 * @param String
	 *            fileName 文件名
	 * @param String
	 *            content 内容
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean newFile(String path, String fileName, String content)
			throws IOException {

		return newFile(path, fileName, content, 1024, false);
	}

	/**
	 * 新建一个文件并写入内容
	 * 
	 * @param String
	 *            path 文件全路径
	 * @param String
	 *            fileName 文件名
	 * @param String
	 *            content 内容
	 * @param boolean
	 *            isWrite 是否追加写入文件
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean newFile(String path, String fileName, String content,
			boolean isWrite) throws IOException {

		return newFile(path, fileName, content, 1024, isWrite);
	}

	/**
	 * 新建一个文件并写入内容
	 * 
	 * @param String
	 *            path 文件全路径
	 * @param String
	 *            fileName 文件名
	 * @param String
	 *            content 内容
	 * @param int
	 *            bufLen 设置缓冲区大小
	 * @param boolean
	 *            isWrite 是否追加写入文件
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean newFile(String path, String fileName, String content,
			int bufLen, boolean isWrite) throws IOException {

		if (path == null || path.equals("") || content == null
				|| content.equals(""))
			return false;
		boolean flag = false;
		FileWriter fw = null;
		BufferedWriter bw = null;

		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();

		}
		try {
			fw = new FileWriter(path + File.separator + fileName, isWrite);
			bw = new BufferedWriter(fw, bufLen);
			bw.write(content);
			flag = true;
		} catch (IOException e) {
			System.out.println("写入文件出错");
			flag = false;
			throw e;
		} finally {
			if (bw != null) {
				bw.flush();
				bw.close();
			}
			if (fw != null)
				fw.close();
		}

		return flag;
	}

	/**
	 * 新建一个目录
	 * 
	 * @param String
	 *            filePath 路径
	 * @return boolean flag
	 * @throws Exception
	 */
	public static boolean newFolder(String filePath) throws Exception {
		boolean flag = false;
		if (filePath == null || filePath.equals("") || filePath.equals("null")) {
			return flag;
		}
		try {
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.mkdirs();
			}
			flag = true;
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:
	 * @return boolean
	 */
	public static boolean copyFile(String oldPath, String newPath)
			throws IOException {
		return copyFile(oldPath, newPath, null);
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/
	 * @return boolean
	 */
	public static boolean copyFile(String oldPath, String newPath,
			String newFileName) throws IOException {

		boolean flag = false;
		if (oldPath == null || newPath == null || newPath.equals("")
				|| oldPath.equals("")) {
			return flag;
		}
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			//int bytesum = 0;
			int byteread = 0;
			File file = null;
			file = new File(newPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(oldPath);
			if (file.exists()) { // 文件存在时
				inStream = new FileInputStream(oldPath); // 读入原文件
				if (newFileName == null || newFileName.equals("")) {
					newFileName = file.getName();
				}
				fs = new FileOutputStream(newPath + File.separator
						+ newFileName);
				byte[] buffer = new byte[1024 * 8];
				while ((byteread = inStream.read(buffer)) != -1) {
					//bytesum += byteread; // 字节数 文件大小
//					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				flag = true;
			}
		} catch (IOException e) {
			throw e;

		} finally {
			try {
				if (fs != null) {
					fs.close();
				}
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				throw e;
			}
		}
		return flag;
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 */
	public static void copyFolder(String oldPath, String newPath)
			throws Exception {
		if (oldPath == null || newPath == null || newPath.equals("")
				|| oldPath.equals("")) {
			return;
		}
		if(oldPath.indexOf("/tmp/")!=-1 || oldPath.endsWith("/tmp")){
		    return;
		}
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			File temp = null;
			temp = new File(oldPath);
			String[] file = temp.list();
			temp = new File(newPath);
			if (!temp.exists()) {
			    temp.mkdirs();// 如果文件夹不存在 则建立新文件夹
			}
			if(file!=null){
				for (int i = 0; i < file.length; i++) {
					if (oldPath.endsWith(File.separator)) {
						temp = new File(oldPath + file[i]);
					} else {
						temp = new File(oldPath + File.separator + file[i]);
					}
					if (temp.isFile()) {
						try {
							input = new FileInputStream(temp);
							output = new FileOutputStream(newPath + File.separator
									+ (temp.getName()).toString());
							byte[] b = new byte[1024 * 8];
							int len;
							while ((len = input.read(b)) != -1) {
								output.write(b, 0, len);
							}
							output.flush();
						} catch (Exception e) {
						}
					}
					if (temp.isDirectory()) {// 如果是子文件夹
						copyFolder(oldPath + File.separator + file[i], newPath
								+ File.separator + file[i]);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			} catch (Exception e) {
				throw e;
			}
		}
	}

	/**
	 * 移动单个文件
	 * 
	 * @param Sring
	 *            srcFile 源文件 如：c:/test.txt
	 * @param destPath
	 *            目标目录 如：c：/test
	 * @param newFileName
	 *            新文件名 如：newTest.txt
	 * @return boolean
	 */
	public static boolean Move(String srcFile, String destPath) {
		return Move(srcFile, destPath, null);
	}

	/**
	 * 移动单个文件
	 * 
	 * @param Sring
	 *            srcFile 源文件 如：c:/test.txt
	 * @param destPath
	 *            目标目录 如：c：/test
	 * @param newFileName
	 *            新文件名 如：newTest.txt
	 * @return boolean
	 */
	public static boolean Move(String srcFile, String destPath,
			String newFileName) {
		boolean flag = false;
		if (srcFile == null || srcFile.equals("") || destPath == null
				|| destPath.equals("")) {
			return flag;
		}
		File file = new File(srcFile);
		File dir = new File(destPath);
		if (newFileName == null || newFileName.equals("")
				|| newFileName.equals("null"))
			newFileName = file.getName();
		flag = file.renameTo(new File(dir, newFileName));

		return flag;
	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param String
	 *            oldPath 如：c:/fqf.txt
	 * @param String
	 *            newPath 如：d:/
	 */
	public static void moveFile(String oldPath, String newPath)
			throws Exception {
		if (copyFile(oldPath, newPath))
			delFile(oldPath);

	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param String
	 *            oldPath 如：c:/fqf.txt
	 * @param String
	 *            newPath 如：d:/
	 * @param String
	 *            newFileName 新文件名 如：newTest.txt
	 */
	public static void moveFile(String oldPath, String newPath,
			String newFileName) throws Exception {
		if (copyFile(oldPath, newPath))
			delFile(oldPath);

	}

	/**
	 * 移动文件夹到指定目录
	 * 
	 * @param String
	 *            oldPath 如：c:/fqf.txt
	 * @param String
	 *            newPath 如：d:/
	 */
	public static void moveFolder(String oldPath, String newPath)
			throws Exception {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}

	/**
	 * 删除文件
	 * 
	 * @param String
	 *            filePathAndName 文件路径及名称 如c:/fqf.txt
	 */
	public static void delFile(String filePathAndName) {
		if (filePathAndName == null || filePathAndName.equals("")
				|| filePathAndName.equals("null"))
			return;
		try {
			File myDelFile = new File(filePathAndName);
			if (myDelFile.exists())
				myDelFile.delete();

		} catch (Exception e) {
			System.out.println("删除文件操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 删除文件夹
	 * 
	 * @param String
	 *            folderPath 文件夹路径及名称 如c:/fqf
	 */
	public static void delFolder(String folderPath) {
		if (folderPath == null || folderPath.equals(""))
			return;
		try {
			File myFilePath = new File(folderPath);
			if (myFilePath.isDirectory() && myFilePath.exists()) {
				delAllFile(folderPath); // 删除完里面所有内容
				myFilePath.delete(); // 删除空文件夹
			}

		} catch (Exception e) {
			System.out.println("删除文件夹操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param String
	 *            path 文件夹路径 如 c:/fqf
	 */
	public static void delAllFile(String path) {
		if (path == null || path.equals(""))
			return;
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + File.separator + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + File.separator + tempList[i]);// 再删除空文件夹
			}
		}
//		file.delete();
	}

	/**
	 * 当文件夹为空时删除文件夹
	 * 
	 * @param path
	 */
	public static void delWhileEmpty(String path) {
		if (path == null || path.equals(""))
			return;
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		if (tempList == null || tempList.length == 0) {
			file.delete();
		}
	}

	/**
	 * 删除目录以及当前目录下的文件或子目录。用递归就可以实现。
	 * 
	 * @param String
	 *            filepath 目录
	 * @throws Exception
	 */
	public static void del(String filepath) throws Exception {
		if (filepath == null || filepath.equals("") || filepath.equals("null"))
			return;
		try {
			File f = new File(filepath);// 定义文件路径
			if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
				if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
					f.delete();
				} else {// 若有则把文件放进数组，并判断是否有下级目录
					File delFile[] = f.listFiles();
					int i = f.listFiles().length;
					for (int j = 0; j < i; j++) {
						if (delFile[j].isDirectory()) {
							del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
						}
						delFile[j].delete();// 删除文件
					}
				}
				del(filepath);// 递归调用
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 分析CSV AND TXT的数据
	 * <p>
	 * Title: analysisLine
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param line
	 *            每行的数据
	 * @return
	 */
	public static List<String> analysisLine(StringBuffer line) {
		StringBuffer str = new StringBuffer();
		line.append(",");
		Pattern pCells = Pattern.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
		Matcher mCells = pCells.matcher(line);
		List<String> cells = new ArrayList<String>(0);// 每行记录一个list
		// 读取每个单元格
		while (mCells.find()) {
			str.append(mCells.group().replaceAll(
					"(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1").replaceAll(
					"(?sm)(\"(\"))", "$2"));
			cells.add(str.toString().trim());
			str.delete(0, str.length());
		}
		return cells;
	}

	/**
	 * 根据文件类型读取文件内容
	 * <p>
	 * Title: readFileByType
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param fileType
	 *            文件类型csv|txt|xls
	 * @param file
	 * @param headTitle
	 *            头部标题开头第一个名称
	 * @return
	 * @throws Exception
	 */
	public static List<List<String>> readFileByType(String fileType, File file,
			String headTitle) {

		List<List<String>> cells = new ArrayList<List<String>>(0);// 记录一个list
		if (".csv".equalsIgnoreCase(fileType)
				|| ".txt".equalsIgnoreCase(fileType)) {
			BufferedReader br = null;
			InputStreamReader isr = null;
			try {
				String encoding = get_charset(file);
				isr = new InputStreamReader(new FileInputStream(file), encoding);
				br = new BufferedReader(isr);
				boolean hasHeadLine = false;
				String line = null;
				StringBuffer strBuffer = null;
				List<String> lineList = null;
				line = br.readLine();
				while (line != null && line.trim().length() > 0) {
					strBuffer = new StringBuffer();
					strBuffer.append(line);
					lineList = analysisLine(strBuffer);
					strBuffer.delete(0, strBuffer.length());

					// 构建数据对象
					if (headTitle != null && headTitle.length() > 0) {
						if (lineList != null && lineList.size() >= 1) {
							for (int j = 0; j < lineList.size(); j++) {
								if (headTitle != null
										&& headTitle.length() > 0
										&& lineList.get(j)
												.trim().equals(headTitle)) {
									hasHeadLine = true;
								}
							}
						}
						if (hasHeadLine) {
							line = br.readLine();// 从文件中继续读取一行数据
							hasHeadLine = false;
							continue;
						}
					}
					cells.add(lineList);
					line = br.readLine();// 从文件中继续读取一行数据
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					if (br != null)
						br.close();
					if (isr != null)
						isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (".xls".equalsIgnoreCase(fileType)) {
			InputStream inp = null;
			try {
				inp = new FileInputStream(file);
				// 根据输入流创建Workbook对象
				Workbook wb = WorkbookFactory.create(inp);
				// get到Sheet对象
				Sheet sheet = wb.getSheetAt(0);
				// xls文件中科学计数法转换为正常数字
				java.text.DecimalFormat formatter = new java.text.DecimalFormat(
						"########");
				String cellString = null;
				// 行循环
				int cols = 0;
				Cell cell = null;
				outLoop: for (Row row : sheet) {
					List<String> lineList = new ArrayList<String>(0);
					cols = row.getLastCellNum();
					for (int i = 0; i < cols; i++) {
						cell = row.getCell(i);
						cellString = "";
						// cell.getCellType是获得cell里面保存的值的type
						// 如Cell.CELL_TYPE_STRING
						if (cell != null) {
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_BOOLEAN:
								// 得到Boolean对象的方法
								cellString = cell.getBooleanCellValue() + "";
								break;
							case Cell.CELL_TYPE_NUMERIC:
								// 先看是否是日期格式
								if (DateUtil.isCellDateFormatted(cell)) {
									// 读取日期格式
									cellString = cell.getDateCellValue() + "";
								} else {
									// 读取数字
									cellString = formatter.format(cell
											.getNumericCellValue());
								}
								break;
							case Cell.CELL_TYPE_FORMULA:
								// 读取公式
								cellString = cell.getCellFormula();
								break;
							case Cell.CELL_TYPE_STRING:
								// 读取String
								cellString = cell.getRichStringCellValue()
										.toString();
								break;
							}
						}
						if (headTitle != null && headTitle.length() > 0
								&& cellString.trim().equals(headTitle)) {
							continue outLoop;
						}
						lineList.add(cellString.trim());
					}
					cells.add(lineList);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					if (inp != null)
						inp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (".properties".equalsIgnoreCase(fileType)) {
			BufferedReader br = null;
			InputStreamReader isr = null;
			try {
				String encoding = get_charset(file);
				isr = new InputStreamReader(new FileInputStream(file), encoding);
				br = new BufferedReader(isr);
				boolean hasHeadLine = false;
				String line = null;
				StringBuffer strBuffer = null;
				List<String> lineList = null;
				line = br.readLine();
				while (line != null) {
					strBuffer = new StringBuffer();
					strBuffer.append(line);
					lineList = analysisLine(strBuffer);
					strBuffer.delete(0, strBuffer.length());

					// 构建数据对象
					if (headTitle != null && headTitle.length() > 0) {
						if (lineList != null && lineList.size() >= 1) {
							for (int j = 0; j < lineList.size(); j++) {
								if (headTitle != null
										&& headTitle.length() > 0
										&& lineList.get(j)
												.trim().equals(headTitle)) {
									hasHeadLine = true;
								}
							}
						}
						if (hasHeadLine) {
							line = br.readLine();// 从文件中继续读取一行数据
							hasHeadLine = false;
							continue;
						}
					}
					cells.add(lineList);
					line = br.readLine();// 从文件中继续读取一行数据
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return cells;
	}
	


	/**
	 * 根据文件类型读取文件内容
	 * <p>
	 * Title: readFileByType
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param file
	 * @param headTitle
	 *            头部标题开头第一个名称
	 * @param sheetNum 第sheetNum个sheet
	 * @return
	 * @throws Exception
	 */
	public static List<List<String>> readXlsFileByType(File file,
	        String headTitle,Integer sheetNum) {
        List<List<String>> cells = new ArrayList<List<String>>(0);// 记录一个list
        InputStream inp = null;
        try {
            inp = new FileInputStream(file);
            // 根据输入流创建Workbook对象
            Workbook wb = WorkbookFactory.create(inp);
            // get到Sheet对象
            Sheet sheet = wb.getSheetAt(sheetNum==null?0:sheetNum);
            // xls文件中科学计数法转换为正常数字
            java.text.DecimalFormat formatter = new java.text.DecimalFormat("########");
            String cellString = null;
            // 行循环
            int cols = 0;
            Cell cell = null;
            outLoop: for (Row row : sheet) {
                List<String> lineList = new ArrayList<String>(0);
                cols = row.getLastCellNum();
                for (int i = 0; i < cols; i++) {
                    cell = row.getCell(i);
                    cellString = "";
                    // cell.getCellType是获得cell里面保存的值的type
                    // 如Cell.CELL_TYPE_STRING
                    if (cell != null) {
                        switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_BOOLEAN:
                            // 得到Boolean对象的方法
                            cellString = cell.getBooleanCellValue() + "";
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            // 先看是否是日期格式
                            if (DateUtil.isCellDateFormatted(cell)) {
                                // 读取日期格式
                                cellString = cell.getDateCellValue() + "";
                            }
                            else {
                                // 读取数字
                                cellString = formatter.format(cell.getNumericCellValue());
                            }
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            // 读取公式
                            cellString = cell.getCellFormula();
                            break;
                        case Cell.CELL_TYPE_STRING:
                            // 读取String
                            cellString = cell.getRichStringCellValue().toString();
                            break;
                        }
                    }
                    if (headTitle != null && headTitle.length() > 0 && cellString.trim().equals(headTitle)) {
                        continue outLoop;
                    }
                    lineList.add(cellString.trim());
                }
                cells.add(lineList);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            try {
                if (inp != null)
                    inp.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cells;
    }

	/**
	 * 获取路径下所有文件
	 * 
	 * @param strPath
	 * @return
	 */
	public static List<String[]> findFileList(String strPath) {
		List<String[]> filelist = new ArrayList<String[]>();
		File dir = new File(strPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return filelist;
		}
		Arrays.sort(files, new CompratorByLastModified());
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				filelist.addAll(findFileList(files[i].getAbsolutePath()));
			} else {
				filelist.add(new String[] { files[i].getName(),
						files[i].getAbsolutePath() });
			}
		}
		return filelist;
	}
	
	/**
	 * 获取路径下所有文件
	 * 
	 * @param strPath
	 * @return
	 */
	public static Map<String, String> findFileMap(String strPath) {
		Map<String, String> filelist = new LinkedHashMap<String, String>();
		File dir = new File(strPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return filelist;
		}
		Arrays.sort(files, new CompratorByLastModified());
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				//filelist.addAll(findFileList(files[i].getAbsolutePath()));
			} else {
				filelist.put(files[i].getName(), files[i].getName());
			}
		}
		return filelist;
	}
	
	/**
	 * 取目录文件按日期排序 
	* compratorByLastModified
	 */
	static class CompratorByLastModified implements Comparator<File> {
		public int compare(File f1, File f2) {
			// 降序
			long diff = f2.lastModified() - f1.lastModified();
			// 升序
			//long diff = f1.lastModified() - f2.lastModified();
			if (diff > 0) {
				return 1;
			}
			else if (diff == 0) {
				return 0;
			}
			else {
				return -1;
			}
		}

		public boolean equals(Object obj) {
			return true;
		}
	}

	/**
	 * 获得本地文件的编码格式 ANSI： 无格式定义； Unicode： 前两个字节为FFFE； Unicode big endian：
	 * 前两字节为FEFF； UTF-8： 前两字节为EFBB；
	 * @param file
	 * @return
	 */
	public static String get_charset(File file) {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		BufferedInputStream bis = null;
		try {
			boolean checked = false;
			bis = new BufferedInputStream(new FileInputStream(file));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1)
				return charset;
			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE
					&& first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF
					&& first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();
			if (!checked) {
				// int len = 0;
				//int loc = 0;

				while ((read = bis.read()) != -1) {
					//loc++;
					if (read >= 0xF0)
						break;
					if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
							// (0x80
							// - 0xBF),也可能在GB编码内
							continue;
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
				// System.out.println( loc + " " + Integer.toHexString( read )
				// );
			}

			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			IOUtils.closeQuietly(bis);
		}
		return charset;
	}

	/**
	 * 获取文件夹大小
	 * <p>
	 * Title: getFileSize
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param file
	 * @return
	 */
	public static long getFileSize(File file) {
		long result = 0;
		if (file != null && file.exists()) {
			if (file.isFile()) {
				result += file.length();
			} else {
				File temp_files[] = file.listFiles();
				for (int i = 0; i < temp_files.length; i++) {
					if (temp_files[i].isFile()) {
						if (!(temp_files[i].getName().endsWith(".ts") || temp_files[i]
								.getName().endsWith(".exe")))
							result += temp_files[i].length();
					} else if (temp_files[i].isDirectory()) {
						result += getFileSize(temp_files[i]);
					}
				}
			}
		}
		return result;
	}
	
	public static String getCharset(byte[] head) {
		String code = "GBK";
		if (head[0] == (byte) 0xFF && head[1] == (byte) 0xFE) {
			code = "UTF-16LE";
		} else if (head[0] == (byte) 0xFE && head[1] == (byte) 0xFF) {
			code = "UTF-16BE";
		} else if (head[0] == (byte) 0xEF && head[1] == (byte) 0xBB
				&& head[2] == (byte) 0xBF) {
			code = "UTF-8";
		}
		return code;
	}
	
	public static void closeReader(Reader reader) {
		if (reader == null)
			return;
		try {
			reader.close();
		} catch (Exception e) {
			// TODO: Ignore it
		}
	}

	public static String get_charset(InputStream input) {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(input);
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1)
				return charset;
			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE
					&& first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF
					&& first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();
			if (!checked) {
				// int len = 0;
				//int loc = 0;

				while ((read = bis.read()) != -1) {
					//loc++;
					if (read >= 0xF0)
						break;
					if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
							// (0x80
							// - 0xBF),也可能在GB编码内
							continue;
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
				// System.out.println( loc + " " + Integer.toHexString( read )
				// );
			}

			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return charset;
	}
	
	public static LogResult readReverse(String filename, String charset, int maxLine) {
		LogResult result = new LogResult();
		List<String> lines = new ArrayList<String>();
		result.setLines(lines);
        RandomAccessFile rf = null;
        try {
        	File file = new File(filename);
        	charset = get_charset(file);
            rf = new RandomAccessFile(filename, "r");
            long fileLength = rf.length();
            long start = rf.getFilePointer();// 返回此文件中的当前偏移量
            long readIndex = start + fileLength -1;
            if (readIndex <= 0){
            	return result;
            }
            String line;
            rf.seek(readIndex);// 设置偏移量为文件末尾
            int c = -1;
            while (readIndex > start) {
            	if (lines.size() >= maxLine){
            		break;
            	}
                c = rf.read();
                if (c == '\n' || c == '\r') {
                    line = rf.readLine();
                    if (line != null) {
                    	lines.add(new String(line.getBytes("ISO-8859-1"), charset));
                    } 
                    readIndex--;
                }
                readIndex--;
                rf.seek(readIndex);
                if (readIndex == 0) {// 当文件指针退至文件开始处，输出第一行
                	lines.add(new String(rf.readLine().getBytes("ISO-8859-1"), charset));
                }
            }
            result.setFilePointerOffset(rf.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rf != null){
                    rf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
	
	public static LogResult realtimeReadLog(String filename, String charset,long filePointerOffset){
		LogResult result = new LogResult();
		List<String> lines = new ArrayList<String>();
		result.setLines(lines);
        RandomAccessFile rf = null;
        try {
        	File file = new File(filename);
        	charset = get_charset(file);
        	
            rf = new RandomAccessFile(filename, "r");
            rf.seek(filePointerOffset);// 设置偏移量为文件末尾
            String line = null;   
            while( (line = rf.readLine())!= null) {     
                lines.add(new String(line.getBytes("ISO8859-1"), charset));
            }  
            result.setFilePointerOffset(rf.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rf != null){
                    rf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result; 
	}
	
	
	public static List<String> readPropertiesFile(File file){
		List<String> lineList = new ArrayList<String>();
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			String encoding = get_charset(file);
			isr = new InputStreamReader(new FileInputStream(file), encoding);
			br = new BufferedReader(isr);
			String line = null;

			line = br.readLine();
			while (line != null) {
				lineList.add(line);
				line = br.readLine();// 从文件中继续读取一行数据
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lineList;
	}
	
	public static BufferedImage readPictureResolution(File file){
	        BufferedImage bi = null;  
	        try {  
	            bi = ImageIO.read(file);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return bi;
	  
	}
	
	/**
	 * replace file extension name. e.g: 
	 * replaceExtName("a.html", "zip") will return a.zip
	 * @param filename
	 * @param newExt
	 * @return
	 */
	public static String replaceExtName(String filename, String newExt) {
		int idx= filename.lastIndexOf(".");
		if(idx==-1) {
			return filename + "." + newExt;
		}
		filename = filename.substring(0,idx);
		filename = filename + "." + newExt;
		return filename;
	}
	
	public static void unZipFiles(File zipFile,String descDir)throws IOException{  
        File pathFile = new File(descDir);  
        if(!pathFile.exists()){  
            pathFile.mkdirs();  
        }  
        ZipFile zip = new ZipFile(zipFile);  
        for(Enumeration<? extends ZipEntry> entries = zip.entries();entries.hasMoreElements();){  
            ZipEntry entry = entries.nextElement();  
            String zipEntryName = entry.getName();  
            InputStream in = zip.getInputStream(entry);  
            String outPath = (descDir + "/" + zipEntryName);;  
            //判断路径是否存在,不存在则创建文件路径  
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));  
            if(!file.exists()){  
                file.mkdirs();  
            }  
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压  
            if(new File(outPath).isDirectory()){  
                continue;  
            }  
            //输出文件路径信息  
            System.out.println(outPath);  
              
            OutputStream out = new FileOutputStream(outPath);  
            byte[] buf1 = new byte[1024];  
            int len;  
            while((len=in.read(buf1))>0){  
                out.write(buf1,0,len);  
            }  
            in.close();  
            out.close();
        }
        zip.close();
        System.out.println("******************解压完毕********************");  
    }  
	
	public static File[] getFileList(String path, final String suffix){
		FileFilter templateFileFilter = new FileFilter(){

			@Override
			public boolean accept(File file) {
				if (file.getName().indexOf(suffix) != -1){
					return true;
				}
				return false;
			}
			
		};
		File zipFile = new File(path);
		File[] fileList = zipFile.listFiles(templateFileFilter);
		
		return fileList;
	}
	
	
}
