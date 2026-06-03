package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "激怒";

    public Skill2(Character belongTo) {
        super(belongTo, -1, 2);
        getBelongTo().addStatus(new StatusJiNu(getBelongTo()));
    }

    @Override
    public String getSkillDesc() {
        return "√\t每个回合增加8%攻击力(未测试增加时机,按照回合后增加处理)";
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusJiNu extends Status implements StatusRunnable, AttributeModifier {
        private int stack;

        public StatusJiNu(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ATTACK;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return stack * 0.08 * belongTo.getInitAttack();
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            stack++;
            return false;
        }
    }
}
