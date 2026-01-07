package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventAttack;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.ParamCauseAttack;
import com.mllfjn.simyys.character.yuhun.YuHunAttack;
import com.mllfjn.simyys.character.yuhun.YuHunHitFeedBack;
import com.mllfjn.simyys.character.yuhun.list.ZhenZhu;
import com.mllfjn.simyys.customnode.CustomText;
import com.mllfjn.simyys.customnode.TextFlowLog;
import com.mllfjn.simyys.ratecontroller.RateController;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.determinant.IgnoreActionDecrease;
import com.mllfjn.simyys.character.status.determinant.IgnoreActionIncrease;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageBeingAttack;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class Interactive {
    private static final TextFlowLog.TextType type = TextFlowLog.TextType.NUMBER;
    private static final TextFlowLog.FontSize size = TextFlowLog.FontSize.SMALL;

    private Character owner;

    private final BattlePane bp;
    private final List<CustomText> guiHuoLog = new ArrayList<>();
    private final Map<Character, List<CustomText>> currentNumberLog = new LinkedHashMap<>();
    private final List<String> increaseLog = new ArrayList<>();
    private final Map<Character, Set<String>> yuHunEffect = new LinkedHashMap<>();

    public Interactive(BattlePane bp) {
        this.bp = bp;
    }

    public void setOwner(Character owner) {
        this.owner = owner;
    }

    public void doInterActive(Character character, Consumer<Interactive> action) {
        Character temp = owner;
        owner = character;

        action.accept(this);

        owner = temp;
    }

    public void display() {
        guiHuoLog.forEach(bp.log::addText);
        currentNumberLog.values().forEach(list -> {
            list.forEach(bp.log::addText);
            bp.log.addText("\n", type, TextFlowLog.TextColor.NORMAL, size);
        });

        yuHunEffect.forEach(((character, strings) ->
                bp.log.addText("\t" + character.name + "触发御魂：" + String.join("、", strings) + "\n"
                        , TextFlowLog.TextType.YU_HUN, TextFlowLog.TextColor.NORMAL, TextFlowLog.FontSize.NORMAL)));

        increaseLog.forEach(bp.log::addLocationChange);

        guiHuoLog.clear();
        currentNumberLog.clear();
        increaseLog.clear();
        yuHunEffect.clear();
    }

    private void addNumberRecord(Character character, CustomText text) {
        currentNumberLog.computeIfAbsent(character, k -> {
            List<CustomText> list = new ArrayList<>();
            list.add(new CustomText("\t" + character.name + "：", type, TextFlowLog.TextColor.NORMAL, size));
            return list;
        });

        currentNumberLog.get(character).add(text);
    }

    // 普攻的对群体伤害
    public InteractiveInfo[] attackTypical(Skill skill, List<Character> targets, int multiplier, AttackType attackType) {
        InteractiveInfo[] interactiveInfos = new InteractiveInfo[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            InteractiveInfo interactiveInfo = InteractiveInfo.createTypicalAttack(owner, skill, targets.get(i), multiplier);
            interactiveInfos[i] = interactiveInfo;
        }

        RateController.baoJi(skill.getName(), owner, bp.calc
                , (target) -> owner.getCritRate() / target.getCritResist(), targets, interactiveInfos);

        for (int i = 0; i < targets.size(); i++) {
            attack(interactiveInfos[i], attackType);
        }

        return interactiveInfos;
    }

    // 普通的对单体伤害
    public InteractiveInfo attackTypical(Skill skill, Character target, int multiplier, AttackType attackType) {
        InteractiveInfo interactiveInfo = InteractiveInfo.createTypicalAttack(owner, skill, target, multiplier);
        attack(interactiveInfo, attackType);
        return interactiveInfo;
    }

    // 对群体伤害,需要指定InteractiveInfo
    public void attack(Skill skill, List<Character> targets, AttackType attackType
            , Function<Character, InteractiveInfo> attackInfoGetter) {
        InteractiveInfo[] interactiveInfos = new InteractiveInfo[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            interactiveInfos[i] = attackInfoGetter.apply(targets.get(i));
        }

        RateController.baoJi(skill.getName(), owner, bp.calc
                , (target) -> owner.getCritRate() / target.getCritResist(), targets, interactiveInfos);

        for (int i = 0; i < targets.size(); i++) {
            attack(interactiveInfos[i], attackType);
        }
    }

    // 对单体伤害,需要指定InteractiveInfo
    public void attack(InteractiveInfo interactiveInfo, AttackType attackType) {
        if (interactiveInfo.canCrit() && !interactiveInfo.isCrit()) {
            RateController.baoJi(interactiveInfo.getSkill().getName(), owner, bp.calc
                    , (target) -> owner.getCritRate() / target.getCritResist()
                    , List.of(interactiveInfo.getTarget()), new InteractiveInfo[]{interactiveInfo});
        }
        attackBase(interactiveInfo, attackType);
    }

    private void attackBase(InteractiveInfo interactiveInfo, AttackType attackType) {
        // https://bbs.nga.cn/read.php?tid=26176854 阴阳师底层机制——单次伤害型技能中的结算顺序总结
        // https://bbs.nga.cn/read.php?tid=35530141 关于减伤的分类
        // https://bbs.nga.cn/read.php?tid=24250479 伤害结算机制详细分析

        Character target = interactiveInfo.getTarget();
        if (!target.alive) {
            return;
        }

        TraceableNumber traceableNumber = interactiveInfo.getTraceableNumber();

        // 技能系数
        double multiplier = interactiveInfo.getMultiplier();
        if (multiplier != 100) {
            traceableNumber.mul(0.01 * multiplier, "技能系数");
        }

        // 暴击
        if (interactiveInfo.isCrit()) {
            traceableNumber.mul(owner.getCritPower() * 0.01, "爆伤");
        }

        // 防御
        if (interactiveInfo.isCalDefence()) {
            double realDefense = Math.max(0, target.getDefence() - owner.getIgnoreDefense());
            traceableNumber.mul(300.0 / (300 + realDefense), "防御");
        }

        // 一般增伤乘区
        if (interactiveInfo.isCalZengShang()) {
            double zengShang = owner.getZengShang();
            if (zengShang != 0) {
                traceableNumber.mul(1 + zengShang / 100, "增伤");
            }
        }

        // 易伤
        if (interactiveInfo.isCalYiShang()) {
            double yiShang = target.getYiShang();
            if (yiShang != 1) {
                traceableNumber.mul(yiShang, "易伤");
            }
        }

        // 地震鲶,荒骷髅,雪幽魂等被攻击触发
        if (interactiveInfo.isCalYuHun()) {
            target.forEachYuHun(yuHun -> {
                if (yuHun instanceof YuHunHitFeedBack f) {
                    f.hitFeedBack();
                }
            });
        }

        // 护盾
        if (!interactiveInfo.isCanThroughShield()) {
            target.checkShield(interactiveInfo);
        }

        // 攻击者身上状态类影响
        for (Status status : owner.getStatuses()) {
            if (status instanceof InfluenceDamageWhenAttack iwa) {
                iwa.doInfluenceWhenAttack(attackType, interactiveInfo);
            }
        }

        // 被攻击者身上状态类影响
        for (Status status : target.getStatuses()) {
            if (status instanceof InfluenceDamageBeingAttack sid) {
                sid.doInfluenceBeingAttack(attackType, interactiveInfo);
            }
        }

        // 御魂 TODO 被攻击的人的御魂
        if (traceableNumber.getNumber() > 0 && interactiveInfo.isCalYuHun()) {
            owner.forEachYuHun(yuHun -> {
                if (yuHun instanceof YuHunAttack yei) {
                    yei.effectInfo(interactiveInfo);
                }
            });
        }

        // 总伤害限制,全游戏通用单段上限一千万,部分技能具有额外限制
        double limit = interactiveInfo.getLimit();
        if (traceableNumber.getNumber() > limit) {
            traceableNumber.set(limit, "达到上限");
        }

        target.beHurt(interactiveInfo);

        addNumberRecord(target, new CustomText(traceableNumber.getNumberString() + " "
                , traceableNumber.getTrace(), type
                , interactiveInfo.isCrit() ? TextFlowLog.TextColor.CRITICAL : TextFlowLog.TextColor.ATTACK, size));

        // 触发攻击的目标身上的状态
        target.statusRun(Trigger.AFTER_ATTACK, new ParamAfterAttack(interactiveInfo));

        // 触发攻击者身上的攻击监听
        owner.statusRun(Trigger.CAUSE_ATTACK, new ParamCauseAttack(interactiveInfo));

        if (!interactiveInfo.isCancel()) {
            // 广播攻击信息
            bp.onTrigger(new EventAttack(interactiveInfo));
        }
    }

    public InteractiveInfo healTypical(Skill skill, Character target, int multiplier) {
        return healTypical(skill, List.of(target), multiplier)[0];
    }

    public InteractiveInfo[] healTypical(Skill skill, List<Character> targets, int multiplier) {
        InteractiveInfo[] interactiveInfos = new InteractiveInfo[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            interactiveInfos[i] = InteractiveInfo.createTypicalHeal(owner, skill, targets.get(i), multiplier);
        }

        RateController.baoJi(skill.getName(), owner, bp.calc
                , (c) -> owner.getCritRate(), targets, interactiveInfos);

        for (int i = 0; i < targets.size(); i++) {
            healBase(targets.get(i), interactiveInfos[i]);
        }

        return interactiveInfos;
    }

    public void heal(Skill skill, List<Character> targets
            , Function<Character, InteractiveInfo> interactiveInfoSupplier) {
        InteractiveInfo[] interactiveInfos = new InteractiveInfo[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            interactiveInfos[i] = interactiveInfoSupplier.apply(targets.get(i));
        }

        RateController.baoJi(skill.getName(), owner, bp.calc
                , (c) -> owner.getCritRate(), targets, interactiveInfos);

        for (int i = 0; i < targets.size(); i++) {
            healBase(targets.get(i), interactiveInfos[i]);
        }
    }

    private void healBase(Character target, InteractiveInfo interactiveInfo) {
        if (!target.alive) {
            return;
        }

        TraceableNumber traceableNumber = interactiveInfo.getTraceableNumber();

        // 技能系数
        double multiplier = interactiveInfo.getMultiplier();
        if (multiplier != 100) {
            traceableNumber.mul(0.01 * multiplier, "技能系数");
        }

        // 暴击
        if (interactiveInfo.isCrit()) {
            traceableNumber.mul(owner.getCritPower() * 0.01, "爆伤");
        }

        owner.forEachYuHun(yuHun -> {
            if (yuHun instanceof ZhenZhu zz) {
                zz.doInteractive(target, traceableNumber);
            }
        });

        target.beHeal(interactiveInfo);

        addNumberRecord(target, new CustomText(traceableNumber.getNumberString() + " "
                , traceableNumber.getTrace()
                , type, TextFlowLog.TextColor.HEAL, size));

    }

    public void recovery(Skill skill, Character target, double num) {
        InteractiveInfo interactiveInfo = InteractiveInfo.createRecovery(owner, skill, target, (c1, c2) -> num);
        TraceableNumber traceableNumber = interactiveInfo.getTraceableNumber();

        target.recovery(num);

        addNumberRecord(target, new CustomText(traceableNumber.getNumberString() + " "
                , traceableNumber.getTrace()
                , type, TextFlowLog.TextColor.HEAL, size));
    }

    public EffectInfo[] effect(Skill skill, List<Character> targets, int baseRate, boolean calHit
            , StatusSupplier statusSupplier) {
        EffectInfo[] infos = RateController
                .mingZhong(skill, statusSupplier.getStatusName(), owner, targets, baseRate, calHit, bp.calc);
        for (int i = 0; i < targets.size(); i++) {
            effect(infos[i], targets.get(i), statusSupplier);
        }
        return infos;
    }

    public void effect(Skill skill, Character target, int baseRate, boolean calHit
            , StatusSupplier statusSupplier) {

        EffectInfo info = RateController
                .mingZhong(skill, statusSupplier.getStatusName(), owner, List.of(target), baseRate, calHit, bp.calc)[0];

        effect(info, target, statusSupplier);
    }

    private void effect(EffectInfo effectInfo, Character target, StatusSupplier statusSupplier) {
        if (effectInfo.isHit()) {
            if (statusSupplier.isCrowdControl()) {
                target.statusRun(Trigger.ADDING_CROWD_CONTROL, new ParamAddCrowdControl(effectInfo));
            }
            if (!effectInfo.isCancel()) {
                statusSupplier.supply(owner, target);
            }
        }
    }

    public void getNewRound(Character target) {
        bp.situation.getNewRound(target);
        increaseLog.add(target.name + "获得新回合");
    }

    public void increaseLocation(Character target, double increase) {
        // 免疫行动条提升效果
        for (Status status : target.getStatuses()) {
            if (status instanceof IgnoreActionIncrease fi && fi.effective(owner)) {
                increaseLog.add(target.name + "免疫行动条改变");
                return;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(target.name).append("行动提前").append((int) increase).append("%");
        double location = target.getLocation();
        if (location + increase > 100) {
            sb.append("(实际提前").append((int) (100 - location)).append("%)");
        }

        target.setLocation(Math.min(100, location + increase));
        increaseLog.add(sb.toString());

        // 拉条事件
        target.statusRun(Trigger.INCREASE_LOCATION, null);
    }

    public void decreaseLocation(Character target, double decrease) {
        // 免疫行动条提升效果
        for (Status status : target.getStatuses()) {
            if (status instanceof IgnoreActionDecrease) {
                increaseLog.add(target.name + "免疫行动条改变");
                return;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(target.name).append("行动推后").append((int) decrease).append("%");
        double location = target.getLocation();
        if (location - decrease < 0) {
            sb.append("(实际推后").append((int) location).append("%)");
        }

        target.setLocation(Math.max(0, location - decrease));
        increaseLog.add(sb.toString());
    }

    public void guiHuo(Character character, int num) {
        guiHuoLog.add(new CustomText(
                "\t" + character.name + (num > 0 ? "获得" : "消耗") + "鬼火" + Math.abs(num) + "\n"
                , TextFlowLog.TextType.GUI_HUO, TextFlowLog.TextColor.NORMAL, TextFlowLog.FontSize.NORMAL));
    }

    public void addYuHunEffectLog(Character character, String yuHunName) {
        yuHunEffect.computeIfAbsent(character, k -> new LinkedHashSet<>()).add(yuHunName);
    }
}
