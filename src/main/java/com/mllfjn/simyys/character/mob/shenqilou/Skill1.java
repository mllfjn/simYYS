package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.ratecontroller.RateController;

class Skill1 extends Skill {
    private static final String SkillName = "钳击";

    public Skill1(Character belongTo) {
        super(belongTo, 0, 0, 0, 1);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        // 对单体敌人造成攻击100%的伤害
        belongTo.getInteractive().attack(SkillName
                , RateController.choose("请选择攻击对象"
                        , CharacterFinder.findEnemy(belongTo, bp.situation.characters), Character::getName, belongTo.bp.isControlRate, belongTo.bp.calc)
                , 100, AttackType.DAN_TI);
    }
}
