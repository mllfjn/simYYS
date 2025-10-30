package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.AttributeCounter;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.customnode.CustomText;
import com.mllfjn.simyys.customnode.TextFlowLog;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.*;

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

        Info info = new Info();
        TraceableNumber traceableNumber = info.getTraceableNumber();

        // 基础伤害
        traceableNumber.add(owner.getAttack(), "攻击力");

        // 技能系数
        traceableNumber.mul(0.01 * multiplier, "技能系数");

        // 暴击
        if (baoJi) {
            traceableNumber.mul(owner.getCritPower() * 0.01, "爆伤");
            info.setBaoJi();
        }

        // 防御
        double realDefense = target.getDefense() - owner.getIgnoreDefense();
        traceableNumber.mul(300.0 / (300 + realDefense), "防御");

        // 一般增伤乘区
        traceableNumber.mul(AttributeCounter.getZengShang(owner), "增伤");

        // 减伤 实际 = 基础 / (1+减伤) = 基础 * (1.0 / (1+减伤))
        // https://bbs.nga.cn/read.php?tid=35530141 关于减伤的分类
        traceableNumber.mul( 1.0 / ( 1 + target.getJianShang() / 100 ), "减伤");

        // 盾
//        traceableNumber.sub();

        target.beHurt(bp, info);

        currentNumberLog.computeIfAbsent(target, k -> createNumberRecord(target.name))
                .add(new CustomText(traceableNumber.getNumberString() + " "
                        , traceableNumber.getTrace()
                        , type, baoJi ? TextFlowLog.TextColor.CRITICAL : TextFlowLog.TextColor.ATTACK, size));


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

        Info info = new Info();
        TraceableNumber traceableNumber = info.getTraceableNumber();

        // 基础数值
        traceableNumber.add(owner.getMaxHp(), "生命上限");

        // 技能系数
        traceableNumber.mul(0.01 * multiplier, "技能系数");

        // 暴击
        if (baoJi) {
            traceableNumber.mul(owner.getCritPower() * 0.01, "爆伤");
            info.setBaoJi();
        }

        target.beHeal(bp, info);

        currentNumberLog.computeIfAbsent(target, k -> createNumberRecord(target.name))
                .add(new CustomText(traceableNumber.getNumberString() + " "
                        , traceableNumber.getTrace()
                        , type, TextFlowLog.TextColor.HEAL, size));

        return info;
    }

    public void recovery(Character target, double num) {
        Info info = new Info();
        TraceableNumber traceableNumber = info.getTraceableNumber();

        traceableNumber.add(num, "恢复数值");

        target.recovery(num);

        currentNumberLog.computeIfAbsent(target, k -> createNumberRecord(target.name))
                .add(new CustomText(traceableNumber.getNumberString() + " "
                , traceableNumber.getTrace()
                , type, TextFlowLog.TextColor.HEAL, size));
    }

    public boolean[] effect(String stateName, List<Character> targets, int base, StateSupplier stateSupplier) {
        boolean[] mingZhong = RateController.mingZhong(stateName, owner, targets, base, bp.isControlRate, bp.calc);
        for (int i = 0; i < targets.size(); i++) {
            if (mingZhong[i]) {
                effect(targets.get(i), stateSupplier);
            }
        }
        return mingZhong;
    }

    public void effect(String stateName, Character target, int base, StateSupplier stateSupplier) {
        if (RateController.mingZhong(stateName, owner, List.of(target), base, bp.isControlRate, bp.calc)[0]) {
            effect(target, stateSupplier);
        }
    }

    private void effect(Character target, StateSupplier stateSupplier) {
        target.addState(stateSupplier.get(owner, target));
    }

}
