package com.example.demo.service;

import com.example.demo.commons.FTPConfigBean;
import com.example.demo.commons.FTPUtil;
import com.example.demo.entity.ExpressionField;
import com.example.demo.entity.RelationQuery;
import com.example.demo.entity.RpReportName;
import com.example.demo.entity.TableRelation;
import com.example.demo.mapper.EditReportMapper;
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
    private EditReportMapper editReportMapper;

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
                map.put("whereDetail", results.get(i).get("whereDetail"));
                map.put("whereString", split[i]);
                allQueryFields.add(map);
            }
            return allQueryFields;
        }
        return null;
    }

    /**
     * 查询自定义公式表中是否有数据
     * @param id
     * @return
     */
    @Override
    public List<ExpressionField> getExression(int id) {
        List<ExpressionField> result = testMapper.getExression(id);
        return result;
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
        //查询自定义公式中是否有数据
        List<ExpressionField> datas = getExression(reportId);
        //如果有，取出公式和别名
        String expressionStr= "";
        if(datas.size()>0){
            expressionStr= "";
            //遍历集合
            for(ExpressionField expressionField:datas){
                /**
                 * 把多个公式拼接成一个字符串，
                 * 如果公式是查询字段就拼接到rp_report_name
                 * 表查询字段和别名字段中
                 *如果是条件字段，就把公式拼接到条件字段中
                 * 并在 rp_relation_query表中插入数据
                 */
                expressionStr += expressionField.getExpressionField() +" ,";
            }
        }

        //拼接sql
        String str = "select ";
        //取出全部字段
        str += expressionStr;
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
        //System.out.println(str1);
        String[] strings = str1.split(",");
        int flag = 0;
        for (int i = 0; i < strings.length; i++ ){
            if(!StringUtils.isEmptyOrWhitespace(strings[i])){
                //根据报表id和字段名称查询取值类型
                String whereDetail = testMapper.selectWhereD(reportId,whereStrings[i]);
                whereStr = whereStr + whereStrings[i] + whereDetail + strings[i].trim() + " and ";
                flag += 1;
            }
        }
        if (flag == 0){
            whereStr = " ";
            lastWhere = " ";
        }
        str = str + whereStr + lastWhere;
        str += "    limit  100000  ";
        System.out.println(str);

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


    @Override
    public Map<String, Object> getAllTableFields(Integer reportId) {
        Map<String,Object> m = new HashMap<>();
        List<Map<String,Object>> list = new ArrayList<>();
        String table_string = testMapper.getAllTableFields(reportId);
        //获取自定义字段
        List<ExpressionField> expres = testMapper.getExpressions(reportId);

        if (table_string != null){
            String[] tabs = table_string.split(",");
            //遍历取字段
            for (int i = 0; i < tabs.length; i++) {
                Map<String,Object> map = new HashMap<>();
                //通过表名获取该表的所有字段
                List<Map<String,String>> files = testMapper.getFilesByTableName(tabs[i]);
                map.put("tableMeanName",testMapper.selectTabName(tabs[i])+"表");
                map.put("tableName",tabs[i]);
                map.put("fileAndMeans",files);
                //System.out.println(testMapper.selectTabName(tabs[i])+":"+tabs[i]);
                list.add(map);
            }
            for (int j = 0; j < expres.size(); j++) {
                //转化字段类型
                String fieldTypeStr = expres.get(j).getFieldType();
                if ("1".equals(fieldTypeStr)) {
                    expres.get(j).setFieldType("展现字段");
                    expres.get(j).setFieldTypeValue("1");
                }else if ("0".equals(fieldTypeStr)) {
                    expres.get(j).setFieldType("过滤字段");
                    expres.get(j).setFieldTypeValue("0");
                }else {
                    expres.get(j).setFieldType("--点击选择--");
                    expres.get(j).setFieldTypeValue("null");
                }
                //转化取值类型
                String whereDetailStr = expres.get(j).getWhereDetail();
                System.out.println(whereDetailStr);
                switch (whereDetailStr) {
                    case "<":
                        expres.get(j).setWhereDetail("小于(<)");
                        break;
                    case ">":
                        expres.get(j).setWhereDetail("大于(>)");
                        break;
                    case "=":
                        expres.get(j).setWhereDetail("等于(=)");
                        break;
                    case "<=":
                        expres.get(j).setWhereDetail("小于等于(<=)");
                        break;
                    case ">=":
                        expres.get(j).setWhereDetail("大于等于(>=)");
                        break;
                    default:
                        expres.get(j).setWhereDetail("--点击选择--");
                }
            }
            m.put("fieldList",list);
            m.put("expreList",expres);
            return m;
        }
        return null;
    }


    @Override
    public void insertExpression(String allAppendExpressions, String allUpdateExpressions, Integer reportId) {
        //System.out.println(allAppendExpressions);
        //插入append数据
        if (!StringUtils.isEmptyOrWhitespace(allAppendExpressions)) {
            String[] appendSplits= allAppendExpressions.split(",");
            for (int i = 0; i < appendSplits.length; i++) {
                ExpressionField expressionField = new ExpressionField();
                RelationQuery relationQuery = new RelationQuery();
                expressionField.setParentId(reportId);
                expressionField.setSign("否");
                expressionField.setType("text");
                String[] strings = appendSplits[i].split(":");
                expressionField.setMeanField(strings[0]);
                expressionField.setExpressionField(strings[1]);
                expressionField.setFieldType(strings[2]);
                switch (strings[3]) {
                    case "1":
                        expressionField.setWhereDetail("<");
                        relationQuery.setWhereDetail("<");
                        break;
                    case "2":
                        expressionField.setWhereDetail(">");
                        relationQuery.setWhereDetail(">");
                        break;
                    case "3":
                        expressionField.setWhereDetail("=");
                        relationQuery.setWhereDetail("=");
                        break;
                    case "4":
                        expressionField.setWhereDetail("<=");
                        relationQuery.setWhereDetail("<=");
                        break;
                    case "5":
                        expressionField.setWhereDetail(">=");
                        relationQuery.setWhereDetail(">=");
                        break;
                    default:
                        expressionField.setWhereDetail("");
                        relationQuery.setWhereDetail("");
                }
                testMapper.insertExpression(expressionField);

                //插入数据到rp_relation_query
                String[] stringss = appendSplits[i].split(":");
                //如果是条件字段就插入，否则不更改
                if ("0".equals(stringss[2])) {
                    relationQuery.setParentId(reportId);
                    relationQuery.setSign("否");
                    relationQuery.setType("text");
                    relationQuery.setFieldMeans(stringss[0]);
                    relationQuery.setFieldString(stringss[1]);
                    testMapper.insertExpressionToQuery(relationQuery);
                }
                //插入数据到rp_report_name
                Map<String,String> whereAndSqlString = editReportMapper.selectWhereAndSqlString(reportId);
                String whereString = whereAndSqlString.get("whereString");
                String sqlString = whereAndSqlString.get("sqlString");
                String lebleString = whereAndSqlString.get("lebleString");
                RpReportName rpReportName = new RpReportName();
                rpReportName.setReportId(reportId);
                //判断field_type是什么类型字段
                if("1".equals(strings[2])) {
                    rpReportName.setSqlString(sqlString + "," + strings[1]);
                    rpReportName.setLebleString(lebleString + "," + strings[0]);
                    rpReportName.setWhereString(whereString);
                }
                if("0".equals(strings[2])) {
                    rpReportName.setWhereString(whereString + "," + strings[1]);
                    rpReportName.setSqlString(sqlString);
                    rpReportName.setLebleString(lebleString);
                }
                editReportMapper.updateWhereAndSqlString(rpReportName);
            }
        }
        //修改原数据
        if (!StringUtils.isEmptyOrWhitespace(allUpdateExpressions)) {
            String[] updateSplits = allUpdateExpressions.split(",");
            for (int j = 0; j < updateSplits.length ; j++) {
                ExpressionField expressionField = new ExpressionField();
                String[] strings = updateSplits[j].split(":");
                //先通过每个expressionid获取对应修改前的数据
                ExpressionField field = testMapper.getExpressionByFieldId(Integer.valueOf(strings[0]));

                //System.out.println(field.toString());
                //修改rp_relation_query的数据
                expressionField.setParentId(reportId);
                expressionField.setSign(field.getSign());
                expressionField.setType(field.getType());
                expressionField.setFieldId(Integer.valueOf(strings[0]));
                expressionField.setMeanField(strings[1]);
                expressionField.setExpressionField(strings[2]);
                expressionField.setFieldType(strings[3]);
                String whereType = "";
                switch (strings[4]) {
                    case "1":
                        whereType = "<";
                        break;
                    case "2":
                        whereType = ">";
                        break;
                    case "3":
                        whereType = "=";
                        break;
                    case "4":
                        whereType = "<=";
                        break;
                    case "5":
                        whereType = ">=";
                        break;
                }
                expressionField.setWhereDetail(whereType);
                //获取rp_report_name中的字段
                Map<String,String> whereAndSqlString = editReportMapper.selectWhereAndSqlString(reportId);
                String sqlString = whereAndSqlString.get("sqlString");
                String whereString = whereAndSqlString.get("whereString");
                String lebleString = whereAndSqlString.get("lebleString");
                //判断修改之前的字段类型
                if ("1".equals(field.getFieldType())) {
                    //判断field_type修改了没有
                    if ("0".equals(strings[3])) {
                        //更新rp_relation_query
                        testMapper.insertExpressionToQuery2(expressionField);
                        //更新rp_report_name
                        String joinSqlString = replaceString(sqlString,field);
                        String joinLebleString = replaceMeans(lebleString,field);
                        String joinWhereString = whereString + "," + strings[2];
                        editReportMapper.updateReportNameByJoinString(joinSqlString,joinLebleString,joinWhereString,reportId);
                    }else if ("1".equals(strings[3])) {
                        //更新rp_report_name
                        String[] sqlStrings = sqlString.split(",");
                        String[] lebleStrings = lebleString.split(",");
                        for (int i = 0; i < sqlStrings.length; i++) {
                            if (field.getExpressionField().equals(sqlStrings[i])) {
                                sqlStrings[i] = strings[2];
                            }
                        }
                        for (int l = 0; l < lebleStrings.length; l++) {
                            if (field.getMeanField().equals(lebleStrings[l])) {
                                lebleStrings[l] = strings[2];
                            }
                        }
                        String joinSqlString = StringUtils.join(sqlStrings,",");
                        String joinLebleStrings = StringUtils.join(lebleStrings,",");
                        System.out.println(joinSqlString + "..." + joinLebleStrings);
                        editReportMapper.updateSqlStringAndLebleString(joinSqlString,joinLebleStrings,reportId);
                    }
                }else if ("0".equals(field.getFieldType())) {
                    //判断field_type修改了没有
                    if ("0".equals(strings[3])) {
                        //更新rp_relation_query
                        testMapper.updateReportQuery(expressionField,field,reportId);
                        //更新rp_report_name
                        String[] whereStrings = whereString.split(",");
                        for (int m = 0; m < whereStrings.length; m++) {
                            if (field.getExpressionField().equals(whereStrings[m])) {
                                whereStrings[m] = strings[2];
                            }
                        }
                        String joinWhereString = StringUtils.join(whereStrings,",");
                        System.out.println(joinWhereString);
                        editReportMapper.updateWhereString(joinWhereString,reportId);
                    }else if ("1".equals(strings[3])) {
                        //更新rp_relation_query（直接删除表中该自定义字段）
                        testMapper.deleteReportQuery(field,reportId);
                        //更新rp_report_name
                        String joinWhereString = replaceMeans(whereString,field);
                        String joinLebleString = lebleString + "," + strings[1];
                        String joinSqlString = sqlString + "," + strings[2];
                        editReportMapper.updateReportNameByJoinString(joinSqlString,joinLebleString,joinWhereString,reportId);
                    }
                }
                //最后更新rp_report_expression
                testMapper.updateExpression(expressionField);
            }
        }
    }

    @Override
    public void deleteFieldExpression(Integer ifieldId, Integer reportId) {
        //先查询field_type是否为0，是删除，否不更改
        ExpressionField expressionField = testMapper.getExpressionByFieldId(ifieldId);
        //查询rp_report_name表中数据
        Map<String,String> whereAndSqlString = editReportMapper.selectWhereAndSqlString(reportId);
        String whereString = whereAndSqlString.get("whereString");
        String sqlString = whereAndSqlString.get("sqlString");
        String lebleString = whereAndSqlString.get("lebleString");
        //判断修改的自定义字段类型
        if ("0".equals(expressionField.getFieldType())) {
            //删除rp_relation_query中的数据
            testMapper.deleteFromQuery(reportId,expressionField);
            //更改rp_report_name中的where_string
            String joinWhereString = replaceString(whereString,expressionField);
            System.out.println(joinWhereString);
            editReportMapper.updateWhereString(joinWhereString,reportId);
        }else if ("1".equals(expressionField.getFieldType())) {
            //由于rp_relation_query中没有保存展示字段，所以不需要操作rp_relation_query表
            String joinSqlString = replaceString(sqlString,expressionField);
            String joinLebleString = replaceMeans(lebleString,expressionField);
            System.out.println(joinSqlString + "...." + joinLebleString);
            editReportMapper.updateSqlStringAndLebleString(joinSqlString,joinLebleString,reportId);
        }
        //最后删除rp_report_expression中的数据
        testMapper.deleteFieldExpression(ifieldId);
    }


    /**
     * 删除字段中的指定元素(sql_string和where_string)
     * @param stringOfField
     * @param expressionField
     * @return
     */
    public String replaceString(String stringOfField,ExpressionField expressionField) {
        String[] stringOfFields = stringOfField.split(",");
        List<String> list = Arrays.asList(stringOfFields);
        List<String> arrayList = null;
        if (list.contains(expressionField.getExpressionField())) {
            //将list转化为ArrayList，否则做删除添加等操作报错
            arrayList = new ArrayList<String>(list);
            arrayList.remove(expressionField.getExpressionField());
        }
        String string = String.join(",", arrayList);
        return string;
    }

    /**
     * 删除字段中的指定元素(leble_string)
     * @param stringOfField
     * @param expressionField
     * @return
     */
    public String replaceMeans(String stringOfField,ExpressionField expressionField) {
        String[] stringOfFields = stringOfField.split(",");
        List<String> list = Arrays.asList(stringOfFields);
        List<String> arrayList = null;
        if (list.contains(expressionField.getMeanField())) {
            //将list转化为ArrayList，否则做删除添加等操作报错
            arrayList = new ArrayList<String>(list);
            arrayList.remove(expressionField.getMeanField());
        }
        String string = String.join(",", arrayList);
        return string;
    }
}

