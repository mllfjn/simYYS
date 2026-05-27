package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.huangkulou;

import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.AttributeModifier;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.List;

class StatusDWCount extends Status implements AttributeModifier {

    private int count = 0;

    private boolean setOff = false;

    private double lastCheckSpeed;
    private double lastCheckDefense;
    private double lastCheckAttack;

    public StatusDWCount(Character character, Skill2 skill2) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        check();

        belongTo.bp.addActionListener(belongTo, event -> {
            if (event instanceof EventActionDone) {
                check();
                if (setOff) {
                    belongTo.doInteractive(interactive -> {
                        List<Character> list = new CharacterFinder(belongTo)
                                .filterEnemy()
                                .getList();
                        interactive.attack(skill2, list,
                                c -> AttackInfo
                                        .createRealAttack(belongTo, skill2, c, count * 3000)
                        );
                    });
                    setOff = false;
                }
            }
            return false;
        });
    }

    void setOff() {
        setOff = true;
    }

    void check() {
        double speed = belongTo.getSpeed();
        if (speed != lastCheckSpeed) {
            if (speed < lastCheckSpeed) {
                count++;
            }
            lastCheckSpeed = speed;
        }
        double defense = belongTo.getDefence();
        if (defense != lastCheckDefense) {
            if (defense < lastCheckDefense) {
                count++;
            }
            lastCheckDefense = defense;
        }
        double attack = belongTo.getAttack();
        if (attack != lastCheckAttack) {
            if (attack < lastCheckAttack) {
                count++;
            }
            lastCheckAttack = attack;
        }
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.CRIT_RATE;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        return count * 5;
    }
}
