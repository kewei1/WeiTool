package com.github.kewei1.http;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class URLUtil {

    private URLUtil() {
    }

    /**
     * 创建URL拼接GET URL
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param charset 编码
     * @return
     * @throws Exception
     */
    public static String buildGetUrl(String url, Map<String, Object> params, String charset) throws Exception {
        String queryParams = buildQueryParams(params, charset);
        if (null != queryParams && !queryParams.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            if (url.endsWith("?")) {
                sb.append(url).append(queryParams);
            } else {
                sb.append(url).append("?").append(queryParams);
            }

            return sb.toString();
        } else {
            return url;
        }
    }

    /**
     * 创建URL拼接GET URL
     *
     * @param url     请求地址
     * @param key     参数名
     * @param value   参数值
     * @param charset 编码
     * @return
     * @throws Exception
     */
    public static String buildGetUrl(String url, String key, String value, String charset) throws Exception {
        return buildGetUrl(url, Collections.singletonMap(key, value), charset);
    }

    /**
     * 构建单个URL查询参数
     *
     * @param key     参数名
     * @param value   参数值
     * @param charset 编码
     * @return
     * @throws Exception
     */
    public static String buildQueryParams(String key, String value, String charset) throws Exception {
        return buildQueryParams(Collections.singletonMap(key, value), charset);
    }


    public static String buildQueryParams(Map<String, Object> params, String charset) throws Exception {
        return buildQueryParams(params, charset, true);
    }


    public static String buildQueryParams(Map<String, Object> params, String charset, boolean uncode) throws Exception {
        if (null != params && !params.isEmpty()) {
            StringBuilder query = new StringBuilder();
            boolean hasParam = false;
            Iterator var4 = params.entrySet().iterator();

            while (var4.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) var4.next();
                Object v = entry.getValue();
                if (v != null) {
                    if (hasParam) {
                        query.append("&");
                    } else {
                        hasParam = true;
                    }
                    String value = null;
                    if (uncode) {
                        value = URLEncoder.encode(v.toString(), charset);
                    } else {
                        value = v.toString();
                    }
                    query.append((String) entry.getKey()).append("=").append(value);
                }
            }

            return query.toString();
        } else {
            return null;
        }
    }
}
