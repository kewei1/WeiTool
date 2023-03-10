package com.github.kewei1.pachong;

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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class zit {

    //ๆฅๅฟ
    private static final cn.hutool.log.Log log = cn.hutool.log.LogFactory.get();

    @Test
    public void test() throws Exception {
//        https://zwmst.com/1671.html

        final CountDownLatch countDownLatch = new CountDownLatch(20000);
        //20000
        for (int i = 0; i < 20000; i++) {
            int finalI = i;
            FutureUtil.doRrnnable(() -> {
                extracted(finalI + "");
                countDownLatch.countDown();
                System.out.println("๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐");
                System.out.println("๐๐๐๐๐ๅฎๆ" + finalI+"๐๐๐๐๐");
                System.out.println("๐๐๐๐๐ๅฉไฝ" + countDownLatch.getCount()+"๐๐๐๐๐");
                System.out.println("๐๐๐๐๐ๆๅ" + array.size()+"๐๐๐๐๐");
                System.out.println("๐๐๐๐๐ๅคฑ่ดฅ" + list2.size()+"๐๐๐๐๐");
                System.out.println("๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐๐ \r\n\r\n\r\n\r\n");
            });
        }

        countDownLatch.await();
        log.info("ไปปๅกๅฎๆ ็ฌๅฐ{}ๆก่ฎฐๅฝ" + array.size());


        //ๅฐๆฐๆฎๅๅฅๆไปถ
        cn.hutool.core.io.FileUtil.writeUtf8String(array.toJSONString(), "D:\\zwmst.json");

        //ๅฐๆฐๆฎ่ฏปๅบๆฅ
        String s = cn.hutool.core.io.FileUtil.readUtf8String("D:\\zwmst.json");


        //ๅฐๆฐๆฎๅๅฅๆไปถ list2
        cn.hutool.core.io.FileUtil.writeUtf8String(list2.toString(), "D:\\zwmst2.json");

        //ๅฐๆฐๆฎ่ฏปๅบๆฅ
        String s2 = cn.hutool.core.io.FileUtil.readUtf8String("D:\\zwmst2.json");

        JSONArray array2 = JSONArray.parseArray(s2);

        array2.forEach(e -> {
            extracted(e.toString());
        });

        //ๅฐๆฐๆฎๅๅฅๆไปถ
        cn.hutool.core.io.FileUtil.writeUtf8String(array.toJSONString(), "D:\\zwmst2.json");

    }


    static JSONArray array = new JSONArray();

    //ๆๅ็url
    static List<String> list  = new ArrayList();

    //ๅคฑ่ดฅ็url
    static List<String> list2  = new ArrayList();

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

        object.put("url", "https://zwmst.com/"+URL+".html");

        docDesc.getElementsByClass("entry-content u-text-format u-clearfix").forEach(e -> {
            object.put("content", e.text());
        });

        if (object.containsKey("title")&&object.containsKey("category")&&object.containsKey("date")&&object.containsKey("content")) {
            array.add(object);
        }else {
            list2.add(URL);
        }


        return object;
    }


}
