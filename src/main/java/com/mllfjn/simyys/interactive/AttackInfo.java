package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.function.BiFunction;

public class AttackInfo {
    // 伤害发起者
    private final Character attacker;
    // 伤害来源技能
    private final Skill skill;
    // 伤害目标
    private final Character target;
    // 基础数值
    private final BiFunction<Character, Character, Double> basicNumber;
    // 伤害溯源
    private final TraceableNumber traceableNumber;


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
    private boolean calYiShang = true;
    // 计算御魂
    private boolean calYuHun = true;

    // 是否被取消
    private boolean cancel = false;

    // 基本伤害类型
    public static AttackInfo createTypicalAttack(Character attacker, Skill skill, Character target, int multiplier) {
        AttackInfo attackInfo = new AttackInfo(attacker, skill, target,
                (from, to) -> from.getAttack());
        attackInfo.multiplier = multiplier;
        return attackInfo;
    }

    // 真实伤害:无视防御,不会暴击(没写,但是无视增伤,减伤和御魂)
    public static AttackInfo createRealAttack(Character attacker, Skill skill, Character target
            , BiFunction<Character, Character, Double> basicNumber) {
        AttackInfo attackInfo = new AttackInfo(attacker, skill, target, basicNumber);

        attackInfo.calDefense = false;
        attackInfo.canCrit = false;
        attackInfo.calZengShang = false;
        attackInfo.calYiShang = false;
        attackInfo.calYuHun = false;

        return attackInfo;
    }

    // 间接伤害:不会触发御魂效果,TODO 无法被分担
    // 对防御为0的敌人必定暴击
    public static AttackInfo createJianJieAttack(Character attacker, Skill skill, Character target
            , BiFunction<Character, Character, Double> basicNumber) {
        AttackInfo attackInfo = new AttackInfo(attacker, skill, target, basicNumber);

        attackInfo.calYuHun = false;
        if (target.getDefense() - target.getIgnoreDefense() == 0) {
            attackInfo.setCrit(true);
        }

        return attackInfo;
    }

    // 基本治疗
    public static AttackInfo createTypicalHeal(Character attacker, Skill skill, Character target, int multiplier) {
        AttackInfo attackInfo = new AttackInfo(attacker, skill, target, (from, to) -> from.getMaxHp());
        attackInfo.multiplier = multiplier;
        return attackInfo;
    }
    // 恢复,不会暴击
    public static AttackInfo createRecovery(Character attacker, Skill skill, Character target
            , BiFunction<Character, Character, Double> basicNumber) {
        AttackInfo attackInfo = new AttackInfo(attacker, skill, target, basicNumber);
        attackInfo.canCrit = false;
        return attackInfo;
    }


    private AttackInfo(Character attacker, Skill skill, Character target
            , BiFunction<Character, Character, Double> basicNumber) {
        this.attacker = attacker;
        this.skill = skill;
        this.target = target;
        this.basicNumber = basicNumber;

        traceableNumber = new TraceableNumber(attacker.name, skill.getName());
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
}
