package com.chen.myo2o.util.weixin;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class WeiXinUser implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // openid  标识该公众号下面该用户的唯一的id
    @JsonProperty("openid")
    private String openId;
    private int id;

    //用户昵称
    @JsonProperty("nickname")
    private String nickName;
    //用户的性别
    @JsonProperty("sex")
    private int sex;
    //用户的省份
    @JsonProperty("province")
    private String province;
    //用户的城市
    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    //区
    private String country;
    //用户的头像地址
    @JsonProperty("headimgurl")
    private String headimgurl;
    //权限
    @JsonProperty("privilege")
    private String privilege;
    @JsonProperty("unionid")
    private String unionid;
    //语言
    @JsonProperty("language")
    private String language;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "WeiXinUser{" +
                "openId='" + openId + '\'' +
                ", id=" + id +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", privilege='" + privilege + '\'' +
                ", unionid='" + unionid + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
