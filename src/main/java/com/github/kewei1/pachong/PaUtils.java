package com.github.kewei1.pachong;

import okhttp3.OkHttpClient;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
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

public class PaUtils {

    private final static OkHttpClient client = new OkHttpClient();

    //cookie
    private static String COOKIES ="";

    //请求头
    private static Map<String,String> HEADER = new HashMap<>();



    //readTimeout
    private static int READTIMEOUT = 60000;
    //connectTimeout
    private static int CONNECTTIMEOUT = 60000;
    //writeTimeout
    private static int WRITETIMEOUT = 60000;

    static {

        try {
            Connection.Response baiduid = Jsoup.connect("https://www.baidu.com")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36")
                    .timeout(60000)
                    .cookie("BAIDUID", "E5E5B5B5B5B5B5B5B5B5B5B5B5B5B5B5:FG=1")
                    .header("", "")
                    .ignoreContentType(true)
                    .execute();
            //Document document = baiduid.parse();

            Document document = baiduid.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    //空构造方法
    private PaUtils() {
        //client
        client.newBuilder().connectTimeout(CONNECTTIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READTIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITETIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }


    //设置readTimeout
    public static PaUtils setREADTIMEOUT(int READTIMEOUT) {
        return new PaUtils(COOKIES,HEADER,READTIMEOUT,CONNECTTIMEOUT,WRITETIMEOUT);
    }
    //设置connectTimeout
    public static PaUtils setCONNECTTIMEOUT(int CONNECTTIMEOUT) {
        PaUtils.CONNECTTIMEOUT = CONNECTTIMEOUT;
        return new PaUtils(COOKIES,HEADER,READTIMEOUT,CONNECTTIMEOUT,WRITETIMEOUT);
    }
    //设置writeTimeout
    public static PaUtils setWRITETIMEOUT(int WRITETIMEOUT) {
        PaUtils.WRITETIMEOUT = WRITETIMEOUT;
        return new PaUtils(COOKIES,HEADER,READTIMEOUT,CONNECTTIMEOUT,WRITETIMEOUT);
    }
    //设置cookie
    public static PaUtils setCOOKIES(String COOKIES) {
        PaUtils.COOKIES = COOKIES;
        return new PaUtils(COOKIES,HEADER,READTIMEOUT,CONNECTTIMEOUT,WRITETIMEOUT);
    }
    //设置请求头
    public static PaUtils setHEADER(Map<String,String> HEADER) {
        PaUtils.HEADER = HEADER;
        return new PaUtils(COOKIES,HEADER,READTIMEOUT,CONNECTTIMEOUT,WRITETIMEOUT);
    }








    //构造方法
    private PaUtils(String cookies, Map<String,String> header, int readTimeout, int connectTimeout, int writeTimeout) {
        this.COOKIES = cookies;
        this.HEADER = header;
        this.READTIMEOUT = readTimeout;
        this.CONNECTTIMEOUT = connectTimeout;
        this.WRITETIMEOUT = writeTimeout;

        //判断 COOKIES 是否为空
        if(!COOKIES.equals("")){
            client.interceptors().add(chain -> {
                okhttp3.Request request = chain.request().newBuilder().addHeader("Cookie", COOKIES).build();
                return chain.proceed(request);
            });
        }

        HEADER.forEach((k,v)->{
            client.interceptors().add(chain -> {
                okhttp3.Request request = chain.request().newBuilder().addHeader(k, v).build();
                return chain.proceed(request);
            });
        });

        client.newBuilder().connectTimeout(CONNECTTIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READTIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITETIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }


    //getPaUtils
    public static PaUtils getPaUtils(){
        return new PaUtils();
    }



    /**
     * 获取H5网页  Document
     *
     * @param url url
     * @author kewei
     * @since 2023/02/26
     */
    public  Document getH5Document(String url){
        //爬取网页
        String htmlStr = null;
        try {
            htmlStr = client.newCall(new okhttp3.Request.Builder().url(url).build()).execute().body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //解析网页
        Document docDesc = Jsoup.parse(htmlStr);

        return docDesc;

    }




    //获取网页的标题
    public  String getH5Title(String url){
        Document h5Document = getH5Document(url);
        String title = h5Document.title();
        return title;
    }

    //获取网页的内容
    public  String getH5Content(String url){
        Document h5Document = getH5Document(url);
        String content = h5Document.body().text();
        return content;
    }

    //获取网页的内容
    public  String getH5Content(String url,String cssQuery){
        Document h5Document = getH5Document(url);
        String content = h5Document.select(cssQuery).text();
        return content;
    }

    //获取网页 所有的 css选择器
    public  Elements getH5AllCssQuery(String url){
        Document h5Document = getH5Document(url);
        return h5Document.getAllElements();
    }


    //all[]	提供对文档中所有 HTML 元素的访问。
    //anchors[]	返回对文档中所有 Anchor 对象的引用。
    //applets	返回对文档中所有 Applet 对象的引用。
    //forms[]	返回对文档中所有 Form 对象引用。
    //images[]	返回对文档中所有 Image 对象引用。
    //links[]	返回对文档中所有 Area 和 Link 对象引用。


    //所有 HTML 元素
    public  Elements getH5All(String url){
        Document h5Document = getH5Document(url);
        return h5Document.getAllElements();
    }

    //所有 Anchor 对象
    public  Elements getH5AllAnchor(String url){
        Document h5Document = getH5Document(url);
        Elements allElements = h5Document.getAllElements();
        //过滤出  所有 Anchor 对象
        Elements anchors = allElements.stream().filter(e->e.tagName().equals("a")).collect(Collectors.toCollection(Elements::new));

        return anchors;
    }

    //所有 Applet 对象
    public  Elements getH5AllApplet(String url){
        Document h5Document = getH5Document(url);
        Elements allElements = h5Document.getAllElements();
        //过滤出  所有 Applet 对象
        Elements applets = allElements.stream().filter(e->e.tagName().equals("applet")).collect(Collectors.toCollection(Elements::new));

        return applets;
    }

    //所有 Form 对象
    public  Elements getH5AllForm(String url){
        Document h5Document = getH5Document(url);
        Elements allElements = h5Document.getAllElements();
        //过滤出  所有 Form 对象
        Elements forms = allElements.stream().filter(e->e.tagName().equals("form")).collect(Collectors.toCollection(Elements::new));

        return forms;
    }

    //所有 Image 对象
    public  Elements getH5AllImage(String url){
        Document h5Document = getH5Document(url);
        Elements allElements = h5Document.getAllElements();
        //过滤出  所有 Image 对象
        Elements images = allElements.stream().filter(e->e.tagName().equals("img")).collect(Collectors.toCollection(Elements::new));

        return images;
    }

    //所有 Area 和 Link 对象
    public  Elements getH5AllAreaAndLink(String url){
        Document h5Document = getH5Document(url);
        Elements allElements = h5Document.getAllElements();
        //过滤出  所有 Area 和 Link 对象
        Elements links = allElements.stream().filter(e->e.tagName().equals("a") || e.tagName().equals("link")).collect(Collectors.toCollection(Elements::new));

        return links;
    }


    //body  TODO
    //
    //提供对 <body> 元素的直接访问。
    //
    //对于定义了框架集的文档，该属性引用最外层的 <frameset>。
    //cookie	设置或返回与当前文档有关的所有 cookie。
    //domain	返回当前文档的域名。
    //lastModified	返回文档被最后修改的日期和时间。
    //referrer	返回载入当前文档的文档的 URL。
    //title	返回当前文档的标题。
    //URL	返回当前文档的 URL。
    //————————————————



    //拿到 Document 中所有的  Attribute
    public  List<Attributes> getH5AttributeAllTag(Document document){
        List<Attributes> attributes = new ArrayList();
        document.getAllElements().forEach(e->{
            attributes.add(e.attributes());
        });
        return attributes;
    }

    // 拿到 Elements 中所有的  Attribute
    public  List<Attributes> getH5ElementsAllTag(Element elements){
        List<Attributes> attributes = new ArrayList();

        elements.getAllElements().forEach(e->{
            attributes.add(e.attributes());
        });
        return attributes;
    }



































    //获取 Document中所有的 Attribute
    public  List<Attributes> getH5AllTag(Elements elements){
        List<Attributes> attributes = new ArrayList();
        elements.forEach(e->{
            e.getAllElements().forEach(f->{
                attributes.add(f.attributes());
            });
        });
        return attributes;
    }

    public  List<Attributes> getH5AllTag(String url){
        Elements h5AllCssQuery = getH5AllCssQuery(url);
        return getH5AllTag(h5AllCssQuery);
    }


    //获取网页 所有的 class
    public  List<String> getH5AllClass(String url){
        List<String> classes = new ArrayList();
        List<Attributes> h5AllTag = getH5AllTag(url);
        h5AllTag.forEach(e->{
            classes.add(e.get("class"));
        });
        return classes;
    }

    //获取网页 所有的 id
    public  List<String> getH5AllId(String url){
        List<String> ids = new ArrayList();
        List<Attributes> h5AllTag = getH5AllTag(url);
        h5AllTag.forEach(e->{
            ids.add(e.get("id"));
        });
        return ids;
    }

    //获取网页 所有的 href
    public  List<String> getH5AllHref(String url){
        List<String> hrefs = new ArrayList();
        List<Attributes> h5AllTag = getH5AllTag(url);
        h5AllTag.forEach(e->{
            hrefs.add(e.get("href"));
        });
        return hrefs;
    }

    //获取网页 所有的 src
    public  List<String> getH5AllSrc(String url){
        List<String> srcs = new ArrayList();
        List<Attributes> h5AllTag = getH5AllTag(url);
        h5AllTag.forEach(e->{
            srcs.add(e.get("src"));
        });
        return srcs;
    }

    //获取网页 所有的 style
    public  List<String> getH5AllStyle(String url){
        List<String> styles = new ArrayList();
        List<Attributes> h5AllTag = getH5AllTag(url);
        h5AllTag.forEach(e->{
            styles.add(e.get("style"));
        });
        return styles;
    }

    //获取网页 所有的 alt
    public  List<String> getH5AllAlt(String url){
        List<String> alts = new ArrayList();
        List<Attributes> h5AllTag = getH5AllTag(url);
        h5AllTag.forEach(e->{
            alts.add(e.get("alt"));
        });
        return alts;
    }

    //Image  返回对文档中所有 Image 对象引用。
    public  List<String> getH5AllImg(String url){
        List<String> imgs = new ArrayList();
        List<Attributes> h5AllTag = getH5AllTag(url);
        h5AllTag.forEach(e->{
            imgs.add(e.get("img"));
        });
        return imgs;
    }

    //获取网页 所有的 a
    public  List<String> getH5AllA(String url){
        List<String> as = new ArrayList();
        List<Attributes> h5AllTag = getH5AllTag(url);
        h5AllTag.forEach(e->{
            as.add(e.get("a"));
        });
        return as;
    }

    //获取网页 所有的 input
    public  List<String> getH5AllInput(String url){
        List<String> inputs = new ArrayList();
        List<Attributes> h5AllTag = getH5AllTag(url);
        h5AllTag.forEach(e->{
            inputs.add(e.get("input"));
        });
        return inputs;
    }

    //去除重复的 List<String> h5AllClass
    public  List<String> removeDuplicate(List<String> list){
        Set<String> set = new HashSet<String>(list);
        List<String> newList = new ArrayList<String>(set);
        return newList;
    }

    //去除重复的 List<Attributes> attributes
    public  List<Attributes> removeDuplicateAttributes(List<Attributes> list){
        Set<Attributes> set = new HashSet<Attributes>(list);
        List<Attributes> newList = new ArrayList<Attributes>(set);
        return newList;
    }


    //获取 网页 所有的 div
    public  List<String> getH5AllDiv(String url,String attribute){
        List<String> divs = new ArrayList();
        List<Attributes> h5AllTag = getH5AllTag(url);
        h5AllTag.forEach(e->{
            divs.add(e.get(attribute));
        });
        return divs;
    }

    //将网页保存到本地
    public String savePage(String url,String path){

        Document h5Document = getH5Document(url);
        String htmlStr = h5Document.html();
        //获取html 文件名
        String fileName = getH5Title(url);
        //获取html 文件路径
        String filePath = path + fileName + ".html";
        //创建文件
        File file = new File(filePath);
        //创建文件夹
        File fileParent = file.getParentFile();
        if(!fileParent.exists()){
            fileParent.mkdirs();
        }


        //将htmlStr 中所有相对地址 换成 绝对地址
//        htmlStr = parseHtml(url);



        //写入文件
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(htmlStr);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return filePath;
    }


    //下载 url 中的所有图片
    public void downloadImg(String url, String path) {
        // 获取url中所有图片链接
        List<String> imgUrls = getH5AllImg(url);
        // 遍历每一个图片链接并下载
        for (String imgUrl : imgUrls) {
            String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
            String filePath = path + fileName;
            File file = new File(filePath);
            File fileParent = file.getParentFile();
            // 如果文件夹不存在，创建文件夹
            if (!fileParent.exists()) {
                boolean result = fileParent.mkdirs();
                if (!result) {
                    throw new RuntimeException("Failed to create directory: " + fileParent.getAbsolutePath());
                }
            }

            // 下载图片
            try {
                URL urlObject = new URL(imgUrl);
                URLConnection urlConnection = urlObject.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, length);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }



    //将htmlStr 中所有相对地址 换成 绝对地址
                public String parseHtml(String url){
                    Document h5Document = getH5Document(url);
                    String baseUrl = h5Document.baseUri();

                    Elements elements = h5Document.getAllElements();

                    elements.forEach(e -> {
                        e.getAllElements().forEach(f -> {
                            Attributes attributes = f.attributes();


                            attributes.forEach(k -> {

                                String key = k.getKey();
                                String value = k.getValue();

                                //src href

                                if (key.equals("href") || key.equals("src")) {
                                    if (!value.startsWith("http") || !value.startsWith("https")) {
                                        f.attr(key, value);
                                    }
                                }
                            });
                        });
                    });


                    return h5Document.html();
                }



}
