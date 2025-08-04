package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

import java.io.Serializable;

public abstract class Skill implements Serializable {
    private final String[] SKILL_LABEL = new String[]{"普攻", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾"};
    private final Character belongTo;
    private final int level;
    public Character lastUsedTarget;
    private final int cost;
    private final int coolDown;
    private int cooling;

    public Skill(Character belongTo, int level, int cost, int coolDown) {
        this.belongTo = belongTo;
        this.level = level;
        this.cost = cost;
        this.coolDown = coolDown;
    }
    public abstract int getSkillID();
    public abstract String getName();
    public void use(BattlePane bp) {
        useBase(bp, true);
    }

    public void useWithoutCost(BattlePane bp) {
        useBase(bp, false);
    }

    private void useBase(BattlePane bp, boolean isCost) {
        if (isCost) {
            bp.useGuiHuo(getBelongTo(), cost);
        }

        usePrivate(bp);

        cooling = coolDown + 1;
        StringBuilder sb = new StringBuilder(belongTo.name);
        if (lastUsedTarget != null) {
            sb.append("对").append(lastUsedTarget.name);
        }
        sb.append("使用了").append(getName());
        bp.log.addSkill(sb.toString());

        getBelongTo().getHit(bp).skillDone();
    }
    public abstract void usePrivate(BattlePane bp);
    public boolean canUse(BattlePane bp) {
        return cooling == 0 && bp.canUseGuiHuo(belongTo, cost);
    }

    public Character getBelongTo() {
        return this.belongTo;
    }

    public int getLevel() {
        return this.level;
    }
    public void pastRound() {
        if (cooling > 0) cooling--;
    }

    @Override
    public String toString() {
        int skillID = getSkillID();
        if (skillID == 0) return getName();
        if (skillID == 1) return SKILL_LABEL[0] + "·" + getName();
        return "妖术" + SKILL_LABEL[skillID - 1] + "·" + getName();
    }
}
