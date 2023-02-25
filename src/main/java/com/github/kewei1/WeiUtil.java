package com.github.kewei1;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.apache.commons.lang3.StringUtils;


public class WeiUtil {
    public static final Log log = LogFactory.get();

    static {
        log.info("WeiUti 初始化");
    }

    //WeiTool 自定义 Http请求
    public static final class Https extends com.github.kewei1.http.HttpUtil { }
    //Hutool Http请求
    public static final class HuHttps extends cn.hutool.http.HttpUtil { }

    //Hutool 自定义 字符串工具类
    public static final class HuStrUtils extends StrUtil { }
    //Lang3 Str 字符串工具类
    private static final class LangStrUtils extends StringUtils { }

    //GitHubHost 自定义
    public static final class GitHubHost extends com.github.kewei1.github.GitHubHost { }

}


