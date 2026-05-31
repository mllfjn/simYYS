package com.mllfjn.simyys.character.list.yys.qiling;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

class QiLingYueMoTu {
    static final String QiLingName = "月魔兔";

    static void install(Character character) {
        character.bp.addPriorityMove(character, () -> character.addStatus(new StatusQLYMTListener(character)));
    }

    static class StatusQLYMTListener extends Status implements AttributeModifier, StatusRunnable {
        public StatusQLYMTListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 30;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            Character target = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .filterSelf()
                    .get(Attribute.LOCATION, CharacterFinder.Criteria.MAX);
            belongTo.doInteractive(interactive -> interactive.increaseLocation(target, 20));
            return false;
        }
    }
}
