package com.mllfjn.simyys.character;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.list.r.chounv.ChouNv;
import com.mllfjn.simyys.character.list.sp.laotou.LaoTou;
import com.mllfjn.simyys.character.list.ssr.shiling.ShiLing;
import com.mllfjn.simyys.character.list.ssr.xuzuo.XuZuo;
import com.mllfjn.simyys.character.list.yys.yuanlaiguang.YuanLaiGuang;
import com.mllfjn.simyys.utils.PicUtil;
import com.mllfjn.simyys.utils.Utils;
import com.mllfjn.simyys.character.list.mob.jifengmo.shenqilou.ShenQiLou;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.list.sp.dayuan.DaYuan;
import com.mllfjn.simyys.character.list.sp.shenshe.ShenShe;
import com.mllfjn.simyys.character.list.ssr.namei.NaMei;
import com.mllfjn.simyys.character.list.ssr.qianji.QianJi;
import com.mllfjn.simyys.character.list.yys.shenle.ShenLe;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class CharacterFactory {
    public static final Map<String, Map<String, Class<? extends Character>>> characterMap = new LinkedHashMap<>();
    private static final Map<String, Image> iconMap = new HashMap<>();
    static {
        Map<String, Class<? extends Character>> mapYYS = new LinkedHashMap<>();
        characterMap.put("阴阳师", mapYYS);
        mapYYS.put(ShenLe.CharacterName, ShenLe.class);
        mapYYS.put(YuanLaiGuang.CharacterName, YuanLaiGuang.class);

        Map<String, Class<? extends Character>> mapSP = new LinkedHashMap<>();
        characterMap.put("SP", mapSP);
        mapSP.put(DaYuan.CharacterName, DaYuan.class);
        mapSP.put(ShenShe.CharacterName, ShenShe.class);
        mapSP.put(LaoTou.CharacterName, LaoTou.class);

        Map<String, Class<? extends Character>> mapSSR = new LinkedHashMap<>();
        characterMap.put("SSR", mapSSR);
        mapSSR.put(NaMei.CharacterName, NaMei.class);
        mapSSR.put(QianJi.CharacterName, QianJi.class);
        mapSSR.put(ShiLing.CharacterName, ShiLing.class);
        mapSSR.put(XuZuo.CharacterName, XuZuo.class);

        Map<String, Class<? extends Character>> mapR = new LinkedHashMap<>();
        characterMap.put("R", mapR);
        mapR.put(ChouNv.CharacterName, ChouNv.class);

        Map<String, Class<? extends Character>> mapMob = new LinkedHashMap<>();
        characterMap.put("怪物", mapMob);
        mapMob.put(ShenQiLou.CharacterName, ShenQiLou.class);
    }

    public static Optional<Character> getCharacter(String name) {
        for (Map<String, Class<? extends Character>> map : characterMap.values()) {
            if (map.containsKey(name)) {
                try {
                    return Optional.of(map.get(name).getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    Utils.throwException("获取角色信息失败", e);
                }

            }
        }
        return Optional.empty();
    }

    public static Optional<Character> getCharacter(PropertiesHolder ph, BattlePane bp) {
        Optional<Character> oc = getCharacter(ph.name);
        oc.ifPresent(character -> {
            character.init(ph, bp);
            character.addSkills();
        });
        return oc;
    }

    public static Optional<PropertiesMap> getProperties(String name) {
        return getCharacter(name).map(Character::getProperties);
    }

    public static Node getImage(String name, ImageSize size) {
        Image image;
        if (iconMap.containsKey(name)) {
            image = iconMap.get(name);
        } else {
            image = PicUtil.loadImage(CharacterFactory.class, "shishen/", name, ".png", size.size);
            iconMap.put(name, image);
        }
        return new ImageView(image);
    }

    public static Node getImageWithStroke(String name, ImageSize size, Paint color, double strokeWidth) {
        return PicUtil.clipAndStroke((ImageView) getImage(name, size), size.size, color, strokeWidth);
    }

    public enum ImageSize {
        LARGE(130),
        BIG(110),
        SMALL(70),
        LABEL(35);

        public final double size;
        ImageSize(double size) {
            this.size = size;
        }
    }
}
