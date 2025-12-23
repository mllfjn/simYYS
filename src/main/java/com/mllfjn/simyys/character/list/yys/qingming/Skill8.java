package com.mllfjn.simyys.character.list.yys.qingming;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;

import java.util.List;
import java.util.Optional;

class Skill8 extends Skill {
    public static final String SkillName = "言灵·星";

    private final int duration;
    private final int bonus;

    public Skill8(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 0, 0, 8);
        duration = level >= 5 ? 3 : 2;
        int baseBonus = switch (level) {
            case 4 -> 30;
            case 3 -> 25;
            case 2 -> 20;
            default -> 15;
        };
        baseBonus += shuYin * 3;
        bonus = baseBonus;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        List<Character> list = new CharacterFinder(getBelongTo())
                .setTargetTeam(CharacterFinder.TargetTeam.TEAMMATE)
                .getList();

        for (Character character : list) {
            StatusXing.install(getBelongTo(), character, duration, bonus);
        }
        return Optional.empty();
    }

    static class StatusXing extends Status implements AttributeModifier, Displayable {
        public static final String StatusName = "星";

        private final int bonus;

        private StatusXing(Character from, Character belongTo, int duration, int bonus) {
            super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            this.bonus = bonus;

            setDurationType(StatusDurationType.CHI_XU, duration);
        }

        public static void install(Character from, Character belongTo, int duration, int bonus) {
            belongTo.getStatus(StatusXing.class).ifPresentOrElse(status -> {
                if (status.bonus < bonus) {
                    status.delete();
                } else if (status.bonus == bonus && status.getDuration() < duration) {
                    status.setDuration(duration);
                }
            }, () -> belongTo.addStatus(new StatusXing(from, belongTo, duration, bonus)));
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ZENG_SHANG;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            return bonus;
        }

        @Override
        public String getText() {
            return StatusName + getDuration();
        }
    }
}
