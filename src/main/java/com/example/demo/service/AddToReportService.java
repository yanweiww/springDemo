package com.example.demo.service;

import com.example.demo.entity.RelationQuery;
import com.example.demo.entity.ReportName;
import com.example.demo.mapper.AddToReportMapper;
import com.example.demo.mapper.EditReportMapper;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by TC20021 on 2018/12/19.
 */
@Service
public class AddToReportService {
    @Autowired
    AddToReportMapper addToReportMapper;
    @Autowired
    EditReportMapper editReportMapper;


    /**
     * 将自定义的报表及sql添加到rp_report_name表中
     * @param reportName
     */
    public void insert(ReportName reportName){
        addToReportMapper.insert(reportName);
    }

    /**
     *根据rp_report_name主键查询条件字段字符串
     */
    public String getWhereStr(int id){
        String str = addToReportMapper.getWhereStr(id);
        return str;
    }
    /**
     * 根据表名和字段名查询字段类型属性
     */
    public String getFieldType(String field,String tab){
        String ss=addToReportMapper.getFieldType(field,tab);
        return ss;
    }
    /**
     * 将查询字段属性和rp_report_name主键，添加到rp_relation_query表
     */

    public void addRpQuery(RelationQuery relationQuery){
        addToReportMapper.addRpQuery(relationQuery);
    }
    /**
     * 根据字段名查询该字段的字段别名
     */
    public String getFieldMeans(String field,String tab){
        String ss= addToReportMapper.getFieldMeans(field,tab);
        return  ss;
    }
    /**
     * 根据id查询显示字段名
     */
    public String getSqlString(int id){
        String str = addToReportMapper.getSqlStr(id);
        return str;

    }
    /**
     * 根据id查询显示字段别名
     */
    public String getLebSqlString(int id){
        String str = addToReportMapper.getLebStr(id);
        return str;

    }

    /**
     *将自定义的报表及sql添加到rp_report_name表中
     * 根据字段名查询rp_field_means 的 field_type
     * 获得报表sql的条件字段的类型属性，将报表的主键、
     * 选择的字段、必填（暂时不设置）、类型属性添加到
     * rp_relation_query表中
     */
    @Transactional
    public String getReport(String str,String strtab,String strLabels,String strwhere,String
            reportname,String whereDetail){
        //实体类保存属性
        /**
         * str:sql显示字段的字符串，逗号隔开
         * strtab:sql关联的表名的字符串,逗号隔开
         * strLabels:sql显示字段的别名
         * strwhere:sql条件查询字段，逗号隔开
         * reportname：自定义报表名
         */
        //添加之前先把选择条件关联的表放到，关联的表字段中，如果有这个表就不添加，没有就添加
        String[] tabs = strtab.split(",");
        String[] wheres = strwhere.split(",");
        //遍历条件查询字段
        for(String s:wheres){
            //取出条件的表
            String[] aa=s.split("\\.");
            //遍历关联的表名的字符串
            for(String ss:tabs){
                if(!ss.equals(aa[0])){
                    //加入到表集合中
                    strtab += ","+ aa[0];
                }
            }
        }

        //对表集合进行去重处理
        String[] test=strtab.split(",");
        List list = Arrays.asList(test);
        Set set = new HashSet(list);
        System.out.println("去重前："+strtab);
        String [] rid=(String [])set.toArray(new String[0]);
        String t1="";
        for (int i = 0; i < rid.length; i++) {
            //System.out.println(rid[i]);
            t1+=rid[i]+",";
        }
        // System.out.println("去重后："+t1);

        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        System.out.println("合并后表集合："+t1);

        ReportName repName = new ReportName();
        repName.setReName(reportname);
        repName.setFieldNames(str);
        repName.setTableNames(t1);
        repName.setWhereNames(strwhere);
        repName.setLableName(strLabels);
        repName.setFlag("0");



        //添加到rp_report_name

            insert(repName);
            //根据返回插入返回id得到条件查询字段
            int cid=repName.getId();
            System.out.println("插入返回id:"+cid);
            String strWheress = getWhereStr(cid);
            System.out.println("//////////////////");
            System.out.println("条件查询字段:  "+strWheress);
            if(!StringUtils.isNullOrEmpty(strWheress)){
                //分割条件查询字段，得到数组
                String[] ss=strWheress.split(",");
                String[] whereSS=whereDetail.split(",");
                //遍历数组
                for (int i=0;i<ss.length;i++){
                    //对遍历的元素再拆分，得到表名和字段名
                    String as=ss[i];
                    String[] aa=as.split("\\.");
                    System.out.println("aa[0]:"+aa[0]+"  aa[1]:"+aa[1]);
                    //得到返回的字段属性
                    String fieldType = getFieldType(aa[0],aa[1]);
                    //得到字段别名
                    String fieldMeans = getFieldMeans(aa[0],aa[1]);
                    System.out.println(fieldType);
                    //将属性和rp_report_name主键，添加到rp_relation_query表
                    RelationQuery re=new RelationQuery();
                    re.setParentId(cid);//关联主键
                    re.setType(fieldType);//查询字段属性
                    re.setFieldString(ss[i]);//查询的字段
                    re.setFieldMeans(fieldMeans);
                    re.setWhereDetail(whereSS[i]);

                    System.out.println(re.getWhereDetail());
                    //添加
                    addRpQuery(re);
                }
            }



            return "0000";

    }
}
