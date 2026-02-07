package com.mllfjn.simyys.character.list.r.chounv;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

public class CaoRen extends CharacterSummonBase {
    private static final String CharacterName = "诅咒草人";

    private static final double[] hpPercent = new double[]{0, 0.1, 0.15, 0.2, 0.25, 0.3};

    private final Character bind;

    public CaoRen(Character chouNv, Character bind, int level) {
        super(chouNv.bp, CharacterName, bind.team);
        this.bind = bind;
        this.isSummon = true;

        this.setInitSpeed(bind.getSpeed());

        this.setInitDefense(bind.getDefence() * 0.5);
        this.forceSetMaxHp(bind.getHp() * hpPercent[level], true);

        this.addStatus(new StatusAfterAttack(chouNv, this, bind));
    }

    public Character getBind() {
        return bind;
    }

    @Override
    public boolean isUncontrollable() {
        return true;
    }

    static class StatusAfterAttack extends Status implements StatusRunnable, Displayable {
        private final Character bind;

        // from是丑女 belongTo是草人 bind是连接的目标
        public StatusAfterAttack(Character from, Character belongTo, Character bind) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.bind = bind;

            setDurationType(StatusDurationType.CHI_XU, 3);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (param instanceof ParamAfterAttack pa) {
                double number = pa.attackInfo.getTraceableNumber().getNumber();
                from.doInteractive(interactive -> {
                            interactive.attack(
                                    AttackInfo.createChuanDaoAttack(from, skill, bind,
                                            (c1, c2) -> number)
                            );
                            skill.useDone();
                        }
                );
            }
            return false;
        }

        private static final Skill skill = Skill.getInstance(CharacterName);

        @Override
        public String getDisplayText() {
            return "剩余回合" + getDuration();
        }

        @Override
        public void beforeDelete() {
            belongTo.die();
        }
    }
}
