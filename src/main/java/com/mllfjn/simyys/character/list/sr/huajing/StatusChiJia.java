package com.mllfjn.simyys.character.list.sr.huajing;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.List;

class StatusChiJia extends Status {
    public StatusChiJia(HuaJing from, Character belongTo, Skill2 skill2) {
        super(Skill2.SkillName, from, belongTo);
        enableStack(3);
        setStack(3);
        displayNameAndStack();
        beforeDelete(() -> from.statusChiJia = null);
        runOn(Trigger.AFTER_ACTION, _ -> {
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
            reduceStack();
            skill2.useDone();

        });
        runOn(Trigger.AFTER_ROUND, _ ->
                from.doInteractive(interactive -> interactive.increaseLocation(belongTo, 20))
        );
    }

    @Override
    public void reduceStack() {
        super.reduceStack();
        if (stack == 0) {
            delete();
        }
    }
}
