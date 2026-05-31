package com.mllfjn.simyys.starter.sceneeffect;

import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.r.shouwu.ShouWu;
import com.mllfjn.simyys.character.list.sr.luoxinfu.LuoXinFu;
import com.mllfjn.simyys.character.list.sr.qingji.QingJi;
import com.mllfjn.simyys.character.list.sr.xuenv.XueNv;
import com.mllfjn.simyys.character.propertygetter.*;
import com.mllfjn.simyys.character.yuhun.list.FuYi;
import com.mllfjn.simyys.character.yuhun.list.LunRuDao;
import com.mllfjn.simyys.character.yuhun.list.NiePanZhiHuo;
import com.mllfjn.simyys.character.yuhun.list.PoShi;

import java.util.LinkedHashMap;
import java.util.List;

class HunTu {
    public static final String Scene_Name = "魂土";

    public static void addCharacterProperty(List<PropertiesHolder> list) {
        // 数据来自https://bbs.nga.cn/read.php?tid=16641284
        PropertiesMap pm;

        // 清姬
        pm = CharacterFactory.getProperties(QingJi.CharacterName).orElseThrow();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("1");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("115");
        ((PropertySelectMulti) pm.get(PropertyKey.YU_HUN_KEY)).setValue(NiePanZhiHuo.YuHunName);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).setValue("12859.2");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("33991.944");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("704");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("145");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_CRIT_RATE_KEY)).setValue("20");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_CRIT_POWER_KEY)).setValue("150");
        list.add(new PropertiesHolder(QingJi.CharacterName, pm, new LinkedHashMap<>(), new LinkedHashMap<>()));

        // 络新妇
        pm = CharacterFactory.getProperties(LuoXinFu.CharacterName).orElseThrow();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("1");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("115");
        ((PropertySelectMulti) pm.get(PropertyKey.YU_HUN_KEY)).setValue(LunRuDao.YuHunName);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).setValue("12055.5");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("37590.376");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("704");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("132");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_CRIT_RATE_KEY)).setValue("30");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_CRIT_POWER_KEY)).setValue("150");
        list.add(new PropertiesHolder(LuoXinFu.CharacterName, pm, new LinkedHashMap<>(), new LinkedHashMap<>()));

        // 首无
        pm = CharacterFactory.getProperties(ShouWu.CharacterName).orElseThrow();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("1");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("111");
        ((PropertySelectMulti) pm.get(PropertyKey.YU_HUN_KEY)).setValue(FuYi.YuHunName);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).setValue("12055.5");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("10356.2");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("704");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("150");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_CRIT_RATE_KEY)).setValue("18");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_CRIT_POWER_KEY)).setValue("150");
        list.add(new PropertiesHolder(ShouWu.CharacterName, pm, new LinkedHashMap<>(), new LinkedHashMap<>()));

        // 雪女
        pm = CharacterFactory.getProperties(XueNv.CharacterName).orElseThrow();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("2");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("112");
        ((PropertySelectMulti) pm.get(PropertyKey.YU_HUN_KEY)).setValue(PoShi.YuHunName + PropertySelectMulti.SPLIT_CHAR);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).setValue("12055.5");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("10356.2");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("704");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("150");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_CRIT_RATE_KEY)).setValue("18");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_CRIT_POWER_KEY)).setValue("150");
        list.add(new PropertiesHolder(XueNv.CharacterName, pm, new LinkedHashMap<>(), new LinkedHashMap<>()));
    }
}
