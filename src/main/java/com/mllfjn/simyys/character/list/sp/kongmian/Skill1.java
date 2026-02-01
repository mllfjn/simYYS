package com.mllfjn.simyys.character.list.sp.kongmian;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    public static final String SkillName = "存湮";

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        AttackInfo info = AttackInfo.createJianJieAttack(getBelongTo(), this, target
                , (from, to) -> from.getAttack());
        info.setMultiplier(multiplierGeneral[getLevel()]);
        interactive.attack(info);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
