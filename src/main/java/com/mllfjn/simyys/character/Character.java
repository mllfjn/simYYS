package com.mllfjn.simyys.character;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.customnode.StringGroup;
import com.mllfjn.simyys.starter.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.skill.SkillAuto;
import com.mllfjn.simyys.customnode.TextFlowLog;
import com.mllfjn.simyys.determinant.ForbidDecrease;
import com.mllfjn.simyys.determinant.ForbidIncrease;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.starter.propertygetter.PropertyCheck;
import com.mllfjn.simyys.starter.propertygetter.PropertyInput;
import com.mllfjn.simyys.starter.propertygetter.PropertySelectSingle;
import com.mllfjn.simyys.state.AttackRecorder;
import com.mllfjn.simyys.guihuo.MobGuiHuo;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateSettleType;
import com.mllfjn.simyys.trigger.Trigger;
import com.mllfjn.simyys.trigger.TriggerSession;
import javafx.collections.FXCollections;
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
    private double yuHunAttack;
    private double defense;
    private double critRate;
    private double critPower;
    private double effectHitRate;
    private double effectResistRate;
    private double speed;

    private boolean isMob;
    private boolean isYYS;
    private boolean isSummon = false;

    private final List<State> states = new ArrayList<>();
    private final List<State> maintainedStates = new ArrayList<>();
    private transient ObservableList<Skill> skills;
    private transient Interactive interactive;

    protected static final String GENERAL_SPEED_KEY = "general-speed";
    protected static final String GENERAL_BASE_ATTACK_KEY = "general-baseAttack";
    protected static final String GENERAL_YU_HUN_ATTACK_KEY = "general-yuHunAttack";
    public static final String GENERAL_TEAM_KEY = "general-team";
    protected static final String GENERAL_HP_KEY = "general-hp";
    protected static final String GENERAL_DEFENSE_KEY = "general-defense";
    protected static final String GENERAL_CRIT_RATE_KEY = "general-critRate";
    protected static final String GENERAL_CRIT_POWER_KEY = "general-critPower";
    protected static final String GENERAL_EFFECT_HIT_RATE_KEY = "general-effectHitRate";
    protected static final String GENERAL_EFFECT_RESIST_RATE_KEY = "general-effectResistRate";
    protected static final String GENERAL_MOB_KEY = "general-isMob";
    protected static final String GENERAL_YYS_KEY = "general-isYYS";
    protected static final String GENERAL_SUMMON_KEY = "general-isSummon";

    public PropertiesMap getProperties() {
        PropertiesMap map = new PropertiesMap();
        map.put(GENERAL_SPEED_KEY, new PropertyInput("速度"));
        map.put(GENERAL_BASE_ATTACK_KEY, new PropertyInput("基础攻击"));
        map.put(GENERAL_YU_HUN_ATTACK_KEY, new PropertyInput("御魂攻击"));
        map.put(GENERAL_TEAM_KEY, new PropertySelectSingle("队伍", new StringGroup[]{new StringGroup(null, new String[]{"己方", "敌方"})}));
        map.put(GENERAL_HP_KEY, new PropertyInput("生命"));
        map.put(GENERAL_DEFENSE_KEY, new PropertyInput("防御"));
        map.put(GENERAL_CRIT_RATE_KEY, new PropertyInput("暴击率"));
        map.put(GENERAL_CRIT_POWER_KEY, new PropertyInput("暴击伤害"));
        map.put(GENERAL_EFFECT_HIT_RATE_KEY, new PropertyInput("效果命中"));
        map.put(GENERAL_EFFECT_RESIST_RATE_KEY, new PropertyInput("效果抵抗"));

        map.put(GENERAL_MOB_KEY, new PropertyCheck("是否为怪物"));
        map.put(GENERAL_YYS_KEY, new PropertyCheck("是否为阴阳师"));
        map.put(GENERAL_SUMMON_KEY, new PropertyCheck("是否为召唤物"));

        return map;
    }

    public void init(PropertiesMap properties) {
        this.speed = properties.get(GENERAL_SPEED_KEY).getDouble();
        this.baseAttack = properties.get(GENERAL_BASE_ATTACK_KEY).getDouble();
        this.yuHunAttack = properties.get(GENERAL_YU_HUN_ATTACK_KEY).getDouble();
        this.team = properties.get(GENERAL_TEAM_KEY).getInt();
        this.hp = properties.get(GENERAL_HP_KEY).getDouble();
        this.maxHp = hp;
        this.defense = properties.get(GENERAL_DEFENSE_KEY).getDouble();
        this.critRate = properties.get(GENERAL_CRIT_RATE_KEY).getDouble();
        this.critPower = properties.get(GENERAL_CRIT_POWER_KEY).getDouble();
        this.effectHitRate = properties.get(GENERAL_EFFECT_HIT_RATE_KEY).getDouble();
        this.effectResistRate = properties.get(GENERAL_EFFECT_RESIST_RATE_KEY).getDouble();

        if (properties.get(GENERAL_MOB_KEY).getBoolean()) {
            this.isMob = true;
            addState(new MobGuiHuo(this));
        }

        this.isYYS = properties.get(GENERAL_YYS_KEY).getBoolean();
        this.isSummon = properties.get(GENERAL_SUMMON_KEY).getBoolean();

        addState(new AttackRecorder(this));
    }

    public abstract void addSkill(ObservableList<Skill> skills);

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

    public double getSpeed() {
        return speed;
    }

    public double getLocation() {
        return location;
    }

    public void setLocation(double newLocation) {
        this.location = newLocation;
    }

    public void increaseLocation(BattlePane bp, Character from, double increase) {
        for (State state : states) {
            if (state instanceof ForbidIncrease fi && fi.effective(from)) {
                return;
            }
        }
        this.location = Math.min(100, location + increase);
        bp.log.addText("\t" + this.name + "行动提前" + increase + "%\n", TextFlowLog.TextType.INCREASE, TextFlowLog.TextColor.NORMAL, TextFlowLog.FontSize.NORMAL);
    }

    public void decreaseLocation(BattlePane bp, Character from, double decrease) {
        for (State state : states) {
            if (state instanceof ForbidDecrease) {
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

        // 危险：可能涉及状态删除

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

        getSkills().forEach(Skill::pastRound);
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
        /*if (lockSkill != 0) {
            Skill skillLock = getSkill(lockSkill);
            if (skillLock.canUse(bp)) {
                skillLock.use(bp);
            } else {
                if (!useSkillAuto(bp)) {
                    Skill skill1 = getSkill(1);
                    if (skill1 != null) {
                        getSkill(1).use(bp);
                    }
                }
            }
        }*/
    }
    protected boolean useSkillAuto(BattlePane bp) {
        return false;
    }

    public double getAttack() {
        return baseAttack + yuHunAttack;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getMaxHp() {
        return maxHp;
    }

    public double getDefense() {
        return AttributeCounter.getGeneralAttribute(Attribute.DEFENCE, defense, states);
    }

    public double getCritRate() {
        return critRate;
    }

    public double getCritPower() {
        return critPower;
    }

    public double getEffectHitRate() {
        return effectHitRate;
    }

    public double getEffectResistRate() {
        return AttributeCounter.getGeneralAttribute(Attribute.EFFECT_RESIST_RATE, effectResistRate, states);
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

    public void setLockSkill(int i) {
        this.lockSkill = i;
    }
    public int getLockSkill() {
        return this.lockSkill;
    }
    public Double beHurt(BattlePane bp, double damage) {
        if (getHp() <= damage) {
            // 预留免死的位置 比如青女坊，金盾
            /*if (TriggerSession.preventDie(this.getStates())) {
                return null;
            }*/
            beForeDie(bp);
        } else {
            this.hp -= damage;
        }
        return damage;
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

    public void addMaintainedState(State state) {
        maintainedStates.add(state);
    }

    public void removeMaintainedState(State state) {
        maintainedStates.remove(state);
    }

    public List<State> getStates() {
        return states;
    }
    public ObservableList<Skill> getSkills() {
        if (skills == null) {
            skills = FXCollections.observableArrayList(SkillAuto.INSTANCE);
            addSkill(skills);
        }
        return skills;
    }

    public Skill getSkill(int i) {
        for (Skill skill : getSkills()) {
            if (skill.getSkillID() == i) {
                return skill;
            }
        }
        return null;
    }

    public void deleteState(String privateName) {
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

    public Interactive getHit(BattlePane bp) {
        if (interactive == null) {
            interactive = new Interactive(bp, this);
        }
        return interactive;
    }
    public void beForeDie(BattlePane bp) {
        alive = false;
        bp.removeCharacter(this);
    }
}
