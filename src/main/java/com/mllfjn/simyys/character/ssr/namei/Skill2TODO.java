package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.state.Flag;
import com.mllfjn.simyys.state.State;

import java.util.List;

class Skill2TODO extends Skill {
    public static final String privateName = "神赐之吻";
    public static final String FLAG_HUI_MIE = "Flag-毁灭";
    private boolean useFront = false;
    public Skill2TODO(Character belongTo) {
        super(belongTo, 0, 2, 0, 2);
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

        State state = getBelongTo().getState(FLAG_HUI_MIE);
        if (state != null) {
            if (state.from != target) {
                state.delete();
            }
            state.delete();
        }

        target.addState(new StateHuiMieTODO(getBelongTo(), target, getLevel()));
        getBelongTo().addState(new Flag(target, getBelongTo(), FLAG_HUI_MIE));
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
    public String getName() {
        return privateName;
    }
}
