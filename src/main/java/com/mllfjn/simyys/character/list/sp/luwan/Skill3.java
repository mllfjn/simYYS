package com.mllfjn.simyys.character.list.sp.luwan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "断末无铭";
    private static final int[] multiplier = new int[]{0, 180, 200, 220, 240, 240};

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 4, 0, 3);
    }

    @Override
    protected int getCost() {
        Optional<StatusLuZe> oStatus = getBelongTo().getStatus(StatusLuZe.class);
        return oStatus.map(statusLuZe -> super.getCost() - statusLuZe.getStack())
                .orElseGet(super::getCost);
    }

    @Override
    public String getSkillDesc() {
        return """
                \t造成攻击(系数)伤害,驭魂形态下,同时对敌方全体附加麓蚀
                √\tlv2-4:增加系数
                √\tlv5-造成伤害前驱散敌方全体1个增益状态
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        List<Character> list = new CharacterFinder(getBelongTo())
                .filterEnemy()
                .getList();
        Interactive interactive = getBelongTo().getInteractive();
        if (getLevel() >= 5) {
            for (Character enemy : list) {
                enemy.dispelBuff(1);
            }
        }
        interactive.attackTypical(this, list, multiplier[getLevel()], AttackType.QUN_TI);

        getBelongTo().removeStatus(StatusLuZe.class);

        return Optional.empty();
    }
}
