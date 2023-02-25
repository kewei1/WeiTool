package com.github.kewei1.yali;

import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.github.kewei1.thread.FutureUtil;
import org.junit.Test;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class CeYa {

    public static final Log log = LogFactory.get();




    @Test
    public void test() throws Exception{


    }


    //get压力测试
    public static void getYaLiCeShi(String url,int count) throws Exception{

        log.info("开始压力测试--{}",url);
        Long start = System.currentTimeMillis();

        final CountDownLatch countDownLatch = new CountDownLatch(count);


        for (int j = 0; j < count; j++) {
            int finalJ = j;
            FutureUtil.doRrnnable(()->{

                try {
                    HttpUtil.get(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                log.info("任务总数--{}--执行任务数--{}--剩余任务数{}",count, finalJ,countDownLatch.getCount());
                countDownLatch.countDown();
            });
        }

        try {
            countDownLatch.await();
            Long end = System.currentTimeMillis();
            log.info("压力测试结束--{}--耗时--{}",url,end-start);
            FutureUtil.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }


    //ping压力测试
    public static void pingYaLiCeShi(String url ,int count){
        log.info("开始压力测试--{}",url);
        Long start = System.currentTimeMillis();
        final CountDownLatch countDownLatch = new CountDownLatch(count);

        for (int j = 0; j < count; j++) {
            FutureUtil.doRrnnable(()->{
                log.info("任务总数--{}--剩余任务数--{}",count,countDownLatch.getCount());
                countDownLatch.countDown();
                try {
                    InetAddress.getByName(url).isReachable(3000);
                } catch (Exception e) {
                    log.error("ping失败--{}",url);
                    throw new RuntimeException(e);
                }

            });
        }

        try {
            countDownLatch.await();
            Long end = System.currentTimeMillis();
            log.info("压力测试结束--{}--耗时--{}",url,end-start);
            FutureUtil.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static boolean ping(String ipAddress) throws Exception {
        int  timeOut =  3000 ;  //超时应该在3钞以上
        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);
        // 当返回值是true时，说明host是可用的，false则不可。
        return status;
    }

    //打开 cmd
    public static void openCmd(String cmd){
        try {

            //查看 IP 地址 UTF-8
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = process.getInputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                System.out.println(new String(bytes, 0, len, "GBK"));
            }
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//
//        //电脑名称  WiFi名称 IP地址 MAC地址 网关  子网掩码
//
//
//        //ping
////        openCmd("cmd /c ping qq.com");
//
//
//
//    }






    //管理员 打开 cmd
    public static void openCmdAdmin(String cmd){
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = process.getInputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                System.out.println(new String(bytes, 0, len, "GBK"));
            }
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //目录打开  cmd
    public static void openCmdDir(String cmd,String dir){
        try {
            Process process = Runtime.getRuntime().exec(cmd,null,new File(dir));
            InputStream inputStream = process.getInputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                System.out.println(new String(bytes, 0, len, "GBK"));
            }
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //打开 Windows PowerShell


    //获取电脑名称
    public static String getComputerName(){
        String computerName = "";
        try {
            Process process = Runtime.getRuntime().exec("cmd /c hostname");
            InputStream inputStream = process.getInputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                computerName = new String(bytes, 0, len, "GBK");
            }
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return computerName;

    }

    //获取电脑内存信息
    public static String getComputerMemory(){
        String computerMemory = "";
        try {
            Process process = Runtime.getRuntime().exec("cmd /c wmic memorychip get capacity");
            InputStream inputStream = process.getInputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                computerMemory = new String(bytes, 0, len, "GBK");
            }
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return computerMemory;

    }

    //获取电脑CPU信息
    public static String getComputerCPU(){
        String computerCPU = "";
        try {
            Process process = Runtime.getRuntime().exec("cmd /c wmic cpu get name");
            InputStream inputStream = process.getInputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                computerCPU = new String(bytes, 0, len, "GBK");
            }
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return computerCPU;

    }

    //获取电脑硬盘信息
    public static String getComputerDisk(){
        String computerDisk = "";
        try {
            Process process = Runtime.getRuntime().exec("cmd /c wmic diskdrive get size");
            InputStream inputStream = process.getInputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                computerDisk = new String(bytes, 0, len, "GBK");
            }
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return computerDisk;

    }

    //获取系统信息
    public static String getComputerSystem(){
        String computerSystem = "";
        try {
            Process process = Runtime.getRuntime().exec("cmd /c wmic os get caption");
            InputStream inputStream = process.getInputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                computerSystem = new String(bytes, 0, len, "GBK");
            }
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return computerSystem;

    }

    // 获取桌面路径
    public static String getDesktopPath() {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com = fsv.getHomeDirectory();
        return com.getPath();
    }

    // 系统版本
    public static String getSystemVersion() {
        return System.getProperty("os.name");
    }
    //系统架构
    public static String getSystemArch() {
        return System.getProperty("os.arch");
    }
    //位置
    public static String getSystemLocation() {
        return System.getProperty("user.country");
    }
    //语言
    public static String getSystemLanguage() {
        return System.getProperty("user.language");
    }
    //用户名
    public static String getSystemUserName() {
        return System.getProperty("user.name");
    }
    //用户主目录
    public static String getSystemUserHome() {
        return System.getProperty("user.home");
    }
    //用户工作目录
    public static String getSystemUserDir() {
        return System.getProperty("user.dir");
    }
    //系统临时目录
    public static String getSystemTempDir() {
        return System.getProperty("java.io.tmpdir");
    }
    //系统分隔符
    public static String getSystemFileSeparator() {
        return System.getProperty("file.separator");
    }
    //系统路径分隔符
    public static String getSystemPathSeparator() {
        return System.getProperty("path.separator");
    }
    //系统行分隔符
    public static String getSystemLineSeparator() {
        return System.getProperty("line.separator");
    }
    //系统编码
    public static String getSystemEncoding() {
        return System.getProperty("file.encoding");
    }

    //IP地址
    public static String getSystemIP() {
        String ip = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }

    //时间
    public static String getSystemTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    //获取 开机时长
    public static String getSystemRunTime() {
        long time = System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime();
        long day = time / (24 * 60 * 60 * 1000);
        long hour = (time / (60 * 60 * 1000) - day * 24);
        System.out.println(time);

        return day + "天" + hour + "小时";
    }









    @Test
    public void test1() {
        System.out.println("电脑名称：" + getComputerName());
        System.out.println("电脑内存：" + getComputerMemory());
        System.out.println("电脑CPU：" + getComputerCPU());
        System.out.println("电脑硬盘：" + getComputerDisk());
        System.out.println("电脑系统：" + getComputerSystem());
        System.out.println("桌面路径：" + getDesktopPath());
        System.out.println("系统版本：" + getSystemVersion());
        System.out.println("系统架构：" + getSystemArch());
        System.out.println("位置：" + getSystemLocation());
        System.out.println("语言：" + getSystemLanguage());
        System.out.println("用户名：" + getSystemUserName());
        System.out.println("用户主目录：" + getSystemUserHome());
        System.out.println("用户工作目录：" + getSystemUserDir());
        System.out.println("系统临时目录：" + getSystemTempDir());
        System.out.println("系统分隔符：" + getSystemFileSeparator());
        System.out.println("系统路径分隔符：" + getSystemPathSeparator());
        System.out.println("系统行分隔符：" + getSystemLineSeparator());
        System.out.println("系统编码：" + getSystemEncoding());
        System.out.println("IP地址：" + getSystemIP());
        System.out.println("时间：" + getSystemTime());
        System.out.println("开机时长：" + getSystemRunTime());


    }














}
