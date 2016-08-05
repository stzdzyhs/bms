package com.db.bms.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 用来读取SPRING配置文件的辅助类
 * 
 */
public class SpringContext implements ApplicationContextAware {
    private static ApplicationContext ctx;

    public static Object getBean(String strBean) {

        return ctx.getBean(strBean);
    }

    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        ctx = arg0;
    }
}
