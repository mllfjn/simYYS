package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.utils.Utils;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) ->
                Utils.throwException("发生未知错误，出现此提示后所有数据不再可靠！保存配置也要新建不要覆盖！", e));
        Application.launch(Initializer.class, args);
    }
}
