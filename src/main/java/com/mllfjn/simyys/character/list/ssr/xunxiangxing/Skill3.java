package com.mllfjn.simyys.character.list.ssr.xunxiangxing;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "菩提愿";
    private static final int[] multiplier = new int[]{0, 80, 85, 90, 95, 100};

    private int enemyCountAtStart;

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);
        if (level >= 5) {
            belongTo.bp.atBattleStart(() ->
                    enemyCountAtStart = belongTo.bp.situation.teamPane[1 - belongTo.team].characters.size()
            );
        }
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t对敌方全体造成攻击80, 85, 90, 95, 100间接伤害
                √\t(觉醒)结界中释放对怪物必定附加附魂香;
                √\tlv5-与战斗开始时相比,每少一个敌方,伤害提升12%,最多提升60%
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        XunXiangXing belongTo = ((XunXiangXing) getBelongTo());
        Interactive interactive = belongTo.getInteractive();
        List<Character> list = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();


        boolean increaseDamage;
        double increasement;
        if (getLevel() >= 5 && list.size() < enemyCountAtStart) {
            increaseDamage = true;
            increasement = 1 + (enemyCountAtStart - list.size()) * 0.12;
        } else {
            increaseDamage = false;
            increasement = 1;
        }

        interactive.attack(this, list, c -> {
            AttackInfo attackInfo = AttackInfo.createJianJieAttack(belongTo, this, c, belongTo.getAttack());
            attackInfo.setMultiplier(multiplier[getLevel()]);
            if (increaseDamage) {
                attackInfo.getTraceableNumber().mul(increasement, "战斗减员加成");
            }
            return attackInfo;
        });

        belongTo.getStatus(StatusHuanJing.class).ifPresent(StatusHuanJing::usedSkill3);

        return Optional.empty();
    }
}
