package com.mllfjn.simyys.battleevent;

import com.mllfjn.simyys.character.Character;

import java.io.Serializable;

public abstract class BattleActionListener implements Serializable {
    public final Character fromCharacter;

    public BattleActionListener(Character fromCharacter) {
        this.fromCharacter = fromCharacter;
    }

    /**
     * @return return true if this listener should be removed after invoke
     */
    public abstract boolean onBattleAction(BattleEvent event);
}
