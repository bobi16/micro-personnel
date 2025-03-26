package com.bobiko.security.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: bobiko
 * @CreateTime: 2025-03-26
 * @Description: 用户信息实体类
 */
public class SysUser implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户类型（0系统管理员）
     */
    private Integer userType;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String disable;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 部门ID
     */
    public Long getDeptId() {
        return deptId;
    }

    /**
     * 部门ID
     */
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    /**
     * 用户账号
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 用户账号
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 用户昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 用户昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 用户类型（0系统管理员）
     */
    public Integer getUserType() {
        return userType;
    }

    /**
     * 用户类型（0系统管理员）
     */
    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    /**
     * 用户邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 用户邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 手机号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 手机号码
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 用户性别（0男 1女 2未知）
     */
    public String getSex() {
        return sex;
    }

    /**
     * 用户性别（0男 1女 2未知）
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 头像地址
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 头像地址
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 帐号状态（0正常 1停用）
     */
    public String getDisable() {
        return disable;
    }

    /**
     * 帐号状态（0正常 1停用）
     */
    public void setDisable(String disable) {
        this.disable = disable;
    }

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    public String getDelFlag() {
        return delFlag;
    }

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * 最后登录IP
     */
    public String getLoginIp() {
        return loginIp;
    }

    /**
     * 最后登录IP
     */
    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    /**
     * 最后登录时间
     */
    public Date getLoginDate() {
        return loginDate;
    }

    /**
     * 最后登录时间
     */
    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    /**
     * 创建者
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 创建者
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 更新者
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 更新者
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}