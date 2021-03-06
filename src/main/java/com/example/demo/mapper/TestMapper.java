package com.example.demo.mapper;

import com.example.demo.entity.ExpressionField;
import com.example.demo.entity.RelationQuery;
import com.example.demo.entity.RpReportName;
import com.example.demo.entity.TableRelation;
import org.apache.ibatis.annotations.*;

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
            "       rrq.show_field," +
            "       rrq.where_detail" +
            " from  rp_report_name rrn " +
            " join  rp_relation_query rrq" +
            "   on  rrn.id = rrq.parent_id" +
            " where rrn.id = #{reportId}")
    @Results({
            @Result(property = "whereString",column = "where_string"),
            @Result(property = "fieldString",column = "field_string"),
            @Result(property = "fieldType",column = "type"),
            @Result(property = "showField",column = "show_field"),
            @Result(property = "whereDetail",column = "where_detail")
    })
    List<Map<String,Object>> getAllQueryFields(@Param("reportId") Integer reportId);
    /*@Select({"select * from rp_report_expression where parent_id = #{reportId} and field_type = '0'"})
    @Results({
            @Result(property = "fieldId",column = "id"),
            @Result(property = "parentId",column = "parent_id"),
            @Result(property = "meanField",column = "mean_field"),
            @Result(property = "type",column = "type"),
            @Result(property = "expressionField",column = "expression_field"),
            @Result(property = "whereDetail",column = "where_detail"),
            @Result(property = "fieldType",column = "field_type")
    })
    List<ExpressionField> getAllExpressionFields(Integer reportId);
    @Select("select where_detail from rp_report_expression where parent_id=#{reportId} and expression_field=#{whereString}")
    String getExpWhereDetail(@Param("whereString") String whereString, @Param("reportId") Integer reportId);*/


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


    /**
     * 根据id获取报表所有关联的表
     * @param reportId
     * @return
     */
    @Select({"select table_string from rp_report_name where id = #{reportId}"})
    String getAllTableFields(@Param("reportId") Integer reportId);

    /**
     * 根据表名获取该表的所有字段
     * @param tab
     * @return
     */
    @Select({"select field,field_means from rp_field_means where table_name = #{tableName}"})
    @Results({
            @Result(property = "field",column = "field"),
            @Result(property = "fieldMeans",column = "field_means")
    })
    List<Map<String, String>> getFilesByTableName(@Param("tableName") String tab);

    /**
     * 根据表名获取表的别名
     * @param tab
     * @return
     */
    @Select({"select show_field_means from rp_field_means where table_name = #{tableName} group by show_field_means"})
    String selectTabName(String tab);

    /**
     * 插入自定义的字段公式
     * @param expressionField
     */
    @Insert("insert into rp_report_expression values (null,#{parentId},#{expressionField},#{sign},#{type},#{meanField},#{whereDetail},#{fieldType})")
    void insertExpression(ExpressionField expressionField);
    /**
     * 插入自定义的字段公式到rp_report_query表
     * @param relationQuery
     */
    @Insert("insert into rp_relation_query values (null,#{parentId},#{fieldString},#{sign},#{type},#{fieldMeans},#{whereDetail})")
    void insertExpressionToQuery(RelationQuery relationQuery);

    /**
     * 查询自定义表达式
     * @param reportId
     * @return
     */
    @Select({"select id,parent_id,sign,type,mean_field,expression_field,where_detail,field_type from rp_report_expression where parent_id = #{reportId}"})
    @Results({
            @Result(property = "fieldId",column = "id"),
            @Result(property = "parentId",column = "parent_id"),
            @Result(property = "sign",column = "sign"),
            @Result(property = "type",column = "type"),
            @Result(property = "meanField",column = "mean_field"),
            @Result(property = "expressionField",column = "expression_field"),
            @Result(property = "whereDetail",column = "where_detail"),
            @Result(property = "fieldType",column = "field_type")
    })
    List<ExpressionField> getExpressions(Integer reportId);

    /**
     * 更新自定义字段
     * @param expressionField
     */
    @Update({"update rp_report_expression set mean_field=#{meanField},expression_field=#{expressionField},where_detail=#{whereDetail},field_type=#{fieldType} where id = #{fieldId}"})
    void updateExpression(ExpressionField expressionField);

    /**
     * 删除自定义字段
     * @param ifieldId
     * @return
     */
    @Delete({"delete from rp_report_expression where id = #{ifieldId}"})
    void deleteFieldExpression(@Param("ifieldId") Integer ifieldId);

    /**
     * 根据id查询自定义公式表里面是否有数据
     */
    @Select("select id,parent_id,expression_field,sign,type,mean_field,where_detail from rp_report_expression where parent_id =#{id} and field_type='1' ")
    @Results({
            @Result(property = "fieldId",column = "id"),
            @Result(property = "parentId",column = "parent_id"),
            @Result(property = "expressionField",column = "expression_field"),
            @Result(property = "meanField",column = "mean_field"),
            @Result(property = "whereDetail",column = "where_detail")
    })
    List<ExpressionField> getExression(@Param("id") int id);


    /**
     * 根据报表id和字段名称查询取值类型
     * @param reportId
     * @param whereString
     * @return
     */
    @Select("select where_detail from rp_relation_query where parent_id=#{reportId} and field_string=#{whereString}")
    String selectWhereD(@Param("reportId") Integer reportId, @Param("whereString") String whereString);


    /**
     * 通过expressionid获取对应修改前的数据
     * @param id
     * @return
     */
    @Select({"select * from rp_report_expression where id = #{id}"})
    @Results({
            @Result(property = "fieldId",column = "id"),
            @Result(property = "parentId",column = "parent_id"),
            @Result(property = "sign",column = "sign"),
            @Result(property = "type",column = "type"),
            @Result(property = "meanField",column = "mean_field"),
            @Result(property = "expressionField",column = "expression_field"),
            @Result(property = "whereDetail",column = "where_detail"),
            @Result(property = "fieldType",column = "field_type")
    })
    ExpressionField getExpressionByFieldId(Integer id);

    /**
     * 更新rp_report_query
     * @param expressionField
     * @param field
     */
    @Update("update rp_relation_query set " +
                "field_string = #{expField.expressionField}," +
                "show_field = #{expField.meanField}," +
                "where_detail = #{expField.whereDetail} " +
            "where field_string = #{field.expressionField} " +
            "and show_field = #{field.meanField} " +
            "and where_detail = #{field.whereDetail}" +
            "and parent_id = #{reportId}")
    void updateReportQuery(@Param("expField") ExpressionField expressionField,
                           @Param("field") ExpressionField field,
                           @Param("reportId") Integer reportId);

    /**
     * 当field_type修改为1时删除rp_report_query中的字段
     * @param field
     * @param reportId
     */
    @Delete("delete from rp_relation_query " +
            "where parent_id = #{reportId} " +
            "and field_string = #{field.expressionField} " +
            "and show_field = #{field.meanField} " +
            "and where_detail = #{field.whereDetail}")
    void deleteReportQuery(@Param("field") ExpressionField field, @Param("reportId") Integer reportId);


    /**
     * 删除rp_relation_query中的自定义字段
     * @param reportId
     * @param expressionField
     */
    @Delete("delete from rp_relation_query " +
            "where parent_id = #{reportId} " +
            "and field_string = #{field.expressionField} " +
            "and show_field = #{field.meanField} " +
            "and where_detail = #{field.whereDetail}")
    void deleteFromQuery(@Param("reportId") Integer reportId,@Param("field") ExpressionField expressionField);

    /**
     * 插入自定义的字段公式到rp_report_query表
     * @param expressionField
     */
    @Insert("insert into rp_relation_query values (null,#{parentId},#{expressionField},#{sign},#{type},#{meanField},#{whereDetail})")
    void insertExpressionToQuery2(ExpressionField expressionField);

}
