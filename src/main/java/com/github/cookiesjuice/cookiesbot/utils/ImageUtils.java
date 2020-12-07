package com.github.cookiesjuice.cookiesbot.utils;

import org.xhtmlrenderer.swing.Java2DRenderer;
import org.xhtmlrenderer.util.FSImageWriter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ImageUtils {
    /**
     * 将html转为图片
     *
     * @param htmlFile html文件
     * @param width    宽度
     * @param height   高度
     * @return 转换后的图片文件
     */
    public static File htmlToImage(File htmlFile, int width, int height) {
        File path = new File("temp");
        if (!path.exists()) {
            path.mkdir();
        }
        String filename = path + "/" + UUID.randomUUID() + ".png";
        try {
            if (!htmlFile.exists()) {
                throw new IOException("The input file does not exists!");
            }
            Java2DRenderer renderer = new Java2DRenderer(htmlFile, width, height);
            BufferedImage image = renderer.getImage();
            FSImageWriter writer = new FSImageWriter();
            writer.setWriteCompressionQuality(0.9f);
            writer.write(image, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(filename);
        return file.exists() ? file : null;
    }

    /**
     * 将二维数组转换为html_table样式的图片
     *
     * @param lists lists
     * @return 转换后的图片文件
     */
    public static File arrToTabImage(List<List<String>> lists) {
        return arrToTabImage(lists, null);
    }

    /**
     * 将二维数组转换为html_table样式的图片
     *
     * @param lists        lists
     * @param tableContent 自定义的table内容(tr th td)
     * @return 转换后的图片文件
     */
    public static File arrToTabImage(List<List<String>> lists, String tableContent) {
        String[][] array = new String[lists.size()][];

        for (int i = 0; i < lists.size(); i++) {
            array[i] = lists.get(i).toArray(new String[]{});
        }

        return arrToTabImage(array, tableContent);
    }

    /**
     * 将二维数组转换为html_table样式的图片
     *
     * @param arr arr
     * @return 转换后的图片文件
     */
    public static File arrToTabImage(String[][] arr) {
        return arrToTabImage(arr, null);
    }

    /**
     * 将二维数组转换为html_table样式的图片
     *
     * @param arr          arr
     * @param tableContent 自定义的table内容(tr th td)
     * @return 转换后的图片文件
     */
    public static File arrToTabImage(String[][] arr, String tableContent) {
        if (arr.length == 0) return null;

        int margin = 20; //table外边距
        int border = 1; //边框宽度
        double charW = 16; //一个字符的宽度
        int charH = 20; //一个字符的高度
        int imageW = margin + border; //图片宽度
        int imageH = margin + border; //图片高度

        int maxCharCount = 0; //最多字符的行,字符总个数
        StringBuilder trTh = new StringBuilder("\t<tr>\n");
        for (String h : arr[0]) {
            maxCharCount += h.length();
            trTh.append("\t\t<th>").append(h).append("</th>\n");
        }
        trTh.append("\t</tr>\n");

        StringBuilder trTd = new StringBuilder();
        for (int i = 1; i < arr.length; i++) {
            int charCount = 0; //当前行字符总个数
            trTd.append("\t<tr>\n");
            for (String v : arr[i]) {
                if (v.isEmpty()) {
                    v = " ";
                }
                charCount += v.length();
                trTd.append("\t\t<td>").append(v).append("</td>\n");
            }
            if (charCount > maxCharCount) {
                maxCharCount = charCount;
            }
            trTd.append("\t</tr>\n");
        }

        imageW += charW * (maxCharCount + 1) + (border + 2) * (arr[0].length - 1); //最长的一行字符宽度 + 边框宽度 + 字与边框的距离
        imageH += charH * arr.length + (border + 2) * (arr.length - 1); //总行数高度 + 边框高度 + 字与边框的距离

        String html = "<html lang=\"zh\">\n" +
                "<head>\n" +
                "    <style type=\"text/css\">\n" +
                "        table {\n" +
                "            border-collapse:collapse;text-align: center;background: #ccc;margin: auto;font-size: 16px;\n" +
                "        }\n" +
                "        th {\n" +
                "            background: #eee;font-weight: normal;border: 1px solid #ccc;\n" +
                "        }\n" +
                "        td {\n" +
                "            background: #fff;color: #06f;border: 1px solid #ccc;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>" +
                "<body>\n" +
                "<table>\n" + (tableContent != null ? tableContent : trTh.toString() + trTd) + "</table>\n" +
                "</body>\n" +
                "</html>";
        File path = new File("temp");
        if (!path.exists()) {
            path.mkdir();
        }
        String filename = path + "/" + UUID.randomUUID() + ".html";
        try {
            FileOutputStream out = new FileOutputStream(new File(filename));
            out.write(html.getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File temp = new File(filename);
        File ret = htmlToImage(temp, imageW, imageH);
        if (!temp.delete()) {
            System.out.println("Temporary file[" + filename + "] deletion failed!");
        }

        return ret;
    }
}
