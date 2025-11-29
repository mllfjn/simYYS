package com.mllfjn.simyys.status;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.trigger.Trigger;

public interface Runnable {
    boolean runnable(Trigger trigger);

    /**
     *
     * @return return true if status should remove after run
     */
    boolean run(Trigger trigger, BattlePane bp);
}
