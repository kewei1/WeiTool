package com.github.kewei1.commons;


import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.apache.commons.lang3.StringUtils;


public  class WeiStr{
    public static final Log log = LogFactory.get();

    static {
        log.info("WeiStr初始化");
    }


    public static final class HuStrUtils extends StrUtil{

    }

    private static final class LangStrUtils extends StringUtils{

    }



}

