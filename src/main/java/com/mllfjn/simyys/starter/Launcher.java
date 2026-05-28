package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.utils.Utils;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) ->
                Utils.throwException("发生未知错误，出现此提示后所有数据不再可靠！保存配置也最好是新建不要覆盖防止出错！", e));
        Application.launch(Initializer.class, args);
    }
}
