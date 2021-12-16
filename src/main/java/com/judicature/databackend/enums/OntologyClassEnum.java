package com.judicature.databackend.enums;

public enum OntologyClassEnum {
    // 疾病
    DISEASE(-1, "疾病"),
    // 组织
    ORGANIZATION(-2, "组织"),
    // 人物
    PERSON(-3, "人物"),
    // 文件
    DOCUMENT(-4, "文件"),
    // 机构
    INSTITUTE(-5, "机构"),
    // 医院
    HOSPITAL(-6, "医院"),
    // 生物
    LIVING(-7, "生物"),
    // 伤害
    HARM(-8, "伤害"),
    // 其它
    OTHERS(-9, "其它"),
    //传播
    PROPAGATION(-10,"传播"),
    //治疗
    TREAT(-11,"治疗"),
    //预防
    PREVENTION(-12,"预防"),
    //药物
    DRUG(-13,"药物"),
    //症状
    SYMPTOM(-14,"症状"),
    //时间
    TIME(-15,"时间"),
    //禁忌
    FORBID(-16,"禁忌"),
    //用法用量
    USAGE(-17,"用法用量"),
    //副作用
    SIDE_EFFECT(-18,"副作用"),
    //事件
    EVENT(-19,"事件"),
    //位置
    LOCATION(-20,"位置"),
    //科属种
    CATEGORY(-21,"类别"),
    //习性
    HABIT(-22,"习性");

    private int index;
    private String name;

    private OntologyClassEnum(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
