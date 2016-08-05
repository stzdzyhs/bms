package com.db.bms.entity;

public class LogType extends BaseModel {

    private Long logTypeNo;

    private String logTypeName;

    public Long getLogTypeNo() {

        return this.logTypeNo;

    }

    public void setLogTypeNo(Long logTypeNo) {

        this.logTypeNo = logTypeNo;

    }

    public String getLogTypeName() {

        return this.logTypeName;

    }

    public void setLogTypeName(String logTypeName) {

        this.logTypeName = logTypeName;

    }

    @Override
    public void setDefaultNull() {
        // TODO Auto-generated method stub

    }

}