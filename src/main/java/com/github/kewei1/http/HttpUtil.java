package com.github.kewei1.http;


import com.alibaba.fastjson.JSON;
import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {



    //默认编码
    public static final String DEFAULT_CHARSET = "UTF-8";

    //默认Socket读取超时时间
    public static final int DEFAULT_READ_TIMEOUT = 60 * 1000;

    //默认Socket连接超时时间
    public static final int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;

    public HttpUtil() {
    }

    /**
     * 发送Get请求
     * @param url 请求地址
     * @return 请求结果
     * @throws Exception
     */

    public static String doGet(String url) throws Exception {
        return doGet(url, (Map)null);
    }

    /**
     * 发送Get请求
     * @param url 请求地址
     * @param params 请求参数，会以拼接url参数
     * @return 请求结果
     * @throws Exception
     */
    public static String doGet(String url, Map<String, Object> params) throws Exception {
        return doGet(url, (Map)null, params);
    }

    /**
     * 发送Get请求
     * @param url 请求地址
     * @param headers http 请求头参数
     * @param params 请求参数，会以拼接url参数
     * @return 请求结果
     * @throws Exception
     */
    public static String doGet(String url, Map<String, Object> headers, Map<String, Object> params) throws Exception {
        return doGet(url, headers, params, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    /**
     * 发送Get请求
     * @param url 请求地址
     * @param params 请求参数，会以拼接url参数
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @return 请求结果
     * @throws Exception
     */
    public static String doGet(String url, Map<String, Object> params, int connectTimeout, int readTimeout) throws Exception {
        return doGet(url, null, params, connectTimeout, readTimeout, DEFAULT_CHARSET);
    }

    /**
     * 发送Get请求
     * @param url 请求地址
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @return 请求结果
     * @throws Exception
     */
    public static String doGet(String url, int connectTimeout, int readTimeout) throws Exception {
        return doGet(url, null, null, connectTimeout, readTimeout, DEFAULT_CHARSET);
    }

    /**
     * 发送Get请求
     * @param url 请求地址
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @return 请求结果
     * @throws Exception
     */
    public static String doGet(String url, Map<String, Object> headers, Map<String, Object> params, int connectTimeout, int readTimeout) throws Exception {
        return doGet(url, headers, params, connectTimeout, readTimeout, DEFAULT_CHARSET);
    }

    /**
     * 发送Get请求，使用默认配置，配置超时时间
     * @param url 请求地址
     * @param headers 请求头参数
     * @param params 请求参数，会以拼接url参数
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @param charset 流编码
     * @return 请求结果
     * @throws Exception
     */
    public static String doGet(String url, Map<String, Object> headers, Map<String, Object> params, int connectTimeout, int readTimeout, String charset) throws Exception {
        String get_url = URLUtil.buildGetUrl(url, params, charset);
        return doRequest("GET", get_url, headers, (String)null, connectTimeout, readTimeout, charset);
    }

    /**
     * 发送POST 请求
     * @param url 请求地址
     * @return 请求结果
     * @throws Exception
     */
    public static String doPost(String url) throws Exception {
        return doPost(url, (Map)null);
    }

    /**
     * 发送POST 请求
     * @param url 请求地址
     * @param params 请求参数 以请求正文并且Content-Type 为application/x-www-form-urlencoded
     * @return 请求结果
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> params) throws Exception {
        return doPost(url, (Map)null, params);
    }

    /**
     * 发送POST 请求
     * @param url 请求地址
     * @param headers 请求头参数
     * @param params 请求参数 以请求正文并且Content-Type 为application/x-www-form-urlencoded
     * @return 请求结果
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> headers, Map<String, Object> params) throws Exception {
        return doPost(url, headers, params, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    /**
     * 发送POST 请求
     * @param url 请求地址
     * @param params 请求参数 以请求正文并且Content-Type 为application/x-www-form-urlencoded
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @return 请求结果
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> params, int connectTimeout, int readTimeout) throws Exception {
        return doPost(url, null, params, connectTimeout, readTimeout, DEFAULT_CHARSET);
    }

    /**
     * 发送POST 请求
     * @param url 请求地址
     * @param headers 请求头参数
     * @param params 请求参数 以请求正文并且Content-Type 为application/x-www-form-urlencoded
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @return 请求结果
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> headers, Map<String, Object> params, int connectTimeout, int readTimeout) throws Exception {
        return doPost(url, headers, params, connectTimeout, readTimeout, DEFAULT_CHARSET);
    }

    /**
     * 发送POST 请求
     * @param url 请求地址
     * @param headers 请求头参数
     * @param params 请求参数 以请求正文并且Content-Type 为application/x-www-form-urlencoded
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @param charset 流编码
     * @return 请求结果
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> headers, Map<String, Object> params, int connectTimeout, int readTimeout, String charset) throws Exception {
        String data = URLUtil.buildQueryParams(params, charset);
        return doRequest("POST", url, headers, data, connectTimeout, readTimeout, charset);
    }

    /**
     * 发送POST 请求
     * @param url 请求地址
     * @param headers 请求头参数
     * @param data 请求正文 以Content-Type 为text/plain
     * @return
     * @throws Exception
     */
    public static String doPostStr(String url, Map<String, Object> headers, String data) throws Exception {
        return doPostStr(url, headers, data, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    /**
     * 发送POST 请求
     * @param url 请求地址
     * @param headers 请求头参数
     * @param data 请求正文 以Content-Type 为text/plain
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @return 请求结果
     * @throws Exception
     */
    public static String doPostStr(String url, Map<String, Object> headers, String data, int connectTimeout, int readTimeout) throws Exception {
        return doPostStr(url, headers, data, connectTimeout, readTimeout, DEFAULT_CHARSET);
    }

    /**
     * 发送POST 请求
     * @param url 请求地址
     * @param headers 请求头参数
     * @param data 请求正文 以Content-Type 为text/plain
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @param charset 流编码
     * @return 请求结果
     * @throws Exception
     */
    public static String doPostStr(String url, Map<String, Object> headers, String data, int connectTimeout, int readTimeout, String charset) throws Exception {
        return doRequest("POST", url, headers, data, connectTimeout, readTimeout, charset);
    }

    /**
     * 发送POST 请求
     * @param url 请求地址
     * @param headers 请求头参数
     * @param params 请求参数 以请求正文并且Content-Type 为application/json
     * @return 请求结果
     * @throws Exception
     */
    public static String doPostJson(String url, Map<String, Object> headers, Map<String, Object> params) throws Exception {
        return doPostJson(url, headers, params, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    /**
     * 发送POST 请求
     * @param url 请求地址
     * @param params 请求参数 以请求正文并且Content-Type 为application/json
     * @return 请求结果
     * @throws Exception
     */
    public static String doPostJson(String url, Map<String, Object> params) throws Exception {
        return doPostJson(url, null, params, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    /**
     * 发送POST 请求
     * @param url 请求地址
     * @param headers 请求头参数
     * @param params 请求参数 以请求正文并且Content-Type 为application/json
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @return
     * @throws Exception
     */
    public static String doPostJson(String url, Map<String, Object> headers, Map<String, Object> params, int connectTimeout, int readTimeout) throws Exception {
        return doPostJson(url, headers, params, connectTimeout, readTimeout, DEFAULT_CHARSET);
    }

    /**
     * 发送POST 请求
     * @param url 请求地址
     * @param params 请求参数 以请求正文并且Content-Type 为application/json
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @return
     * @throws Exception
     */
    public static String doPostJson(String url, Map<String, Object> params, int connectTimeout, int readTimeout) throws Exception {
        return doPostJson(url, null, params, connectTimeout, readTimeout, DEFAULT_CHARSET);
    }

    /**
     * 发送POST 请求
     * @param url 请求地址
     * @param headers 请求头参数
     * @param params 请求参数 以请求正文并且Content-Type 为application/json
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @param charset 流编码
     * @return 返回结果
     * @throws Exception
     */
    public static String doPostJson(String url, Map<String, Object> headers, Map<String, Object> params, int connectTimeout, int readTimeout, String charset) throws Exception {
        if (headers == null) {
            headers = new HashMap();
        }

        if (!((Map)headers).containsKey("Content-Type")) {
            ((Map)headers).put("Content-Type", "application/json; charset=" + charset);
        }

        String data = JSON.toJSONString(params);
        return doRequest("POST", url, (Map)headers, data, connectTimeout, readTimeout, charset);
    }

    /**
     * 发送POST 文件请求
     * @param url 请求地址
     * @param headers 请求头参数
     * @param params 请求参数
     * @param fileParams 文件参数
     * @return
     * @throws Exception
     */
    public static String doPostFile(String url, Map<String, Object> headers, Map<String, Object> params, Map<String, FileItem> fileParams) throws Exception {
        return doPostFile(url, headers, params, fileParams, DEFAULT_READ_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    /**
     * 发送POST 文件请求
     * @param url 请求地址
     * @param headers 请求头参数
     * @param params 请求参数
     * @param fileParams 文件参数
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @return
     * @throws Exception
     */
    public static String doPostFile(String url, Map<String, Object> headers, Map<String, Object> params, Map<String, FileItem> fileParams, int connectTimeout, int readTimeout) throws Exception {
        return doPostFile(url, headers, params, fileParams, connectTimeout, readTimeout, DEFAULT_CHARSET);
    }

    /**
     * 发送POST 文件请求
     * @param url 请求地址
     * @param headers 请求头参数
     * @param params 请求参数
     * @param fileParams 文件参数
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @param charset 流编码
     * @return
     * @throws Exception
     */
    public static String doPostFile(String url, Map<String, Object> headers, Map<String, Object> params, Map<String, FileItem> fileParams, int connectTimeout, int readTimeout, String charset) throws Exception {
        HttpURLConnection http = null;
        InputStream in = null;
        OutputStream out = null;

        String var32;
        try {
            String boundary = String.valueOf(System.currentTimeMillis());
            http = getHttpConnection("POST", url, connectTimeout, readTimeout);
            http.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary + ";charset=" + charset);
            if (headers != null && !headers.isEmpty()) {
                Iterator var11 = headers.entrySet().iterator();

                while(var11.hasNext()) {
                    Entry<String, Object> entry = (Entry)var11.next();
                    http.setRequestProperty((String)entry.getKey(), entry.getValue().toString());
                }
            }

            out = http.getOutputStream();
            byte[] entryBoundaryBytes = ("\r\n--" + boundary + "\r\n").getBytes(charset);
            Entry fileEntry;
            Iterator var30;
            if (params != null && !params.isEmpty()) {
                var30 = params.entrySet().iterator();

                while(var30.hasNext()) {
                    fileEntry = (Entry)var30.next();
                    byte[] textBytes = getTextEntry((String)fileEntry.getKey(), String.valueOf(fileEntry.getValue()), charset);
                    out.write(entryBoundaryBytes);
                    out.write(textBytes);
                }
            }

            if (fileParams != null && !fileParams.isEmpty()) {
                var30 = fileParams.entrySet().iterator();

                while(var30.hasNext()) {
                    fileEntry = (Entry)var30.next();
                    FileItem fileItem = (FileItem)fileEntry.getValue();
                    byte[] fileBytes = getFileEntry((String)fileEntry.getKey(), fileItem.getFileName(), fileItem.getMimeType(), charset);
                    out.write(entryBoundaryBytes);
                    out.write(fileBytes);
                    out.write(fileItem.getContent());
                }
            }

            byte[] endBoundaryBytes = ("\r\n--" + boundary + "--\r\n").getBytes(charset);
            out.write(endBoundaryBytes);
            out.flush();
            in = http.getInputStream();
            var32 = getStreamAsString(in, charset);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception var27) {
                var27.printStackTrace();
            }

            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception var26) {
                var26.printStackTrace();
            }

            try {
                if (http != null) {
                    http.disconnect();
                }
            } catch (Exception var25) {
                var25.printStackTrace();
            }

        }

        return var32;
    }

    /**
     * 直接发送HTTP请求
     * @param method 请求方法 GET，POST PUT，DELETE ...
     * @param url 请求地址
     * @param headers 请求头参数
     * @param data 请求正文（对有正文请求 请求正文 以Content-Type 为text/plain）
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @param charset 流编码
     * @return 请求结果
     * @throws Exception
     */
    public static String doRequest(String method, String url, Map<String, Object> headers, String data, int connectTimeout, int readTimeout, String charset) throws Exception {
        HttpURLConnection http = null;
        InputStream in = null;
        OutputStream out = null;

        String var25;
        try {
            http = getHttpConnection(method, url, connectTimeout, readTimeout);
            if (headers != null && !headers.isEmpty()) {
                Iterator var10 = headers.entrySet().iterator();

                while(var10.hasNext()) {
                    Entry<String, Object> entry = (Entry)var10.next();
                    http.setRequestProperty((String)entry.getKey(), entry.getValue().toString());
                }
            }

            if (data != null && !data.trim().isEmpty()) {
                out = http.getOutputStream();
                out.write(data.getBytes(charset));
                out.flush();
            }

            in = http.getInputStream();
            var25 = getStreamAsString(in, charset);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception var23) {
                var23.printStackTrace();
            }

            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception var22) {
                var22.printStackTrace();
            }

            try {
                if (http != null) {
                    http.disconnect();
                }
            } catch (Exception var21) {
                var21.printStackTrace();
            }

        }

        return var25;
    }

    /**
     * 获取HttpURLConnection对象
     * @param method 请求方式
     * @param url 请求地址
     * @param connectTimeout 连接超时时间 单位 毫秒
     * @param readTimeout 读取超时时间 单位 毫秒
     * @return java.net.HttpURLConnection
     * @throws Exception
     */
    private static HttpURLConnection getHttpConnection(String method, String url, int connectTimeout, int readTimeout) throws Exception {
        boolean isSSL = url.startsWith("https");
        if (isSSL) {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(new KeyManager[0], new TrustManager[]{new HttpUtil.SimpleTrustManager()}, new SecureRandom());
            SSLSocketFactory sslf = sslContext.getSocketFactory();
            HttpsURLConnection https = (HttpsURLConnection)(new URL(url)).openConnection();
            https.setHostnameVerifier(new HttpUtil.SimpleHostnameVerifier());
            https.setSSLSocketFactory(sslf);
            https.setRequestMethod(method);
            https.setDoOutput(true);
            https.setDoInput(true);
            https.setUseCaches(false);
            https.setConnectTimeout(connectTimeout);
            https.setReadTimeout(readTimeout);
            return https;
        } else {
            HttpURLConnection http = (HttpURLConnection)(new URL(url)).openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.setDoInput(true);
            http.setUseCaches(false);
            http.setConnectTimeout(connectTimeout);
            http.setReadTimeout(readTimeout);
            return http;
        }
    }

    /**
     *
     * @param in
     * @param charset
     * @return
     * @throws Exception
     */
    public static String getStreamAsString(InputStream in, String charset) throws Exception {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(in, charset));
            StringBuilder buffer = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String var5 = buffer.toString();
            return var5;
        } finally {
            if (reader != null) {
                reader.close();
            }

        }
    }



    private static byte[] getTextEntry(String fieldName, String fieldValue, String charset) throws Exception {
        StringBuilder entry = new StringBuilder();
        entry.append("Content-Disposition:form-data; name=\"");
        entry.append(fieldName);
        entry.append("\"\r\nContent-Type:text/plain\r\n\r\n");
        entry.append(fieldValue);
        return entry.toString().getBytes(charset);
    }

    private static byte[] getFileEntry(String fieldName, String fileName, String mimeType, String charset) throws Exception {
        StringBuilder entry = new StringBuilder();
        entry.append("Content-Disposition:form-data; name=\"");
        entry.append(fieldName);
        entry.append("\"; filename=\"");
        entry.append(fileName);
        entry.append("\"\r\nContent-Type:");
        entry.append(mimeType);
        entry.append("\r\n\r\n");
        return entry.toString().getBytes(charset);
    }

    private static class SimpleTrustManager implements X509TrustManager {
        private SimpleTrustManager() {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }

    private static class SimpleHostnameVerifier implements HostnameVerifier {
        private SimpleHostnameVerifier() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}