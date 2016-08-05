package com.db.bms.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

import org.apache.log4j.Logger;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.springframework.web.multipart.MultipartFile;

import com.sun.imageio.plugins.bmp.BMPImageReader;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.png.PNGImageReader;

public class FileUpload {
    private static Logger log = Logger.getLogger(FileUpload.class);

    private static int BUFFER_SIZE = 10240;

    private static String last_prefix = null;

    private static int index = 1;

    public FileUpload() {
    }

    /**
     * 保存文件到指定的路径
     * @param tempFile 文件
     * @param savePath 指定路径
     * @param fileName 文件名
     * @return
     */
    public static boolean saveFile(String tempFile, String savePath, String fileName) {
        boolean isok = false;
        if (tempFile != null && tempFile.trim().length() > 0) {
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream(tempFile);
                File fileDir = new File(savePath);
                if (!fileDir.exists())
                    fileDir.mkdirs();
                fos = new FileOutputStream(fileDir + File.separator + fileName);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                isok = true;
            }
            catch (Exception e) {
                isok = false;
                e.printStackTrace();
            }
            finally {
                if (fis != null)
                    try {
                        fis.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                if (fos != null)
                    try {
                        fos.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
        return isok;
    }

    public static boolean saveFile(MultipartFile file, String savePath, String fileName) {
        boolean isok = false;
        if (file != null) {
            InputStream fiss = null;
            FileOutputStream fos = null;
            try {
                fiss = file.getInputStream();
                File fileDir = new File(savePath);
                if (!fileDir.exists())
                    fileDir.mkdirs();
                fos = new FileOutputStream(fileDir + File.separator + fileName);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = fiss.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                isok = true;
            }
            catch (Exception e) {
                isok = false;
                e.printStackTrace();
            }
            finally {
                if (fiss != null)
                    try {
                        fiss.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                if (fos != null)
                    try {
                        fos.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
        return isok;
    }

    /**
     * 根据时间自动生成上传文件的名字
     * @param fileName
     * @return
     */
    public static String geneFileName(String fileName) {
        String prefix = "";
        String extension = "";
        try {
            prefix = DateUtil.format(new Date(), "yyyyMMddHHmmss");
            if (prefix.equals(last_prefix)) {
                index = index + 1;
            }
            else {
                index = 1;
            }
            last_prefix = prefix;

            int extIndex = fileName.lastIndexOf(".");
            if (extIndex > 0) {
                extension = fileName.substring(extIndex);
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return prefix + index + extension;
    }

    /**
     * 南京需求需分别去掉“tip.mis”和“index.mis”的mis
     * @param folderPath
     * @return
     */
    public static String[] getIndexFile(String folderPath) {
        String[] fileInfo = null;
        List<String[]> filelist = FileUtils.findFileList(folderPath);
        if (filelist != null && filelist.size() > 0) {
            fileInfo = new String[] { "", "", "", "" };
            for (String[] arr : filelist) {
                if (arr[0].startsWith("tip.mis")) {
                    arr[1] = arr[1].replaceAll("\\\\", "/").replace(
                            folderPath.replaceAll("\\\\", "/").replaceAll("//", "/"), "");
                    if (fileInfo[1].equals("")
                            || fileInfo[1].substring(0, fileInfo[1].lastIndexOf("/")).length() >= arr[1].substring(0,
                                    arr[1].lastIndexOf("/")).length()) {
                        if (fileInfo[1].equals("")
                                || fileInfo[1].substring(0, fileInfo[1].lastIndexOf("/")).length() > arr[1].substring(
                                        0, arr[1].lastIndexOf("/")).length()) {
                            fileInfo[2] = "";
                        }
                        fileInfo[0] = arr[0];
                        fileInfo[1] = arr[1];
                        fileInfo[3] = "tip.mis";
                    }
                }
                else if (arr[0].startsWith("index.mis")) {
                    arr[1] = arr[1].replaceAll("\\\\", "/").replace(
                            folderPath.replaceAll("\\\\", "/").replaceAll("//", "/"), "");
                    if (fileInfo[1].equals("")
                            || fileInfo[1].substring(0, fileInfo[1].lastIndexOf("/")).length() >= arr[1].substring(0,
                                    arr[1].lastIndexOf("/")).length()) {
                        if (fileInfo[1].equals("")
                                || fileInfo[1].substring(0, fileInfo[1].lastIndexOf("/")).length() > arr[1].substring(
                                        0, arr[1].lastIndexOf("/")).length()) {
                            fileInfo[3] = "";
                        }
                        fileInfo[0] = arr[0];
                        fileInfo[1] = arr[1];
                        fileInfo[2] = arr[0];
                    }
                }
            }
            if (fileInfo[0].equals("")) {
                fileInfo = null;
            }
        }
        return fileInfo;
    }

    public static boolean hasTip(String folderPath) {
        boolean flag = false;
        File dir = new File(folderPath);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile() && files[i].getName().startsWith("tip.")) {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    public static void checkPicSize(File file) throws Exception {
        long maxPicSize = 180000;//1800k
        if (maxPicSize > 0) {
            File temp_files[] = file.listFiles();
            String fileName = null;
            for (int i = 0; i < temp_files.length; i++) {
                if (temp_files[i].isFile()) {
                    fileName = temp_files[i].getName().toLowerCase();
                    if (fileName.endsWith(".gif") || fileName.endsWith(".png") || fileName.endsWith(".jpg")
                            || fileName.endsWith(".jpeg")) {
                        if (temp_files[i].length() > maxPicSize) {
                            throw new Exception("图片" + fileName + "大小不能大于" + String.valueOf(maxPicSize));
                        }
                    }
                }
                else if (temp_files[i].isDirectory()) {
                    checkPicSize(temp_files[i]);
                }
            }
        }
    }

    public static byte[] getBytesFromFile(String fileName) throws IOException {
        //      String name = basePath+File.separator + fileName;
        File file = new File(fileName);
        if (file.exists() && file.canRead()) {
            return getBytesFromFile(file);
        }
        else {
            log.warn("File[" + fileName + "] NOT exist or CAN NOT read!! so ignore it.");
            return null;
        }
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        try {
            long length = file.length();
            byte[] bytes = new byte[(int) length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) > 0) {
                offset += numRead;
            }
            if (offset < bytes.length) {
                throw new IOException("不能完成读文件：" + file.getName());
            }
            is.close();
            return bytes;
        }
        finally {
        	is.close();
        }
    }

    public static int getPicSuffix(String picName) {
        int res = 0;
        byte[] data = null;
        try {
            data = getBytesFromFile(picName);
        }
        catch (IOException e) {
        }
        if (data != null) {
            ByteArrayInputStream bais = null;
            MemoryCacheImageInputStream mcis = null;
            bais = new ByteArrayInputStream(data);
            mcis = new MemoryCacheImageInputStream(bais);
            Iterator<ImageReader> itr = ImageIO.getImageReaders(mcis);
            ImageReader reader = null;
            while (itr.hasNext()) {
                reader = itr.next();
                try {
                    reader.setInput(mcis);
                    reader.getHeight(0);
                }
                catch (Exception e) {
                    res = -1;
                    break;
                }
                if (reader instanceof JPEGImageReader) {
                    res = 1;
                    break;
                }
                else if (reader instanceof BMPImageReader) {
                    res = 2;
                    break;
                }
                else if (reader instanceof GIFImageReader) {
                    res = 4;
                    break;
                }
                else if (reader instanceof PNGImageReader) {
                    res = 3;
                    break;
                }
            }
            if (mcis != null) {
                try {
                    mcis.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bais != null) {
                try {
                    bais.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (res == 0) {
                //检查是不是i帧
                int size = data.length;
                int pt = 0;
                while (pt++ < (size - 6)) {
                    if (data[pt] == 0x00 && data[pt + 1] == 0x00 && data[pt + 2] == 0x01 && data[pt + 3] == 0x00) {
                        if ((data[pt + 5] & 0x38) == 8) {
                            res = 5;
                            break;
                        }
                    }
                    else {
                        continue;
                    }
                }
                if (res == 0) {
                    log.warn("[" + picName + "]'s type is unkonwn");
                }

            }
        }
        data = null;
        log.debug("[" + picName + "] get type=[" + res + "]");
        return res == 0 ? -1 : res;
    }

    /**
     * {取得图片的详细信息}
     * [getFormat={PNG: PNG}][getHeight=720][getWidth=1280][getColorType=2]
     * 图片格式，高，宽，颜色类型（2：rgb，3：cmyk）
     * 如果颜色类型为cmyk，则该图片非法
     * @return
     * @author: wanghaotao
     */
    public static String getPictureInfo(String abolutePath) {
        String picInfo = "";
        String formatStr = "";
        try {
            ImageInfo imgInfo = Sanselan.getImageInfo(new File(abolutePath));
            if (imgInfo.getColorType() == 3)
                return null;
            formatStr = imgInfo.getFormat().toString();
            picInfo += formatStr.substring(formatStr.lastIndexOf(":"));
            picInfo += ",";
            picInfo += imgInfo.getColorTypeDescription();
            picInfo += ",";
            picInfo += "height:" + imgInfo.getHeight();
            picInfo += ",";
            picInfo += "width:" + imgInfo.getWidth();
            return picInfo;
        }
        catch (ImageReadException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static PicInfo getPicInfo(String abolutePath){
    	PicInfo picInfo=new PicInfo();
    	try {
            ImageInfo imgInfo = Sanselan.getImageInfo(new File(abolutePath));
            if (imgInfo.getColorType() == 3)
                return null;
           
            Long size= (new File(abolutePath)).length();
            picInfo.setSize(size);
            picInfo.setFormat((imgInfo.getMimeType()).split("/")[1]);
            picInfo.setHeight( imgInfo.getHeight());
            picInfo.setWidth(imgInfo.getWidth());
        }
        catch (ImageReadException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    	return picInfo;
    }

    public static void main(String[] args) {
        try {
            ImageInfo imgInfo = Sanselan.getImageInfo(new File("D:/testpic/HD-1.jpg"));
            printImgInfo(imgInfo);
            System.out.println("------------------------");
            ImageInfo imgInfo1 = Sanselan.getImageInfo(new File("D:/testpic/hd-bootA.jpg"));
            printImgInfo(imgInfo1);
            System.out.println("------------------------");
            ImageInfo imgInfo2 = Sanselan.getImageInfo(new File("D:/testpic/hd-botA-2.jpg"));
            printImgInfo(imgInfo2);
            System.out.println("------------------------");
            ImageInfo imgInfo3 = Sanselan.getImageInfo(new File("D:/testpic/开机u互动b.jpg"));
            printImgInfo(imgInfo3);
            System.out.println("------------------------");
            ImageInfo imgInfo4 = Sanselan.getImageInfo(new File("D:/testpic/开机u互动b.png"));
            printImgInfo(imgInfo4);
            System.out.println("------------------------");
        }
        catch (ImageReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void printImgInfo(ImageInfo imgInfo) {
        System.out.println("[getFormat=" + imgInfo.getFormatName() + "][getHeight=" + imgInfo.getHeight()
                + "][getWidth=" + imgInfo.getWidth() + "][getColorType=" + imgInfo.getColorType()
                + "][getColorTypeDesc=" + imgInfo.getColorTypeDescription() + "]");
        System.out.println(imgInfo);
    }

}