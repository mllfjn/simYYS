package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

public abstract class InteractiveInfo {
    // 发起者
    private final Character attacker;
    // 发起技能
    private final Skill skill;
    // 作用目标
    private final Character target;
    // 数值溯源
    private final TraceableNumber traceableNumber;

    // 技能系数
    protected double multiplier = 100;
    // 可否暴击
    protected boolean canCrit = true;
    // 是否暴击
    protected Boolean crit;
    // 是否被取消
    private boolean cancel = false;

    public InteractiveInfo(Character attacker, Skill skill, Character target, double basicNumber) {
        this.attacker = attacker;
        this.skill = skill;
        this.target = target;

        traceableNumber = new TraceableNumber(attacker.name, skill.getName());
        traceableNumber.add(basicNumber, "基础数值");
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

    public boolean canCrit() {
        return canCrit;
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

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}
