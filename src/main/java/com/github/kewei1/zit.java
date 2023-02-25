package com.github.kewei1;

import cn.hutool.http.HttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class zit {

    private final static OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .build();


    static {



    }


    public static void main(String[] args) {

        Set<String> ids = new HashSet<>();
        //
        for (int i = 1; i < 15; i++) {
            Request request = new Request.Builder()
                    .addHeader("Cookie","PHPSESSID=ohmmsd71vd1vii07q8fm5729jg; wordpress_test_cookie=WP%20Cookie%20check; wordpress_logged_in_5d06bcc535360be7c314d6b0998af49a=kewei%7C1677370160%7Ch8O8TLc1UMMBn6U3WSdhi4HEjecqXZGIoNySIJ69L9q%7C57cb298ed06e2fe4a35706ff7828ec9225ef06212f505d8ea1718be13586d57c")
                    .url("https://www.217zy.com/page/"+i)
                    .build();


            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String str = null;
            try {
                str = response.body().string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Document docDesc = Jsoup.parse(str);

            docDesc.getAllElements().forEach(element -> {
                if (element.tagName().equals("a") && element.attr("href").contains("https://www.217zy.com/")) {
                    //正则匹配
                    String regex = "https://www.217zy.com/\\d+.html";
                    //https://www.217zy.com/2761.html
                    if (element.attr("href").matches(regex)) {
                        String href = element.attr("href");
                        //2761
                        String id = href.substring(href.lastIndexOf("/") + 1, href.lastIndexOf("."));
                        ids.add(id);
                    }


                }


            });
        }

        System.out.println(ids);

        ArrayList<String> urls = new ArrayList<>();

        ids.forEach(e->{
            //
            Request request = new Request.Builder()
                    .addHeader("Cookie","PHPSESSID=ohmmsd71vd1vii07q8fm5729jg; wordpress_test_cookie=WP%20Cookie%20check; wordpress_logged_in_5d06bcc535360be7c314d6b0998af49a=kewei%7C1677370160%7Ch8O8TLc1UMMBn6U3WSdhi4HEjecqXZGIoNySIJ69L9q%7C57cb298ed06e2fe4a35706ff7828ec9225ef06212f505d8ea1718be13586d57c")
                    .url("https://www.217zy.com/go?post_id="+e)
                    .build();

            try {
                Response execute = client.newCall(request).execute();
                String string = execute.body().string();
                System.out.println(string);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }







        });




    }



}
