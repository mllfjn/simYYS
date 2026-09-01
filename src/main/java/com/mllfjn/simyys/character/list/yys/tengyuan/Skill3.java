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

    private final int shuYin;

    public Skill3(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 0, 0, 3);
        this.shuYin = shuYin;
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

        interactive.decreaseLocation(target, 50);

        if (level >= 2) {
            StatusReduceAttack.install(belongTo, target,
                    level >= 4 ? 0.5 : 0.25,
                    level >= 5 ? 0.5 : level >= 3 ? 0.25 : 0
            );
        }

        StatusLvYin lvYin = belongTo.getLvYin();
        lvYin.use(costLvYin);
        if (shuYin > 0) {
            lvYin.addStack(shuYin);
        }

        return Optional.of(target);
    }

    static class StatusReduceAttack extends Status {
        public StatusReduceAttack(Character from, Character belongTo, double attackRatio, double defenseRatio) {
            super(SkillName + "降低属性", from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);

            duration(StatusDurationType.CHI_XU, 2);
            displayNameAndDuration();
            attribute(Attribute.ATTACK, _ -> -belongTo.getInitAttack() * attackRatio);
            if (defenseRatio > 0) {
                attribute(Attribute.DEFENCE, _ -> -belongTo.getInitDefense() * defenseRatio);
            }
        }

        public static void install(Character from, Character belongTo, double attackRatio, double defenseRatio) {
            belongTo.getStatus(StatusReduceAttack.class)
                    .ifPresentOrElse(
                            status -> status.duration(2),
                            () -> belongTo.addStatus(new StatusReduceAttack(from, belongTo, attackRatio, defenseRatio))
                    );
        }
    }
}
