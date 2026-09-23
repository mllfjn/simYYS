package com.mllfjn.simyys.character.list.sp.fuji;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

class StatusFJReduceDefense extends Status {
    private static final String StatusName = "缚姬减防";

    private int stack = 1;

    private StatusFJReduceDefense(Character from, Character belongTo, int defensePerStack) {
        super(StatusName, from, belongTo);
        attribute(Attribute.DEFENCE, _ -> (double) (-defensePerStack * stack));
        display(() -> StatusName + stack);
    }

    public static void addStack(Character from, Character belongTo, int defensePerStack) {
        belongTo.getStatus(StatusFJReduceDefense.class)
                .ifPresentOrElse(
                        StatusFJReduceDefense::addStack,
                        () -> belongTo.addStatus(new StatusFJReduceDefense(from, belongTo, defensePerStack))
                );
    }

    private void addStack() {
        if (stack < 10) {
            stack++;
        }
    }
}
