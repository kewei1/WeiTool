package com.github.kewei1.commons;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.github.kewei1.HuStringUtils;
import com.github.kewei1.thread.FutureUtil;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FileClass {

    public static final Log log = LogFactory.get();

    /**
     * @since 2023/02/08
     * 扫描文件后缀
     */
    private static  List<String> SUFFIX = new ArrayList<String>();

    /**
     * @since 2023/02/08
     * 正则表达式 中文
     */
    private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";

    /**
     * @since 2023/02/08
     * 存放路径
     */
    private static String path = "D:\\File\\分类\\";

    public static boolean addSUFFIX(String suffix){
        return SUFFIX.add(suffix);
    }

    public static void setPath(String pa){
        path = pa;
    }



    /**
     * @since 2023/02/08
     * 扫描统计
     */
    private static Integer count = 0;
    private static Integer copyCount = 0;


    private static Integer taskCount = -1;
    private static Integer completedTaskCount = 0;

    private static Long startRunTime = 0L;



    private static void await(){


        if (taskCount.equals(completedTaskCount)){
            FutureUtil.executorCount("任务完成");
            FutureUtil.shutdown();
            log.info("耗时："+(System.currentTimeMillis()-startRunTime)/1000+"秒");
            return;
        }



        if (taskCount.equals(FutureUtil.getTaskCount()) && completedTaskCount.equals(FutureUtil.getCompletedTaskCount())){
            FutureUtil.executorCount("任务完成");

            FutureUtil.shutdown();
            FutureUtil.executorCount("任务完成");
            log.info("耗时："+((System.currentTimeMillis()-startRunTime)/1000 - 20) +"秒");
            return;
        }

        taskCount = FutureUtil.getTaskCount();
        completedTaskCount = FutureUtil.getCompletedTaskCount();


        if (taskCount > completedTaskCount){
            FutureUtil.executorCount("任务完成");
            try {
                Thread.sleep(20000);
                await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }







    static {
        log.info("初始化");
        startRunTime = System.currentTimeMillis();
        init();

    }

    /**
     * 初始化
     *
     * @author kewei
     * @since 2023/02/08
     */
    private static void init(){
        
        FutureUtil.doRrnnable(()->{
            await();
        });

        SUFFIX.add("pdf");
        SUFFIX.add("doc");
        SUFFIX.add("docx");
        SUFFIX.add("xls");
        SUFFIX.add("xlsx");
        SUFFIX.add("ppt");
        SUFFIX.add("pptx");
        SUFFIX.add("txt");

        SUFFIX.stream().forEach(e->{
            String dirStr = path+e.toString();
            File directory = new File(dirStr);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        });
    }






    public static void main(String[] args) throws Exception {
        fileClass();
    }


    /**
     * 文件分类
     *
     * @author kewei
     * @since 2023/02/08
     */
    public static void fileClass(){
        fileClass(null);
    }

    /**
     * 文件分类
     *
     * @param fileLocation 文件位置
     * @author kewei
     * @since 2023/02/08
     */
    public static void fileClass(String fileLocation) {

        log.info("第{}次执行",count++);

            File file;

            if (!HuStringUtils.isEmpty(fileLocation)){
                file = new File(fileLocation);
            }else {
                file = null;
                //获取磁盘分区列表
                File[] parts =File.listRoots();
                Arrays.stream(parts).forEach(e->{
                    ArrayList<String> dir = getDir(new File(e.toString()));

                    dir.stream().forEach(f->{
                        fileClass(f);
                    });
                });
            }

            if (null!=file &&file.exists()) {

                if (file.isDirectory() && !path.equals(fileLocation)) {

                    log.info("文件夹"+fileLocation);

                    ArrayList<String> dir = getDir(new File(fileLocation));

                    FutureUtil.doRrnnable(()->{

                        log.info("执行"+Thread.currentThread().getName());

                        dir.stream().forEach(e->{
                            fileClass(e);
                        });
                    });

                } else {

                    log.info("文件"+fileLocation);

                    SUFFIX.forEach(e->{
                        if (fileLocation.endsWith(e)){

                            String lastAccessTime = getLastAccessTime(file);

                            String pathName =path +e+"\\"+lastAccessTime.replaceAll("-","").substring(0,6)+"\\";

                            File pathFile = new File(pathName);
                            if (!pathFile.exists()) {
                                pathFile.mkdirs();
                            }


                            File copyFile = new File(pathName+ file.getName());

                            try {
                                copyFile(file, copyFile);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                        }
                    });

                }
            }


    }


    /**
     * 复制文件
     *
     * @param source 源
     * @param dest   桌子
     * @author kewei
     * @since 2023/02/08
     */
    private static void copyFile(File source, File dest) throws IOException {
        Integer integer = copyCount++;
        log.info("开始整理第{}个文件",integer);
        if (!dest.exists()) {
            FileChannel inputChannel = null;
            FileChannel outputChannel = null;
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } finally {
                log.info("整理{}    整理完成第{}个文件   {}整理中",copyCount,integer,copyCount-integer);
                inputChannel.close();
                outputChannel.close();
            }
        }

    }


    /**
     * 获取 文件夹下的所有文件
     *
     * @param dirFile dir文件
     * @author kewei
     * @since 2023/02/08
     */
    public static ArrayList<String> getDir(File dirFile)  {
        ArrayList<String> dirStrArr = new ArrayList<String>();

        if (dirFile.exists()) {
            //直接取出利用listFiles()把当前路径下的所有文件夹、文件存放到一个文件数组
            File files[] = dirFile.listFiles();
            if (null!=files ) {
                for (File file : files) {
                    //如果传递过来的参数dirFile是以文件分隔符，也就是/或者\结尾，则如此构造
                    if (dirFile.getPath().endsWith(File.separator)) {
                        dirStrArr.add(dirFile.getPath() + file.getName());
                    } else {
                        //否则，如果没有文件分隔符，则补上一个文件分隔符，再加上文件名，才是路径
                        dirStrArr.add(dirFile.getPath() + File.separator + file.getName());
                    }
                }
            }
        }
        return dirStrArr;
    }




    /**
     * 得到创建时间
     *
     * @param file 文件
     * @author kewei
     * @since 2023/02/08
     */
    public static String getCreationTime(File file) {
        if (file == null) {
            return null;
        }

        BasicFileAttributes attr = null;
        try {
            Path path =  file.toPath();
            attr = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 创建时间
        Instant instant = attr.creationTime().toInstant();
        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault()).format(instant);
        return format;
    }


    /**
     * 得到最后访问时间
     *
     * @param file 文件
     * @author kewei
     * @since 2023/02/08
     */
    public static String getLastAccessTime(File file) {
        if (file == null) {
            return null;
        }

        BasicFileAttributes attr = null;
        try {
            Path path =  file.toPath();
            attr = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 上次访问时间
        Instant instant = attr.lastAccessTime().toInstant();
        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault()).format(instant);
        return format;
    }


    /**
     * 得到最后修改时间
     *
     * @param file 文件
     * @author kewei
     * @since 2023/02/08
     */
    public static String getLastModifiedTime(File file) {
        if (file == null) {
            return null;
        }
        BasicFileAttributes attr = null;
        try {
            Path path =  file.toPath();
            attr = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 更新时间
        Instant instant = attr.lastModifiedTime().toInstant();
        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault()).format(instant);
        return format;
    }




}
