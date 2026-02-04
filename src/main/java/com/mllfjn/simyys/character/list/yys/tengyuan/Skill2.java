package com.mllfjn.simyys.character.list.yys.tengyuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

class Skill2 extends Skill {
    public static final String SkillName = "破阵奏曲";

    private final int chargeMultiplier;
    private final int chargeDecrease;
    private final boolean getLvYin;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 2);

        chargeMultiplier = level >= 4 ? 240 : (level >= 2 ? 200 : 160);
        chargeDecrease = level >= 5 ? 20 : 10;
        getLvYin = level >= 3;
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t攻击敌方目标2次,每次造成攻击80%伤害并击退其5%行动条
                √\t蓄力攻击目标造成攻击160%伤害并击退其10%行动条
                √\tlv2-蓄力攻击伤害增加至200%
                √\tlv3-释放时获得1层律音
                √\tlv4-蓄力攻击伤害增加至240%
                √\tlv5-蓄力攻击击退行动条效果增至20%
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        TengYuanDaoZhang belongTo = (TengYuanDaoZhang) getBelongTo();
        Interactive interactive = belongTo.getInteractive();

        Character target = new CharacterFinder(belongTo)
                .filterEnemy()
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MIN);

        for (int i = 0; i < 2; i++) {
            interactive.attackTypical(this, target, 80, AttackType.DAN_TI);
            interactive.decreaseLocation(target, 5);
        }

        interactive.attackTypical(this, target, chargeMultiplier, AttackType.DAN_TI);
        interactive.decreaseLocation(target, chargeDecrease);

        if (getLvYin) {
            belongTo.getLvYin().addStack(1);
        }

        return Optional.of(target);
    }
}
