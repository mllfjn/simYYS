package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.TraversalOrderManager;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.function.BiFunction;

public class AttackInfo extends InteractiveInfo {
    private final AttackType attackType;
    // 计算防御
    private boolean calDefence = true;
    // 计算增伤
    private boolean calZengShang = true;
    // 计算易伤
    private boolean calYiShang = true;
    // 是否穿盾
    private boolean canThroughShield = false;

    // 伤害上限
    private double limit = 10000000;

    private AttackInfo(Character attacker, Skill skill, Character target, AttackType attackType,
                       BiFunction<Character, Character, Double> basicNumber) {
        super(attacker, skill, target, basicNumber);
        this.attackType = attackType;
    }

    // 基本伤害类型
    public static AttackInfo createTypicalAttack(Character attacker, Skill skill, Character target,
                                                 int multiplier, AttackType attackType) {
        AttackInfo attackInfo = new AttackInfo(attacker, skill, target, attackType,
                (from, to) -> from.getAttack());
        attackInfo.multiplier = multiplier;
        return attackInfo;
    }

    // 真实伤害:无视防御,不会暴击(没写,但是无视增伤,减伤和御魂)
    public static AttackInfo createRealAttack(Character attacker, Skill skill, Character target
            , BiFunction<Character, Character, Double> basicNumber) {
        AttackInfo attackInfo = new AttackInfo(attacker, skill, target, AttackType.ZHEN_SHI, basicNumber);

        attackInfo.calDefence = false;
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
        AttackInfo attackInfo = new AttackInfo(attacker, skill, target, AttackType.JIAN_JIE, basicNumber);

        attackInfo.calYuHun = false;

        // 防御为0必定暴击
        if (TraversalOrderManager.getActualDefense(attacker, target, AttackType.JIAN_JIE) == 0) {
            attackInfo.setCrit(true);
            // 防御为0就可以跳过防御了
            attackInfo.setCalDefence(false);
        }

        return attackInfo;
    }

    // 传导伤害:不会暴击,不触发御魂TODO薙魂
    // 没写，但是不吃防御、增伤、易伤
    public static AttackInfo createChuanDaoAttack(Character attacker, Skill skill, Character target
            , BiFunction<Character, Character, Double> basicNumber) {
        AttackInfo attackInfo = new AttackInfo(attacker, skill, target, AttackType.CHUAN_DAO, basicNumber);

        attackInfo.canCrit = false;
        attackInfo.calDefence = false;
        attackInfo.calZengShang = false;
        attackInfo.calYiShang = false;
        return attackInfo;
    }

    public AttackType getAttackType() {
        return attackType;
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

    public void setLimit(double limit) {
        this.limit = Math.min(limit, this.limit);
    }

    public double getLimit() {
        return limit;
    }

    public void setCalDefence(boolean calDefence) {
        this.calDefence = calDefence;
    }

    public void setCalZengShang(boolean calZengShang) {
        this.calZengShang = calZengShang;
    }

    public boolean isCanThroughShield() {
        return canThroughShield;
    }

    public void setCanThroughShield(boolean canThroughShield) {
        this.canThroughShield = canThroughShield;
    }
}
