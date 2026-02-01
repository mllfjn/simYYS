package com.mllfjn.simyys.character.list.yys.qiling;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.List;

class QiLingZhenMuShou {
    public static final String QiLingName = "镇墓兽";

    public static void install(Character character) {
        character.addStatus(new StatusQLZMUListener(character));
    }

    static class StatusQLZMUListener extends Status implements StatusRunnable {

        public StatusQLZMUListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.HP_CHANGE && belongTo.getHp() < belongTo.getMaxHp() * 0.7;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            List<Character> list = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .getList();
            for (Character target : list) {
                target.addStatus(new StatusQiCritPower(belongTo, target));
            }
            return true;
        }

        static class StatusQiCritPower extends Status implements AttributeModifier, Displayable {

            public StatusQiCritPower(Character from, Character belongTo) {
                super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            }

            @Override
            public boolean isAffectAttribute(Attribute attribute) {
                return attribute == Attribute.CRIT_POWER;
            }

            @Override
            public double getInfluence(Attribute attribute) {
                return 30;
            }

            @Override
            public String getDisplayText() {
                return "契镇";
            }
        }
    }
}
