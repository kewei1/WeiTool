package com.github.kewei1;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.github.kewei1.pachong.PaUtils;
import com.github.kewei1.pachong.PaUtilsV2;
import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class test extends BaseTest{


    private static final Log log = LogFactory.get();

    @Test
    public void test() throws Exception {

        PaUtilsV2 paUtils = PaUtilsV2.getPaUtils();

        Document document = paUtils.getDocument("https://xiaolincoding.com/");

        paUtils.documentToBufferedImage(document,"D:\\test\\imgss\\");

    }
}
