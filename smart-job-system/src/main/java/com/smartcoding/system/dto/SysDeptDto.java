package com.smartcoding.system.dto;

import com.smartcoding.common.core.domain.TreeSelect;
import lombok.Data;

import java.util.List;

@Data
public class SysDeptDto {
    private List<Integer> checkedKeys;

    private List<TreeSelect>  depts;
}
