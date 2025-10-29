package com.mllfjn.simyys.trigger;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

public abstract class BattleActionListener {
    public abstract void onBattleAction(BattleActionTrigger trigger, Character character, BattlePane bp);
}
