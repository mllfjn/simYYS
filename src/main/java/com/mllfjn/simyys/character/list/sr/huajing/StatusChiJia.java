package com.mllfjn.simyys.character.list.sr.huajing;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.List;

class StatusChiJia extends Status implements StatusRunnable, Displayable {

    private final Skill2 skill2;
    private int stack = 3;

    public StatusChiJia(Character from, Character belongTo, Skill2 skill2) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.skill2 = skill2;
    }

    @Override
    public void beforeDelete() {
        ((HuaJing) from).statusChiJia = null;
    }

    @Override
    public String getDisplayText() {
        return Skill2.SkillName + stack;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ACTION || trigger == Trigger.AFTER_ROUND;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (trigger == Trigger.AFTER_ACTION) {
            List<Character> list = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .getList();
            belongTo.doInteractive(interactive -> {
                for (Character character : list) {
                    character.sealPassiveSkill();
                }
                double attack = belongTo.getAttack();
                interactive.attack(skill2, list, c -> {
                    AttackInfo attackInfo = AttackInfo.createRealAttack(belongTo, skill2, c,
                            Math.min(attack, c.getMaxHp() * skill2.getCoefficient())
                    );
                    attackInfo.setNotCalYuHun();
                    return attackInfo;
                });
                for (Character character : list) {
                    character.unsealPassiveSkill();
                }
            });
            stack--;
            skill2.useDone();
            if (stack == 1) {
                return true;
            } else {
                stack--;
            }
        } else {
            from.doInteractive(interactive -> interactive.increaseLocation(belongTo, 20));
        }

        return false;
    }
}
