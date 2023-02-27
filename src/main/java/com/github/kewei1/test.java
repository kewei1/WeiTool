package com.github.kewei1;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.github.kewei1.pachong.PaUtils;
import com.github.kewei1.pachong.PaUtilsV2;
import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class test extends BaseTest{


    private static final Log log = LogFactory.get();

    @Test
    public void test() throws Exception {

//        Connection.Response response = PaUtilsV2.getPaUtils().getResponse("https://www.baidu.com");

        Document document = PaUtilsV2.getPaUtils().getDocument("https://xiaolincoding.com/network/1_base/tcp_ip_model.html");


        document.select("");







    }
}
