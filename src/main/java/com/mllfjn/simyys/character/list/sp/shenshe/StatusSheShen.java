package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.character.status.determinant.IgnoreChangeMaxHp;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;
import com.mllfjn.simyys.character.status.determinant.PreventDie;
import com.mllfjn.simyys.battleevent.EventActionDone;

// 无法改变生命上限,免疫减益和 TODO 放逐
public class StatusSheShen extends Status implements IgnoreChangeMaxHp, IgnoreDebuff, PreventDie, StatusRunnable, Displayable {
    private static final String text = "蛇神";
    // 变身前的血量和上限
    private final double originalMaxHp;
    private final double originalHp;
    // 吸取的攻击力
    private final double attack;
    // 技能等级
    private final int level;
    // 受到致命伤害时不会立即返回蛇神,而是在下一次行动结束时
    private boolean die = false;

    public StatusSheShen(Character character, int skillLevel, double attack) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);

        originalHp = character.getHp();
        originalMaxHp = character.getMaxHp();

        this.attack = attack;
        this.level = skillLevel;

        // 生命为神堕八岐大蛇攻击200%
        character.setMaxHp(character.getAttack() * (skillLevel == 1 ? 2 : 3.8), true);
    }

    public double getAttack() {
        return attack;
    }

    @Override
    public void preventDie(double excessDamage) {
        if (!die) {
            belongTo.bp.addActionListener(new BattleActionListener(belongTo) {
                @Override
                public boolean onBattleAction(BattleEvent event) {
                    if (event instanceof EventActionDone) {
                        backToNormal();
                        // lv4-蛇神被击败时,自身提升100点速度,持续1个回合
                        if (level >= 4) {
                            belongTo.addStatus(new StatusSheShenSpeed(belongTo));
                        }
                        return true;
                    }
                    return false;
                }
            });
            die = true;
        }
    }

    @Override
    public String getName() {
        return text;
    }

    public void backToNormal() {
        // 当蛇神通过审判仪式破除天羽羽斩镇压或受到致命伤害时,本体重新回到场上
        delete();
        belongTo.setMaxHp(originalMaxHp, false);
        belongTo.setHp(originalHp);
    }

    @Override
    public String getDisplayText() {
        return text;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return level >= 3 && trigger == Trigger.BEING_ATTACKED;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
        if (attackInfo.getAttackType() == AttackType.QUN_TI) {
            attackInfo.getTraceableNumber().mul(0.7, text);
        }

        return false;
    }

    static class StatusSheShenSpeed extends Status implements AttributeModifier {
        public StatusSheShenSpeed(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.setDurationType(StatusDurationType.CHI_XU, 1);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 100;
        }
    }
}


