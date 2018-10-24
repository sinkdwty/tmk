package com.hjzddata.modular.common.model;

import com.hjzddata.core.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * <p>
 * <date>2012-03-01</date><br/>
 * <span>软维提供的JAVA接口信息（短信，彩信）调用API</span><br/>
 * <span>----------基础访问方法-------------</span>
 * </p>
 *
 * @author LIP
 * @version 1.0.1
 */
public class SmsClientAccessTool {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private static SmsClientAccessTool smsClientToolInstance;

    /**
     * 采用单列方式来访问操作
     *
     * @return
     */
    public static synchronized SmsClientAccessTool getInstance() {

        if (smsClientToolInstance == null) {
            smsClientToolInstance = new SmsClientAccessTool();
        }
        return smsClientToolInstance;
    }

    /**
     * <p>
     * POST方法
     * </p>
     *
     * @param sendUrl       ：访问URL
     * @param sendParam     ：参数串
     * @param backEncodType ：返回的编码
     * @return
     */
    public String doAccessHTTPPost(String sendUrl, String sendParam, String backEncodType) {

        StringBuffer receive = new StringBuffer();
        BufferedWriter wr = null;
        try {
            if (backEncodType == null || backEncodType.equals("")) {
                backEncodType = "UTF-8";
            }

            URL url = new URL(sendUrl);
            HttpURLConnection URLConn = (HttpURLConnection) url.openConnection();
            URLConn.setDoOutput(true);
            URLConn.setDoInput(true);
            ((HttpURLConnection) URLConn).setRequestMethod("POST");
            URLConn.setUseCaches(false);
            URLConn.setAllowUserInteraction(true);
            HttpURLConnection.setFollowRedirects(true);
            URLConn.setInstanceFollowRedirects(true);
            URLConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            URLConn.setRequestProperty("Content-Length", String.valueOf(sendParam.getBytes().length));
            URLConn.getResponseMessage();

            URLConn.setDoOutput(true);
            // 发送域信息
            OutputStreamWriter out = new OutputStreamWriter(URLConn
                    .getOutputStream(), backEncodType);

            out.write(sendParam);

            BufferedReader rd = new BufferedReader(new InputStreamReader(URLConn.getInputStream(), backEncodType));
            String line;
            while ((line = rd.readLine()) != null) {
                receive.append(line).append("\r\n");
            }
            rd.close();
            if (log.isInfoEnabled()) {
                log.info("http post应答报文:" + receive.toString());
            }
        } catch (java.io.IOException e) {
            receive.append("访问产生了异常-->").append(e.getMessage());
            if (log.isInfoEnabled()) {
                log.info(receive.toString());
            }
            e.printStackTrace();
        } finally {
            try {
                ToolUtil.closeObject(wr);
            } catch (Exception ex) {
                if (log.isErrorEnabled())
                    log.error("流对象关闭异常" + ex.getMessage());
            }
            wr = null;
        }
        return receive.toString();
    }

    public String doAccessHTTPGet(String sendUrl, String backEncodType) {

        StringBuffer receive = new StringBuffer();
        BufferedReader in = null;
        try {
            if (backEncodType == null || backEncodType.equals("")) {
                backEncodType = "UTF-8";
            }

            URL url = new URL(sendUrl);
            HttpURLConnection URLConn = (HttpURLConnection) url
                    .openConnection();

            URLConn.setDoInput(true);
            URLConn.setDoOutput(true);
            URLConn.connect();
            URLConn.getOutputStream().flush();
            in = new BufferedReader(new InputStreamReader(URLConn
                    .getInputStream(), backEncodType));

            String line;
            while ((line = in.readLine()) != null) {
                receive.append(line).append("\r\n");
            }

        } catch (IOException e) {
            receive.append("访问产生了异常-->").append(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                ToolUtil.closeObject(in);
            } catch (Exception ex) {
                if (log.isErrorEnabled())
                    log.error("流对象关闭异常" + ex.getMessage());
            }
            in = null;
        }

        return receive.toString();
    }

    /**
     * 真实使用的执行发送方法
     * 原post方法使用2进制转化，导致发送汉字乱码
     *
     * @param
     */
    public String sendPost(String sendUrl, String params, String outEncoding) {

        String retMsg = "";
        BufferedReader reader = null;
        OutputStreamWriter outs = null;
        try {

            URL url = new URL(sendUrl);
            URLConnection connection = url.openConnection();

            //***********************************//
            connection.setDoInput(true);
            ((HttpURLConnection) connection).setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(true);
            HttpURLConnection.setFollowRedirects(true);

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(params.getBytes().length));

            connection.setDoOutput(true);
            // 发送域信息
            outs = new OutputStreamWriter(connection
                    .getOutputStream(), outEncoding);

            outs.write(params);
            outs.flush();

            // 获取返回数据
            if (log.isInfoEnabled()) {
                log.info("开始获取http post 应答报文");
            }
            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            retMsg = buffer.toString();
            if (log.isInfoEnabled()) {
                log.info("获取http post 应答报文: " + retMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMsg = "reuid_error";
        } finally {
            try {
                ToolUtil.closeObject(reader);
                ToolUtil.closeObject(outs);
            } catch (Exception ex) {
                if (log.isErrorEnabled())
                    log.error("流对象关闭异常" + ex.getMessage());
            }

        }
        return retMsg.trim();
    }


}
