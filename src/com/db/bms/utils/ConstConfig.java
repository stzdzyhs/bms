package com.db.bms.utils;
import java.util.LinkedHashMap;
import java.util.Map;

import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.EntityType;

public class ConstConfig {
	
	public static Map<Integer, String> featureCodeMap = new LinkedHashMap<Integer, String>();
	
	public static Map<Integer, String> featureCodeStatus = new LinkedHashMap<Integer, String>();
	
    public static Map<Integer, String> userStatusMap = new LinkedHashMap<Integer, String>(0);
    
    public static Map<Integer, String> userTypeMap = new LinkedHashMap<Integer, String>(0);
    
    public static Map<Integer, String> logBackupTypeMap = new LinkedHashMap<Integer, String>(0);
    
    public static Map<Integer, String> logRunTypeMap = new LinkedHashMap<Integer, String>(0);
    
    public static final String COMPANY_CONFIG_FILE_PATH = "opermgmt/systemConfig/";
    
	public static final String UPLOAD_PATH = "files/upload/"; 
	
	public static final String TOPIC_FILE_PATH = "picmgmt/topic/";
	
	public static final String ALBUM_FILE_PATH = "picmgmt/album/";
	
	public static final String COLUMN_FILE_PATH = "textmgmt/column/";
	
	public static final String PIC_FILE_PATH = "picmgmt/picture/";
	
	public static final String TEMP_PIC_FILE_PATH = "picmgmt/picture/temp/";
	
	public static final String DOWNLOAD_PATH = "files/download/";
	
	public static final String TEMPLATE_FILE_PATH = "opermgmt/template/";
	
	public static final String PREVIEW_PATH = "files/preview/";
	
	public static final String FEATURECODE_PATH = "opermgmt/featureCode/";
	
	public static final String WORDSTOCK_PATH = "textmgmt/wordstock/";
	
	public static Map<Integer, String> auditStatusMap = new LinkedHashMap<Integer, String>(0);
	
	public static Map<Integer, String> cardRegionTypeMap = new LinkedHashMap<Integer, String>(0);
	
	public static Map<Integer, String> cardRegionCodeTypeMap = new LinkedHashMap<Integer, String>(0);
	
	public static final String overdue_fail_reason = "处理超时！";
	
	public static Map<Integer, String> topicTypeMap = new LinkedHashMap<Integer, String>(0);
	
	public static Map<Integer, String> topicStatusMap = new LinkedHashMap<Integer, String>(0);
	
	public static Map<Integer, String> albumStatusMap = new LinkedHashMap<Integer, String>(0);
	
	public static Map<Integer, String> articleStatusMap = new LinkedHashMap<Integer, String>(0);
	
	// audit status name
	public static final int DRAFT = 0;
	public static final int AUDITING=(1);
	public static final int AUDIT_PASS=(2);
	public static final int AUDIT_NO_PASS=(3);
	public static final int PUBLISH=(4);
	public static final int UNPUBLISH=(5);
	
    public static final int STATUS_EDIT=0;
    public static final int STATUS_SUBMIT=1;
    public static final int STATUS_PASS=2;
    public static final int STATUS_FAIL=3;
    public static final int STATUS_PUBLISH=4;
    public static final int STATUS_UNPUBLISH=5; //a fake status
    
	public static Map<Integer, String> pictureStatusMap = new LinkedHashMap<Integer, String>(0);
	
	public static Map<Integer, String> pictureFormatMap = new LinkedHashMap<Integer, String>(0);
	
	public static Map<Integer, String> pictureVoteFlagMap = new LinkedHashMap<Integer, String>(0);
	
	public static Map<Integer, String> portalStatusMap = new LinkedHashMap<Integer, String>(0);
	
	public static Map<Integer, String> videoStatusMap = new LinkedHashMap<Integer, String>(0);
	
	public static Map<Integer, String> columnStatusMap = new LinkedHashMap<Integer, String>(0);
	
	public static Map<Integer, String> captureFlagMap = new LinkedHashMap<Integer, String>(0);
	
	public static Map<Integer, String> resourceTypeMap = new LinkedHashMap<Integer, String>(0);
	
	// parentTypeMap is not used. just use: resourceTypeMap
	//public static Map<Integer, String> parentTypeMap = new LinkedHashMap<Integer, String>(0);
	
	public static Map<Integer, String> templateTypeMap = new LinkedHashMap<Integer, String>(0);
	
	public static Map<Integer, String> templateStatusMap = new LinkedHashMap<Integer, String>(0);

	/**
	 * application/json utf8 
	 */
	public final static String APP_JSON = "application/json;charset=utf-8";
	public final static String TEXT_PLAIN = "text/plain;charset=utf-8";
	
	public final static String DEF_ENCODING="utf8";
	
	/*
	public static final String ARTICLE_DIR="article.dir";
	public static final String TEMPLATE_DIR="template.dir";
	public static final String PAGE_DIR="page.dir";
	*/	
	
    static {
        
        userStatusMap.put(0, "启用");
        userStatusMap.put(1, "禁用");
        
        userTypeMap.put(1, "管理员");
        userTypeMap.put(2, "普通用户");
        
        logBackupTypeMap.put(1, "一个月前");
        logBackupTypeMap.put(2, "三个月前");
        logBackupTypeMap.put(3, "半年前");
        logBackupTypeMap.put(4, "一年前");
        logBackupTypeMap.put(5, "两年前");
        
        logRunTypeMap.put(1, "每天");
        logRunTypeMap.put(2, "每周");
        logRunTypeMap.put(3, "每月");
        
        auditStatusMap.put(AuditStatus.DRAFT, "草稿");
        auditStatusMap.put(AuditStatus.AUDITING, "正在审核");
        auditStatusMap.put(AuditStatus.AUDIT_PASS, "审核通过");
        auditStatusMap.put(AuditStatus.AUDIT_NO_PASS, "审核不通过");
        auditStatusMap.put(AuditStatus.PUBLISH, "启用");
        auditStatusMap.put(AuditStatus.UNPUBLISH, "禁用");

        cardRegionTypeMap.put(0, "区域");
        cardRegionTypeMap.put(1, "分段");

        cardRegionCodeTypeMap.put(0, "单码");
        cardRegionCodeTypeMap.put(1, "分段");
        
        topicTypeMap.put(1, "生成页面");
        topicTypeMap.put(2, "不生成页面");
        
        topicStatusMap.put(0, "编辑");
        topicStatusMap.put(1, "提交审核");
        topicStatusMap.put(2, "审核通过");
        topicStatusMap.put(3, "审核不通过");
        topicStatusMap.put(4, "发布");
        
//        albumStatusMap.put(0, "编辑");
//        albumStatusMap.put(1, "提交审核");
//        albumStatusMap.put(2, "审核通过");
//        albumStatusMap.put(3, "审核不通过");
//        albumStatusMap.put(4, "发布");
        albumStatusMap.put(AuditStatus.DRAFT, "草稿");
        albumStatusMap.put(AuditStatus.AUDITING, "正在审核");
        albumStatusMap.put(AuditStatus.AUDIT_PASS, "审核通过");
        albumStatusMap.put(AuditStatus.AUDIT_NO_PASS, "审核不通过");
        albumStatusMap.put(AuditStatus.PUBLISH, "发布");
        albumStatusMap.put(AuditStatus.UNPUBLISH, "取消发布");
        
        articleStatusMap.put(0, "编辑");
        articleStatusMap.put(1, "提交审核");
        articleStatusMap.put(2, "审核通过");
        articleStatusMap.put(3, "审核不通过");
        articleStatusMap.put(4, "发布");
        
        pictureStatusMap.put(STATUS_EDIT, "编辑");
        pictureStatusMap.put(STATUS_SUBMIT, "提交审核");
        pictureStatusMap.put(STATUS_PASS, "审核通过");
        pictureStatusMap.put(STATUS_FAIL, "审核不通过");
        pictureStatusMap.put(STATUS_PUBLISH, "发布");
        pictureStatusMap.put(STATUS_UNPUBLISH, "取消发布");
        
        pictureFormatMap.put(1, "jpg");
        pictureFormatMap.put(2, "gif");
        pictureFormatMap.put(3, "png");
        
        pictureVoteFlagMap.put(0, "否");
        pictureVoteFlagMap.put(1, "是");
        
        portalStatusMap.put(0, "编辑");
        portalStatusMap.put(1, "启用");
        portalStatusMap.put(2, "禁用");
        
        videoStatusMap.put(0, "编辑");
        videoStatusMap.put(1, "正在提交");
        videoStatusMap.put(2, "提交成功");
        videoStatusMap.put(3, "提交失败");
        videoStatusMap.put(4, "正在下载");
        videoStatusMap.put(5, "下载失败");
        videoStatusMap.put(6, "正在截图");
        videoStatusMap.put(7, "截图失败");
        videoStatusMap.put(8, "正在注入");
        videoStatusMap.put(9, "截图完成");
        videoStatusMap.put(10, "注入失败");
        
        featureCodeMap.put(0, "用户喜好特征");
        featureCodeMap.put(1, "地理位置特征");
        featureCodeMap.put(2, "大客户特征");
        featureCodeMap.put(3, "用户年龄特征");
        
        featureCodeStatus.put(0, "禁用");
        featureCodeStatus.put(1, "启用");
        
        columnStatusMap.put(0, "编辑");
        columnStatusMap.put(1, "启用");
        columnStatusMap.put(2, "禁用");
        
        captureFlagMap.put(0, "否");
        captureFlagMap.put(1, "是");
        
        resourceTypeMap.put(EntityType.TYPE_TOPIC, "专题");
        resourceTypeMap.put(EntityType.TYPE_MENU, "栏目");
        resourceTypeMap.put(EntityType.TYPE_ALBUM, "相册");
        resourceTypeMap.put(EntityType.TYPE_PICTURE, "图片");
        resourceTypeMap.put(EntityType.TYPE_COLUMN, "版块");
        resourceTypeMap.put(EntityType.TYPE_ARTICLE, "文章");
        
        /*
        parentTypeMap.put(0, "专题");
        parentTypeMap.put(1, "栏目");
        parentTypeMap.put(2, "相册");
        parentTypeMap.put(3, "版块");
        */
        
        templateTypeMap.put(1, "专题");
        templateTypeMap.put(2, "相册");
        templateTypeMap.put(3, "图片");
        templateTypeMap.put(4, "版块");
        templateTypeMap.put(5, "文章");
        
        templateStatusMap.put(0, "编辑");
        templateStatusMap.put(1, "启用");
        templateStatusMap.put(2, "禁用");
        
    }

    private ConstConfig() {    
    }
    
 
    public static void checkStatusValid(Integer status) throws Exception {
		if(status==null || status< ConstConfig.STATUS_EDIT || status > ConstConfig.STATUS_UNPUBLISH) {
			throw new IllegalArgumentException("无效的状态");
		}
    }
}
