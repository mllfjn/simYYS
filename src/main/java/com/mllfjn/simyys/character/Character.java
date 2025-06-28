package com.mllfjn.simyys.character;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.determinant.ForbidDecrease;
import com.mllfjn.simyys.determinant.ForbidIncrease;
import com.mllfjn.simyys.starter.info.CharacterInfo;
import com.mllfjn.simyys.state.AttackRecorder;
import com.mllfjn.simyys.guihuo.MobGuiHuo;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateSettleType;
import com.mllfjn.simyys.trigger.Trigger;
import com.mllfjn.simyys.trigger.TriggerSession;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.Serializable;
import java.util.*;

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
    private double yuHunAttack;
    private double defense;
    private double critRate;
    private double critPower;
    private double effectHitRate;
    private double effectResistRate;
    private double speed;
    private CharacterType type = CharacterType.SHI_SHEN;

    private final List<State> states = new ArrayList<>();
    private final List<State> maintainedStates = new ArrayList<>();
//    private final Map<Integer, Skill> skills = new HashMap<>();
    private final ObservableMap<Integer, Skill> skills = new SimpleMapProperty<>();
    public transient CharacterIcon characterIcon;

    public void init(CharacterInfo characterInfo, int[] skillLevels) {
        this.name = characterInfo.name();
        this.speed = Double.parseDouble(characterInfo.speed());
        this.baseAttack = Double.parseDouble(characterInfo.baseAttack());
        this.yuHunAttack = Double.parseDouble(characterInfo.yuHunAttack());
        this.team = Integer.parseInt(characterInfo.team());
        this.hp = Double.parseDouble(characterInfo.hp());
        this.maxHp = Double.parseDouble(characterInfo.hp());
        this.defense = Double.parseDouble(characterInfo.defense());
        this.critRate = Double.parseDouble(characterInfo.critRate());
        this.critPower = Double.parseDouble(characterInfo.critPower());
        this.effectHitRate = Double.parseDouble(characterInfo.effectHitRate());
        this.effectResistRate = Double.parseDouble(characterInfo.effectResistRate());

        initSelf(skillLevels);

        addState(new AttackRecorder(this));


        if (team < 0) {
            team = -team;
            type = CharacterType.MOB;
            addState(new MobGuiHuo(this));
        }
    }

    public abstract void initSelf(int[] skillLevels);

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

    public void increaseLocation(Character comeFrom, double increase) {
        for (State state : states) {
            if (state instanceof ForbidIncrease fi && fi.effective(comeFrom)) {
                return;
            }
        }
        this.location = Math.min(100, location + increase);
    }

    public void decreaseLocation(Character comeFrom, double decrease) {
        for (State state : states) {
            if (state instanceof ForbidDecrease) {
                return;
            }
        }
        this.location = Math.max(0, location - decrease);
    }

    public void beforeRound(BattlePane bp) {
        TriggerSession.trigger(bp, Trigger.BEFORE_ROUND, this.getStates());
    }
    public void round(BattlePane bp) {
        act(bp);

        TriggerSession.trigger(bp, Trigger.AFTER_ROUND, this.getStates());

        // 危险：可能涉及状态删除
        /*for (int i = states.size() - 1; i >= 0; i--) {
            states.get(i).pastRound();
        }*/

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


    }

    private void act(BattlePane bp) {
        // 锁定技能不为0时,检测是否可用,可用时使用,不可用时普攻
        // 锁定技能为0时,根据技能顺序使用技能,无可用技能时普攻

        if (lockSkill != 0 ) {
            if (skills.get(lockSkill) != null && skills.get(lockSkill).canUse(bp)) {
                useSkill(bp, lockSkill);
                return;
            }
        } else {
            int[] order = getUseSkillOrder();
            for (int i : order) {
                if (skills.get(i).canUse(bp)) {
                    useSkill(bp, i);
                    return;
                }
            }
        }

        useSkill(bp, 1);
    }

    private void useSkill(BattlePane bp, int i) {
        if (skills.get(i) != null) {
            skills.get(i).use(bp);
        }
    }

    public abstract int[] getUseSkillOrder();

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
        return type == CharacterType.MOB;
    }

    public boolean isSummon() {
        return type == CharacterType.SUMMON;
    }

    public void setLockSkill(int i) {
        this.lockSkill = i;
    }
    public int getLockSkill() {
        return this.lockSkill;
    }
    public void beHurt(BattlePane bp, double damage) {
        this.hp -= damage;
        if (this.hp <= 0) {
            alive = false;
            bp.characterDie(this);
        }
    }

    public void setCharacterIcon(CharacterIcon characterIcon) {
        this.characterIcon = characterIcon;
    }

    public ObservableValue<? extends ObservableList<String>> getSkillListProperty() {
        return new SimpleListProperty<>(FXCollections.observableArrayList("妖术"));
    }

    public void useFrontSkill() {}

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
    public Map<Integer, Skill> getSkills() {
        return skills;
    }

    public void deleteState(String privateName) {
        for (State state : states) {
            if (state.name.equals(privateName)) {
                state.delete();
                return;
            }
        }
    }

    public void setType(CharacterType type) {
        this.type = type;
    }
    public void beForeDie() {

    }
}
