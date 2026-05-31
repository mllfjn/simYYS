package com.mllfjn.simyys.character.list.r.shouwu;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "冥火";

    private boolean isActive = false;
    private StatusStealCritRate status;

    public Skill2(Character belongTo) {
        super(belongTo, 1, 2);
    }

    void madeAttack(Character target) {
        if (isActive) {
            if (status == null) {
                status = new StatusStealCritRate(getBelongTo(), target);
            } else {
                status.steal(target);
            }
        }
    }

    @Override
    public void enable() {
        isActive = true;
    }

    @Override
    public void disable() {
        isActive = false;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusStealCritRate extends Status implements AttributeModifier {
        private StatusBeStolen statusBeStolen;

        public StatusStealCritRate(Character character, Character target) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            setDurationType(StatusDurationType.CHI_XU, character.isInRound() ? 2 : 1);

            statusBeStolen = new StatusStealCritRate.StatusBeStolen(character, target);
        }

        void steal(Character target) {
            if (statusBeStolen.belongTo != target) {
                statusBeStolen.delete();
                statusBeStolen = new StatusBeStolen(belongTo, target);
                target.addStatus(statusBeStolen);
            }
            setDuration(belongTo.isInRound() ? 2 : 1);
        }

        @Override
        public void beforeDelete() {
            if (statusBeStolen != null) {
                statusBeStolen.delete();
            }
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.CRIT_RATE;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 40;
        }

        static class StatusBeStolen extends Status implements AttributeModifier, Displayable {
            public StatusBeStolen(Character from, Character belongTo) {
                super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            }

            @Override
            public boolean isAffectAttribute(Attribute attribute) {
                return attribute == Attribute.CRIT_RATE;
            }

            @Override
            public double getInfluence(Attribute attribute, StatusModifyParam param) {
                return -40;
            }

            @Override
            public String getDisplayText() {
                return "暴击率降低";
            }
        }
    }
}
