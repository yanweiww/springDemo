package com.example.demo.mapper;


import com.example.demo.entity.RpReportQuery;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

/**
 * Created by TC20021 on 2018/12/24.
 */
public interface AddToTableMapper {
    /**
     * 单击查询按钮，根据报表id，条件选项，当前时间插入数据，状态默认为0，，url为空
     */

    @Insert(" insert into rp_report_select values (null,#{reportId},#{sqlWhere},#{requestUrl},#{state},#{startTime},#{endTime},#{sqlStr},#{lebStr},#{sqls}) ")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=int.class)
    void insert(RpReportQuery rep);

    /**
     *spark执行完毕以后，根据返回结果，修改数据
     */
    @Update(" update rp_report_select set request_url=#{requestUrl} ,state=#{state},end_time=#{endTime} where id=#{id} ")
    void updateDategrid(RpReportQuery rep);

    /**
     * 根据reportid加载table数据
     */
    @Select(" select id,rep_name_id,sql_where,request_url,state,start_time,end_time from rp_report_select " +
            "where rep_name_id=#{reportId} order by id desc")
    @Results({
            @Result(property = "reportId" , column = "rep_name_id"),
            @Result(property = "sqlWhere" , column = "sql_where"),
            @Result(property = "requestUrl" , column = "request_url"),
            @Result(property = "startTime" , column = "start_time"),
            @Result(property = "endTime" , column = "end_time")
    })
    List<Map<String ,Object>> getTabList(@Param("reportId") int reportId);

    /**
     *根据reportid加载table数据
     */
}
