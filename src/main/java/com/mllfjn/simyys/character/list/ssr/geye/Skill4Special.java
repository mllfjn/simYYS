package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill4Special extends Skill {
    private static final String SkillName = "终式·破空";
    private static final int[] additionalMultiplier = new int[]{100, 200, 360};

    public Skill4Special(Character belongTo) {
        super(belongTo, -1, 3, 0, 4);
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t恢复自身初始攻击100%的生命
                √\t攻击敌方全体3次,每次造成攻击300%伤害,伤害平均分配给敌方全体;
                \t\t这里是按照把伤害系数平均分配写的
                \t每幻化1名式神,末次伤害分别提升100%、200%、360%.
                \t\t提升代指不明确,按增加系数写的
                \t敌方人数每比友方初始人数减少1人,伤害衰减12%
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

        // 恢复生命
        interactive.recovery(this, belongTo, belongTo.getInitAttack());

        // 攻击3次
        List<Character> list = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();
        int enemyCount = list.size();

        // 前两次固定系数
        int multiplier = 300 / enemyCount;
        for (int i = 0; i < 2; i++) {
            interactive.attackTypical(this, list, multiplier, AttackType.QUN_TI);
        }

        // 第三次
        int thirdMultiplier = (
                300
                        + additionalMultiplier[belongTo.getStatus(StatusDaYao.class).orElseThrow().getStack()]
        ) / enemyCount;

        interactive.attackTypical(this, list, thirdMultiplier, AttackType.QUN_TI);

        return Optional.empty();
    }
}
