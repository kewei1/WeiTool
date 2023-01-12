package com.github.kewei1.github;

import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class GitHubHost {

    private static Set<String>  HOSTS =  new HashSet<>();
    private static StringBuilder content = new StringBuilder();
    private static int count = 0;
    private static int overtimePing = 0;
    private static int successPing = 0;
    private static int successCount = 0;
    private static int pingCount = 0;
    private static int queryCount = 0;

    private static  Long speeed = 0L;

    public static final String MYSSL = "https://myssl.com/api/v1/tools/dns_query?qtype=1&qmode=-1&host=";

    public static final String ADDRESSV4 = "https://www.ipaddress.com/site/";
    private static final String ADDRESSV6 = "https://www.ipaddress.com/site/";


    private static  String DNS_TYPE = "";

    private final static String IPV4 = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
    private final static String IPV6 ="((?:[\\da-fA-F]{0,4}:[\\da-fA-F]{0,4}){2,7})(?:[\\/\\\\%](\\d{1,3}))?";

    private final static void  init() {
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

        content.append("## 非github DNS").append("\n");
        of1.forEachOrdered(e -> {
            if (null!=e && !e.equals("") && e.length()>0 && !e.contains("github") &&  !e.startsWith("#")){
                content.append(e).append("\n");
                count++;
            }
        });

        content.append("\n\n\n\n## github DNS").append("\n");
        HOSTS.stream().forEach(e -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String ip ="";

                    if (DNS_TYPE.equals(MYSSL)){
                        ip = getIpforMyssl(e);
                    }
                    if (DNS_TYPE.equals(ADDRESSV4)){
                        ip = getIpforIpaddressV4(e);
                    }
//                    if (DNS_TYPE.equals(ADDRESSV6)){
//                        getIpforIpaddressV6(e);
//                    }

                    count++;
                    if (!StrUtil.isBlankIfStr(ip)) {
                        if (!ip.contains("## 连接耗时99999")) {
                            content.append(ip).append(" ").append(e).append("\n");
                            successCount++;
                        }
                    }

                    if (count == HOSTS.size()) {
                        writeHost();
                        System.out.println(content.toString());
                        System.out.println(StrUtil.format("获取{}次DNS记录",queryCount));
                        System.out.println(StrUtil.format("测速{}次",pingCount));
                        System.out.println(StrUtil.format("成功{}次",successPing));
                        System.out.println(StrUtil.format("超时{}次",overtimePing));

                        System.out.println(StrUtil.format("配置成功{}条DNS记录\n",successCount));

                        System.out.println(StrUtil.format("执行耗时{}秒",(System.currentTimeMillis() -speeed)/1000));
                        System.out.println("配置完成");
                        System.out.println("请重启浏览器");
                    }
                }
            },e).start();
        });

    }

    public static void config(String dnsType) {
        DNS_TYPE = dnsType;
        speeed = System.currentTimeMillis();
        init();
    }


    private final static void writeHost(){

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
            InetAddress[] ips = java.net.InetAddress.getAllByName(domain);
            ip = ips[0].getHostAddress();
        } catch (Exception e) {
        }
        return ip;
    }

    private final static String getIpforMyssl(String domain){
        String url = MYSSL+domain;
        queryCount++;
        System.out.println(StrUtil.format("正在 查询{}DNS \n",domain));
        String result = HttpUtil.get(url);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject data = jsonObject.getJSONObject("data");

        if (null == data){
            System.out.println(StrUtil.format("查询{}DNS 失败 \n",domain));
           //暂停 5秒
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getIpforMyssl(domain);
        }

        System.out.println(StrUtil.format("查询{}DNS 成功 \n",domain));


        List<JSONObject> list = new ArrayList<>();

        data.keySet().forEach(e -> {
            data.getJSONArray(e).stream().forEach(e1 -> {
                JSONObject jsonObject1 = (JSONObject) e1;
                JSONArray jsonArray = jsonObject1.getJSONObject("answer").getJSONArray("records");
                if (null!=jsonArray && jsonArray.size()>0) {
                    jsonArray.stream().forEach(f->{
                        JSONObject record = (JSONObject) f;
                        String ip = record.getString("value");
                        record.put("speed",getSpeed(ip));
                        list.add(record);
                    });
                }

            });
        });

        JSONObject speed = list.stream().min(Comparator.comparing(e -> e.getLong("speed"))).get();

        return "## 连接耗时" +speed.getLong("speed") + "\n" +speed.getString("value") ;

    }


    private final static String getIpforIpaddressV4(String domain){
        queryCount++;
        System.out.println(StrUtil.format("正在 查询{}DNS \n",domain));
        String result = HttpUtil.get(ADDRESSV4+domain);
        System.out.println(StrUtil.format("查询{}DNS 成功 \n",domain));

        String[] ipv4 = result.split("https://www.ipaddress.com/ipv4/");

        Pattern ipv4Pattern = Pattern.compile(IPV4);
        List<JSONObject> list = new ArrayList<>();
        for (String s1 : ipv4) {
            String substring = s1.substring(0, s1.indexOf(">"));
            Matcher m = ipv4Pattern.matcher(substring); // 获取 matcher 对象
            if (m.find()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("value",substring.substring(m.start(), m.end()));
                jsonObject.put("speed",getSpeed(substring.substring(m.start(), m.end())));
                list.add(jsonObject);
            }
        }



        String[] ipv6 = result.split("https://www.ipaddress.com/ipv6/");
        Pattern ipv6Pattern = Pattern.compile(IPV6);
        List<String> ipv6s = new ArrayList<>();
        for (String s1 : ipv6) {
            String substring = s1.substring(0, s1.indexOf(">"));
            Matcher m = ipv6Pattern.matcher(substring); // 获取 matcher 对象
            if (m.find()) {
                ipv6s.add(substring.substring(m.start(), m.end()));
            }
        }


        JSONObject speed = list.stream().min(Comparator.comparing(e -> e.getLong("speed"))).get();

        return "## 连接耗时" +speed.getLong("speed") + "\n" +speed.getString("value") ;
    }


    private final static String getIpforIpaddressV6(String domain){
        queryCount++;
        System.out.println(StrUtil.format("正在 查询{}DNS \n",domain));
        String result = HttpUtil.get(ADDRESSV6+domain);
        System.out.println(StrUtil.format("查询{}DNS 成功 \n",domain));

        String[] ipv6 = result.split("https://www.ipaddress.com/ipv6/");

        Pattern ipv6Pattern = Pattern.compile(IPV6);
        List<JSONObject> list = new ArrayList<>();
        for (String s1 : ipv6) {
            String substring = s1.substring(0, s1.indexOf(">"));
            Matcher m = ipv6Pattern.matcher(substring); // 获取 matcher 对象
            if (m.find()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("value",substring.substring(m.start(), m.end()));
                jsonObject.put("speed",getSpeed(substring.substring(m.start(), m.end())));
                list.add(jsonObject);
            }
        }

        JSONObject speed = list.stream().min(Comparator.comparing(e -> e.getLong("speed"))).get();
        return "## 连接耗时" +speed.getLong("speed") + "\n" +speed.getString("value") ;
    }

    public final static Long getSpeed(String ipAddress) {
        System.out.println(StrUtil.format("正在 测试{} 连接速度 \n",ipAddress));
        Long ipSpeeed  = System.currentTimeMillis();
        try{
            pingCount++;
            HttpUtil.get(ipAddress,3000);
        }catch (Exception e){
            System.out.println(StrUtil.format("测试{} 连接速度 超时 \n",ipAddress));
            overtimePing++;
            return 999999999999999999L;
        }
        successPing++;
        System.out.println(StrUtil.format("测试{} 连接速度 成功 速度为{}毫秒 \n",ipAddress,System.currentTimeMillis() - ipSpeeed));
        return System.currentTimeMillis() - ipSpeeed;
    }

}
