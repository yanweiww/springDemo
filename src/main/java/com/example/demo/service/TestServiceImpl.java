package com.example.demo.service;

import com.example.demo.commons.FTPConfigBean;
import com.example.demo.commons.FTPUtil;
import com.example.demo.entity.RpReportName;
import com.example.demo.entity.TableRelation;
import com.example.demo.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.*;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private FTPConfigBean ftpConfigBean;

    /**
     * 加载表信息展示
     * @return
     */
    @Override
    public List<Map<String, Object>> getDatas() {
        //获取表数据
        List<String> tableNames = testMapper.getTableNames();
        //定义list集合装载所有表数据
        List<Map<String, Object>> allDatas = new ArrayList<>();
        for (String tableName : tableNames) {
            //定义map集合封装单表数据
            Map<String, Object> tableData = new HashMap<>();
            tableData.put("tableName", tableName);
            List<Map<String, String>> tableFields = testMapper.getTableFields(tableName);
            tableData.put("tableFields", tableFields);
            allDatas.add(tableData);
        }
        return allDatas;
    }


    /**
     * 按选择字段查询
     * @param nameData
     * @param tabData
     * @param fieldMean
     */
    @Override
    public void queryDatas(String nameData, String tabData, String fieldMean) {
        List<String> fields = Arrays.asList(nameData.split(","));
        List<String> tabNames = Arrays.asList(tabData.split(","));

        //拼接查询字段
        StringBuilder str1 = new StringBuilder(" ");
        for (int i = 0; i < fields.size();i++){
            if (i != fields.size()-1){
                str1.append(fields.get(i)).append(",");
            }else {
                str1.append(fields.get(i));
            }
        }
        String sql1 = "select " + str1.toString() + " from loan_base_dsj ";

        //拼接表连接
        StringBuilder str2 = new StringBuilder(" ");
        for (int i = 0; i < tabNames.size();i++){
            if (!"loan_base_dsj".equals(tabNames.get(i))){
                StringBuilder str3 = new StringBuilder(" left join ");
                str3.append(tabNames.get(i)).append(" on loan_base_dsj.id = ").append(tabNames.get(i)).append(".id ");
                str2.append(str3);
            }
        }
        String sql = sql1 + str2.toString();
        System.out.println(sql);
    }

    /**
     * 获取表名称
     * @return
     */
    @Override
    public List<RpReportName> getTabs() {
        return testMapper.getTabs();
    }

    /**
     * 根据id查询需要条件查询的字段和展示类型
     * @param reportId
     * @return
     */
    @Override
    public List<Map<String, Object>> getAllQueryFields(Integer reportId) {
        List<Map<String, Object>> results = testMapper.getAllQueryFields(reportId);
        if (results.size()!=0) {
            //将别名字段取出切割
            String whereStrings = (String) results.get(0).get("whereString");
            String[] split = whereStrings.split(",");
            //重新定义集合装载数据
            List<Map<String, Object>> allQueryFields = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                //定义map装载单个数据组
                Map<String, Object> map = new HashMap<>();
                map.put("fieldString", results.get(i).get("fieldString"));
                map.put("fieldType", results.get(i).get("fieldType"));
                map.put("showField", results.get(i).get("showField"));
                map.put("whereString", split[i]);
                allQueryFields.add(map);
            }
            return allQueryFields;
        }
        return null;
    }

    /**
     * 根据id查询表字段拼接sql
     * @param reportId
     * @param whereFields
     */
    @Override
    public Map<String,Object> getQuerySql(Integer reportId, String whereFields) {
        RpReportName rpReportName = testMapper.getQuerySql(reportId);
        //获取并切割sqlString
        String sqlString = rpReportName.getSqlString();
        String[] sqlStrings = sqlString.split(",");
        //获取并切割tableString
        String tableString = rpReportName.getTableString();
        String[] tableStrings = tableString.split(",");
        //获取并切割whereString
        String whereString = rpReportName.getWhereString();
        String[] whereStrings = whereString.split(",");


        //拼接sql
        String str = "select ";
        //取出全部字段
        for(String field:sqlStrings){
            str += field + ",";
        }
        str = str.substring(0,str.length()-1);
        str += "  from  loan_base_dsj ";
        //遍历表名数组取出集合
        for(String tableTwo:tableStrings){
            List<TableRelation> serviceResult1 = testMapper.getSql(tableTwo);
            //遍历集合取出字段
            for(TableRelation tab:serviceResult1){
                str += tab.getTableRelation()+"  " +tableTwo+"  on  "
                        + tab.getTableOne()+"."+tab.getFieldOne()+" = "+ tab.getTableTwo()+"."+tab.getFieldTwo()+" "
                        + " ";
            }
        }

        //拼接where字段
        //=111& , =& , =333& , =
        String whereStr  = " where ";
        String lastWhere = " 1 = 1 ";
        String str1 = whereFields.replace("whereFieldStrings="," ").replace("&",",");
        System.out.println(str1);
        String[] strings = str1.split(",");
        int flag = 0;
        for (int i = 0; i < strings.length; i++ ){
            if(!StringUtils.isEmptyOrWhitespace(strings[i])){
                whereStr = whereStr + whereStrings[i] + "='"+strings[i].trim() +"'" + " and ";
                flag += 1;
            }
        }
        if (flag == 0){
            whereStr = " ";
            lastWhere = " ";
        }
        str = str + whereStr + lastWhere;
        str += "    limit  100000  ";


        Map<String,Object> map=new HashMap<>();
        map.put("sql",str);
        map.put("tabName",rpReportName.getReportName());
        return map;
    }


    @Override
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

