package com.smartcoding.schedule.core.trigger;

import com.smartcoding.schedule.core.util.I18nUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TriggerMsg {

    private String key;
    private String name;
    private String value;
    private Integer sort;


    public static TriggerMsg build(String key, String value,int sort) {
        return new TriggerMsg(key, I18nUtil.getString(key), value,sort);
    }


}
