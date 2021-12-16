package com.judicature.databackend.vo;

import com.judicature.databackend.enums.OperationType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class HistoryVO {
    private String id;
    private OperationType operationType;
    private String date;
    private Long objectId;
    private Map<String, String> properties;

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    public HistoryVO() {
    }

    public HistoryVO(OperationType operationType, Long identity) {
        this.setOperationType(operationType);
        this.setObjectId(identity);
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT);
        this.setDate(dateTime.format(formatter));
    }

    public HistoryVO(OperationType operationType, Long identity, Map<String, String> properties) {
        this.setOperationType(operationType);
        this.setObjectId(identity);
        this.setProperties(properties);
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT);
        this.setDate(dateTime.format(formatter));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
