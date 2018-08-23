package com.cxcy.zjb.springboot.domain;


public class JsonObject {
    private int code = 0;

    private Object data;

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "JsonObject [code=" + code + ", data=" + data + ", msg=" + msg + "]";
    }


}

