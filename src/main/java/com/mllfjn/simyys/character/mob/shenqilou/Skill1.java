package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.ratecontroller.RateController;

class Skill1 extends Skill {
    private static final String SkillName = "极-钳击";

    public Skill1(Character belongTo) {
        super(belongTo, 0, 0, 0, 1);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        ShenQiLou belongTo = (ShenQiLou) getBelongTo();
        // 对单体敌人造成攻击100%的伤害
        Character target = RateController.choose("请选择攻击对象"
                , CharacterFinder.findEnemy(belongTo, bp.situation.characters)
                , Character::getName, belongTo.bp.isControlRate, belongTo.bp.calc);

        belongTo.getInteractive().attack(SkillName, target, 100, AttackType.DAN_TI);
        // 并降低敌人30%的暴击
        target.addState(new StateReduceCritRate(belongTo, target, 30));
    }
}
