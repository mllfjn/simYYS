package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.TraversalOrderManager;
import com.mllfjn.simyys.character.skill.Skill;

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
    // 是否波动
    private double fluctuationLimit = 0.01;
    // 计算伤害御魂
    private boolean calAttackYuHun = true;
    // 计算效果类御魂
    private boolean calEffectYuHun = true;

    // 伤害上限
    private double limit = 10000000;

    public AttackInfo(Character attacker, Skill skill, Character target, AttackType attackType, double basicNumber) {
        super(attacker, skill, target, basicNumber);
        this.attackType = attackType;
    }

    // 基本伤害类型
    public static AttackInfo createTypicalAttack(Character attacker, Skill skill, Character target,
                                                 double multiplier, AttackType attackType) {
        AttackInfo attackInfo = new AttackInfo(attacker, skill, target, attackType, attacker.getAttack());
        attackInfo.multiplier = multiplier;
        return attackInfo;
    }

    // 真实伤害:无视防御,不会暴击(没写,但是无视增伤,减伤)
    public static AttackInfo createRealAttack(Character attacker, Skill skill, Character target, double basicNumber) {
        AttackInfo attackInfo = new AttackInfo(attacker, skill, target, AttackType.ZHEN_SHI, basicNumber);

        attackInfo.calDefence = false;
        attackInfo.canCrit = false;
        attackInfo.calZengShang = false;
        attackInfo.calYiShang = false;
        attackInfo.fluctuationLimit = 0;

        return attackInfo;
    }

    // 间接伤害:不会触发御魂效果,对防御为0的敌人必定暴击 TODO 无法被分担
    public static AttackInfo createJianJieAttack(Character attacker, Skill skill, Character target,
                                                 double basicNumber
    ) {
        AttackInfo attackInfo = new AttackInfo(attacker, skill, target, AttackType.JIAN_JIE, basicNumber);

        attackInfo.calAttackYuHun = false;
        attackInfo.calEffectYuHun = false;

        // 防御为0必定暴击
        if (TraversalOrderManager.getActualDefense(attacker, target, AttackType.JIAN_JIE) == 0) {
            attackInfo.setCrit(true);
            // 防御为0就可以跳过防御了
            attackInfo.setCalDefence(false);
        }

        return attackInfo;
    }

    // 传导伤害:不会暴击,不触发御魂,不吃防御、增伤、易伤TODO薙魂
    public static AttackInfo createChuanDaoAttack(Character attacker, Skill skill, Character target, double basicNumber) {
        AttackInfo attackInfo = new AttackInfo(attacker, skill, target, AttackType.CHUAN_DAO, basicNumber);

        attackInfo.canCrit = false;
        attackInfo.calDefence = false;
        attackInfo.calZengShang = false;
        attackInfo.calYiShang = false;
        return attackInfo;
    }

    // 固定伤害:无视防御,不会暴击,不受增减伤效果影响
    public static AttackInfo createGuDingAttack(Character attacker, Skill skill, Character target, double basicNumber) {
        AttackInfo attackInfo = new AttackInfo(attacker, skill, target, AttackType.GU_DING, basicNumber);
        attackInfo.calDefence = false;
        attackInfo.canCrit = false;
        attackInfo.calZengShang = false;
        attackInfo.calYiShang = false;
        attackInfo.calAttackYuHun = false;

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

    public boolean isCanThroughShield() {
        return canThroughShield;
    }

    public void setCanThroughShield(boolean canThroughShield) {
        this.canThroughShield = canThroughShield;
    }

    public void setFluctuationLimit(double fluctuationLimit) {
        this.fluctuationLimit = fluctuationLimit;
    }

    public double getFluctuationLimit() {
        return fluctuationLimit;
    }

    public void setNotCalYuHun() {
        this.calAttackYuHun = false;
        this.calEffectYuHun = false;
    }

    public boolean isCalAttackYuHun() {
        return calAttackYuHun;
    }

    public boolean isCalEffectYuHun() {
        return calEffectYuHun;
    }
}
