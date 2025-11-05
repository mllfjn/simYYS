package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Info;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;
import com.mllfjn.simyys.state.determinant.InfluenceDamage;

public class StateShenSheJianShang extends State implements InfluenceDamage {
    private int count;

    public StateShenSheJianShang(Character character) {
        super(character, character, StateType.SPECIAL, StateForm.SPECIAL);
    }

    public static void add(Character character) {
        character.getState(StateShenSheJianShang.class)
                .or(() -> character.addState(new StateShenSheJianShang(character)))
                .ifPresent(StateShenSheJianShang::add);
    }

    public static void reduce(Character character) {
        character.getState(StateShenSheJianShang.class).ifPresent(StateShenSheJianShang::reduce);
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
    public boolean effective(AttackType attackType, Character character) {
        return true;
    }

    @Override
    public void doInfluence(AttackType attackType, Info info) {
        // 每存在1把(count),神堕八岐大蛇受到的伤害减少20%
        info.getTraceableNumber().mul(Math.max(0, 1 - count * 0.2), "神蛇堕落之剑减伤");
    }
}
