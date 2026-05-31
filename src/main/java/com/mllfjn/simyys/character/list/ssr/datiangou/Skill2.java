package com.mllfjn.simyys.character.list.ssr.datiangou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusBiHu;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.ratecontroller.RateController;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "钢铁之羽";
    private static final int[] maxStack = new int[]{0, 10, 30, 50, 80, 80};

    private Skill3 skill3;

    private StatusGTZYListener status;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
    }

    public StatusGTZYListener getStatus() {
        if (status == null) {
            status = new StatusGTZYListener(getBelongTo(), maxStack[getLevel()], getLevel() >= 5);
        }
        return status;
    }

    void addStack(int addStack) {
        StatusGTZYListener s = getStatus();
        if (s.statusXZYF.stack < s.maxStack) {
            s.statusXZYF.stack = Math.min(s.statusXZYF.stack + addStack, s.maxStack);
        }
    }

    void setSkill3(Skill3 skill3) {
        this.skill3 = skill3;
    }

    @Override
    public void enable() {
        getBelongTo().addStatus(getStatus());
    }

    @Override
    protected void disable() {
        getBelongTo().removeStatus(status);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    class StatusGTZYListener extends Status implements StatusRunnable {
        private final int maxStack;
        private final StatusXZYF statusXZYF;
        private final StatusGTZYBiHu statusGTZYBiHu;

        public StatusGTZYListener(Character character, int maxStack, boolean useSkill3AfterBiHuUsed) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            statusXZYF = new StatusXZYF(character);
            character.addStatus(statusXZYF);

            statusGTZYBiHu = new StatusGTZYBiHu(character, useSkill3AfterBiHuUsed);
            character.addStatus(statusGTZYBiHu);

            this.maxStack = maxStack;
        }

        @Override
        public void beforeDelete() {
            Skill2.this.status = null;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return (trigger == Trigger.CAUSE_ATTACK && statusXZYF.stack < maxStack)
                    || trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (trigger == Trigger.CAUSE_ATTACK) {
                if (RateController.otherWhether(SkillName, "获得" + StatusXZYF.StatusName, bp.calc, 50)) {
                    statusXZYF.stack++;
                }
            } else {
                statusGTZYBiHu.isActive = true;
            }
            return false;
        }

        static class StatusXZYF extends Status implements Displayable, AttributeModifier {
            private static final String StatusName = "雄姿英发";

            private int stack;

            public StatusXZYF(Character character) {
                super(character, character, StatusType.GENERAL, StatusForm.YIN_JI);
            }

            @Override
            public boolean isAffectAttribute(Attribute attribute) {
                return attribute == Attribute.ZENG_SHANG;
            }

            @Override
            public double getInfluence(Attribute attribute, StatusModifyParam param) {
                return stack;
            }

            @Override
            public String getDisplayText() {
                if (stack == 0) {
                    return null;
                }
                return StatusName + stack;
            }
        }

        class StatusGTZYBiHu extends StatusBiHu {
            private boolean isActive;
            private final boolean useSkill3AfterBiHuUsed;

            public StatusGTZYBiHu(Character character, boolean useSkill3AfterBiHuUsed) {
                super(character, character);
                this.useSkill3AfterBiHuUsed = useSkill3AfterBiHuUsed;
            }

            @Override
            public boolean runnable(Trigger trigger) {
                if (!isActive) {
                    return false;
                } else {
                    return super.runnable(trigger);
                }
            }

            @Override
            protected void used() {
                isActive = false;
                if (useSkill3AfterBiHuUsed) {
                    belongTo.bp.addOutRoundSkill(() -> Skill2.this.skill3.useWithoutCost());
                }
            }

            @Override
            public String getDisplayText() {
                if (isActive) {
                    return super.getDisplayText();
                } else {
                    return null;
                }
            }
        }
    }
}
