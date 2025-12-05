package com.mllfjn.simyys.character.yuhun;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.yuhun.list.*;
import com.mllfjn.simyys.utils.PicUtil;
import com.mllfjn.simyys.utils.Utils;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class YuHunFactory {
    public static final double ICON_SIZE = 30;
    public static final double ICON_RADIUS = ICON_SIZE / 2;
    private static final Map<String, Image> iconMap = new HashMap<>();
    private static final Map<String, Class<? extends YuHun>> yuHunMap = new HashMap<>();

    static {
        yuHunMap.put(HuoLing.YuHunName, HuoLing.class);
        yuHunMap.put(KuangGu.YuHunName, KuangGu.class);
        yuHunMap.put(TuZhiZhu.YuHunName, TuZhiZhu.class);
        yuHunMap.put(DiZhenNian.YuHunName, DiZhenNian.class);
        yuHunMap.put(QingNvFang.YuHunName, QingNvFang.class);
        yuHunMap.put(ShangHunNiao.YuHunName, ShangHunNiao.class);
        yuHunMap.put(HuoZhiChe.YuHunName, HuoZhiChe.class);
    }

    public static Image getImage(String name) {
        Image image;
        if (iconMap.containsKey(name)) {
            image = iconMap.get(name);
        } else {
            image = PicUtil.loadImage(YuHunFactory.class, "", name, ".png", ICON_SIZE);
            iconMap.put(name, image);
        }
        return image;
    }

    public static <T extends YuHun> Optional<T> getYuHun(String name, Character character) {
        if (yuHunMap.containsKey(name)) {
            return getYuHun(yuHunMap.get(name), character);
        }

        return Optional.empty();
    }

    public static <T extends YuHun> Optional<T> getYuHun(Class<? extends YuHun> yClass, Character character) {
        try {
            @SuppressWarnings("unchecked")
            T yuHun = (T) yClass.getDeclaredConstructor().newInstance();
            yuHun.init(character);
            yClass.cast(yuHun);
            return Optional.of(yuHun);
        } catch (Exception e) {
            Utils.throwException("加载御魂失败", e);
        }
        return Optional.empty();
    }
}