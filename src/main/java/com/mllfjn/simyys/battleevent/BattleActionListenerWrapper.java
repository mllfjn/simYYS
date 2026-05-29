package com.mllfjn.simyys.battleevent;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.utils.SerializableConsumer;

public class BattleActionListenerWrapper extends BattleActionListener {
    private final SerializableConsumer<Character> action;

    public BattleActionListenerWrapper(Character fromCharacter, SerializableConsumer<Character> action) {
        super(fromCharacter);
        this.action = action;
    }

    @Override
    public boolean onBattleAction(BattleEvent event) {
        if (event instanceof EventAddCharacter eac) {
            action.accept(eac.getCharacter());
        }
        return false;
    }
}
