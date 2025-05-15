package com.mllfjn.simyys.state;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.trigger.Trigger;

import java.io.Serializable;

public abstract class State implements Serializable {
    public String name;
    public Character comeFrom;
    public Character belongTo;
    public StateType stateType;
    public StateForm stateForm;

    public State(Character belongTo, Character comeFrom, StateType stateType, StateForm stateForm) {
        this.comeFrom = comeFrom;
        this.belongTo = belongTo;
        this.stateType = stateType;
        this.stateForm = stateForm;

        setName();
    }

    public abstract void setName();

    public boolean isAffectAttribute(Attribute attribute) {
        return false;
    }

    public double getInfluence(Attribute attribute) {
        return 0;
    }

    public boolean runnable(Trigger trigger) {
        return false;
    }

    public void run(Trigger trigger, BattlePane bp) {

    }

    /**
     * 用于覆盖状态
     *
     * @param state 新的状态
     */
    public void cover(State state) {

    }

    public enum StateType{
        BUFF, // 增益
        DEBUFF, // 减益
        GENERAL, // 通用
        SPECIAL // 特殊,指在游戏里没有显式定义
    }

    public enum StateForm{
        ZHUANG_TAI, // 状态,可以驱散
        YIN_JI, // 印记,不可驱散
        SPECIAL // 特殊,指在游戏里没有显式定义
    }
}
