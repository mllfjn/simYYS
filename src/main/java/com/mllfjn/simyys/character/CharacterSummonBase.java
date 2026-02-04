package com.mllfjn.simyys.character;

import com.mllfjn.simyys.BattlePane;

/**
 * 召唤单位基类,不一定要求是召唤物,只要是战斗中新增单位就可以用
 */
public class CharacterSummonBase extends Character {
    public CharacterSummonBase(BattlePane bp, String name, int team) {
        this.bp = bp;
        this.name = name;
        this.team = team;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return null;
    }

    @Override
    protected void addOwnSkills() {
    }
}
