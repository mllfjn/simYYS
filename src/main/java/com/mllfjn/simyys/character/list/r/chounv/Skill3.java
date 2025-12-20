package com.mllfjn.simyys.character.list.r.chounv;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

// √     召唤诅咒草人连接敌方目标,当草人受到伤害时,对被连接目标造成等量传导伤害,草人存在3回合
// √     草人继承目标10%生命,50%防御
// √     lv2-继承生命值增至15%
// √     lv3-继承生命值增至20%
// √     lv4-继承生命值增至25%
// √     lv5-继承生命值增至30%

class Skill3 extends Skill {
    public static final String SkillName = "草人替身";

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp) && bp.canSummon(CharacterFinder.getEnemyTeam(getBelongTo().team));
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Character target = new CharacterFinder(belongTo)
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MAX);

        bp.addCharacter(new CaoRen(belongTo, target, getLevel()));

        return Optional.of(target);
    }
}
