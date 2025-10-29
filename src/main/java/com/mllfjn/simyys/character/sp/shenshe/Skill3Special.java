package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;

public class Skill3Special extends Skill {
    public static final String privateName = "终焉裁决";

    public Skill3Special(Character belongTo) {
        super(belongTo, 0, 3, 0, 3);
    }

    @Override
    public String getName() {
        return privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Interactive interactive = getBelongTo().getInteractive(bp);
        // 对全体敌方造成攻击313%伤害
        interactive.attack(privateName, CharacterFinder.findEnemy(getBelongTo(), bp.characters), 313, AttackType.QUN_TI);
        // 之后追击4次,每次对随机敌方目标造成攻击128%伤害
        for (int i = 0; i < 4; i++) {
            List<Character> enemy = CharacterFinder.findEnemy(getBelongTo(), bp.characters);
            if (enemy.isEmpty()) {
                return;
            }
            interactive.attack(privateName, RateController.choose(privateName, enemy, character -> character.name, bp.isControlRate), 128, AttackType.DAN_TI);
        }

    }
}
