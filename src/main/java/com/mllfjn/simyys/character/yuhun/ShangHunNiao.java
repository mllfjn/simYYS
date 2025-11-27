package com.mllfjn.simyys.character.yuhun;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.*;
import com.mllfjn.simyys.trigger.BattleActionListener;
import com.mllfjn.simyys.trigger.battleevent.EventCharacterDie;

public class ShangHunNiao extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "伤魂鸟";
    private final BattleActionListener listener;

    public ShangHunNiao() {
        listener = event -> {
            // 任意非怪物目标死亡时,治疗生命上限20%的生命
            if (event instanceof EventCharacterDie ed && !ed.getCharacter().isMob()) {
                Character belongTo = getBelongTo();
                belongTo.getInteractive().heal(YuHunName, belongTo, 20);
                // 并提升20%伤害
                StateShangHunNiao.addStack(belongTo);
            }
            return false;
        };
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void enable() {
        getBelongTo().bp.addActionTrigger(getBelongTo(), listener);
    }

    @Override
    public void disable() {
        getBelongTo().bp.removeActionTrigger(getBelongTo(), listener);
    }
}

class StateShangHunNiao extends State implements AttributeModifier, Displayable {
    private int stack;

    public StateShangHunNiao(Character character) {
        super(character, character, StateType.BUFF, StateForm.YIN_JI);
    }

    public static void addStack(Character character) {
        character.getState(StateShangHunNiao.class)
                .or(() -> character.addState(new StateShangHunNiao(character)))
                .ifPresent(state -> {
                    if (state.stack < 6) {
                        state.stack++;
                    }
                });
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.ZENG_SHANG;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return 20 * stack;
    }

    @Override
    public String getText() {
        return "伤" + stack;
    }
}