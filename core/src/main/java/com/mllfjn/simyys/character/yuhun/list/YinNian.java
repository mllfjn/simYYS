package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.YuHunAttack;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.HashMap;
import java.util.Map;

public class YinNian extends Equip implements YuHunAttack {
    public static final String YuHunName = "隐念";
    private static final double[] multiplier = {1.2, 1.4, 1.6};

    private Skill skill;
    private Map<Character, Integer> indexMap;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        indexMap = new HashMap<>();
    }

    @Override
    public void effectInfo(AttackInfo attackInfo) {
        if (skill == null) {
            skill = attackInfo.getSkill();
            skill.addSkillEndListener(() -> {
                skill = null;
                indexMap.clear();
            });
        }
        Integer count = indexMap.merge(attackInfo.getTarget(), 0, (old, val) -> (old + 1) % 3);
        attackInfo.getTraceableNumber().mul(multiplier[count], YuHunName);
        yuHunEffect();
    }
}
