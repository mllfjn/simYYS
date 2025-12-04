package com.mllfjn.simyys.character.status;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

public interface Runnable {
    boolean runnable(Trigger trigger);

    /**
     *
     * @return return true if status should remove after run
     */
    boolean run(Trigger trigger, BattlePane bp, TriggerParam param);
}
