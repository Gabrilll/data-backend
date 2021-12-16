package com.judicature.databackend.vo;

/**
 * @author Gabri
 */
public class PropertyVO {
    private String key;
    private String value;

    public PropertyVO(){}

    public PropertyVO(String key,String value){
        this.key=key;
        this.value=value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
