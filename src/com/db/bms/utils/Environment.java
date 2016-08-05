package com.db.bms.utils;

import java.io.File;

/**
 * 获得根目录路径
 * 
 * 
 */
public final class Environment {
    
    public static final String APPLICATION_PATH = Environment.class.getResource("/").getPath().replaceAll("%20", " ");

    public static final String FILE_SYSTEM_DIRECTORY = APPLICATION_PATH + File.separator + ".." + File.separator + ".."
            + File.separator;
}
