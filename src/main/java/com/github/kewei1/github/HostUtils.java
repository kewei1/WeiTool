package com.github.kewei1.github;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.kewei1.thread.FutureUtil;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class HostUtils {

    private static Set<String>  HOSTS =  new HashSet<>();
    private static StringBuilder content = new StringBuilder();
    private static final  String REGEX_CHINESE = "[\u4e00-\u9fa5]";

    static {
        HOSTS.add("github.com");
        HOSTS.add("github.global.ssl.fastly.net");
        HOSTS.add("github-cloud.s3.amazonaws.com");
        HOSTS.add("github-production-release-asset-2e65be.s3.amazonaws.com");
        HOSTS.add("github-production-user-asset-6210df.s3.amazonaws.com");
        HOSTS.add("github-production-repository-file-5c1aeb.s3.amazonaws.com");
        HOSTS.add("githubstatus.com");
        HOSTS.add("avatars0.githubusercontent.com");
        HOSTS.add("avatars1.githubusercontent.com");
        HOSTS.add("avatars2.githubusercontent.com");
        HOSTS.add("avatars3.githubusercontent.com");
        HOSTS.add("avatars4.githubusercontent.com");
        HOSTS.add("avatars5.githubusercontent.com");
        HOSTS.add("avatars6.githubusercontent.com");
        HOSTS.add("avatars7.githubusercontent.com");
        HOSTS.add("avatars8.githubusercontent.com");
        HOSTS.add("camo.githubusercontent.com");
        HOSTS.add("raw.githubusercontent.com");
        HOSTS.add("favicons.githubusercontent.com");
        HOSTS.add("avatars.githubusercontent.com");
        HOSTS.add("github.map.fastly.net");
        HOSTS.add("github.global.ssl.fastly.net");
        HOSTS.add("api.github.com");
        HOSTS.add("assets-cdn.github.com");
        HOSTS.add("gist.githubusercontent.com");
        HOSTS.add("cloud.githubusercontent.com");
        HOSTS.add("avatars.githubusercontent.com");
        HOSTS.add("user-images.githubusercontent.com");
        HOSTS.add("favicons.githubusercontent.com");
        HOSTS.add("desktop.githubusercontent.com");
        HOSTS.add("repository-images.githubusercontent.com");
        HOSTS.add("marketplace-screenshots.githubusercontent.com");
        HOSTS.add("raw.github.com");
        HOSTS.add("www.github.com");


        File file = new File("C:\\Windows\\System32\\drivers\\etc\\hosts");
        Stream<String> of1 = StreamUtil.of(file, CharsetUtil.CHARSET_UTF_8);


        of1.forEachOrdered(e -> {
            if (null==e || e.equals("") || !e.contains("github")) {
                content.append(e).append("\n");
            }
        });


        HOSTS.stream().forEach(e -> {
//            FutureUtil.synchronizeExecute(()->{
                String ip = getIP(e);
                System.out.println(ip);
                if (!StrUtil.isBlankIfStr(ip)) {
                    content.append(ip).append(" ").append(e).append("\n");
                }
//            });
        });
        System.out.println(content.toString());
//        FutureUtil.shutdown();
    }

    public HostUtils() {
        System.out.println(content);
    }



    public static void gitHubHost(){
        File file = new File("C:\\Windows\\System32\\drivers\\etc\\hosts");
        //write
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), CharsetUtil.CHARSET_UTF_8));
            writer.write(content.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //flushdns
        try {
            Runtime.getRuntime().exec("ipconfig /flushdns");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getIP(String domain){
        String ip = "";
        try {
            ip = java.net.InetAddress.getByName(domain).getHostAddress();
        } catch (Exception e) {
        }
        return ip;
    }

    public static String getIpformMyssl(String domain){
        String url = "https://myssl.com/api/v1/tools/dns_query?qtype=1&qmode=-1&host="+domain;
        String result = HttpUtil.get(url);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject data = jsonObject.getJSONObject("data");

        List<JSONObject> list = new ArrayList<>();

        data.keySet().forEach(e -> {
            data.getJSONArray(e).stream().forEach(e1 -> {
                JSONObject jsonObject1 = (JSONObject) e1;
                list.add(jsonObject1.getJSONObject("answer"));
            });
        });


        JSONObject timeConsume = list.stream().min(Comparator.comparing(e -> e.getBigDecimal("time_consume"))).get();




        return JSONObject.parseObject( timeConsume.getJSONArray("records").get(0).toString()).getString("value");

    }

    public static Integer getTTL(String ip){

        return 0;
    }


    private static String pingTest(String ip) {


        String pingResult = "";
        String pingCmd = "ping -c 3 " + ip;

        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);
            BufferedReader in = new BufferedReader(new
            InputStreamReader(p.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                pingResult += inputLine;
            }
            in.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return pingResult;
    }













    public static void main(String[] args) {
        System.out.println();
        System.out.println(pingTest("192.168.1.1"));
    }




    public static void update(){

    }

}
