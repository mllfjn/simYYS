package com.mllfjn.simyys.character.list.ssr.maochuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "洗筋伐髓";
    private static final double[] puGongDamageIncrease = {0, 1.2, 1.35, 1.35, 1.5, 1.5};

    private final Skill2 skill2;

    public Skill3(Character belongTo, int level, Skill2 skill2) {
        super(belongTo, level, 2, 0, 3);
        this.skill2 = skill2;

        if (level >= 5) {
            belongTo.bp.atBattleStart(this::useWithoutCost);
        }
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t召唤别馆私汤.
                √\t别馆私汤在场时,猫川的普攻系数翻倍,同时使友方全体造成的普攻伤害提升20%.
                √\t再次召唤别馆私汤时,恢复别馆私汤100%的生命并使友方全体享受一次温泉疗愈
                √\tlv2-造成的普攻伤害提升至35%
                √\tlv3-自身经过别馆私汤时,视为到达行动条终点
                √\tlv4-造成的普攻伤害提升至50%
                √\tlv5-先机:释放本技能(无消耗)
                √\t别馆私汤:固定在行动条70%的位置,免疫减益和控制效果,继承猫川攻击550%的生命值和100%防御,无法行动.
                \t\t若自身处于可行动状态,友方每使用普攻6次,泉水化猫攻击6次,每次对随机敌方目标造成攻击125%伤害
                \t\t不会触发敌方全体御魂效果,随后再次召唤别馆私汤
                """;
    }

    @Override
    public boolean canUse(BattlePane bp) {
        MaoChuan belongTo = (MaoChuan) getBelongTo();
        // 当存在别馆私汤
        return (belongTo.isBieGuanSiTangExist() || belongTo.bp.canSummon(belongTo.team)) && super.canUse(bp);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        MaoChuan belongTo = (MaoChuan) getBelongTo();
        CharacterBieGuanSiTang bieGuanSiTang = belongTo.getBieGuanSiTang();
        if (bieGuanSiTang == null) {
            belongTo.bp.addCharacter(new CharacterBieGuanSiTang(belongTo, puGongDamageIncrease[getLevel()], skill2));
        } else {
            bieGuanSiTang.refresh();
        }
        return Optional.empty();
    }
}
