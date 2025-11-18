package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.character.Character;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Info {
    private final TraceableNumber traceableNumber = new TraceableNumber();

    // 基础数值
    private final BiFunction<Character, Character, Double> basicNumber;

    // 技能系数
    private int multiplier = 100;
    // 可否暴击
    private boolean canCrit = true;
    // 是否暴击
    private boolean crit;
    // 计算防御
    private boolean calDefense = true;
    // 计算增伤
    private boolean calZengShang = true;
    // 计算减伤
    private boolean calJianShang = true;
    /*// 无视护盾 给鬼切铃鹿准备的
    private boolean ignoreShield = false;*/

    // 基本伤害类型
    public static Info createTypicalAttack(int multiplier) {
        Info info = new Info((from, to) -> from.getAttack());
        info.multiplier = multiplier;
        return info;
    }
    // 真实伤害:无视防御,不会暴击(没写,但是无视增伤和减伤)
    public static Info createRealAttack(BiFunction<Character, Character, Double> basicNumber) {
        Info info = new Info(basicNumber);

        info.calDefense = false;
        info.canCrit = false;
        info.calZengShang = false;
        info.calJianShang = false;

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
}
