package com.hjzddata.modular.common.model;

public class JsonResult {
    private Integer code;
    private String message;
    private Object data;

    public JsonResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
