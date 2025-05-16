package com.mllfjn.simyys.hit;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.AttributeCounter;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;

public class Hit {
    final BattlePane bp;
    final Character owner;
    public Hit(BattlePane bp, Character owner) {
        this.bp = bp;
        this.owner = owner;
    }
    public void attack(String skillName, List<Character> targets, int multiplier, AttackType attackType) {
        boolean[] baoJi = RateController.baoJi(skillName, owner, targets, bp.isControlRate);
        for (int i = 0; i < targets.size(); i++) {
            attack(targets.get(i), multiplier, attackType, baoJi[i]);
        }
    }

    public void attack(String skillName, Character target, int multiplier, AttackType attackType) {
        attack(target, multiplier, attackType, RateController.baoJi(skillName, owner, List.of(target), bp.isControlRate)[0]);
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
    public void effect(String stateName, List<Character> targets, int base, StateSupplier stateSupplier) {
        boolean[] mingZhong = RateController.mingZhong(stateName, owner, targets, base, bp.isControlRate);
        for (int i = 0 ; i < targets.size(); i++) {
            if (mingZhong[i]) {
                effect(targets.get(i), stateSupplier);
            }
        }
    }

    public void effect(String stateName, Character target, int base, StateSupplier stateSupplier) {
        if (RateController.mingZhong(stateName, owner, List.of(target), base, bp.isControlRate)[0]) {
            effect(target, stateSupplier);
        }
    }
    private void effect(Character target, StateSupplier stateSupplier) {
        target.addState(stateSupplier.get(target, owner));
    }

}
