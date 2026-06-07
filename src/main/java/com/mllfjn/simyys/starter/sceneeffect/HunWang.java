package com.mllfjn.simyys.starter.sceneeffect;

import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.sr.huajing.HuaJing;
import com.mllfjn.simyys.character.list.sr.luoxinfu.LuoXinFu;
import com.mllfjn.simyys.character.list.sr.rihefang.RiHeFang;
import com.mllfjn.simyys.character.list.sr.xiazhongshaonv.XiaZhongShaoNv;
import com.mllfjn.simyys.character.list.ssr.axiuluo.AXiuLuo;
import com.mllfjn.simyys.character.list.ssr.dashe.DaShe;
import com.mllfjn.simyys.character.propertygetter.*;
import com.mllfjn.simyys.collections.SerializableObservableList;

public class HunWang {
    public static final String SCENE_NAME = "魂王";

    public static void addCharacterProperty(SerializableObservableList<PropertiesHolder> list) {
        // 数据来自https://bbs.nga.cn/read.php?tid=35316684
        PropertiesMap pm;

        // 络新妇
        pm = CharacterFactory.getProperties(LuoXinFu.CharacterName).orElseThrow();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("1");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("111");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("130000");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("320.32");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("132");
        list.add(new PropertiesHolder(LuoXinFu.CharacterName, pm));

        // 阿修罗
        pm = CharacterFactory.getProperties(AXiuLuo.CharacterName).orElseThrow();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("1");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("111");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("300000");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("425");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("141");
        list.add(new PropertiesHolder(AXiuLuo.CharacterName, pm));

        // 匣子
        pm = CharacterFactory.getProperties(XiaZhongShaoNv.CharacterName).orElseThrow();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("1");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("111");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("120000");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("320.32");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("145");
        list.add(new PropertiesHolder(XiaZhongShaoNv.CharacterName, pm));

        // 化鲸
        pm = CharacterFactory.getProperties(HuaJing.CharacterName).orElseThrow();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("2");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("111");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("44000");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("900");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("148");
        list.add(new PropertiesHolder(HuaJing.CharacterName, pm));

        // 八岐大蛇
        pm = CharacterFactory.getProperties(DaShe.CharacterName).orElseThrow();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("2");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("111");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("666666");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("390");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("137");
        list.add(new PropertiesHolder(DaShe.CharacterName, pm));

        // 日和坊
        pm = CharacterFactory.getProperties(RiHeFang.CharacterName).orElseThrow();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("2");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("111");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("198500");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("0");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("152");
        list.add(new PropertiesHolder(RiHeFang.CharacterName, pm));

        /*// 蛇魔
        pm = CharacterFactory.getProperties(.CharacterName).orElseThrow();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("3");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("120000");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("570");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("145");
        list.add(new PropertiesHolder(.CharacterName, pm));

        // 白蛇
        pm = CharacterFactory.getProperties(.CharacterName).orElseThrow();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("3");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("120000");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("320.32");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("145");
        list.add(new PropertiesHolder(.CharacterName, pm));

        // 蛇魔
        pm = CharacterFactory.getProperties(.CharacterName).orElseThrow();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("3");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("120000");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("570");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("145");
        list.add(new PropertiesHolder(.CharacterName, pm));*/
    }
}
