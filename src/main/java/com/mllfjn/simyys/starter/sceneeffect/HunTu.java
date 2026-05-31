package com.mllfjn.simyys.starter.sceneeffect;

import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.sr.qingji.QingJi;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyCheck;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;

import java.util.LinkedHashMap;
import java.util.List;

class HunTu {
    public static final String Scene_Name = "魂土";

    public static void addCharacterProperty(List<PropertiesHolder> list) {
        // 数据来自https://bbs.nga.cn/read.php?tid=16641284

        PropertiesHolder ph;

        // 清姬,技能115,御魂涅槃,攻击12859.2,生命33991.944,防御704,速度145,暴击20
        PropertiesMap pm = CharacterFactory.getProperties(QingJi.CharacterName).orElseThrow();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("1");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("115");
        ph = new PropertiesHolder(QingJi.CharacterName, pm, new LinkedHashMap<>(), new LinkedHashMap<>());
        list.add(ph);

        // 络新妇,技能115,御魂轮入道(50),攻击12055.5,生命337590.3760,防御704,速度132,暴击30
    }
}
