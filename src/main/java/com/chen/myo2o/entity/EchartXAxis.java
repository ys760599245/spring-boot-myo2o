package com.chen.myo2o.entity;

import java.util.HashSet;

/**
 * @author yss
 * @date 2019/5/25 18:40
 * <p>
 * 包描述   com.chen.myo2o.entity
 * 类名称   spring-boot-myo2o
 * 类描述
 * 迎合echart里面的XAxis项目
 */
public class EchartXAxis {
    private String type = "category";
    //为了去重
    private HashSet<String> getData;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashSet<String> getGetData() {
        return getData;
    }

    public void setGetData(HashSet<String> getData) {
        this.getData = getData;
    }
}
