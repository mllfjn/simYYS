package com.mllfjn.simyys.character.list.ssr.namei;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "湮灭";
    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        // lv5-并有25%基础概率附加凋零,持续1回合
        // 没有测试是先上凋零还是先造成伤害,这里猜测是先凋零
        if (getLevel() >= 5) {
            interactive.effect(this, target, 25, 0, true, StatusDiaoLing.getSupplier(1));
        }

        // 造成攻击(系数)伤害
        super.usePrivate(interactive, target);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
