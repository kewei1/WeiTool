package main.java.com.github.kewei1.xxlJob;




import okhttp3.*;


import java.io.IOException;
import java.util.concurrent.TimeUnit;



public class XxlJobUtils {

//    private static final Logger logger = LoggerFactory.getLogger(XxlJobUtils.class);

    private final static OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .build();

    private final static String BASE_URL = "http://192.168.1.75:8080";
    private final static String LOGIN_URL = "/xxl-job-admin/login";
    private final static String JOBLOG_URL = "/xxl-job-admin/joblog/pageList";
    private final static String JOBINFO_URL = "/xxl-job-admin/jobinfo/pageList";
    private final static String CHARTINFO_URL = "/xxl-job-admin/chartInfo";
    private final static String JOBGROUP_URL = "/xxl-job-admin/jobgroup/pageList";
    private final static String USERNAME = "admin";
    private final static String PASSWORD = "hsh2021";
    public final static int Faild = 2;
    public final static int Success = 1;
    public final static int Running = 3;
    public final static int ALL = -1;


    private  static String COOKIE = "";

    private final static MediaType mediaType = MediaType.parse("multipart/form-data; boundary=---011000010111000001101001") ;
    private final static RequestBody body = RequestBody.create(mediaType, "-----011000010111000001101001--\r\n\r\n");

    static {
        Request request = new Request.Builder()
                .url(BASE_URL+LOGIN_URL+"?userName="+ USERNAME+"+&password="+PASSWORD)
                .post(body)
                .addHeader("content-type", "multipart/form-data; boundary=---011000010111000001101001")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            COOKIE = response.headers().get("Set-Cookie").split(";")[0];
        } catch (IOException e) {
//            logger.error("初始化 xxx-job 监控 信息失败",e);
        }
    }


    public static String getJobLog(int jobGroup, int jobId, int logStatus , int start, int length, String jobStartTime, String jobEndTime) {
        Request request = new Request.Builder()
                .url(BASE_URL+JOBLOG_URL+"?jobGroup="+jobGroup+"&jobId="+jobId+"&logStatus="+logStatus+"&filterTime="+jobStartTime.replace(" ","+")+"+-+"+jobEndTime.replace(" ","+")+"&start="+start+"&length="+length)
                .post(body)
                .addHeader("cookie", COOKIE)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();

            return response.body().string();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String getChartinfoUrlhartInfo(String startDate, String endDate) {
        Request request = new Request.Builder()
                .url(BASE_URL+CHARTINFO_URL+"?startDate="+startDate.replace(" ","+")+"&endDate="+endDate.replace(" ","+"))
                .post(body)
                .addHeader("cookie", COOKIE)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();

            return response.body().string();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //
    public static String getJobgroup(String start, String length) {
        Request request = new Request.Builder()
                .url(BASE_URL+JOBGROUP_URL+"?start="+start+"&length="+length)
                .post(body)
                .addHeader("cookie", COOKIE)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();

            return response.body().string();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public static String getJobInfo() {
        Request request = new Request.Builder()
                .url(BASE_URL+JOBINFO_URL+"?triggerStatus=-1&start=0&length=1000&jobGroup=0")
                .post(body)
                .addHeader("cookie", COOKIE)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();

            return response.body().string();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}