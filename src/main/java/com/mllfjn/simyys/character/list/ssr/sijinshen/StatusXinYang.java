package com.mllfjn.simyys.character.list.ssr.sijinshen;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;

import java.util.List;

class StatusXinYang extends Status implements Displayable {
    private int stack;

    private final boolean getNewRoundWhenXinYangRemoved;

    StatusXinYang(Character character, boolean getNewRoundWhenXinYangRemoved) {
        super(character, character, StatusType.BUFF, StatusForm.YIN_JI);
        this.getNewRoundWhenXinYangRemoved = getNewRoundWhenXinYangRemoved;
    }

    @Override
    public void beforeDelete() {
        ((SiJinShen) belongTo).statusXinYang = null;
    }

    void addStack() {
        if (stack == 11) {
            stack = 0;
            List<Character> list = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .getList();
            double shield = belongTo.getMaxHp() * 0.16;
            for (Character character : list) {
                character.addStatus(new StatusShield(belongTo, character, shield));
            }
            if (getNewRoundWhenXinYangRemoved) {
                ((SiJinShen) belongTo).qm.getMaxAttacker().ifPresent(c ->
                        belongTo.doInteractive(interactive -> interactive.getNewRound(c))
                );
            }
        }
    }

    @Override
    public String getDisplayText() {
        if (stack != 0) {
            return "信仰" + stack;
        } else {
            return null;
        }
    }
}
