package com.mllfjn.simyys.trigger;

import java.io.Serializable;

public interface BattleActionListener extends Serializable {
    /**
     *
     * @return return true if this event should be removed after use
     */
    boolean onBattleAction(TriggerEvent event);
}
