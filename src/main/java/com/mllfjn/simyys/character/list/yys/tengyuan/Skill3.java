package com.mllfjn.simyys.character.list.yys.tengyuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

class Skill3 extends Skill {
    static final String SkillName = "神鸟惊弦";
    private static final int costLvYin = 3;

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 3);
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t消耗3层律音
                √\t击退指定敌方目标50%行动条
                \t\t并附加缴械,持续1回合
                √\tlv2-额外降低目标25%初始攻击,持续2回合
                √\tlv3-额外降低目标25%初始防御,持续2回合
                √\tlv4-初始攻击降低增至50%
                √\tlv5-初始防御降低增至50%
                """;
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return ((TengYuanDaoZhang) getBelongTo()).getLvYin().canUse(costLvYin) && super.canUse(bp);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        int level = getLevel();
        TengYuanDaoZhang belongTo = (TengYuanDaoZhang) getBelongTo();
        Interactive interactive = belongTo.getInteractive();

        Character target = new CharacterFinder(belongTo)
                .filterEnemy()
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MIN);

        belongTo.getLvYin().use(costLvYin);
        interactive.decreaseLocation(target, 50);

        if (level >= 2) {
            StatusReduceAttack.install(belongTo, target, level >= 4 ? 0.5 : 0.25);
            if (level >= 3) {
                StatusReduceDefense.install(belongTo, target, level >= 5 ? 0.5 : 0.25);
            }
        }

        return Optional.of(target);
    }

    static class StatusReduceAttack extends Status implements AttributeModifier, Displayable {
        private final double ratio;

        public StatusReduceAttack(Character from, Character belongTo, double ratio) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            this.ratio = ratio;

            setDurationType(StatusDurationType.CHI_XU, 2);
        }

        public static void install(Character from, Character belongTo, double ratio) {
            belongTo.getStatus(StatusReduceAttack.class)
                    .ifPresentOrElse(
                            status -> status.setDuration(2),
                            () -> belongTo.addStatus(new StatusReduceAttack(from, belongTo, ratio))
                    );
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ATTACK;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            return -belongTo.getInitAttack() * ratio;
        }

        @Override
        public String getDisplayText() {
            return "攻击降低" + getDuration();
        }
    }

    static class StatusReduceDefense extends Status implements AttributeModifier, Displayable {
        private final double ratio;

        public StatusReduceDefense(Character from, Character belongTo, double ratio) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            this.ratio = ratio;

            setDurationType(StatusDurationType.CHI_XU, 2);
        }

        public static void install(Character from, Character belongTo, double ratio) {
            belongTo.getStatus(StatusReduceDefense.class)
                    .ifPresentOrElse(
                            status -> status.setDuration(2),
                            () -> belongTo.addStatus(new StatusReduceDefense(from, belongTo, ratio))
                    );
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.DEFENCE;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            return -belongTo.getInitDefense() * ratio;
        }

        @Override
        public String getDisplayText() {
            return "防御降低" + getDuration();
        }
    }
}
