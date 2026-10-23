package com.mllfjn.simyys.character.list.sp.guitun;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.instance.StatusSleep;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    public Skill3(Character belongTo, int level) {
        super(belongTo, level, "天火怒焱", 3, 0, 3);
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        int level = getLevel();
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        double decrease = level >= 4 ? 40 : 30;
        int dispelBuffAmount = level >= 2 ? 2 : 1;

        List<Character> enemy = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();
        for (Character c : enemy) {
            interactive.decreaseLocation(c, decrease);
            c.dispelBuff(dispelBuffAmount);
        }

        return Optional.empty();
    }

    private static class StatusGuiWangZiTai extends Status {
        private StatusGWZTZhuoShao currentStatus;

        protected StatusGuiWangZiTai(Character character) {
            super("鬼王姿态", character, character, StatusType.GENERAL, StatusForm.YIN_JI);
            StatusAdder<?> adder = character.bp().addStatusAdder(c -> c.team == character.team
                    ? new StatusGWZTZhuoShao(character, c, this)
                    : null
            );

            BattleActionListener listener = new BattleActionListener(character) {
                @Override
                public boolean onBattleAction(BattleEvent event) {
                    if (event instanceof Event)
                    return false;
                }
            };

            character.bp().addActionListener(listener);

            beforeDelete(() -> {
                adder.deleteAndRemove();
                character.bp().removeActionListener(listener);
            });
            duration(2);
            displayNameAndDuration();
        }

        private void onBeforeRound(StatusGWZTZhuoShao currentStatus) {
            this.currentStatus = currentStatus;
        }

        private static class StatusGWZTZhuoShao extends Status {

            protected StatusGWZTZhuoShao(Character from, Character belongTo, StatusGuiWangZiTai status) {
                super("鬼王姿态-灼烧", from, belongTo);
                runOn(Trigger.BEFORE_ROUND, _ -> {
                    belongTo.lostHP(belongTo.getHp() * 0.24);
                    StatusSleep.removeSleep(belongTo);
                    enableAction(Trigger.BEING_ATTACKED);
                });
                runOnAndDisable(Trigger.BEING_ATTACKED, param ->
                        ((ParamAttackInfo) param).getAttackInfo().setCancel(true));
            }
        }
    }
}
