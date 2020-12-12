package com.smartcoding.common.robot.model.message.wx.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxNewsArticle {
    private String title;
    private String description;
    private String picurl;
    private String url;
}
