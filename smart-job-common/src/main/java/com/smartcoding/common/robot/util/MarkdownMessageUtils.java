package com.smartcoding.common.robot.util;

import com.smartcoding.common.robot.exception.WebHookRobotIllegalArgumentException;

import java.util.List;

public class MarkdownMessageUtils {

    public static String getBoldText(String text) {
        return "**" + text + "**";
    }

    public static String getItalicText(String text) {
        return "*" + text + "*";
    }

    public static String getLinkText(String text, String href) {
        return "[" + text + "](" + href + ")";
    }

    public static String getImageText(String imageUrl) {
        return "![image](" + imageUrl + ")";
    }

    public static String getHeaderText(int headerType, String text) {
        if (headerType < 1 || headerType > 6) {
            throw new WebHookRobotIllegalArgumentException("headerType should be in [1, 6]");
        }
        StringBuilder numbers = new StringBuilder();
        for (int i = 0; i < headerType; i++) {
            numbers.append("#");
        }
        return numbers + " " + text;
    }

    public static String getReferenceText(String text) {
        return "> " + text;
    }

    public static String getOrderListText(List<String> orderItem) {
        if (orderItem.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= orderItem.size() - 1; i++) {
            sb.append(String.valueOf(i) + ". " + orderItem.get(i - 1) + "\n");
        }
        sb.append(String.valueOf(orderItem.size()) + ". " + orderItem.get(orderItem.size() - 1));
        return sb.toString();
    }

    public static String getUnorderListText(List<String> unorderItem) {
        if (unorderItem.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < unorderItem.size() - 1; i++) {
            sb.append("- ").append(unorderItem.get(i)).append("\n");
        }
        sb.append("- ").append(unorderItem.get(unorderItem.size() - 1));
        return sb.toString();
    }
}
