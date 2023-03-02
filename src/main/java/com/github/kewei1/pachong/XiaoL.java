package com.github.kewei1.pachong;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.github.kewei1.thread.FutureUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class XiaoL {

    /**
     * https://xiaolincoding.com/
     */
    private static final String BASE_URL = "https://xiaolincoding.com/";

    /**
     * 书籍列表
     */
    private static final List<JSONObject> BOOKS = new ArrayList<>();
    /**
     * 页面列表
     */
    private static final List<JSONObject> PAGES = new ArrayList<>();
    /**
     * 内容列表
     */
    private static final List<JSONObject> CONTEXT = new ArrayList<>();

    public static void main(String[] args) {
        String html = HttpUtil.get(BASE_URL);
        Document docDesc = Jsoup.parse(html);
        Elements elementsByClass = docDesc.getElementsByClass("theme-default-content custom content__default");

        elementsByClass.forEach(e->{
            e.getElementsByTag("ul").forEach(f->{
                f.getElementsByTag("li").forEach(g->{
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
            JSONObject book = e;
            String href = book.getString("href");
            getPageV2(href);
        });

        System.out.println(PAGES);

        final CountDownLatch countDownLatch = new CountDownLatch(PAGES.size());

        PAGES.forEach(e->{
            JSONObject page = e;

            FutureUtil.doRrnnable(()->{
                String href = page.getString("href");
                getContextV2(href);
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


        FutureUtil.shutdown();

    }

    /**
     * 该方法用于获取指定网址的网页内容，并解析出指定类名下的所有标题标签。
     *
     * @param href 需要获取内容的网址
     *
     * @since 2023-02-26
     * @modified-by kewei
     * @modified-time 2023-02-26 14:30:00
     * @contact keweii@qq.com
     * @author kewei
     * @version 1.0
     *
     */
    private static void getContextV2(String href) {
        String html = HttpUtil.get(BASE_URL + href);
        Document docDesc = Jsoup.parse(html);
        docDesc.getElementsByClass("theme-default-content content__default").forEach(e->{
            e.getElementsByTag("h1").forEach(f->{
                JSONObject context = new JSONObject();
                context.put("title",f.text());
                context.put("href",href);
                context.put("page",html);
                CONTEXT.add(context);
            });
        });
    }

    /**
     * 该方法用于获取指定网址的网页内容，并解析出指定类名下的所有标题标签。
     *
     * @param href 需要获取内容的网址
     *
     * @since 2023-02-26
     * @modified-by kewei
     * @modified-time 2023-02-26 14:30:00
     * @contact keweii@qq.com
     * @author kewei
     * @version 1.0
     */
    private static void getPageV2(String href) {
        String html = HttpUtil.get(BASE_URL + href);
        Document docDesc = Jsoup.parse(html);
        Elements elementsByClass = docDesc.getElementsByClass("theme-default-content content__default");

        elementsByClass.forEach(e-> {
            e.getElementsByTag("ul").forEach(f->{
                f.getElementsByTag("li").forEach(g->{
                    g.getElementsByTag("a").forEach(h->{
                        JSONObject page = new JSONObject();
                        page.put("href",h.attr("href"));
                        page.put("text",h.text());
                        PAGES.add(page);
                    });
                });
            });
        });
    }
}

