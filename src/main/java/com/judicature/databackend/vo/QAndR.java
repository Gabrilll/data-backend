package com.judicature.databackend.vo;

import java.util.List;

public class QAndR {
    String answer;
    List<String> recommend;

    public void setAnswer(String answer){this.answer=answer;}

    public void setRecommend(List<String> recommend) {this.recommend=recommend;}

    public String getAnswer(){return this.answer;}

    public List<String> getRecommend(){return this.recommend;}
}
