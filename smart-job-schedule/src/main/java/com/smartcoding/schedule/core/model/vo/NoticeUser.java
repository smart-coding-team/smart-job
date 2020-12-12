package com.smartcoding.schedule.core.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class NoticeUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mobile;
    private String email;
}
