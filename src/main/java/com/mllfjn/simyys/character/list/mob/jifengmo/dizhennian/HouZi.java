package com.mllfjn.simyys.character.list.mob.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.InteractiveInfo;

import java.util.Optional;

public class HouZi extends Character {
    public static final String CharacterName = "猴子";

    public HouZi(BattlePane bp, int team) {
        this.name = CharacterName;
        this.bp = bp;
        this.team = team;
        setMob(1, 1);

        this.setInitDefense(352);
        addSkills();
    }

    @Override
    protected String getDefaultBaseAttack() {
        return CharacterName;
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new HouZiSkill(this));
    }

    static class HouZiSkill extends Skill {
        public static final String SkillName = "猴子飞踢";

        public HouZiSkill(Character belongTo) {
            super(belongTo, 0, 0, 0, 1);
        }

        @Override
        public String getName() {
            return SkillName;
        }

        @Override
        public Optional<Character> usePrivate(BattlePane bp) {
            Character belongTo = getBelongTo();
            Character target = new CharacterFinder(belongTo)
                    .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                    .getAutoOrElseRandom();

            InteractiveInfo info = InteractiveInfo.createRealAttack(belongTo, this, target
                    , (c1, c2) -> 5000.0);

            belongTo.getInteractive().attack(info, AttackType.ZHEN_SHI);

            return Optional.of(target);
        }
    }
}
