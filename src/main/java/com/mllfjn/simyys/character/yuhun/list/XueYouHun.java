package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusFrozen;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunAfterCauseAttack;
import com.mllfjn.simyys.character.yuhun.YuHunHitFeedBack;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.Interactive;

public class XueYouHun extends YuHun implements YuHunAfterCauseAttack, YuHunHitFeedBack {
    public static final String YuHunName = "雪幽魂";
    private static final Skill skill = Skill.getInstance(YuHunName);

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void action(AttackInfo attackInfo, Interactive interactive) {
        Character target = attackInfo.getTarget();
        interactive.effect(skill, target, isHaveReduceSpeed(target) ? 30 : 15, 0, true,
                StatusFrozen.getSupplier(1)
        );
    }

    public static boolean isHaveReduceSpeed(Character character) {
        for (Status status : character.getStatuses()) {
            if (status instanceof AttributeModifier am && am.getInfluence(Attribute.SPEED, null) < 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void hitFeedBack(AttackInfo info) {
        StatusReduceSpeed.install(character, info.getAttacker());
    }

    static class StatusReduceSpeed extends Status implements Displayable, AttributeModifier {
        private StatusReduceSpeed(Character from, Character belongTo) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 1);
        }

        static void install(Character from, Character belongTo) {
            belongTo.getStatus(StatusReduceSpeed.class)
                    .ifPresentOrElse(
                            status -> status.setDuration(1),
                            () -> belongTo.addStatus(new StatusReduceSpeed(from, belongTo))
                    );
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return -30;
        }

        @Override
        public String getDisplayText() {
            return YuHunName + "减速";
        }
    }
}
