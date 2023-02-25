package com.github.kewei1;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.github.kewei1.commons.FileClass;
import org.junit.Test;

public class test extends BaseTest{


    private static final Log log = LogFactory.get();

    @Test
    public void test() throws Exception {
        String s = WeiUtil.HuHttps.get("https://www.baidu.com");
        String s1 = WeiUtil.Https.doGet("https://www.baidu.com");


    }
}
