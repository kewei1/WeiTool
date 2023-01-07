package com.github.kewei1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    /**
     * @author kewei
     * @date 2023/01/07
     * @params
     * @doc 获取当前时间戳
     */
    public static Long getTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * @author kewei
     * @date 2023/01/07
     * @params @param date 日期
     * @doc 时间戳
     */
    public static Long getTimeMillis(Date date) {
        return date.getTime();
    }


    /**
     * @param pattern 样式
     * @author kewei
     * @date 2023/01/07
     * @params @param strDate String日期
     * @doc 时间戳
     */
    public static long getTimeMillis(String strDate,String pattern) {
        if (strDate == null || strDate.isEmpty()) {
            return -1;
        }
        DateFormat format = null;

        format = new SimpleDateFormat(pattern);

        if(DateType.GMT.equals(pattern)){
            //GMT时间  Mon, 11 May 2020 07:21:02 GMT
            format = new SimpleDateFormat(DateType.GMT, Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
        }if (DateType.UTC.equals(pattern)) {
            //UTC时间 2020-05-12T01:56:15.890Z**
            format = new SimpleDateFormat(DateType.UTC, Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        try {
            return getTimeMillis(format.parse(strDate));
        } catch (Exception e) {
            return -1;
        }
    }


    /**
     * @author kewei
     * @date 2023/01/07
     * @params @param strDate GMT类型str日期
     * @doc 时间戳
     */
    public static long getTimeMillisGMT(String strDate ) {
        DateFormat df = new SimpleDateFormat(DateType.GMT, Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            return getTimeMillis(df.parse(strDate));
        } catch (ParseException e) {
            return -1;
        }
    }


    /**
     * @author kewei
     * @date 2023/01/07
     * @params @param strDate UTC类型str日期
     * @doc 时间戳
     */
    public static long getTimeMillisUTC(String strDate) {
        DateFormat df = new SimpleDateFormat(DateType.UTC, Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return getTimeMillis(df.parse(strDate));
        } catch (ParseException e) {
            return -1;
        }
    }

    /**
     * @param locale 语言环境
     * @author kewei
     * @date 2023/01/07
     * @params @param timeMillis 时间戳
     * @doc 得到简单str日期
     */
    public static String getSimpleStrDate(Long timeMillis,Locale locale) {
        SimpleDateFormat format = new SimpleDateFormat(DateType.DATE_TIME, locale);

        return format.format(new Date(timeMillis));
    }

    /**
     * @author kewei
     * @date 2023/01/07
     * @params @param timeMillis 时间戳
     * @doc 得到简单str日期
     */
    public static String getSimpleStrDate(Long timeMillis) {
        SimpleDateFormat format = new SimpleDateFormat(DateType.DATE_TIME, Locale.getDefault());
        return format.format(new Date(timeMillis));
    }

    /**
     * @param pattern 模式
     * @param locale  语言环境
     * @param zone    区
     * @author kewei
     * @date 2023/01/07
     * @params @param timeMillis 时间戳
     * @doc 得到str日期
     */
    public static String getStrDate(Long timeMillis, String pattern, Locale locale ,int zone) {

        timeMillis = timeMillis + zone;
        SimpleDateFormat format = new SimpleDateFormat(pattern, locale);

        if(DateType.GMT.equals(pattern)){
            //GMT时间  Mon, 11 May 2020 07:21:02 GMT
            format = new SimpleDateFormat(DateType.GMT, Locale.ENGLISH);
        }if (DateType.UTC.equals(pattern)) {
            //UTC时间 2020-05-12T01:56:15.890Z**
            format = new SimpleDateFormat(DateType.UTC, Locale.ENGLISH);
        }

        return format.format(new Date(timeMillis));
    }

    /**
     * @param pattern 模式
     * @param locale  语言环境
     * @author kewei
     * @date 2023/01/07
     * @params @param timeMillis 时间戳
     * @doc 得到str日期
     */
    public static String getStrDate(Long timeMillis, String pattern, Locale locale ) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, locale);

        if(DateType.GMT.equals(pattern)){
            //GMT时间  Mon, 11 May 2020 07:21:02 GMT
            format = new SimpleDateFormat(DateType.GMT, Locale.ENGLISH);
        }if (DateType.UTC.equals(pattern)) {
            //UTC时间 2020-05-12T01:56:15.890Z**
            format = new SimpleDateFormat(DateType.UTC, Locale.ENGLISH);
        }

        return format.format(new Date(timeMillis));
    }

    /**
     * @param pattern 模式
     * @author kewei
     * @date 2023/01/07
     * @params @param timeMillis 时间戳
     * @doc 得到str日期
     */
    public static String getStrDate(Long timeMillis, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        return format.format(new Date(timeMillis));
    }


    public static void main(String[] args) {
        long timeMillis = getTimeMillis("Wed, 07 Jan 2022 03:00:00 GMT", DateType.GMT);
        long timeMil = getTimeMillisGMT("Wed, 07 Jan 2020 03:00:00 GMT" );
        long timeMillis1 = getTimeMillis("2020-01-07T03:00:00.000Z", DateType.UTC);
        long timeMillis2 = getTimeMillis("2020-01-07 11:00:00", DateType.DEFAULT);
        long timeMillis3 = getTimeMillis("2020-01-07", DateType.DATE);
        long timeMillis4 = getTimeMillis("2020-01-07 11:00:00", DateType.DATE_TIME);

        System.out.println(getSimpleStrDate(timeMillis));
        System.out.println(getStrDate(timeMillis, DateType.GMT,Locale.ENGLISH,-DateType.CHINA));
        System.out.println(getStrDate(timeMillis, DateType.GMT,Locale.ENGLISH));


        System.out.println(getSimpleStrDate(timeMil));
        System.out.println(getSimpleStrDate(timeMillis1));
        System.out.println(getSimpleStrDate(timeMillis2));
        System.out.println(getSimpleStrDate(timeMillis3));
        System.out.println(getSimpleStrDate(timeMillis4));



        System.out.println(timeMillis);
        System.out.println(getSimpleStrDate(timeMillis, Locale.getDefault()));

        //2020-05-12T01:56:15.890Z
        long timeMillisUTC = getTimeMillisUTC("2020-05-12T01:56:15.890Z");
        System.out.println(getSimpleStrDate(timeMillisUTC));
        System.out.println(timeMillisUTC);


    }




    //






}
