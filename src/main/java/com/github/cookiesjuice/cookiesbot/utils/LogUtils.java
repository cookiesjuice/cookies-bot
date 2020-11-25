package com.github.cookiesjuice.cookiesbot.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class LogUtils {
    public static boolean setOutToFile(File file) {
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                return false;
            }
        }
        PrintStream printStream = null;
        try {
            PrintStream sysPrint = new PrintStream(System.out);
            printStream = new PrintStream(file) {
                @Override
                public void println() {
                    super.println();
                    sysPrint.println();
                }

                @Override
                public void println(boolean x) {
                    super.println(x);
                    sysPrint.println(x);
                }

                @Override
                public void println(char x) {
                    super.println(x);
                    sysPrint.println(x);
                }

                @Override
                public void println(int x) {
                    super.println(x);
                    sysPrint.println(x);
                }

                @Override
                public void println(long x) {
                    super.println(x);
                    sysPrint.println(x);
                }

                @Override
                public void println(float x) {
                    super.println(x);
                    sysPrint.println(x);
                }

                @Override
                public void println(double x) {
                    super.println(x);
                    sysPrint.println(x);
                }

                @Override
                public void println(@NotNull char[] x) {
                    super.println(x);
                    sysPrint.println(x);
                }

                @Override
                public void println(@Nullable String x) {
                    super.println(x);
                    sysPrint.println(x);
                }

                @Override
                public void println(@Nullable Object x) {
                    super.println(x);
                    sysPrint.println(x);
                }
            };
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (printStream == null) return false;

        System.setOut(printStream);
        return true;
    }
}
