package com.mllfjn.simyys.character;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.starter.info.CharacterInfo;
import com.mllfjn.simyys.state.AttackRecorder;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.trigger.Trigger;
import com.mllfjn.simyys.trigger.TriggerSession;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Character implements Serializable{
    public String name;
    public int team;
    public int timesToAct;
    private double location;
    private int lockSkill;
    public boolean alive = true;
    private double maxHp;
    private double hp;
    private double baseAttack;
    private double yuHunAttack;
    private double defense;
    private double critRate;
    private double critPower;
    private double effectHitRate;
    private double effectResistRate;
    private double speed;
    private List<State> states = new ArrayList<>();


    public Character(){

    }

    public void init(CharacterInfo characterInfo) {
        this.name = characterInfo.name;
        this.speed = Double.parseDouble(characterInfo.speed);
        this.baseAttack = Double.parseDouble(characterInfo.baseAttack);
        this.yuHunAttack = Double.parseDouble(characterInfo.yuHunAttack);
        this.team = Integer.parseInt(characterInfo.team);
        this.hp = Double.parseDouble(characterInfo.hp);
        this.maxHp = Double.parseDouble(characterInfo.hp);
        this.defense = Double.parseDouble(characterInfo.defense);
        this.critRate = Double.parseDouble(characterInfo.critRate);
        this.critPower = Double.parseDouble(characterInfo.critPower);
        this.effectHitRate = Double.parseDouble(characterInfo.effectHitRate);
        this.effectResistRate = Double.parseDouble(characterInfo.effectResistRate);

        addState(new AttackRecorder(this));
    }

    public static double getTTA(double distance, double speed) {
        return distance / speed;
    }

    public double getTTA() {
        return getTTA(100.0 - this.getLocation(), this.getSpeed());
    }

    public static boolean before(double distance1, double v1, double distance2, double v2) {
        double tta1 = getTTA(distance1, v1);
        double tta2 = getTTA(distance2, v2);

        if (tta1 < tta2) {
            return true;
        }

        if (tta1 > tta2) {
            return false;
        }

        return v1 > v2;
    }

    public boolean before(Character character) {
        return before(100.0 - this.getLocation(), this.getSpeed(), 100 - character.getLocation(), character.getSpeed());
    }

    public double getSpeed() {
        return speed;
    }

    public double getLocation() {
        return location;
    }

    public void setLocation(double newLocation) {
        this.location = newLocation;
    }

    public void round(BattlePane battlePane) {
        TriggerSession.trigger(battlePane, Trigger.BEFOREROUND, this.getStates());

        act();

        TriggerSession.trigger(battlePane, Trigger.AFTERROUND, this.getStates());
    }

    private void act() {

    }

    public double getAttack() {
        return baseAttack + yuHunAttack;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getMaxHp() {
        return maxHp;
    }

    public double getDefense() {
        return defense;
    }

    public double getCritRate() {
        return critRate;
    }

    public double getCritPower() {
        return critPower;
    }

    public double getEffectHitRate() {
        return effectHitRate;
    }

    public double getEffectResistRate() {
        return effectResistRate;
    }

    public void setLockSkill(int i) {
        this.lockSkill = i;
    }
    public int getLockSkill() {
        return this.lockSkill;
    }


    public ObservableValue<? extends ObservableList<String>> getSkillListProperty() {
        return new SimpleListProperty<>(FXCollections.observableArrayList("妖术"));
    }

    public void useFrontSkill() {

    }

    public AttackRecorder getAttackRecorder() {
        return (AttackRecorder) getState(AttackRecorder.privateName);
    }

    public State getState(String name) {
        for (State state : states) {
            if (state.name.equals(name)) {
                return state;
            }
        }
        return null;
    }

    public void addState(State newState) {
        for (State state : states) {
            if (state.name.equals(newState.name)) {
                state.cover(newState);
                return;
            }
        }

        states.add(newState);
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }
}
