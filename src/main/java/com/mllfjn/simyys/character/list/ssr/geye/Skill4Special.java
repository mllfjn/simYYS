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
    private static final double[] coefficient = new double[]{0, 2, 3, 4.6};

    private final int initTeammateCount;

    public Skill4Special(Character belongTo, int initTeammateCount) {
        super(belongTo, -1, 3, 0, 4);
        this.initTeammateCount = initTeammateCount;
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t恢复自身初始攻击100%的生命
                √\t攻击敌方全体3次,每次造成攻击300%伤害,伤害平均分配给敌方全体;
                √\t每幻化1名式神,末次伤害分别提升100%、200%、360%.
                √\t敌方人数每比友方初始人数减少1人,伤害衰减12%
                
                \t\t平均分配、提升伤害、伤害衰减作用方式未测试，这里按照更改伤害系数写的
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
        if (enemyCount < initTeammateCount) {
            multiplier = (int) (multiplier * (1 - (initTeammateCount - enemyCount) * 0.12));
        }

        for (int i = 0; i < 2; i++) {
            interactive.attackTypical(this, list, multiplier, AttackType.QUN_TI);
        }

        // 第三次
        int thirdMultiplier = (int) (
                multiplier * coefficient[belongTo.getStatus(StatusDaYao.class).orElseThrow().getStack()]
        );

        interactive.attackTypical(this, list, thirdMultiplier, AttackType.QUN_TI);

        return Optional.empty();
    }
}
