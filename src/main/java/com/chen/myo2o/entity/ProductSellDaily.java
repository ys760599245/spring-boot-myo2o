package com.chen.myo2o.entity;

import java.util.Date;

/**
 * @author yss
 * @date 2019/5/22 15:28
 * <p>
 * 包描述   com.imooc.myo2o.entity
 * 类名称   myo2o
 * 类描述  顾客消费的商品映射
 */
public class ProductSellDaily {
    //主键id
    private Long productSellDailyId;
    //那天的销量 精确到天
    private Date createTime;
    //销量
    private Integer total;
    //商品信息实体类
    private Product product;
    //店铺信息实体类
    private Shop shop;


    @Override
    public String toString() {
        return "ProductSellDaily{" +
                "productSellDailyId=" + productSellDailyId +
                ", createTime=" + createTime +
                ", total=" + total +
                ", product=" + product +
                ", shop=" + shop +
                '}';
    }

    public Long getProductSellDailyId() {
        return productSellDailyId;
    }

    public void setProductSellDailyId(Long productSellDailyId) {
        this.productSellDailyId = productSellDailyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
