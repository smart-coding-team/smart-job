

package cn.smartcoding.schedule.core.model.bo;

import java.io.Serializable;
import java.util.Date;

public class XxlJobUserBO implements Serializable {
    /**
     * INTEGER(10) 必填
     *
     */
    private Integer id;

    /**
     * VARCHAR(50) 必填
     * 账号
     */
    private String username;

    /**
     * VARCHAR(50) 必填
     * 登录方式
     */
    private String loginWay;

    /**
     * VARCHAR(100) 默认值[]
     * 花名
     */
    private String nickName;

    /**
     * VARCHAR(200) 默认值[]
     * 备注
     */
    private String remark;

    /**
     * VARCHAR(100) 默认值[]
     * 邮箱
     */
    private String email;

    /**
     * VARCHAR(30) 默认值[]
     *
     */
    private String mobile;

    /**
     * TINYINT(3) 必填
     * 角色：0-普通用户、1-管理员
     */
    private Integer role;

    /**
     * VARCHAR(255)
     * 权限：执行器ID列表，多个逗号分割
     */
    private String permission;

    /**
     * TIMESTAMP(19) 默认值[CURRENT_TIMESTAMP] 必填
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * TIMESTAMP(19) 默认值[CURRENT_TIMESTAMP] 必填
     * 修改时间
     */
    private Date gmtModified;

    private static final long serialVersionUID = 1L;

    /**
     * INTEGER(10) 必填
     * 获得
     */
    public Integer getId() {
        return id;
    }

    /**
     * INTEGER(10) 必填
     * 设置
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * VARCHAR(50) 必填
     * 获得 账号
     */
    public String getUsername() {
        return username;
    }

    /**
     * VARCHAR(50) 必填
     * 设置 账号
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * VARCHAR(50) 必填
     * 获得 登录方式
     */
    public String getLoginWay() {
        return loginWay;
    }

    /**
     * VARCHAR(50) 必填
     * 设置 登录方式
     */
    public void setLoginWay(String loginWay) {
        this.loginWay = loginWay;
    }

    /**
     * VARCHAR(100) 默认值[]
     * 获得 花名
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * VARCHAR(100) 默认值[]
     * 设置 花名
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * VARCHAR(200) 默认值[]
     * 获得 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * VARCHAR(200) 默认值[]
     * 设置 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * VARCHAR(100) 默认值[]
     * 获得 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * VARCHAR(100) 默认值[]
     * 设置 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * VARCHAR(30) 默认值[]
     * 获得
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * VARCHAR(30) 默认值[]
     * 设置
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * TINYINT(3) 必填
     * 获得 角色：0-普通用户、1-管理员
     */
    public Integer getRole() {
        return role;
    }

    /**
     * TINYINT(3) 必填
     * 设置 角色：0-普通用户、1-管理员
     */
    public void setRole(Integer role) {
        this.role = role;
    }

    /**
     * VARCHAR(255)
     * 获得 权限：执行器ID列表，多个逗号分割
     */
    public String getPermission() {
        return permission;
    }

    /**
     * VARCHAR(255)
     * 设置 权限：执行器ID列表，多个逗号分割
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * TIMESTAMP(19) 默认值[CURRENT_TIMESTAMP] 必填
     * 获得 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * TIMESTAMP(19) 默认值[CURRENT_TIMESTAMP] 必填
     * 设置 创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * TIMESTAMP(19) 默认值[CURRENT_TIMESTAMP] 必填
     * 获得 修改时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * TIMESTAMP(19) 默认值[CURRENT_TIMESTAMP] 必填
     * 设置 修改时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", password=").append(loginWay);
        sb.append(", nickName=").append(nickName);
        sb.append(", remark=").append(remark);
        sb.append(", email=").append(email);
        sb.append(", MOBILE=").append(mobile);
        sb.append(", role=").append(role);
        sb.append(", permission=").append(permission);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        XxlJobUserBO other = (XxlJobUserBO) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getLoginWay() == null ? other.getLoginWay() == null : this.getLoginWay().equals(other.getLoginWay()))
            && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getMobile() == null ? other.getMobile() == null : this.getMobile().equals(other.getMobile()))
            && (this.getRole() == null ? other.getRole() == null : this.getRole().equals(other.getRole()))
            && (this.getPermission() == null ? other.getPermission() == null : this.getPermission().equals(other.getPermission()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getGmtModified() == null ? other.getGmtModified() == null : this.getGmtModified().equals(other.getGmtModified()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getLoginWay() == null) ? 0 : getLoginWay().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getMobile() == null) ? 0 : getMobile().hashCode());
        result = prime * result + ((getRole() == null) ? 0 : getRole().hashCode());
        result = prime * result + ((getPermission() == null) ? 0 : getPermission().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
        return result;
    }
}
