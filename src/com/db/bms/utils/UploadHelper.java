package com.db.bms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class UploadHelper {
	 /** 
     * @descrption 根据HttpServletRequest对象获取MultipartFile集合 
     * @create 2012-11-19下午5:11:41 
     * @param request 
     * @param maxLength 
     *            文件 最大限制 
     * @param allowExtName 
     *            不允许上传的文件 扩展名 
     * @return MultipartFile集合 
     */  
    public static List<MultipartFile> getFileSet(HttpServletRequest request,  
            long maxLength, String[] allowExtName,String name) {  
        MultipartHttpServletRequest multipartRequest = null;  
        try {  
            multipartRequest = (MultipartHttpServletRequest) request;  
        } catch (Exception e) {  
            return new LinkedList<MultipartFile>();  
        }  
  
        List<MultipartFile> files = new LinkedList<MultipartFile>();  
        files = multipartRequest.getFiles(name);  
        // 移除不符合条件的  
      /*  for (int i = 0; i < files.size(); i++) {  
            if (!validateFile(files.get(i), maxLength, allowExtName)) {  
                files.remove(files.get(i));  
                if (files.size() == 0) {  
                    return files;  
                }  
            }  
        }*/  
        return files;  
    }  
  
    /** 
     * @descrption 保存 文件  
     * @create 2012-11-19下午4:17:36 
     * @param file 
     *            MultipartFile对象 
     * @param path 
     *            保存路径，如“D:\\File\\” 
     * @return 保存的全路径 如“D:\\File\\2345678.txt” 
     * @throws Exception 
     *             文件 保存失败 
     */  
    public static String uploadFile(MultipartFile file, String path,String lastFileName)  
            throws Exception {  
  
        String filename = file.getOriginalFilename();  
        String extName = filename.substring(filename.lastIndexOf("."))  
                .toLowerCase();  
       // String lastFileName = UUID.randomUUID().toString() + extName;  
        if (!path.endsWith(File.separator)) {  
            path = path + File.separator;  
        }  
        File temp = new File(path);  
        if (!temp.exists()) {  
            temp.mkdirs();  
        }  
        // 图片存储的全路径  
        String fileFullPath = path + lastFileName;  
        FileCopyUtils.copy(file.getBytes(), new File(fileFullPath));  
        return fileFullPath;  
    }  
  
    /** 
     * @descrption 验证 文件 格式，这里主要验证后缀名 
     * @create 2012-11-19下午4:08:12 
     * @param file 
     *            MultipartFile对象 
     * @param maxLength 
     *            文件 最大限制 
     * @param allowExtName 
     *            不允许上传的 文件 扩展名 
     * @return 文件 格式是否合法 
     */  
    private static boolean validateFile(MultipartFile file, long maxLength,  
            String[] allowExtName) {  
        if (file.getSize() < 0 || file.getSize() > maxLength)  
            return false;  
        String filename = file.getOriginalFilename();  
  
        // 处理不选择 文件 点击上传时，也会有MultipartFile对象，在此进行过滤  
        if (filename == "") {  
            return false;  
        }  
        String extName = filename.substring(filename.lastIndexOf("."))  
                .toLowerCase();  
        if (allowExtName == null || allowExtName.length == 0  
                || Arrays.binarySearch(allowExtName, extName) != -1) {  
            return true;  
        } else {  
            return false;  
        }  
    } 
    
    /**
     * MD5的生成
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
	try {
	    MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
	    MessageDigest md5 = MessageDigest.getInstance("MD5");
	    md5.update(byteBuffer);
	    BigInteger bi = new BigInteger(1, md5.digest());
	    value = bi.toString(16);
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
            if(null != in) {
	            try {
		        in.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	    }
	}
	return value;
    }
  

}
