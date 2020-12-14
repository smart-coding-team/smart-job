package cn.smartcoding.common.robot.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;

/**
 * --添加相关注释--
 *
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2020/02/07- 3:20 下午
 */
@Slf4j
public class HttpUtils {
    private static HttpClient HTTP_CLIENT = null;
    //连接超时时间
    private static int connectionTimeout = 5000;
    //读取超时时间
    private static int socketTimeout = 60000;
    private static int MAX_TOTAL = 100;
    //单路由的最大并发连接数
    private static int MAX_PER_ROUTE = 10;
    //设置从链接池中获取连接时间为无限大
    private static int CONNECTION_REQUEST_TIMEOUT = 0;

    static {
        RequestConfig defaultRequestConfig = null;
        //  创建连接池管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        //  设置socket配置
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .build();
        connManager.setDefaultSocketConfig(socketConfig);
        connManager.setMaxTotal(MAX_TOTAL);
        connManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        //  设置获取连接超时时间、建立连接超时时间、从服务端读取数据的超时时间
        defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .build();
        //  创建httpclient实例
        HTTP_CLIENT = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
    }

    public static HttpResponse sendPost(String webHook, String param) throws IOException {
        HttpPost httppost = new HttpPost(webHook);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity se = new StringEntity(param, "utf-8");
        httppost.setEntity(se);
        return HTTP_CLIENT.execute(httppost);
    }

    public static byte[] getBytes(String url) {
        try {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse execute = HTTP_CLIENT.execute(httpGet);
            return IOUtils.toByteArray(execute.getEntity().getContent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
