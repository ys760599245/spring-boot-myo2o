package com.chen.myo2o.entity;

import java.util.Date;

//顾客消费的商品映射
public class UserProductMap {
    //主键id
    private Long userProductId;
    private Long userId;
    private Long productId;
    private Long shopId;
    private String userName;
    private String productName;
    //创建时间
    private Date createTime;
    //消费商品所获得积分
    private Integer point;
    //顾客信息实体类
    private PersonInfo user;
    //商品信息实体类
    private Product product;
    //店铺信息实体类
    private Shop shop;

    public Long getUserProductId() {
        return userProductId;
    }

    public void setUserProductId(Long userProductId) {
        this.userProductId = userProductId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public PersonInfo getUser() {
        return user;
    }

    public void setUser(PersonInfo user) {
        this.user = user;
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

    @Override
    public String toString() {
        return "UserProductMap{" +
                "userProductId=" + userProductId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", shopId=" + shopId +
                ", userName='" + userName + '\'' +
                ", productName='" + productName + '\'' +
                ", createTime=" + createTime +
                ", point=" + point +
                ", user=" + user +
                ", product=" + product +
                ", shop=" + shop +
                '}';
    }
}
