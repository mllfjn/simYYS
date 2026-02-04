package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.list.sr.yaoqin.YaoQin;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusCanNotChoose;
import com.mllfjn.simyys.character.status.instance.StatusRejectAllStatusesInstance;

import java.util.Optional;

class SpecialYaoQin extends YaoQin {
    private SpecialYaoQin(BattlePane bp, int team) {
        this.name = CharacterName;
        this.bp = bp;
        this.team = team;

        this.setMaxHp(100, true);
        this.setInitBaseAttack(3483);
        this.setInitSpeed(100);

        skill1Level = 5;
        fillSkills();
        removeSkill(2);
        removeSkill(3);

        addSkill(new SpecialYaoQinSkill2(this));
        addStatus(new StatusCanNotChoose(this, this));
        addStatus(new StatusRejectAllStatusesInstance(this));
    }

    private SpecialYaoQin(Character character) {
        this(character.bp, character.team);
        this.lockSkillMap = character.getLockSkillMap();
        this.flagChangeMap = character.getFlagChangeMap();
    }

    public static void add(BattlePane bp, int team) {
        for (Character character : bp.situation.characters) {
            if (character.getInitSpeed() == 100
                    && character.team != team
                    && character instanceof YaoQin
            ) {
                bp.addCharacter(new SpecialYaoQin(character));
                bp.removeCharacterWithoutTrigger(character);
                return;
            }
        }
        bp.addCharacter(new SpecialYaoQin(bp, CharacterFinder.getEnemyTeam(team)));
    }

    static class SpecialYaoQinSkill2 extends Skill {
        public static final String SkillName = "余音";

        public SpecialYaoQinSkill2(com.mllfjn.simyys.character.Character belongTo) {
            super(belongTo, 5, 0, 15, 2);
        }

        @Override
        public String getName() {
            return SkillName;
        }

        @Override
        public Optional<Character> usePrivate(BattlePane bp) {
            Character belongTo = getBelongTo();
            Character target = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .getAutoOrElseRandom();

            target.addStatus(new StatusAttack(belongTo, target));

            belongTo.getInteractive().getNewRound(target);

            return Optional.of(target);
        }

        static class StatusAttack extends Status implements AttributeModifier, Displayable {

            public StatusAttack(Character from, Character belongTo) {
                super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
                setDurationType(StatusDurationType.CHI_XU, 2);
            }

            @Override
            public boolean isAffectAttribute(Attribute attribute) {
                return attribute == Attribute.ATTACK;
            }

            @Override
            public double getInfluence(Attribute attribute) {
                return belongTo.getInitBaseAttack();
            }

            @Override
            public String getDisplayText() {
                return "妖琴攻击" + getDuration();
            }
        }
    }
}
