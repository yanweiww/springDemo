package com.example.demo.controller;

import com.example.demo.commons.ExcelExportUtil;
import com.example.demo.entity.RpReportName;
import com.example.demo.entity.RpReportQuery;
import com.example.demo.entity.ScheduleTask;
import com.example.demo.service.AddToReportService;
import com.example.demo.service.AddToTableService;
import com.example.demo.service.SparkTestService;
import com.example.demo.service.TestService;
import com.mysql.jdbc.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/index")
public class TestController {

    @Autowired
    TestService testService;
    @Autowired
    SparkTestService sparkTestService;
    @Autowired
    AddToReportService addToReportService;
    @Autowired
    AddToTableService addToTableService;

    /**
     * 加载页面表数据
     * @param model
     * @return
     */
    @RequestMapping("/getDatas")
    public String getDatas (Model model){
        List<Map<String, Object>> allDatas = testService.getDatas();
        //
        model.addAttribute("allDatas",allDatas);
        return "111";
    }


    /**
     * 根据选择查询字段
     * @param nameData
     * @param tabData
     * @param fieldMean
     * @return
     */
    @RequestMapping(value = "/queryDatas")
    @ResponseBody
    public String queryDatas(@RequestParam("nameData") String nameData,
                             @RequestParam("tabData") String tabData,
                             @RequestParam("fieldMean") String fieldMean){
        testService.queryDatas(nameData,tabData,fieldMean);
        return ".....";
    }

    /**
     * 加载表名称
     * @param model
     * @return
     */
    @RequestMapping("/getTabs")
    public String getTabs(Model model){
        List<RpReportName> RpReportNames = testService.getTabs();
        model.addAttribute("RpReportNames",RpReportNames);
        return "222";
    }

private int idd=0;
    /**
     * 点击表名时获取条件字段和展示类型
     * @param reportId
     */
    @RequestMapping("/getAllQueryFields")
    public String getAllQueryFields(Model model,Integer reportId){
        System.out.println(reportId);
        idd=reportId;
        List<Map<String, Object>> allQueryFields = testService.getAllQueryFields(reportId);
        if(allQueryFields!=null){
            //获取页面查询字段

            model.addAttribute("allQueryFields",allQueryFields);
            model.addAttribute("reportId",reportId);

            return "product";
        }
        return "wang";
    }

    @RequestMapping("/getTabData")
    @ResponseBody
    public Map<String,Object> getbb(String reportId){
        Map<String,Object> datas=new HashMap<>();
        if(!StringUtils.isNullOrEmpty(reportId)){
            datas=addToTableService.getTabList(Integer.parseInt(reportId));
            System.out.println(datas);
            return datas;
        }
        return null;
    }


    @RequestMapping("/getQuerySql")
    @ResponseBody
    public String getQuerySql(@RequestParam("reportId") Integer reportId,
                              @RequestParam("whereFields") String whereFields) throws IOException {

        //得到条件
        Map<String,Object> maps = testService.getQuerySql(reportId,whereFields);
        String sql=maps.get("sql").toString();

       // String tabName = maps.get("tabName").toString();
        System.out.println(sql);
        //根据reportId查询排序表，如果没有数据，在直接查当前表，有数据就是用排序后的字段

        String sqlStr="";
        String lebStr="";
        sqlStr = addToReportService.getSqlString(reportId);
        lebStr = addToReportService.getLebSqlString(reportId);

        /**
         * 单击查询按钮，根据报表id，条件选项，当前时间插入数据，状态默认为0，，url为空,执行插入方法
         * 在spark根据sql查询之前生成
         * 返回生成的主键
         */
        int id = addToTableService.getAddToTable(reportId,sqlStr,lebStr,sql);

      return id+"0000";
    }

    /**
     * 根据主键获得查询字段的别名
     * @return
     */
    @RequestMapping("/getLable")
    @ResponseBody
    public String getLable(String reportId){
        String lable = addToReportService.getLebSqlString(Integer.parseInt(reportId));

        return lable;
    }
    /**
     * 返回数据错误页面
     */
    @RequestMapping("/error")
    public String getError(){

        return "err";
    }

    /**
     * 测试分页功能
     */
    @RequestMapping("/test")
    @ResponseBody
    public List<Map<String,Object>> getTest(){
        String sql="select " +
                "com_organization_dsj.name,person_info_dsj.name " +
                "from  " +
                "loan_base_dsj  " +
                "left join com_organization_dsj on loan_base_dsj.sales_department_id=com_organization_dsj.id  " +
                "left join person_info_dsj on loan_base_dsj.borrower_id=person_info_dsj.id  " +
                "left join loan_initial_info_dsj on loan_base_dsj.id=loan_initial_info_dsj.loan_id  limit 1000";

        String fields[] ={"com_organization_dsj.name","person_info_dsj.name"};

        List<Map<String,Object>> lists=sparkTestService.sparkDemo(sql,fields,67);
        System.out.println(">>>>>>>>>>>>"+lists.size()+"<<<<<<<<<<<<<<<<<<");
        return lists;
    }


    /**
     * 文件上传
     * @param reportId
     * @param whereFields
     * @return
     * @throws IOException
     */
   /* public String getFTPUrl(Integer reportId,String whereFields,int id) throws IOException {

//调用执行sql查询结果，上传到ftp的方法,得到上传文件的地址
        String requestUrl = getFTPUrl(reportId,whereFields,id);
        System.out.println("路径地址为："+requestUrl);
        Boolean flag = false;
        if(!StringUtils.isNullOrEmpty(requestUrl)){
            //如果返回路径不为空，就根据id，修改table中requestUrl字段的值
           // flag = addToTableService.updateData(id,requestUrl);
        }



        Map<String,Object> maps = testService.getQuerySql(reportId,whereFields);
        String sql=maps.get("sql").toString();
        System.out.println(sql);



        String tabName = maps.get("tabName").toString();
        System.out.println(sql);
        String sqlStr = addToReportService.getSqlString(idd);
        String lebStr = addToReportService.getLebSqlString(idd);
        //查询条件
        String[] fields = sqlStr.split(",");
        for (String a : fields) {
            System.out.println(a);
        }
        //查询条件别名
        String[] labels = lebStr.split(",");
        for (String a : labels) {
            System.out.println(a);
        }


        List<Map<String, Object>> objs = sparkTestService.sparkDemo(sql, fields,id);
        Workbook excel = ExcelExportUtil.createExcelByMap("报表.xlsx", labels, fields, objs, "报表", true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            excel.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] b = os.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(b);

        return testService.getFTPUrl(tabName,inputStream);

    }*/


}
