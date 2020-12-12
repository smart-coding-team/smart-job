

package com.smartcoding.schedule.core.model.bo;

import com.smartcoding.schedule.core.model.XxlJobInfo;

public class XxlJobInfoBO extends XxlJobInfo {

    private int onLineNum;



    private String title;



    public int getOnLineNum() {
        return onLineNum;
    }

    public void setOnLineNum(int onLineNum) {
        this.onLineNum = onLineNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
