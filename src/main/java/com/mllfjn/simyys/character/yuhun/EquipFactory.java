package com.mllfjn.simyys.character.yuhun;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.yuhun.list.*;
import com.mllfjn.simyys.character.yuhun.list.youchizi.YouChiZi;
import com.mllfjn.simyys.utils.PicUtil;
import com.mllfjn.simyys.utils.Utils;
import javafx.scene.image.Image;

import java.util.*;

public class EquipFactory {
    public static final double ICON_SIZE = 25;
    public static final double ICON_RADIUS = ICON_SIZE / 2;
    private static final Map<String, Image> iconMap = new HashMap<>();

    private static final Map<EquipType, List<EquipMeta>> EQUIP_LIST_MAP = new EnumMap<>(EquipType.class);
    public static final Map<Integer, EquipMeta> ID_MAP = new HashMap<>();
    private static final Map<String, EquipMeta> NAME_MAP = new HashMap<>();

    static {
        for (EquipType value : EquipType.values()) {
            EQUIP_LIST_MAP.put(value, new ArrayList<>());
        }
        putEquipMeta(300048, KuangGu.YuHunName, KuangGu.class, EquipType.ATTACK);
        putEquipMeta(300022, XinYan.YuHunName, XinYan.class, EquipType.ATTACK);
        putEquipMeta(300012, LunRuDao.YuHunName, LunRuDao.class, EquipType.ATTACK);
        putEquipMeta(300004, FuYi.YuHunName, FuYi.class, EquipType.ATTACK);
        putEquipMeta(300086, YinNian.YuHunName, YinNian.class, EquipType.ATTACK);

        putEquipMeta(300075, QingNvFang.YuHunName, QingNvFang.class, EquipType.CRIT_RATE);
        putEquipMeta(300036, ZhenNv.YuHunName, ZhenNv.class, EquipType.CRIT_RATE);
        putEquipMeta(300031, ZhenMuShou.YuHunName, ZhenMuShou.class, EquipType.CRIT_RATE);
        putEquipMeta(300030, PoShi.YuHunName, PoShi.class, EquipType.CRIT_RATE);
        putEquipMeta(300029, ShangHunNiao.YuHunName, ShangHunNiao.class, EquipType.CRIT_RATE);
        putEquipMeta(300083, HaiYueHuoYu.YuHunName, HaiYueHuoYu.class, EquipType.CRIT_RATE);
        putEquipMeta(300088, YingShengChong.YuHunName, YingShengChong.class, EquipType.CRIT_RATE);
        putEquipMeta(300055, PianYeZhiWei.YuHunName, PianYeZhiWei.class, EquipType.CRIT_RATE);

        putEquipMeta(300076, TuFo.YuHunName, TuFo.class, EquipType.HP);
        putEquipMeta(300006, NiePanZhiHuo.YuHunName, NiePanZhiHuo.class, EquipType.HP);
        putEquipMeta(300081, ELou.YuHunName, ELou.class, EquipType.HP);

        putEquipMeta(300032, ZhenZhu.YuHunName, ZhenZhu.class, EquipType.DEFENSE);
        putEquipMeta(300013, RiNvSiShi.YuHunName, RiNvSiShi.class, EquipType.DEFENSE);
        putEquipMeta(300010, ZhaoCaiMao.YuHunName, ZhaoCaiMao.class, EquipType.DEFENSE);
        putEquipMeta(300002, XueYouHun.YuHunName, XueYouHun.class, EquipType.DEFENSE);
        putEquipMeta(300084, ChuShiLuo.YuHunName, ChuShiLuo.class, EquipType.DEFENSE);
        putEquipMeta(300085, HuoZhiChe.YuHunName, HuoZhiChe.class, EquipType.DEFENSE);
        putEquipMeta(300093, FengHaiTu.YuHunName, FengHaiTu.class, EquipType.DEFENSE);

        putEquipMeta(300019, HuoLing.YuHunName, HuoLing.class, EquipType.EFFECT_HIT);
        putEquipMeta(300079, YiNianHuo.YuHunName, YiNianHuo.class, EquipType.EFFECT_HIT);
        putEquipMeta(300089, YuanXingSi.YuHunName, YuanXingSi.class, EquipType.EFFECT_HIT);
        putEquipMeta(300057, YouChiZi.YuHunName, YouChiZi.class, EquipType.EFFECT_HIT);

        putEquipMeta(300080, GongQian.YuHunName, GongQian.class, EquipType.EFFECT_RESIST);
        putEquipMeta(300090, DiaoPingHuo.YuHunName, DiaoPingHuo.class, EquipType.EFFECT_RESIST);

        putEquipMeta2Set(300077, GuiLingGeJi.YuHunName, GuiLingGeJi.class);
        putEquipMeta2Set(300054, ShenQiLou.YuHunName, ShenQiLou.class);
        putEquipMeta2Set(300053, DiZhenNian.YuHunName, DiZhenNian.class);
        putEquipMeta2Set(300052, HuangKuLou.YuHunName, HuangKuLou.class);
        putEquipMeta2Set(300051, LongChe.YuHunName, LongChe.class);
        putEquipMeta2Set(300050, TuZhiZhu.YuHunName, TuZhiZhu.class);
        putEquipMeta2Set(300091, YeHuangHun.YuHunName, YeHuangHun.class);
    }

    private static void putEquipMeta(int id, String name, Class<? extends Equip> clazz, EquipType equipType) {
        EquipMeta equipMeta = new EquipMeta(id, name, clazz, equipType);
        putEquipMeta(id, name, equipType, equipMeta);
    }

    private static void putEquipMeta2Set(int id, String name, Class<? extends Equip> clazz) {
        EquipMeta equipMeta = new EquipMeta(id, name, clazz, EquipType.SINGLE, 2);
        putEquipMeta(id, name, EquipType.SINGLE, equipMeta);
    }

    private static void putEquipMeta(int id, String name, EquipType equipType, EquipMeta equipMeta) {
        EQUIP_LIST_MAP.get(equipType).add(equipMeta);
        NAME_MAP.put(name, equipMeta);
        ID_MAP.put(id, equipMeta);
    }

    public static Image getImage(String name) {
        Image image;
        if (iconMap.containsKey(name)) {
            image = iconMap.get(name);
        } else {
            image = PicUtil.loadImage(EquipFactory.class, "", name, ".png", ICON_SIZE);
            iconMap.put(name, image);
        }
        return image;
    }

    public static <T extends Equip> Optional<T> getEquip(String name, Character character, boolean isInit) {
        EquipMeta equipMeta = NAME_MAP.get(name);
        if (equipMeta != null) {
            try {
                @SuppressWarnings("unchecked")
                T yuHun = (T) equipMeta.clazz.getDeclaredConstructor().newInstance();
                yuHun.init(character, isInit);
                equipMeta.clazz.cast(yuHun);
                return Optional.of(yuHun);
            } catch (Exception e) {
                Utils.throwException("加载御魂失败", e);
            }
        }
        return Optional.empty();
    }

    public record EquipMeta(int id, String name, Class<? extends Equip> clazz, EquipType equipType, int setCount) {
        public EquipMeta(int id, String name, Class<? extends Equip> clazz, EquipType equipType) {
            this(id, name, clazz, equipType, 4);
        }
    }

    public enum EquipType {
        ATTACK,
        CRIT_RATE,
        CRIT_POWER,
        DEFENSE,
        HP,
        EFFECT_HIT,
        EFFECT_RESIST,
        SINGLE
    }
}