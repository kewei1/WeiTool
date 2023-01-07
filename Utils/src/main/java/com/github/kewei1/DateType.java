package com.github.kewei1;

public interface DateType {
    public static final String GMT = "EEE, dd MMM yyyy hh:mm:ss 'GMT'";

    public static final String  UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String  DEFAULT = "yyyy-MM-dd HH:mm:ss";

    public static final String  DATE = "yyyy-MM-dd";

    public static final String  DATE_TIME = "yyyy-MM-dd HH:mm:ss";



    public static final int ONE_MINUTE = 60*1000;
    public static final int ONE_HOUR   = 60*ONE_MINUTE;
    public static final int ONE_DAY    = 24*ONE_HOUR;
    public static final int CHINA    = 8*ONE_HOUR;

}
