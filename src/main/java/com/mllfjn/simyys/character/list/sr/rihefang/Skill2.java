package com.mllfjn.simyys.character.list.sr.rihefang;

import com.mllfjn.simyys.battleevent.*;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.ParamHealInfo;
import com.mllfjn.simyys.interactive.HealInfo;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "晴雨";

    private StatusAdder<?> statusAdder;
    private final BattleActionListener listener;

    // 晴天娃娃重生回合
    private int reviveRound;
    // 标志在任意式神行动结束后随机复活单位
    private boolean doReviveAfterAction;
    // 存储的日光能量
    private double stored;
    private Skill3 skill3;

    public Skill2(Character belongTo) {
        super(belongTo, 1, 2);
        listener = new BattleActionListener(belongTo) {
            @Override
            public boolean onBattleAction(BattleEvent event) {
                if (event instanceof EventCharacterDie) {
                    doReviveAfterAction = true;
                } else if (doReviveAfterAction && event instanceof EventActionDone) {
                    Character character = RateController.choose("晴天娃娃复活单位",
                            belongTo.bp.situation.getDeadCharacters(belongTo.team),
                            c -> c.name, belongTo.bp.calc
                    );
                    character.revive(belongTo.bp, character.getMaxHp());
                    double heal = stored * skill3.getAverageHeal();
                    stored = 0;
                    belongTo.doInteractive(interactive -> {
                        List<Character> list = new CharacterFinder(belongTo)
                                .filterTeammate()
                                .filterSummon(false)
                                .getList();
                        interactive.heal(Skill2.this, list, c ->
                                new HealInfo(belongTo, Skill2.this, c, heal)
                        );
                    });
                    doReviveAfterAction = false;
                    reviveRound = skill3.getReviveRound();
                    disable();
                }
                return false;
            }
        };
    }

    void setSkill3(Skill3 skill3) {
        this.skill3 = skill3;
    }

    void addStore(double store) {
        stored = Math.min(getBelongTo().getMaxHp(), stored + store);
    }

    void heal() {
        Character character = new CharacterFinder(getBelongTo())
                .filterTeammate()
                .filterSummon(false)
                .get(Attribute.HP_PERCENT, CharacterFinder.Criteria.MIN);
        double loseHp = character.getMaxHp() - character.getHp();
        if (loseHp > 0) {
            double useStore = Math.min(stored, loseHp * 0.3);
            stored -= useStore;
            getBelongTo().doInteractive(interactive ->
                    interactive.heal(this, List.of(character),
                            c -> new HealInfo(getBelongTo(), this, c, useStore)
                    )
            );
        }
    }

    void useSkill3() {
        if (reviveRound == 0) {
            addStore(skill3.getUseStore());
        } else {
            reduceReviveRound();
        }
    }

    @Override
    public void pastRound() {
        super.pastRound();
        reduceReviveRound();
    }

    void reduceReviveRound() {
        if (reviveRound > 0) {
            reviveRound--;
            if (reviveRound == 0) {
                if (getBelongTo().getSealPassiveSkillCount() == 0) {
                    enable();
                }
            }
        }
    }

    @Override
    public void enable() {
        if (reviveRound > 0) {
            return;
        }
        Character belongTo = getBelongTo();
        statusAdder = belongTo.bp.addStatusAdder(c ->
                c.isSummon()
                        ? null
                        : c.team == belongTo.team
                        ? new StatusToTeammate(belongTo, c)
                          : new StatusHealListener(belongTo, c)
        );
        belongTo.bp.addActionListener(listener);
    }

    @Override
    public void disable() {
        if (statusAdder != null) {
            statusAdder.deleteAndRemove();
        }
        getBelongTo().bp.removeActionListener(listener);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    private class StatusHealListener extends Status {
        public StatusHealListener(Character from, Character belongTo) {
            super(SkillName + "治疗监听", from, belongTo);
            runOn(Trigger.AFTER_HEAL, param ->
                    Skill2.this.addStore(((ParamHealInfo) param).healInfo.getTraceableNumber().getNumber() * 0.2)
            );
            runOn(Trigger.AFTER_ROUND, _ -> Skill2.this.heal());
        }
    }

    private class StatusToTeammate extends Status {
        public StatusToTeammate(Character from, Character belongTo) {
            super(SkillName + "受到伤害监听", from, belongTo);
            runOn(Trigger.BEFORE_ATTACK, param ->
                    Skill2.this.addStore(((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().getNumber() * 0.25)
            );
        }
    }
}
