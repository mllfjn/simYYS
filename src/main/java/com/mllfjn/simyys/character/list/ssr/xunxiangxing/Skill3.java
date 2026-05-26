package com.mllfjn.simyys.character.list.ssr.xunxiangxing;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "菩提愿";
    private static final int[] multiplier = new int[]{0, 80, 85, 90, 95, 100};

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);
    }

    @Override
    public String getSkillDesc() {
        return """
                \t对敌方全体造成攻击80, 85, 90, 95, 100间接伤害
                \t(觉醒)结界中释放对怪物必定附加附魂香;
                \tlv5-与战斗开始时相比,每少一个敌方,伤害提升12%,最多提升60%
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        return Optional.empty();
    }
}
