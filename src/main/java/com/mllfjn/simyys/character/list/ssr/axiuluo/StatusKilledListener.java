package com.mllfjn.simyys.character.list.ssr.axiuluo;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.RetainAfterChangeWave;
import com.mllfjn.simyys.character.status.determinant.RetainAfterDie;
import com.mllfjn.simyys.character.status.triggerParam.ParamKilledCharacter;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

class StatusKilledListener extends Status
        implements StatusRunnable, RetainAfterDie, RetainAfterChangeWave {
    private final SkillWuJianShaLu skillWuJianShaLu;

    public StatusKilledListener(Character character, Skill2 skill2) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        skillWuJianShaLu = new SkillWuJianShaLu(character, skill2);
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.KILLED_CHARACTER;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        Character target = ((ParamKilledCharacter) param).getCharacter();
        target.replaceStatus(new StatusZhuMie(belongTo, target));
        skillWuJianShaLu.replace();
        return false;
    }

    private static class StatusZhuMie extends Status
            implements AttributeModifier, RetainAfterDie, RetainAfterChangeWave, Displayable {
        private static final String StatusName = "诛灭";

        public StatusZhuMie(Character from, Character belongTo) {
            super(from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ATTACK;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return -belongTo.getInitAttack() * 0.5;
        }

        @Override
        public String getDisplayText() {
            return StatusName;
        }
    }
}
