package com.mllfjn.simyys.character.list.sr.xuenv;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusShield;
import com.mllfjn.simyys.interactive.InteractiveInfo;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "冰甲术";

    private final StatusBJSListener status;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
        status = new StatusBJSListener(belongTo, level);
    }

    @Override
    public void enable() {
        getBelongTo().addStatus(status);
    }

    @Override
    public void disable() {
        getBelongTo().removeStatus(status);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    class StatusBJSListener extends Status {
        private static final int[] ShieldPercent = new int[]{0, 6, 9, 9, 12, 12};

        private final boolean reduceSpeed;
        private final int reduceSpeedRate;
        private final int shieldPercent;

        public StatusBJSListener(Character character, int level) {
            super(SkillName + "回合结束监听", character);
            reduceSpeed = level >= 3;
            reduceSpeedRate = level >= 5 ? 50 : 25;
            shieldPercent = ShieldPercent[level];
            runOn(Trigger.AFTER_ROUND, _ -> {
                belongTo.addStatus(new StatusBJShield(belongTo, belongTo));
                Character c = new CharacterFinder(belongTo)
                        .filterTeammate()
                        .filterSelf()
                        .get(Attribute.CRIT_POWER, CharacterFinder.Criteria.MAX);

                if (c != null) {
                    c.addStatus(new StatusBJShield(belongTo, c));
                }
            });
        }

        class StatusBJShield extends StatusShield {

            public StatusBJShield(Character from, Character belongTo) {
                super(from, belongTo, from.getMaxHp() * shieldPercent / 100);
            }

            @Override
            public boolean handle(InteractiveInfo interactiveInfo) {
                if (reduceSpeed) {
                    from.doInteractive(interactive ->
                            interactive.effect(Skill2.this, interactiveInfo.getAttacker(),
                                    reduceSpeedRate, true, StatusReduceSpeed.getSupplier()
                            )
                    );
                }
                return super.handle(interactiveInfo);
            }
        }
    }
}
