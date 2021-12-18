package com.judicature.databackend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DocumentVO {
    private String id;
    private String name;
    private List<String> keywords;
    private String text;
}
