package com.db.bms.entity;

public class SystemConfig extends BaseModel implements java.io.Serializable {

    private Long configNo;

    private String property;

    private String value;

    private String configDescrible;

    private Long companyNo;

    public Long getConfigNo() {

        return this.configNo;

    }

    public void setConfigNo(Long configNo) {

        this.configNo = configNo;

    }

    public String getProperty() {

        return this.property;

    }

    public void setProperty(String property) {

        this.property = property;

    }

    public String getValue() {

        return this.value;

    }

    public void setValue(String value) {

        this.value = value;

    }

    public String getConfigDescrible() {

        return this.configDescrible;

    }

    public void setConfigDescrible(String configDescrible) {

        this.configDescrible = configDescrible;

    }

    public Long getCompanyNo() {

        return this.companyNo;

    }

    public void setCompanyNo(Long companyNo) {

        this.companyNo = companyNo;

    }

    @Override
    public void setDefaultNull() {
        // TODO Auto-generated method stub

    }

}