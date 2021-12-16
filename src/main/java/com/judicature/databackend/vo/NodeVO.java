package com.judicature.databackend.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeVO {
    private Long identity;
    private List<String> labels;
    private Map<String, String> properties;
    private String cls;

    public Long getIdentity() {
        return identity;
    }

    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void addProperties(Map<String, String> properties) {
        if (properties == null) {
            this.properties = new HashMap<>();
        }
        assert properties != null;
        for (Map.Entry<String, String> ent : properties.entrySet()) {
            this.properties.put(ent.getKey(), ent.getValue());
        }
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public void addProperty(String name, String value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(name, value);
    }

    public String getPropertyValueByName(String name) {
        for (Map.Entry<String, String> ent : properties.entrySet()) {
            if (ent.getKey().equals(name)) {
                return ent.getValue();
            }
        }
        return null;
    }
}
