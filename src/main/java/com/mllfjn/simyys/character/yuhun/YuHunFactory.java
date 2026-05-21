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
    public static final double ICON_SIZE = 25;
    public static final double ICON_RADIUS = ICON_SIZE / 2;
    private static final Map<String, Image> iconMap = new HashMap<>();

    private static final Map<String, Class<? extends YuHun>> yuHunMap = new HashMap<>();
//    public static final Map<String, Map<String, Class<? extends YuHun>>> yuHunMap = new LinkedHashMap<>();

    static {
        /*Map<String, Class<? extends YuHun>> mapAttack = new LinkedHashMap<>();
        yuHunMap.put("攻击两件套", mapAttack);
        mapAttack.put(KuangGu.YuHunName, KuangGu.class);

        Map<String, Class<? extends YuHun>> mapCritRate = new LinkedHashMap<>();
        yuHunMap.put("暴击两件套", mapCritRate);
        mapCritRate.put(QingNvFang.YuHunName, QingNvFang.class);
        mapCritRate.put(ShangHunNiao.YuHunName, ShangHunNiao.class);
        mapCritRate.put(HaiYueHuoYu.YuHunName, HaiYueHuoYu.class);

        Map<String, Class<? extends YuHun>> mapEffectHit = new LinkedHashMap<>();
        yuHunMap.put("命中两件套", mapEffectHit);
        mapEffectHit.put(HuoLing.YuHunName, HuoLing.class);

        Map<String, Class<? extends YuHun>> mapEffectResist = new LinkedHashMap<>();
        yuHunMap.put("抵抗两件套", mapEffectResist);
        mapEffectResist.put(DiaoPingHuo.YuHunName, DiaoPingHuo.class);

        Map<String, Class<? extends YuHun>> mapDefense = new LinkedHashMap<>();
        yuHunMap.put("防御两件套", mapDefense);
        mapDefense.put(HuoZhiChe.YuHunName, HuoZhiChe.class);
        mapDefense.put(ZhenZhu.YuHunName, ZhenZhu.class);

        Map<String, Class<? extends YuHun>> mapShouLing = new LinkedHashMap<>();
        yuHunMap.put("首领两件套", mapShouLing);
        mapShouLing.put(TuZhiZhu.YuHunName, TuZhiZhu.class);
        mapShouLing.put(DiZhenNian.YuHunName, DiZhenNian.class);
        mapShouLing.put(ShenQiLou.YuHunName, ShenQiLou.class);
        mapShouLing.put(HuangKuLou.YuHunName, HuangKuLou.class);*/

        yuHunMap.put(HuoLing.YuHunName, HuoLing.class);
        yuHunMap.put(KuangGu.YuHunName, KuangGu.class);
        yuHunMap.put(TuZhiZhu.YuHunName, TuZhiZhu.class);
        yuHunMap.put(DiZhenNian.YuHunName, DiZhenNian.class);
        yuHunMap.put(QingNvFang.YuHunName, QingNvFang.class);
        yuHunMap.put(ShangHunNiao.YuHunName, ShangHunNiao.class);
        yuHunMap.put(HuoZhiChe.YuHunName, HuoZhiChe.class);
        yuHunMap.put(ShenQiLou.YuHunName, ShenQiLou.class);
        yuHunMap.put(HaiYueHuoYu.YuHunName, HaiYueHuoYu.class);
        yuHunMap.put(HuangKuLou.YuHunName, HuangKuLou.class);
        yuHunMap.put(DiaoPingHuo.YuHunName, DiaoPingHuo.class);
        yuHunMap.put(ZhenZhu.YuHunName, ZhenZhu.class);
        yuHunMap.put(TuFo.YuHunName, TuFo.class);
        yuHunMap.put(ZhenMuShou.YuHunName, ZhenMuShou.class);
        yuHunMap.put(YuanXingSi.YuHunName, YuanXingSi.class);
        yuHunMap.put(YiNianHuo.YuHunName, YiNianHuo.class);
        yuHunMap.put(LunRuDao.YuHunName, LunRuDao.class);
        yuHunMap.put(ZhaoCaiMao.YuHunName, ZhaoCaiMao.class);
        yuHunMap.put(LongChe.YuHunName, LongChe.class);
        yuHunMap.put(RiNvSiShi.YuHunName, RiNvSiShi.class);
        yuHunMap.put(FengHaiTu.YuHunName, FengHaiTu.class);
        yuHunMap.put(YingShengChong.YuHunName, YingShengChong.class);
        yuHunMap.put(YeHuangHun.YuHunName, YeHuangHun.class);
        yuHunMap.put(ELou.YuHunName, ELou.class);
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

    public static <T extends YuHun> Optional<T> getYuHun(String name, Character character, boolean isInit) {
        /*for (Map<String, Class<? extends YuHun>> map : yuHunMap.values()) {
            if (map.containsKey(name)) {
                return getYuHun(map.get(name), character)
            }
        }*/
        if (yuHunMap.containsKey(name)) {
            return getYuHun(yuHunMap.get(name), character, isInit);
        }

        return Optional.empty();
    }

    public static <T extends YuHun> Optional<T> getYuHun(Class<? extends YuHun> yClass, Character character, boolean isInit) {
        try {
            @SuppressWarnings("unchecked")
            T yuHun = (T) yClass.getDeclaredConstructor().newInstance();
            yuHun.init(character, isInit);
            yClass.cast(yuHun);
            return Optional.of(yuHun);
        } catch (Exception e) {
            Utils.throwException("加载御魂失败", e);
        }
        return Optional.empty();
    }
}