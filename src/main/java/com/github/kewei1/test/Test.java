package com.github.kewei1.test;

import com.alibaba.fastjson.parser.Feature;
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
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


public class Test {

    private static  List<String> files = new ArrayList<String>();
    private static  List<String> ends = new ArrayList<String>();

    private static Integer count = 0;

    private static String path = "D:\\File\\分类\\";




    public static void main(String[] args) throws Exception {
        ends.add("pdf");
        ends.add("doc");
        ends.add("docx");
        ends.add("xls");
        ends.add("xlsx");
        ends.add("ppt");
        ends.add("pptx");
        ends.add("txt");

//        ends.add("jpg");
//        ends.add("jpeg");
//        ends.add("png");
//        ends.add("gif");
//        ends.add("bmp");
//        ends.add("ico");
//        ends.add("svg");
//
//        ends.add("mp3");
//        ends.add("wav");
//        ends.add("wma");
//        ends.add("ogg");
//        ends.add("ape");
//
//        ends.add("mp4");
//        ends.add("avi");
//        ends.add("rmvb");
//        ends.add("rm");
//        ends.add("flv");
//        ends.add("wmv");




        ends.stream().forEach(e->{
            String dirStr = path+e.toString();
            File directory = new File(dirStr);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        });


//        ends.add(".txt");
//        ends.add(".rar");
//        ends.add(".zip");
//        ends.add(".7z");
//        deleteChina("C:\\Users\\15400\\Documents");
//        deleteChina("D:\\");
        deleteChina();
    }


    private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";

    public static void deleteChina(){
        deleteChina(null);
        FutureUtil.shutdown();
    }

    public static void deleteChina(String fileLocation) {
        count++;
        System.out.println(count);

            File file;
            if (!HuStringUtils.isEmpty(fileLocation)){
                file = new File(fileLocation);
            }else {
                file = null;
                File[] parts =File.listRoots();

                Arrays.stream(parts).forEach(e->{
                    ArrayList<String> dir = Dir(new File(e.toString()));
                    dir.stream().forEach(f->{
                        deleteChina(f);
                    });
                });
            }

            if (null!=file &&file.exists()) {

                if (file.isDirectory() && !path.equals(fileLocation)) {
                    System.out.print("--文件夹");
                    System.out.println(fileLocation);
                    ArrayList<String> dir = Dir(new File(fileLocation));

                    FutureUtil.synchronizeExecute(2, TimeUnit.HOURS,()->{
                        System.out.println("执行"+Thread.currentThread().getName());
                        dir.stream().forEach(e->{
                            deleteChina(e);
                        });
                        return null;
                    });

                } else {
                    System.out.print("--文件");
                    System.out.println(fileLocation);

                    ends.forEach(e->{
                        if (fileLocation.endsWith(e)){
                            file.getName();

                            String lastAccessTime = getLastAccessTime(file);

                            String pathName =path +e+"\\"+lastAccessTime.replaceAll("-","").substring(0,6)+"\\";

                            File pathFile = new File(pathName);
                            if (!pathFile.exists()) {
                                pathFile.mkdirs();
                            }


                            File copyFile = new File(pathName+ file.getName());

                            try {
                                copyFile(file, copyFile);
                                count++;
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            files.add(fileLocation);
                            System.out.println(Thread.currentThread().getName());
                        }
                    });


//                Stream<String> of1 = StreamUtil.of(file, CharsetUtil.CHARSET_UTF_8);
//                StringBuilder content = new StringBuilder();
//
//                of1.forEachOrdered(e->{
//                    content.append(e.replaceAll(REGEX_CHINESE,"")).append("\n");
//                });
//
//                BufferedWriter out = null;
//                try {
//                    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
//                    String target = content.toString();
//                    out.write(target);
//                    out.flush();
//                    out.close();
//                } catch (Exception e) {
//                }finally {
//                }
                }
            }

//            return null;



    }

    private static void copyFile(File source, File dest) throws IOException {
        if (!dest.exists()) {
            FileChannel inputChannel = null;
            FileChannel outputChannel = null;
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } finally {
                inputChannel.close();
                outputChannel.close();
            }
        }
    }




    public static ArrayList<String> Dir(File dirFile)  {
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




//

    // ----------------------------------------------------------------



    static Pattern pattern = Pattern.compile("(\r\n)*", Pattern.DOTALL);

    /**
     * 里德文件
     *
     * @param file 文件
     * @throws IOException ioexception
     */
    public static void readeFile(File file) throws IOException {
        if(file.exists()){
            for(File fileson : file.listFiles()){
                if(fileson.isFile()){
                    //对java文件逐行读取修改注释
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new FileReader(fileson));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    StringBuilder content = new StringBuilder();
                    String tmp = null;
                    while ((tmp = reader.readLine()) != null) {
                        if(tmp.indexOf("//") >= 0){
                            tmp = tmp.replace(tmp.substring(tmp.indexOf("//"),tmp.length()),"");
                        }
                        //删除已空格开始，以空格结尾的行
                        if(tmp.matches("^\t+$")){
                            continue;
                        }

                        //删除空行
                        tmp = tmp.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1");
                        if("".equals(tmp) || null == tmp || "\t".equals(tmp) || "".equals(tmp.trim())){
                            continue;
                        }
                        content.append(tmp);
                        content.append("\n");
                    }
                    String target = content.toString();
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileson)));
                    out.write(target);
                    out.flush();
                    out.close();
                } else {
                    System.out.println(fileson.getName());
                    readeFile(fileson);
                }
            }
        }
    }










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
