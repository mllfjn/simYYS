package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusBind;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.StatusSupplier;
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
                √\t使目标中毒和速度降低20点,持续1回合
                √\t并有33%的概率额外击退目标25%的行动条
                √\t\t有20%基础概率束缚敌方1回合
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        List<Character> list = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();

        // 因为有可能回合外释放，保险起见用doInteractive
        belongTo.doInteractive(interactive -> {
            interactive.attackTypical(this, list, 100, AttackType.QUN_TI);

            for (Character character : list) {
                StatusTZZPoisoning.addTZZPoisoning(belongTo, character, 1);
                if (RateController.otherWhether(SkillName + "击退" + character.name + "行动条",
                        "击退", belongTo.bp.calc, 33
                )) {
                    interactive.decreaseLocation(character, 25);
                }
            }

            interactive.effect(this, list, 100, true, StatusTZZReduceSpeed.getSupplier());
            interactive.effect(this, list, 20, true, StatusBind.getSupplier(1));
        });

        return Optional.empty();
    }

    static class StatusTZZReduceSpeed extends Status implements AttributeModifier, Displayable {
        private static final String StatusName = "减速";

        private StatusTZZReduceSpeed(Character from, Character belongTo) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        }

        static StatusSupplier getSupplier() {
            return new StatusSupplier(StatusName, StatusTZZReduceSpeed.class,
                    (from, to) -> {
                        if (to.getStatus(StatusTZZReduceSpeed.class).isEmpty()) {
                            to.addStatus(new StatusTZZReduceSpeed(from, to));
                        }
                    }
            );
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return -20;
        }

        @Override
        public String getDisplayText() {
            return StatusName;
        }
    }
}
