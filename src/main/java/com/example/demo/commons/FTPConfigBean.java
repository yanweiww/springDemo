package com.example.demo.commons;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FTPConfigBean {

    @Value("${ftp.ip}")//服务器ip地址
    private String FTP_IP;

    @Value("${ftp.port}")//端口
    private String FTP_PORT;

    @Value("${ftp.filepath}")//文件保存路径
    private String FTP_FILEPATH;

    @Value("${ftp.username}")//用户名
    private String FTP_USERNAME;

    @Value("${ftp.password}")//密码
    private String FTP_PASSWORD;

    @Value("${ftp.basepath}")//服务器访问路径
    private String FTP_BASEPATH;

    public String getFTP_IP() {
        return FTP_IP;
    }

    public String getFTP_PORT() {
        return FTP_PORT;
    }

    public String getFTP_FILEPATH() {
        return FTP_FILEPATH;
    }

    public String getFTP_USERNAME() {
        return FTP_USERNAME;
    }

    public String getFTP_PASSWORD() {
        return FTP_PASSWORD;
    }

    public String getFTP_BASEPATH() {
        return FTP_BASEPATH;
    }

    public void setFTP_IP(String FTP_IP) {
        this.FTP_IP = FTP_IP;
    }

    public void setFTP_PORT(String FTP_PORT) {
        this.FTP_PORT = FTP_PORT;
    }

    public void setFTP_FILEPATH(String FTP_FILEPATH) {
        this.FTP_FILEPATH = FTP_FILEPATH;
    }

    public void setFTP_USERNAME(String FTP_USERNAME) {
        this.FTP_USERNAME = FTP_USERNAME;
    }

    public void setFTP_PASSWORD(String FTP_PASSWORD) {
        this.FTP_PASSWORD = FTP_PASSWORD;
    }

    public void setFTP_BASEPATH(String FTP_BASEPATH) {
        this.FTP_BASEPATH = FTP_BASEPATH;
    }
}
