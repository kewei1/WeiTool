package com.github.kewei1.conversion;

import java.util.List;

public  class JsonMetaNode {
    private String key;
    private String valueType;
    //数据库中的列名
    private String dbColName;
    private List<JsonMetaNode> children;
    public JsonMetaNode() {
    }
    public JsonMetaNode(String key, String valueType) {
        this.key = key;
        this.valueType = valueType;
    }
    public String getKey() {
        return key;
    }
    public String getValueType() {
        return valueType;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
}
