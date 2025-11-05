package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;

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
    public void usePrivate(BattlePane bp) {
        Interactive interactive = getBelongTo().getInteractive();
        // 对全体敌方造成攻击313%伤害
        interactive.attack(SkillName, CharacterFinder.findEnemy(getBelongTo(), bp.situation.characters), 313, AttackType.QUN_TI);
        // 之后追击4次,每次对随机敌方目标造成攻击128%伤害
        for (int i = 0; i < 4; i++) {
            List<Character> enemy = CharacterFinder.findEnemy(getBelongTo(), bp.situation.characters);
            if (enemy.isEmpty()) {
                return;
            }
            interactive.attack(SkillName, RateController.choose(SkillName, enemy, character -> character.name, bp.isControlRate, bp.calc), 128, AttackType.DAN_TI);
        }

    }
}
