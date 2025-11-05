package com.mllfjn.simyys.state;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.trigger.Trigger;

public interface Runnable {
    boolean runnable(Trigger trigger);

    /**
     *
     * @return return true if state should remove after run
     */
    boolean run(Trigger trigger, BattlePane bp);
}
