package cn.smartcoding.system.dto;

import cn.smartcoding.common.core.domain.TreeSelect;
import lombok.Data;

import java.util.List;

@Data
public class SysDeptDto {
    private List<Integer> checkedKeys;

    private List<TreeSelect>  depts;
}
