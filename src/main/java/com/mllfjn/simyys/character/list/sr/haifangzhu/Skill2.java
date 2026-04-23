package com.mllfjn.simyys.character.list.sr.haifangzhu;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.HealInfo;

import java.util.List;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "祝福之水";

    private final StatusHeal status;

    public Skill2(Character belongTo) {
        super(belongTo, -1, 2);
        status = new StatusHeal(belongTo);
    }

    @Override
    public void enable() {
        getBelongTo().addStatus(status);
    }

    @Override
    protected void disable() {
        getBelongTo().removeStatus(status);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    class StatusHeal extends Status implements StatusRunnable {
        private double decay = 1;

        public StatusHeal(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.CAUSE_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (trigger == Trigger.CAUSE_ATTACK) {
                AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                Skill skill = attackInfo.getSkill();
                if (skill instanceof Skill1 || skill instanceof Skill3) {
                    Character target = new CharacterFinder(belongTo)
                            .filterTeammate()
                            .get(Attribute.HP_PERCENT, CharacterFinder.Criteria.MIN);
                    belongTo.doInteractive(interactive ->
                            interactive.heal(Skill2.this, List.of(target),
                                    (c) -> HealInfo.createHeal(belongTo, Skill2.this, target,
                                            (c1, c2) ->
                                                    attackInfo.getTraceableNumber().getNumber() * decay
                                    )
                            )
                    );

                    if (skill instanceof Skill3) {
                        if (decay == 1) {
                            skill.addSkillEndListener(() -> decay = 1);
                        } else {
                            decay *= 0.85;
                        }
                    }
                }
            }
            return false;
        }
    }
}
