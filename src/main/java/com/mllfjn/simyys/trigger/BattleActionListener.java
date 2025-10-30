package com.mllfjn.simyys.trigger;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

public interface BattleActionListener {
    void onBattleAction(BattleActionTrigger trigger, Character character, BattlePane bp);

    /**
     * 生效时可以调用该方法通知添加者
     */
    default void response(BattlePane bp) {}
}
