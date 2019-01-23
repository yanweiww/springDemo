package com.example.demo.mapper;

import com.example.demo.entity.ReportSort;
import com.example.demo.entity.RpReportName;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by TC20021 on 2019/1/3.
 */
public interface EditReportMapper {

    //根据id查询报表的基本信息
    @Select("select sql_string,leble_string from rp_report_name where id=#{id} ")
    @Results({
            @Result(property = "sqlString",column = "sql_string"),
            @Result(property = "lebleString" ,column = "leble_string")
    })
    Map<String,Object> getInfo(@Param("id") String id);

    //根据id查询rp_report_name的条件字段
    @Select("select id,field_string,show_field,where_detail from rp_relation_query where parent_id=#{id}")
    @Results({
            @Result(property = "fieldString",column = "field_string"),
            @Result(property = "showField" ,column = "show_field"),
            @Result(property = "whereDetail" ,column = "where_detail")
    })
    List<Map<String ,Object>> getWhere(@Param("id") String id);

    //根据页面修改排序，把新排序修改到rp_report_name表中,并返回主键
    @Update("update rp_report_name set sql_string=#{fieldSql},leble_string=#{lableSql} where id=#{repNameId}")
    void updateTosort(ReportSort reportSort);

    //查询自定义报表的信息
    @Select("select " +
            "a.id as id," +
            "a.report_name as reportName," +
            "a.sql_string as sqlString," +
            "a.table_string as tableString," +
            "a.where_string as whereString," +
            "a.leble_string as lableString," +
            "b.field_String as fieldString," +
            "b.sign as sign," +
            "b.where_detail as whereDetail " +

            "from " +
            "rp_report_name as a " +
            "left join  rp_relation_query as b on a.id=b.parent_id  " +
            " where flag='0' ")
    List<Map<String ,Object>> reportTab();


    //根据id查询rp_report_name的where_string
    @Select("select where_string,sql_string,leble_string from rp_report_name where id = #{reportId}")
    @Results({
            @Result(property = "whereString",column = "where_string"),
            @Result(property = "sqlString" ,column = "sql_string"),
            @Result(property = "lebleString" ,column = "leble_string")
    })
    Map<String,String> selectWhereAndSqlString(Integer reportId);


    //更新rp_report_name的字段
    @Update("update rp_report_name set sql_string=#{sqlString},where_string=#{whereString},leble_string=#{lebleString} where id=#{reportId}")
    void updateWhereAndSqlString(RpReportName rpReportName);

    //更改rp_report_name中的where_string
    @Update("update rp_report_name set where_string = #{whereString} where id = #{reportId}")
    void updateWhereString(@Param("whereString") String joinStr, @Param("reportId") Integer reportId);

    //更改rp_report_name中的sql_string和leble_string
    @Update("update rp_report_name set sql_string = #{sqlString},leble_string = #{lebleString} where id = #{reportId}")
    void updateSqlStringAndLebleString(@Param("sqlString") String joinStr1,
                                       @Param("lebleString")String joinStr2,
                                       @Param("reportId")Integer reportId);

    //更改rp_report_name中的where_string,sql_string和leble_string
    @Update("update rp_report_name set sql_string = #{sqlString},leble_string = #{lebleString},where_string = #{whereString} where id = #{reportId}")
    void updateReportNameByJoinString(@Param("sqlString") String sqlString,
                                      @Param("lebleString") String lebleString,
                                      @Param("whereString") String whereString,
                                      @Param("reportId")Integer reportId);
}
