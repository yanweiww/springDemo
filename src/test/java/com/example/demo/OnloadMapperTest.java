package com.example.demo;

import com.example.demo.commons.ExcelExportUtil;
import com.example.demo.commons.FTPConfigBean;
import com.example.demo.commons.FTPUtil;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TC20021 on 2018/12/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OnloadMapperTest {


    @Autowired
    private FTPConfigBean ftpConfigBean;

    @Test
    public void getLoanBase() throws Exception{

        List<Map<String, Object>> objs = new ArrayList<>();
        Map<String, Object> map=new HashMap<>();
        map.put("name","name");
        map.put("age","age");
        objs.add(map);
        String[] labels={"姓名","年龄"};
        String[] fields={"name","age"};
        Workbook excel = ExcelExportUtil.createExcelByMap("报表.xlsx", labels, fields, objs, "报表", true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            excel.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] b = os.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(b);





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
        String tabName = "王五";
        //上传图片并返回图片新的名称
        String imageNewname = FTPUtil.uploadFile(tabName, inputStream,map2);
        //判断filepath是否是空路径
        if (ftp_filepath.equals("/")) {
            ftpFullPath = ftp_basepath + "/" + imageNewname;
        }else{
            ftpFullPath = ftp_basepath + "/" + ftp_filepath + "/" + imageNewname;
        }
        System.out.println(ftpFullPath);
       // return ftpFullPath;

    }
}
