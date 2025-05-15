package com.mllfjn.simyys.hit;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.AttributeCounter;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.ratecontroller.RateController;
import com.mllfjn.simyys.state.State;

import java.util.List;
import java.util.function.Supplier;

public class Hit {
    BattlePane bp;
    Character owner;
    public Hit(BattlePane bp, Character owner) {
        this.bp = bp;
        this.owner = owner;
    }
    public void attack(List<Character> targets, int multiplier, AttackType attackType) {
        boolean[] baoJi = RateController.baoJi(owner, targets, bp.isControlRate);
        for (int i = 0; i < targets.size(); i++) {
            attack(targets.get(i), multiplier, attackType, baoJi[i]);
        }
    }

    public void attack(Character target, int multiplier, AttackType attackType) {
        attack(target, multiplier, attackType, RateController.baoJi(owner, List.of(target), bp.isControlRate)[0]);
    }

    private void attack(Character target, int multiplier, AttackType attackType, boolean baoJi) {
        if (!target.alive) {
            return;
        }

        double damage = getDamage(target, multiplier, attackType, baoJi);

        target.beHurt(bp, damage);

    }

    private double getDamage(Character target, int multiplier, AttackType attackType, boolean baoJi) {
        // 基础伤害
        double damage = owner.getAttack() * multiplier / 100;

        // 计算防御
        damage *= 300 / (300 + target.getDefense());

        // 暴击
        if (baoJi) {
            damage *= owner.getCritPower() / 100;
        }

        // 一般增伤乘区
        damage *= AttributeCounter.getZengShang(owner);


        return damage;
    }

    public void heal() {

    }
    public void effect(List<Character> targets, StateSupplier stateSupplier, int base, boolean controlRate) {
        boolean[] mingZhong = RateController.mingZhong(owner, targets, base, controlRate);
        for (int i = 0 ; i < targets.size(); i++) {
            if (mingZhong[i]) {
                effect(targets.get(i), stateSupplier);
            }
        }
    }

    public void effect(Character target, StateSupplier stateSupplier, int base, boolean controlRate) {
        if (RateController.mingZhong(owner, List.of(target), base, controlRate)[0]) {
            effect(target, stateSupplier);
        }
    }
    private void effect(Character target, StateSupplier stateSupplier) {
        target.addState(stateSupplier.get(owner, target));
    }

    private class AttackRecorder {

    }

}
