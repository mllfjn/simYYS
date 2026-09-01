package com.mllfjn.simyys.character.list.ssr.datiangou;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusBiHu;
import com.mllfjn.simyys.ratecontroller.RateController;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "钢铁之羽";
    private static final int[] maxStack = new int[]{0, 10, 30, 50, 80, 80};

    private Skill3 skill3;

    private StatusGTZYListener status;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
        belongTo.addStatus(new StatusGTZYBiHu(belongTo, level >= 5));
    }

    public StatusGTZYListener getStatus() {
        if (status == null) {
            status = new StatusGTZYListener(getBelongTo(), maxStack[getLevel()]);
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
    public void disable() {
        getBelongTo().removeStatus(status);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    class StatusGTZYListener extends Status {
        private final int maxStack;
        private final StatusXZYF statusXZYF;

        public StatusGTZYListener(Character character, int maxStack) {
            super(SkillName + "监听", character);
            statusXZYF = new StatusXZYF(character);
            character.addStatus(statusXZYF);

            this.maxStack = maxStack;
            beforeDelete(() -> Skill2.this.status = null);

            runOn(Trigger.CAUSE_ATTACK, _ -> {
                if (RateController.otherWhether(SkillName, "获得" + StatusXZYF.StatusName, belongTo().bp().calc, 50)) {
                    statusXZYF.stack++;
                    if (statusXZYF.stack == maxStack) {
                        removeAction(Trigger.CAUSE_ATTACK);
                    }
                }
            });
        }

        static class StatusXZYF extends Status {
            private static final String StatusName = "雄姿英发";

            private int stack;

            public StatusXZYF(Character character) {
                super(StatusName, character, character, StatusType.GENERAL, StatusForm.YIN_JI);
                attribute(Attribute.ZENG_SHANG, _ -> (double) stack);
                display(() -> {
                    if (stack == 0) {
                        return null;
                    }
                    return StatusName + stack;
                });
            }
        }
    }

    class StatusGTZYBiHu extends StatusBiHu {
        private boolean active;
        private final boolean useSkill3AfterBiHuUsed;

        public StatusGTZYBiHu(Character character, boolean useSkill3AfterBiHuUsed) {
            super(character, character);
            this.useSkill3AfterBiHuUsed = useSkill3AfterBiHuUsed;
            runOnAndDisable(Trigger.AFTER_ROUND, _ -> {
                active = true;
                disableAction(Trigger.AFTER_ROUND);
            });
            display(() -> {
                if (active) {
                    return super.getDisplayText();
                } else {
                    return null;
                }
            });
        }

        @Override
        public boolean runnable(Trigger trigger) {
            if (!active) {
                return false;
            } else {
                return super.runnable(trigger);
            }
        }

        @Override
        protected void used() {
            active = false;
            enableAction(Trigger.AFTER_ROUND);
            if (useSkill3AfterBiHuUsed) {
                belongTo.bp.addOutRoundSkill(skill3, () -> skill3.useWithoutCost());
            }
        }
    }
}
