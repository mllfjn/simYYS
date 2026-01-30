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
    public static final String[] SKILL_LABEL = new String[]{"普攻", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾"};

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
    private final List<SkillListener> skillListeners = new ArrayList<>();

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
//        bp.onTrigger(new EventWillUseSkill(belongTo, this));
        useBase(bp, true);
    }

    protected void useDone() {
        if (!skillListeners.isEmpty()) {
            skillListeners.forEach(SkillListener::run);
            skillListeners.clear();
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

    protected void log(Character target) {
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

    /*public int getRealCost(boolean reallyUse) {
        int mustUse = cost;
        // 强制增加或减少消耗 比如猛火、SP千
        for (Status status : belongTo.getStatuses()) {
            if (status instanceof ForceChangeCost rc) {
                mustUse += rc.getChange();
            }
        }

        // 可选消耗更多 比如海月火玉
        AtomicInteger optionalUse = new AtomicInteger(0);
        belongTo.forEachYuHun(yuHun -> {
            if (yuHun instanceof HaiYueHuoYu) {
                optionalUse.set(optionalUse.get() + 1);
            }
        });

        int optionalUseInt = optionalUse.get();

        // 如果加上可选消耗都还小于等于0,直接返回0
        if (mustUse + optionalUseInt <= 0) {
            if (reallyUse) {
                belongTo.forEachYuHun(yuHun -> {
                    if (yuHun instanceof HaiYueHuoYu) {
                        ((HaiYueHuoYu) yuHun).enable();
                    }
                });
            }
            return 0;
        }

        // 有代价的减少，并且优先级较高，比如遗念火，入梦
        int mustUseForCounting = mustUse;
        int optionalUseForCounting = mustUse + optionalUseInt;
        List<ConditionalReduceCost> mustUseReduceCostList = null;
        List<ConditionalReduceCost> optionalUseReduceCostList = null;

        if (mustUseForCounting > 0) {
            mustUseReduceCostList = new ArrayList<>();
        }

        if (optionalUseInt > 0) {
            optionalUseReduceCostList = new ArrayList<>();
        }

        for (Status status : belongTo.getStatuses()) {
            if (status instanceof ConditionalReduceCost crc) {
                int reduce = crc.getReduce();
                if (mustUseForCounting > 0) {
                    mustUseReduceCostList.add(crc);
                    mustUseForCounting -= reduce;
                }

                if (optionalUseForCounting > 0 && optionalUseReduceCostList != null) {
                    optionalUseReduceCostList.add(crc);
                    optionalUseForCounting -= reduce;
                }

                if (mustUseForCounting <= 0 && optionalUseForCounting <= 0) {
                    break;
                }
            }
        }

        if (optionalUseReduceCostList != null && belongTo.bp.canUseGuiHuo(belongTo, optionalUseForCounting)) {
            if (reallyUse) {
                for (ConditionalReduceCost conditionalReduceCost : optionalUseReduceCostList) {
                    conditionalReduceCost.enable();
                }
                belongTo.forEachYuHun(yuHun -> {
                    if (yuHun instanceof HaiYueHuoYu) {
                        ((HaiYueHuoYu) yuHun).enable();
                    }
                });
            }
            return optionalUseForCounting;
        } else {
            if (reallyUse) {
                for (ConditionalReduceCost conditionalReduceCost : mustUseReduceCostList) {
                    conditionalReduceCost.enable();
                }
            }
            return mustUseForCounting;
        }
    }*/

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
        int skillID = getSkillID();
        String name = getName();
        return switch (skillID) {
            case 0 -> name;
            case 1 -> SKILL_LABEL[0] + "·" + name;
            default -> "妖术" + SKILL_LABEL[skillID - 1] + "·" + name;
        };
        /*if (skillID == 0) return name;
        if (skillID == 1) return SKILL_LABEL[0] + "·" + name;
        return "妖术" + SKILL_LABEL[skillID - 1] + "·" + name;*/
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

    public void addSkillListener(Runnable runnable) {
        skillListeners.add(new SkillListener(runnable));
    }

    private SkillCostResult getCostResult() {
        if (costResult == null) {
            costResult = new SkillCostResult(this);
        }
        return costResult;
    }

    static class SkillListener {
        private final Runnable runnable;

        public SkillListener(Runnable runnable) {
            this.runnable = runnable;
        }

        public void run() {
            runnable.run();
        }
    }
}
