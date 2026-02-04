package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;
import java.util.Optional;

class Skill4 extends Skill {
    private static final String SkillName = "天罗地网·极";

    public Skill4(Character belongTo) {
        super(belongTo, -1, 3, 0, 4);
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t对全体敌方单位造成攻击力100%伤害
                \t使目标中毒和速度降低20点,持续1回合(没写概率,但是可以被抵抗的)
                √\t并有33%的概率额外击退目标25%的行动条
                \t有20%基础概率束缚敌方1回合
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();

        List<Character> list = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();

        interactive.attackTypical(this, list, 100, AttackType.QUN_TI);

        for (Character character : list) {
            if (RateController.otherWhether(SkillName + "击退" + character.name + "行动条",
                    "击退", belongTo.bp.calc, 33
            )) {
                interactive.decreaseLocation(character, 25);
            }
        }

        return Optional.empty();
    }
}
