package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventUsePuGong;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.ForceChangeCost;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.yuhun.list.HaiYueHuoYu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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

        useDone();
    }

    protected void useDone() {
        if (!skillListeners.isEmpty()) {
            skillListeners.forEach(SkillListener::run);
            skillListeners.clear();
        }
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

    protected void useBase(BattlePane bp, boolean isCost) {
        int realCost = 0;
        if (isCost) {
            realCost = getRealCost(true);
            bp.useGuiHuo(getBelongTo(), realCost);
        }

        Optional<Character> target = usePrivate(bp);

        if (coolDown != 0) {
            cooling = coolDown + 1;
        }
        StringBuilder sb = new StringBuilder(belongTo.name);
        target.ifPresent(character -> sb.append("对").append(character.name));
        sb.append("使用了").append(getName());
        if (realCost != 0) {
            bp.interactive.guiHuo(belongTo, -realCost);
//            bp.log.addGuiHuo(belongTo, -realCost);
//            sb.append("\n\t\t消耗鬼火:").append(realCost);
        }
        bp.log.addSkill(sb.toString());
    }

    public int getRealCost(boolean reallyUse) {
        int realCost = cost;
        for (Status status : belongTo.getStatuses()) {
            if (status instanceof ForceChangeCost rc) {
                realCost += rc.getChange();
                // 因为后续可能会消耗更多，所以不能在小于0时就返回
                /*if (realCost <= 0) {
                    return 0;
                }*/
            }
        }

        // 海月火玉 可选消耗更多
        AtomicInteger optionalUse = new AtomicInteger(0);
        belongTo.forEachYuHun(yuHun -> {
            if (yuHun instanceof HaiYueHuoYu) {
                optionalUse.set(optionalUse.get() + 1);
            }
        });

        if (belongTo.bp.canUseGuiHuo(belongTo, realCost + optionalUse.get())) {
            if (reallyUse) {
                belongTo.forEachYuHun(yuHun -> {
                    if (yuHun instanceof HaiYueHuoYu) {
                        ((HaiYueHuoYu) yuHun).enable();
                    }
                });
            }
            return Math.max(0, realCost + optionalUse.get());
        } else {
            return Math.max(0, realCost);
        }
    }

    public abstract Optional<Character> usePrivate(BattlePane bp);

    public boolean canUse(BattlePane bp) {
        return cooling == 0 && bp.canUseGuiHuo(belongTo, getRealCost(false));
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

    public void addSkillListener(Runnable runnable) {
        skillListeners.add(new SkillListener(runnable));
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
