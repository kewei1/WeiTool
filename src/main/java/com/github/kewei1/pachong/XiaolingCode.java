package com.github.kewei1.pachong;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.kewei1.pachong.PaUtils;
import com.github.kewei1.thread.FutureUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class XiaolingCode {

    //https://xiaolincoding.com/

    private static String BASEURL ="https://xiaolincoding.com/";
    private static ArrayList BOOKS = new ArrayList<>();
    private static ArrayList PAGES = new ArrayList<>();

    private static ArrayList CONTEXT = new ArrayList<>();

    public static void main(String[] args)  {


        Document document = PaUtils.getPaUtils().getDocument(BASEURL);

        document.select("div.theme-default-content.custom.content__default").forEach(e->{
            e.select("ul").forEach(f->{
                f.select("li").forEach(g->{
                    g.select("a").forEach(h->{
                        JSONObject book = new JSONObject();
                        book.put("href",h.attr("href"));
                        book.put("text",h.text());
                        if (!h.attr("href").contains("http")){
                            BOOKS.add(book);
                        }
                    });
                });
            });
        });

        BOOKS.forEach(e->{
            JSONObject book = (JSONObject) e;
            String href = book.getString("href");
            getPage(href);
        });



        final CountDownLatch countDownLatch = new CountDownLatch(PAGES.size());

        PAGES.forEach(e->{
            JSONObject page = (JSONObject) e;

            FutureUtil.doRrnnable(()->{
                getContext(page.getString("href"));
                countDownLatch.countDown();
            });
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        System.out.println("end");
        System.out.println("爬取完成"+PAGES.size()+"条数据");

        cn.hutool.core.io.FileUtil.writeUtf8String(PAGES.toString(), "D:\\PAGE1.json");
        cn.hutool.core.io.FileUtil.writeUtf8String(CONTEXT.toString(), "D:\\CONTEXT1.json");

        System.out.println("写入完成");


    }


    private static void getContext(String s){
        PaUtils.getPaUtils().getDocument(BASEURL + s).select("div.theme-default-content.content__default").forEach(e->{
            e.select("h1").forEach(f->{
                JSONObject context = new JSONObject();
                context.put("title",f.text());
                context.put("href",s);
                CONTEXT.add(context);
            });
        });
    }



    private static void getPage(String s){
        PaUtils.getPaUtils().getDocument(BASEURL + s).select("div.theme-default-content.custom.content__default").forEach(e->{
            e.select("ul").forEach(f->{
                f.select("li").forEach(g->{
                    g.select("a").forEach(h->{
                        JSONObject book = new JSONObject();
                        book.put("href",h.attr("href"));
                        book.put("text",h.text());
                        PAGES.add(book);
                    });
                });
            });
        });
    }







}
