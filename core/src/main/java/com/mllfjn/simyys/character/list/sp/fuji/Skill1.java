package com.mllfjn.simyys.character.list.sp.fuji;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "缠心";
    private static final int[] multiplier = {0, 70, 80, 90, 100, 100};

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t对目标造成70%伤害
                √\t每次攻击时,对目标附加持续5回合的3级中毒
                √\t\t和持续至战斗结束的20点减防(上限200点)
                √\tlv2-伤害增加至80%
                √\tlv3-伤害增加至90%
                √\tlv4-伤害增加至100%
                √\tlv5-每次攻击时,改为对目标附加持续5回合的5级中毒和持续至战斗结束的50点减防(上限500点)
                """;
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        interactive.attackTypical(this, target, multiplier[getLevel()], AttackType.DAN_TI);
        ((FuJi) getBelongTo()).attack(target);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
