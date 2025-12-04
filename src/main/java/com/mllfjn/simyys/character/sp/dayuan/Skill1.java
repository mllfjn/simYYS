package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

class Skill1 extends Skill {
    public static final String SkillName = "纺缘";
    private static final int[] multiplier = new int[]{0, 100, 105, 110, 115, 125};
    public Skill1(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 1);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character target = new CharacterFinder(getBelongTo())
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .get(CharacterFinder.Property.HP, CharacterFinder.Criteria.MIN);

        Interactive interactive = getBelongTo().getInteractive();
        // 用久违的神力攻击敌方目标,造成攻击(系数)的伤害
        interactive.attack(SkillName, target, multiplier[getLevel()], AttackType.DAN_TI);
        // lv5-获得1层神力
        if (getLevel() >= 5) {
            StatusShenLi.addStack(getBelongTo(), 1);
        }
        return Optional.of(target);
    }
}
