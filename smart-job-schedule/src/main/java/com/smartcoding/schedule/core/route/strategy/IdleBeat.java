package com.smartcoding.schedule.core.route.strategy;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class IdleBeat implements Serializable {
    private String title;
    private String address;
    private Integer code;
    private String msg;

    public String toJson() {
        return JSON.toJSONString(this);
    }

    public String toJsonIgnoreTitle() {
        Map<String, Object> map = new LinkedHashMap<>(3);
        map.put("address", this.getAddress());
        map.put("code", this.getCode());
        map.put("msg", this.getMsg());
        return JSONUtil.toJsonStr(map);
    }
}
