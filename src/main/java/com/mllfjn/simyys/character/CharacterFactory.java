package com.mllfjn.simyys.character;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian.DiZhenNian;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.huangkulou.HuangKuLou;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu.TuZhiZhu;
import com.mllfjn.simyys.character.list.r.chounv.ChouNv;
import com.mllfjn.simyys.character.list.r.shouwu.ShouWu;
import com.mllfjn.simyys.character.list.sp.fuji.FuJi;
import com.mllfjn.simyys.character.list.sp.laotou.LaoTou;
import com.mllfjn.simyys.character.list.sp.luwan.LuWan;
import com.mllfjn.simyys.character.list.sp.sphongye.SPHongYe;
import com.mllfjn.simyys.character.list.sp.spjin.SpJin;
import com.mllfjn.simyys.character.list.sp.yinfan.YinFan;
import com.mllfjn.simyys.character.list.sphudie.SpHuDie;
import com.mllfjn.simyys.character.list.sr.haifangzhu.HaiFangZhu;
import com.mllfjn.simyys.character.list.sr.huajing.HuaJing;
import com.mllfjn.simyys.character.list.sr.luoxinfu.LuoXinFu;
import com.mllfjn.simyys.character.list.sr.qingji.QingJi;
import com.mllfjn.simyys.character.list.sr.xienv.XieNv;
import com.mllfjn.simyys.character.list.sr.xuenv.XueNv;
import com.mllfjn.simyys.character.list.sr.yaoqin.YaoQin;
import com.mllfjn.simyys.character.list.ssr.axiuluo.AXiuLuo;
import com.mllfjn.simyys.character.list.ssr.beimihu.BeiMiHu;
import com.mllfjn.simyys.character.list.ssr.bujianyue.BuJianYue;
import com.mllfjn.simyys.character.list.ssr.datiangou.DaTianGou;
import com.mllfjn.simyys.character.list.ssr.dishitian.DiShiTian;
import com.mllfjn.simyys.character.list.ssr.geye.GeYe;
import com.mllfjn.simyys.character.list.ssr.guiqie.GuiQie;
import com.mllfjn.simyys.character.list.ssr.shenwuyue.ShenWuYue;
import com.mllfjn.simyys.character.list.ssr.shijiamei.ShiJiaMei;
import com.mllfjn.simyys.character.list.ssr.shiling.ShiLing;
import com.mllfjn.simyys.character.list.ssr.sijinshen.SiJinShen;
import com.mllfjn.simyys.character.list.ssr.tianzhao.TianZhao;
import com.mllfjn.simyys.character.list.ssr.xueyuqian.XueYuQian;
import com.mllfjn.simyys.character.list.ssr.xunxiangxing.XunXiangXing;
import com.mllfjn.simyys.character.list.ssr.xuzuo.XuZuo;
import com.mllfjn.simyys.character.list.yys.boya.BoYa;
import com.mllfjn.simyys.character.list.yys.qingming.QingMing;
import com.mllfjn.simyys.character.list.yys.tengyuan.TengYuanDaoZhang;
import com.mllfjn.simyys.character.list.yys.yuanlaiguang.YuanLaiGuang;
import com.mllfjn.simyys.utils.PicUtil;
import com.mllfjn.simyys.utils.Utils;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou.ShenQiLou;
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
import javafx.scene.paint.Color;

import java.util.*;

public class CharacterFactory {
    public static final Map<String, Map<String, Class<? extends Character>>> characterMap = new LinkedHashMap<>();
    private static final Map<String, Image> iconMap = new HashMap<>();
    public static final List<Class<? extends Character>> FIRE_CHARACTER =
            List.of(FuJi.class, SPHongYe.class, ShiLing.class, SiJinShen.class, QingJi.class);

    static {
        Map<String, Class<? extends Character>> yysMap = new LinkedHashMap<>();
        characterMap.put("阴阳师", yysMap);
        yysMap.put(ShenLe.CharacterName, ShenLe.class);
        yysMap.put(YuanLaiGuang.CharacterName, YuanLaiGuang.class);
        yysMap.put(QingMing.CharacterName, QingMing.class);
        yysMap.put(BoYa.CharacterName, BoYa.class);
        yysMap.put(TengYuanDaoZhang.CharacterName, TengYuanDaoZhang.class);

        Map<String, Class<? extends Character>> spMap = new LinkedHashMap<>();
        characterMap.put("SP", spMap);
        spMap.put(DaYuan.CharacterName, DaYuan.class);
        spMap.put(ShenShe.CharacterName, ShenShe.class);
        spMap.put(LaoTou.CharacterName, LaoTou.class);
//        spMap.put(KongMian.CharacterName, KongMian.class);
        spMap.put(SpJin.CharacterName, SpJin.class);
        spMap.put(YinFan.CharacterName, YinFan.class);
        spMap.put(FuJi.CharacterName, FuJi.class);
        spMap.put(LuWan.CharacterName, LuWan.class);
        spMap.put(SPHongYe.CharacterName, SPHongYe.class);
        spMap.put(SpHuDie.CharacterName, SpHuDie.class);


        Map<String, Class<? extends Character>> ssrMap = new LinkedHashMap<>();
        characterMap.put("SSR", ssrMap);
        ssrMap.put(NaMei.CharacterName, NaMei.class);
        ssrMap.put(QianJi.CharacterName, QianJi.class);
        ssrMap.put(ShiLing.CharacterName, ShiLing.class);
        ssrMap.put(XuZuo.CharacterName, XuZuo.class);
        ssrMap.put(BeiMiHu.CharacterName, BeiMiHu.class);
        ssrMap.put(ShenWuYue.CharacterName, ShenWuYue.class);
//        ssrMap.put(MaoChuan.CharacterName, MaoChuan.class);
        ssrMap.put(GeYe.CharacterName, GeYe.class);
        ssrMap.put(ShiJiaMei.CharacterName, ShiJiaMei.class);
        ssrMap.put(DiShiTian.CharacterName, DiShiTian.class);
        ssrMap.put(XunXiangXing.CharacterName, XunXiangXing.class);
        ssrMap.put(DaTianGou.CharacterName, DaTianGou.class);
        ssrMap.put(GuiQie.CharacterName, GuiQie.class);
        ssrMap.put(TianZhao.CharacterName, TianZhao.class);
        ssrMap.put(XueYuQian.CharacterName, XueYuQian.class);
        ssrMap.put(BuJianYue.CharacterName, BuJianYue.class);
        ssrMap.put(SiJinShen.CharacterName, SiJinShen.class);
        ssrMap.put(AXiuLuo.CharacterName, AXiuLuo.class);



        Map<String, Class<? extends Character>> srMap = new LinkedHashMap<>();
        characterMap.put("SR", srMap);
        srMap.put(YaoQin.CharacterName, YaoQin.class);
        srMap.put(XieNv.CharacterName, XieNv.class);
        srMap.put(HaiFangZhu.CharacterName, HaiFangZhu.class);
        srMap.put(QingJi.CharacterName, QingJi.class);
        srMap.put(LuoXinFu.CharacterName, LuoXinFu.class);
        srMap.put(XueNv.CharacterName, XueNv.class);
        srMap.put(HuaJing.CharacterName, HuaJing.class);

        Map<String, Class<? extends Character>> rMap = new LinkedHashMap<>();
        characterMap.put("R", rMap);
        rMap.put(ChouNv.CharacterName, ChouNv.class);
        rMap.put(ShouWu.CharacterName, ShouWu.class);

        Map<String, Class<? extends Character>> mobMap = new LinkedHashMap<>();
        characterMap.put("怪物", mobMap);
        mobMap.put(ShenQiLou.CharacterName, ShenQiLou.class);
        mobMap.put(TuZhiZhu.CharacterName, TuZhiZhu.class);
        mobMap.put(HuangKuLou.CharacterName, HuangKuLou.class);
        mobMap.put(DiZhenNian.CharacterName, DiZhenNian.class);
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
        if (ph.characterClass != null) {
            try {
                Character character = ph.characterClass.getDeclaredConstructor().newInstance();
                character.init(ph, bp);
                character.fillSkills();
                return Optional.of(character);
            } catch (Exception e) {
                Utils.throwException("获取角色信息失败", e);
            }
        }
        Optional<Character> oc = getCharacter(ph.name);
        oc.ifPresent(character -> {
            character.init(ph, bp);
            character.fillSkills();
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

    public static Node getImageWithStroke(Character character, ImageSize size, double strokeWidth) {
        return PicUtil.clipAndStroke(
                (ImageView) getImage(character.name, size),
                size.size,
                character.team == 0 ? Color.ORANGE : Color.RED,
                strokeWidth
        );
    }

    public enum ImageSize {
        CHARACTER_ICON_IMAGE(120),
        BIG(110),
        SMALL(70),
        LABEL(35);

        public final double size;
        ImageSize(double size) {
            this.size = size;
        }
    }
}
