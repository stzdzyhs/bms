package com.db.bms.entity;

import java.util.HashMap;
import java.util.Map;

public class CommandNameMap {

	public static Map<Long,String> cmdNameMap = new HashMap<Long, String>();
	static{
		cmdNameMap.put(1l, "关联相册");
		cmdNameMap.put(2l, "发布相册");
		cmdNameMap.put(3l, "新增图片");
		cmdNameMap.put(4l, "审核图片");
		cmdNameMap.put(5l, "发布图片");
		cmdNameMap.put(6l, "关联子版块");
		cmdNameMap.put(7l, "添加文章");
	}
}
