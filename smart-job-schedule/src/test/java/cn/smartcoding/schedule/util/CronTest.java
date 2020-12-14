
//
//package com.smartcoding.schedule.util;
//
//import cn.hutool.com.smartcoding.robot.core.util.IdUtil;
//import cn.hutool.http.HttpUtil;
//import cn.hutool.json.JSONArray;
//import cn.hutool.json.JSONUtil;
//import com.smartcoding.xxl.job.com.smartcoding.robot.core.biz.model.ReturnT;
//import org.junit.Test;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author 无缺
// * @date 2019-07-10
// */
//public class CronTest {
//    @Test
//    public void name() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("CronExpression", "10 * * * * 2?");
//        String s = HttpUtil.get("http://cron.qqe2.com/CalcRunTime.ashx", map);
//        //JSONUtil.toBean(s,);
//        System.out.println("s = " + s);
//        ReturnT<List<String>> returnT = new ReturnT<>();
//        List<String> stringList = new ArrayList<>();
//        stringList.add("2019/7/11 13:24:10");
//        stringList.add("2019/7/11 13:34:10");
//        stringList.add("2019/7/11 13:54:10");
//        String[] asr = new String[]{"2019/7/11 13:24:10", "2019/7/11 13:44:10", "2019/7/11 13:24:30"};
//        System.out.println("stringList = " + JSONUtil.toJsonStr(stringList));
//        JSONArray objects = JSONUtil.parseArray(s);
//        List<String> stringList1 = objects.toList(String.class);
//        System.out.println("stringList1 = " + stringList1);
//        System.out.println("asr = " + asr);
//
//    }
//
//    @Test
//    public void name5() {
//        String s = IdUtil.fastUUID();
//        String s1 = IdUtil.simpleUUID();
//        String s2 = IdUtil.fastSimpleUUID();
//        System.out.println("s = " + s);
//        System.out.println("s = " + s1);
//        System.out.println("s = " + s2);
//    }
//
//    @Test
//    public void name111() {
//
//        Double duration=3.85D;
//        String covertMinuteSecond = durationCovertMinuteSecond(duration);
//        System.out.println(covertMinuteSecond);
//
//        Double duration1=5.85D;
//        String covertMinuteSecond2 = durationCovertMinuteSecond(duration1);
//        System.out.println(covertMinuteSecond2);
//    }
//    /**
//     * 通话时长1.5分转成xxx分xxx秒
//     *
//     * @param duration
//     * @return
//     */
//    public static String durationCovertMinuteSecond(Double duration) {
//        if (duration == null) {
//            return "0秒";
//        }
//        BigDecimal bigDecimal = new BigDecimal(duration.toString());
//        //分：只保留整数
//        BigDecimal minute = bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
//        //秒：四舍五入
//        BigDecimal second = bigDecimal.subtract(minute).multiply(new BigDecimal("60")).setScale(0, BigDecimal.ROUND_UP);
//        return BigDecimal.ZERO.equals(minute) ? second + "秒" : minute + "分" + second + "秒";
//    }
//}
