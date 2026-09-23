package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.huangkulou;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.List;

class StatusDWCount extends Status {

    private int count = 0;

    private boolean setOff = false;

    private double lastCheckSpeed;
    private double lastCheckDefense;
    private double lastCheckAttack;

    public StatusDWCount(Character character, Skill2 skill2) {
        super("毒雾计数器", character);
        check();

        belongTo.bp.addActionListener(new BattleActionListener(belongTo) {
            @Override
            public boolean onBattleAction(BattleEvent event) {
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
            }
        });
        attribute(Attribute.CRIT_RATE, _ -> 5.0 * count);
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
}
