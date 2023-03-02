package com.github.kewei1.test;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.github.kewei1.pachong.PaUtils;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.HashMap;

public class test extends BaseTest{


    private static final Log log = LogFactory.get();

    @Test
    public void test() throws Exception {

        PaUtils paUtils = PaUtils.getPaUtils();

        HashMap<String, Object> cookie = new HashMap<>();
        cookie.put("Cookie","__jsluid_s=fd6c7fc57c9dcb7735c18a1283744430; gr_user_id=af4470fb-072d-4767-beae-1ef352d7660d; Hm_lvt_a808a1326b6c06c437de769d1b85b870=1676360636,1677309363,1677740436; NOWCODERUID=698A1DEB8A4DC6CA4E9F1833C9183873; NOWCODERCLINETID=145F5D86FA95490D09ECB6453AC907D8; __snaker__id=r4vJrpHTrfYQixwn; gdxidpyhxdE=GWI6dxq3p386GoU4uHDg%5CSioQK4aWudO9qAKwGnA%2FS8M582NPjhcMQ33iMYicpO4b9oPmi1WjwLsAbMKrDmXI2rhlfwyLwpeUwg8o%2FJRZYLcbtTsL%2B%2BYEoOsuG%5Cmo97WIcPtPVRJOMOBNN%5C7Szt2iW1bPeQgNZ%2FP7B3SKQ9JkhAqnnLv%3A1677313481867; YD00000586307807%3AWM_NI=pHTA%2FCCLWIVjbKnhzRDE%2BotggEm%2BYRl8d1WJiiAOx%2BqNQeXTB5C%2B5aq7y3XARS%2F%2BWOHrbUUiIxZDG662ddHU9yHToLlekxOLgTpvNVp4%2BO6ijjx85oRSXgCW%2BEzMfcpjNE0%3D; YD00000586307807%3AWM_NIKE=9ca17ae2e6ffcda170e2e6ee8eeb6d8ff59cdacd63b0ac8ab2d85b838b9bb0c46f9a9fbed1f13af1ecfe85e62af0fea7c3b92ab4989ca2ae49819499aad562a295a289f95a889be590f76da59ba3ccdc5ab0e8acd2d642abb78fb0fb21b3929f86db6082eb8dccb86bb3e9b7d4db4b928de1ccd6458caeaeb6d8638ba896a5c63ef6eeac85cb5ffba788b5ee48b0bba599ce48a58cc097cd5eb4ece5b0b563aab6b8d9b865928d9888c25de99bb6aee569b4b682a6b337e2a3; YD00000586307807%3AWM_TID=wlf3TKUv5HZABBEEUReRbCW5rEab0rqV; t=724553889DACF75F1772A890A1E1062B; c196c3667d214851b11233f5c17f99d5_gr_last_sent_cs1=580726620; acw_tc=821127ebf96eca35d51dff434f61005aff4f49b4ef524db4112d1b8698c75fab; csrfToken=ytQXBxC3eWrUSJjef-SjYlzn; c196c3667d214851b11233f5c17f99d5_gr_session_id_68fa9d19-0108-4468-870f-1734ac144c2c=true; c196c3667d214851b11233f5c17f99d5_gr_session_id=68fa9d19-0108-4468-870f-1734ac144c2c; c196c3667d214851b11233f5c17f99d5_gr_last_sent_sid_with_cs1=68fa9d19-0108-4468-870f-1734ac144c2c; Hm_lpvt_a808a1326b6c06c437de769d1b85b870=1677740726; c196c3667d214851b11233f5c17f99d5_gr_cs1=580726620");

        paUtils.setCOOKIES(cookie);

        Document document = paUtils.getDocument("https://www.nowcoder.com/exam/test/66225215/submission?examPageSource=Company&pid=39932240&testCallback=https%3A%2F%2Fwww.nowcoder.com%2Fexam%2Fcompany&testclass=%E8%BD%AF%E4%BB%B6%E5%BC%80%E5%8F%91");
        //<div class="question-info question-info" data-v-2b881dd3 data-v-49de60a5>
        //question-info question-info


        document.select("div.question-item").forEach(element -> {

            System.out.println("=====================================");
            //tw-text-size-head-pure tw-font-semibold tw-text-gray-800 dark:tw-text-[#E6E6E6] tw-leading-7 tw-flex
//            element.select("div.tw-text-size-head-pure").forEach(element1 -> {
//                System.out.println(element1.text());
//            });

            //题号
//            System.out.println(element.attr("data-question-index"));

            //题型
            element.select("div.commonClass").forEach(element1 -> {
                System.out.println(element1.text());
            });

            //tw-text-size-head-pure tw-font-semibold tw-text-gray-800 dark:tw-text-[#E6E6E6] tw-leading-7 tw-flex

            element.select("div.tw-text-size-head-pure").forEach(element1 -> {
                System.out.println(element1.text());
            });


//            //题目
//            element.select("div.commonPaperHtml").forEach(element1 -> {
//                System.out.println(element1.text());
//            });

            //选项
            element.select("div.question-select").forEach(element1 -> {
                System.out.println(element1.text());
            });


            //答案 answer-wrap
            element.select("div.answer-wrap").forEach(element1 -> {
                System.out.println(element1.text());
            });



        });







    }
}
