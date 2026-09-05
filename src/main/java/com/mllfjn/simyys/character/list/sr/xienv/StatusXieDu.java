package com.mllfjn.simyys.character.list.sr.xienv;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import javafx.scene.paint.Color;

import java.util.List;

class StatusXieDu extends Status {
    private static final String StatusName = "蝎毒";
    public static final Skill SKILL = Skill.getInstance(StatusName);

    private final int multiplier;
    private final Skill2 skill2;

    private int stack = 1;
    private double receivedDamage = 0;

    public StatusXieDu(Character from, Character belongTo, int multiplier,
                       boolean setOffAfterRound, Skill2 skill2) {
        super(StatusName, from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
        this.multiplier = multiplier;
        this.skill2 = skill2;
        display(() -> StatusName + stack);
        setColor(Color.PURPLE);
        attribute(Attribute.DEFENCE, _ -> -80.0 * stack);
        if (setOffAfterRound) {
            runOn(Trigger.AFTER_ROUND_FIRST, _ -> setOff());
        }
    }

    public void setOff() {
        AttackInfo info = AttackInfo.createJianJieAttack(from, SKILL, belongTo, from.getAttack());
        info.setMultiplier(multiplier);
        if (!from.alive) {
            from.reset(belongTo.bp);
        }
        from.doInteractive(interactive -> interactive.attack(info));
        if (stack < 5) {
            stack++;
            if (stack == 5 && skill2.canCount()) {
                runOn(Trigger.AFTER_ATTACK, param -> {
                    if (skill2.isActive()) {
                        AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                        if (attackInfo.getAttackType() == AttackType.JIAN_JIE) {
                            if (receivedDamage == 0) {
                                from.bp.addActionListener(new BattleActionListener(from) {
                                    @Override
                                    public boolean onBattleAction(BattleEvent event) {
                                        if (event instanceof EventActionDone) {
                                            jianShe();
                                            return true;
                                        }
                                        return false;
                                    }
                                });
                            }
                            receivedDamage += attackInfo.getTraceableNumber().getNumber();
                        }
                    }
                });
            }
        }
    }

    public int getStack() {
        return stack;
    }

    private void jianShe() {
        List<Character> list = new CharacterFinder(from)
                .filterEnemy()
                .getList();
        list.remove(belongTo);
        double average = receivedDamage / list.size() * 0.6;
        from.doInteractive(interactive -> {
            double limit = from.getAttack() * 8;
            interactive.attack(SKILL, list, (target) -> {
                AttackInfo attackInfo = AttackInfo.createJianJieAttack(from, SKILL, target, average);
                attackInfo.setLimit(limit);
                return attackInfo;
            });
        });
        receivedDamage = 0;
    }
}
