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

    public Info[] attack(String skillName, List<Character> targets, int multiplier, AttackType attackType) {
        boolean[] baoJi = RateController.baoJi(skillName, owner, targets, bp.isControlRate, bp.calc);
        Info[] infos = new Info[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            infos[i] = attack(targets.get(i), multiplier, attackType, baoJi[i]);
        }
        return infos;
    }

    public Info attack(String skillName, Character target, int multiplier, AttackType attackType) {
        return attack(target, multiplier, attackType, RateController.baoJi(skillName, owner, List.of(target), bp.isControlRate, bp.calc)[0]);
    }

    private Info attack(Character target, int multiplier, AttackType attackType, boolean baoJi) {
        if (!target.alive) {
            return null;
        }

        Info info = getDamage(target, multiplier, attackType, baoJi);
        currentNumberLog.computeIfAbsent(target, k -> createNumberRecord(target.name))
                .add(new CustomText(info.getTraceableNumber().getNumber() + " "
                        , info.getTraceableNumber().getTrace()
                        , type, baoJi ? TextFlowLog.TextColor.CRITICAL : TextFlowLog.TextColor.ATTACK, size));


        return info;
    }

    private Info getDamage(Character target, int multiplier, AttackType attackType, boolean baoJi) {
        TraceableNumber traceableNumber = new TraceableNumber();
        Info info = new Info(traceableNumber);

        // 基础伤害
        traceableNumber.add(owner.getAttack(), "攻击力");

        // 暴击
        if (baoJi) {
            traceableNumber.mul(owner.getCritPower() * 0.01, "爆伤");
            info.setBaoJi();
        }

        // 技能系数
        traceableNumber.mul(0.01 * multiplier, "技能系数");

        // 防御
        traceableNumber.mul(300.0 / (300 + target.getDefense()), "防御");

        // 一般增伤乘区
        traceableNumber.mul(AttributeCounter.getZengShang(owner), "增伤");

        // 盾
//        traceableNumber.sub();

        return info;
    }

    public Info heal(String skillName, Character target, int multiplier) {
        return heal(target, multiplier, RateController.baoJi(skillName, owner, List.of(target), bp.isControlRate, bp.calc)[0]);
    }

    public void heal(String skillName, List<Character> targets, int multiplier) {
        boolean[] baoJi = RateController.baoJi(skillName, owner, targets, bp.isControlRate, bp.calc);
        for (int i = 0; i < targets.size(); i++) {
            heal(targets.get(i), multiplier, baoJi[i]);
        }
    }

    private Info heal(Character target, int multiplier, boolean baoJi) {
        if (!target.alive) {
            return null;
        }

        TraceableNumber traceableNumber = new TraceableNumber();
        Info info = new Info(traceableNumber);

        // 基础数值
        traceableNumber.add(owner.getMaxHp(), "生命上限");

        // 暴击
        if (baoJi) {
            traceableNumber.mul(owner.getCritPower() * 0.01, "爆伤");
            info.setBaoJi();
        }

        // 技能系数
        traceableNumber.mul(0.01 * multiplier, "技能系数");

        currentNumberLog.computeIfAbsent(target, k -> createNumberRecord(target.name))
                .add(new CustomText(info.getTraceableNumber().getNumber() + " "
                        , info.getTraceableNumber().getTrace()
                        , type, TextFlowLog.TextColor.HEAL, size));

        return info;
    }

    public void effect(String stateName, List<Character> targets, int base, StateSupplier stateSupplier) {
        boolean[] mingZhong = RateController.mingZhong(stateName, owner, targets, base, bp.isControlRate, bp.calc);
        for (int i = 0; i < targets.size(); i++) {
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
