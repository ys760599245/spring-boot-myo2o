package com.chen.myo2o.entity;

import java.util.List;

/**
 * @author yss
 * @date 2019/5/25 18:43
 * <p>
 * 包描述   com.chen.myo2o.entity
 * 类名称   spring-boot-myo2o
 * 类描述
 * echart里面的series项目
 */
public class EchartSeries {
    private String name;
    private String type = "bar";
    private List<Integer> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
