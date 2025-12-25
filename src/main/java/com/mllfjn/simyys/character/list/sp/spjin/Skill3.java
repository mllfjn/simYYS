package com.mllfjn.simyys.character.list.sp.spjin;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

// √     攻击敌方目标6次,每次造成攻击70%伤害
// √     lv2-伤害增至80%
// √     lv3-伤害增至90%
// √     lv4-伤害增至100%
// √     lv5-攻击怪物时,自身与玄象的单次攻击内伤害系数递增23%

class Skill3 extends Skill {
    public static final String SkillName = "世间之曲";

    private int increaseTimes = 0;
    private int multiplier;

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);

        multiplier = switch (level) {
            case 4 -> 100;
            case 3 -> 90;
            case 2 -> 80;
            default -> 70;
        };
    }

    public void increaseMultiplier() {
        if (increaseTimes < 2) {
            multiplier += 12;
            increaseTimes++;
        }
    }

    public boolean isIncreasing(Character target) {
        return getLevel() >= 5 && target.isMob();
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        Character target = new CharacterFinder(belongTo)
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MAX);

        int currentMultiplier = multiplier;
        boolean increasing = isIncreasing(target);
        for (int i = 0; i < 6; i++) {
            interactive.attackTypical(this, target, currentMultiplier, AttackType.DAN_TI);
            if (increasing) {
                currentMultiplier += 23;
            }
        }

        return Optional.of(target);
    }
}
