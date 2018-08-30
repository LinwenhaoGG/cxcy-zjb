package com.cxcy.zjb.springboot.Vo;

import lombok.Data;

/**
 * Created on 2018/3/3 0003.
 * 传输文件
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
@Data
public class FileInfo {
    private int success = 1;
    private String message = "上传成功";
    private String url;

    public FileInfo() {
    }

    public FileInfo(int success, String message, String url) {
        this.success = success;
        this.message = message;
        this.url = url;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
