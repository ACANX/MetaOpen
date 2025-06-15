package com.acanx.meta.model.deepseek.enums;

public enum DeepSeekModel {

    CHAT("deepseek-chat"),

    R1("deepseek-reasoner");


    private String model;

    private DeepSeekModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }
}
