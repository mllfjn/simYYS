package com.mllfjn.simyys.character.list.sr.qingji;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "淬毒";

    public Skill2(Character belongTo) {
        super(belongTo, 1, 2);
    }

    void madePoisoning(Character target) {
        if (isActive()) {
            StatusQingJiReduceDefense.addStack(getBelongTo(), target);
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusQingJiReduceDefense extends Status {
        private int stack = 1;

        private StatusQingJiReduceDefense(Character from, Character belongTo) {
            super(SkillName + "减防", from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            attribute(Attribute.DEFENCE, _ -> -20.0 * stack);
        }

        static void addStack(Character from, Character belongTo) {
            belongTo.getStatus(StatusQingJiReduceDefense.class)
                    .ifPresentOrElse(
                            status -> {
                                if (status.stack < 15) {
                                    status.stack++;
                                }
                            },
                            () -> belongTo.addStatus(new StatusQingJiReduceDefense(from, belongTo))
                    );
        }
    }
}
