package com.github.kewei1.cert;

import javax.net.ssl.*;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Cert {



    public static X509Certificate getCertExpired(String httpsUrl) {
        X509Certificate x509Certificate = null;
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new Cert().new NullHostNameVerifier());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            URL url = new URL(httpsUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(5000);
            conn.connect();
            Certificate[] certificates= conn.getServerCertificates();
            x509Certificate = (X509Certificate)certificates[0];
            System.out.println("证书版本:" + x509Certificate.getVersion());
            System.out.println("证书编号:" + x509Certificate.getSerialNumber());
            System.out.println("颁发机构:" + x509Certificate.getSubjectDN().getName());
            System.out.println("颁发者:" + x509Certificate.getIssuerDN().getName());
            //DateUtil.parse(str) str -> Date
            System.out.println("证书开始时间:" + formatDate(x509Certificate.getNotBefore().toString()));
            System.out.println("有效期止" + formatDate(x509Certificate.getNotAfter().toString()));
            System.out.println("签名算法:" + x509Certificate.getSigAlgName());
            System.out.println("证书公钥:" + x509Certificate.getPublicKey());
            System.out.println("证书签名:" + x509Certificate.getSignature());
            System.out.println("+++++++++分割线++++++++++");
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  x509Certificate;
    }


    public static String formatDate(String dateStr) throws ParseException {
        DateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = originalFormat.parse(dateStr);
        return targetFormat.format(date);
    }

    // str -> Date


    static TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {


        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }


        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }


        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    } };
    public class NullHostNameVerifier implements HostnameVerifier {

        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    }
}