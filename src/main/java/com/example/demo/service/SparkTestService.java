package com.example.demo.service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.hive.HiveContext;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SparkTestService {

    public List<Map<String,Object>> sparkDemo(String sqlname,String[] fields) {
        double xxx=Math.random();
        SparkConf conf = new SparkConf().setAppName("programdept").setMaster("spark://10.100.200.11:7077")
                .set("spark.executor.memory","2g")
                .setJars(new String[]{"D:\\datahouse\\demo.jar"});
        JavaSparkContext sc = new JavaSparkContext(conf);
        HiveContext hiveContext = new HiveContext(sc.sc());
        Gson gson = new Gson();

        hiveContext.sql("use oracle_table");
        DataFrame sql = hiveContext.sql(sqlname);
        // DataFrame sql = hiveContext.sql("show tables");
        List<Row> collect =sql.collectAsList();
        System.out.println(collect.size());
        System.out.println(collect.toString());
        List<Map<String,Object>> dataL = new ArrayList<>();
        for(int i=0;i< collect.size() ;i++ ){
            Map<String,Object> map=new HashMap<>();
            for(int j=0;j<collect.get(i).size();j++){
              /*  System.out.println(collect.get(i).schema()+"--------------------------");
                System.out.println(collect.get(i)+"======================");*/
               // System.out.println(collect.get(i).get(j).toString()+"*******************");

                StructType nameStr= collect.get(i).schema();

              //  System.out.println(nameStr.apply(j).name()+"======================");
              //  System.out.println(collect.get(i).get(j).toString()+"======================");
                map.put(fields[j].toString(),collect.get(i).get(j).toString());
            }
            dataL.add(map);
        }

        System.out.println(dataL);

        sc.close();
        System.out.println("-------------------开始--------------------");
     //   List<Map<String,Object>> dataList = listConvert(collect);
        System.out.println("-------------------结束--------------------");
        return dataL;

    }

  /*  public <T> List<Map<String,Object>> listConvert(List<T> list){
        List<Map<String,Object>> list_map=new ArrayList<Map<String,Object>>();
        try {
            for (T t : list) {
                Field[] fields=t.getClass().getDeclaredFields();
                System.out.println("-------------------t.getClass()--------------------:"+t.getClass());
                Map<String, Object> m = new HashMap<String, Object>();
                for(Field field:fields){
                    String keyName=field.getName();
                    System.out.println("-------------------field--------------------:"+field.toString());
                 //   PropertyDescriptor pd = new PropertyDescriptor(keyName,t.getClass());
                 //   Method getMethod = pd.getReadMethod();// 获得getter方法
                //    Object o = getMethod.invoke(t);// 执行get方法返回一个Object
                    m.put(keyName, "234");
                }
                list_map.add(m);
            }
            return list_map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/


}
