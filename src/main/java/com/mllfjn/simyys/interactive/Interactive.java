package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.TraversalOrderManager;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.yuhun.YuHunAfterCauseAttack;
import com.mllfjn.simyys.character.yuhun.YuHunAttack;
import com.mllfjn.simyys.character.yuhun.YuHunHitFeedBack;
import com.mllfjn.simyys.character.yuhun.list.ZhenZhu;
import com.mllfjn.simyys.customnode.CustomText;
import com.mllfjn.simyys.customnode.TextFlowLog;
import com.mllfjn.simyys.ratecontroller.RateController;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.determinant.IgnoreActionDecrease;
import com.mllfjn.simyys.character.status.determinant.IgnoreActionIncrease;

import java.util.*;
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
        currentNumberLog.values().forEach(list -> {
            list.forEach(bp.log::addText);
            bp.log.addText("\n", type, TextFlowLog.TextColor.NORMAL, size);
        });

        yuHunEffect.forEach(((character, strings) ->
                bp.log.addText("\t" + character.name + "触发御魂：" + String.join("、", strings) + "\n"
                        , TextFlowLog.TextType.YU_HUN, TextFlowLog.TextColor.NORMAL, TextFlowLog.FontSize.NORMAL)));

        guiHuoLog.forEach(bp.log::addText);
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
    public AttackInfo[] attackTypical(Skill skill, List<Character> targets, double multiplier, AttackType attackType) {
        AttackInfo[] attackInfos = new AttackInfo[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            AttackInfo attackInfo = AttackInfo.createTypicalAttack(owner, skill, targets.get(i), multiplier, attackType);
            attackInfos[i] = attackInfo;
        }

        RateController.baoJi(skill.getName(), owner, bp.calc
                , (target) -> owner.getCritRate() - target.getCritResist(), targets, attackInfos);

        for (int i = 0; i < targets.size(); i++) {
            attack(attackInfos[i]);
        }

        return attackInfos;
    }

    // 普通的对单体伤害
    public AttackInfo attackTypical(Skill skill, Character target, double multiplier, AttackType attackType) {
        AttackInfo attackInfo = AttackInfo.createTypicalAttack(owner, skill, target, multiplier, attackType);
        attack(attackInfo);
        return attackInfo;
    }

    // 对群体伤害,需要指定AttackInfo
    public void attack(Skill skill, List<Character> targets, Function<Character, AttackInfo> attackInfoGetter) {
        AttackInfo[] attackInfos = new AttackInfo[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            attackInfos[i] = attackInfoGetter.apply(targets.get(i));
        }

        RateController.baoJi(skill.getName(), owner, bp.calc
                , (target) -> owner.getCritRate() - target.getCritResist(), targets, attackInfos);

        for (int i = 0; i < targets.size(); i++) {
            attack(attackInfos[i]);
        }
    }

    // 对单体伤害,需要指定AttackInfo
    public void attack(AttackInfo attackInfo) {
        if (attackInfo.canCrit() && !attackInfo.isCrit()) {
            RateController.baoJi(attackInfo.getSkill().getName(), owner, bp.calc
                    , (target) -> owner.getCritRate() - target.getCritResist()
                    , List.of(attackInfo.getTarget()), new AttackInfo[]{attackInfo});
        }
        attackBase(attackInfo);
    }

    private void attackBase(AttackInfo attackInfo) {
        // https://bbs.nga.cn/read.php?tid=26176854 阴阳师底层机制——单次伤害型技能中的结算顺序总结
        // https://bbs.nga.cn/read.php?tid=35530141 关于减伤的分类
        // https://bbs.nga.cn/read.php?tid=24250479 伤害结算机制详细分析

        Character target = attackInfo.getTarget();
        if (!target.alive) {
            return;
        }

        TraceableNumber traceableNumber = attackInfo.getTraceableNumber();

        // 技能系数
        double multiplier = attackInfo.getMultiplier();
        if (multiplier != 100) {
            traceableNumber.mul(0.01 * multiplier, "技能系数");
        }

        // 暴击
        if (attackInfo.isCrit()) {
            traceableNumber.mul(owner.getCritPower() * 0.01, "爆伤");
        }

        // 防御
        if (attackInfo.isCalDefence()) {
            double defense = TraversalOrderManager
                    .getActualDefense(attackInfo.getAttacker(), target, attackInfo.getAttackType());
            traceableNumber.mul(300.0 / (300 + defense), (int) defense + "防御");
        }

        // 伤害波动
        double fluctuationLimit = attackInfo.getFluctuationLimit();
        if (fluctuationLimit != 0) {
            traceableNumber.mul(
                    RateController.getFluctuation(bp.calc, fluctuationLimit),
                    "伤害波动"
            );
        }

        // 一般增伤乘区
        if (attackInfo.isCalZengShang()) {
            double zengShang = owner.getZengShang();
            if (zengShang != 0) {
                traceableNumber.mul(1 + zengShang / 100, "增伤");
            }
        }

        // 易伤
        if (attackInfo.isCalYiShang()) {
            double yiShang = target.getYiShang();
            if (yiShang != 1) {
                traceableNumber.mul(yiShang, "易伤");
            }
        }

        // 地震鲶,荒骷髅,雪幽魂等被攻击触发
        if (attackInfo.isCalAttackYuHun()) {
            target.forEachYuHun(yuHun -> {
                if (yuHun instanceof YuHunHitFeedBack f) {
                    f.hitFeedBack(attackInfo);
                }
            });
        }

        // 护盾
        if (!attackInfo.isCanThroughShield()) {
            target.checkShield(attackInfo);
        }

        // 攻击者身上状态类影响
        for (Status status : owner.getStatuses()) {
            if (status instanceof InfluenceDamageWhenAttack iwa) {
                iwa.doInfluenceWhenAttack(attackInfo);
            }
        }

        // 被攻击者身上状态类影响
        target.statusRun(Trigger.BEING_ATTACKED, new ParamAttackInfo(attackInfo));

        if (attackInfo.isCancel()) {
            return;
        }

        // 部分造成伤害时生效的御魂(破势狂骨等)
        if (attackInfo.isCalAttackYuHun() && traceableNumber.getNumber() > 0) {
            owner.forEachYuHun(yuHun -> {
                if (yuHun instanceof YuHunAttack yei) {
                    yei.effectInfo(attackInfo);
                }
            });
        }

        // 总伤害限制,全游戏通用单段上限一千万,部分技能具有额外限制
        double limit = attackInfo.getLimit();
        if (traceableNumber.getNumber() > limit) {
            traceableNumber.set(limit, "达到上限");
        }

        target.beHurt(attackInfo);

        addNumberRecord(target, new CustomText(traceableNumber.getNumberString() + " "
                , traceableNumber.getTrace(), type
                , attackInfo.isCrit() ? TextFlowLog.TextColor.CRITICAL : TextFlowLog.TextColor.ATTACK, size));

        // 部分造成伤害后生效的御魂(日女等)
        if (traceableNumber.getNumber() > 0 && attackInfo.isCalEffectYuHun()) {
            owner.forEachYuHun(yuHun -> {
                if (yuHun instanceof YuHunAfterCauseAttack yca) {
                    yca.action(attackInfo, this);
                }
            });
        }

        // 触发攻击的目标身上的状态
        target.statusRun(Trigger.AFTER_ATTACK, new ParamAttackInfo(attackInfo));

        // 触发攻击者身上的攻击监听
        owner.statusRun(Trigger.CAUSE_ATTACK, new ParamAttackInfo(attackInfo));
    }

    public HealInfo healTypical(Skill skill, Character target, int multiplier) {
        return healTypical(skill, List.of(target), multiplier)[0];
    }

    public HealInfo[] healTypical(Skill skill, List<Character> targets, int multiplier) {
        HealInfo[] healInfos = new HealInfo[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            healInfos[i] = HealInfo.createTypicalHeal(owner, skill, targets.get(i), multiplier);
        }

        RateController.baoJi(skill.getName(), owner, bp.calc
                , (c) -> owner.getCritRate(), targets, healInfos);

        for (int i = 0; i < targets.size(); i++) {
            healBase(targets.get(i), healInfos[i]);
        }

        return healInfos;
    }

    public HealInfo[] heal(Skill skill, List<Character> targets
            , Function<Character, HealInfo> healInfoSupplier) {
        HealInfo[] healInfos = new HealInfo[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            healInfos[i] = healInfoSupplier.apply(targets.get(i));
        }

        RateController.baoJi(skill.getName(), owner, bp.calc
                , (c) -> owner.getCritRate(), targets, healInfos);

        for (int i = 0; i < targets.size(); i++) {
            healBase(targets.get(i), healInfos[i]);
        }
        return healInfos;
    }

    private void healBase(Character target, HealInfo healInfo) {
        if (!target.alive) {
            return;
        }

        TraceableNumber traceableNumber = healInfo.getTraceableNumber();

        // 技能系数
        double multiplier = healInfo.getMultiplier();
        if (multiplier != 100) {
            traceableNumber.mul(0.01 * multiplier, "技能系数");
        }

        // 暴击
        if (healInfo.isCrit()) {
            traceableNumber.mul(owner.getCritPower() * 0.01, "爆伤");
        }

        owner.forEachYuHun(yuHun -> {
            if (yuHun instanceof ZhenZhu zz) {
                zz.doInteractive(target, traceableNumber);
            }
        });

        target.beHeal(healInfo);

        addNumberRecord(target, new CustomText(traceableNumber.getNumberString() + " "
                , traceableNumber.getTrace()
                , type, TextFlowLog.TextColor.HEAL, size));

    }

    public void recovery(Skill skill, Character target, double num) {
        HealInfo healInfo = HealInfo.createRecovery(owner, skill, target, num);
        TraceableNumber traceableNumber = healInfo.getTraceableNumber();

        target.recovery(num);

        addNumberRecord(target, new CustomText(traceableNumber.getNumberString() + " "
                , traceableNumber.getTrace()
                , type, TextFlowLog.TextColor.HEAL, size));
    }

    public EffectInfo[] effect(Skill skill, List<Character> targets, int baseRate, int additionRate, boolean calHit
            , StatusSupplier statusSupplier) {
        if (targets.isEmpty()) {
            return null;
        }
        EffectInfo[] infos = RateController
                .mingZhong(skill, statusSupplier.getStatusName(), owner, targets, baseRate, additionRate, calHit, bp.calc);
        for (int i = 0; i < targets.size(); i++) {
            effectBase(infos[i], targets.get(i), statusSupplier);
        }
        return infos;
    }

    public EffectInfo effect(Skill skill, Character target, int baseRate, int additionRate, boolean calHit
            , StatusSupplier statusSupplier) {

        EffectInfo info = RateController
                .mingZhong(skill, statusSupplier.getStatusName(), owner, List.of(target), baseRate, additionRate, calHit, bp.calc)[0];

        effectBase(info, target, statusSupplier);
        return info;
    }

    private void effectBase(EffectInfo effectInfo, Character target, StatusSupplier statusSupplier) {
        if (effectInfo.isHit()) {
            if (statusSupplier.isCrowdControl()) {
                target.statusRun(Trigger.ADDING_CROWD_CONTROL, new ParamAddCrowdControl(effectInfo));
            }
            if (!effectInfo.isCancel()) {
                statusSupplier.supply(owner, target);
                if (statusSupplier.isCrowdControl()) {
                    owner.statusRun(Trigger.MAKING_CROWD_CONTROL, new ParamAddCrowdControl(effectInfo));
                }
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

        target.setLocation(Math.min(100, location + increase), true);
        increaseLog.add(sb.toString());
    }

    public void decreaseLocation(Character target, double decrease) {
        // 免疫行动条提升效果
        for (Status status : target.getStatuses()) {
            if (status instanceof IgnoreActionDecrease iad) {
                increaseLog.add(target.name + "免疫行动条改变");
                iad.takeEffectFeedBack();
                return;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(target.name).append("行动推后").append((int) decrease).append("%");
        double location = target.getLocation();
        if (location - decrease < 0) {
            sb.append("(实际推后").append((int) location).append("%)");
        }

        target.setLocation(Math.max(0, location - decrease), false);
        increaseLog.add(sb.toString());
    }

    public void guiHuo(Character character, int num, String name) {
        guiHuoLog.add(new CustomText(
                "\t" + character.name + (num > 0 ? "获得" : "消耗") + name + Math.abs(num) + "\n"
                , TextFlowLog.TextType.GUI_HUO, TextFlowLog.TextColor.NORMAL, TextFlowLog.FontSize.NORMAL));
    }

    public void addYuHunEffectLog(Character character, String yuHunName) {
        yuHunEffect.computeIfAbsent(character, k -> new LinkedHashSet<>()).add(yuHunName);
    }
}
