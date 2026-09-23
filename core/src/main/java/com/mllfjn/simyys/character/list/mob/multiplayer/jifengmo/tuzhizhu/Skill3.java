package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.ratecontroller.RateController;

class Skill3 extends PassiveSkill {
    private static final String SkillName = "天罗地网";

    private boolean use;

    public Skill3(Character belongTo, Skill4 skill4) {
        super(belongTo, -1, 3);

        Status.of(SkillName + "监听器", belongTo)
                .runOn(Trigger.AFTER_ATTACK, _ -> {
                    if (!use) {
                        if (RateController.otherWhether(SkillName, "使用", belongTo.bp().calc, 35)) {
                            use = true;
                            belongTo.bp.addActionListener(new BattleActionListener(belongTo) {
                                @Override
                                public boolean onBattleAction(BattleEvent event) {
                                    if (event instanceof EventActionDone) {
                                        skill4.useWithoutCost();
                                        use = false;
                                        return true;
                                    }
                                    return false;
                                }
                            });
                        }
                    }

                }).addTo();
    }

    @Override
    public String getSkillDesc() {
        return "√\t受到一定(?)伤害时,有35%概率立即释放一次天罗地网";
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
