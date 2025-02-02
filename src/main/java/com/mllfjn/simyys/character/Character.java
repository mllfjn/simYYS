package com.mllfjn.simyys.character;

import com.mllfjn.simyys.starter.info.CharacterInfo;

public class Character {
    private String name;
    private int team;
    private int timesToAct;
    private double location;
    private int lockSkill;
    public boolean alive = true;
    private double baseHp;
    private double hp;
    private double baseAttack;
    private double yuHunAttack;
    private double defense;
    private double criticalRate;
    private double criticalMultiplier;
    private double mingZhong;
    private double diKang;
    public double speed;




    public Character(){

    }

    public void init(CharacterInfo characterInfo) {

    }

    public double getTTA() {
        return (100 - location) / getSpeed();
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
}
