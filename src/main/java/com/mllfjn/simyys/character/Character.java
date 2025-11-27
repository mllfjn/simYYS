package com.mllfjn.simyys.character;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.skill.SkillAuto;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunFactory;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;
import com.mllfjn.simyys.character.yys.QiLingFactory;
import com.mllfjn.simyys.interactive.TraceableNumber;
import com.mllfjn.simyys.state.*;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.state.determinant.*;
import com.mllfjn.simyys.interactive.Info;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.character.propertygetter.PropertyCheck;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;
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

    // 自身状态
    private final List<State> states = new ArrayList<>();
    // 维持的状态
    private final List<State> maintainedStates = new ArrayList<>();
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
        this.lockSKillMap = propertiesHolder.lockSKill;
        PropertiesMap properties = propertiesHolder.map;
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
            addState(new MobGuiHuo(this));
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
        return AttributeCounter.getGeneralAttribute(Attribute.ATTACK, baseAttack + additionAttack, getStates());
    }
    public double getHp() {
        return hp;
    }
    public double getMaxHp() {
        return maxHp;
    }
    public double getSpeed() {
        return AttributeCounter.getGeneralAttribute(Attribute.SPEED, speed, getStates());
    }
    public double getDefense() {
        return AttributeCounter.getGeneralAttribute(Attribute.DEFENCE, defense, states);
    }
    public double getCritRate() {
        return AttributeCounter.getGeneralAttribute(Attribute.CRIT_RATE, critRate, states);
    }
    public double getCritPower() {
        return AttributeCounter.getGeneralAttribute(Attribute.CRIT_POWER, critPower, states);
    }
    public double getEffectHitRate() {
        return AttributeCounter.getGeneralAttribute(Attribute.EFFECT_HIT_RATE, effectHitRate, states);
    }
    public double getEffectResistRate() {
        return AttributeCounter.getGeneralAttribute(Attribute.EFFECT_RESIST_RATE, effectResistRate, states);
    }
    public double getZengShang() {
        return AttributeCounter.getGeneralAttribute(Attribute.ZENG_SHANG, 0, getStates());
    }
    public double getJianShang() {
        return AttributeCounter.getGeneralAttribute(Attribute.JIAN_SHANG, 0, getStates());
    }
    public double getIgnoreDefense() {
        return AttributeCounter.getGeneralAttribute(Attribute.IGNORE_DEFENCE, 0, getStates());
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
        stateRun(Trigger.BEFORE_ROUND);

        // 维持类状态过回合
        maintainedStates.removeIf(state -> {
            state.setDuration(state.getDuration() - 1);
            if (state.getDuration() == 0) {
                state.delete();
                return true;
            }
            return false;
        });

        if (lockSKillMap != null && lockSKillMap.containsKey(timesToAct)) {
            setLockSkill(lockSKillMap.get(timesToAct));
        }
    }
    public void round(boolean skip) {
        if (!skip) {
            act();
        }
        // 以下是回合后事件

        // 非怪物推进鬼火条
        if (!isMob()) {
            bp.addProgress(this.team);
        }
        // 行动后触发状态
        stateRun(Trigger.AFTER_ROUND_FIRST);
        stateRun(Trigger.AFTER_ROUND);

        // 持续类状态过回合
        states.removeIf(state -> {
            if (state.getSettleType() == StateSettleType.CHI_XU) {
                state.setDuration(state.getDuration() - 1);
                if (state.getDuration() == 0) {
                    state.beforeDelete();
                    return true;
                }
            }
            return false;
        });
        // 技能冷却
        skills.forEach(Skill::pastRound);
    }
    private void act() {
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
        Iterator<State> iterator = getStates().iterator();
        while (traceableNumber.getNumber() > 0 && iterator.hasNext()) {
            if (iterator.next() instanceof StateShield ss) {
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
        getStates().removeIf(state -> state.stateType == StateType.BUFF && state.stateForm == StateForm.ZHUANG_TAI);
    }
    public void dispelAllDebuff() {
        getStates().removeIf(state -> state.stateType == StateType.DEBUFF && state.stateForm == StateForm.ZHUANG_TAI);
    }
    public void dispelDeBuff(int count) {
        // TODO 驱散指定数量的减益状态
    }
    public void removeAllCrowControl() {
        getStates().removeIf(state -> state instanceof CrowdControl);
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
    public <T extends State> Optional<T> getState(Class<T> clazz) {
        for (State state : states) {
            if (clazz.isInstance(state)) {
                return Optional.of(clazz.cast(state));
            }
        }
        return Optional.empty();
    }
    public boolean isHaveState(Class<? extends State> clazz) {
        return getState(clazz).isPresent();
    }
    public <T extends State> Optional<T> addState(T newState) {
        if (newState.stateType == StateType.DEBUFF && isIgnoreDebuff()) {
            return Optional.empty();
        }

        states.add(newState);
        return Optional.of(newState);
    }
    public boolean isIgnoreDebuff() {
        for (State state : getStates()) {
            if (state instanceof IgnoreDebuff) {
                return true;
            }
        }
        return false;
    }
    public boolean isIgnoreChangeMaxHp() {
        for (State state : getStates()) {
            if (state instanceof IgnoreChangeMaxHp) {
                return true;
            }
        }
        return false;
    }
    public void addMaintainedState(State state) {
        maintainedStates.add(state);
    }

    public void removeMaintainedState(State state) {
        maintainedStates.remove(state);
    }


    public List<State> getStates() {
        return states;
    }
    public <T extends State> void removeState(Class<T> clazz) {
        for (State state : states) {
            if (clazz.isInstance(state)) {
                state.beforeDelete();
                states.remove(state);
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
        for (State state : getStates()) {
            if (state instanceof PreventDie pd && pd.effective()) {
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

        // 通过老头死亡时可以叠一层伤魂鸟判断，应该先触发死亡，再执行
        dieHandle();
    }

    protected void dieHandle() {}
    public boolean controllable() {
        // 如果被控了,无法行动
        for (State state : getStates()) {
            if (state instanceof CrowdControl) {
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
    private void stateRun(Trigger trigger) {
        List<State> copy = new ArrayList<>(states);
        for (State state : copy) {
            if (state instanceof Runnable r && r.runnable(trigger)) {
                if (r.run(trigger, bp)) {
                    states.remove(state);
                }
            }
        }
    }
}
