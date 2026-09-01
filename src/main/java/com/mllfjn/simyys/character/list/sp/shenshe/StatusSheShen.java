package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.character.status.determinant.IgnoreChangeMaxHp;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;
import com.mllfjn.simyys.battleevent.EventActionDone;

// 无法改变生命上限,免疫减益和 TODO 放逐
public class StatusSheShen extends Status implements IgnoreChangeMaxHp, IgnoreDebuff {
    private static final String StatusName = "蛇神";
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
        super(StatusName, character);

        originalHp = character.getHp();
        originalMaxHp = character.getMaxHp();

        this.attack = attack;
        this.level = skillLevel;

        // 生命为神堕八岐大蛇攻击200%
        character.setMaxHp(character.getAttack() * (skillLevel == 1 ? 2 : 3.8), true);

        displayName();

        if (level >= 3) {
            runOn(Trigger.BEING_ATTACKED, triggerParam -> {
                AttackInfo attackInfo = ((ParamAttackInfo) triggerParam).getAttackInfo();
                if (attackInfo.getAttackType() == AttackType.QUN_TI) {
                    attackInfo.getTraceableNumber().mul(0.7, StatusName);
                }
            });
        }

        preventDie(_ -> {
            if (!die) {
                belongTo.bp.addActionListener(new BattleActionListener(belongTo) {
                    @Override
                    public boolean onBattleAction(BattleEvent event) {
                        if (event instanceof EventActionDone) {
                            backToNormal();
                            // lv4-蛇神被击败时,自身提升100点速度,持续1个回合
                            if (level >= 4) {
                                Status.of("神蛇速度", belongTo)
                                        .attribute(Attribute.SPEED, 100.0)
                                        .duration(StatusDurationType.CHI_XU, 1)
                                        .addTo();
                            }
                            return true;
                        }
                        return false;
                    }
                });
                die = true;
            }
        });
    }

    public double getAttack() {
        return attack;
    }

    public void backToNormal() {
        // 当蛇神通过审判仪式破除天羽羽斩镇压或受到致命伤害时,本体重新回到场上
        delete();
        belongTo.setMaxHp(originalMaxHp, false);
        belongTo.setHp(originalHp);
    }
}


