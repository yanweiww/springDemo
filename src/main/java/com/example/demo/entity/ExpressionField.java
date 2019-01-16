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
}
