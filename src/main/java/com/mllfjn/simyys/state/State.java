package com.mllfjn.simyys.state;

import com.mllfjn.simyys.character.Character;

import java.io.Serializable;

public abstract class State implements Serializable {
    public String name;
    public final Character from;
    public final Character belongTo;
    public final StateType stateType;
    public final StateForm stateForm;
    private StateSettleType settleType = StateSettleType.NONE;
    private int duration = 1;

    public State(Character from, Character belongTo, StateType stateType, StateForm stateForm) {
        this.from = from;
        this.belongTo = belongTo;
        this.stateType = stateType;
        this.stateForm = stateForm;


        setName();
    }

    public void setSettleType(StateSettleType settleType, int duration) {
        this.settleType = settleType;
        this.duration = duration;

        if (settleType == StateSettleType.WEI_CHI) {
            from.addMaintainedState(this);
        }
    }

    public StateSettleType getSettleType() {
        return settleType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int num) {
        duration = num;
    }

    public abstract void setName();

    /**
     * 用于覆盖状态
     *
     * @param state 新的状态
     */
    public void cover(State state) {
        duration = Math.max(duration, state.duration);
    }

    public void delete() {
        belongTo.getStates().remove(this);
        if (settleType == StateSettleType.WEI_CHI) {
            from.removeMaintainedState(this);
        }
    }
}
