package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "礼教";
    private static final int[] multiplier = new int[]{0, 100, 105, 110, 115, 125};

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        LaoTou laoTou = (LaoTou) getBelongTo();
        Interactive interactive = laoTou.getInteractive();
        Character target = new CharacterFinder(laoTou)
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getPriorAuto(CharacterFinder.Property.HP, CharacterFinder.Criteria.MIN);

        // 造成攻击（系数）伤害
        interactive.attack(this, target, multiplier[getLevel()], AttackType.DAN_TI);
        return Optional.of(target);
    }
}
