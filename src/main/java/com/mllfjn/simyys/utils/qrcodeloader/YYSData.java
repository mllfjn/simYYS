package com.mllfjn.simyys.utils.qrcodeloader;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.yys.CharacterYYSBase;
import com.mllfjn.simyys.character.list.yys.qiling.QiLingFactory;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyMultiInput;

import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class YYSData extends CharacterData {
    public int[][] skills; // 技能
    public QiLingData qiLingData;
    public int lockSkill; // 锁定技能,妖术为11

    public YYSData() {
    }

    @Override
    public void get(PropertiesHolder ph, Map<LoadAttribute, Float> maxAttributeMap) {
        if (lockSkill > 0 && lockSkill != 11) {
            ph.lockSkillMap.put(1, lockSkill);
        }

        PropertiesMap map = ph.propertiesMap;

        if (qiLingData != null) {
            QiLingFactory.QiLingMeta meta = QiLingFactory.ID_MAP.get(qiLingData.id);
            if (meta != null) {
                map.get(PropertyKey.QI_LING_KEY).setValue(meta.name());
            }
        }

        CharacterYYSBase.YYSSkill[] yysSkills = ((CharacterYYSBase) CharacterFactory.getCharacter(ph.name)).yysSkills;
        for (int i = 0; i < yysSkills.length; i++) {
            changeSkill(yysSkills[i], i, map);
        }
    }

    private void changeSkill(CharacterYYSBase.YYSSkill yysSkill, int index, PropertiesMap map) {
        if (yysSkill == null) {
            return;
        }

        int targetLevel = index == 0 ? 5 : 0; // 普攻等级没写在这里
        int targetShuYin = 0;
        for (int[] skill : skills) {
            int skillID = skill[0];
            if (skillID == (index + 1)) {
                targetLevel = skill[1];
                if (qiLingData != null) {
                    targetShuYin = qiLingData.getShuYin(skillID);
                }
                break;
            }
        }
        ((PropertyMultiInput) map.get(yysSkill.name()))
                .setValue(0, String.valueOf(targetLevel))
                .setValue(1, String.valueOf(targetShuYin));
    }
}
