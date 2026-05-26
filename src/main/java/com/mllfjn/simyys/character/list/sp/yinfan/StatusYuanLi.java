package com.mllfjn.simyys.character.list.sp.yinfan;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

public class StatusYuanLi extends Status implements Displayable {
    public static final String StatusName = "愿力";

    private final int skill2Level;
    private final int skill3Level;

    private int stack;

    public StatusYuanLi(Character character, int skill2Level, int skill3Level) {
        super(character, character, StatusType.GENERAL, StatusForm.YIN_JI);
        this.skill2Level = skill2Level;
        this.skill3Level = skill3Level;
        character.bp.setYuanLi(character.team, this);
    }

    public static void addYuanLi(com.mllfjn.simyys.character.Character character, int num, int skill2Level, int skill3Level) {
        StatusYuanLi status = character.getStatus(StatusYuanLi.class).orElseGet(() -> {
            StatusYuanLi newStatus = new StatusYuanLi(character, skill2Level, skill3Level);
            character.addStatus(newStatus);
            return newStatus;
        });

        status.stack = Math.min(8, status.stack + num);
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }

    public int getGuiHuo(int num, boolean isFromYuHun) {
        if (skill2Level >= 2 && isFromYuHun && stack < 8) {
            int spaceLeft = 8 - stack;
            if (num <= spaceLeft) {
                stack += num;
                return 0;
            } else {
                stack = 8;
                return num - spaceLeft;
            }
        } else {
            return num;
        }
    }

    public int maxUse() {
        return stack;
    }

    public void use(int num) {
        stack -= num;
        if (skill3Level >= 2) {
            StatusYuanLiCritPower.addStack(belongTo, num);
            if (skill3Level >= 3) {
                StatusYuanLiShield.addShield(belongTo);
            }
        }
    }

    static class StatusYuanLiCritPower extends Status implements AttributeModifier {
        private int stack = 0;

        public StatusYuanLiCritPower(com.mllfjn.simyys.character.Character character) {
            super(character, character, StatusType.BUFF, StatusForm.YIN_JI);
        }

        public static void addStack(com.mllfjn.simyys.character.Character character, int num) {
            StatusYuanLiCritPower status = character.getStatus(StatusYuanLiCritPower.class).orElseGet(() -> {
                StatusYuanLiCritPower newStatus = new StatusYuanLiCritPower(character);
                character.addStatus(newStatus);
                return newStatus;
            });

            status.stack = Math.min(24, status.stack + num);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.CRIT_POWER;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return stack * 5;
        }
    }

    static class StatusYuanLiShield extends StatusShield {

        public StatusYuanLiShield(com.mllfjn.simyys.character.Character character, double shield) {
            super(character, character, shield);
            setDurationType(StatusDurationType.CHI_XU, 2);
        }

        public static void addShield(Character character) {
            int count = 0;
            for (Status status : character.getStatuses()) {
                if (status instanceof StatusYuanLiShield) {
                    count++;
                    if (count == 8) {
                        return;
                    }
                }
            }

            character.addStatus(new StatusYuanLiShield(character, character.getMaxHp() * 0.12));
        }
    }
}
