package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.function.BiFunction;

public class InteractiveInfo {
    // 伤害发起者
    private final Character attacker;
    // 伤害来源技能
    private final Skill skill;
    // 伤害目标
    private final Character target;
    // 伤害溯源
    private final TraceableNumber traceableNumber;


    // 技能系数
    private int multiplier = 100;
    // 可否暴击
    private boolean canCrit = true;
    // 是否暴击
    private Boolean crit;
    // 计算防御
    private boolean calDefence = true;
    // 计算增伤
    private boolean calZengShang = true;
    // 计算减伤
    private boolean calYiShang = true;
    // 计算御魂
    private boolean calYuHun = true;
    // 是否穿盾
    private boolean canThroughShield = false;

    // 伤害上限
    private double limit = 10000000;
    // 是否被取消
    private boolean cancel = false;

    // 基本伤害类型
    public static InteractiveInfo createTypicalAttack(Character attacker, Skill skill, Character target, int multiplier) {
        InteractiveInfo interactiveInfo = new InteractiveInfo(attacker, skill, target,
                (from, to) -> from.getAttack());
        interactiveInfo.multiplier = multiplier;
        return interactiveInfo;
    }

    // 真实伤害:无视防御,不会暴击(没写,但是无视增伤,减伤和御魂)
    public static InteractiveInfo createRealAttack(Character attacker, Skill skill, Character target
            , BiFunction<Character, Character, Double> basicNumber) {
        InteractiveInfo interactiveInfo = new InteractiveInfo(attacker, skill, target, basicNumber);

        interactiveInfo.calDefence = false;
        interactiveInfo.canCrit = false;
        interactiveInfo.calZengShang = false;
        interactiveInfo.calYiShang = false;
        interactiveInfo.calYuHun = false;

        return interactiveInfo;
    }

    // 间接伤害:不会触发御魂效果,TODO 无法被分担
    // 对防御为0的敌人必定暴击
    public static InteractiveInfo createJianJieAttack(Character attacker, Skill skill, Character target
            , BiFunction<Character, Character, Double> basicNumber) {
        InteractiveInfo interactiveInfo = new InteractiveInfo(attacker, skill, target, basicNumber);

        interactiveInfo.calYuHun = false;
        if (target.getDefence() - target.getIgnoreDefense() == 0) {
            interactiveInfo.setCrit(true);
        }

        return interactiveInfo;
    }

    // 传导伤害:不会暴击,不触发御魂TODO薙魂
    // 没写，但是不吃防御、增伤、易伤
    public static InteractiveInfo createChuanDaoAttack(Character attacker, Skill skill, Character target
            , BiFunction<Character, Character, Double> basicNumber) {
        InteractiveInfo interactiveInfo = new InteractiveInfo(attacker, skill, target, basicNumber);

        interactiveInfo.canCrit = false;
        interactiveInfo.calDefence = false;
        interactiveInfo.calZengShang = false;
        interactiveInfo.calYiShang = false;
        return interactiveInfo;
    }

    // 基本治疗
    public static InteractiveInfo createTypicalHeal(Character owner, Skill skill, Character target, int multiplier) {
        InteractiveInfo interactiveInfo = new InteractiveInfo(owner, skill, target, (from, to) -> from.getMaxHp());
        interactiveInfo.multiplier = multiplier;
        return interactiveInfo;
    }

    public static InteractiveInfo createHeal(Character owner, Skill skill, Character target
            , BiFunction<Character, Character, Double> basicNumber) {
        return new InteractiveInfo(owner, skill, target, basicNumber);
    }
    // 恢复,不会暴击
    public static InteractiveInfo createRecovery(Character attacker, Skill skill, Character target
            , BiFunction<Character, Character, Double> basicNumber) {
        InteractiveInfo interactiveInfo = new InteractiveInfo(attacker, skill, target, basicNumber);
        interactiveInfo.canCrit = false;
        return interactiveInfo;
    }


    public InteractiveInfo(Character attacker, Skill skill, Character target
            , BiFunction<Character, Character, Double> basicNumber) {
        this.attacker = attacker;
        this.skill = skill;
        this.target = target;

        traceableNumber = new TraceableNumber(attacker.name, skill.getName());
        traceableNumber.add(basicNumber.apply(attacker, target), "基础数值");
    }

    public TraceableNumber getTraceableNumber() {
        return traceableNumber;
    }

    public void setCrit(boolean crit) {
        if (canCrit) {
            this.crit = crit;
        }
    }

    public boolean isCrit() {
        return crit != null && crit;
    }

    public Boolean getCrit() {
        return crit;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public boolean isCalDefence() {
        return calDefence;
    }

    public boolean isCalZengShang() {
        return calZengShang;
    }

    public boolean isCalYiShang() {
        return calYiShang;
    }

    public boolean canCrit() {
        return canCrit;
    }

    public boolean isCalYuHun() {
        return calYuHun;
    }

    public Character getAttacker() {
        return attacker;
    }

    public Character getTarget() {
        return target;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setLimit(double limit) {
        this.limit = Math.min(limit, this.limit);
    }

    public double getLimit() {
        return limit;
    }

    public void setCanCrit(boolean canCrit) {
        this.canCrit = canCrit;
    }

    public void setCalDefence(boolean calDefence) {
        this.calDefence = calDefence;
    }

    public void setCalZengShang(boolean calZengShang) {
        this.calZengShang = calZengShang;
    }

    public void setCalYuHun(boolean calYuHun) {
        this.calYuHun = calYuHun;
    }

    public boolean isCanThroughShield() {
        return canThroughShield;
    }

    public void setCanThroughShield(boolean canThroughShield) {
        this.canThroughShield = canThroughShield;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}
