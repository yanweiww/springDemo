package com.example.demo.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.example.demo.commons.ApplicationConfiguration;
import com.google.gson.Gson;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.hive.HiveContext;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.collection.Iterator;

@Service
public class SparkTestService {

    @Autowired
    AddToTableService addToTableService;
    @Autowired
    ApplicationConfiguration applicationConfiguration;

    public List<Map<String, Object>> sparkDemo(String sqlname, String[] fields,int id) {

        /*SparkConf conf = new SparkConf().setAppName("spark-onyarn-test").setMaster("yarn-client")
                .set("yarn.resourcemanager.hostname", "172.16.250.240")
                .set("spark.yarn.preserve.staging.files", "false")
                .set("spark.executor.extraClassPath", "/opt/cloudera/parcels/CDH-5.14.0-1.cdh5.14.0.p0.24/lib/hive/lib/*")
                .set("spark.yarn.jar", "hdfs://172.16.250.240:8020/user/root/spark-assembly-1.6.0-cdh5.14.0-hadoop2.6.0-cdh5.14.0.jar")
                .set("spark.executor.instances", "5")
                .set("spark.executor.memory", "1024M")
                .set("spark.executor.cores", "1")
                .setJars(new String[]{"D:\\datahouse\\demo.jar"});

                JavaSparkContext sc = new JavaSparkContext(conf);

*/

        JavaSparkContext sc = applicationConfiguration.javaSparkContext();
        List<Map<String, Object>> dataL = new ArrayList<>();
        try {

            HiveContext hiveContext = new HiveContext(sc.sc());
            Gson gson = new Gson();


            hiveContext.sql("use oracle_table");
            DataFrame sql = hiveContext.sql(sqlname);

          /*  sql.repartition(100).foreachPartition(new JavaForeachPartitionFunc(){
                                                      @Override
                                                      public void call(Iterator<Row> it) {
                                                          while (it.hasNext()){
                                                              System.out.println(it.next().toString());
                                                          }
                                                      }
                                                  }
            );*/


            // DataFrame sql = hiveContext.sql("show tables");
            List<Row> collect = sql.collectAsList();
           // System.out.println(collect.size());
            // System.out.println(collect.toString());


            for (int i = 0; i < collect.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                for (int j = 0; j < collect.get(i).size(); j++) {
                    //*  System.out.println(collect.get(i).schema()+"--------------------------");
                   // System.out.println(collect.get(i) + "======================");//*
                    // System.out.println(collect.get(i).get(j).toString()+"*******************");

                    StructType nameStr = collect.get(i).schema();

                    //  System.out.println(nameStr.apply(j).name()+"======================");
                    //  System.out.println(collect.get(i).get(j).toString()+"======================");
                    map.put(fields[j].toString(), collect.get(i).get(j));
                }
                dataL.add(map);
            }

          //  System.out.println(dataL);


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("出现异常，线程关闭");
            sc.close();
            //修改table表状态字段为出现异常
            addToTableService.updateEx(id);
        } finally {
            sc.close();
            System.out.println("-------------------开始--------------------");
            //   List<Map<String,Object>> dataList = listConvert(collect);
            System.out.println("-------------------结束--------------------");

            return dataL;

        }


    }
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



