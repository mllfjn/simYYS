package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.character.Character;

import java.util.function.BiFunction;

public class Info {
    private final TraceableNumber traceableNumber = new TraceableNumber();

    // 基础数值
    private final BiFunction<Character, Character, Double> basicNumber;

    // 技能系数
    private int multiplier = 100;
    // 可否暴击
    private boolean canCrit = true;
    // 是否暴击
    private Boolean crit;
    // 计算防御
    private boolean calDefense = true;
    // 计算增伤
    private boolean calZengShang = true;
    // 计算减伤
    private boolean calJianShang = true;
    // 计算御魂
    private boolean calYuHun = true;

    // 基本伤害类型
    public static Info createTypicalAttack(int multiplier) {
        Info info = new Info((from, to) -> from.getAttack());
        info.multiplier = multiplier;
        return info;
    }

    // 真实伤害:无视防御,不会暴击(没写,但是无视增伤,减伤和御魂)
    public static Info createRealAttack(BiFunction<Character, Character, Double> basicNumber) {
        Info info = new Info(basicNumber);

        info.calDefense = false;
        info.canCrit = false;
        info.calZengShang = false;
        info.calJianShang = false;
        info.calYuHun = false;

        return info;
    }

    // 间接伤害:不会触发御魂效果,TODO 无法被分担
    // 对防御为0的敌人必定暴击
    public static Info createJianJieAttack(BiFunction<Character, Character, Double> basicNumber, Character owner, Character target) {
        Info info = new Info(basicNumber);

        info.calYuHun = false;
        if (target.getDefense() - owner.getIgnoreDefense() == 0) {
            info.setCrit(true);
        }

        return info;
    }

    // 基本治疗
    public static Info createTypicalHeal(int multiplier) {
        Info info = new Info((from, to) -> from.getMaxHp());
        info.multiplier = multiplier;
        return info;
    }
    // 恢复,不会暴击
    public static Info createRecovery(BiFunction<Character, Character, Double> basicNumber) {
        Info info = new Info(basicNumber);
        info.canCrit = false;
        return info;
    }


    private Info(BiFunction<Character, Character, Double> basicNumber) {
        this.basicNumber = basicNumber;
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

    public BiFunction<Character, Character, Double> getBasicNumber() {
        return basicNumber;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public boolean isCalDefense() {
        return calDefense;
    }

    public boolean isCalZengShang() {
        return calZengShang;
    }

    public boolean isCalJianShang() {
        return calJianShang;
    }

    public boolean canCrit() {
        return canCrit;
    }

    public boolean isCalYuHun() {
        return calYuHun;
    }
}
