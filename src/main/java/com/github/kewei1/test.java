package com.github.kewei1;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.github.kewei1.pachong.PaUtils;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import java.util.List;

public class test extends BaseTest{


    private static final Log log = LogFactory.get();

    @Test
    public void test() throws Exception {
//        String s = WeiUtil.HuHttps.get("https://www.baidu.com");
////        String s1 = WeiUtil.Https.doGet("https://www.baidu.com");
//
        PaUtils paUtils = PaUtils.getPaUtils().setREADTIMEOUT(1000);
//
//
//        System.out.println(paUtils.getH5AllDiv("https://www.baidu.com", "id"));
//
//        paUtils.savePage("https://www.baidu.com", "D:\\test\\");

        paUtils.downloadImg("https://image.baidu.com/search/detail?z=0&word=%E7%9A%AE%E5%BD%B1&hs=0&pn=0&spn=0&di=&pi=140237&rn=&tn=baiduimagedetail&is=&ie=utf-8&oe=utf-8&cs=2168645659%2C3174029352&os=&simid=&adpicid=0&lpn=0&fr=albumsdetail&fm=&ic=0&sme=&cg=&bdtype=&oriquery=&objurl=https%3A%2F%2Ft7.baidu.com%2Fit%2Fu%3D2168645659%2C3174029352%26fm%3D193%26f%3DGIF&fromurl=ipprf_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bkt2rtvf_z%26e3BvgAzdH3Fip4sAzdH3F8n0AzdH3FWSPpWMYHCcM_z%26e3Bip4s&gsm=&islist=&querylist=&album_tab=%E8%AE%BE%E8%AE%A1%E7%B4%A0%E6%9D%90&album_id=394", "D:\\test\\");


    }
}
