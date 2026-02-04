package com.mllfjn.simyys.character.list.ssr.beimihu;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

// √     攻击敌方全体2次，每次造成攻击80%伤害
// √     lv2-伤害增至100%
//      lv3-敌方目标时之缝移除时无消耗释放,该次伤害降低30%
// √     lv4-伤害增至120%
// √     lv5-伤害无视御魂效果

class Skill3 extends Skill {
    private static final String SkillName = "日耀时辉";

    private final int multiplier;

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 3);

        multiplier = level >= 4 ? 120 : level >= 2 ? 100 : 80;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        BeiMiHu belongTo = (BeiMiHu) getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        List<Character> targets = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();

        for (int i = 0; i < 2; i++) {
            interactive.attack(this, targets, character -> {
                AttackInfo info = AttackInfo
                        .createTypicalAttack(belongTo, this, character, multiplier, AttackType.QUN_TI);
                if (getLevel() >= 5) {
                    info.setCalYuHun(false);
                }
                return info;
            });
        }

        belongTo.getShiZhiHuiCarrier().ifPresent(StatusShiZhiHui::useSkill3);

        return Optional.empty();
    }
}
