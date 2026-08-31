package com.mllfjn.simyys.character.list.sp.sphongye;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.ParamLocationChange;
import com.mllfjn.simyys.character.status.triggerParam.ParamStatus;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "经霜";

    private final Status statusAttackListener;
    private final double damageIncreasement;

    private boolean canAddStackWhenTeammateBeingAttack = true;
    private BattleActionListener listener;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
        statusAttackListener = Status.of(SkillName + "攻击监听", belongTo);
        statusAttackListener.runOn(Trigger.CAUSE_ATTACK, param ->
                StatusYeJin.addStack(belongTo, ((ParamAttackInfo) param).getAttackInfo().getTarget(), Skill2.this)
        );
        damageIncreasement = level >= 2 ? 0.1 : 0.05;

        if (level >= 3) {
            belongTo.bp.addStatusAdder(c ->
                    c.team == belongTo.team
                            ? new StatusAddingDebuffListener(belongTo, c)
                            : level >= 4 ? new StatusLocationChangeListener(belongTo, c) : null
            );
            listener = new BattleActionListener(belongTo) {
                @Override
                public boolean onBattleAction(BattleEvent event) {
                    if (event instanceof EventActionDone) {
                        canAddStackWhenTeammateBeingAttack = true;
                        return true;
                    } else {
                        return false;
                    }
                }
            };
        }
    }

    private boolean canAddStackWhenTeammateBeingAttack() {
        return canAddStackWhenTeammateBeingAttack;
    }

    private void addStackWhenTeammateBeingAttack() {
        canAddStackWhenTeammateBeingAttack = false;
        getBelongTo().bp.addActionListener(listener);
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t攻击敌方目标时附加1层叶烬
                √\tlv2-每层叶烬提升受到的间接伤害增至10%
                √\tlv3-友方目标被附加减益状态时为来源施加1层叶烬,每回合内最多触发1次
                √\tlv4-敌方行动条增加达到30%时,为其附加1层叶烬
                \tlv5-每层叶烬额外降低10%攻击与防御,心狩鬼女红叶被攻击时每层额外降低20%暴击
                """;
    }

    double getDamageIncreasement() {
        return damageIncreasement;
    }

    @Override
    public void enable() {
        getBelongTo().addStatus(statusAttackListener);
    }

    @Override
    public void disable() {
        getBelongTo().removeStatus(statusAttackListener);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    class StatusAddingDebuffListener extends Status {
        public StatusAddingDebuffListener(Character from, Character belongTo) {
            super(SkillName + "减益状态监听", from, belongTo);
            runOn(Trigger.ADDING_DEBUFF, param -> {
                if (Skill2.this.canAddStackWhenTeammateBeingAttack()) {
                    StatusYeJin.addStack(from, ((ParamStatus) param).getStatus().from, Skill2.this);
                    Skill2.this.addStackWhenTeammateBeingAttack();
                }
            });
        }
    }

    class StatusLocationChangeListener extends Status {
        public StatusLocationChangeListener(Character from, Character belongTo) {
            super(SkillName + "拉条监听", from, belongTo);
            runOn(Trigger.LOCATION_WILL_CHANGE, param -> {
                ParamLocationChange plc = (ParamLocationChange) param;
                if (plc.isFromIncrease && (plc.newLocation - plc.oldLocation) > 30) {
                    StatusYeJin.addStack(from, belongTo, Skill2.this);
                }
            });
        }
    }
}
