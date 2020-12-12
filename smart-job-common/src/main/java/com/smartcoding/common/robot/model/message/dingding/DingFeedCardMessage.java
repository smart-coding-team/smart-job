package com.smartcoding.common.robot.model.message.dingding;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.smartcoding.common.robot.exception.WebHookRobotIllegalArgumentException;
import com.smartcoding.common.robot.model.message.Message;
import com.smartcoding.common.robot.util.ErrorStringUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DingFeedCardMessage implements Message {


    private List<LinksBean> links;


    public static class LinksBean {

        /**
         * 单条信息文本
         */
        private String title;
        /**
         * 点击单条信息到跳转链接
         */
        private String messageURL;
        /**
         * 单条信息后面图片的URL
         */
        private String picURL;

        public LinksBean() {
        }

        public LinksBean(String title, String messageURL, String picURL) {
            this.title = title;
            this.messageURL = messageURL;
            this.picURL = picURL;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessageURL() {
            return messageURL;
        }

        public void setMessageURL(String messageURL) {
            this.messageURL = messageURL;
        }

        public String getPicURL() {
            return picURL;
        }

        public void setPicURL(String picURL) {
            this.picURL = picURL;
        }
    }

    @Override
    public String toJsonString() {
        if (CollectionUtil.isEmpty(links)) {
            throw new WebHookRobotIllegalArgumentException(ErrorStringUtils.dingdingError(" [links] should not be empty"));
        }
        Map<String, Object> items = new HashMap<String, Object>();
        items.put("msgtype", "feedCard");

        Map<String, List<LinksBean>> listMap = new HashMap<String, List<LinksBean>>(1);

        listMap.put("links", links);

        items.put("feedCard", listMap);
        return JSON.toJSONString(items);
    }
}
