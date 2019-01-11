package com.example.demo.entity;



import com.example.demo.service.AddToTableService;
import com.example.demo.service.SparkTestService;
import com.example.demo.service.quartz.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by TC20021 on 2018/12/27.
 */

@Component
public class ScheduleTask {
    @Autowired
    TaskService taskService;
    @Autowired
    SparkTestService sparkTestService;
    @Autowired
    AddToTableService addToTableService;

   @Scheduled(cron = "0/10 * * * * *")
    public void work() {
        // task execution logic

        System.out.println("++++++++++++++"+new Date());
        try {
            //放入spark代码块
            //取出数据库中的数据,得到数据的集合
            Map<String ,Object> taskList = taskService.getQueryAll();
            System.out.println(taskList);
            System.out.println(taskList==null);
            if(taskList!=null){
                //如果不为空，则处理sql
                //查询条件
                String sql=taskList.get("sqls").toString();
                int id=Integer.parseInt(taskList.get("id").toString());

                String[] fields = taskList.get("sqlStr").toString().split(",");
                for (String a : fields) {
                    System.out.println(a);
                }
                //查询条件别名
                String[] labels = taskList.get("lebStr").toString().split(",");
                for (String a : labels) {
                    System.out.println(a);
                }
                String requestUrl = "";
                try{
                    List<Map<String, Object>> objs = sparkTestService.sparkDemo(sql, fields,id);
                    System.out.println(objs.size());
                    //进行文件上传操作
                    requestUrl  = taskService.uploadFiless(objs,labels,fields);
                }catch (Exception e){
                    e.printStackTrace();
                }
                //修改tab状态为over
                addToTableService.updateData(id,requestUrl);


            }
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(">>>>>>>>>>>>>"+new Date());
    }



}
