package com.github.cookiesjuice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.service.SetuService;
import com.github.cookiesjuice.util.ConfigInfo;
import com.github.cookiesjuice.util.HttpUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SetuServiceImpl implements SetuService {
    /**
     * 涩图根目录
     */
    public final String SETU_PATH = "E:/setu";

    /**
     * 涩图总数量
     */
    public int imgLen;

    /**
     * 配置文件json对象
     */
    public JSONObject configObject;

    /**
     * 读取并初始化配置文件
     */
    public SetuServiceImpl() {
        File file = new File(SETU_PATH + "/config");
        FileInputStream fi = null;
        FileOutputStream fo = null;
        try {
            if (!file.exists()) {
                JSONObject object = new JSONObject();
                JSONObject config = new JSONObject();
                config.put("imgLen", 0);
                object.put("config", config);
                fo = new FileOutputStream(file);
                fo.write(object.toJSONString().getBytes());
                fo.flush();
            }
            fi = new FileInputStream(file);
            StringBuilder fileStr = new StringBuilder();
            byte[] buffer = new byte[512];
            int len;
            while ((len = fi.read(buffer)) != -1) {
                fileStr.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
            }
            configObject = JSON.parseObject(fileStr.toString());
            JSONObject config = configObject.getJSONObject("config");

            imgLen = config.getInteger("imgLen");
            System.out.println("SeTu Mod init finish...imgLen=" + imgLen);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fi != null) {
                try {
                    fi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fo != null) {
                try {
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从涩图库中随机获得涩图文件
     *
     * @return 涩图文件
     */
    public File randomSeTuFile() {
        String setuLibPath = SETU_PATH + "/img";
        File setuLib = new File(setuLibPath);
        if (!setuLib.exists()) return null;

        String[] setuPaths = setuLib.list();
        if (setuPaths != null && setuPaths.length > 0) {
            int idx = (int) (Math.random() * setuPaths.length); //库中随机一个文件
            String setuPath = setuPaths[idx];
            File setuFile = new File(setuLibPath + "/" + setuPath);
            if (setuFile.exists()) return setuFile; //如果涩图文件存在则返回
        }

        return null;
    }

    /**
     * 爬取最新涩图
     */
    public void updateSeTu() {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        WebDriver driver = null;
        try {
            driver = new ChromeDriver();  //创建浏览器驱动

            WebDriverWait wait = new WebDriverWait(driver, 10);

            String url = "https://user.qzone.qq.com/570185461/photo/V118cnBA0s01ub/";

            driver.get(url);

            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login_frame")));   //等待登录frame加载
            driver.switchTo().frame("login_frame"); //切入qq登录frame

            wait.until(ExpectedConditions.elementToBeClickable(By.id("switcher_plogin")));    //等待 "账号密码登录" 可点击

            driver.findElement(By.id("switcher_plogin")).click();   //点击 "账号密码登录"

            //输入账号密码并登录，这里登录的qq号需要有访问空间相册的权限
            WebElement qqid = wait.until(ExpectedConditions.elementToBeClickable(By.name("u")));
            qqid.sendKeys(ConfigInfo.config.getString("service_setu_id"));
            WebElement pwd = wait.until(ExpectedConditions.elementToBeClickable(By.name("p")));
            pwd.sendKeys(ConfigInfo.config.getString("service_setu_password"));
            WebElement searchButton = driver.findElement(By.id("login_button"));
            searchButton.click();

            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tphoto")));   //等待相册frame加载
            driver.switchTo().frame("tphoto"); //切入相册frame
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("j-pl-photoitem-img")));  //等待涩图加载

            WebElement j_pl_photoitem_img = driver.findElement(By.className("j-pl-photoitem-img"));
            j_pl_photoitem_img.click();
            driver = driver.switchTo().parentFrame();

            System.out.println(driver.getPageSource());
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("device-info")));  //等待查看涩图获得信息区加载
            WebElement device_info = driver.findElement(By.className("device-info"));   //获得信息区
            WebElement info_span = device_info.findElement(By.tagName("span"));    //获得信息span
            String title = info_span.getAttribute("title");
            String[] titles = title.split("/");
//            int now = Integer.parseInt(titles[0]);
            int sum = Integer.parseInt(titles[1]);
            //对比上次保存总数量，爬取新增的涩图
            for (int i = imgLen; i < sum; i++) {
                WebElement js_img_border = driver.findElement(By.id("js-img-border"));
                WebElement img = js_img_border.findElement(By.tagName("img"));
                String src = img.getAttribute("src");

                wait.until(ExpectedConditions.elementToBeClickable(By.id("js-btn-nextPhoto")));
                WebElement js_btn_nextPhoto = driver.findElement(By.id("js-btn-nextPhoto"));
                js_btn_nextPhoto.click();

                String name = src.substring(src.lastIndexOf("/")) + ".jpg";
                name = name.replaceAll("[\\s\\\\/:*?\"<>|]", "");

                src = src.substring(0, src.lastIndexOf("/")) + "/r";
                System.out.println(src);

                String path = SETU_PATH + "/img/" + name;
                File file = new File(path);
                if (!file.exists()) {
                    HttpUtils.downloadFile(src, path);
                    imgLen++;
                    saveConfig();
                }
            }
        } catch (Exception e) {
            if (driver != null) {
                driver.quit();
            }
            throw e;
        }

        driver.quit();
    }

    /**
     * 保存配置文件
     */
    private void saveConfig() {
        File file = new File(SETU_PATH + "/config");
        FileOutputStream fo = null;
        try {
            JSONObject config = configObject.getJSONObject("config");
            config.put("imgLen", imgLen);
            fo = new FileOutputStream(file);
            fo.write(configObject.toJSONString().getBytes());
            fo.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fo != null) {
                try {
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new SetuServiceImpl().updateSeTu();
    }

}
