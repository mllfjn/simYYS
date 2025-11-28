package com.mllfjn.simyys.character.sp.laotou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;
import com.mllfjn.simyys.trigger.Trigger;

import java.util.List;

class Skill2 {
    public static final String SkillName = "洪福降临";


    public Skill2(LaoTou laoTou, int level) {
        laoTou.addState(new StateAfterRound(laoTou, level));
        // lv5-战斗开始后,自身首次受到伤害时开始打盹 TODO
        if (level >= 5) {

        }
    }

    static class StateAfterRound extends State implements Runnable {
        private final int level;

        public StateAfterRound(LaoTou laoTou, int level) {
            super(laoTou, laoTou, StateType.SPECIAL, StateForm.SPECIAL);
            this.level = level;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp) {
            // 自身回合结束时，击退全体敌方目标10%行动条
            // lv2-击退行动条效果提升至15%
            belongTo.doInteractive(interactive -> {
                List<Character> enemy = CharacterFinder.findEnemy(belongTo, belongTo.bp.situation.characters);
                for (Character character : enemy) {
                    interactive.decreaseLocation(character, level >= 2 ? 15 : 10);
                }
            });

            // 若回合中释放过委以重任,则开始打盹
            if (belongTo.isHaveState(StateUse3Flag.class)) {
                belongTo.addState(new StateDaDun((LaoTou) belongTo));
                // lv4-打盹额外获得1点鬼火
                if (level >= 4) {
                    belongTo.bp.gainGuiHuo(belongTo, 1);
                }
            }

            // lv3-回合结束时额外获得2点鬼火
            if (level >= 3) {
                belongTo.bp.gainGuiHuo(belongTo, 2);
            }

            return false;
        }
    }
}


