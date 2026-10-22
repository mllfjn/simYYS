package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "激怒";

    public Skill2(Character belongTo) {
        super(belongTo, -1, 2);
        Status status = Status.of(SkillName, belongTo);
        status.enableStack(Integer.MAX_VALUE)
                .attribute(Attribute.ATTACK, _ -> status.getStack() * 0.08 * belongTo.getInitAttack())
                .runOn(Trigger.AFTER_ROUND, _ -> status.addStack())
                .addTo();
    }

    @Override
    public String getSkillDesc() {
        return "√\t每个回合增加8%攻击力(未测试增加时机,按照回合后增加处理)";
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
