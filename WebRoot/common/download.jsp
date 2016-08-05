<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.io.OutputStream"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>下载</title>

</head>
<body>
<%
    //关于文件下载时采用文件流输出的方式处理：
    //加上response.reset()，并且所有的％>后面不要换行，包括最后一个；
    //因为Application Server在处理编译jsp时对于％>和<％之间的内容一般是原样输出，而且默认是PrintWriter，
    //而你却要进行流输出：ServletOutputStream，这样做相当于试图在Servlet中使用两种输出机制，
    //就会发生：getOutputStream() has already been called for this response的错误
    //详细请见《More Java Pitfill》一书的第二部分 Web层Item 33：试图在Servlet中使用两种输出机制 270
    //而且如果有换行，对于文本文件没有什么问题，但是对于其它格式，比如AutoCAD、Word、Excel等文件
    //下载下来的文件中就会多出一些换行符0x0d和0x0a，这样可能导致某些格式的文件无法打开，有些也可以正常打开。

    String fileName = request.getParameter("path");
    response.reset();//可以加也可以不加
    response.setContentType("application/x-download");//设置为下载application/x-download
    // /../../退WEB-INF/classes两级到应用的根目录下去，注意Tomcat与WebLogic下面这一句得到的路径不同，WebLogic中路径最后没有/
    String filenamedownload = request.getRealPath("common/"+fileName);
    String fencode = request.getParameter("fencode");
    if(fencode==null || fencode.isEmpty()){
    	fencode = "UTF-8";
    }
    fileName = URLEncoder.encode(fileName,"UTF-8");
    response.addHeader("Content-Disposition","attachment;filename=" + fileName);

    OutputStream output = null;
    FileInputStream fis = null;
    try
    {
        output  = response.getOutputStream();
        fis = new FileInputStream(filenamedownload);
        byte[] b = new byte[1024];
        int i = 0;

        while((i = fis.read(b)) > 0)
        {
            output.write(b, 0, i);
        }
        output.flush();
        out.clear(); 
    	out = pageContext.pushBody(); 
    }
    catch(Exception e)
    {
        System.out.println("Error!");
        e.printStackTrace();
    }
    finally
    {
        if(fis != null)
        {
            fis.close();
            fis = null;
        }
        if(output != null)
        {
            output.close();
            output = null;
        }
    }
%>
</body>
</html>