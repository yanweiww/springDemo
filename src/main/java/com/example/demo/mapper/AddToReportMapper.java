package com.example.demo.mapper;

import com.example.demo.entity.RelationQuery;
import com.example.demo.entity.ReportName;
import org.apache.ibatis.annotations.*;

/**
 * Created by TC20021 on 2018/12/19.
 */
public interface AddToReportMapper {

    @Insert(" insert into rp_report_name values (null,#{reName},#{fieldNames},#{tableNames},#{whereNames},#{lableName},#{flag}) ")
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    void insert(ReportName reportName);

    /**
     * 取出p_report_name表中条件字段
     */
    @Select(" select where_string from rp_report_name where id=#{id} ")
    @Results({
        @Result(property = "whereString" ,column ="where_string" )
    })
    String getWhereStr(@Param("id") int id);

    /**
     * 根据字段名查询该字段的字段属性
     */
    @Select(" SELECT field_type  FROM rp_field_means WHERE table_name=#{tab} AND `field`=#{field} ")
    @Results({
            @Result(property = "fieldType",column = "field_type")
    })
    String getFieldType(@Param("tab") String tab,@Param("field") String field);
    /**
     * 将查询字段属性和rp_report_name主键，添加到rp_relation_query表
     */
    @Insert(" insert into rp_relation_query values(null,#{parentId},#{fieldString},#{sign},#{type},#{fieldMeans},#{whereDetail}) ")
    void addRpQuery(RelationQuery relationQuery);


    /**
     * 根据字段名查询该字段的字段别名
     */
    @Select(" SELECT field_means  FROM rp_field_means WHERE table_name=#{tab} AND `field`=#{field} ")
    @Results({
            @Result(property = "fieldMeans",column = "field_means")
    })
    String getFieldMeans(@Param("tab") String tab,@Param("field") String field);


    /**
     * 根据id查询显示字段名
     */
    @Select("select sql_string from rp_report_name where id=#{id} ")
    @Results({
            @Result(property = "sqlString" ,column ="sql_string" )
    })
    String getSqlStr(@Param("id") int id);
    /**
     * 根据id查询显示字段别名
     */
    @Select("select leble_string from rp_report_name where id=#{id} ")
    @Results({
            @Result(property = "lebleString" ,column ="leble_string" )
    })
    String getLebStr(@Param("id") int id);
}
