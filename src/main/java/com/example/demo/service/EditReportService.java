package com.example.demo.service;

import com.example.demo.entity.ReportSort;
import com.example.demo.mapper.EditReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TC20021 on 2019/1/3.
 */
@Service
public class EditReportService {
    @Autowired
    EditReportMapper editReportMapper;

    /**
     * 获得查询字段
     * @param id
     * @return
     */
    public Map<String ,Object> getInfo(String id){
        Map<String ,Object> resultMap = new HashMap<>();
        //条件字段和查询自读啊你放到一个集合中
        List<Map<String ,Object>> listAll= new ArrayList<>();

        resultMap.put("total",1);

       Map<String ,Object> maps=editReportMapper.getInfo(id);
       String[] strSql = maps.get("sqlString").toString().split(",");
       String[] labSql = maps.get("lebleString").toString().split(",");
       //把数组每个元素放进集合中
       for(int i=0;i<strSql.length;i++){
           Map<String ,Object> m = new HashMap<>();
           m.put("lable",labSql[i]);
           m.put("field",strSql[i]);
           listAll.add(m);
       }
        resultMap.put("total",1);
        //排序表格的数据
        resultMap.put("rows",listAll);
        // 报表的数据
        return resultMap;
    }

    /**
     * 将修改后的排序跟新到新表rp_sort_report
     */
    public void updateToTab(ReportSort reportSort){
        editReportMapper.updateTosort(reportSort);
    }

    /**
     * 查询报表字段的信息
     */
    public Map<String,Object> getReportTab(){
        Map<String,Object> resultService = new HashMap<>();
        resultService.put("code",0);
        resultService.put("msg","");
        resultService.put("count",100);

        return resultService;

    }

}
