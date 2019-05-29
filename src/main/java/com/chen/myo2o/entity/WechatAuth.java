/**
 *
 */
package com.chen.myo2o.entity;

import java.util.Date;

/** 微信登录的实体类
 * @author 760599245 微信
 */
public class WechatAuth {
    //主键ID
    private Long wechatAuthId;
    private Long userId;
    //微信获取用户信息的凭证 对于某个公众号具有唯一性别
    private String openId;
    //创建时间
    private Date createTime;
    //用户的信息
    private PersonInfo personInfo;

    public Long getWechatAuthId() {
        return wechatAuthId;
    }

    public void setWechatAuthId(Long wechatAuthId) {
        this.wechatAuthId = wechatAuthId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

}
