package com.github.kewei1.github;

import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.CharsetUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.stream.Stream;

public class HostUtils {

    private static List<String>  HOSTS =  new ArrayList<>();
    private static final  String REGEX_CHINESE = "[\u4e00-\u9fa5]";

    static {
        HOSTS.add("github.com");


        File file = new File("C:\\Windows\\System32\\drivers\\etc\\hosts");
        Stream<String> of1 = StreamUtil.of(file, CharsetUtil.CHARSET_UTF_8);
        StringBuilder content = new StringBuilder();

        of1.forEachOrdered(e -> {

            if (null!=e &&!e.equals("") && !e.contains("github")){
                content.append(e).append("\n");
            }

            if (null!=e && !e.startsWith("#")  &&!e.equals("")  &&e.contains("github")){

            }

            //185.199.108.133 avatars1.githubusercontent.com
            //20.27.177.113 www.github.com
            //185.199.108.133 camo.githubusercontent.com
            //185.199.108.133 avatars7.githubusercontent.com
            //185.199.108.133 raw.github.com
            //185.199.108.133 avatars8.githubusercontent.com
            //185.199.108.133 avatars0.githubusercontent.com
            //185.199.108.133 avatars3.githubusercontent.com
            //185.199.108.133 avatars4.githubusercontent.com
            //185.199.108.133 avatars5.githubusercontent.com
            //185.199.108.133 marketplace-screenshots.githubusercontent.com
            //185.199.108.133 gist.githubusercontent.com
            //185.199.108.133 cloud.githubusercontent.com
            //185.199.108.133 repository-images.githubusercontent.com
            //185.199.108.133 user-images.githubusercontent.com
            //185.199.108.133 assets-cdn.github.com
            //185.199.108.133 avatars2.githubusercontent.com
            //185.199.108.133 raw.githubusercontent.com
            //185.199.108.133 avatars6.githubusercontent.com
            //185.199.108.133 desktop.githubusercontent.com
            //20.27.177.113


        });

        System.out.println(content);

//        BufferedWriter out = null;
//        try {
//            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
//            String target = content.toString();
//            out.write(target);
//            out.flush();
//            out.close();
//        } catch (Exception e) {
//        } finally {
//        }
    }

    public static void main(String[] args) {
        System.out.println();
    }




    public static void update(){

    }

}
