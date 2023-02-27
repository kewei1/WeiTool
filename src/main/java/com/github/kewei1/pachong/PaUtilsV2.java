package com.github.kewei1.pachong;

import cn.hutool.crypto.digest.MD5;
import okhttp3.OkHttpClient;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PaUtilsV2 {

    /**
     *  url
     *
     * @since 2023/02/27
     */
    private static  String URL = "";

    /**
     *  浏览器标识
     * @since 2023/02/27
     */
    private static  String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";

    /**
     *  连接超时时间
     * @since 2023/02/27
     */
    private static  int CONNECTTIMEOUT = 60000;

    /**
     *  data 数据
     * @since 2023/02/27
     */
    private static  Map<String, String> DATA = new HashMap<>();

    /**
     *  cookies 数据
     * @since 2023/02/27
     */
    private static  Map<String, String> COOKIES = new HashMap<>();

    /**
     *  headers 数据
     * @since 2023/02/27
     */
    private static  Map<String, String> HEADERS = new HashMap<>();

    /**
     *  忽略内容类型
     * @since 2023/02/27
     */
    private static  boolean IGNORECONTENTTYPE = true;



    //构造器私有化
    private PaUtilsV2() {
    }

    private PaUtilsV2(String userAgent, int connectTimeout, Map<String, String> data, Map<String, String> cookies, Map<String, String> headers, boolean ignoreContentType) {
        USER_AGENT = userAgent;
        CONNECTTIMEOUT = connectTimeout;
        DATA = data;
        COOKIES = cookies;
        HEADERS = headers;
        IGNORECONTENTTYPE = ignoreContentType;
    }

    /**
     *  获取实例
     * @since 2023/02/27
     */
    public static PaUtilsV2 getPaUtils() {
        return new PaUtilsV2();
    }

    /**
     *  获取实例
     * @since 2023/02/27
     */
    public static PaUtilsV2 getPaUtils(String userAgent, int connectTimeout, Map<String, String> data, Map<String, String> cookies, Map<String, String> headers, boolean ignoreContentType) {
        return new PaUtilsV2(userAgent, connectTimeout, data, cookies, headers, ignoreContentType);
    }





    //更改浏览器标识
    public PaUtilsV2 setUSER_AGENT(String userAgent) {
        USER_AGENT = userAgent;
        return new PaUtilsV2(USER_AGENT, CONNECTTIMEOUT, DATA, COOKIES, HEADERS, IGNORECONTENTTYPE);
    }

    //更改连接超时时间
    public PaUtilsV2 setCONNECTTIMEOUT(int connectTimeout) {
        CONNECTTIMEOUT = connectTimeout;
        return new PaUtilsV2(USER_AGENT, CONNECTTIMEOUT, DATA, COOKIES, HEADERS, IGNORECONTENTTYPE);
    }

    //更改 data 数据
    public PaUtilsV2 setDATA(Map<String, String> data) {
        DATA = data;
        return new PaUtilsV2(USER_AGENT, CONNECTTIMEOUT, DATA, COOKIES, HEADERS, IGNORECONTENTTYPE);
    }

    //更改 cookies 数据
    public PaUtilsV2 setCOOKIES(Map<String, String> cookies) {
        COOKIES = cookies;
        return new PaUtilsV2(USER_AGENT, CONNECTTIMEOUT, DATA, COOKIES, HEADERS, IGNORECONTENTTYPE);
    }

    //更改 headers 数据
    public PaUtilsV2 setHEADERS(Map<String, String> headers) {
        HEADERS = headers;
        return new PaUtilsV2(USER_AGENT, CONNECTTIMEOUT, DATA, COOKIES, HEADERS, IGNORECONTENTTYPE);
    }

    //更改 忽略内容类型
    public PaUtilsV2 setIGNORECONTENTTYPE(boolean ignoreContentType) {
        IGNORECONTENTTYPE = ignoreContentType;
        return new PaUtilsV2(USER_AGENT, CONNECTTIMEOUT, DATA, COOKIES, HEADERS, IGNORECONTENTTYPE);
    }



    //获取 Connection.Response
    public Connection.Response getResponse(String url) throws IOException {

        Validate.notNull(url, "url 不能为空");
        Validate.isTrue(url.startsWith("http") || url.startsWith("https"), "url 必须以 http 或 https 开头");


        Connection.Response execute = Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .timeout(CONNECTTIMEOUT)
                .data(DATA)
                .cookies(COOKIES)
                .headers(HEADERS)
                .ignoreContentType(IGNORECONTENTTYPE)
                .execute();

        return execute;
    }

    //获取 Document
    public Document getDocument(String url) throws IOException {

        Validate.notNull(url, "url 不能为空");
        Validate.isTrue(url.startsWith("http") || url.startsWith("https"), "url 必须以 http 或 https 开头");

        Document document = Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .timeout(CONNECTTIMEOUT)
                .data(DATA)
                .cookies(COOKIES)
                .headers(HEADERS)
                .ignoreContentType(IGNORECONTENTTYPE)
                .get();



        return document;
    }




    //获取 Document 所有 DOM 元素对象

    public static Elements getElements(Document document) throws IOException {

        Validate.notNull(document, "document 不能为空");
        //判断 document 是否  Document 对象
        Validate.isTrue(document instanceof Document, "document 必须是 Document 对象");

        Elements elements = document.getAllElements();

        return elements;
    }

    //获取 Element 中 所有DOM 属性 对象
    public static Attributes getAttributes(Element element) throws IOException {

        Validate.notNull(element, "element 不能为空");
        //判断 element 是否  Element 对象
        Validate.isTrue(element instanceof Element, "element 必须是 Element 对象");

        Attributes attributes = element.attributes();

        return attributes;
    }

    //获取 Document 中 所有的 图片
    public static Elements getImages(Document document) throws IOException {

        Validate.notNull(document, "document 不能为空");
        //判断 document 是否  Document 对象
        Validate.isTrue(document instanceof Document, "document 必须是 Document 对象");

        Elements images = document.select("img[src~=(?i)\\.(png|jpe?g|gif)]");

        return images;
    }


    //获取 Document 中 所有的 图片 链接
    public static List<String> getImagesUrl(Document document) throws IOException {

        Validate.notNull(document, "document 不能为空");
        //判断 document 是否  Document 对象
        Validate.isTrue(document instanceof Document, "document 必须是 Document 对象");

        Elements images = document.select("img[src~=(?i)\\.(png|jpe?g|gif)]");

        List<String> list = new ArrayList<>();

        for (Element image : images) {
            list.add(image.attr("src"));
        }

        //去重
        list = list.stream().distinct().collect(Collectors.toList());

         list = extracted(document, list);


        return list;
    }

    private static List<String> extracted(Document document, List<String> list) {

        //去除"" 项
        list.remove("");


        //如果存在 //www 则替换为 https://www
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).startsWith("//www")) {
                list.set(i, list.get(i).replace("//www", "https://www"));
            }
        }
        //如果存在 //https 则替换为 https
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).startsWith("//https")) {
                list.set(i, list.get(i).replace("//https", "https"));
            }
        }

        //如果存在 //http 则替换为 http
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).startsWith("//http")) {
                list.set(i, list.get(i).replace("//http", "http"));
            }
        }

        //如果是相对地址 则拼接 成 绝对地址
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).startsWith("/")) {
                //https://xiaolincoding.com/images/system/download.png 转换成 https://xiaolincoding.com 去除.com 后面的内容
                String baseUrl = document.baseUri().substring(0, document.baseUri().indexOf("."));
                baseUrl =baseUrl + document.baseUri().replace(baseUrl,"").substring(0, document.baseUri().indexOf("/")-1);
                list.set(i, baseUrl);
            }
        }

        return list;
    }


    //下载 所有 链接的 内容
    public static void downloadAllLinksContent(List<String> urls, String path ,String prefix) throws IOException {

            Validate.notNull(urls, "urls 不能为空");
            Validate.notNull(path, "path 不能为空");

            //判断 urls https http
            for (String s : urls) {
                Validate.isTrue(s.startsWith("http") || s.startsWith("https"), "url 必须以 http 或 https 开头");
            }

            //判断 path 是否存在
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            for (String s : urls) {
                downloadLinkContent(s, path,prefix);
            }
    }

    //downloadLinkContent
    public static void downloadLinkContent(String url, String path,String prefix)  {
        try {
            URL url1 = new URL(url);
            URLConnection urlConnection = url1.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            //如果 prefix 为null 则不加前缀
            if (prefix == null) {
                prefix = "";
            }

            FileOutputStream fileOutputStream = new FileOutputStream(path + prefix+"_" +fileName);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, len);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }


    }









    //获取 Document 中 所有的 链接
    public static Elements getLinks(Document document) throws IOException {

        Validate.notNull(document, "document 不能为空");
        //判断 document 是否  Document 对象
        Validate.isTrue(document instanceof Document, "document 必须是 Document 对象");

        Elements links = document.select("a[href]");

        return links;
    }

    //获取 Document 中 所有的 链接 URL
    public static List<String> getLinksUrl(Document document) throws IOException {

        Validate.notNull(document, "document 不能为空");
        //判断 document 是否  Document 对象
        Validate.isTrue(document instanceof Document, "document 必须是 Document 对象");

        Elements links = document.select("a[href]");

        List<String> list = new ArrayList<>();

        for (Element link : links) {
            list.add(link.attr("abs:href"));
        }

        //去重
        list = list.stream().distinct().collect(Collectors.toList());

        list = extracted(document, list);

        return list;
    }


    //获取 Document 中 所有的 资源链接 URL
    public static List<String> getSourcesUrl(Document document) throws IOException {

        Validate.notNull(document, "document 不能为空");
        //判断 document 是否  Document 对象
        Validate.isTrue(document instanceof Document, "document 必须是 Document 对象");

        //获取所有的 link 和 图片
        Elements links = document.select("link[href],img[src~=(?i)\\.(png|jpe?g|gif)]");

        List<String> list = new ArrayList<>();

        for (Element link : links) {
            list.add(link.attr("abs:href"));
        }

        //去重
        list = list.stream().distinct().collect(Collectors.toList());

        list = extracted(document, list);

        return list;
    }

















}
