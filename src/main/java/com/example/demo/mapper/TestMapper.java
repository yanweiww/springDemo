package com.example.demo.mapper;

import com.example.demo.entity.RpReportName;
import com.example.demo.entity.TableRelation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface TestMapper {

    /**
     * 查询所有表名
     * @return
     */
    @Select({"select table_name from rp_table_field group by table_name"})
    List<String> getTableNames();

    /**
     * 查询各个表的所有字段
     * @param tableName
     * @return
     */
    @Select("select field,field_means from rp_field_means where table_name = #{tableName}")
    @Results({
            @Result(property = "field",column = "field"),
            @Result(property = "fieldMeans",column = "field_means")
    })
    List<Map<String,String>> getTableFields(@Param("tableName") String tableName);

    /**
     * 查询表名称
     * @return
     */
    @Select("select * from rp_report_name where flag='0'  ")
    @Results({
            @Result(property = "reportId",column = "id"),
            @Result(property = "reportName",column = "report_name"),
            @Result(property = "sqlString",column = "sql_string"),
            @Result(property = "tableString",column = "table_string"),
            @Result(property = "whereString",column = "where_string")
    })
    List<RpReportName> getTabs();

    /**
     * 根据id查询需要条件查询的字段和展示类型
     * @return
     * @param reportId
     */
    @Select("select rrn.where_string," +
            "       rrq.field_string," +
            "       rrq.type," +
            "       rrq.show_field" +
            " from  rp_report_name rrn " +
            " join  rp_relation_query rrq" +
            "   on  rrn.id = rrq.parent_id" +
            " where rrn.id = #{reportId}")
    @Results({
            @Result(property = "whereString",column = "where_string"),
            @Result(property = "fieldString",column = "field_string"),
            @Result(property = "fieldType",column = "type"),
            @Result(property = "showField",column = "show_field")
    })
    List<Map<String,Object>> getAllQueryFields(@Param("reportId") Integer reportId);


    /**
     * 根据id查询表字段拼接sql
     * @param reportId
     */
    @Select("select * from rp_report_name where id = #{reportId} and flag='0'  ")
    @Results({
            @Result(property = "lebleString",column = "leble_string"),
            @Result(property = "whereString",column = "where_string"),
            @Result(property = "tableString",column = "table_string"),
            @Result(property = "sqlString",column = "sql_string"),
            @Result(property = "reportName",column = "report_name")
    })
    RpReportName getQuerySql(@Param("reportId") Integer reportId);


    /**
     * 获取sql的主表
     */
    @Select("select table_one,field_one,table_relation,table_two,field_two from rp_table_relation where table_two= #{tableTwo}")
    @Results({
            @Result(property = "tableOne" ,column = "table_one"),
            @Result(property = "fieldOne" ,column = "field_one"),
            @Result(property = "tableRelation" ,column = "table_relation"),
            @Result(property = "tableTwo" ,column = "table_two"),
            @Result(property = "fieldTwo" ,column = "field_two")
    })
    List<TableRelation> getSql(String tableTwo);
}
