package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3Special extends Skill {
    public static final String SkillName = "终焉裁决";

    public Skill3Special(Character belongTo) {
        super(belongTo, 0, 3, 0, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Interactive interactive = getBelongTo().getInteractive();
        // 对全体敌方造成攻击313%伤害
        List<Character> targets = new CharacterFinder(getBelongTo())
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getList();
        interactive.attackTypical(this, targets, 313, AttackType.QUN_TI);
        // 之后追击4次,每次对随机敌方目标造成攻击128%伤害
        for (int i = 0; i < 4; i++) {
            Character target = new CharacterFinder(getBelongTo())
                    .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                    .getRandom();
            if (target == null) {
                break;
            }
            interactive.attackTypical(this, target, 128, AttackType.DAN_TI);
        }
        return Optional.empty();
    }
}
