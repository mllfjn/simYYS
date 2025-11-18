package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.customnode.CustomText;
import com.mllfjn.simyys.customnode.TextFlowLog;
import com.mllfjn.simyys.ratecontroller.RateController;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.determinant.IgnoreActionIncrease;
import com.mllfjn.simyys.state.determinant.InfluenceDamage;

import java.util.*;
import java.util.function.Consumer;

public class Interactive {
    private final BattlePane bp;
    private Character owner;
    private Map<Character, List<CustomText>> currentNumberLog = new LinkedHashMap<>();
    private Set<String> increaseLog = new LinkedHashSet<>();
    private static final TextFlowLog.TextType type = TextFlowLog.TextType.NUMBER;
    private static final TextFlowLog.FontSize size = TextFlowLog.FontSize.SMALL;

    public Interactive(BattlePane bp) {
        this.bp = bp;
    }

    public void setOwner(Character owner) {
        this.owner = owner;
    }

    public void doInterActive(Character character, Consumer<Interactive> action) {
        Character temp = owner;
        owner = character;

        action.accept(this);

        owner = temp;
    }

    public void display() {
        currentNumberLog.values().forEach(list -> {
            list.forEach(bp.log::addText);
            bp.log.addText("\n", type, TextFlowLog.TextColor.NORMAL, size);
        });

        increaseLog.forEach(bp.log::addLocationChange);

        currentNumberLog = new LinkedHashMap<>();
        increaseLog = new LinkedHashSet<>();
    }

    private void addNumberRecord(Character character, CustomText text) {
        currentNumberLog.computeIfAbsent(character, k -> {
            List<CustomText> list = new ArrayList<>();
            list.add(new CustomText("\t" + character.name + "：", type, TextFlowLog.TextColor.NORMAL, size));
            return list;
        });

        currentNumberLog.get(character).add(text);
    }

    public Info[] attack(String skillName, List<Character> targets, int multiplier, AttackType attackType) {
        boolean[] crit = RateController.baoJi(skillName, owner, targets, bp.isControlRate, bp.calc);
        Info[] infos = new Info[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            Info info = Info.createTypicalAttack(multiplier);
            infos[i] = info;
            info.setCrit(crit[i]);
            attack(targets.get(i), attackType, info);
        }
        return infos;
    }

    public Info attack(String skillName, Character target, int multiplier, AttackType attackType) {
        Info info = Info.createTypicalAttack(multiplier);
        info.setCrit(RateController.baoJi(skillName, owner, List.of(target), bp.isControlRate, bp.calc)[0]);
        attack(target, attackType, info);
        return info;
    }

    public void attack(Character target, AttackType attackType, Info info) {
        if (!target.alive) {
            return;
        }

        TraceableNumber traceableNumber = info.getTraceableNumber();

        // 基础伤害
        traceableNumber.add(info.getBasicNumber().apply(owner, target), "基础伤害");

        // 技能系数
        traceableNumber.mul(0.01 * info.getMultiplier(), "技能系数");

        // 暴击
        if (info.isCrit()) {
            traceableNumber.mul(owner.getCritPower() * 0.01, "爆伤");
        }

        // 防御
        if (info.isCalDefense()) {
            double realDefense = target.getDefense() - owner.getIgnoreDefense();
            traceableNumber.mul(300.0 / (300 + realDefense), "防御");
        }

        // 一般增伤乘区
        if (info.isCalZengShang()) {
            traceableNumber.mul(1 + owner.getZengShang() / 100, "增伤");
        }

        // 减伤 实际 = 基础 / (1+减伤) = 基础 * (1.0 / (1+减伤)) https://bbs.nga.cn/read.php?tid=35530141 关于减伤的分类
        if (info.isCalJianShang()) {
            traceableNumber.mul(1.0 / (1 + target.getJianShang() / 100), "减伤");
        }

        for (State state : target.getStates()) {
            if (state instanceof InfluenceDamage sid && sid.effective(attackType, owner)) {
                sid.doInfluence(attackType, info);
            }
        }

        target.beHurt(info);

        addNumberRecord(target, new CustomText(traceableNumber.getNumberString() + " "
                , traceableNumber.getTrace()
                , type, info.isCrit() ? TextFlowLog.TextColor.CRITICAL : TextFlowLog.TextColor.ATTACK, size));
    }

    public Info heal(String skillName, Character target, int multiplier) {
        Info info = Info.createTypicalHeal(multiplier);
        info.setCrit(RateController.baoJi(skillName, owner, List.of(target), bp.isControlRate, bp.calc)[0]);
        return heal(target, info);
    }

    public void heal(String skillName, List<Character> targets, int multiplier) {
        boolean[] crit = RateController.baoJi(skillName, owner, targets, bp.isControlRate, bp.calc);
        for (int i = 0; i < targets.size(); i++) {
            Info info = Info.createTypicalHeal(multiplier);
            info.setCrit(crit[i]);
            heal(targets.get(i), info);
        }
    }

    private Info heal(Character target, Info info) {
        if (!target.alive) {
            return null;
        }

        TraceableNumber traceableNumber = info.getTraceableNumber();

        // 基础数值
        traceableNumber.add(info.getBasicNumber().apply(owner, target), "生命上限");

        // 技能系数
        traceableNumber.mul(0.01 * info.getMultiplier(), "技能系数");

        // 暴击
        if (info.isCrit()) {
            traceableNumber.mul(owner.getCritPower() * 0.01, "爆伤");
        }

        target.beHeal(info);

        addNumberRecord(target, new CustomText(traceableNumber.getNumberString() + " "
                , traceableNumber.getTrace()
                , type, TextFlowLog.TextColor.HEAL, size));

        return info;
    }

    public void recovery(Character target, double num) {
        Info info = Info.createRecovery((c1, c2) -> num);
        TraceableNumber traceableNumber = info.getTraceableNumber();

        traceableNumber.add(num, "恢复数值");

        target.recovery(num);

        addNumberRecord(target, new CustomText(traceableNumber.getNumberString() + " "
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

    public void getNewRound(Character target) {
        bp.situation.getNewRound(target);
        increaseLog.add(target.name + "获得新回合");
    }

    public void increaseLocation(Character target, double increase) {
        // 免疫行动条提升效果
        for (State state : target.getStates()) {
            if (state instanceof IgnoreActionIncrease fi && fi.effective(owner)) {
                return;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(target.name).append("行动提前").append((int) increase).append("%");
        double location = target.getLocation();
        if (location + increase > 100) {
            sb.append("(实际提前").append(100 - location).append("%)");
        }

        target.setLocation(location + increase);
        increaseLog.add(sb.toString());
    }

    /*public void decreaseLocation(Character from, double decrease) {
        for (State state : states) {
            if (state instanceof IgnoreActionDecrease) {
                return;
            }
        }
        this.location = Math.max(0, location - decrease);
        bp.log.addLocationChange(this.name + "行动推后" + (int)decrease);
    }*/
}
