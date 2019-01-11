package com.example.demo.controller;

import com.example.demo.entity.ReportSort;
import com.example.demo.entity.RpReportName;
import com.example.demo.service.EditReportService;
import com.example.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TC20021 on 2019/1/3.
 */
@Controller
@RequestMapping("/edit")
public class editReportController {
    @Autowired
    TestService testService;
    @Autowired
    EditReportService editReportService;

    @RequestMapping("/getEdit")
    public String getEdit(Model model){
        List<RpReportName> RpReportNames = testService.getTabs();
        model.addAttribute("RpReportNames",RpReportNames);

        return "update";
    }

    @RequestMapping("/getIframe")
    public String getif(String id,Model model){
       System.out.println("id"+id);
        Map<String,Object> map=new HashMap<>();
        map.put("id",id);
       //保存id
        model.addAttribute("map",map);

        return "iframeReport";
    }
    @RequestMapping("/getData")
    @ResponseBody
    public Map<String ,Object> getttabs(String id){

        Map<String ,Object> result = editReportService.getInfo(id);
        return result;
    }

    @RequestMapping("/geterr")
    public String getErr(){

        return "err";
    }

    @RequestMapping("/sortTab")
    @ResponseBody
    public String sortTab(ReportSort reportSort){
        System.out.println(reportSort.getFieldSql()+"*********");
        //更新修改后排序
        editReportService.updateToTab(reportSort);
        return "修改成功";
    }

    //编辑报表
    @RequestMapping("/editReport")
    public String getEditReport(String id,Model model){
      //  System.out.println("id"+id);
        Map<String,Object> map=new HashMap<>();
        map.put("id",id);
        //保存id
        model.addAttribute("map",map);
        return "saveReport";
    }

    //layui样式表格，管理报表，增删改
    @RequestMapping("/layui")
    public String getui(String id,Model model){

        return "/report/editReport";
    }
    //easyui布局的编辑页面
    @RequestMapping("/easyui")
    public String getuis(String id,Model model){
        List<RpReportName> RpReportNames = testService.getTabs();
        Map<String,Object> map=new HashMap<>();
        map.put("id",id);
        model.addAttribute("RpReportNames",RpReportNames);
        return "/report/report";
    }
    //layui  url 传参
    @RequestMapping("/url")
    @ResponseBody
    public String getUrl(String id){
        System.out.println(id);

        return "000";
    }
}
