package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunAttack;
import com.mllfjn.simyys.interactive.AttackInfo;

public class YinNian extends YuHun implements YuHunAttack {
    public static final String YuHunName = "隐念";
    private static final double[] multiplier = {1.2, 1.4, 1.6};

    private Skill skill;
    private int index = 0;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void effectInfo(AttackInfo attackInfo) {
        if (skill == null) {
            skill = attackInfo.getSkill();
            skill.addSkillEndListener(() -> {
                skill = null;
                index = 0;
            });
        }
        attackInfo.getTraceableNumber().mul(multiplier[index], YuHunName);
        index = (index + 1) % 3;
        yuHunEffect();
    }
}
