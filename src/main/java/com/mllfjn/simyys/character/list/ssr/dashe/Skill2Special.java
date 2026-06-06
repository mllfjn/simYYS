package com.mllfjn.simyys.character.list.ssr.dashe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.AttributeModifier;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.Optional;

class Skill2Special extends Skill {
    private static final String SkillName = "八岐之影";

    private final Character target;
    private final Skill2 skill2;

    public Skill2Special(Character belongTo, Character target, Skill2 skill2) {
        super(belongTo, -1, 0, 0, 2);
        this.target = target;
        this.skill2 = skill2;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        DaShe belongTo = (DaShe) getBelongTo();

        target.die();
        skill2.convert(belongTo);
        belongTo.getInteractive().getNewRound(belongTo);
        belongTo.addStatus(new StatusBQZY(belongTo));
        belongTo.removeSkill(this);

        return Optional.of(target);
    }

    static class StatusBQZY extends Status implements AttributeModifier, InfluenceDamageWhenAttack {
        public StatusBQZY(Character character) {
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
        public void doInfluenceWhenAttack(AttackInfo attackInfo) {
            attackInfo.getTraceableNumber().mul(1.3, SkillName);
        }
    }
}
