package com.mllfjn.simyys.character.yuhun.list.youchizi;

import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

public class StatusYCZ extends Status {
    private int stack = 2;
    private final StatusAdder<?> adder;

    StatusYCZ(Character character) {
        super("灵元", character);
        adder = character.bp.addStatusAdder(c ->
                c.team == character.team
                        ? new StatusYCZDefense(character, c)
                        : null
        );
        character.bp.getGuiHuoInstance(character.team).setYCZ(this);
        display(() -> "灵元" + stack);
    }

    public void use(int usedCount) {
        if (usedCount == stack) {
            adder.deleteAndRemove();
            belongTo.bp.getGuiHuoInstance(belongTo.team).setYCZ(null);
            delete();
        } else {
            stack -= usedCount;
        }
    }

    public int maxUse() {
        return stack;
    }

    class StatusYCZDefense extends Status {
        public StatusYCZDefense(Character from, Character belongTo) {
            super(YouChiZi.YuHunName + "防御", from, belongTo);
            attribute(Attribute.DEFENCE, _ -> belongTo.getInitDefense() * StatusYCZ.this.stack * 0.08);
        }
    }
}