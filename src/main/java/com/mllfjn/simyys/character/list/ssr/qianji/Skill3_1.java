package com.mllfjn.simyys.character.list.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill3_1 extends Skill {
    public static final String SkillName = "海潮入梦";

    public Skill3_1(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 3);
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp) && bp.canSummon(getBelongTo().team);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        // 召唤海原贝戟
        new HaiYuanBeiJi((QianJi) getBelongTo(), bp, getLevel());
        // 释放后技能替换为永生之汐(3-2)
        getBelongTo().removeSkill(3);
        getBelongTo().addSkill(new Skill3_2(getBelongTo()));
        return Optional.empty();
    }
}
