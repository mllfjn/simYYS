package com.mllfjn.simyys.character.list.sr.yaoqin;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;

import java.util.List;
import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "余音";
    private static final int[] bonus = new int[]{0, 8, 11, 14, 17, 20};

    private final int currentBonus;


    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 2);
        currentBonus = bonus[getLevel()];
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        // 为了配合地震鲶里不会加速的妖琴，这里把两个效果分开了

        addYuYin();
        return Optional.of(newRound());
    }

    protected Character newRound() {
        Character target = new CharacterFinder(getBelongTo())
                .filterTeammate()
                .getAutoOrElseRandom();

        getBelongTo().getInteractive().getNewRound(target);

        return target;
    }

    private void addYuYin() {
        List<Character> targets = new CharacterFinder(getBelongTo())
                .filterTeammate()
                .getList();

        for (Character target : targets) {
            target.addStatus(new StatusYuYin(getBelongTo(), target, currentBonus));
        }
    }

    static class StatusYuYin extends Status implements AttributeModifier {
        private final int num;

        public StatusYuYin(Character from, Character belongTo, int num) {
            super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            this.num = num;

            duration(StatusDurationType.CHI_XU, 1);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return num;
        }
    }
}
