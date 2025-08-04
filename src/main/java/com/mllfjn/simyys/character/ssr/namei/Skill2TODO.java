package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.state.State;

import java.util.List;

public class Skill2TODO extends Skill {
    public static final String privateName = "神赐之吻";
    private boolean useFront = false;
    public Skill2TODO(Character belongTo) {
        super(belongTo, 0, 2, 0);
    }

    public void useFront(BattlePane bp) {
        useFront = true;
        this.useWithoutCost(bp);
        useFront = false;
    }
    @Override
    public void usePrivate(BattlePane bp) {
        Character target = getTarget(bp);
        lastUsedTarget = target;

        State state = getBelongTo().getState(StateFlagHuiMie.privateName);
        if (state != null) {
            if (state.from != target) {
                state.from.deleteState(StateHuiMieTODO.privateName);
            }
            state.delete();
        }

        target.addState(new StateHuiMieTODO(getBelongTo(), target, getLevel()));
        getBelongTo().addState(new StateFlagHuiMie(target, getBelongTo()));
    }

    private Character getTarget(BattlePane bp) {
        List<Character> teammates = CharacterFinder.findTeammate(getBelongTo(), bp.characters);
        teammates.remove(getBelongTo());
        if (useFront) {
            teammates.removeIf(Character::isYYS);
            return CharacterFinder.find(teammates, getBelongTo().team, CharacterFinder.Property.ATTACK, CharacterFinder.Criteria.MAX);
        } else {
            return CharacterFinder.findPriorAuto(teammates, bp, getBelongTo().team, CharacterFinder.Property.ATTACK, CharacterFinder.Criteria.MAX);
        }
    }

    @Override
    public int getSkillID() {
        return 2;
    }

    @Override
    public String getName() {
        return privateName;
    }
}
