package com.mllfjn.simyys.character.list.ssr.dashe;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "魂魄碎裂";
    private static final int[] multiplier = {0, 100, 105, 110, 115, 115};

    private final StatusIgnoreDefense status;

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
        if (level >= 5) {
            status = new StatusIgnoreDefense(belongTo);
        } else {
            status = null;
        }
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        DaShe belongTo = (DaShe) getBelongTo();
        if (status != null) {
            status.add(target);
        }
        interactive.attackTypical(this, target, multiplier[getLevel()], AttackType.DAN_TI);
        if (status != null) {
            belongTo.removeStatus(status);
        }
        // 不太确定在移除状态前还是后
        belongTo.addMo(target);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    private static class StatusIgnoreDefense extends Status {
        private double ignoreDefense;

        public StatusIgnoreDefense(Character character) {
            super(SkillName, character);
            attribute(Attribute.IGNORE_DEFENCE, _ -> ignoreDefense);
        }

        void add(Character target) {
            ignoreDefense = target.getDefence() * 0.2;
            addTo();
        }
    }
}
