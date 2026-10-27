package com.mllfjn.simyys.character.list.ssr.kongquemingwang;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.Trigger;

import java.util.List;

// 天机工程
class StatusKongQueLing extends Status {
    private boolean speedUp;
    private boolean isCounting;

    private boolean kaiPing;
    private final boolean removeDebuff;
    private final boolean immune;
    private final boolean dispel;
    private final boolean increaseAttack;

    StatusKongQueLing(Character belongTo,
                                boolean removeDebuff, boolean immune, boolean dispel, boolean increaseAttack
    ) {
        super("孔雀翎", belongTo, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
        this.removeDebuff = removeDebuff;
        this.immune = immune;
        this.dispel = dispel;
        this.increaseAttack = increaseAttack;

        enableStackAndAddStack(5);
        displayNameAndNonZeroStack();
        attribute(Attribute.EFFECT_HIT_RATE, _ -> 15.0 * getStack());
        retainAfterDie();
        retainAfterChangeWave(() -> setStack(1));
        runOn(Trigger.AFTER_ROUND, _ -> addStack());
        runOn(Trigger.BEFORE_ROUND, _ -> {
            if (stack == maxStack) {
                setStack(0);
                kaiPing();
            }
        });

        attribute(Attribute.SPEED, _ -> {
            if (!speedUp || isCounting) {
                return 0.0;
            }
            isCounting = true;
            double speed = belongTo.getSpeed();
            isCounting = false;
            return speed * 0.5;
        });
        addTo();
    }

    @Override
    public void addStack() {
        if (kaiPing) {
            return;
        }

        if (dispel && stack < maxStack) {
            List<Character> list = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .getList();
            for (Character character : list) {
                character.dispelDeBuff(1);
            }
        }
        super.addStack();
        if (stack == maxStack) {
            speedUp = true;
        }
    }

    @Override
    public void setStack(int stack) {
        super.setStack(stack);
        speedUp = false;
    }

    void kaiPing() {
        kaiPing = true;
        int duration = 3;
        if (removeDebuff) {
            if (belongTo.isUnderCrowdControl()) {
                duration = 1;
            }
            belongTo.removeAllDeBuff();
        }
        new StatusKaiPing(belongTo, this, duration, immune, increaseAttack);
        ((KongQueMingWang) belongTo).kaiPing();
    }

    void stopKaiPing() {
        kaiPing = false;

        // 因为停止开屏使用beforeDelete写的,转周目时会触发
        if (stack != 1) {
            addStack();
        }

        ((KongQueMingWang) belongTo).stopKaiPing();
    }
}
