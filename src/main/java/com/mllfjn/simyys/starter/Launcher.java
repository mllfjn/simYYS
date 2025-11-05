package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.utils.Utils;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Utils.throwException("发生未知错误", e));
        Application.launch(Initializer.class, args);
    }
}
