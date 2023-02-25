package com.github.kewei1;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.kewei1.thread.FutureUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class zit {

    //日志
    private static final cn.hutool.log.Log log = cn.hutool.log.LogFactory.get();

    private final static OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .build();


    static {



    }









    @Test
    public void test() throws Exception {
//        https://zwmst.com/1671.html

        JSONArray array = new JSONArray();
        //10000
        for (int i = 0; i < 100000; i++) {
            int finalI = i;
            Object o = FutureUtil.doCallable(() -> {
                return extracted(finalI + "");
            }).get();

            JSONObject object = JSONObject.parseObject(o.toString());

            if (object.containsKey("title")&&object.containsKey("category")&&object.containsKey("date")&&object.containsKey("content")) {
                array.add(object);
            }
        }

        System.out.println(array.size());

        //将数据写入文件
        cn.hutool.core.io.FileUtil.writeUtf8String(array.toJSONString(), "D:\\zwmst.json");

        //将数据读出来
        String s = cn.hutool.core.io.FileUtil.readUtf8String("D:\\zwmst.json");

    }




    private static JSONObject extracted(String URL) {
        JSONObject object = new JSONObject();
        String s = HttpUtil.get("https://zwmst.com/"+URL+".html");
        Document docDesc = Jsoup.parse(s);
        docDesc.getElementsByClass("entry-title").forEach(e -> {
            if (e.tagName().equals("h1")) {
                object.put("title", e.text());
            }
        });
        docDesc.getElementsByClass("meta-category").forEach(e -> {
            object.put("category", e.text());
        });
        docDesc.getElementsByClass("meta-date").forEach(e -> {
            object.put("date", e.text());
        });

        docDesc.getElementsByClass("entry-content u-text-format u-clearfix").forEach(e -> {
            object.put("content", e.text());
        });

        if (object.containsKey("title")&&object.containsKey("category")&&object.containsKey("date")&&object.containsKey("content")) {
            System.out.println(StrUtil.format(   "爬取:{}成功", "https://zwmst.com/"+URL+".html"));
            System.out.println(StrUtil.format( "爬取{}页成功", URL));
        }else {
            System.out.println(StrUtil.format( "爬取:{}失败", "https://zwmst.com/"+URL+".html"));
            System.out.println(StrUtil.format( "爬取失败 此{}页不存在数据", URL));

            System.out.println();
        }


        return object;
    }


}
