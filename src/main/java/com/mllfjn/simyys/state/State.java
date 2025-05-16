package com.mllfjn.simyys.state;

import com.mllfjn.simyys.character.Character;

import java.io.Serializable;

public abstract class State implements Serializable {
    public String name;
    public final Character comeFrom;
    public final Character belongTo;
    public final StateType stateType;
    public final StateForm stateForm;
    private StateSettleType settleType = StateSettleType.NONE;
    private int restRound = 1;
    public boolean tobeDelete = false;


    public State(Character belongTo, Character comeFrom, StateType stateType, StateForm stateForm) {
        this.comeFrom = comeFrom;
        this.belongTo = belongTo;
        this.stateType = stateType;
        this.stateForm = stateForm;

        setName();
    }

    public void setSettleType(StateSettleType settleType, int restRound) {
        this.settleType = settleType;
        this.restRound = restRound;
    }

    public int getRestRound() {
        return restRound;
    }

    public abstract void setName();

    /**
     * 用于覆盖状态
     *
     * @param state 新的状态
     */
    public void cover(State state) {
        restRound = Math.max(restRound, state.restRound);
    }
    public void pastRound() {
        if (settleType != StateSettleType.CHI_XU) {
            return;
        }

        restRound--;
        if (restRound == 0) {
            delete();
        }
    }

    public void delete() {
        tobeDelete = true;
    }

}
