package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventAttack;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.yuhun.YuHunEffectInfo;
import com.mllfjn.simyys.customnode.CustomText;
import com.mllfjn.simyys.customnode.TextFlowLog;
import com.mllfjn.simyys.ratecontroller.RateController;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.determinant.IgnoreActionDecrease;
import com.mllfjn.simyys.character.status.determinant.IgnoreActionIncrease;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamage;

import java.util.*;
import java.util.function.Consumer;

public class Interactive {
    private final BattlePane bp;
    private Character owner;
    private Map<Character, List<CustomText>> currentNumberLog = new LinkedHashMap<>();
    private Set<String> increaseLog = new LinkedHashSet<>();
    private Map<Character, Set<String>> yuHunEffect = new LinkedHashMap<>();
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

        yuHunEffect.forEach(((character, strings) ->
                bp.log.addText("\t" + character.name + "触发御魂：" + String.join("、", strings) + "\n"
                        , TextFlowLog.TextType.YU_HUN, TextFlowLog.TextColor.NORMAL, TextFlowLog.FontSize.NORMAL)));

        increaseLog.forEach(bp.log::addLocationChange);

        /*currentNumberLog = new LinkedHashMap<>();
        increaseLog = new LinkedHashSet<>();*/
        currentNumberLog.clear();
        increaseLog.clear();
        yuHunEffect.clear();
    }

    private void addNumberRecord(Character character, CustomText text) {
        currentNumberLog.computeIfAbsent(character, k -> {
            List<CustomText> list = new ArrayList<>();
            list.add(new CustomText("\t" + character.name + "：", type, TextFlowLog.TextColor.NORMAL, size));
            return list;
        });

        currentNumberLog.get(character).add(text);
    }

    public AttackInfo[] attack(String skillName, List<Character> targets, int multiplier, AttackType attackType) {
        AttackInfo[] attackInfos = new AttackInfo[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            AttackInfo attackInfo = AttackInfo.createTypicalAttack(owner, targets.get(i), multiplier);
            attackInfos[i] = attackInfo;
        }

        RateController.baoJi(skillName, owner, bp.calc, targets, attackInfos);

        for (int i = 0; i < targets.size(); i++) {
            attack(targets.get(i), attackType, attackInfos[i]);
        }

        return attackInfos;
    }

    public AttackInfo attack(String skillName, Character target, int multiplier, AttackType attackType) {
        AttackInfo attackInfo = AttackInfo.createTypicalAttack(owner, target, multiplier);
        RateController.baoJi(skillName, owner, bp.calc, List.of(target), attackInfo);
        attack(target, attackType, attackInfo);
        return attackInfo;
    }

    public void attack(Character target, AttackType attackType, AttackInfo attackInfo) {
        if (!target.alive) {
            return;
        }

        TraceableNumber traceableNumber = attackInfo.getTraceableNumber();

        // 基础伤害
        traceableNumber.add(attackInfo.getBasicNumber().apply(owner, target), "基础伤害");

        // 技能系数
        traceableNumber.mul(0.01 * attackInfo.getMultiplier(), "技能系数");

        // 暴击
        if (attackInfo.isCrit()) {
            traceableNumber.mul(owner.getCritPower() * 0.01, "爆伤");
        }

        // 防御
        if (attackInfo.isCalDefense()) {
            double realDefense = target.getDefense() - owner.getIgnoreDefense();
            traceableNumber.mul(300.0 / (300 + realDefense), "防御");
        }

        // 一般增伤乘区
        if (attackInfo.isCalZengShang()) {
            traceableNumber.mul(1 + owner.getZengShang() / 100, "增伤");
        }

        // 减伤 实际 = 基础 / (1+减伤) = 基础 * (1.0 / (1+减伤)) https://bbs.nga.cn/read.php?tid=35530141 关于减伤的分类
        if (attackInfo.isCalJianShang()) {
            traceableNumber.mul(1.0 / (1 + target.getJianShang() / 100), "减伤");
        }

        // 护盾
        target.checkShield(attackInfo);

        // 状态类影响
        for (Status status : target.getStatuses()) {
            if (status instanceof InfluenceDamage sid && sid.effective(attackType, owner)) {
                sid.doInfluence(attackType, attackInfo);
            }
        }

        // 御魂 TODO 被攻击的人的御魂
        if (traceableNumber.getNumber() > 0 && attackInfo.isCalYuHun()) {
            owner.forEachYuHun(yuHun -> {
                if (yuHun instanceof YuHunEffectInfo yei) {
                    yei.effectInfo(attackInfo);
                }
            });
        }

        target.beHurt(attackInfo);

        addNumberRecord(target
                , new CustomText(traceableNumber.getNumberString() + " "
                        , traceableNumber.getTrace(), type
                        , attackInfo.isCrit() ? TextFlowLog.TextColor.CRITICAL : TextFlowLog.TextColor.ATTACK, size));

        // 广播攻击信息
        bp.onTrigger(new EventAttack(attackInfo));
    }

    public AttackInfo heal(String skillName, Character target, int multiplier) {
        AttackInfo attackInfo = AttackInfo.createTypicalHeal(owner, target, multiplier);
        RateController.baoJi(skillName, owner, bp.calc, List.of(target), attackInfo);

        return heal(target, attackInfo);
    }

    public void heal(String skillName, List<Character> targets, int multiplier) {
        AttackInfo[] attackInfos = new AttackInfo[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            attackInfos[i] = AttackInfo.createTypicalHeal(owner, targets.get(i), multiplier);
        }

        RateController.baoJi(skillName, owner, bp.calc, targets, attackInfos);

        for (int i = 0; i < targets.size(); i++) {
            heal(targets.get(i), attackInfos[i]);
        }

    }

    private AttackInfo heal(Character target, AttackInfo attackInfo) {
        if (!target.alive) {
            return null;
        }

        TraceableNumber traceableNumber = attackInfo.getTraceableNumber();

        // 基础数值
        traceableNumber.add(attackInfo.getBasicNumber().apply(owner, target), "生命上限");

        // 技能系数
        traceableNumber.mul(0.01 * attackInfo.getMultiplier(), "技能系数");

        // 暴击
        if (attackInfo.isCrit()) {
            traceableNumber.mul(owner.getCritPower() * 0.01, "爆伤");
        }

        target.beHeal(attackInfo);

        addNumberRecord(target, new CustomText(traceableNumber.getNumberString() + " "
                , traceableNumber.getTrace()
                , type, TextFlowLog.TextColor.HEAL, size));

        return attackInfo;
    }

    public void recovery(Character target, double num) {
        AttackInfo attackInfo = AttackInfo.createRecovery(owner, target, (c1, c2) -> num);
        TraceableNumber traceableNumber = attackInfo.getTraceableNumber();

        traceableNumber.add(num, "恢复数值");

        target.recovery(num);

        addNumberRecord(target, new CustomText(traceableNumber.getNumberString() + " "
                , traceableNumber.getTrace()
                , type, TextFlowLog.TextColor.HEAL, size));
    }

    public EffectInfo[] effect(String statusName, List<Character> targets, int baseRate, boolean calHit, StatusSupplier statusSupplier) {
        EffectInfo[] infos = RateController.mingZhong(statusName, owner, targets, baseRate, calHit, bp.calc);
        for (int i = 0; i < targets.size(); i++) {
            if (infos[i].isHit()) {
                effect(targets.get(i), statusSupplier);
            }
        }
        return infos;
    }

    public void effect(String statusName, Character target, int baseRate, boolean calHit, StatusSupplier statusSupplier) {
        if (RateController.mingZhong(statusName, owner, List.of(target), baseRate, calHit, bp.calc)[0].isHit()) {
            effect(target, statusSupplier);
        }
    }

    private void effect(Character target, StatusSupplier statusSupplier) {
        target.addStatus(statusSupplier.get(owner, target));
    }

    public void getNewRound(Character target) {
        bp.situation.getNewRound(target);
        increaseLog.add(target.name + "获得新回合");
    }

    public void increaseLocation(Character target, double increase) {
        // 免疫行动条提升效果
        for (Status status : target.getStatuses()) {
            if (status instanceof IgnoreActionIncrease fi && fi.effective(owner)) {
                increaseLog.add(target.name + "免疫行动条改变");
                return;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(target.name).append("行动提前").append((int) increase).append("%");
        double location = target.getLocation();
        if (location + increase > 100) {
            sb.append("(实际提前").append((int) (100 - location)).append("%)");
        }

        target.setLocation(Math.min(100, location + increase));
        increaseLog.add(sb.toString());
    }

    public void decreaseLocation(Character target, double decrease) {
        // 免疫行动条提升效果
        for (Status status : target.getStatuses()) {
            if (status instanceof IgnoreActionDecrease) {
                increaseLog.add(target.name + "免疫行动条改变");
                return;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(target.name).append("行动推后").append((int) decrease).append("%");
        double location = target.getLocation();
        if (location - decrease < 0) {
            sb.append("(实际推后").append((int) location).append("%)");
        }

        target.setLocation(Math.max(0, location - decrease));
        increaseLog.add(sb.toString());
    }

    public void addYuHunEffectLog(Character character, String yuHunName) {
        yuHunEffect.computeIfAbsent(character, k -> new LinkedHashSet<>()).add(yuHunName);
    }
}
