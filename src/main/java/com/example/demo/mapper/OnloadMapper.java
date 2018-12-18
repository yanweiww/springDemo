package com.example.demo.mapper;

import com.example.demo.entity.TableRelation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by TC20021 on 2018/12/11.
 */
public interface OnloadMapper {
    /**
     * 表分组
     */
    @Select("select table_name from rp_table_field group by table_name")
    @Results({
            @Result(property = "tableName", column = "table_name")
    })
    List<Map<String , Object>> getGroupTable();

    /**
     * 根据表名查询字段
     */
    @Select("select field from rp_table_field where table_name = #{tabName} ")
    List<Map<String , Object>> getList(@Param("tabName") String tabName);


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
    List<TableRelation> getSql(@Param("tableTwo") String tableTwo);













    /**
     * 表分组
     */
    @Select("select field,field_means,table_name from rp_field_means")
    @Results({
            @Result(property = "tableName", column = "table_name"),
            @Result(property = "fieldMeans", column = "field_means")
    })
    List<Map<String , Object>> getAll();
}
