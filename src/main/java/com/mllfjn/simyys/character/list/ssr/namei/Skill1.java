package com.mllfjn.simyys.character.list.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

class Skill1 extends Skill1PuGongBase {
    public static final String SkillName = "湮灭";
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
        Character target = new CharacterFinder(getBelongTo())
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getPriorAuto(CharacterFinder.Property.HP, CharacterFinder.Criteria.MIN);
        Interactive interactive = getBelongTo().getInteractive();

        // lv5-并有25%基础概率附加凋零,持续1回合
        // 没有测试是先上凋零还是先造成伤害,这里猜测是先凋零
        if (getLevel() == 5) {
            interactive.effect(StatusDiaoLing.text, target, 25, true
                    , (naMei, character) -> new StatusDiaoLing(naMei, character, 1));
        }

        // 造成攻击(系数)伤害
        interactive.attack(this, target, multiplier[getLevel()], AttackType.DAN_TI);

        return Optional.of(target);
    }
}
