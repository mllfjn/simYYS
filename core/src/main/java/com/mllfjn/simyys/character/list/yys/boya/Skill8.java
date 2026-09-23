package com.mllfjn.simyys.character.list.yys.boya;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.ratecontroller.RateController;

// √     使用破魔矢[skill1]攻击时,黑豹有30%概率进行协同攻击,造成源博雅攻击100%伤害
// √     术印:黑豹的协同攻击概率提升5%

class Skill8 extends PassiveSkill {
    static final String SkillName = "秘术·穷追";

    private final int rate;

    public Skill8(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 8);
        rate = 30 + 5 * shuYin;
    }

    public void judgment(Character target) {
        if (RateController.otherWhether(SkillName, "协同攻击", getBelongTo().bp.calc, rate)) {
            use(target);
        }
    }

    public void use(Character target) {
        getBelongTo().doInteractive(interactive ->
                interactive.attackTypical(this, target, 100, AttackType.DAN_TI));
        log(target);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
