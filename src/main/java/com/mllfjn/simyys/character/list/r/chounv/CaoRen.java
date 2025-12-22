package com.mllfjn.simyys.character.list.r.chounv;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.InteractiveInfo;
import com.mllfjn.simyys.interactive.AttackType;

public class CaoRen extends Character {
    public static final String CharacterName = "诅咒草人";

    public CaoRen(Character chouNv, Character bind, int level) {
        this.isSummon = true;
        this.name = CharacterName;
        this.bp = chouNv.bp;
        this.team = bind.team;
        this.setInitSpeed(bind.getSpeed());

        double hpPercent = switch (level) {
            case 1 -> 0.1;
            case 2 -> 0.15;
            case 3 -> 0.2;
            case 4 -> 0.25;
            case 5 -> 0.3;
            default -> 0;
        };
        this.setInitDefense(bind.getDefence() * 0.5);
        this.setMaxHp(bind.getHp() * hpPercent, true);

        this.addStatus(new StatusAfterAttack(chouNv, this, bind));
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "";
    }

    @Override
    protected void addOwnSkills() {

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
                double number = pa.interactiveInfo.getTraceableNumber().getNumber();
                from.doInteractive(interactive ->
                        interactive.attack(InteractiveInfo
                                        .createChuanDaoAttack(from, skill, bind, (c1, c2) -> number)
                                , AttackType.CHUAN_DAO));
            }
            return false;
        }

        private static final Skill skill = Skill.getInstance(CharacterName);

        @Override
        public String getText() {
            return "剩余回合" + getDuration();
        }

        @Override
        public void beforeDelete() {
            belongTo.bp.removeCharacter(belongTo);
        }
    }
}
