package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Skill implements Serializable {
    private static final String[] SKILL_LABEL = new String[]{"普攻", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾"};

    private final Character belongTo;
    private final int level;
    private final int skillID;
    // 技能本身的消耗
    private int cost;
    // 技能冷却时间
    private int coolDown;
    // 技能当前的冷却回合数，因为回合结束后判定一次技能冷却，所以设定时要+1
    private int cooling;

    // 技能消耗计算结果:技能是否可以使用计算一次,技能确定使用计算一次,UI的技能选择框又计算一次,所以保存结果省计算次数
    private transient SkillCostResult costResult;

    // 当技能结束时，遍历通知
    private List<SkillEndListener> skillEndListeners;

    public Skill(Character belongTo, int level, int cost, int coolDown, int skillID) {
        this.belongTo = belongTo;
        this.level = level;
        this.cost = cost;
        this.coolDown = coolDown;
        this.skillID = skillID;
    }

    public static Skill getInstance(String name) {
        return new Skill(null, 0, 0, 0, 0) {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public Optional<Character> usePrivate(BattlePane bp) {
                return Optional.empty();
            }
        };
    }

    public void refresh() {
        costResult = null;
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

    public void useDone() {
        if (skillEndListeners != null && !skillEndListeners.isEmpty()) {
            skillEndListeners.forEach(SkillEndListener::run);
            skillEndListeners.clear();
        }
    }

    protected void setCost(int num) {
        cost = num;
    }

    protected int getCost() {
        return cost;
    }

    public void useWithoutCost(BattlePane bp) {
        useBase(bp, false);
    }

    protected void useBase(BattlePane bp, boolean isCost) {
        belongTo.statusRun(Trigger.WILL_USE_SKILL, null);

        if (isCost) {
            int finalCost = getCostResult().getFinalCost();
            costResult.reallyUse();
            if (finalCost > 0) {
                bp.useGuiHuo(getBelongTo(), finalCost);
            }
        }

        Character target = usePrivate(bp).orElse(null);

        // 冷却
        if (coolDown != 0) {
            cooling = coolDown + 1;
        }

        // 消息记录
        log(target);

        // 释放完毕技能
        belongTo.statusRun(Trigger.USED_SKILL, new ParamUseSkill(this, target));

        useDone();
    }

    public void log(Character target) {
        log(belongTo, target);
    }

    protected void log(Character skillUser, Character target) {
        StringBuilder sb = new StringBuilder(skillUser.name);
        if (target != null) {
            sb.append("对").append(target.name);
        }
        sb.append("使用了").append(getName());
        belongTo.bp.log.addSkill(sb.toString());
    }

    public int getRealCost() {
        return getCostResult().getFinalCost();
    }

    public abstract Optional<Character> usePrivate(BattlePane bp);

    public boolean canUse(BattlePane bp) {
        return cooling == 0 && bp.canUseGuiHuo(belongTo, getRealCost());
    }

    public Character getBelongTo() {
        return this.belongTo;
    }

    public int getLevel() {
        return this.level;
    }

    public int getCooling() {
        return cooling;
    }

    public void pastRound() {
        if (cooling > 0) cooling--;
    }

    @Override
    public String toString() {
        String name = getName();
        return switch (skillID) {
            case 0 -> name;
            case 1 -> SKILL_LABEL[0] + "·" + name;
            default -> "妖术" + SKILL_LABEL[skillID - 1] + "·" + name;
        };
    }

    public int getSkillID() {
        return skillID;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    public void setCooling(int cooling) {
        this.cooling = cooling;
    }

    public String getSkillDesc() {
        return null;
    }

    public void addSkillEndListener(Runnable runnable) {
        if (skillEndListeners == null) {
            skillEndListeners = new ArrayList<>();
        }
        skillEndListeners.add(new SkillEndListener(runnable));
    }

    private SkillCostResult getCostResult() {
        if (costResult == null) {
            costResult = new SkillCostResult(this);
        }
        return costResult;
    }

    private record SkillEndListener(Runnable runnable) {
        private void run() {
            runnable.run();
        }
    }
}
