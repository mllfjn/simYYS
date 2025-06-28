package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.hit.AttackType;
import com.mllfjn.simyys.hit.Hit;

class Skill1 extends Skill {
    public static final String privateName = "纺缘";
    private static final int[] multiplier = new int[]{0, 100, 105, 110, 115, 125};
    public Skill1(Character belongTo, int level) {
        super(belongTo, level, 0, 0);
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Character target = CharacterFinder.findPriorAuto(bp, CharacterFinder.getEnemyTeam(getBelongTo()), CharacterFinder.Property.HP, CharacterFinder.Criteria.MIN);
        lastUsedTarget = target.name;

        Hit hit = new Hit(bp, getBelongTo());
        hit.attack(privateName, target, multiplier[getLevel()], AttackType.DAN_TI);

        if (getLevel() >= 5) {
            DaYuan daYuan = (DaYuan) getBelongTo();
            daYuan.addShenLi(1);
        }
    }

    @Override
    public void setName() {
        this.name = privateName;
    }
}
