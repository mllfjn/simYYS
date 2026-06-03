package com.mllfjn.simyys.character.yuhun.list.youchizi;

import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

public class StatusYCZ extends Status implements Displayable {
    private int stack = 2;
    private final StatusAdder<?> adder;

    StatusYCZ(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        adder = character.bp.addStatusAdder(c ->
                c.team == character.team
                        ? new StatusYCZDefense(character, c)
                        : null
        );
        character.bp.getGuiHuoInstance(character.team).setYCZ(this);
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

    @Override
    public String getDisplayText() {
        return "灵元" + stack;
    }

    class StatusYCZDefense extends Status implements AttributeModifier {
        public StatusYCZDefense(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.DEFENCE;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return belongTo.getInitDefense() * StatusYCZ.this.stack * 0.08;
        }
    }
}