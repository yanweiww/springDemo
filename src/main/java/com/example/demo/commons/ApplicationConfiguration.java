package com.example.demo.commons;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by TC20021 on 2018/12/26.
 */
@Component
public class ApplicationConfiguration  implements Serializable {
    private static final long serialVersionUID = 1L;

    public SparkConf sparkconf(){
        SparkConf conf = new SparkConf()
                .setAppName("spark-onyarn-test").setMaster("yarn-client")
                .set("yarn.resourcemanager.hostname", "172.16.250.240")
                .set("spark.yarn.preserve.staging.files", "false")
                .set("spark.executor.extraClassPath", " /opt/cloudera/parcels/CDH-5.14.0-1.cdh5.14.0.p0.24/lib/hive/lib/*")
                .set("spark.yarn.jar", "hdfs://172.16.250.240:8020/user/root/spark-assembly-1.6.0-cdh5.14.0-hadoop2.6.0-cdh5.14.0.jar")
                .set("spark.executor.instances", "10")
                .set("spark.executor.memory", "1500M")
                .set("spark.executor.cores", "2")
                .setJars(new String[]{"D:\\datahouse\\demo.jar"});

       // conf.getBoolean("spark.driver.allowMultipleContexts",true);

        return conf;
    }
    public JavaSparkContext javaSparkContext(){

        return new JavaSparkContext(sparkconf());
    }

    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }



}
