package com.chen.myo2o.entity;

import java.util.Date;

public class LocalAuth {
    //主键id
    private Long localAuthId;
    //账号
    private String userName;
    //密码
    private String password;
    private Long userId;
    //创建时间
    private Date createTime;
    //最后一次的更新时间
    private Date lastEditTime;
    //个人信息 关系为--对应
    private PersonInfo personInfo;

    public Long getLocalAuthId() {
        return localAuthId;
    }

    public void setLocalAuthId(Long localAuthId) {
        this.localAuthId = localAuthId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    @Override
    public String toString() {
        return "LocalAuth{" +
                "localAuthId=" + localAuthId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                ", personInfo=" + personInfo +
                '}';
    }
}
