package com.github.cookiesjuice.cookiesbot.util;

import org.apache.http.Consts;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Map;

/**
 * Http客户端工具
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 下载文件到指定目录
     *
     * @param srcUrl   源路径
     * @param savePath 保存目录
     */
    public static void downloadFile(String srcUrl, String savePath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(savePath);
            byte[] data = doGet(srcUrl);
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发起Get请求
     *
     * @param httpUrl 请求链接
     * @return 响应数据
     */
    public static byte[] doGet(String httpUrl) {
        logger.debug("[doGet]->httpUrl={}", httpUrl);
        HttpURLConnection connection = null;
        byte[] result = null; // 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();

            if (connection.getResponseCode() == 200) {
                result = getRespBytes(connection.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (connection != null) {
                connection.disconnect();// 关闭远程连接
            }
        }

        return result;
    }

    /**
     * 发起Post请求
     *
     * @param httpUrl 请求链接
     * @param param   参数
     * @return 响应数据
     */
    public static byte[] doPost(String httpUrl, String param) {
        logger.debug("[doPost]->httpUrl={} & body={}", httpUrl, param);
        HttpURLConnection connection = null;
        OutputStream os = null;
        byte[] result = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(150000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(600000);

            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
//            connection.setRequestProperty("Authorization",
//                    "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(param.getBytes());
            if (connection.getResponseCode() == 200) {
                result = getRespBytes(connection.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();// 关闭远程连接
            }
        }
        return result;
    }

    /**
     * 发起multipart/form-data类型Post请求
     *
     * @param httpUrl     请求链接
     * @param textBodys   普通文本参数
     * @param binaryBodys 文件参数
     * @return 响应数据
     */
    public static byte[] doPostByMultipart(String httpUrl, Map<String, String> textBodys, Map<String, File> binaryBodys) {
        byte[] result = null;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //设置浏览器兼容模式
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        //设置请求的编码格式
        builder.setCharset(Consts.UTF_8);
        builder.setContentType(ContentType.MULTIPART_FORM_DATA);
        textBodys.forEach(builder::addTextBody);
        binaryBodys.forEach(builder::addBinaryBody);

        HttpPost httpPost = new HttpPost(httpUrl);
        httpPost.setEntity(builder.build());

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            result = getRespBytes(httpResponse.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * md5加密
     *
     * @param s 要加密的字符串
     * @return 加密后的字符串
     */
    public static String md5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取连接的响应字节流
     *
     * @param is 输入流
     * @return 响应字节
     */
    private static byte[] getRespBytes(InputStream is) {
        ByteArrayOutputStream bos = null;
        byte[] result = null;
        try {
            // 通过连接对象获取一个输入流，向远程读取
            bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            result = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
