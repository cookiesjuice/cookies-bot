package com.github.cookiesjuice.cookiesbot.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 清理文件任务
 */
@Configuration
public class ClearFileTask {

    /**
     * 清理超过一周的日志文件
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void clearLog() {
        File path = new File("log");
        if (path.exists() && path.isDirectory()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH");
            Date now = new Date();
            for (File file : Objects.requireNonNull(path.listFiles())) {
                try {
                    Date date = sdf.parse(file.getName());
                    if (now.getTime() - date.getTime() > 60 * 60 * 24 * 7 * 1000L) {
                        if (!file.delete()) {
                            System.gc();
                            file.delete();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 清理每天的临时文件
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void clearTempFile() {
        File path = new File("temp");
        if (path.exists() && path.isDirectory()) {
            for (File file : Objects.requireNonNull(path.listFiles())) {
                if (!file.delete()) {
                    System.gc();
                    file.delete();
                }
            }
        }
    }
}
