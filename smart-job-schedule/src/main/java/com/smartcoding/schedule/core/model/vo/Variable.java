package com.smartcoding.schedule.core.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Variable implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String variable;
    private String value;
    private String prop;
}
