package com.github.kewei1;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.kewei1.thread.FutureUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class XiaolingCode {

    //https://xiaolincoding.com/

    private static String BASEURL ="https://xiaolincoding.com/";
    private static ArrayList BOOKS = new ArrayList<>();
    private static ArrayList PAGES = new ArrayList<>();

    private static ArrayList CONTEXT = new ArrayList<>();

    public static void main(String[] args) {
        String s = HttpUtil.get(BASEURL);
        Document docDesc = Jsoup.parse(s);

        Elements elementsByClass = docDesc.getElementsByClass("theme-default-content custom content__default");

        elementsByClass.forEach(e->{
            e.getElementsByTag("ul").forEach(f->{
                //<li></li>
                f.getElementsByTag("li").forEach(g->{
                    //<a></a>
                    g.getElementsByTag("a").forEach(h->{
                        JSONObject book = new JSONObject();
                        book.put("href",h.attr("href"));
                        book.put("text",h.text());
                        BOOKS.add(book);
                    });
                });
            });

        });


        BOOKS.forEach(e->{
            JSONObject book = (JSONObject) e;
            String href = book.getString("href");
            getPage(href);
        });






        System.out.println(PAGES);

        final CountDownLatch countDownLatch = new CountDownLatch(PAGES.size());

        PAGES.forEach(e->{
            JSONObject page = (JSONObject) e;

            FutureUtil.doRrnnable(()->{
                String href = page.getString("href");
                getContext(href);
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

        cn.hutool.core.io.FileUtil.writeUtf8String(PAGES.toString(), "D:\\PAGE.json");
        cn.hutool.core.io.FileUtil.writeUtf8String(CONTEXT.toString(), "D:\\CONTEXT.json");

        System.out.println("写入完成");


    }


    private static void getContext(String s){
        String s1 = HttpUtil.get(BASEURL + s);
        Document docDesc = Jsoup.parse(s1);

        docDesc.getElementsByClass("theme-default-content content__default").forEach(e->{
            e.getElementsByTag("h1").forEach(f->{
                JSONObject context = new JSONObject();
                context.put("title",f.text());
                context.put("href",s);
                context.put("page",s1);
                CONTEXT.add(context);
            });
        });



    }



    private static void getPage(String s){
        String s1 = HttpUtil.get(BASEURL + s);

        //s1 转成HTML 文件



        Document docDesc = Jsoup.parse(s1);

        Elements elementsByClass = docDesc.getElementsByClass("theme-default-content content__default");

        elementsByClass.forEach(e-> {
            //<ul></ul>
            e.getElementsByTag("ul").forEach(f->{
                //<li></li>
                f.getElementsByTag("li").forEach(g->{
                    //<a></a>
                    g.getElementsByTag("a").forEach(h->{
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
