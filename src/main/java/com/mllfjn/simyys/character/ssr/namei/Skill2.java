package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.List;

class Skill2 extends Skill {
    public static final String privateName = "神赐之吻";
    private boolean useFront = false;
    private final boolean awakening;
    public Skill2(Character belongTo, boolean awakening) {
        super(belongTo, 0, 2, 0, 2);
        this.awakening = awakening;
    }

    public void useFront(BattlePane bp) {
        useFront = true;
        this.useWithoutCost(bp);
        useFront = false;
    }
    @Override
    public void usePrivate(BattlePane bp) {
        Character target = getTarget(bp);
        lastUsedTarget = target;

        // 指定友方目标施加毁灭
        if (!target.isHaveState(StateHuiMie.privateName)) {
            target.addState(new StateHuiMie((NaMei) getBelongTo(), target, getLevel(), awakening));
        } else { // 若该目标已处于毁灭,则使其额外失去最大生命50%的生命(该效果不致命)
            target.setHp(Math.max(0, target.getHp() - target.getMaxHp() * 0.5));
        }

    }

    private Character getTarget(BattlePane bp) {
        List<Character> teammates = CharacterFinder.findTeammate(getBelongTo(), bp.characters);
        // 为自身以外的
        teammates.remove(getBelongTo());
        // 先机时只能给攻击最高的友方式神,其他时候可以给除自身以外的任意友方包括阴阳师
        if (useFront) {
            teammates.removeIf(Character::isYYS);
            return CharacterFinder.find(teammates, getBelongTo().team, CharacterFinder.Property.ATTACK, CharacterFinder.Criteria.MAX);
        } else {
            return CharacterFinder.findPriorAuto(teammates, bp, getBelongTo().team, CharacterFinder.Property.ATTACK, CharacterFinder.Criteria.MAX);
        }
    }

    @Override
    public String getName() {
        return privateName;
    }
}
