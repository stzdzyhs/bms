package com.db.bms.utils;

public class VedioInfo {

    /** 
     * 编码格式
     */
    public enum EncodeType {
        H264("h264"), MPEG2VIDEO("mpeg2video"),MP4("mpeg4"),ERROR("other");

        EncodeType(String name) {
            this.name = name;
        }

        private String name;

        public String getName() {
            return name;
        }
    }

    /** 
     * 视频格式
     */
    public enum VedioType {
        TS("mpegts"), MP4("mp4"),ERROR("other");

        VedioType(String name) {
            this.name = name;
        }

        private String name;

        public String getName() {
            return name;
        }

    }

    private EncodeType encodeType;

    private VedioType vedioType;
    
    private String pixel;

    public EncodeType getEncodeType() {
        return encodeType;
    }

    public void setEncodeType(EncodeType encodeType) {
        this.encodeType = encodeType;
    }

    public VedioType getVedioType() {
        return vedioType;
    }

    public void setVedioType(VedioType vedioType) {
        this.vedioType = vedioType;
    }
    
    

    public String getPixel() {
		return pixel;
	}

	public void setPixel(String pixel) {
		this.pixel = pixel;
	}

	public static EncodeType getEncodeType(String name){
        for(EncodeType type:EncodeType.values()){
            if(type.getName().equals(name)){
                return type;
            }
        }
        return EncodeType.ERROR;
    }
    public static VedioType getVedioType(String name){
        for(VedioType type:VedioType.values()){
            if(type.getName().equals(name)){
                return type;
            }
        }
        return VedioType.ERROR;
    }
}
