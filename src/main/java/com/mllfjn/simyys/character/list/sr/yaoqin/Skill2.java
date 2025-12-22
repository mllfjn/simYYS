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
    public static final String SkillName = "余音";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 2);
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
                .setTargetTeam(CharacterFinder.TargetTeam.TEAMMATE)
                .getAutoOrElseRandom();

        getBelongTo().getInteractive().getNewRound(target);

        return target;
    }

    private void addYuYin() {
        List<Character> targets = new CharacterFinder(getBelongTo())
                .setTargetTeam(CharacterFinder.TargetTeam.TEAMMATE)
                .getList();

        int num = switch (getLevel()) {
            case 1 -> 8;
            case 2 -> 11;
            case 3 -> 14;
            case 4 -> 17;
            case 5 -> 20;
            default -> throw new IllegalArgumentException("妖琴技能等级错误: " + getLevel());
        };

        for (Character target : targets) {
            target.addStatus(new StatusYuYin(getBelongTo(), target, num));
        }
    }

    static class StatusYuYin extends Status implements AttributeModifier {
        private final int num;

        public StatusYuYin(Character from, Character belongTo, int num) {
            super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            this.num = num;

            setDurationType(StatusDurationType.CHI_XU, 1);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            return num;
        }
    }
}
