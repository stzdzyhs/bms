package com.db.bms.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import com.db.bms.utils.VedioInfo.EncodeType;
import com.db.bms.utils.VedioInfo.VedioType;


/**
 * 图片转换<br>
 * <br>
 * 这里的图片转换是 利用 java 调用外部程序 ffmpeg 进行处理.<br>
 * ffmpeg.exe能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）<br>
 * 对ffmpeg.exe无法解析的文件格式(wmv9，rm，rmvb等),<br>
 * 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式；<br>
 * mencoder.exe;<br>
 * drv43260.dll;<br>
 * pncrt.dll<br>
 * 以上这3个文件是为文件格式(wmv9，rm，rmvb等）<br>
 * 转换为avi(ffmpeg能解析的)格式准备的；再把转换好的avi文件再用ffmpeg.exe转换成flv格式的视频文件<br>
 * <br>
 * 
 * 
 */
public class ImageConvertor {

    private static final String FILE_SEPARATOR = File.separator;
    
    private static boolean isSurpportedType(String type) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(asx|asf|mpg|wmv|3gp|mp4|mov|avi|flv|ts){1}$", java.util.regex.Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(type);
        return matcher.find();
    }

    public static String getFfmpegPath() throws Exception{
        String osName = System.getProperty("os.name");
        String retPath ="";
        if(osName.toLowerCase().indexOf("win")>=0){
            retPath = Environment.FILE_SYSTEM_DIRECTORY + "/common/ffmpeg.exe";
        }
        else{            
            retPath = "/usr/local/bin/ffmpeg";
        }          
        return retPath;
    }
    /**
     *B类视频格式转换 
     */
    public static void ConverVideoToMp4(String ffmpegPath, File sourceFile, String destination, String pattern)
            throws IOException {
        if (!sourceFile.exists())
            throw new IOException("invalid file path");
        if (!new File(destination).exists())
            throw new IOException("invalid file path " + destination);

        String fileName = sourceFile.getName();
        //String surffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        //      if (!isSurpportedType(surffix))
        //          throw new RuntimeException("unsurpported file type " + surffix);

        List<String> cmdParam = new LinkedList<String>();
        cmdParam.add(ffmpegPath);
        //cmdParam.add("-y");
        //      cmdParam.add("-i");
        //      System.out.println("sourceFile.getAbsolutePath()="+sourceFile.getAbsolutePath());
        //      cmdParam.add(sourceFile.getAbsolutePath());
        //      cmdParam.add("-ab");
        //      cmdParam.add("128");
        //      cmdParam.add("-acodec");
        //      cmdParam.add("libmp3lame");
        //      cmdParam.add("-ac");
        //      cmdParam.add("1");
        //      cmdParam.add("-ar");
        //      cmdParam.add("22050");
        //      cmdParam.add("-r");
        //      cmdParam.add("29.27");
        //      cmdParam.add("-qscale");
        //      cmdParam.add("6");
        //      cmdParam.add("-y");

        cmdParam.add("-i");
        cmdParam.add(sourceFile.getAbsolutePath());

        cmdParam.add("-ab");
        cmdParam.add("128k");

        cmdParam.add("-s");
        cmdParam.add("1280*720");

        cmdParam.add("-b");
        cmdParam.add("2000k");

        cmdParam.add("-f");
        cmdParam.add("mp4");

        cmdParam.add("-y");

        //高清
        cmdParam.add(destination + FILE_SEPARATOR + fileName.substring(0, fileName.lastIndexOf(".")) + "." + pattern);

        execCmd(cmdParam);
    }

    /**
     * 
     * @param sourceFile
     *            将要被转换的目标文件
     * @param desctination
     *            转换之后文件的存放路径 ffmpeg commandLine： ffmpeg -y -i
     *            /usr/local/bin/lh.mp4 -ab 56 -ar 22050 -b 500 -s 320x240
     *            /usr/local/bin/lh.flv
     * @throws IOException
     */
    public static void converToFlv(String ffmpegPath, File sourceFile, String destination) throws IOException {
        // if(!sourceFile.exists()) throw new IOException("invalid file path");
        // if(!new File(destination).exists()) throw new IOException("invalid
        // file path "+destination);
        //  
        // String fileName = sourceFile.getName();
        // String surffix = fileName.substring(fileName.lastIndexOf(".")+1);
        // if(!isSurpportedType(surffix)) throw new
        // RuntimeException("unsurpported file type "+surffix);
        //  
        // List<String> cmdParam = new LinkedList<String>();
        // cmdParam.add(ffmpegPath);
        // cmdParam.add("-y");
        // cmdParam.add("-i");
        // cmdParam.add(sourceFile.getAbsolutePath());
        // cmdParam.add("-ab");
        // cmdParam.add("56");
        // cmdParam.add("-ar");
        // cmdParam.add("22050");
        // cmdParam.add("-b");
        // cmdParam.add("500");
        // cmdParam.add("-s");
        // cmdParam.add("320*240");
        // cmdParam.add(destination+FILE_SEPARATOR+fileName.substring(0,fileName.lastIndexOf("."))+".bmp");
        //
        // execCmd(cmdParam);
        converTo(ffmpegPath, sourceFile, destination, "flv");
    }

    public static void converTo(String ffmpegPath, File sourceFile, String destination, String pattern)
            throws IOException {
        if (!sourceFile.exists())
            throw new IOException("invalid file path");
        if (!new File(destination).exists())
            throw new IOException("invalid file path " + destination);

        String fileName = sourceFile.getName();
        String surffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!isSurpportedType(surffix))
            throw new RuntimeException("unsurpported file type " + surffix);

        List<String> cmdParam = new LinkedList<String>();
        cmdParam.add(ffmpegPath);
        cmdParam.add("-y");
        cmdParam.add("-i");
        cmdParam.add(sourceFile.getAbsolutePath());
        cmdParam.add("-ab");
        cmdParam.add("56");
        cmdParam.add("-ar");
        cmdParam.add("22050");
        cmdParam.add("-b");
        cmdParam.add("500");
        cmdParam.add("-s");
        cmdParam.add("720*572");
        cmdParam.add(destination + FILE_SEPARATOR + fileName.substring(0, fileName.lastIndexOf(".")) + "." + pattern);

        execCmd(cmdParam);
    }

    public static void main(String[] args) {
        String ffmpegPath = "E:\\workspace\\MIS\\mis\\WebRoot\\common\\ffmpeg.exe";
        //        File source = new File("D:\\EasyUI.avi");
                File source = new File("D:\\124.ts");
//                File source = new File("D:\\3.mp4");
//        File source = new File("D:\\hunan-1386309098-6.ts");
        try {
                        ImageConvertor.captureFirstFrame(ffmpegPath, source, "D:\\", "jpg");
//            ImageConvertor.vedioInfo(ffmpegPath, source, "D:\\", "jpg");
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 
     * 获取图片的第一帧 ffmpeg commandLine： ffmpeg -y -i /usr/local/bin/lh.3gp -vframes
     * 1 -r 1 -ac 1 -ab 2 -s 320x240 -f image2 /usr/local/bin/lh.jpg
     * 
     * @param sourceFile
     *            源文件
     * @param destination
     *            目标文件
     * @param surfix
     *            要保存的图片格式：jpg,jpeg,gif
     * @throws IOException
     */
    public static void captureFirstFrame(String ffmpegPath, File sourceFile, String destination, String surfix)
            throws IOException {
        if (!sourceFile.exists())
            throw new IOException("invalid file path");
        if (!new File(destination).exists())
            throw new IOException("invalid file path " + destination);

        String fileName = sourceFile.getName();
        String surffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!isSurpportedType(surffix))
            throw new RuntimeException("unsurpported file type " + surffix);

        List<String> cmd = new LinkedList<String>();
        cmd.add(ffmpegPath);
        cmd.add("-y");
        cmd.add("-i");
        cmd.add(sourceFile.getAbsolutePath());
        cmd.add("-vframes");
        cmd.add("1");
        cmd.add("-r");
        cmd.add("1");
        cmd.add("-ac");
        cmd.add("1");
        cmd.add("-ab");
        cmd.add("2");
        //        cmd.add("-ss");
        //        cmd.add("300");
        cmd.add("-s");
        cmd.add("640*520");
//        cmd.add("320*240");
        cmd.add("-f");
        cmd.add("image2");
        cmd.add(destination + FILE_SEPARATOR + fileName.substring(0, fileName.lastIndexOf(".")) + "." + surfix);

        execCmd(cmd);
    }

    public static void vedioInfo(String ffmpegPath, File sourceFile, String destination, String surfix)
            throws IOException {
        if (!sourceFile.exists())
            throw new IOException("invalid file path");
        if (!new File(destination).exists())
            throw new IOException("invalid file path " + destination);

        String fileName = sourceFile.getName();
        String surffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!isSurpportedType(surffix))
            throw new RuntimeException("unsurpported file type " + surffix);

        List<String> cmd = new LinkedList<String>();
        cmd.add(ffmpegPath);
        cmd.add("-i");
        cmd.add(sourceFile.getAbsolutePath());

        execCmd(cmd);
    }

    /**
     * 
     * @param cmd
     */
    private static void execCmd(List<String> cmd) {
        final ProcessBuilder pb = new ProcessBuilder();

        // 每个进程都有标准的输入、输出、和错误流。(stdin ,stdout ,stderr)
        // 合并子进程的 【错误流】和 常规的 【输出流】
        pb.redirectErrorStream(true);
        pb.command(cmd);

        try {
            final Process p = pb.start();

            OutputStream os = p.getOutputStream();
            InputStream in = p.getInputStream();
            // 开启单独的线程来处理输入和输出流，避免缓冲区满导致线程阻塞.
            new Thread(new Receiver(in)).start();
            new Thread(new Sender(os)).start();

            try {
                p.waitFor();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                // 唤醒当前线程
                Thread.currentThread().interrupt();
            }

            System.out.println("Child done");
            // at this point the child is complete. All of its output may or may
            // not have been processed however.
            // The Receiver thread will continue until it has finished
            // processing it.
            // You must close the streams even if you never use them! In this
            // case the threads close is and os.
            p.getErrorStream().close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * wrapper for Runtime.exec. No input/output. Optionally wait for child to
     * finish.
     * 
     * @param command
     *            fully qualified *.exe or *.com command
     * @param wait
     *            true if you want to wait for the child to finish.
     */
    public static Process exec(String command, boolean wait) {
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
        }
        catch (IOException e) {
            return null;
        }
        if (wait) {
            try {
                p.waitFor();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        // You must close these even if you never use them!
        try {
            p.getInputStream().close();
            p.getOutputStream().close();
            p.getErrorStream().close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    public static void main1(String[] args) {
        String ffmpegPath = "ffmpeg.exe";
        File file = new File("E:\\boot.mpg");
        try {
            converToFlv(ffmpegPath, file, "E:\\");
            converTo(ffmpegPath, file, "E:\\", "jpg");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        /*
         * for(int i=0;i<12;i++){ File file = new
         * File("F:\\\\3gp\\t"+(i+1)+".3gp"); getFirstFrame(ffmpegPath,
         * file,"F:\\\\3gp\\image\\","jpg"); }
         */

    }

    //ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）  
    private static String processVedio(String ffmpegPath,String sourcePath) {
        
        //检测文件路径合法性
        String surffix = sourcePath.substring(sourcePath.lastIndexOf(".") + 1);
        if (!isSurpportedType(surffix))
            throw new RuntimeException("unsurpported file type " + surffix);
        
        List<String> commend = new java.util.ArrayList<String>();
        commend.add(ffmpegPath);
        commend.add("-i");
        commend.add(sourcePath);

        try {

            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            builder.redirectErrorStream(true);
            Process p = builder.start();

            //1. start  
            BufferedReader buf = null; // 保存ffmpeg的输出结果流  
            String line = null;
            //read the standard output  

            buf = new BufferedReader(new InputStreamReader(p.getInputStream()));

            StringBuffer sb = new StringBuffer();
            while ((line = buf.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
                continue;
            }
            //int ret = 
            p.waitFor();//这里线程阻塞，将等待外部转换进程运行成功运行结束后，才往下执行  
            //1. end  
            return sb.toString();
        }
        catch (Exception e) {
//            e.printStackTrace(); 
            return null;
        }
    }
    /**
     * {获得视频信息：编码格式和视频格式}
     * 
     * @param ffmpegPath
     * @param sourcePath
     * @return
     * @author: wanghaotao
     */
    public static VedioInfo getVedioInfo(String ffmpegPath, String sourcePath) {
        String result = processVedio(ffmpegPath, sourcePath);
        if(result == null)
            return null;
        PatternCompiler compiler = new Perl5Compiler();
        String[] videoTypes;//解析视频时可能是多种格式，即是兼容格式。如mov,mp4,m4a,3gp,3g2,mj2
        try {
            String input = "Input (.*?), (.*?), (.*?)";
            String regexVideo = "Video: (.*?), (.*?), (.*?)[,\\s]";
            VedioInfo vedioRes = new VedioInfo();
            MatchResult matcheResult = getMatcherResult(result, compiler, input);
            if (matcheResult == null)
                return null;
            videoTypes = matcheResult.group(2).split(",");//mov,mp4,m4a,3gp,3g2,mj2
            VedioType vedioType = null;
            for(String vType:videoTypes){
                vedioType = VedioInfo.getVedioType(vType);
                if (vedioType != VedioType.ERROR) {//视频格式
                    vedioRes.setVedioType(vedioType);
                    break;
                }
            }
            if(vedioRes.getVedioType()==null){
                return null;
            }
            

            matcheResult = getMatcherResult(result, compiler, regexVideo);
            if (matcheResult == null)
                return null;
//            EncodeType encodeType = VedioInfo.getEncodeType(matcheResult.group(1));
            //edit for linux 2014年5月16日
            EncodeType encodeType = VedioInfo.getEncodeType(matcheResult.group(1).split(" ")[0]);
            vedioRes.setPixel(matcheResult.group(3).split(" ")[0]);
            if (encodeType == EncodeType.ERROR) {//编码格式
                return null;
            }
            vedioRes.setEncodeType(encodeType);
            return vedioRes;
        }
        catch (MalformedPatternException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MatchResult getMatcherResult(String result, PatternCompiler compiler, String input)
            throws MalformedPatternException {
        Pattern patternInput = compiler.compile(input,Perl5Compiler.CASE_INSENSITIVE_MASK);  
        PatternMatcher matcherInput = new Perl5Matcher();  
        if(matcherInput.contains(result, patternInput)){  
            MatchResult re = matcherInput.getMatch();  
            
            return re;
        }
        return null;
    }
    public static void main4(String[] args) {
        String ffmpegPath = "E:\\workspace\\IVOD\\mis\\WebRoot\\common\\ffmpeg.exe";

        VedioInfo info = getVedioInfo(ffmpegPath,"D:\\hunan-1386309098-6.ts");
        System.out.println(info);
    }
    public static void getVedioInfoDemo() {  
        String ffmpegPath = "E:\\workspace\\IVOD\\mis\\WebRoot\\common\\ffmpeg.exe";
        //        File source = new File("D:\\EasyUI.avi");
        //        File source = new File("D:\\3.mp4");
//        File source = new File("D:\\hunan-1386309098-6.ts");

        String result =    processVedio(ffmpegPath, "D:\\hunan-1386309098-6.ts");  
         
         
        PatternCompiler compiler =new Perl5Compiler();  
        try {  
            String input ="Input (.*?), (.*?), (.*?)";  
            String regexDuration ="Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";  
            String regexVideo ="Video: (.*?), (.*?), (.*?)[,\\s]";  
            String regexAudio ="Audio: (\\w*), (\\d*) Hz";  
         
            getMatcherResult(result, compiler, input);  
            Pattern patternDuration = compiler.compile(regexDuration,Perl5Compiler.CASE_INSENSITIVE_MASK);  
            PatternMatcher matcherDuration = new Perl5Matcher();  
            if(matcherDuration.contains(result, patternDuration)){  
                MatchResult re = matcherDuration.getMatch();  
      
                System.out.println("提取出播放时间  ===" +re.group(1));  
                System.out.println("开始时间        =====" +re.group(2));  
                System.out.println("bitrate 码率 单位 kb==" +re.group(3));  
            }  
             
            Pattern patternVideo = compiler.compile(regexVideo,Perl5Compiler.CASE_INSENSITIVE_MASK);  
            PatternMatcher matcherVideo = new Perl5Matcher();  
             
            if(matcherVideo.contains(result, patternVideo)){  
                MatchResult re = matcherVideo.getMatch();  
                System.out.println("编码格式  ===" +re.group(1));  
                System.out.println("视频格式 ===" +re.group(2));  
                System.out.println(" 分辨率  == =" +re.group(3));  
            }  
             
            Pattern patternAudio = compiler.compile(regexAudio,Perl5Compiler.CASE_INSENSITIVE_MASK);  
            PatternMatcher matcherAudio = new Perl5Matcher();  
             
            if(matcherAudio.contains(result, patternAudio)){  
                MatchResult re = matcherAudio.getMatch();  
                System.out.println("音频编码             ===" +re.group(1));  
                System.out.println("音频采样频率  ===" +re.group(2));  
            }  
      
        } catch (MalformedPatternException e) {  
            e.printStackTrace();  
        }  
      
        }  
}

final class Sender implements Runnable {
    private static final String lineSeparator = System.getProperty("line.separator");

    private final OutputStream os;

    public void run() {
        try {
            final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os), 500);
            for (int i = 99; i >= 0; i--) {
                bw.write("There are " + i + " bottles of beer on the wall, " + i + " bottles of beer.");
                bw.write(lineSeparator);
            }
            bw.close();
        }
        catch (IOException e) {
            throw new IllegalArgumentException("IOException sending data to child process.");
        }
    }

    Sender(OutputStream os) {
        this.os = os;
    }
}

final class Receiver implements Runnable {
    private final InputStream is;

    public void run() {
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(is), 500);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        }
        catch (IOException e) {
            throw new IllegalArgumentException("IOException receiving data from child process.");
        }
    }

    Receiver(InputStream is) {
        this.is = is;
    }
}
