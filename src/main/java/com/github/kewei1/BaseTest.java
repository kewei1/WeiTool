package com.github.kewei1;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.github.kewei1.commons.FileClass;
import junit.framework.TestSuite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.util.Map;

public class BaseTest {
    private static final Log log = LogFactory.get();

    @Before
    public void before() throws Exception{
        log.info("-----------------测试开始-----------------");
    }

    public static void main(String[] args) {
        FileClass.addSUFFIX("mp9");
        FileClass.setPath("D:\\File\\分类1\\");
        FileClass.fileClass("D:\\");
    }


    @Test
    public void test(){
        FileClass.addSUFFIX("mp4");
        FileClass.setPath("D:\\File\\分类1\\");
        FileClass.fileClass("D:\\");
    }

    @After
    public void after() throws Exception{
        log.info("-----------------测试结束-----------------");
    }

}
