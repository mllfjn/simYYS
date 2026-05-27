package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.StatusSupplier;

public class StatusConfusion extends Status implements CrowdControl, Displayable {
    private static final String StatusName = "混乱";

    public StatusConfusion(Character from, Character belongTo, int duration) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        setDurationType(StatusDurationType.CHI_XU, duration);
    }

    public static StatusSupplier getSupplier(int duration) {
        return new StatusSupplier(StatusName, StatusConfusion.class, (from, to) ->
                to.getStatus(StatusConfusion.class).ifPresentOrElse(
                        status -> {
                            if (status.getDuration() < duration) {
                                status.setDuration(duration);
                            }
                        },
                        () -> to.addStatus(new StatusConfusion(from, to, duration))
                )
        );
    }

    public void doConfusion() {
        // 被混乱的敌方在行动时,强制使用普攻攻击场上随机目标
        Character target = new CharacterFinder(belongTo)
                .filterSelf()
                .getRandom();

        belongTo.getPuGong().ifPresent(
                skill1 -> {
                    skill1.usePrivate(belongTo.getInteractive(), target);
                    skill1.useOver(target);
                });
    }

    @Override
    public String getDisplayText() {
        return StatusName + getDuration();
    }
}
