package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpressionField {
    /**
     * fieldId
     */
    private Integer fieldId;

    /**
     * parentId
     */
    private Integer parentId;

    /**
     *expressionField
     */
    private String expressionField;

    /**
     *sign
     */
    private String sign;

    /**
     *type
     */
    private String type;

    /**
     *meanField
     */
    private String meanField;

    /**
     *whereDetail
     */
    private String whereDetail;

    /**
     *fieldType
     */
    private String fieldType;

    /**
     *fieldTypeValue
     */
    private String fieldTypeValue;


    @Override
    public String toString() {
        return "ExpressionField{" +
                "fieldId=" + fieldId +
                ", parentId=" + parentId +
                ", expressionField='" + expressionField + '\'' +
                ", sign='" + sign + '\'' +
                ", type='" + type + '\'' +
                ", meanField='" + meanField + '\'' +
                ", whereDetail='" + whereDetail + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", fieldTypeValue='" + fieldTypeValue + '\'' +
                '}';
    }
}
