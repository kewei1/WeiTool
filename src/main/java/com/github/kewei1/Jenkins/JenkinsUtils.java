package com.github.kewei1.Jenkins;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.kewei1.xxlJob.XxlJobUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class JenkinsUtils {

    /**
     * @since 2023/02/03
     * 后缀
     */
    private final  static String SUFFIX="/api/json";
    /**
     * @since 2023/02/03
     * job
     */
    private final  static String JOB="/job/";

    /**
     * @since 2023/02/03
     * 基础url
     */
    private static String BASE_URL="";
    /**
     * @since 2023/02/03
     * JENKINS令牌
     */
    private static String JENKINS_TOKEN = "";
    /**
     * @since 2023/02/03
     * JENKINS用户名
     */
    private static String JENKINS_USERNAME = "";


    /**
     * JENKINS配置
     *
     * @param url      url
     * @param token    令牌
     * @param username 用户名
     * @author kewei
     * @since 2023/02/03
     */
    public  JenkinsUtils(String url,String token,String username){
        BASE_URL = url;
        JENKINS_TOKEN = token;
        JENKINS_USERNAME = username;
    }


    /**
     * 自定义http请求
     *
     * @param url         url
     * @param httpRequest http请求
     * @author kewei
     * @since 2023/02/03
     */
    public  String customHttpMsg(String url, HttpRequest httpRequest) throws Exception {
        URI uri = new URI(url);

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()), new UsernamePasswordCredentials(JENKINS_USERNAME, JENKINS_TOKEN));
        AuthCache authCache = (AuthCache) new BasicAuthCache();
        HttpHost host = new HttpHost(uri.getHost(), uri.getPort());
        BasicScheme basicScheme = new BasicScheme();
        authCache.put(host,basicScheme);
        
        try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build()) {
            HttpClientContext httpClientContext = HttpClientContext.create();
            httpClientContext.setAuthCache(authCache);

            CloseableHttpResponse response = httpClient.execute(host, httpRequest, httpClientContext);
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        }
    }

    /**
     * 获取所有的job信息
     *
     * @author kewei
     * @since 2023/02/03
     */
    public    JSONArray getJobs()  {
        String JENKINS_URL =BASE_URL+SUFFIX;
        HttpPost httpPost = new HttpPost(JENKINS_URL);
        String res = null;
        try {
            res = customHttpMsg(JENKINS_URL, httpPost);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JSONObject parse = (JSONObject) JSONObject.parse(res);
        return parse.getJSONArray("jobs");
    }

    /**
     * 获取job的构建信息
     *
     * @param job job信息
     * @author kewei
     * @since 2023/02/03
     */
    public   JSONArray getNumbers(String job) {
        String JENKINS_URL =BASE_URL+JOB+job+SUFFIX;
        HttpPost httpPost = new HttpPost(JENKINS_URL);
        String res = null;
        try {
            res = customHttpMsg(JENKINS_URL, httpPost);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JSONObject parse = (JSONObject) JSONObject.parse(res);
        return parse.getJSONArray("builds");
    }

    /**
     * 获取构建信息
     *
     * @param job   工作
     * @param build 构建
     * @author kewei
     * @since 2023/02/03
     */
    public   JSONObject getBuild(String job,String build)  {
        String JENKINS_URL =BASE_URL+JOB+job+"/"+build+SUFFIX;
        HttpPost httpPost = new HttpPost(JENKINS_URL);
        String res = null;
        try {
            res = customHttpMsg(JENKINS_URL, httpPost);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  (JSONObject) JSONObject.parse(res);
    }


}