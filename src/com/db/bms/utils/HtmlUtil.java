package com.db.bms.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class HtmlUtil {
    /**
     * {导出文件}
     * 
     * @param fileName 导出文件名，包括后缀名
     * @param content   文件内容
     * @param response
     * @author: wanghaotao
     */
    public static void outPutFile(String fileName, String content, HttpServletResponse response) {
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        OutputStream output = null;
        try {
            output = response.getOutputStream();
            output.write(content.getBytes());
            output.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (output != null) {
                try {
                    output.close();
                }
                catch (IOException e) {
                }
                output = null;
            }
        }
    }

    /**
     * 
     * <br>
     * <b>功能：</b>输出json格式<br>
     * @param response
     * @param jsonStr
     * @throws Exception
     */
    public static void writerJson(HttpServletResponse response, String jsonStr) {
        writer(response, jsonStr);
    }

    public static void writerJson(HttpServletResponse response, Object object) {
        response.setContentType("application/json");
        writer(response, JsonUtil.toJson(object));
    }

    /**
     * 
     * <br>
     * <b>功能：</b>输出HTML代码<br>
     * @param response
     * @param htmlStr
     * @throws Exception
     */
    public static void writerHtml(HttpServletResponse response, String htmlStr) {
        writer(response, htmlStr);
    }

    /**
     * {往html页面写数据,html显示形式依赖于contentType,不填默认为text/html网页格式}
     * 
     */
    private static void writer(HttpServletResponse response, String str) {
        try {
            //设置页面不缓存
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = null;
            out = response.getWriter();
            out.print(str);
            out.flush();
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
