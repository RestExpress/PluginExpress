package com.strategicgains.restexpress.plugin.openapi;

import java.math.BigDecimal;
import java.util.List;

import com.strategicgains.restexpress.plugin.openapi.annotations.ApiModelProperty;
import com.wordnik.swagger.annotations.ApiModel;

@com.wordnik.swagger.annotations.ApiModel
public class DummyModel extends DummyBase {
    @ApiModel
    public static class One extends Base {
        private String prop1;
        private String prop2;
    }

    public static class Base {
        @ApiModelProperty(position = -1)
        private String base1;
        @ApiModelProperty(position = -2)
        private String base2;
    }

    public enum ColorEnum {
        RED,
        GREEN,
        BLUE
    }

    @ApiModelProperty
    private List<One> ones;
    @ApiModelProperty
    private String dummy1;
    @ApiModelProperty
    private int dummy2;
    private transient String transient1;
    private static int static1;
    @ApiModelProperty
    private float dummy3;
    @ApiModelProperty
    private boolean dummy4;
    @ApiModelProperty
    private One[] dummy5;
    @ApiModelProperty
    private int[] dummy6;
    @ApiModelProperty
    private Integer dummy7;
    @ApiModelProperty
    private Double dummy8;
    @ApiModelProperty
    private ColorEnum color;
    @ApiModelProperty(dataType = "number")
    private BigDecimal dummy9;
    @ApiModelProperty(dataType = "number", format = "double", excludeFromModels = {"AlternativeDummyModel"})
    private BigDecimal dummy10;
}
