package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.AttributeCounter;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.customnode.CustomText;
import com.mllfjn.simyys.customnode.TextFlowLog;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.*;
import java.util.function.Function;

public class Interactive {
    private final BattlePane bp;
    private final Character owner;
    private Map<Character, List<CustomText>> currentNumberLog = new LinkedHashMap<>();
    private Map<Character, List<CustomText>> increaseLog = new LinkedHashMap<>();
    private static final TextFlowLog.TextType type = TextFlowLog.TextType.NUMBER;
    private static final TextFlowLog.FontSize size = TextFlowLog.FontSize.NORMAL;
    public Interactive(BattlePane bp, Character owner) {
        this.bp = bp;
        this.owner = owner;
    }

    public void skillDone() {
        currentNumberLog.values().forEach(list -> {
            list.forEach(bp.log::addText);
            bp.log.addText("\n", type, TextFlowLog.TextColor.NORMAL, size);
        });

        currentNumberLog = new HashMap<>();
    }

    public void increase() {

    }
    private List<CustomText> createNumberRecord(String s) {
        List<CustomText> list = new ArrayList<>();
        list.add(new CustomText("\t" + s + "：", type, TextFlowLog.TextColor.NORMAL, size));
        return list;
    }

    public void attack(String skillName, List<Character> targets, int multiplier, AttackType attackType) {
        boolean[] baoJi = RateController.baoJi(skillName, owner, targets, bp.isControlRate, bp.calc);
        for (int i = 0; i < targets.size(); i++) {
            attack(targets.get(i), multiplier, attackType, baoJi[i]);
        }
    }

    public void attack(String skillName, Character target, int multiplier, AttackType attackType) {
        attack(target, multiplier, attackType, RateController.baoJi(skillName, owner, List.of(target), bp.isControlRate, bp.calc)[0]);
    }

    private void attack(Character target, int multiplier, AttackType attackType, boolean baoJi) {
        if (!target.alive) {
            return;
        }

        double damage = getDamage(target, multiplier, attackType, baoJi);

        Double realHurt = target.beHurt(bp, damage);
        if (realHurt != null) {
            currentNumberLog.computeIfAbsent(target, k -> createNumberRecord(target.name))
                    .add(new CustomText(realHurt.intValue() + " ", type, baoJi ? TextFlowLog.TextColor.CRITICAL : TextFlowLog.TextColor.ATTACK, size));
        }

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
    private void heal(Character target, int multiplier, boolean baoJi, Function<Character, Double> AttributeGetter) {
        if (!target.alive) {
            return;
        }

        double heal = AttributeGetter.apply(owner) * multiplier;
        if (baoJi) {
            heal *= owner.getCritPower() / 100;
        }


    }
    public void effect(String stateName, List<Character> targets, int base, StateSupplier stateSupplier) {
        boolean[] mingZhong = RateController.mingZhong(stateName, owner, targets, base, bp.isControlRate, bp.calc);
        for (int i = 0 ; i < targets.size(); i++) {
            if (mingZhong[i]) {
                effect(targets.get(i), stateSupplier);
            }
        }
    }

    public void effect(String stateName, Character target, int base, StateSupplier stateSupplier) {
        if (RateController.mingZhong(stateName, owner, List.of(target), base, bp.isControlRate, bp.calc)[0]) {
            effect(target, stateSupplier);
        }
    }
    private void effect(Character target, StateSupplier stateSupplier) {
        target.addState(stateSupplier.get(target, owner));
    }

}
