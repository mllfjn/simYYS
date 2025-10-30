package com.mllfjn.simyys.character;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.skill.SkillAuto;
import com.mllfjn.simyys.customnode.TextFlowLog;
import com.mllfjn.simyys.state.*;
import com.mllfjn.simyys.state.determinant.*;
import com.mllfjn.simyys.interactive.Info;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.character.propertygetter.PropertyCheck;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;
import com.mllfjn.simyys.guihuo.MobGuiHuo;
import com.mllfjn.simyys.trigger.Trigger;
import com.mllfjn.simyys.trigger.TriggerSession;
import com.mllfjn.simyys.collections.SerializableObservableList;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.*;

public abstract class Character implements Serializable{
    public String name;
    public transient CharacterIcon characterIcon;
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
    private boolean isSummon;

    protected final SerializableObservableList<Skill> skills = new SerializableObservableList<>();

    private final List<State> states = new ArrayList<>();
    private final List<State> maintainedStates = new ArrayList<>();
    private transient Interactive interactive;
    public PropertiesMap getProperties() {
        PropertiesMap map = new PropertiesMap();
        map.put(PropertyKey.GENERAL_SPEED_KEY, new PropertyInput());
        map.put(PropertyKey.GENERAL_BASE_ATTACK_KEY, new PropertyInput());
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
    public void init(PropertiesMap properties, BattlePane bp) {
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
        this.isSummon = properties.get(PropertyKey.GENERAL_SUMMON_KEY).getBoolean();

        addState(new AttackRecorder(this));
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


    public void setMaxHp(double num) {
        if (isIgnoreChangeMaxHp()) {
            return;
        }

        maxHp = num;
    }
    public void setHp(double num) {
        this.hp = Math.min(maxHp, num);
    }

    public void setLockSkill(int i) {
        this.lockSkill = i;
    }
    public int getLockSkill() {
        return this.lockSkill;
    }


    public void addSkills() {
        skills.add(SkillAuto.INSTANCE);
    }
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
    public void increaseLocation(BattlePane bp, Character from, double increase) {
        // 免疫行动条提升效果
        for (State state : states) {
            if (state instanceof IgnoreActionIncrease fi && fi.effective(from)) {
                return;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\t").append(this.name).append("行动提前").append(increase);
        if (location + increase > 100) {
            double real = 100 - location;
            sb.append("(实际提前").append((int)real).append("%,已到达行动条末端)");
        }
        sb.append("%\n");

        this.location = Math.min(100, location + increase);
        bp.log.addText(sb.toString(), TextFlowLog.TextType.INCREASE, TextFlowLog.TextColor.NORMAL, TextFlowLog.FontSize.NORMAL);
    }
    public void decreaseLocation(BattlePane bp, Character from, double decrease) {
        for (State state : states) {
            if (state instanceof IgnoreActionDecrease) {
                return;
            }
        }
        this.location = Math.max(0, location - decrease);
        bp.log.addText("\t" + this.name + "行动推后" + decrease + "%\n", TextFlowLog.TextType.INCREASE, TextFlowLog.TextColor.NORMAL, TextFlowLog.FontSize.NORMAL);
    }
    public void beforeRound(BattlePane bp) {
        TriggerSession.trigger(bp, Trigger.BEFORE_ROUND, this.getStates());
    }
    public void round(BattlePane bp) {
        act(bp);

        TriggerSession.trigger(bp, Trigger.AFTER_ROUND, this.getStates());

        states.removeIf(state -> {
            if (state.getSettleType() == StateSettleType.CHI_XU) {
                state.setDuration(state.getDuration() - 1);
                return state.getDuration() == 0;
            }
            return false;
        });

        maintainedStates.removeIf(state -> {
            state.setDuration(state.getDuration() - 1);
            if (state.getDuration() == 0) {
                state.delete();
                return true;
            }
            return false;
        });

        skills.forEach(Skill::pastRound);
    }
    private void act(BattlePane bp) {
        if (lockSkill != 0) {
            Skill skillLock = getSkill(lockSkill);
            if (skillLock != null && skillLock.tryUse(bp)) {
                return;
            }
        }
        if (!useSkillAuto(bp)) {
            Skill skill1 = getSkill(1);
            if (skill1 != null) {
                skill1.use(bp);
            }
        }
    }
    public Skill getSkill(int skillID) {
        for (Skill skill : skills) {
            if (skill.getSkillID() == skillID) {
                return skill;
            }
        }
        return null;
    }
    public ObservableList<Skill> getSkills() {
        return skills.getObservableList();
    }
    protected boolean useSkillAuto(BattlePane bp) {
        return false;
    }

    public void beHurt(BattlePane bp, Info info) {
        double damage = info.getTraceableNumber().getNumber();
        if (getHp() <= damage) {
            info.getTraceableNumber().addTrace("\t(击杀)");
            beforeDie(bp);
        } else {
            this.hp -= damage;
        }
    }

    /**
     * 失去:区别于受到伤害,失去生命不会暴击,无法被护盾吸收,不触发受到伤害相关效果
     */
    public void lostHP(BattlePane bp, double num) {
        // 如果后来给失去生命默认解除睡眠状态,修改神蛇的堕化
        hp -= num;
        if (hp <= 0) {
            die(bp);
        }
    }

    public void beHeal(BattlePane bp, Info info) {
        setHp(getHp() + info.getTraceableNumber().getNumber());
    }

    /** 恢复：区别于治疗，不受减疗效果影响，不触发治疗相关效果，不会暴击
     *
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

    public CharacterIcon getCharacterIcon(CharacterIcon.OnClickListener onClickListener) {
        if (characterIcon == null) {
            characterIcon = new CharacterIcon(this, onClickListener);
        }
        return characterIcon;
    }
    public void useFrontSkill(BattlePane bp) {}
    public AttackRecorder getAttackRecorder() {
        return (AttackRecorder) getState(AttackRecorder.privateName);
    }
    public State getState(String name) {
        for (State state : states) {
            if (state.name.equals(name)) {
                return state;
            }
        }
        return null;
    }
    public void addState(State newState) {
        if (newState.stateType == StateType.DEBUFF && isIgnoreDebuff()) {
            return;
        }

        for (State state : states) {
            if (state.name.equals(newState.name)) {
                state.cover(newState);
                return;
            }
        }

        states.add(newState);
        if (characterIcon != null) {
            characterIcon.updateState();
        }
    }
    public boolean isHaveState(String name) {
        return getState(name) != null;
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
    public void removeState(String privateName) {
        for (State state : states) {
            if (state.name.equals(privateName)) {
                state.delete();
                return;
            }
        }
        if (characterIcon != null) {
            characterIcon.updateState();
        }
    }
    public Interactive getInteractive(BattlePane bp) {
        if (interactive == null) {
            interactive = new Interactive(bp, this);
        }
        return interactive;
    }
    public void beforeDie(BattlePane bp) {
        for (State state : getStates()) {
            if (state instanceof PreventDie pd && pd.effective()) {
                pd.action();
                return;
            }
        }

        die(bp);
    }
    public void die(BattlePane bp) {
        alive = false;
        bp.removeCharacter(this);
    }
    public boolean controllable() {
        for (State state : getStates()) {
            if (state instanceof CrowdControl) {
                return false;
            }
        }
        return true;
    }
}
