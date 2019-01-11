package com.example.demo.service.quartz;


import com.example.demo.commons.ExcelExportUtil;
import com.example.demo.commons.FTPConfigBean;
import com.example.demo.commons.FTPUtil;
import com.example.demo.mapper.TaskMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TC20021 on 2018/12/27.
 */
@Service
public class TaskService {


    @Autowired
    TaskMapper taskMapper;
    @Autowired
    private FTPConfigBean ftpConfigBean;

    /**
     * 定时任务查询数据库的sql
     */
    public Map<String ,Object> getQueryAll(){
        Map<String ,Object> maps=taskMapper.getQueryAll();

        return  maps;
    }

    /**
     * 进行文件上传处理，返回文嘉所在路径
     */
    public String uploadFiless(List<Map<String, Object>> objs,String[] labels,String[] fields )throws Exception{
        String tabName = "aaa";

        Workbook excel = ExcelExportUtil.createExcelByMap("报表.xlsx", labels, fields, objs, "报表", true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            excel.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] b = os.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(b);



        return getFTPUrl(tabName,inputStream);
    }

    ///
    public String getFTPUrl(String tabName,ByteArrayInputStream inputStream) {
        HashMap<String, String> map2 = new HashMap<>();
        String ftp_ip = ftpConfigBean.getFTP_IP();
        String ftp_port = ftpConfigBean.getFTP_PORT();
        String ftp_filepath = ftpConfigBean.getFTP_FILEPATH();
        String ftp_basepath = ftpConfigBean.getFTP_BASEPATH();
        String ftp_username = ftpConfigBean.getFTP_USERNAME();
        String ftp_password = ftpConfigBean.getFTP_PASSWORD();
        map2.put("ip",ftp_ip);
        map2.put("port",ftp_port);
        map2.put("filepath",ftp_filepath);
        map2.put("basepath",ftp_basepath);
        map2.put("username",ftp_username);
        map2.put("password",ftp_password);
        String ftpFullPath;
        //上传图片并返回图片新的名称
        String imageNewname = FTPUtil.uploadFile(tabName,inputStream,map2);
        //判断filepath是否是空路径
        if (ftp_filepath.equals("/")) {
            ftpFullPath = ftp_basepath + "/" + imageNewname;
        }else{
            ftpFullPath = ftp_basepath + "/" + ftp_filepath + "/" + imageNewname;
        }
        System.out.println(ftpFullPath);
        return ftpFullPath;
    }
}
