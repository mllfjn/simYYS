package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventAttack;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.yuhun.YuHunAttack;
import com.mllfjn.simyys.character.yuhun.list.DiZhenNian;
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

    public AttackInfo[] attackTypical(Skill skill, List<Character> targets, int multiplier, AttackType attackType) {
        AttackInfo[] attackInfos = new AttackInfo[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            AttackInfo attackInfo = AttackInfo.createTypicalAttack(owner, skill, targets.get(i), multiplier);
            attackInfos[i] = attackInfo;
        }

        RateController.baoJi(skill.getName(), owner, bp.calc, targets, attackInfos);

        for (int i = 0; i < targets.size(); i++) {
            attack(attackInfos[i], attackType);
        }

        return attackInfos;
    }

    public AttackInfo attackTypical(Skill skill, Character target, int multiplier, AttackType attackType) {
        AttackInfo attackInfo = AttackInfo.createTypicalAttack(owner, skill, target, multiplier);
        attack(attackInfo, attackType);
        return attackInfo;
    }

    public AttackInfo attack(AttackInfo attackInfo, AttackType attackType) {
        if (attackInfo.canCrit() && !attackInfo.isCrit()) {
            RateController.baoJi(attackInfo.getSkill().getName(), owner, bp.calc
                    , List.of(attackInfo.getTarget()), attackInfo);
        }
        attackBase(attackInfo, attackType);
        return attackInfo;
    }

    private void attackBase(AttackInfo attackInfo, AttackType attackType) {
        // https://bbs.nga.cn/read.php?tid=26176854 阴阳师底层机制——单次伤害型技能中的结算顺序总结
        // https://bbs.nga.cn/read.php?tid=35530141 关于减伤的分类
        // https://bbs.nga.cn/read.php?tid=24250479 伤害结算机制详细分析

        Character target = attackInfo.getTarget();

        if (!target.alive) {
            return;
        }

        TraceableNumber traceableNumber = attackInfo.getTraceableNumber();

        // 基础伤害
        traceableNumber.add(attackInfo.getBasicNumber().apply(owner, target), "基础伤害");

        // 技能系数
        traceableNumber.mul(0.01 * attackInfo.getMultiplier(), "技能系数");

        // 暴击
        if (attackInfo.isCrit()) {
            traceableNumber.mul(owner.getCritPower() * 0.01, "爆伤");
        }

        // 防御
        if (attackInfo.isCalDefence()) {
            double realDefense = target.getDefence() - owner.getIgnoreDefense();
            traceableNumber.mul(300.0 / (300 + realDefense), "防御");
        }

        // 一般增伤乘区
        if (attackInfo.isCalZengShang()) {
            traceableNumber.mul(1 + owner.getZengShang() / 100, "增伤");
        }

        // 易伤
        if (attackInfo.isCalYiShang()) {
            traceableNumber.mul(target.getYiShang(), "易伤");
        }

        // 地震鲶
        // 挺奇怪的,好像只有地震鲶才在这里触发,其他御魂都是要在护盾之后
        // 发现雪幽魂被攻击减速也是在这里
        if (attackInfo.isCalYuHun()) {
            target.forEachYuHun(yuHun -> {
                if (yuHun instanceof DiZhenNian d) {
                    d.takeEffect();
                }
            });
        }

        // 护盾
        target.checkShield(attackInfo);

        // 攻击者身上状态类影响
        for (Status status : owner.getStatuses()) {
            if (status instanceof InfluenceDamageWhenAttack iwa) {
                iwa.doInfluenceWhenAttack(attackType, attackInfo);
            }
        }

        // 被攻击者身上状态类影响
        for (Status status : target.getStatuses()) {
            if (status instanceof InfluenceDamageBeingAttack sid) {
                sid.doInfluenceBeingAttack(attackType, attackInfo);
            }
        }

        // 御魂 TODO 被攻击的人的御魂
        if (traceableNumber.getNumber() > 0 && attackInfo.isCalYuHun()) {
            owner.forEachYuHun(yuHun -> {
                if (yuHun instanceof YuHunAttack yei) {
                    yei.effectInfo(attackInfo);
                }
            });
        }

        target.beHurt(attackInfo);


        addNumberRecord(target, new CustomText(traceableNumber.getNumberString() + " "
                , traceableNumber.getTrace(), type
                , attackInfo.isCrit() ? TextFlowLog.TextColor.CRITICAL : TextFlowLog.TextColor.ATTACK, size));

        if (!attackInfo.isCancel()) {
            // 广播攻击信息
            bp.onTrigger(new EventAttack(attackInfo));
        }
    }

    public AttackInfo heal(Skill skill, Character target, int multiplier) {
        AttackInfo attackInfo = AttackInfo.createTypicalHeal(owner, skill, target, multiplier);
        RateController.baoJi(skill.getName(), owner, bp.calc, List.of(target), attackInfo);

        return heal(target, attackInfo);
    }

    public void heal(Skill skill, List<Character> targets, int multiplier) {
        AttackInfo[] attackInfos = new AttackInfo[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            attackInfos[i] = AttackInfo.createTypicalHeal(owner, skill, targets.get(i), multiplier);
        }

        RateController.baoJi(skill.getName(), owner, bp.calc, targets, attackInfos);

        for (int i = 0; i < targets.size(); i++) {
            heal(targets.get(i), attackInfos[i]);
        }

    }

    private AttackInfo heal(Character target, AttackInfo attackInfo) {
        if (!target.alive) {
            return null;
        }

        TraceableNumber traceableNumber = attackInfo.getTraceableNumber();

        // 基础数值
        traceableNumber.add(attackInfo.getBasicNumber().apply(owner, target), "生命上限");

        // 技能系数
        traceableNumber.mul(0.01 * attackInfo.getMultiplier(), "技能系数");

        // 暴击
        if (attackInfo.isCrit()) {
            traceableNumber.mul(owner.getCritPower() * 0.01, "爆伤");
        }

        target.beHeal(attackInfo);

        addNumberRecord(target, new CustomText(traceableNumber.getNumberString() + " "
                , traceableNumber.getTrace()
                , type, TextFlowLog.TextColor.HEAL, size));

        return attackInfo;
    }

    public void recovery(Skill skill, Character target, double num) {
        AttackInfo attackInfo = AttackInfo.createRecovery(owner, skill, target, (c1, c2) -> num);
        TraceableNumber traceableNumber = attackInfo.getTraceableNumber();

        traceableNumber.add(num, "恢复数值");

        target.recovery(num);

        addNumberRecord(target, new CustomText(traceableNumber.getNumberString() + " "
                , traceableNumber.getTrace()
                , type, TextFlowLog.TextColor.HEAL, size));
    }

    public EffectInfo[] effect(Skill skill, String statusName, List<Character> targets, int baseRate, boolean calHit
            , BiFunction<Character, Character, Status> statusSupplier) {
        EffectInfo[] infos = RateController
                .mingZhong(skill, statusName, owner, targets, statusSupplier, baseRate, calHit, bp.calc);
        for (int i = 0; i < targets.size(); i++) {
            effect(infos[i], targets.get(i), statusSupplier);
        }
        return infos;
    }

    public void effect(Skill skill, String statusName, Character target, int baseRate, boolean calHit
            , BiFunction<Character, Character, Status> statusSupplier) {

        EffectInfo info = RateController
                .mingZhong(skill, statusName, owner, List.of(target), statusSupplier, baseRate, calHit, bp.calc)[0];

        effect(info, target, statusSupplier);
    }

    private void effect(EffectInfo effectInfo, Character target
            , BiFunction<Character, Character, Status> statusSupplier) {
        if (effectInfo.isHit()) {
            target.statusRun(Trigger.ADDING_CROWD_CONTROL, new ParamAddCrowdControl(effectInfo));
            if (!effectInfo.isCancel()) {
                target.addStatus(statusSupplier.apply(owner, target));
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
