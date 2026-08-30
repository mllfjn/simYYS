package com.mllfjn.simyys.character.status;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.utils.serializable.*;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class Status implements Serializable {
    // 状态名称
    public final String name;
    // 状态来源
    public final Character from;
    // 状态持有者
    public final Character belongTo;
    // 状态类型 增减益
    public StatusType statusType = StatusType.SPECIAL;
    // 状态形式 状态/印记
    public StatusForm statusForm = StatusForm.SPECIAL;

    protected Status(String name, Character from, Character belongTo) {
        this.name = name;
        this.from = from;
        this.belongTo = belongTo;
    }

    protected Status(String name, Character character) {
        this.name = name;
        this.from = character;
        this.belongTo = character;
    }

    public static Status of(String name, Character character) {
        return new Status(name, character);
    }

    public static Status of(String name, Character from, Character belongTo) {
        return new Status(name, from, belongTo);
    }

    public final Character belongTo() {
        return belongTo;
    }

    public final Character from() {
        return from;
    }

    public final Status type(StatusType statusType, StatusForm statusForm) {
        this.statusType = statusType;
        this.statusForm = statusForm;
        return this;
    }

    public final String getName() {
        return name;
    }

    public void addTo() {
        belongTo.addStatus(this);
    }

    // ====================可运行===============================
    private Map<Trigger, SerialConsumer<TriggerParam>> actions = Collections.emptyMap();

    public final boolean runnable(Trigger trigger) {
        return actions.containsKey(trigger);
    }

    public final Status runOn(Trigger trigger, SerialConsumer<TriggerParam> action) {
        if (actions == Collections.EMPTY_MAP) {
            actions = new EnumMap<>(Trigger.class);
        }
        actions.put(trigger, action);
        return this;
    }

    public final void removeAction(Trigger trigger) {
        actions.remove(trigger);
    }

    public final void run(Trigger trigger, TriggerParam param) {
        actions.get(trigger).accept(param);
    }

    // ========================影响属性============================
    private Map<Attribute, SerialFunction<StatusModifyParam, Double>> attributeModifier = Collections.emptyMap();

    public final Status attribute(Attribute attribute, SerialFunction<StatusModifyParam, Double> getter) {
        if (attributeModifier == Collections.EMPTY_MAP) {
            attributeModifier = new EnumMap<>(Attribute.class);
        }
        attributeModifier.put(attribute, getter);
        return this;
    }

    public double getAttribute(Attribute attribute, StatusModifyParam param) {
        SerialFunction<StatusModifyParam, Double> function = attributeModifier.get(attribute);
        if (function == null) {
            return 0;
        } else {
            return function.apply(param);
        }
    }

    public record StatusModifyParam(Character target, AttackType attackType) {
    }

    // ====================显示在状态栏===========================
    public static final String DELIMITER = " ";

    private SerialSupplier<String> displayTextSupplier;
    private transient Color displayColor;
    private boolean displayNameAndDuration;
    private boolean displayName;

    private Double colorR;
    private Double colorG;
    private Double colorB;

    public final Status display(SerialSupplier<String> textSupplier) {
        displayTextSupplier = textSupplier;
        if (textSupplier != null) {
            if (statusType == StatusType.DEBUFF) {
                setColor(Color.RED);
            } else {
                setColor(Color.BLACK);
            }
        } else {
            setColor(null);
        }

        return this;
    }

    public final Status display(SerialSupplier<String> textSupplier, Color color) {
        displayTextSupplier = textSupplier;
        setColor(color);
        return this;
    }

    public final Status displayName() {
        displayName = true;
        return this;
    }

    public final Status displayNameAndDuration() {
        displayNameAndDuration = true;
        return this;
    }

    public boolean isDisplayText() {
        return displayTextSupplier != null;
    }

    public final String getDisplayText() {
        if (displayNameAndDuration) {
            return name + getDuration();
        } else if (displayName) {
            return name;
        } else {
            return displayTextSupplier.get();
        }
    }

    public Color getDisplayColor() {
        if (displayColor == null) {
            displayColor = Color.color(colorR, colorG, colorB);
        }
        return displayColor;
    }

    private void setColor(Color color) {
        displayColor = color;
        if (color != null) {
            colorR = color.getRed();
            colorG = color.getGreen();
            colorB = color.getBlue();
        } else {
            colorR = null;
        }
    }

    // ========================改变鬼火消耗===============================
    private int forceChangeSkillCost;

    public final Status forceChangeSkillCost(int changeSkillCost) {
        this.forceChangeSkillCost = changeSkillCost;
        return this;
    }

    public int getForceChangeSkillCost() {
        return forceChangeSkillCost;
    }

    // =========================状态留存===============================
    private StatusDurationType durationType = StatusDurationType.NONE;
    private int duration = 0;

    public final Status duration(StatusDurationType durationType, int duration) {
        this.durationType = durationType;
        this.duration = duration;

        if (durationType == StatusDurationType.WEI_CHI) {
            from.addMaintainedStatus(this);
        } else if (durationType == StatusDurationType.CHI_XU && belongTo.isInRound()) {
            this.duration++;
        }
        return this;
    }

    public final void duration(int num) {
        if (durationType == StatusDurationType.NONE) {
            throw new RuntimeException("需要先设置持续方式");
        }
        duration = num;
        if (durationType == StatusDurationType.CHI_XU && belongTo.isInRound()) {
            this.duration++;
        }
    }

    public StatusDurationType getDurationType() {
        return durationType;
    }

    public int getDuration() {
        return duration;
    }

    private boolean retainAfterDie = false;
    private boolean retainAfterChangeWave = false;
    private SerialRunnable changeAction;

    public final Status retainAfterDie() {
        retainAfterDie = true;
        return this;
    }

    public boolean isRetainAfterDie() {
        return retainAfterDie;
    }

    public final Status retainAfterChangeWave() {
        retainAfterChangeWave = true;
        return this;
    }

    public final Status retainAfterChangeWave(SerialRunnable changeAction) {
        retainAfterChangeWave = true;
        this.changeAction = changeAction;
        return this;
    }

    public final void changeWave() {
        if (!retainAfterChangeWave) {
            delete();
        } else if (changeAction != null) {
            changeAction.run();
        }
    }


    private SerialRunnable beforeDelete;

    public final Status beforeDelete(SerialRunnable beforeDelete) {
        this.beforeDelete = beforeDelete;
        return this;
    }

    public final void delete() {
        if (beforeDelete != null) {
            beforeDelete.run();
        }
        belongTo.getStatuses().remove(this);
    }

    // =============================免死===================================

    private SerialSupplier<Boolean> preventDieEffective;
    private SerialConsumer<Double> preventDieAction;

    public final Status preventDie() {
        preventDieEffective = SerialSupplier.ALWAYS_TRUE;
        return this;
    }

    public final Status preventDie(SerialConsumer<Double> preventDieAction) {
        this.preventDieAction = preventDieAction;
        return preventDie();
    }

    public final void preventDie(SerialSupplier<Boolean> preventDieEffective, SerialConsumer<Double> preventDieAction) {
        this.preventDieEffective = preventDieEffective;
        this.preventDieAction = preventDieAction;
    }

    public final void removePreventDie() {
        preventDieEffective = SerialSupplier.ALWAYS_FALSE;
    }

    public final void doPreventDie(double excessDamage) {
        if (preventDieAction != null) {
            preventDieAction.accept(excessDamage);
        }
    }

    public final boolean isPreventDie() {
        return preventDieEffective.get();
    }

    @Override
    public String toString() {
        return name;
    }
}
