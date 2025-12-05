package com.mllfjn.simyys.character.list.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.Runnable;
import com.mllfjn.simyys.character.status.determinant.PreventDie;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

public class StatusHuiMie extends Status implements Displayable, Runnable, AttributeModifier, PreventDie {
    private static final String text = "毁灭";
    // 毁灭等级
    private int stack = 1;
    // 那美二技能等级
    private final int level;
    // 免死是否可以触发
    private boolean canTrigger = true;
    // 剩余可以触发免死的次数
    private int times = 3;
    // 那美攻击时无视护甲的状态,需要在毁灭移除时一起移除
    private final StatusNaMeiFlag NaMeiFlag;

    public StatusHuiMie(NaMei from, Character belongTo, int level, boolean awakening) {
        super(from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
        this.level = level;

        // 我方场上至多存在1个此效果
        from.getStatus(StatusNaMeiFlag.class).ifPresent(status -> status.huiMie.delete());

        NaMeiFlag = new StatusNaMeiFlag(from, this, awakening);
        from.addStatus(NaMeiFlag);
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        // 毁灭6
        sb.append(text).append(stack);
        // 免死生效
        if (effective()) {
            sb.append(Displayable.DELIMITER).append("金盾");
        }

        return sb.toString();
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEFORE_ROUND && stack < 6// 毁灭等级可以在回合开始前上升5次
                || trigger == Trigger.BEFORE_ROUND && !canTrigger && times > 0;// 毁灭免死还可以触发
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (stack < 6) {
            stack++;
        }

        if (times > 0) {
            canTrigger = true;
        }

        return false;
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.DEFENCE || attribute == Attribute.IGNORE_DEFENCE;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        if (attribute == Attribute.DEFENCE) {
            return (1 - stack) * 100; // 降低防御(stack - 1) * 100，这里算的是+防御，所以负的
        }

        // 造成伤害时无视100点防御
        // lv4-友方每因毁灭降低100点防御,额外无视40点防御,至多额外无视200点防御
        if (attribute == Attribute.IGNORE_DEFENCE) {
            return 100 + level >= 4 ? (stack - 1) * 40 : 0;
        }

        return 0;
    }

    @Override
    public void beforeDelete() {
        if (NaMeiFlag != null) {
            NaMeiFlag.delete();
        }
    }

    @Override
    public boolean effective() {
        // 只有那美自身在场时可以触发
        return canTrigger && from.alive;
    }

    @Override
    public void preventDie() {
        canTrigger = false;
        times--;
    }

    @Override
    public String getName() {
        return text;
    }
}

class StatusNaMeiFlag extends Status implements AttributeModifier {
    public final StatusHuiMie huiMie;
    private final boolean awakening;

    public StatusNaMeiFlag(NaMei naMei, StatusHuiMie huiMie, boolean awakening) {
        super(naMei, naMei, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.huiMie = huiMie;
        this.awakening = awakening;
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        // 觉醒-伊邪那美攻击时,也可触发友方目标当前毁灭的防御无视效果
        return awakening && attribute == Attribute.IGNORE_DEFENCE;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return huiMie.getInfluence(Attribute.IGNORE_DEFENCE);
    }
}