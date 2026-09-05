package com.mllfjn.simyys.character.list.sr.haifangzhu;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.HealInfo;

import java.util.List;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "祝福之水";
    private double decay = 1;

    private final Status status;

    public Skill2(Character belongTo) {
        super(belongTo, -1, 2);
        status = Status.of(SkillName + "攻击监听", belongTo);
        status.runOn(Trigger.CAUSE_ATTACK, param -> {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            Character target = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .get(Attribute.HP_PERCENT, CharacterFinder.Criteria.MIN);
            belongTo.doInteractive(interactive ->
                    interactive.heal(this, List.of(target),
                            _ ->
                                    HealInfo.createHeal(belongTo, this, target,
                                            attackInfo.getTraceableNumber().getNumber() * decay
                                    )
                    )
            );

            Skill skill = attackInfo.getSkill();
            if (skill instanceof Skill3) {
                if (decay == 1) {
                    skill.addSkillEndListener(() -> decay = 1);
                } else {
                    decay *= 0.85;
                }
            }
        });
    }

    @Override
    public void enable() {
        getBelongTo().addStatus(status);
    }

    @Override
    public void disable() {
        getBelongTo().removeStatus(status);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
