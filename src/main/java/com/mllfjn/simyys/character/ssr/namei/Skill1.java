package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill {
    public static final String SkillName = "湮灭";
    private static final int[] multiplier = new int[]{0, 100, 105, 110, 115, 125};
    public Skill1(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 1);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Character target = CharacterFinder.findPriorAuto(bp, CharacterFinder.getEnemyTeam(getBelongTo()), CharacterFinder.Property.HP, CharacterFinder.Criteria.MIN);
        lastUsedTarget = target;
        Interactive interactive = getBelongTo().getInteractive();

        // lv5-并有25%基础概率附加凋零,持续1回合
        // 没有测试是先上凋零还是先造成伤害,这里猜测是先凋零
        if (getLevel() == 5) {
            interactive.effect(StateDiaoLing.text, target, 25, (naMei, character) -> new StateDiaoLing(naMei, character, 1));
        }

        // 造成攻击(系数)伤害
        interactive.attack(SkillName, target, multiplier[getLevel()], AttackType.DAN_TI);

    }
}
