package com.wr.domain.LoginPojo;


import java.util.ArrayList;
import java.util.List;

public class SysUserBo {

    private Long userId;
    private String userName;
    private String mobile;
    private String password;
    private boolean admin;
    private String status;

    /** 角色对象 */
    private List<SysRoleBo> sysRoleBos;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SysRoleBo> getSysRoleBos() {
        return new ArrayList<>();
    }

    public void setSysRoleBos(List<SysRoleBo> sysRoleBos) {
        this.sysRoleBos = sysRoleBos;
    }
}
