package com.mllfjn.simyys.character.list.sp.yinfan;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusShield;

public class StatusYuanLi extends Status {
    public static final String StatusName = "愿力";

    private final int skill2Level;
    private final int skill3Level;

    private int stack;

    public StatusYuanLi(Character character, int skill2Level, int skill3Level) {
        super(StatusName, character);
        type(StatusType.GENERAL, StatusForm.YIN_JI);
        beforeDelete(() -> belongTo.bp.getGuiHuoInstance(belongTo.team).setYuanLi(null));
        display(() -> StatusName + stack);
        this.skill2Level = skill2Level;
        this.skill3Level = skill3Level;
        character.bp.getGuiHuoInstance(character.team).setYuanLi(this);
    }

    public static void addYuanLi(Character character, int num, int skill2Level, int skill3Level) {
        StatusYuanLi status = character.getStatus(StatusYuanLi.class).orElseGet(() -> {
            StatusYuanLi newStatus = new StatusYuanLi(character, skill2Level, skill3Level);
            character.addStatus(newStatus);
            return newStatus;
        });

        status.stack = Math.min(8, status.stack + num);
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

    static class StatusYuanLiCritPower extends Status {
        private int stack = 0;

        public StatusYuanLiCritPower(Character character) {
            super("因幡自身加爆伤", character);
            type(StatusType.BUFF, StatusForm.YIN_JI);
            attribute(Attribute.CRIT_POWER, _ -> 5.0 * stack);
        }

        public static void addStack(Character character, int num) {
            StatusYuanLiCritPower status = character.getStatus(StatusYuanLiCritPower.class).orElseGet(() -> {
                StatusYuanLiCritPower newStatus = new StatusYuanLiCritPower(character);
                character.addStatus(newStatus);
                return newStatus;
            });

            status.stack = Math.min(24, status.stack + num);
        }
    }

    static class StatusYuanLiShield extends StatusShield {

        public StatusYuanLiShield(Character character, double shield) {
            super(character, character, shield);
            duration(StatusDurationType.CHI_XU, 2);
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
