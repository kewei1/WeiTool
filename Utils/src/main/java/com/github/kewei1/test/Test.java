package com.github.kewei1.test;

import cn.hutool.Hutool;
import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.CharsetUtil;
import com.github.kewei1.HuStringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Test {

    public static void main(String[] args) throws Exception {
//        deleteChina(null);
    }


    private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";

    public static void deleteChina(){
        deleteChina(null);
    }

    public static void deleteChina(String fileLocation) {

        File file = null;
        if (!HuStringUtils.isEmpty(fileLocation)){
            file = new File(fileLocation);
        }else {
            File[] parts =File.listRoots();

            Arrays.stream(parts).forEach(e->{
                ArrayList<String> dir = Dir(new File(e.toString()));
                dir.stream().forEach(f->{
                    deleteChina(f);
                });
            });
        }

        if (file.exists()) {

            if (file.isDirectory()) {
                System.out.print("--文件夹");
                System.out.println(fileLocation);

                ArrayList<String> dir = Dir(new File(fileLocation));
                System.out.println();
                dir.stream().forEach(f->{
                    deleteChina(f);
                });
            } else {
                System.out.print("--文件");
                System.out.println(fileLocation);
                Stream<String> of1 = StreamUtil.of(file, CharsetUtil.CHARSET_UTF_8);
                StringBuilder content = new StringBuilder();

                of1.forEachOrdered(e->{
                    content.append(e.replaceAll(REGEX_CHINESE,"")).append("\n");
                });

                BufferedWriter out = null;
                try {
                    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                    String target = content.toString();
                    out.write(target);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                }finally {
                }
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
                        dirStrArr.add(dirFile.getPath() + File.separator
                                + file.getName());
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

}
