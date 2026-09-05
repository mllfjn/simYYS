package com.mllfjn.simyys.character.list.sp.spyue;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;
import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "炎崖";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 2);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {


        return Optional.empty();
    }

    private static class StatusYSHJ extends Status {
        public StatusYSHJ(Character character) {
            super("御神火界", character);
            duration(StatusDurationType.WEI_CHI, 3);

            StatusAdder<Status> adder = character.bp().addStatusAdder();
        }
    }
}

