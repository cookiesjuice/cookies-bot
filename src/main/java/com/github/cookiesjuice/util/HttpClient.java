package com.github.cookiesjuice.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Http客户端工具
 */
public class HttpClient {

    /**
     * 下载文件到指定目录
     *
     * @param srcurl   源路径
     * @param savepath 保存目录
     */
    public static void downloadFile(String srcurl, String savepath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(savepath);
            byte[] datas = doGet(srcurl);
            fos.write(datas);
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
     * @param httpurl 请求链接
     * @return 响应数据
     */
    public static byte[] doGet(String httpurl) {
        HttpURLConnection connection = null;
        byte[] result = null; // 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
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

            result = getRespBytes(connection);
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
            result = getRespBytes(connection);
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
     * 读取连接的响应字节流
     *
     * @param connection 连接
     * @return 响应字节
     */
    private static byte[] getRespBytes(HttpURLConnection connection) {
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        byte[] result = null;
        try {
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                bos = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                result = bos.toByteArray();
            }
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
