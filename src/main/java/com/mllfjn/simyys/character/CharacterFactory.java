package com.mllfjn.simyys.character;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.utils.Utils;
import com.mllfjn.simyys.character.mob.shenqilou.ShenQiLou;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.sp.dayuan.DaYuan;
import com.mllfjn.simyys.character.sp.shenshe.ShenShe;
import com.mllfjn.simyys.character.ssr.namei.NaMei;
import com.mllfjn.simyys.character.ssr.qianji.QianJi;
import com.mllfjn.simyys.character.ssr.xiaoyuan.XiaoYuan;
import com.mllfjn.simyys.character.yys.shenle.ShenLe;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class CharacterFactory {
    /*public static final String[] characterListYYS = {"晴明", "神乐", "八百比丘尼", "源博雅"};
    public static final String[] characterListSP = {"纺愿缘结神", "神堕八岐大蛇", "因幡辉夜姬", "浮世青行灯"};
    public static final String[] characterListSSR = {"伊邪那美", "天照", "千姬", "缘结神"};
    public static final String[] characterListSR = {"蝎女"};
    public static final String[] characterListR = {"丑时之女"};
    public static final String[] characterListN = {};
    public static final String[] characterListMob = {"鬼灵歌姬", "蜃气楼", "土蜘蛛", "荒骷髅", "地震鲶", "胧车", "达摩"};
    public static final String[][] characterList = new String[][]{characterListYYS, characterListSP, characterListSSR, characterListSR, characterListR, characterListN, characterListMob};
    public static final String[] characterType = new String[]{"阴阳师", "SP", "SSR", "SR", "R", "N", "怪物"};*/

    public static final Map<String, Map<String, Class<? extends Character>>> characterMap = new LinkedHashMap<>();
    static {
        Map<String, Class<? extends Character>> mapYYS = new LinkedHashMap<>();
        characterMap.put("阴阳师", mapYYS);
        mapYYS.put(ShenLe.privateName, ShenLe.class);

        Map<String, Class<? extends Character>> mapSP = new LinkedHashMap<>();
        characterMap.put("SP", mapSP);
        mapSP.put(DaYuan.privateName, DaYuan.class);
        mapSP.put(ShenShe.privateName, ShenShe.class);

        Map<String, Class<? extends Character>> mapSSR = new LinkedHashMap<>();
        characterMap.put("SSR", mapSSR);
        mapSSR.put(NaMei.privateName, NaMei.class);
        mapSSR.put(QianJi.privateName, QianJi.class);
        mapSSR.put(XiaoYuan.privateName, XiaoYuan.class);

        Map<String, Class<? extends Character>> mapMob = new LinkedHashMap<>();
        characterMap.put("怪物", mapMob);
        mapMob.put(ShenQiLou.privateName, ShenQiLou.class);
    }

    public static Character getCharacter(String name) {
        for (Map<String, Class<? extends Character>> map : characterMap.values()) {
            if (map.containsKey(name)) {
                try {
                    return map.get(name).getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    Utils.throwException("获取角色信息失败", e);
                }

            }
        }
        return null;
    }

    public static Character getCharacter(PropertiesHolder ph, BattlePane bp) {
        Character character = getCharacter(ph.name);
        if (character != null) {
            character.init(ph.map, bp);
            character.name = ph.name;
            character.addSkills();
            return character;
        }
        return null;
    }

    public static PropertiesMap getProperties(String name) {
        Character character = getCharacter(name);
        if (character != null) {
            return character.getProperties();
        }
        return null;
    }

    public static Pane getImageByName(String name, ImageSize size, Paint color, double strokeWidth) {
        URL url = CharacterFactory.class.getResource("images/" + name + ".png");
        ImageView imageView = new ImageView(new Image(String.valueOf(url)));
        imageView.setFitWidth(size.size);
        imageView.setFitHeight(size.size);

        double radius = size.size / 2;
        Circle border = new Circle(radius, radius, radius);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(color);
        border.setStrokeWidth(strokeWidth);

        StackPane container = new StackPane(border, imageView);
        container.setPadding(new Insets(strokeWidth));

        return container;
    }

    public enum ImageSize {
        LARGE(140),
        BIG(120),
        SMALL(75);

        public final double size;
        ImageSize(double size) {
            this.size = size;
        }
    }
}
