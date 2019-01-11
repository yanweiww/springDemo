package com.example.demo.service;

import com.example.demo.entity.LoanBase;
import com.example.demo.entity.ReportName;
import com.example.demo.entity.TableRelation;
import com.example.demo.mapper.OnloadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TC20021 on 2018/12/11.
 */
@Service
public class OnloadService {

    @Autowired
    OnloadMapper onloadMapper;



    /**
     * 表分组
     */
    public List<Map<String , Object>> getGroupTable(){
        List<Map<String , Object>> serviceResult1 = onloadMapper.getGroupTable();

        return serviceResult1;
    }

    /**
     *根据表名查询字段
     */
    public List<Map<String , Object>> getList(String tabName){


        List<Map<String, Object>> serviceResult1 = onloadMapper.getList(tabName);

        return serviceResult1;
    }

    /**
     * 根据表名获取sql
     */
    public String getSql(String[] tableTwos,String[] fields){

        String str = "select ";
        //取出全部字段
        for(String field:fields){
            str += field + ",";
        }
        str = str.substring(0,str.length()-1);
        str += "  from  loan_base_dsj ";
        //遍历表名数组取出集合
        for(String tableTwo:tableTwos){
            List<TableRelation> serviceResult1 = onloadMapper.getSql(tableTwo);
            //遍历集合取出字段
            for(TableRelation tab:serviceResult1){
                str += tab.getTableRelation()+"  " +tableTwo+"  on  "
                        + tab.getTableOne()+"."+tab.getFieldOne()+" = "+ tab.getTableTwo()+"."+tab.getFieldTwo()+" "
                        + " ";
            }
        }
        str +="  limit 100";
        System.out.println(str);
        return str;
    }



    /**
     * 获取所有数据
     */
    public  List<LoanBase> getAll(){
        //获取所有表名
        List<Map<String , Object>> tableAll = onloadMapper.getGroupTable();
        //获取所有字段
        List<Map<String, Object>> listAll= onloadMapper.getAll();
        //实体类集合
        List<LoanBase> lbs = new ArrayList<>();

        for(Map map:tableAll){
            String tab1=map.get("tableName").toString();
            //初始化对象

            LoanBase ls=new LoanBase();

            List<Map<String ,Object>> ppp = new ArrayList<Map<String ,Object>>();

            for(int i=0;i<listAll.size();i++){
                String tab2=listAll.get(i).get("tableName").toString();
                String showTable = listAll.get(i).get("showFieldMeans").toString();
                Map<String ,Object> ooo=new HashMap<String ,Object>();
                if(tab1.equals(tab2)){
                    //获得表名
                    ls.setTabName(tab2);
                    ls.setShowName(showTable);
                    ooo.put("field",listAll.get(i).get("field"));
                    ooo.put("fieldMeans",listAll.get(i).get("fieldMeans"));
                    // System.out.println(listAll.get(i).get("field").toString());
                    ppp.add(ooo);
                }
            }
            ls.setCalList(ppp);
            lbs.add(ls);
        }

        return lbs;
    }


}
