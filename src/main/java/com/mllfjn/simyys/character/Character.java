package com.mllfjn.simyys.character;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.TeamPane;
import com.mllfjn.simyys.character.list.mob.multiplayer.InfoDisplay;
import com.mllfjn.simyys.character.list.sp.dayuan.StatusCombined;
import com.mllfjn.simyys.character.list.sp.dayuan.StatusShengTian;
import com.mllfjn.simyys.character.list.ssr.beimihu.StatusShiZhiHui;
import com.mllfjn.simyys.character.list.ssr.beimihu.StatusShiZhiXi;
import com.mllfjn.simyys.character.propertygetter.*;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.skill.SkillAuto;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.StatusRunnable;
import com.mllfjn.simyys.character.status.determinant.IgnoreChangeMaxHp;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;
import com.mllfjn.simyys.character.status.determinant.PreventDie;
import com.mllfjn.simyys.character.status.determinant.RejectAllStatuses;
import com.mllfjn.simyys.character.status.instance.StatusBind;
import com.mllfjn.simyys.character.status.instance.StatusConfusion;
import com.mllfjn.simyys.character.status.instance.StatusPoisoning;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.ParamLocationChange;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunFactory;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;
import com.mllfjn.simyys.character.list.yys.qiling.QiLingFactory;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.TraceableNumber;
import com.mllfjn.simyys.interactive.InteractiveInfo;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.guihuo.MobGuiHuo;
import com.mllfjn.simyys.collections.SerializableObservableList;
import com.mllfjn.simyys.battleevent.EventHpChange;
import com.mllfjn.simyys.ratecontroller.RateController;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class Character implements Serializable {
    public String name;
    public int team;
    public int timesToAct;
    private double location;
    private int lockSkill;
    public boolean alive = true;
    private boolean isInRound = false;

    private double maxHp;
    private double hp;
    private double baseAttack;
    private double additionAttack;
    private double defence;
    private double critRate;
    private double critPower;
    private double effectHitRate;
    private double effectResistRate;
    private double speed;

    private boolean isMob;
    private boolean isYYS;
    protected boolean isSummon;

    private final SerializableObservableList<Skill> skills = new SerializableObservableList<>();
    protected Map<Integer, Integer> lockSkillMap;
    protected Map<Integer, FlagChangeInfo> flagChangeMap;

    // 自身状态
    private final List<Status> statuses = new ArrayList<>();
    // 维持的状态
    private final List<Status> maintainedStatuses = new ArrayList<>();
    // 御魂列表
    private final LinkedHashSet<YuHun> yuHunSet = new LinkedHashSet<>();
    // 头像
    private CharacterIcon characterIcon;

    public transient BattlePane bp;

    public PropertiesMap getProperties() {
        PropertiesMap map = new PropertiesMap();
        for (String key : PropertyKey.GENERAL_INPUT_KEYS) {
            map.put(key, new PropertyInput());
        }
        ((PropertyInput) map.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).setValue(getDefaultBaseAttack());

        for (String key : PropertyKey.GENERAL_CHECK_KEYS) {
            map.put(key, new PropertyCheck());
        }

        return map;
    }

    protected abstract String getDefaultBaseAttack();

    public String getName() {
        return name;
    }

    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        reset(bp);
        this.lockSkillMap = propertiesHolder.lockSkillMap;
        this.flagChangeMap = propertiesHolder.flagChangeMap;
        PropertiesMap properties = propertiesHolder.propertiesMap;
        this.name = propertiesHolder.name;
        this.speed = properties.get(PropertyKey.GENERAL_SPEED_KEY).getDouble();
        this.baseAttack = properties.get(PropertyKey.GENERAL_BASE_ATTACK_KEY).getDouble();
        this.additionAttack = properties.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY).getDouble();
        this.team = properties.get(PropertyKey.GENERAL_TEAM_KEY).getBoolean() ? 1 : 0;
        this.hp = Math.max(properties.get(PropertyKey.GENERAL_HP_KEY).getDouble(), 1);
        this.maxHp = hp;
        this.defence = properties.get(PropertyKey.GENERAL_DEFENSE_KEY).getDouble();
        this.critRate = properties.get(PropertyKey.GENERAL_CRIT_RATE_KEY).getDouble();
        this.critPower = properties.get(PropertyKey.GENERAL_CRIT_POWER_KEY).getDouble();
        this.effectHitRate = properties.get(PropertyKey.GENERAL_EFFECT_HIT_RATE_KEY).getDouble();
        this.effectResistRate = properties.get(PropertyKey.GENERAL_EFFECT_RESIST_RATE_KEY).getDouble();

        if (properties.get(PropertyKey.GENERAL_MOB_KEY).getBoolean()) {
            setMob(0, 3);
        }

        this.isYYS = properties.get(PropertyKey.GENERAL_YYS_KEY).getBoolean();
        if (isYYS) {
            QiLingFactory.addQiLing(properties, this);
        }
        this.isSummon = properties.get(PropertyKey.GENERAL_SUMMON_KEY).getBoolean();

        // 如果有御魂,读取御魂
        if (properties.containsKey(PropertyKey.YU_HUN_KEY)) {
            String yuHun = properties.get(PropertyKey.YU_HUN_KEY).getString();
            if (yuHun != null) {
                addAllYuHun(yuHun.split(","));
            }
        }
    }

    public void reset(BattlePane bp) {
        this.bp = bp;
        if (characterIcon != null) {
            characterIcon.reset();
        }
    }

    public double getAttack() {
        return TraversalOrderManager.getAttribute(Attribute.ATTACK, baseAttack + additionAttack, getStatuses());
    }

    public double getHp() {
        return hp;
    }

    public double getMaxHp() {
        return maxHp;
    }

    public double getSpeed() {
        return TraversalOrderManager.getAttribute(Attribute.SPEED, speed, getStatuses());
    }

    public double getDefence() {
        return TraversalOrderManager.getAttribute(Attribute.DEFENCE, defence, statuses);
    }

    public double getCritRate() {
        return TraversalOrderManager.getAttribute(Attribute.CRIT_RATE, critRate, statuses);
    }

    public double getCritPower() {
        return TraversalOrderManager.getAttribute(Attribute.CRIT_POWER, critPower, statuses);
    }

    public double getEffectHitRate() {
        return TraversalOrderManager.getAttribute(Attribute.EFFECT_HIT_RATE, effectHitRate, statuses);
    }

    public double getEffectResistRate() {
        return TraversalOrderManager.getAttribute(Attribute.EFFECT_RESIST_RATE, effectResistRate, statuses);
    }

    public double getZengShang() {
        return TraversalOrderManager.getAttribute(Attribute.ZENG_SHANG, 0, getStatuses());
    }

    public double getJianShang() {
        return TraversalOrderManager.getAttribute(Attribute.JIAN_SHANG, 0, getStatuses());
    }

    public double getYiShang() {
        // 易伤系数b=1+x(当x>0)
        // b=1/(1-x)(当x<0)
        // x=敌方易伤效果之和 - 敌方减伤效果之和。
        // 易伤效果举例：灭、丑女的咒火、缚姬放蛇；
        // 减伤效果举例：生、骁川、兵俑开嘲讽。
        // 举例：晴明贴了灭易伤30%，敌方有不知火星火结界的18%减伤效果，则x=0.12，b=1.12。
        // 鬼吞的大妖之力也是减伤，所以100%减伤只是将伤害降低到一半。

        double yiShang = TraversalOrderManager.getAttribute(Attribute.YI_SHANG, 0, getStatuses());
        double jianShang = TraversalOrderManager.getAttribute(Attribute.JIAN_SHANG, 0, getStatuses());

        double x = (yiShang - jianShang) / 100;

        if (x >= 0) {
            return 1 + x;
        } else {
            return 1 / (1 - x);
        }
    }

    public double getIgnoreDefense() {
        return TraversalOrderManager.getAttribute(Attribute.IGNORE_DEFENCE, 0, getStatuses());
    }

    public double getCritResist() {
        return TraversalOrderManager.getAttribute(Attribute.CRIT_RESIST, 0, getStatuses());
    }

    public double getXieZhanProbability() {
        return TraversalOrderManager.getProbabilityAtLeastOne(Attribute.XIE_ZHAN, getStatuses());
    }


    public boolean isMob() {
        return isMob;
    }

    public void setMob(int now, int max) {
        isMob = true;
        getStatus(MobGuiHuo.class).ifPresentOrElse(
                status -> {
                    status.setGuiHuo(now);
                    status.setMax(max);
                },
                () -> addStatus(new MobGuiHuo(this, now, max))
        );
    }

    public boolean isShiShen() {
        return !isYYS && !isSummon && !isMob;
    }

    public boolean isYYS() {
        return isYYS;
    }

    public boolean isSummon() {
        return isSummon;
    }

    public double getInitAttack() {
        return baseAttack + additionAttack;
    }

    public double getInitBaseAttack() {
        return baseAttack;
    }

    public double getInitAdditionAttack() {
        return additionAttack;
    }

    public double getInitDefense() {
        return defence;
    }

    public double getInitSpeed() {
        return speed;
    }

    public double getInitCritRate() {
        return critRate;
    }

    public double getInitCritPower() {
        return critPower;
    }

    public double getInitEffectHitRate() {
        return effectHitRate;
    }

    public double getInitEffectResistRate() {
        return effectResistRate;
    }


    protected void setInitBaseAttack(double baseAttack) {
        this.baseAttack = baseAttack;
    }

    protected void setInitAdditionAttack(double additionAttack) {
        this.additionAttack = additionAttack;
    }

    protected void setInitDefense(double defense) {
        this.defence = defense;
    }

    protected void setInitCritRate(double critRate) {
        this.critRate = critRate;
    }

    protected void setInitCritPower(double critPower) {
        this.critPower = critPower;
    }

    protected void setInitSpeed(double speed) {
        this.speed = speed;
    }

    protected void setInitEffectHitRate(double effectHitRate) {
        this.effectHitRate = effectHitRate;
    }

    protected void setInitEffectResistRate(double effectResistRate) {
        this.effectResistRate = effectResistRate;
    }


    public void setMaxHp(double num, boolean replenish) {
        if (isIgnoreChangeMaxHp()) {
            return;
        }

        forceSetMaxHp(num, replenish);
    }

    public void forceSetMaxHp(double num, boolean replenish) {
        maxHp = num;
        if (replenish) {
            hp = maxHp;
        }
    }

    public void setHp(double num) {
        if (this.hp != num) {
            this.hp = Math.min(maxHp, num);
            statusRun(Trigger.HP_CHANGE, null);
            bp.onTrigger(new EventHpChange(this));
        }
    }

    public void setHpWithoutTrigger(double num) {
        this.hp = Math.min(maxHp, num);
    }

    public void setLockSkill(int i) {
        boolean changed = false;
        Optional<Skill> os = getSkill(i);
        if (os.isPresent()) {
            Skill skill = os.get();
            if (!(skill instanceof PassiveSkill)) {
                lockSkill = i;
                changed = true;
            }
        } else if (skills.size() > i) {
            lockSkill = i;
            changed = true;
        }

        if (changed) {
            doIfCharacterIconExist(CharacterIcon::selectLockSkill);
        }
    }

    public int getLockSkill() {
        return this.lockSkill;
    }

    public void fillSkills() {
        skills.add(0, SkillAuto.INSTANCE);
        addOwnSkills();
    }

    protected abstract void addOwnSkills();

    public static double getTTA(double distance, double speed) {
        return distance / speed;
    }

    public static boolean before(double distance1, double v1, double distance2, double v2) {
        double tta1 = getTTA(distance1, v1);
        double tta2 = getTTA(distance2, v2);

        if (tta1 < tta2) {
            return true;
        }

        if (tta1 > tta2) {
            return false;
        }

        return v1 > v2;
    }

    public double getLocation() {
        return location;
    }

    // 好像只有猫川用得到这个
    public double getLocationWhenGettingOrder() {
        return location;
    }

    public void setLocation(double newLocation) {
        if (newLocation != location) {
            statusRun(Trigger.LOCATION_CHANGE, new ParamLocationChange(location, newLocation));
            this.location = newLocation;
        }
    }

    public void forceSetLocation(double newLocation) {
        this.location = newLocation;
    }

    public void beforeRound() {
        // 如果没有时之隙但是有时之辉,转化成时之隙
        // 需要在"行动前生效"的状态和维持类过回合之前判定
        Optional<StatusShiZhiHui> oStatus = getStatus(StatusShiZhiHui.class);
        if (oStatus.isPresent()) {
            StatusShiZhiHui status = oStatus.get();
            status.transform();
            statusRun(Trigger.OUT_ROUND_ACTION, null);
            return;
        }

        isInRound = true;

        statusRun(Trigger.BEFORE_ROUND, null);

        // 维持类状态过回合
        maintainedStatuses.removeIf(status -> {
            status.setDuration(status.getDuration() - 1);
            if (status.getDuration() == 0) {
                status.delete();
                return true;
            }
            return false;
        });
    }

    public void setLockSkillAndAuto() {
        if (lockSkillMap != null && lockSkillMap.containsKey(timesToAct)) {
            setLockSkill(lockSkillMap.get(timesToAct));
        }

        if (flagChangeMap != null && flagChangeMap.containsKey(timesToAct)) {
            FlagChangeInfo flagChangeInfo = flagChangeMap.get(timesToAct);
            FlagChangeInfo.FlagType flagType = flagChangeInfo.flagType;
            TeamPane teamPane = bp.situation.teamPane[team];
            Character auto = teamPane.getAuto(flagType).orElse(null);
            // 如果目标角色序号值为0且标存在,则取消.如果目标值为0且标不存在,不需要任何操作
            // 如果目标角色序号值不为0并且值有效,如果目标角色!=现有标,切换标,如果目标角色==现有标不需要任何操作
            if (flagChangeInfo.target == 0) {
                if (auto != null) {
                    teamPane.setAuto(auto, flagType);
                }
            } else {
                int targetIndex = flagChangeInfo.target - 1;
                TeamPane targetTeamPane = bp.situation
                        .teamPane[flagType == FlagChangeInfo.FlagType.GREEN ? team : 1 - team];
                List<Character> characters = targetTeamPane.getCharacters();
                if (targetIndex >= 0 && targetIndex < characters.size()) {
                    Character target = characters.get(targetIndex);
                    if (target != auto) {
                        teamPane.setAuto(target, flagType);
                    }
                }
            }


        }
    }

    public void round() {
        // TODO :一次遍历完成控制效果确认
        // 束缚:记录一次普攻,但是没有效果
        Optional<StatusBind> oSBind = getStatus(StatusBind.class);
        if (oSBind.isPresent()) {
            oSBind.get().doBind();
            return;
        }

        // 混乱:使用普攻随机攻击一个目标,包括队友
        Optional<StatusConfusion> oSConfusion = getStatus(StatusConfusion.class);
        if (oSConfusion.isPresent()) {
            oSConfusion.get().doConfusion();
            return;
        }

        if (isUnderCrowdControl()) {
            return;
        }

        if (lockSkill != 0) {
            Optional<Skill> os = getSkill(lockSkill);
            if (os.isPresent() && os.get().tryUse(bp)) {
                return;
            }
        } else {
            if (useSkillAuto()) {
                return;
            }
        }

        usePuGong();
    }

    public void afterRound() {
        isInRound = false;
        // 不知道为什么，但是在时之隙跳过回合后结算的情况下需要让大缘拉条生效
        for (Status status : statuses) {
            if (status instanceof StatusShengTian sST) {
                sST.active();
                break;
            } else if (status instanceof StatusCombined combined) {
                combined.active();
                break;
            }
        }

        statusRun(Trigger.AFTER_ACTION, null);

        // 时之隙跳过回合后结算
        Optional<StatusShiZhiXi> oStatus = getStatus(StatusShiZhiXi.class);
        if (oStatus.isPresent()) {
            removeStatus(oStatus.get());
            return;
        }

        // 非怪物或者召唤物推进鬼火条
        if (!isMob() && !isSummon) {
            bp.addGuiHuoProgress(this.team);
        }

        // 行动后触发状态
        statusRun(Trigger.AFTER_ROUND_FIRST, null);
        statusRun(Trigger.AFTER_ROUND, null);

        // 持续类状态过回合
        statuses.removeIf(status -> {
            if (status.getDurationType() == StatusDurationType.CHI_XU) {
                if (status.getDuration() == 1) {
                    status.beforeDelete();
                    return true;
                } else {
                    status.setDuration(status.getDuration() - 1);
                }
            }
            return false;
        });
        // 技能冷却
        skills.forEach(Skill::pastRound);
    }

    public boolean isInRound() {
        return isInRound;
    }

    public Optional<Skill> getSkill(int skillID) {
        for (Skill skill : skills) {
            if (skill.getSkillID() == skillID) {
                return Optional.of(skill);
            }
        }
        if (skills.size() > skillID) {
            return Optional.of(skills.get(skillID));
        }
        return Optional.empty();
    }

    public Optional<Skill1PuGongBase> getPuGong() {
        if (skills.size() < 2) {
            return Optional.empty();
        }
        if (skills.get(1) instanceof Skill1PuGongBase s1) {
            return Optional.of(s1);
        }
        return Optional.empty();
    }

    public void usePuGong() {
        getPuGong().ifPresent(s1 -> s1.use(bp));
    }

    public boolean canXieZhan(Skill skill) {
        Optional<Skill1PuGongBase> puGong = getPuGong();
        return puGong.map(s1 -> s1.canXieZhan(skill)).orElse(false);
    }

    public void xieZhan(Skill skill, Character target) {
        getPuGong().ifPresent(s1 -> s1.xieZhan(skill, target));
    }

    public void removeSkill(int skillID) {
        Optional<Skill> os = getSkill(skillID);
        os.ifPresent(skill -> {
            skills.remove(skill);
            if (skillID == lockSkill) {
                lockSkill = 0;
                doIfCharacterIconExist(CharacterIcon::selectLockSkill);
            }
        });
    }

    public boolean tryUseSkill(int skillID) {
        return getSkill(skillID)
                .map(skill -> skill.tryUse(bp))
                .orElse(false);
    }

    public ObservableList<Skill> getReadOnlySkillList() {
        return skills.getObservableList();
    }

    public void addSkill(Skill skill, boolean needOrder) {
        if (needOrder) {
            int i = 0;
            Iterator<Skill> iterator = skills.iterator();
            while (iterator.hasNext() && iterator.next().getSkillID() < skill.getSkillID()) {
                i++;
            }
            skills.add(i, skill);
        } else {
            skills.add(skill);
        }

        if (skill instanceof PassiveSkill ps) {
            ps.enable();
        }
    }

    public void addSkill(Skill skill) {
        addSkill(skill, false);
    }

    protected boolean useSkillAuto() {
        return false;
    }

    public void checkShield(AttackInfo attackInfo) {
        TraceableNumber traceableNumber = attackInfo.getTraceableNumber();
        // 遍历状态找护盾
        Iterator<Status> iterator = getStatuses().iterator();
        while (traceableNumber.getNumber() > 0 && iterator.hasNext()) {
            if (iterator.next() instanceof StatusShield ss) {
                if (ss.handle(attackInfo)) {
                    iterator.remove();
                } else {
                    break;
                }
            }
        }
    }

    public void beHurt(AttackInfo attackInfo) {
        statusRun(Trigger.BEFORE_ATTACK, new ParamAttackInfo(attackInfo));

        if (attackInfo.isCancel()) {
            return;
        }

        double damage = attackInfo.getTraceableNumber().getNumber();
        if (damage > 0) {
            // 如果剩余血量比伤害小,进入死亡判定
            if (getHp() <= damage) {
                attackInfo.getTraceableNumber().addTrace("\t(击杀)");
                beforeDie(attackInfo, damage - getHp());
            } else {
                // 受到攻击
                setHp(getHp() - damage);
            }
        }
    }

    /**
     * 失去:区别于受到伤害,失去生命不会暴击,无法被护盾吸收,不触发受到伤害相关效果
     */
    public void lostHP(double num) {
        // 如果后来给失去生命默认解除睡眠状态,修改神蛇的堕化
        setHp(getHp() - num);
        if (hp <= 0) {
            die();
        }
    }

    public void beHeal(InteractiveInfo interactiveInfo) {
        setHp(getHp() + interactiveInfo.getTraceableNumber().getNumber());
    }

    /**
     * 恢复：区别于治疗，不受减疗效果影响，不触发治疗相关效果，不会暴击
     */
    public void recovery(double num) {
        setHp(getHp() + num);
    }

    public void dispelAllDebuff() {
        getStatuses().removeIf(status -> {
            if (status.statusType == StatusType.DEBUFF && status.statusForm == StatusForm.ZHUANG_TAI) {
                status.beforeDelete();
                return true;
            }
            return false;
        });
    }

    public void dispelDeBuff(int count) {
        List<Status> debuffs = new ArrayList<>();
        for (Status status : statuses) {
            if (status.statusType == StatusType.DEBUFF
                    && status.statusForm == StatusForm.ZHUANG_TAI
                    && status instanceof Displayable) {
                debuffs.add(status);
            }
        }

        List<Status> tobeDelete = RateController
                .choose(name + "驱散减益状态", debuffs, Status::toString, bp.calc, count);

        for (Status status : tobeDelete) {
            status.delete();
        }
    }

    public void dispelDeBuffPrioritizeCrowdControl(int count) {
        List<Status> debuffs = new ArrayList<>();
        List<Status> crowdControl = new ArrayList<>();

        for (Status status : statuses) {
            if (status.statusType == StatusType.DEBUFF && status.statusForm == StatusForm.ZHUANG_TAI) {
                if (status instanceof CrowdControl) {
                    crowdControl.add(status);
                } else {
                    debuffs.add(status);
                }
            }
        }

        int remainingCount = count;
        if (!crowdControl.isEmpty()) {
            if (crowdControl.size() <= count) {
                crowdControl.forEach(Status::delete);
                if (crowdControl.size() == count) {
                    return;
                } else {
                    remainingCount = count - crowdControl.size();
                }
            } else {
                for (Status status : RateController
                        .choose(name + "驱散控制效果", crowdControl, Status::toString, bp.calc, count)) {
                    status.delete();
                }
                return;
            }
        }

        List<Status> tobeDelete = RateController
                .choose(name + "驱散减益状态", debuffs, Status::toString, bp.calc, remainingCount);
        for (Status status : tobeDelete) {
            status.delete();
        }
    }

    public void dispelBuff(int count) {
        List<Status> buffs = new ArrayList<>();
        for (Status status : statuses) {
            if (status.statusType == StatusType.BUFF
                    && status.statusForm == StatusForm.ZHUANG_TAI) {
                buffs.add(status);
            }
        }

        List<Status> tobeDelete = RateController
                .choose(name + "驱散增益状态", buffs, Status::toString, bp.calc, count);

        for (Status status : tobeDelete) {
            status.delete();
        }
    }

    public void removeStatusIf(Predicate<Status> filter) {
        final Iterator<Status> iterator = getStatuses().iterator();
        while (iterator.hasNext()) {
            Status next = iterator.next();
            if (filter.test(next)) {
                next.beforeDelete();
                iterator.remove();
            }
        }
    }

    public void removeAllCrowControl() {
        getStatuses().removeIf(status -> {
            if (status instanceof CrowdControl) {
                status.beforeDelete();
                return true;
            }
            return false;
        });
    }

    public void removeAllDeBuff() {
        getStatuses().removeIf(status -> {
            if (status.statusType == StatusType.DEBUFF) {
                status.beforeDelete();
                return true;
            }
            return false;
        });
    }

    public CharacterIcon getCharacterIcon() {
        if (characterIcon == null) {
            characterIcon = new CharacterIcon(this);
            characterIcon.reset();
        }
        return characterIcon;
    }

    public void doIfCharacterIconExist(Consumer<CharacterIcon> action) {
        if (characterIcon != null) {
            action.accept(characterIcon);
        }
    }

    protected EventHandler<MouseEvent> getEventHandler() {
        return null;
    }

    protected InfoDisplay getInfoDisplay() {
        return null;
    }

    public <T extends Status> Optional<T> getStatus(Class<T> clazz) {
        for (Status status : statuses) {
            if (clazz.isInstance(status)) {
                return Optional.of(clazz.cast(status));
            }
        }
        return Optional.empty();
    }

    public boolean isHaveStatus(Class<? extends Status> clazz) {
        return getStatus(clazz).isPresent();
    }

    public <T extends Status> Optional<T> addStatus(T newStatus) {
        // 拒绝添加所有状态:堕落之剑和青女房
        for (Status status : getStatuses()) {
            if (status instanceof RejectAllStatuses ||
                    (newStatus.statusType == StatusType.DEBUFF
                            && newStatus.statusForm == StatusForm.ZHUANG_TAI
                            && status instanceof IgnoreDebuff id && id.ignoreDebuffEffective()
                    )
            ) {
                return Optional.empty();
            }
        }

        statuses.add(newStatus);
        return Optional.of(newStatus);
    }

    public boolean isIgnoreChangeMaxHp() {
        for (Status status : getStatuses()) {
            if (status instanceof IgnoreChangeMaxHp) {
                return true;
            }
        }
        return false;
    }

    public void addMaintainedStatus(Status status) {
        maintainedStatuses.add(status);
    }

    public void removeMaintainedStatus(Status status) {
        maintainedStatuses.remove(status);
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public <T extends Status> void removeStatus(Class<T> clazz) {
        for (Status status : statuses) {
            if (clazz.isInstance(status)) {
                status.beforeDelete();
                statuses.remove(status);
                return;
            }
        }

    }

    public void removeStatus(Status status) {
        statuses.remove(status);
    }

    // 区别:getInteractive会将interactive的owner设为该角色且不再改变
    // doInteractive会在完成操作后将interactive的owner归位
    public Interactive getInteractive() {
        return bp.getInteractive(this);
    }

    public void doInteractive(Consumer<Interactive> action) {
        bp.doInteractive(this, action);
    }

    public void addYuHun(YuHun yuHun) {
        yuHunSet.add(yuHun);
        if (yuHun instanceof YuHunSealResponse sr) {
            sr.enable();
        }
    }

    public void removeYuHun(YuHun yuHun) {
        if (!yuHunSet.contains(yuHun)) {
            return;
        }
        if (yuHun instanceof YuHunSealResponse sr) {
            sr.disable();
        }
        yuHunSet.remove(yuHun);
    }

    public void addAllYuHun(String[] names) {
        for (String s : names) {
            YuHunFactory.getYuHun(s, this, true).ifPresent(this::addYuHun);
        }
    }

    public void forEachYuHun(Consumer<YuHun> action) {
        if (isYuHunSeal()) {
            return;
        }
        yuHunSet.forEach(action);
    }

    public <T> Optional<T> getYuHun(Class<T> tClass) {
        if (isYuHunSeal()) {
            return Optional.empty();
        }
        return yuHunSet.stream().filter(tClass::isInstance).map(tClass::cast).findFirst();
    }

    public boolean isYuHunSeal() {
        return false;
    }

    public <T extends YuHun> YuHun removeYuHun(Class<T> tClass) {
        Set<YuHun> yuHunSet = getYuHunSet();
        for (YuHun yuHun : yuHunSet) {
            if (tClass.isInstance(yuHun)) {
                if (yuHun instanceof YuHunSealResponse sr) {
                    sr.disable();
                }
                yuHunSet.remove(yuHun);
                return yuHun;
            }
        }
        return null;
    }

    public LinkedHashSet<YuHun> getYuHunSet() {
        return yuHunSet;
    }

    public void beforeDie(InteractiveInfo interactiveInfo, double excessDamage) {
        for (Status status : getStatuses()) {
            if (status instanceof PreventDie pd && pd.preventDieEffective()) {
                pd.preventDie(excessDamage);
                interactiveInfo.getTraceableNumber().addTrace("(" + pd.getName() + "免死生效)");
                interactiveInfo.setCancel(true);
                return;
            }
        }

        die();
    }

    public void die() {
        alive = false;
        bp.removeCharacter(this);

        statusRun(Trigger.DIE, null);
        // 通过老头死亡时可以叠一层伤魂鸟判断，应该先触发死亡，再执行
        dieHandle();
    }

    protected void dieHandle() {
    }

    /**
     * 该方法仅用于确定在battlePane中算作需要控制的回合.
     * 如果需要确认是否处于控制状态: {@link Character#isUnderCrowdControl()}
     */
    public boolean isUncontrollable() {
        // 如果被控了,无法行动
        return isUnderCrowdControl();
    }

    public boolean isUnderCrowdControl() {
        for (Status status : getStatuses()) {
            if (status instanceof CrowdControl) {
                return true;
            }
        }
        return false;
    }

    public void statusRun(Trigger trigger, TriggerParam param) {
        List<Status> copy = new ArrayList<>(statuses);
        for (Status status : copy) {
            if (status instanceof StatusRunnable r && r.runnable(trigger)) {
                if (r.run(trigger, bp, param)) {
//                    statuses.remove(status);
                    status.delete();
                }
            }
        }
    }

    public void refreshSkills() {
        for (Skill skill : skills) {
            skill.refresh();
        }
    }

    public Map<Integer, Integer> getLockSkillMap() {
        return lockSkillMap;
    }

    public Map<Integer, FlagChangeInfo> getFlagChangeMap() {
        return flagChangeMap;
    }
}
