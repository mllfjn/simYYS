package com.mllfjn.simyys.character.yuhun.list.youchizi;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;

import java.util.List;

public class StatusYCZ extends Status implements Displayable {
    private int stack = 2;
    BattleActionListener listener;

    StatusYCZ(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        listener = character.bp.forEveryone(character, c -> {
            if (c.team == character.team) {
                c.addStatus(new StatusYCZDefense(character, c));
            }
        });
        character.bp.getGuiHuoInstance(character.team).setYCZ(this);
    }

    public void use(int usedCount) {
        if (usedCount == stack) {
            List<Character> list = new CharacterFinder(belongTo, true)
                    .filterTeammate()
                    .getList();
            for (Character character : list) {
                character.removeStatus(StatusYCZDefense.class);
            }
            belongTo.bp.removeActionListener(belongTo, listener);
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