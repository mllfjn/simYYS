package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

import java.io.Serializable;

public abstract class Skill implements Serializable {
    public static final String[] SKILL_LABEL = new String[]{"普攻", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾"};

    private final Character belongTo;
    private final int level;
    // 技能冷却时间
    private int coolDown;
    private final int skillID;
    // 技能本身的消耗
    private int cost;
    // 技能当前的冷却回合数，因为回合结束后判定一次技能冷却，所以设定时要+1
    private int cooling;
    public Character lastUsedTarget;

    public Skill(Character belongTo, int level, int cost, int coolDown, int skillID) {
        this.belongTo = belongTo;
        this.level = level;
        this.cost = cost;
        this.coolDown = coolDown;
        this.skillID = skillID;
    }

    public abstract String getName();

    public boolean tryUse(BattlePane bp) {
        if (canUse(bp)) {
            use(bp);
            return true;
        }
        return false;
    }

    public void use(BattlePane bp) {
        useBase(bp, true);
    }

    // 技能本身的消耗，妒火之类的状态不要改这个
    protected void setCost(int num) {
        cost = num;
    }

    protected int getCost() {
        return cost;
    }

    public void useWithoutCost(BattlePane bp) {
        useBase(bp, false);
    }

    private void useBase(BattlePane bp, boolean isCost) {
        if (isCost) {
            bp.useGuiHuo(getBelongTo(), cost);
        }

        lastUsedTarget = null;
        usePrivate(bp);

        if (coolDown != 0) {
            cooling = coolDown + 1;
        }
        StringBuilder sb = new StringBuilder(belongTo.name);
        if (lastUsedTarget != null) {
            sb.append("对").append(lastUsedTarget.name);
        }
        sb.append("使用了").append(getName());
        bp.log.addSkill(sb.toString());
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

    public int getSkillID() {
        return skillID;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }
}
