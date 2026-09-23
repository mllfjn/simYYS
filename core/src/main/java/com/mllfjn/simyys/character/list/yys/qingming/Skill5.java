package com.mllfjn.simyys.character.list.yys.qingming;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;

import java.util.List;
import java.util.Optional;

class Skill5 extends Skill {
    static final String SkillName = "符咒·灭";

    private static final int[] baseBonus = new int[]{0, 15, 20, 25, 30, 30};

    private final int duration;
    private final int bonus;

    public Skill5(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 0, 0, 5);

        duration = level >= 5 ? 3 : 2;

        bonus = baseBonus[level] + shuYin * 3;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {

        List<Character> list = new CharacterFinder(getBelongTo())
                .filterEnemy()
                .getList();

        for (Character character : list) {
            StatusMie.install(getBelongTo(), character, duration, bonus);
        }
        return Optional.empty();
    }

    static class StatusMie extends Status {
        private static final String StatusName = "灭";
        private final int bonus;

        public StatusMie(Character from, Character belongTo, int duration, int bonus) {
            super(StatusName, from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);

            this.bonus = bonus;
            duration(StatusDurationType.CHI_XU, duration);
            displayNameAndDuration();
            attribute(Attribute.YI_SHANG, bonus);
        }

        public static void install(Character from, Character belongTo, int duration, int bonus) {
            belongTo.getStatus(StatusMie.class).ifPresentOrElse(status -> {
                if (status.bonus < bonus) {
                    status.delete();
                    belongTo.addStatus(new StatusMie(from, belongTo, duration, bonus));
                } else if (status.bonus == bonus && status.getDuration() < duration) {
                    status.duration(duration);
                }
            }, () -> belongTo.addStatus(new StatusMie(from, belongTo, duration, bonus)));
        }
    }
}
