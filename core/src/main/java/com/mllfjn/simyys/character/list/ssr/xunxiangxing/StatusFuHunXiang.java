package com.mllfjn.simyys.character.list.ssr.xunxiangxing;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.StatusSupplier;

class StatusFuHunXiang extends Status {
    static final String StatusName = "缚魂香";

    private final double percent;
    private final Skill2 skill2;
    private int defenseStack = 1;
    private int damageStack = 1;

    private StatusFuHunXiang(Character from, Character belongTo) {
        super(StatusName, from, belongTo);
        type(StatusType.DEBUFF, StatusForm.YIN_JI);
        XunXiangXing xxx = (XunXiangXing) from;
        skill2 = xxx.getSkill2();
        percent = (xxx).getPercent();

        display(() -> {
            StringBuilder sb = new StringBuilder("减防");
            sb.append(defenseStack);
            if (damageStack > 0) {
                sb.append(Status.DELIMITER).append(StatusName).append(damageStack);
            }
            return sb.toString();
        });
        attribute(Attribute.DEFENCE, _ -> -defenseStack * percent * belongTo.getInitDefense());
    }

    static StatusSupplier getSupplier() {
        return new StatusSupplier(StatusName, StatusFuHunXiang.class, StatusFuHunXiang::addStack);
    }

    static void addStack(Character from, Character belongTo) {
        belongTo.getStatus(StatusFuHunXiang.class)
                .ifPresentOrElse(
                        StatusFuHunXiang::addStack,
                        () -> belongTo.addStatus(new StatusFuHunXiang(from, belongTo))
                );
    }

    private void addStack() {
        if (defenseStack < 5) {
            defenseStack++;
        }

        if (damageStack == 2) {
            damageStack = 0;
            StatusShiShen.install(from, belongTo);
            AttackInfo attackInfo = AttackInfo.createJianJieAttack(from, skill2, belongTo, from.getAttack());
            attackInfo.setMultiplier(231);
            from.bp.interactive.attack(attackInfo);
        } else {
            damageStack++;
        }
    }
}
