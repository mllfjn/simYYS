package com.mllfjn.simyys.character.list.sp.guitun;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;

class Skill1Special extends Skill1PuGongBase {
    private static final int[] MULTIPLIER = new int[]{0, 196, 206, 216, 226, 245};
    private static final double[] RATIO = new double[]{0, 0.2, 0.25, 0.3, 0.35, 0.4};

    public Skill1Special(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public String getName() {
        return "烈焰焚天";
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        Character belongTo = getBelongTo();
        int level = getLevel();

        AttackInfo attackInfo = interactive.attackTypical(this, target, MULTIPLIER[level], AttackType.DAN_TI);
        double damage = attackInfo.getTraceableNumber().getNumber();
        if (damage > 0) {
            List<Character> enemy = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .getList();
            enemy.remove(target);
            double d = damage * RATIO[level];
            interactive.attack(this, enemy, c -> {
                AttackInfo info = AttackInfo.createRealAttack(belongTo, this, c, d);
                info.setNotCalYuHun();
                return info;
            });
        }
    }
}
