package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Info;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;
import com.mllfjn.simyys.state.determinant.InfluenceDamage;

public class StateShenSheJianShang extends State implements InfluenceDamage {
    public static final String privateName = "神蛇堕落之剑减伤";
    private int count;

    public StateShenSheJianShang(Character character) {
        super(character, character, StateType.SPECIAL, StateForm.SPECIAL);
    }

    public static void add(Character character) {
        StateShenSheJianShang state = (StateShenSheJianShang) character.getState(privateName);
        if (state == null) {
            state = new StateShenSheJianShang(character);
            character.addState(state);
        }
        state.add();
    }

    public static void reduce(Character character) {
        StateShenSheJianShang state = (StateShenSheJianShang) character.getState(privateName);
        state.reduce();
    }

    private void add() {
        count++;
    }
    private void reduce() {
        count--;
        if (count == 0) {
            delete();
        }
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public boolean effective(AttackType attackType) {
        return true;
    }

    @Override
    public void doInfluence(AttackType attackType, Info info) {
        // 每存在1把(count),神堕八岐大蛇受到的伤害减少20%
        info.getTraceableNumber().mul(Math.max(0, 1 - count * 0.2), privateName);
    }
}
