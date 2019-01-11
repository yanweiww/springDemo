package com.example.demo.service;

import com.example.demo.entity.RpReportQuery;
import com.example.demo.mapper.AddToTableMapper;
import javolution.io.Struct;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TC20021 on 2018/12/24.
 */
@Service
public class AddToTableService {
    @Autowired
    AddToTableMapper addToTableMapper;

    /**
     * 根据reportid加载table数据
     */
    public Map<String ,Object> getTabList(int reportId){

        List<Map<String ,Object>> list=addToTableMapper.getTabList(reportId);
        Map<String ,Object> resultService=new HashMap<>();
        resultService.put("total","100");
        resultService.put("rows",list);
        return resultService;
    }

    /**
     * 单击查询按钮，根据报表id，条件选项，当前时间插入数据，状态默认为0，，url为空,执行插入方法
     * 返回主键
     */
    @Transactional
    public int getAddToTable(int reportId,String sqlStr,String lebStr,String sql){
        RpReportQuery rp=new RpReportQuery();
        SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
        String startTime = sdf.format(new Date());
        //把当前时间添加到table
        rp.setReportId(reportId);
        rp.setStartTime(startTime);
        rp.setRequestUrl("");
        rp.setEndTime("");
        rp.setState("select");
        rp.setSqlWhere("");

        rp.setSqls(sql);
        rp.setLebStr(lebStr);
        rp.setSqlStr(sqlStr);

        addToTableMapper.insert(rp);
        System.out.println(rp.getId());
        return rp.getId();
    }

    /**
     * sql处理完毕，根据插入是返回的id修改数据
     */
    @Transactional
    public void updateData(int id,String tabName){
        RpReportQuery rp=new RpReportQuery();
        //获取当前时间
        SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
        String endTime = sdf.format(new Date());
        if(tabName!=null && tabName !=""){
            rp.setId(id);
            rp.setState("over");
            rp.setEndTime(endTime);
            rp.setRequestUrl(tabName);
        }else{
            rp.setId(id);
            rp.setState("error");
            rp.setEndTime(endTime);
            rp.setRequestUrl("/index/error");
        }

        //调用修改数据接口
        try{
            addToTableMapper.updateDategrid(rp);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("出现异常");

        }


    }
    /**
     * 出现异常修改
     */
    public void updateEx(int id){
        RpReportQuery rp=new RpReportQuery();
        //获取当前时间
        SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
        String endTime = sdf.format(new Date());

        rp.setId(id);
        rp.setState("error");
        rp.setEndTime(endTime);
        rp.setRequestUrl("/error");

        //调用修改数据接口
        addToTableMapper.updateDategrid(rp);

    }
}
