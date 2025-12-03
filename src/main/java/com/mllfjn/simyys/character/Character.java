package com.mllfjn.simyys.character;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.TeamPane;
import com.mllfjn.simyys.character.propertygetter.*;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.skill.SkillAuto;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.Runnable;
import com.mllfjn.simyys.character.status.determinant.IgnoreChangeMaxHp;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;
import com.mllfjn.simyys.character.status.determinant.PreventDie;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunFactory;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;
import com.mllfjn.simyys.character.yys.QiLingFactory;
import com.mllfjn.simyys.interactive.TraceableNumber;
import com.mllfjn.simyys.interactive.Info;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.guihuo.MobGuiHuo;
import com.mllfjn.simyys.trigger.*;
import com.mllfjn.simyys.collections.SerializableObservableList;
import com.mllfjn.simyys.trigger.battleevent.EventHpChange;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

public abstract class Character implements Serializable{
    public String name;
    public int team;
    public int timesToAct;
    private double location;
    private int lockSkill;
    public boolean alive = true;

    private double maxHp;
    private double hp;
    private double baseAttack;
    private double additionAttack;
    private double defense;
    private double critRate;
    private double critPower;
    private double effectHitRate;
    private double effectResistRate;
    private double speed;

    private boolean isMob;
    private boolean isYYS;
    protected boolean isSummon;

    protected final SerializableObservableList<Skill> skills = new SerializableObservableList<>();
    private Map<Integer, Integer> lockSKillMap;
    private Map<Integer, FlagChangeInfo> flagChangeMap;

    // 自身状态
    private final List<Status> statuses = new ArrayList<>();
    // 维持的状态
    private final List<Status> maintainedStatuses = new ArrayList<>();
    // 御魂列表
    private final LinkedHashSet<YuHun> yuHunList = new LinkedHashSet<>();

    private transient CharacterIcon characterIcon;
    public transient BattlePane bp;
    public PropertiesMap getProperties() {
        PropertiesMap map = new PropertiesMap();
        map.put(PropertyKey.GENERAL_SPEED_KEY, new PropertyInput());
        map.put(PropertyKey.GENERAL_BASE_ATTACK_KEY, new PropertyInput().setValue(getDefaultBaseAttack()));
        map.put(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY, new PropertyInput());
        map.put(PropertyKey.GENERAL_HP_KEY, new PropertyInput());
        map.put(PropertyKey.GENERAL_DEFENSE_KEY, new PropertyInput());
        map.put(PropertyKey.GENERAL_CRIT_RATE_KEY, new PropertyInput());
        map.put(PropertyKey.GENERAL_CRIT_POWER_KEY, new PropertyInput());
        map.put(PropertyKey.GENERAL_EFFECT_HIT_RATE_KEY, new PropertyInput());
        map.put(PropertyKey.GENERAL_EFFECT_RESIST_RATE_KEY, new PropertyInput());

        map.put(PropertyKey.GENERAL_TEAM_KEY, new PropertyCheck());
        map.put(PropertyKey.GENERAL_MOB_KEY, new PropertyCheck());
        map.put(PropertyKey.GENERAL_YYS_KEY, new PropertyCheck());
        map.put(PropertyKey.GENERAL_SUMMON_KEY, new PropertyCheck());

        return map;
    }
    protected abstract String getDefaultBaseAttack();

    public String getName() {
        return name;
    }
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        setBattlePane(bp);
        this.lockSKillMap = propertiesHolder.lockSkillMap;
        this.flagChangeMap = propertiesHolder.flagChangeMap;
        PropertiesMap properties = propertiesHolder.propertiesMap;
        this.name = propertiesHolder.name;
        this.speed = properties.get(PropertyKey.GENERAL_SPEED_KEY).getDouble();
        this.baseAttack = properties.get(PropertyKey.GENERAL_BASE_ATTACK_KEY).getDouble();
        this.additionAttack = properties.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY).getDouble();
        this.team = properties.get(PropertyKey.GENERAL_TEAM_KEY).getBoolean() ? 1 : 0;
        this.hp = properties.get(PropertyKey.GENERAL_HP_KEY).getDouble();
        this.maxHp = hp;
        this.defense = properties.get(PropertyKey.GENERAL_DEFENSE_KEY).getDouble();
        this.critRate = properties.get(PropertyKey.GENERAL_CRIT_RATE_KEY).getDouble();
        this.critPower = properties.get(PropertyKey.GENERAL_CRIT_POWER_KEY).getDouble();
        this.effectHitRate = properties.get(PropertyKey.GENERAL_EFFECT_HIT_RATE_KEY).getDouble();
        this.effectResistRate = properties.get(PropertyKey.GENERAL_EFFECT_RESIST_RATE_KEY).getDouble();

        if (properties.get(PropertyKey.GENERAL_MOB_KEY).getBoolean()) {
            this.isMob = true;
            addStatus(new MobGuiHuo(this));
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
    public void setBattlePane(BattlePane bp) {
        this.bp = bp;
    }

    public double getAttack() {
        return AttributeCounter.getGeneralAttribute(Attribute.ATTACK, baseAttack + additionAttack, getStatuses());
    }
    public double getHp() {
        return hp;
    }
    public double getMaxHp() {
        return maxHp;
    }
    public double getSpeed() {
        return AttributeCounter.getGeneralAttribute(Attribute.SPEED, speed, getStatuses());
    }
    public double getDefense() {
        return AttributeCounter.getGeneralAttribute(Attribute.DEFENCE, defense, statuses);
    }
    public double getCritRate() {
        return AttributeCounter.getGeneralAttribute(Attribute.CRIT_RATE, critRate, statuses);
    }
    public double getCritPower() {
        return AttributeCounter.getGeneralAttribute(Attribute.CRIT_POWER, critPower, statuses);
    }
    public double getEffectHitRate() {
        return AttributeCounter.getGeneralAttribute(Attribute.EFFECT_HIT_RATE, effectHitRate, statuses);
    }
    public double getEffectResistRate() {
        return AttributeCounter.getGeneralAttribute(Attribute.EFFECT_RESIST_RATE, effectResistRate, statuses);
    }
    public double getZengShang() {
        return AttributeCounter.getGeneralAttribute(Attribute.ZENG_SHANG, 0, getStatuses());
    }
    public double getJianShang() {
        return AttributeCounter.getGeneralAttribute(Attribute.JIAN_SHANG, 0, getStatuses());
    }
    public double getIgnoreDefense() {
        return AttributeCounter.getGeneralAttribute(Attribute.IGNORE_DEFENCE, 0, getStatuses());
    }

    public boolean isMob() {
        return isMob;
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
        return defense;
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
        this.defense = defense;
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

        maxHp = num;
        if (replenish) {
            setHp(maxHp);
        }
    }
    public void setHp(double num) {
        this.hp = Math.min(maxHp, num);
        bp.onTrigger(new EventHpChange(this));
    }

    public void setLockSkill(int i) {
        getSkill(i).ifPresent(skill -> lockSkill = i);
        if (characterIcon != null) {
            characterIcon.selectLockSkill();
        }
    }
    public int getLockSkill() {
        return this.lockSkill;
    }
    public void addSkills() {
        skills.add(SkillAuto.INSTANCE);
        addOwnSkills();
    }
    protected abstract void addOwnSkills();
    public static double getTTA(double distance, double speed) {
        return distance / speed;
    }
    public double getTTA() {
        return getTTA(100.0 - this.getLocation(), this.getSpeed());
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
    public boolean before(Character character) {
        return before(100.0 - this.getLocation(), this.getSpeed(), 100 - character.getLocation(), character.getSpeed());
    }
    public double getLocation() {
        return location;
    }
    public void setLocation(double newLocation) {
        this.location = newLocation;
    }
    public void beforeRound() {
        statusRun(Trigger.BEFORE_ROUND);

        // 维持类状态过回合
        maintainedStatuses.removeIf(status -> {
            status.setDuration(status.getDuration() - 1);
            if (status.getDuration() == 0) {
                status.delete();
                return true;
            }
            return false;
        });

        if (lockSKillMap != null && lockSKillMap.containsKey(timesToAct)) {
            setLockSkill(lockSKillMap.get(timesToAct));
        }

        if (flagChangeMap != null && flagChangeMap.containsKey(timesToAct)) {
            FlagChangeInfo flagChangeInfo = flagChangeMap.get(timesToAct);
            int targetTeam = flagChangeInfo.flagType == FlagChangeInfo.FlagType.GREEN ? team : 1 - team;

            TeamPane targetTeamPane = bp.situation.teamPane[targetTeam];
            Character auto = targetTeamPane.getAuto().orElse(null);
            // 如果目标角色序号值为0且标存在,则取消.如果目标值为0且标不存在,不需要任何操作
            // 如果目标角色序号值不为0并且值有效,如果目标角色!=现有标,切换标,如果目标角色==现有标不需要任何操作
            if (flagChangeInfo.target == 0) {
                if (auto != null) {
                    targetTeamPane.setAuto(auto);
                }
            } else {
                int targetIndex = flagChangeInfo.target - 1;
                List<Character> characters = targetTeamPane.getCharacters();
                if (targetIndex > 0 && targetIndex < characters.size()) {
                    Character target = characters.get(targetIndex);
                    if (target != auto) {
                        targetTeamPane.setAuto(target);
                    }
                }
            }


        }
    }
    public void round() {
        if (lockSkill != 0) {
            Optional<Skill> os = getSkill(lockSkill);
            if (os.isPresent() && os.get().tryUse(bp)) {
                return;
            }
        }
        if (!useSkillAuto()) {
            getSkill(1).ifPresent(skill -> skill.use(bp));
        }
    }
    public void afterRound() {
        // 非怪物或者召唤物推进鬼火条
        if (!isMob() && !isSummon) {
            bp.addProgress(this.team);
        }
        // 行动后触发状态
        statusRun(Trigger.AFTER_ROUND_FIRST);
        statusRun(Trigger.AFTER_ROUND);

        // 持续类状态过回合
        statuses.removeIf(status -> {
            if (status.getDurationType() == StatusDurationType.CHI_XU) {
                status.setDuration(status.getDuration() - 1);
                if (status.getDuration() == 0) {
                    status.beforeDelete();
                    return true;
                }
            }
            return false;
        });
        // 技能冷却
        skills.forEach(Skill::pastRound);
    }
    public Optional<Skill> getSkill(int skillID) {
        for (Skill skill : skills) {
            if (skill.getSkillID() == skillID) {
                return Optional.of(skill);
            }
        }
        return Optional.empty();
    }
    public void removeSkill(int skillID) {
        Optional<Skill> os = getSkill(skillID);
        os.ifPresent(skill -> {
            if (characterIcon != null) {
                characterIcon.startChangeSkill();
            }
            skills.remove(skill);
            if (skillID == lockSkill) {
                lockSkill = 0;
            }
            if (characterIcon != null) {
                characterIcon.endChangeSkill();
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
    public void addSkill(Skill skill) {
        int i = 0;
        Iterator<Skill> iterator = skills.iterator();
        while (iterator.hasNext() && iterator.next().getSkillID() < skill.getSkillID()) {
            i++;
        }

        if (characterIcon != null) {
            characterIcon.startChangeSkill();
        }

        skills.add(i, skill);

        if (characterIcon != null) {
            characterIcon.endChangeSkill();
        }
    }
    protected boolean useSkillAuto() {
        return false;
    }

    public void beHurt(Info info) {
        TraceableNumber traceableNumber = info.getTraceableNumber();
        // 遍历状态找护盾
        Iterator<Status> iterator = getStatuses().iterator();
        while (traceableNumber.getNumber() > 0 && iterator.hasNext()) {
            if (iterator.next() instanceof StatusShield ss) {
                if (ss.handle(info)) {
                    iterator.remove();
                } else {
                    break;
                }
            }
        }
        double damage = traceableNumber.getNumber();
        // 如果护盾没能抵消完伤害
        if (damage > 0) {
            // 如果剩余血量比伤害小,进入死亡判定
            if (getHp() <= damage) {
                info.getTraceableNumber().addTrace("\t(击杀)");
                beforeDie(info);
            } else {
                setHp(getHp() - damage);
                statusRun(Trigger.AFTER_ATTACK);
            }
        }
    }

    /**
     * 失去:区别于受到伤害,失去生命不会暴击,无法被护盾吸收,不触发受到伤害相关效果
     */
    public void lostHP(double num) {
        // 如果后来给失去生命默认解除睡眠状态,修改神蛇的堕化
        hp -= num;
        if (hp <= 0) {
            die();
        }
    }

    public void beHeal(Info info) {
        setHp(getHp() + info.getTraceableNumber().getNumber());
    }

    /**
     *  恢复：区别于治疗，不受减疗效果影响，不触发治疗相关效果，不会暴击
     */
    public void recovery(double num) {
        setHp(getHp() + num);
    }
    public void dispelAllBuff() {
        getStatuses().removeIf(status -> status.statusType == StatusType.BUFF && status.statusForm == StatusForm.ZHUANG_TAI);
    }
    public void dispelAllDebuff() {
        getStatuses().removeIf(status -> status.statusType == StatusType.DEBUFF && status.statusForm == StatusForm.ZHUANG_TAI);
    }
    public void dispelDeBuff(int count) {
        // TODO 驱散指定数量的减益状态
    }
    public void removeAllCrowControl() {
        getStatuses().removeIf(status -> status instanceof CrowdControl);
    }

    public CharacterIcon getCharacterIcon() {
        if (characterIcon == null) {
            characterIcon = new CharacterIcon(this);
            characterIcon.setEventHandler(getEventHandler());
        }
        return characterIcon;
    }
    protected EventHandler<MouseEvent> getEventHandler() {
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
        if (newStatus.statusType == StatusType.DEBUFF && isIgnoreDebuff()) {
            return Optional.empty();
        }

        statuses.add(newStatus);
        return Optional.of(newStatus);
    }
    public boolean isIgnoreDebuff() {
        for (Status status : getStatuses()) {
            if (status instanceof IgnoreDebuff) {
                return true;
            }
        }
        return false;
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

    public Interactive getInteractive() {
        return bp.getInteractive(this);
    }

    public void doInteractive(Consumer<Interactive> action) {
        bp.doInteractive(this, action);
    }

    public void addYuHun(YuHun yuHun) {
        yuHunList.add(yuHun);
        if (yuHun instanceof YuHunSealResponse sr) {
            sr.enable();
        }
    }

    public void addAllYuHun(String[] names) {
        for (String s : names) {
            YuHunFactory.getYuHun(s, this).ifPresent(this::addYuHun);
        }
    }

    public void forEachYuHun(Consumer<YuHun> action) {
        if (isYuHunSeal()) {
            return;
        }
        yuHunList.forEach(action);
    }

    private boolean isYuHunSeal() {
        return false;
    }

    public <T extends YuHun> void removeYuHun(Class<T> tClass) {
        Set<YuHun> yuHunSet = getYuHunSet();
        for (YuHun yuHun : yuHunSet) {
            if (tClass.isInstance(yuHun)) {
                yuHunSet.remove(yuHun);
                if (yuHun instanceof YuHunSealResponse sr) {
                    sr.disable();
                }
                return;
            }
        }
    }

    public LinkedHashSet<YuHun> getYuHunSet() {
        return yuHunList;
    }
    public void beforeDie(Info info) {
        for (Status status : getStatuses()) {
            if (status instanceof PreventDie pd && pd.effective()) {
                pd.preventDie();
                info.getTraceableNumber().addTrace("(" + pd.getName() + "免死生效)");
                return;
            }
        }

        die();
    }
    public void die() {
        alive = false;
        bp.removeCharacter(this);

        for (Status status : getStatuses()) {
            if (status instanceof ActionWhenDie awd) {
                awd.action(bp);
            }
        }
        // 通过老头死亡时可以叠一层伤魂鸟判断，应该先触发死亡，再执行
        dieHandle();
    }

    protected void dieHandle() {}
    public boolean controllable() {
        // 如果被控了,无法行动
        for (Status status : getStatuses()) {
            if (status instanceof CrowdControl) {
                return false;
            }
        }
        // 如果技能全都用不了,无法行动
        boolean canUse = false;
        for (Skill skill : skills) {
            if (skill instanceof SkillAuto) {
                continue;
            }
            if (skill.canUse(bp)) {
                canUse = true;
                break;
            }
        }
        return canUse;
    }
    private void statusRun(Trigger trigger) {
        List<Status> copy = new ArrayList<>(statuses);
        for (Status status : copy) {
            if (status instanceof Runnable r && r.runnable(trigger)) {
                if (r.run(trigger, bp)) {
                    statuses.remove(status);
                }
            }
        }
    }
}
