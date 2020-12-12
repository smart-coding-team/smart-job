package com.smartcoding.system.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysLdapConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Boolean enabled;

    private String urls;

    private Boolean enableSsl;

    private String base;

    private String managerDn;

    private String managerPassword;

    private String userDnPatterns;

    private String attributesMail;

    private String attributesTelephone;

    private String remark;

    private String updateBy;

    private Date gmtCreate;

    private Date gmtModified;

}
