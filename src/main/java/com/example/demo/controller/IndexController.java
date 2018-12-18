package com.example.demo.controller;

import com.example.demo.commons.ExcelExportUtil;
import com.example.demo.entity.LoanBase;
import com.example.demo.service.OnloadService;
import com.example.demo.service.SparkTestService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by TC20021 on 2018/12/11.
 */
@Controller
@RequestMapping("/load")
public class IndexController {
    @Autowired
    OnloadService onloadService;
    @Autowired
    private SparkTestService sparkTestService;

    @RequestMapping(value = "/in" , method = RequestMethod.GET)
    public String getOne(Model model){

        return "index";
    }



    @RequestMapping(value = "/det")

    public String getDatg(Model model){
        List<LoanBase> lbs = onloadService.getAll();
        model.addAttribute("lbs",lbs);
        return "index";
    }


    @RequestMapping(value = "/spit" ,method = RequestMethod.POST)
    @ResponseBody
    public void getSpit( String str,String strtab,String strLabels,HttpServletResponse response) throws Exception {

        String[] fields=str.split(",");
        for(String s:fields){
            System.out.print(s+"\t");
        }
        System.out.println("");

        String[] tname = strtab.split(",");
        for(String ss:tname){
            System.out.print(ss+"\t");
        }
        System.out.println("");

        String[] labels = strLabels.split(",");
        for(String sss:labels){
            System.out.print(sss+"\t");
        }

        String sql = onloadService.getSql(tname,fields);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(sql);

        List<Map<String,Object>> objs = sparkTestService.sparkDemo(sql,fields);


/*****************************************************************************************/
        Workbook excel=(Workbook)ExcelExportUtil.createExcelByMap("报表.xlsx", labels, fields, objs, "报表",true);

        String fileName = "报表";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        fileName += dateFormat.format(new Date()) + ".xlsx";
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");

        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/vnd.ms-excel;");
        OutputStream out = response.getOutputStream();
        excel.write(out);
        out.flush();

        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {

            }
            out = null;
        }
        if (excel != null) {
            try {
                excel.close();
            } catch (IOException e) {

            }
            excel = null;
        }
       // export(objs,response,labels,fields);
        //return obj.size()+"";
       // return "000";
    }


    /**
     * 导出报表
     * @param model
     * @return
     */
    public void export(List<Map<String,Object>> dataList,HttpServletResponse response,String[] labels
        ,String[] fields) throws Exception{

        Workbook excel=(Workbook)ExcelExportUtil.createExcelByMap("报表.xlsx", labels, fields, dataList, "报表",true);

        String fileName = "报表";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        fileName += dateFormat.format(new Date()) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/msexcel");
        OutputStream out = response.getOutputStream();
        excel.write(out);
        out.flush();

        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {

            }
            out = null;
        }
        if (excel != null) {
            try {
                excel.close();
            } catch (IOException e) {

            }
            excel = null;
        }
    }


    @RequestMapping(value = "/lgd")
    @ResponseBody
    public  Map<String, Object> getLg1(Model model){
        //表名
        List<Map<String , Object>> res = onloadService.getGroupTable();
        Map<String, Object> response = new HashMap<String, Object>();
        //表字段

        for (Map map:res) {
            List<Map<String , Object>> maps = new ArrayList<Map<String , Object>>();
            String tableName = map.get("tableName").toString();
            maps = onloadService.getList(tableName);
           // response.put(tableName,maps);
            response.putAll(map);
        }
        System.out.println(response);
        return response;

    }

    @RequestMapping(value = "/lgddd")
    @ResponseBody
    public List<Map<String , Object>> getLgddd(String tabName){
        List<Map<String , Object>> response = onloadService.getList(tabName);
        System.out.println(response);
        return response;
    }


}
