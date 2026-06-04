package com.mllfjn.simyys.character.list.ssr.axiuluo;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "炼狱虐杀";
    private static final int[] multiplier = new int[]{0, 290, 305, 315, 315, 315};

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        AXiuLuo belongTo = (AXiuLuo) getBelongTo();
        Interactive interactive = belongTo.getInteractive();

        Character target = new CharacterFinder(belongTo)
                .filterEnemy()
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MIN);

        List<Character> list = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();

        belongTo.statusLiXing.consume(list.size());
        interactive.attackTypical(this, list, multiplier[getLevel()], AttackType.QUN_TI);

        Character currentTarget = target;
        int pursuitMultiplier = getLevel() >= 4 ? 63 : 54;
        while (belongTo.statusLiXing.getStack() > 0) {
            if (currentTarget.alive) {
                belongTo.statusLiXing.consume(1);
                interactive.attackTypical(this, currentTarget, pursuitMultiplier, AttackType.DAN_TI);
            } else if (getLevel() >= 5) {
                // lv5切换目标
                currentTarget = new CharacterFinder(belongTo)
                        .filterEnemy()
                        .get(Attribute.HP, CharacterFinder.Criteria.MIN);
                // 如果为空代表杀完了
                if (currentTarget == null) {
                    break;
                }
            } else {
                // 没到lv5就结束
                break;
            }
        }


        return Optional.of(target);
    }
}
