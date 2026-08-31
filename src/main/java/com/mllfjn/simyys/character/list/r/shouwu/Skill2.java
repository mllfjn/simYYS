package com.mllfjn.simyys.character.list.r.shouwu;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "冥火";

    private StatusStealCritRate status;

    public Skill2(Character belongTo) {
        super(belongTo, 1, 2);
    }

    void madeAttack(Character target) {
        if (isActive()) {
            if (status == null) {
                status = new StatusStealCritRate(getBelongTo(), target);
            } else {
                status.steal(target);
            }
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusStealCritRate extends Status {
        private StatusBeStolen statusBeStolen;

        public StatusStealCritRate(Character character, Character target) {
            super(SkillName, character);
            duration(StatusDurationType.CHI_XU, 1);
            beforeDelete(() -> {
                if (statusBeStolen != null) {
                    statusBeStolen.delete();
                }
            });
            attribute(Attribute.CRIT_RATE, 40.0);
            statusBeStolen = new StatusStealCritRate.StatusBeStolen(character, target);
        }

        void steal(Character target) {
            if (statusBeStolen.belongTo != target) {
                statusBeStolen.delete();
                statusBeStolen = new StatusBeStolen(belongTo, target);
                target.addStatus(statusBeStolen);
            }
            duration(1);
        }

        static class StatusBeStolen extends Status {
            public StatusBeStolen(Character from, Character belongTo) {
                super("暴击率降低", from, belongTo);
                type(StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
                displayName();
                attribute(Attribute.CRIT_RATE, -40.0);
            }
        }
    }
}
