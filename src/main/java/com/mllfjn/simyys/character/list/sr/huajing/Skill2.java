package com.mllfjn.simyys.character.list.sr.huajing;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill2 extends Skill {
    static final String SkillName = "齿甲";
    private static final int[] coefficient = new int[]{0, 12, 13, 14, 15, 15};

    public Skill2(HuaJing belongTo, int level, Skill3 skill3) {
        super(belongTo, level, 2, 0, 2);
        belongTo.bp.addPriorityMove(belongTo, () -> {
            StatusChiJia statusChiJia = new StatusChiJia(belongTo, belongTo, this);
            belongTo.addStatus(statusChiJia);
            belongTo.statusChiJia = statusChiJia;

            StatusTiJia statusTiJia = new StatusTiJia(belongTo, belongTo, skill3);
            belongTo.addStatus(statusTiJia);
            belongTo.statusTiJia = statusTiJia;
        });
        if (level < 5) {
            setCost(3);
        }
    }

    double getCoefficient() {
        return 0.01 * coefficient[getLevel()];
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        HuaJing belongTo = ((HuaJing) getBelongTo());
        Character target = new CharacterFinder(belongTo)
                .filterTeammate()
                .getPriorAuto(Attribute.ATTACK, CharacterFinder.Criteria.MAX);

        StatusChiJia statusChiJia = new StatusChiJia(belongTo, target, this);
        target.replaceStatus(statusChiJia);
        belongTo.statusChiJia = statusChiJia;

        return Optional.of(target);
    }
}
