package com.judicature.databackend.vo;

import java.util.Map;

public class RelationVO {
    private Long identity;
    private Long start;
    private Long end;
    private String type;
    private Map<String,String> properties;


    public Long getIdentity() {
        return identity;
    }

    public void setIdentity(Long ientity) {
        this.identity = ientity;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Long getStart() {
        return start;
    }

    public Long getEnd() {
        return end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}

