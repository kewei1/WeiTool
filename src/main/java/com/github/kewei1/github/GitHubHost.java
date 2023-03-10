package com.github.kewei1.github;

import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author kewei
 *  github hosts 配置<br>
 *  1.获取原始hosts文件 <br>
 *  2.获取github hosts信息<br>
 *  3.合并hosts文件<br>
 *  4.写入hosts文件<br>
 *  5.刷新dns缓存<br>
 * @since 2023/02/03
 */
public class GitHubHost {

    /**
     * @since 2023/02/04
     * 日志
     */
    private static final Log log = LogFactory.get();

    /**
     *  需要配置host的域名
     */
    private static Set<String>  HOSTS =  new HashSet<>();
    /**
     *  host内容
     */
    private static StringBuilder content = new StringBuilder();


    /**
     *  ping超时信息统计
     */
    private static volatile  int overtimePing = 0;
    /**
     *  ping成功信息统计
     */
    private static volatile   int successPing = 0;
    /**
     *  ping成功数统计
     */
    private static volatile  int successCount = 0;
    /**
     *  ping总数统计
     */
    private static volatile  int pingCount = 0;
    /**
     *  DNS查询统计
     */
    private static volatile  int queryCount = 0;

    /**
     *  程序运行时间
     */
    private static  volatile Long speeed = 0L;


    /**
     *  myssl DNS查询
     */
    public static final String MYSSL = "https://myssl.com/api/v1/tools/dns_query?qtype=1&qmode=-1&host=";

    /**
     *  addressV4 DNS查询
     */
    public static final String ADDRESSV4 = "https://www.ipaddress.com/site/";
    /**
     *  addressV6 DNS查询
     */
    private static final String ADDRESSV6 = "https://www.ipaddress.com/site/";


    /**
     *  传入 dns类型
     */
    private static  String DNS_TYPE = "";

    /**
     *  IPV4正则
     */
    private final static String IPV4 = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";

    /**
     *  IPV6正则
     */
    private final static String IPV6 ="((?:[\\da-fA-F]{0,4}:[\\da-fA-F]{0,4}){2,7})(?:[\\/\\\\%](\\d{1,3}))?";


    /**
     * @param
     *  初始化
     * @author kewei
     * @since 2023/02/03
     *
     */
    private final static void  init() throws InterruptedException {

        setHOSTS();

        log.info("获取原始hosts文件");
        File file = new File("C:\\Windows\\System32\\drivers\\etc\\hosts");
        Stream<String> of1 = StreamUtil.of(file, CharsetUtil.CHARSET_UTF_8);
        log.info("获取原始hosts文件成功");

        log.info("开始处理hosts文件 非github DNS 保留");
        content.append("## 非github DNS").append("\n");
        of1.forEachOrdered(e -> {
            if (null!=e && !e.equals("") && e.length()>0 && !e.contains("github") &&  !e.startsWith("#")){
                content.append(e).append("\n");
            }
        });
        log.info("处理hosts文件 非github DNS 保留成功");


        log.info("开始处理hosts文件 github DNS");
        content.append("\n\n\n\n## github DNS").append("\n");
        final int threadSize = HOSTS.size();
        final CountDownLatch countDownLatch = new CountDownLatch(threadSize);
        ExecutorService executorService = Executors.newCachedThreadPool();
        HOSTS.stream().forEach(e -> {
            executorService.execute(() -> {
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

                if (!StrUtil.isBlankIfStr(ip)) {
                    if (!ip.contains("## 连接耗时99999")) {
                        content.append(ip).append(" ").append(e).append("\n");
                        successCount++;
                    }
                }


                countDownLatch.countDown();
            });
        });
        countDownLatch.await();
        log.info("处理hosts文件 github DNS 成功");
        executorService.shutdown();
        saveHOSTS();
    }

    public static void main(String[] args) throws InterruptedException {
        GitHubHost.config(MYSSL);
    }

    /**
     * @param
     *  设置hosts文件
     * @author kewei
     * @since 2023/02/03
     */
    private static void setHOSTS() {
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
        HOSTS.add("docs.github.com");
        HOSTS.add("github.io");
        HOSTS.add("dtstack.github.io");
    }


    /**
     * @param
     *  保存hosts文件
     * @author kewei
     * @since 2023/02/03
     */
    private static void saveHOSTS() {
        writeHost();
        log.info(StrUtil.format("获取{}次DNS记录",queryCount));
        log.info(StrUtil.format("测速{}次",pingCount));
        log.info(StrUtil.format("成功{}次",successPing));
        log.info(StrUtil.format("超时{}次",overtimePing));
        log.info(StrUtil.format("配置成功{}条DNS记录\n",successCount));
        log.info(StrUtil.format("执行耗时{}秒",(System.currentTimeMillis() -speeed)/1000));
        log.info("配置完成");
        log.info("请重启浏览器");
    }

    /**
     * @param  type dns类型
     * 配置dns类型
     * @author kewei
     * @since 2023/02/03
     * @throws  InterruptedException 异常
     */
    public static void config(String type) throws InterruptedException {
        DNS_TYPE = type;
        speeed = System.currentTimeMillis();
        init();
    }




    /**
     * @param
     *  写入hosts文件
     * @author kewei
     * @since 2023/02/03
     */
    private final static void writeHost(){

        File file = new File("C:\\Windows\\System32\\drivers\\etc\\hosts");
        //write
        try {
            log.info("开始写入hosts");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), CharsetUtil.CHARSET_UTF_8));
            writer.write(content.toString());
            writer.flush();
            writer.close();
            log.info("写入hosts完成");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //flushdns
        try {
            log.info("开始刷新DNS");
            Runtime.getRuntime().exec("ipconfig /flushdns");
            log.info("刷新DNS完成");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param  domain 域
     *  获取domain的ip地址
     * @author kewei
     * @since 2023/02/03
     * @return ip
     */
    public static String getIP(String domain){
        String ip = "";
        try {
            InetAddress[] ips = java.net.InetAddress.getAllByName(domain);
            ip = ips[0].getHostAddress();
        } catch (Exception e) {
        }
        return ip;
    }

    /**
     * @param  domain 域
     *  通过MYSSL 获取domain的ip
     * @author kewei
     * @since 2023/02/03
     */
    private final static String getIpforMyssl(String domain){
        String url = MYSSL+domain;
        queryCount++;
        log.info(StrUtil.format("正在 查询{}DNS \n",domain));
        String result = HttpUtil.get(url);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject data = jsonObject.getJSONObject("data");

        if (null == data){
            log.info(StrUtil.format("查询{}DNS 失败 \n",domain));
           //暂停 5秒
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getIpforMyssl(domain);
        }

        log.info(StrUtil.format("查询{}DNS 成功 \n",domain));


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


    /**
     * @param  domain 域
     *  通过ipaddress 获取domain的ip
     * @author kewei
     * @since 2023/02/03
     */
    private final static String getIpforIpaddressV4(String domain){
        queryCount++;
        log.info(StrUtil.format("正在 查询{}DNS \n",domain));
        String result ="";
        try {
             result = HttpUtil.get(ADDRESSV4+domain);
        }catch (Exception e){
            return "## 连接超时"  ;
        }


        log.info(StrUtil.format("查询{}DNS 成功 \n",domain));

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
        if(list.size() == 0){
            return "## 连接超时"  ;
        }

        JSONObject speed = list.stream().min(Comparator.comparing(e -> e.getLong("speed"))).get();

        return "## 连接耗时" +speed.getLong("speed") + "\n" +speed.getString("value") ;
    }

    /**
     * @param  domain 域
     *  通过ipaddress 获取domain的ipv6
     * @author kewei
     * @since 2023/02/03
     */
    private final static String getIpforIpaddressV6(String domain){
        queryCount++;
        log.info(StrUtil.format("正在 查询{}DNS \n",domain));
        String result = HttpUtil.get(ADDRESSV6+domain);
        log.info(StrUtil.format("查询{}DNS 成功 \n",domain));

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

    /**
     * @param  ipAddress ip地址
     *  ip连接速度 毫秒为单位
     * @author kewei
     * @since 2023/02/03
     * @return 速度
     */
    public final static synchronized  Long getSpeed(String ipAddress) {
        log.info(StrUtil.format("正在 测试{} 连接速度 \n",ipAddress));
        Long ipSpeeed  = System.currentTimeMillis();
        try{
            pingCount++;
            HttpUtil.get(ipAddress,3000);
        }catch (Exception e){
            log.info(StrUtil.format("测试{} 连接速度 超时 \n",ipAddress));
            overtimePing++;
            return 999999999999999999L;
        }
        successPing++;
        log.info(StrUtil.format("测试{} 连接速度 成功 速度为{}毫秒 \n",ipAddress,System.currentTimeMillis() - ipSpeeed));
        return System.currentTimeMillis() - ipSpeeed;
    }

    //回收时
    @Override
    public void finalize() throws Throwable {
        System.out.println("GitHubHost 对象被回收了");
    }

    //创建时

}
