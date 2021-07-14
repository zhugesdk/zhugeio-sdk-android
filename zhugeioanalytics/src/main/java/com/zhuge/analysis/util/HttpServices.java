package com.zhuge.analysis.util;

import android.net.Uri;

import com.zhuge.analysis.stat.Constants;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 请求服务器，传送数据
 * Created by Omen on 15/11/13.
 */
public class HttpServices {

    private static final String TAG = "ZhugeSDK.Http";

    private NullHostNameVerifier mVerifier;


    public HttpServices(){
        mVerifier = new NullHostNameVerifier();
    }

    private SSLSocketFactory getSSLFactory() {
        SSLSocketFactory foundSSLFactory;
        try {
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new X509TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            }, new SecureRandom());
            foundSSLFactory = sslContext.getSocketFactory();
        } catch (final GeneralSecurityException e) {
            ZGLogger.handleException("Zhuge.Http", "System has no SSL support.", e);
            foundSSLFactory = null;
        }
        return foundSSLFactory;
    }

    class NullHostNameVerifier implements HostnameVerifier{

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    public byte[] sendRequest(String url, String backUrl, byte[] data){
        byte[] response = null;
        InputStream in = null;
        OutputStream out = null;
        BufferedOutputStream bout = null;
        HttpURLConnection connection = null;
        int retry = 0;
        boolean success = false;
        while (!success && retry < 3) {
            try {
                String apiUrl;
                if (null != backUrl && retry > 1) {
                    apiUrl = Constants.API_PATH_BACKUP;
                } else {
                    apiUrl = url;
                }
                ZGLogger.logMessage(TAG,"attempt request to :"+apiUrl);
                URL remoteURL = new URL(apiUrl);
                connection = (HttpURLConnection) remoteURL.openConnection();
                if (connection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) connection).setSSLSocketFactory(getSSLFactory());
                    ((HttpsURLConnection) connection).setHostnameVerifier(mVerifier);
                }
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(30000);
                if (data != null){
                    connection.setFixedLengthStreamingMode(data.length);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    out = connection.getOutputStream();
                    bout = new BufferedOutputStream(out);
                    bout.write(data);
                    bout.flush();
                    bout.close();
                    bout = null;
                    out.close();
                    out = null;
                }
                in = connection.getInputStream();
                response = slurp(in);
                in.close();
                in = null;
                success = true;
            } catch (final Exception e) {
                ZGLogger.handleException(TAG, "上传数据失败 , url :"+url,e);
                retry++;
            } finally {
                if (null != bout)
                    try {
                        bout.close();
                    } catch (final IOException e) {
                        ZGLogger.logMessage(TAG, "流关闭出错" + e.getMessage());
                    }
                if (null != out)
                    try {
                        out.close();
                    } catch (final IOException e) {
                        ZGLogger.logMessage(TAG, "流关闭出错" + e.getMessage());
                    }
                if (null != in)
                    try {
                        in.close();
                    } catch (final IOException e) {
                        ZGLogger.logMessage(TAG, "流关闭出错" + e.getMessage());
                    }
                if (null != connection)
                    connection.disconnect();
            }
        }
        if (retry >= 3) {
            ZGLogger.logMessage(TAG, "重连三次仍然出错");
        }
        return response;
    }

    public byte[] requestApi(String url, String backUrl , Map<String, Object> params) {
        byte[] response = null;
        InputStream in = null;
        OutputStream out = null;
        BufferedOutputStream bout = null;
        HttpURLConnection connection = null;
        int retry = 0;
        boolean success = false;
        while (!success && retry < 3) {
            try {
                String apiUrl;
                if (retry > 1 && backUrl!= null) {
                    apiUrl = backUrl;
                } else {
                    apiUrl = url;
                }
                ZGLogger.logMessage(TAG,"retry "+retry+" : attempt request to :"+apiUrl);
                URL remoteURL = new URL(apiUrl);
                connection = (HttpURLConnection) remoteURL.openConnection();
                if (connection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) connection).setSSLSocketFactory(getSSLFactory());
                    ((HttpsURLConnection) connection).setHostnameVerifier(mVerifier);
                }
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(30000);
                if (null != params) {
                    Uri.Builder builder = new Uri.Builder();
                    for (Map.Entry<String, Object> param : params.entrySet()) {
                        builder.appendQueryParameter(param.getKey(), param.getValue().toString());
                    }
                    byte[] query = builder.build().getEncodedQuery().getBytes("UTF-8");

                    connection.setFixedLengthStreamingMode(query.length);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    out = connection.getOutputStream();
                    bout = new BufferedOutputStream(out);
                    bout.write(query);
                    bout.flush();
                    bout.close();
                    bout = null;
                    out.close();
                    out = null;
                }
                in = connection.getInputStream();
                response = slurp(in);
                in.close();
                in = null;
                success = true;
            } catch (final Exception e) {
                ZGLogger.handleException(TAG, "上传数据出错："+e.getMessage(),e);
                retry++;
            } finally {
                if (null != bout)
                    try {
                        bout.close();
                    } catch (final IOException e) {
                        ZGLogger.logMessage(TAG, "流关闭出错" + e.getMessage());
                    }
                if (null != out)
                    try {
                        out.close();
                    } catch (final IOException e) {
                        ZGLogger.logMessage(TAG, "流关闭出错" + e.getMessage());
                    }
                if (null != in)
                    try {
                        in.close();
                    } catch (final IOException e) {
                        ZGLogger.logMessage(TAG, "流关闭出错" + e.getMessage());
                    }
                if (null != connection)
                    connection.disconnect();
            }
        }
        if (retry >= 3) {
            ZGLogger.logMessage(TAG, "重连三次仍然出错");
        }
        return response;
    }


    private static byte[] slurp(final InputStream inputStream)
            throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[512];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
}
