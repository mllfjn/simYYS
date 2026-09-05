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
import com.mllfjn.simyys.character.list.sp.sphudie.SpHuDie;
import com.mllfjn.simyys.character.list.sr.haifangzhu.HaiFangZhu;
import com.mllfjn.simyys.character.list.sr.huajing.HuaJing;
import com.mllfjn.simyys.character.list.sr.luoxinfu.LuoXinFu;
import com.mllfjn.simyys.character.list.sr.qingji.QingJi;
import com.mllfjn.simyys.character.list.sr.rihefang.RiHeFang;
import com.mllfjn.simyys.character.list.sr.xiazhongshaonv.XiaZhongShaoNv;
import com.mllfjn.simyys.character.list.sr.xienv.XieNv;
import com.mllfjn.simyys.character.list.sr.xuenv.XueNv;
import com.mllfjn.simyys.character.list.sr.yaoqin.YaoQin;
import com.mllfjn.simyys.character.list.ssr.axiuluo.AXiuLuo;
import com.mllfjn.simyys.character.list.ssr.beimihu.BeiMiHu;
import com.mllfjn.simyys.character.list.ssr.bujianyue.BuJianYue;
import com.mllfjn.simyys.character.list.ssr.dashe.DaShe;
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
    public static final Map<String, List<CharacterMeta>> characterMapList = new LinkedHashMap<>();
    public static final Map<Integer, CharacterMeta> idMap = new HashMap<>();
    public static final Map<String, CharacterMeta> nameMap = new HashMap<>();

    private static final Map<String, Image> iconMap = new HashMap<>();
    public static final List<Class<? extends Character>> FIRE_CHARACTER =
            List.of(FuJi.class, SPHongYe.class, ShiLing.class, SiJinShen.class, QingJi.class);

    static {
        List<CharacterMeta> yysMap = new ArrayList<>();
        characterMapList.put("阴阳师", yysMap);
        int index = 1;
        putCharacterMeta(yysMap, index++, QingMing.CharacterName, QingMing.class);
        putCharacterMeta(yysMap, index++, ShenLe.CharacterName, ShenLe.class);
        putCharacterMeta(yysMap, index++, BoYa.CharacterName, BoYa.class);
        putCharacterMeta(yysMap, index++, YuanLaiGuang.CharacterName, YuanLaiGuang.class);
        putCharacterMeta(yysMap, index++, TengYuanDaoZhang.CharacterName, TengYuanDaoZhang.class);

        List<CharacterMeta> spMap = new ArrayList<>();
        characterMapList.put("SP", spMap);
        putCharacterMeta(spMap, 554, DaYuan.CharacterName, DaYuan.class);
        putCharacterMeta(spMap, 383, ShenShe.CharacterName, ShenShe.class);
        putCharacterMeta(spMap, 566, LaoTou.CharacterName, LaoTou.class);
        putCharacterMeta(spMap, 579, SpJin.CharacterName, SpJin.class);
        putCharacterMeta(spMap, 372, YinFan.CharacterName, YinFan.class);
        putCharacterMeta(spMap, 352, FuJi.CharacterName, FuJi.class);
        putCharacterMeta(spMap, 355, LuWan.CharacterName, LuWan.class);
        putCharacterMeta(spMap, 388, SPHongYe.CharacterName, SPHongYe.class);
        putCharacterMeta(spMap, 594, SpHuDie.CharacterName, SpHuDie.class);

        List<CharacterMeta> ssrMap = new ArrayList<>();
        characterMapList.put("SSR", ssrMap);
        putCharacterMeta(ssrMap, 557, NaMei.CharacterName, NaMei.class);
        putCharacterMeta(ssrMap, 356, QianJi.CharacterName, QianJi.class);
        putCharacterMeta(ssrMap, 369, ShiLing.CharacterName, ShiLing.class);
        putCharacterMeta(ssrMap, 389, XuZuo.CharacterName, XuZuo.class);
        putCharacterMeta(ssrMap, 583, BeiMiHu.CharacterName, BeiMiHu.class);
        putCharacterMeta(ssrMap, 596, ShenWuYue.CharacterName, ShenWuYue.class);
        putCharacterMeta(ssrMap, 597, GeYe.CharacterName, GeYe.class);
        putCharacterMeta(ssrMap, 600, ShiJiaMei.CharacterName, ShiJiaMei.class);
        putCharacterMeta(ssrMap, 363, DiShiTian.CharacterName, DiShiTian.class);
        putCharacterMeta(ssrMap, 391, XunXiangXing.CharacterName, XunXiangXing.class);
        putCharacterMeta(ssrMap, 217, DaTianGou.CharacterName, DaTianGou.class);
        putCharacterMeta(ssrMap, 312, GuiQie.CharacterName, GuiQie.class);
        putCharacterMeta(ssrMap, 556, TianZhao.CharacterName, TianZhao.class);
        putCharacterMeta(ssrMap, 591, XueYuQian.CharacterName, XueYuQian.class);
        putCharacterMeta(ssrMap, 379, BuJianYue.CharacterName, BuJianYue.class);
        putCharacterMeta(ssrMap, 601, SiJinShen.CharacterName, SiJinShen.class);
        putCharacterMeta(ssrMap, 364, AXiuLuo.CharacterName, AXiuLuo.class);
        putCharacterMeta(ssrMap, 325, DaShe.CharacterName, DaShe.class);

        List<CharacterMeta> srMap = new ArrayList<>();
        characterMapList.put("SR", srMap);
        putCharacterMeta(srMap, 256, YaoQin.CharacterName, YaoQin.class);
        putCharacterMeta(srMap, 350, XieNv.CharacterName, XieNv.class);
        putCharacterMeta(srMap, 247, HaiFangZhu.CharacterName, HaiFangZhu.class);
        putCharacterMeta(srMap, 260, QingJi.CharacterName, QingJi.class);
        putCharacterMeta(srMap, 270, LuoXinFu.CharacterName, LuoXinFu.class);
        putCharacterMeta(srMap, 201, XueNv.CharacterName, XueNv.class);
        putCharacterMeta(srMap, 324, HuaJing.CharacterName, HuaJing.class);
        putCharacterMeta(srMap, 297, RiHeFang.CharacterName, RiHeFang.class);
        putCharacterMeta(srMap, 287, XiaZhongShaoNv.CharacterName, XiaZhongShaoNv.class);

        List<CharacterMeta> rMap = new ArrayList<>();
        characterMapList.put("R", rMap);
        putCharacterMeta(rMap, 228, ChouNv.CharacterName, ChouNv.class);
        putCharacterMeta(rMap, 244, ShouWu.CharacterName, ShouWu.class);

        List<CharacterMeta> mobMap = new ArrayList<>();
        characterMapList.put("怪物", mobMap);
        index = 50;
        putCharacterMeta(mobMap, index++, ShenQiLou.CharacterName, ShenQiLou.class);
        putCharacterMeta(mobMap, index++, TuZhiZhu.CharacterName, TuZhiZhu.class);
        putCharacterMeta(mobMap, index++, HuangKuLou.CharacterName, HuangKuLou.class);
        putCharacterMeta(mobMap, index++, DiZhenNian.CharacterName, DiZhenNian.class);
    }

    private static void putCharacterMeta(List<CharacterMeta> list,
                                         int id, String name, Class<? extends Character> clazz) {
        CharacterMeta characterMeta = new CharacterMeta(id, name, clazz);
        list.add(characterMeta);
        idMap.put(id, characterMeta);
        nameMap.put(name, characterMeta);
    }

    public static Character getCharacter(String name) {
        if (nameMap.containsKey(name)) {
            try {
                return nameMap.get(name).clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                Utils.throwException("获取角色信息失败", e);
            }
        }
        return new EmptyCharacter();
    }

    public static Character getCharacter(PropertiesHolder ph, BattlePane bp) {
        try {
            Character character = getCharacter(ph.name);


            character.init(ph, bp);
            character.fillSkills();
            ph.created(character);
            return character;
        } catch (Exception e) {
            Utils.throwException("获取角色信息失败", e);
        }
        return null;
    }

    public static PropertiesMap getProperties(String name) {
        return getCharacter(name).getProperties();
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

    public static CharacterMeta getCharacterMeta(String name) {
        return nameMap.get(name);
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

    private static class EmptyCharacter extends Character {
        @Override
        protected String getDefaultBaseAttack() {
            return "";
        }

        @Override
        protected void addOwnSkills() {

        }
    }

    public record CharacterMeta(int id, String name, Class<? extends Character> clazz) {
    }
}
