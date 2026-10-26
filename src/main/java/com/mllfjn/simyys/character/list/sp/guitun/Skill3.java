package com.mllfjn.simyys.character.list.sp.guitun;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionStart;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusSleep;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private final Skill1 skill1;

    public Skill3(Character belongTo, int level, Skill1 skill1) {
        super(belongTo, level, "天火怒焱", 3, 0, 3);
        this.skill1 = skill1;
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp) && !getBelongTo().isHaveStatus(StatusGuiWangZiTai.class);
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        int level = getLevel();
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive(this);
        // 推条
        double decrease = level >= 4 ? 40 : 30;
        // 驱散增益数量
        int dispelBuffAmount = level >= 2 ? 2 : 1;

        List<Character> enemy = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();
        for (Character c : enemy) {
            interactive.decreaseLocation(c, decrease);
            c.dispelBuff(dispelBuffAmount);
        }

        // 进入鬼王姿态
        new StatusGuiWangZiTai(belongTo, level >= 3, level >= 5);

        return Optional.empty();
    }

    private class StatusGuiWangZiTai extends Status {
        private StatusGWZTZhuoShao currentStatus;

        private double damage;

        protected StatusGuiWangZiTai(Character character, boolean recovery, boolean removeCrowedControl) {
            super("鬼王姿态", character, character, StatusType.GENERAL, StatusForm.YIN_JI);

            character.removeSkill(skill1);
            Skill1Special skill1Special = new Skill1Special(character, skill1.getLevel());
            character.addSkill(skill1Special, true);

            duration(StatusDurationType.CHI_XU, 2);
            displayNameAndDuration();
            addTo();

            if (recovery) {
                runOn(Trigger.CAUSE_ATTACK, param ->
                        damage += ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().getNumber());
            }

            StatusAdder<?> adder = character.bp().addStatusAdder(c -> c.team == character.team
                    ? new StatusGWZTZhuoShao(character, c, this, removeCrowedControl)
                    : null
            );
            BattleActionListener listener = new BattleActionListener(character) {
                @Override
                public boolean onBattleAction(BattleEvent event) {
                    // 免疫伤害直至下一回合开始
                    if (currentStatus != null && event instanceof EventActionStart) {
                        currentStatus.disableAction(Trigger.BEING_ATTACKED);
                        currentStatus = null;
                    }
                    return false;
                }
            };
            character.bp().addActionListener(listener);
            beforeDelete(() -> {
                adder.deleteAndRemove();
                character.bp().removeActionListener(listener);
                character.removeSkill(skill1Special);
                character.addSkill(skill1, true);
                if (recovery && damage > 0) {
                    belongTo.doInteractive(Skill3.this, interactive -> {
                        List<Character> teammate = new CharacterFinder(belongTo())
                                .filterTeammate()
                                .getList();
                        double r = damage * 0.2;
                        for (Character c : teammate) {
                            interactive.recovery(c, r);
                        }
                    });
                }
            });
        }

        private void setCurrentStatus(StatusGWZTZhuoShao currentStatus) {
            this.currentStatus = currentStatus;
            currentStatus.enableAction(Trigger.BEING_ATTACKED);
        }

        private static class StatusGWZTZhuoShao extends Status {
            protected StatusGWZTZhuoShao(Character from, Character belongTo,
                                         StatusGuiWangZiTai status, boolean removeCrowedControl
            ) {
                super("鬼王姿态-灼烧", from, belongTo);
                // 回合开始前效果
                runOn(Trigger.BEFORE_ROUND, _ -> {
                    belongTo.lostHP(belongTo.getHp() * 0.24);
                    StatusSleep.removeSleep(belongTo);
                    status.setCurrentStatus(this);

                    if (removeCrowedControl && belongTo.getHpPercent() < 0.3) {
                        belongTo.removeAllCrowControl();
                    }
                });
                // 免疫伤害,默认不生效
                runOnAndDisable(Trigger.BEING_ATTACKED, param ->
                        ((ParamAttackInfo) param).getAttackInfo().setCancel(true));
            }
        }
    }
}
